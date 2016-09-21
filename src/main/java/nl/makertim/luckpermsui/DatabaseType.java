package nl.makertim.luckpermsui;

public enum DatabaseType {

	MYSQL("mysql"), SQLITE("sqlite"), H2("h2"), FLATFILE("flatfile"), MONGODB("mongodb");

	public String type;

	DatabaseType(String type) {
		this.type = type;
	}
}
