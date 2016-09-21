package nl.makertim.luckpermsui.util;

import java.sql.ResultSet;

public interface IDatabaseManager {

	boolean openConnection();

	boolean closeConnection();

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

	default boolean executeQuery(String query, Object... obj) {
		return executeQuery(String.format(query, obj));
	}

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

	ResultSet selectQuery(String query);

	default ResultSet insertQuery(String query, Object... obj) {
		return insertQuery(String.format(query, obj));
	}

	ResultSet insertQuery(String query);

	default ResultSet updateQuery(String query, Object... obj) {
		return updateQuery(String.format(query, obj));
	}

	ResultSet updateQuery(String query);

	default ResultSet deleteQuery(String query, Object... obj) {
		return deleteQuery(String.format(query, obj));
	}

	ResultSet deleteQuery(String query);
}
