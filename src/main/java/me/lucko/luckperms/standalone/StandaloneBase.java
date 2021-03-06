package me.lucko.luckperms.standalone;

import javafx.application.Platform;
import lombok.Getter;
import me.lucko.luckperms.api.Contexts;
import me.lucko.luckperms.api.Logger;
import me.lucko.luckperms.api.PlatformType;
import me.lucko.luckperms.common.LuckPermsPlugin;
import me.lucko.luckperms.common.api.ApiProvider;
import me.lucko.luckperms.common.calculators.CalculatorFactory;
import me.lucko.luckperms.common.commands.ConsecutiveExecutor;
import me.lucko.luckperms.common.commands.Sender;
import me.lucko.luckperms.common.config.LPConfiguration;
import me.lucko.luckperms.common.constants.Constants;
import me.lucko.luckperms.common.constants.Permission;
import me.lucko.luckperms.common.contexts.ContextManager;
import me.lucko.luckperms.common.core.UuidCache;
import me.lucko.luckperms.common.data.Importer;
import me.lucko.luckperms.common.groups.GroupManager;
import me.lucko.luckperms.common.messaging.RedisMessaging;
import me.lucko.luckperms.common.storage.AbstractDatastore;
import me.lucko.luckperms.common.storage.Datastore;
import me.lucko.luckperms.common.storage.backing.*;
import me.lucko.luckperms.common.tracks.TrackManager;
import me.lucko.luckperms.common.users.UserManager;
import me.lucko.luckperms.common.utils.BufferedRequest;
import me.lucko.luckperms.common.utils.LocaleManager;
import me.lucko.luckperms.common.utils.LogFactory;
import me.lucko.luckperms.standalone.factory.SimpleViewFactory;
import me.lucko.luckperms.standalone.model.StorageOptions;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Getter
public class StandaloneBase implements LuckPermsPlugin {
    private static final String VERSION = StandaloneBase.class.getPackage().getImplementationVersion();

    private final LPStandaloneApp app;
    private final ExecutorService threadPool = Executors.newCachedThreadPool();

    private final java.util.logging.Logger logger;

    private final UserManager userManager;
    private final GroupManager groupManager;
    private final TrackManager trackManager;
    private final LPConfiguration configuration;
    private final Logger log;
    private final UuidCache uuidCache;
    private final ApiProvider apiProvider;
    private final LocaleManager localeManager;
    private Datastore datastore;

    public StandaloneBase(LPStandaloneApp app, StorageOptions options) {
        this.app = app;

        logger = java.util.logging.Logger.getGlobal();
        log = LogFactory.wrap(logger);

        configuration = new StandaloneConfiguration(this);

        switch (options.getType()) {
            case MYSQL:
                datastore = AbstractDatastore.wrap(this, new MySQLBacking(this, options));
                break;
            case SQLITE:
                datastore = AbstractDatastore.wrap(this, new SQLiteBacking(this, options.getFile()));
                break;
            case H2:
                datastore = AbstractDatastore.wrap(this, new H2Backing(this, options.getFile()));
                break;
            case JSON:
                datastore = AbstractDatastore.wrap(this, new JSONBacking(this, options.getFile()));
                break;
            case YAML:
                datastore = AbstractDatastore.wrap(this, new YAMLBacking(this, options.getFile()));
                break;
            case MONGODB:
                datastore = AbstractDatastore.wrap(this, new MongoDBBacking(this, options));
                break;
            default:
                datastore = null;
        }

        localeManager = new LocaleManager();

        uuidCache = new UuidCache(true);
        userManager = new UserManager(this);
        groupManager = new GroupManager(this);
        trackManager = new TrackManager();

        apiProvider = new ApiProvider(this);

        doAsync(() -> {
            app.setBase(StandaloneBase.this);
            loadDatastore(datastore);
            doSync(() -> SimpleViewFactory.getInstance().openManager(app, this));
        });
    }

    public void loadDatastore(Datastore datastore) {
        this.datastore = datastore;
        datastore.init();
        reloadGroups();
        datastore.loadAllTracks();
        doAsync(() -> {
            Set<UUID> users = datastore.getUniqueUsers().getUnchecked();
            users.forEach(user -> doAsync(() -> {
                try {
                    datastore.loadUser(user, "null");
                } catch (Exception ex) {
                    System.err.println("Loading user error: " + ex + "\t by UUID " + user);
                }
            }));
        });
    }

    public void reloadGroups() {
        groupManager.unloadAll();
        datastore.loadAllGroups();
    }

    public void shutdown() {
        datastore.shutdown();
        threadPool.shutdown();
    }

    @Override
    public String getVersion() {
        return VERSION;
    }

    @Override
    public PlatformType getType() {
        return PlatformType.STANDALONE;
    }

    @Override
    public File getMainDir() {
        return new File(".");
    }

    @Override
    public File getDataFolder() {
        return new File(getMainDir(), "data");
    }

    @Override
    public void doAsync(Runnable r) {
        threadPool.execute(r);
    }

    @Override
    public void doSync(Runnable r) {
        Platform.runLater(r);
    }

    @Override
    public void doAsyncRepeating(Runnable runnable, long l) {

    }

    @Override
    public RedisMessaging getRedisMessaging() {
        return null;
    }

    @Override
    public Importer getImporter() {
        return null;
        // TODO: Importer for later
    }

    @Override
    public ConsecutiveExecutor getConsecutiveExecutor() {
        return null;
    }

    @Override
    public ContextManager getContextManager() {
        return null;
    }

    @Override
    public CalculatorFactory getCalculatorFactory() {
        return null;
    }

    @Override
    public int getPlayerCount() {
        return 0;
    }

    @Override
    public List<String> getPlayerList() {
        return Collections.emptyList();
    }

    @Override
    public Set<UUID> getOnlinePlayers() {
        return Collections.emptySet();
    }

    @Override
    public boolean isOnline(UUID uuid) {
        return false;
    }

    @Override
    public List<Sender> getNotifyListeners() {
        return Collections.emptyList();
    }

    @Override
    public Sender getConsoleSender() {
        final LuckPermsPlugin instance = this;
        return new Sender() {
            @Override
            public LuckPermsPlugin getPlatform() {
                return instance;
            }

            @Override
            public String getName() {
                return Constants.getConsoleName();
            }

            @Override
            public UUID getUuid() {
                return Constants.getConsoleUUID();
            }

            @Override
            public void sendMessage(String s) {
                getLogger().info(s);
            }

            @Override
            public boolean hasPermission(Permission permission) {
                return true;
            }
        };
    }

    @Override
    public Set<Contexts> getPreProcessContexts(boolean b) {
        return null;
    }

    @Override
    public Set<UUID> getIgnoringLogs() {
        return Collections.emptySet();
    }

    @Override
    public Object getPlugin(String name) {
        return null;
    }

    @Override
    public Object getService(Class clazz) {
        return null;
    }

    @Override
    public UUID getUUID(String playerName) {
        return null;
    }

    @Override
    public boolean isPluginLoaded(String name) {
        return false;
    }

    @Override
    public BufferedRequest<Void> getUpdateTaskBuffer() {
        return null;
    }
}