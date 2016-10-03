package nl.makertim.luckpermsui;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.lucko.luckperms.api.data.DatastoreConfiguration;

import java.io.File;

@Getter
@AllArgsConstructor
public class StorageOptions implements DatastoreConfiguration {

    private final String type;
    private final String address;
    private final String database;
    private final String username;
    private final String password;
    private final File file;

}
