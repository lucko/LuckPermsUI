package me.lucko.luckperms.standalone.model;

import java.io.File;

import lombok.Getter;
import me.lucko.luckperms.standalone.DatabaseType;
import me.lucko.luckperms.common.storage.DatastoreConfiguration;

@Getter
public class StorageOptions extends DatastoreConfiguration {

    private final DatabaseType type;
    private final File file;

    public StorageOptions(DatabaseType type, File file) {
        super(file.getAbsolutePath(), null, null, null, -1);
        this.type = type;
        this.file = file;
    }

    public StorageOptions(DatabaseType type, String address, String database, String username, String password) {
        super(address, database, username, password, 10);
        this.type = type;
        this.file = null;
    }
}
