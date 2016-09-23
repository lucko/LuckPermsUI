package nl.makertim.luckpermsui.util;

import java.sql.*;
import java.util.function.Consumer;

public abstract class AbstractDatabaseManager implements IDatabaseManager, Runnable {

	protected Connection connection;
	private int delay;
	private int port;
	private final String type;
	private final String username;
	private final String password;
	private final String database;
	private final String url;

	/**
	 * This is a wrapper for databases
	 *
	 * @param type
	 *            the type of the server, like 'mysql'
	 * @param url
	 *            the url/ip of the server
	 * @param username
	 *            the username to login with
	 * @param password
	 *            the password to login with
	 * @param database
	 *            the name of the database
	 */
	public AbstractDatabaseManager(String type, String url, int port, String username, String password, String database) {
		this.port = port;
		this.type = type;
		this.database = database;
		this.username = username;
		this.password = password;
		this.url = url;
		new Thread(this).start();
	}

	public String getRawSQLVersion() {
		try {
			return connection.getMetaData().getDriverVersion();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return "-1";
	}

	public boolean openConnection() {
		boolean didOpen = false;
		try {
			connection = DriverManager.getConnection(String.format("jdbc:%s://%s:%d/%s", type, url, port, database),
				username, password);
			didOpen = true;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return didOpen;
	}

	public boolean closeConnection() {
		boolean didClose = false;
		try {
			if (connection != null) {
				connection.close();
				didClose = true;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			connection = null;
		}
		return didClose;
	}

	@Override
	public PreparedStatement prepare(String query) {
		try {
			return connection.prepareStatement(query);
		} catch (Exception ex) {
			System.err.println(ex);
			return null;
		}
	}

	@Override
	public ResultSet executePrepared(PreparedStatement prepared) {
		try {
			openIfNotClosed();
			prepared.executeQuery();
			return prepared.executeQuery();
		} catch (Exception ex) {
			System.err.println(ex.toString());
			return null;
		}
	}

	@Override
	public ResultSet executePrepared(String query, Consumer<PreparedStatement> prepare) {
		try {
			openIfNotClosed();
			PreparedStatement statement = connection.prepareStatement(query);
			prepare.accept(statement);
			return executePrepared(statement);
		} catch (Exception ex) {
			System.err.println(ex.toString());
			return null;
		}
	}

	public boolean executeQuery(String query) {
		try {
			openIfNotClosed();
			Statement statement = connection.createStatement();
			statement.executeQuery(query);
		} catch (Exception ex) {
			System.err.println(ex.toString());
			return false;
		}
		return true;
	}

	public ResultSet selectQuery(String query) {
		ResultSet result = null;
		try {
			openIfNotClosed();
			Statement statement = connection.createStatement();
			result = statement.executeQuery(query);
		} catch (Exception ex) {
			System.err.println(ex.toString());
		}
		return result;
	}

	public ResultSet insertQuery(String query) {
		ResultSet result = null;
		try {
			openIfNotClosed();
			Statement statement = connection.createStatement();
			statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
			result = statement.getGeneratedKeys();
		} catch (Exception ex) {
			System.err.println(ex.toString());
		}
		return result;
	}

	public ResultSet updateQuery(String query) {
		return insertQuery(query);
	}

	public ResultSet deleteQuery(String query) {
		return insertQuery(query);
	}

	private boolean openIfNotClosed() throws SQLException {
		delay = 100;
		return !(connection == null || connection.isClosed()) || this.openConnection();
	}

	public void run() {
		delay--;
		if (delay == 0) {
			closeConnection();
		}
		try {
			Thread.sleep(1000L);
		} catch (Exception ex) {
		}
	}
}
