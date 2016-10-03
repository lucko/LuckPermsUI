package nl.makertim.luckpermsui;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DatabaseType {

	MYSQL("mysql"),
	SQLITE("sqlite"),
	H2("h2"),
	JSON("json"),
	YAML("yaml"),
	MONGODB("mongodb");

	public final String type;

}
