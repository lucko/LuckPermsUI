package nl.makertim.luckpermsui.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLDatabaseManager implements IDatabaseManager, Runnable {

	private Connection connection;
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
	public MySQLDatabaseManager(String type, String url, int port, String username, String password, String database) {
		this.port = port;
		this.type = type;
		this.database = database;
		this.username = username;
		this.password = password;
		this.url = url;
		new Thread(this).start();
	}

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
	public MySQLDatabaseManager(String type, String url, String username, String password, String database) {
		this(type, url, 3306, username, password, database);
	}

	/**
	 * This is a wrapper for databases
	 * 
	 * @param url
	 *            the url/ip of the server
	 * @param username
	 *            the username to login with
	 * @param password
	 *            the password to login with
	 * @param database
	 *            the name of the database
	 */
	public MySQLDatabaseManager(String url, String username, String password, String database) {
		this("mysql", url, username, password, database);
	}

	/**
	 *
	 * Open a connection with the credentials
	 */
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

	/**
	 *
	 * Close connection if not already
	 */
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

	public String getRawSQLVersion() {
		String ret = null;
		ResultSet rs = selectQuery("SELECT @@version as \"version\";");
		try {
			if (rs != null && rs.next()) {
				ret = rs.getString("version");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return ret;
	}

	/**
	 *
	 * Make a string save to use in databases, escape all invald or dangerous
	 * characters
	 * 
	 * @param raw
	 *            The object to make string save
	 */
	public String prepareString(Object raw) {
		return raw.toString().replaceAll(
			"\\{|\\}|\\\\|\\,|\\&|\\?|\\(|\\)|\\[|\\]|\\-|\\;|\\~|\\||\\$|\\!|\\<|\\>|\\*|\\%|\\_|\'|\\\"", "\\\\$0");
	}

	/**
	 *
	 * Execute sql
	 * 
	 * @param query
	 *            The SQL that will be executed
	 */
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

	/**
	 *
	 * Execute SELECT sql
	 * 
	 * @param query
	 *            The SQL that will be executed
	 */
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

	/**
	 *
	 * Execute INSERT sql
	 * 
	 * @param query
	 *            The SQL that will be executed
	 */
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

	/**
	 *
	 * Execute UPDATE sql
	 * 
	 * @param query
	 *            The SQL that will be executed
	 */
	public ResultSet updateQuery(String query) {
		return insertQuery(query);
	}

	/**
	 *
	 * Execute DELETE sql
	 * 
	 * @param query
	 *            The SQL that will be executed
	 */
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