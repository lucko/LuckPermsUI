package nl.makertim.luckpermsui.internal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import nl.makertim.luckpermsui.Main;

public class UserManager {

	private static final PreparedStatement UPDATE_USR = Main.manager
			.prepare("UPDATE lp_users SET name = ?, primary_group = ?, perms = ? WHERE uuid = ?;");

	public static List<User> getUsers(String filter) {
		filter = Main.manager.prepareString(filter);
		List<User> users = new ArrayList<>();
		ResultSet rs = Main.manager
				.selectQuery("SELECT * FROM lp_users WHERE name LIKE '%" + filter + "%' ORDER BY name;");
		try {
			while (rs.next()) {
				UUID uuid = UUID.fromString(rs.getString("uuid"));
				String name = rs.getString("name");
				String primaryGroup = rs.getString("primary_group");
				String json = rs.getString("perms");
				users.add(new User(uuid, name, primaryGroup, json));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return users;
	}

	public static void updateUser(User user) {
		synchronized (UPDATE_USR) {
			try {
				UPDATE_USR.setString(1, user.getName());
				UPDATE_USR.setString(2, user.getDefaultGroup());
				UPDATE_USR.setString(3, user.getJson());
				UPDATE_USR.setString(4, user.getUuid().toString());
				Main.manager.updatePrepared(UPDATE_USR);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
