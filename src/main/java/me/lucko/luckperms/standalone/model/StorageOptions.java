package me.lucko.luckperms.standalone.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.lucko.luckperms.DatabaseType;
import me.lucko.luckperms.api.data.DatastoreConfiguration;

import java.io.File;

@Getter
@AllArgsConstructor
public class StorageOptions implements DatastoreConfiguration {

	private final DatabaseType type;
    private final String address;
    private final String database;
    private final String username;
    private final String password;
    private final File file;

}
