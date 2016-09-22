package nl.makertim.luckpermsui.util;

import java.sql.ResultSet;

public class MySQLDatabaseManager extends AbstractDatabaseManager {

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
	public MySQLDatabaseManager(String url, int port, String username, String password, String database) {
		super("mysql", url, port, username, password, database);
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
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
		this(url, 3306, username, password, database);
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

	public String prepareString(Object raw) {
		return raw.toString().replaceAll(
			"\\{|\\}|\\\\|\\,|\\&|\\?|\\(|\\)|\\[|\\]|\\-|\\;|\\~|\\||\\$|\\!|\\<|\\>|\\*|\\%|\\_|\'|\\\"", "\\\\$0");
	}
}