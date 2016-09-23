package nl.makertim.luckpermsui.util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.function.Consumer;

public interface IDatabaseManager {

	/**
	 *
	 * Open a connection with the credentials
	 */
	boolean openConnection();

	/**
	 *
	 * Close connection if not already
	 */
	boolean closeConnection();

	/**
	 *
	 * Make a string save to use in databases, escape all invald or dangerous
	 * characters
	 *
	 * @param raw
	 *            The object to make string save
	 */
	String prepareString(Object raw);

	default double getSQLVersion() {
		String ret = getRawSQLVersion();
		if (ret == null) {
			return -1;
		}
		ret = ret.replaceFirst("\\.", "");
		try {
			return Double.parseDouble(ret);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return -1;
	}

	String getRawSQLVersion();

	ResultSet executePrepared(String query, Consumer<PreparedStatement> prepare);

	PreparedStatement prepare(String query);

	ResultSet executePrepared(PreparedStatement prepared);

	default boolean executeQuery(String query, Object... obj) {
		return executeQuery(String.format(query, obj));
	}

	/**
	 *
	 * Execute sql
	 *
	 * @param query
	 *            The SQL that will be executed
	 */
	boolean executeQuery(String query);

	default Object getFirst(String query, Object... obj) {
		return getFirst(String.format(query, obj));
	}

	default Object getFirst(String query) {
		ResultSet rs = selectQuery(query);
		Object ret = null;
		try {
			if (rs.next()) {
				ret = rs.getObject(1);
			}
		} catch (Exception ex) {
			System.err.println(ex.toString());
		}
		return ret;
	}

	default ResultSet selectQuery(String query, Object... obj) {
		return selectQuery(String.format(query, obj));
	}

	/**
	 *
	 * Execute SELECT sql
	 *
	 * @param query
	 *            The SQL that will be executed
	 */
	ResultSet selectQuery(String query);

	default ResultSet insertQuery(String query, Object... obj) {
		return insertQuery(String.format(query, obj));
	}

	/**
	 *
	 * Execute INSERT sql
	 *
	 * @param query
	 *            The SQL that will be executed
	 */
	ResultSet insertQuery(String query);

	default ResultSet updateQuery(String query, Object... obj) {
		return updateQuery(String.format(query, obj));
	}

	/**
	 *
	 * Execute UPDATE sql
	 *
	 * @param query
	 *            The SQL that will be executed
	 */
	ResultSet updateQuery(String query);

	default ResultSet deleteQuery(String query, Object... obj) {
		return deleteQuery(String.format(query, obj));
	}

	/**
	 *
	 * Execute DELETE sql
	 *
	 * @param query
	 *            The SQL that will be executed
	 */
	ResultSet deleteQuery(String query);
}
