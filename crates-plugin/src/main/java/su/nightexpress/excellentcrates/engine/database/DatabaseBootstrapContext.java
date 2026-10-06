package su.nightexpress.excellentcrates.engine.database;

import java.nio.file.Path;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.DatabaseClient;
import su.nightexpress.excellentcrates.ExcellentCratesPlugin;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.db.config.DatabaseConfig;

@NullMarked
public class DatabaseBootstrapContext {

    private static final String SETTINGS_FILE_NAME = "engine.database.yml";

    public final DatabaseClient databaseClient;

    public DatabaseBootstrapContext(ExcellentCratesPlugin plugin) {
        Path settingsPath = plugin.configPath().resolve(SETTINGS_FILE_NAME);
        FileConfig config = FileConfig.load(settingsPath);

        DatabaseConfig dbConfig = DatabaseConfig.read(config, "", "ecrates", "data.db");
        DataHandler dataHandler = new DataHandler(plugin, dbConfig);
        dataHandler.setup();

        config.saveChanges();

        this.databaseClient = dataHandler;
    }
}
