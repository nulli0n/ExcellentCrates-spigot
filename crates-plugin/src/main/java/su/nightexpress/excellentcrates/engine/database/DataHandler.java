package su.nightexpress.excellentcrates.engine.database;

import java.sql.SQLException;
import java.util.function.Consumer;

import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import su.nightexpress.engine.component.DatabaseClient;
import su.nightexpress.excellentcrates.ExcellentCratesPlugin;
import su.nightexpress.nightcore.db.AbstractDatabaseManager;
import su.nightexpress.nightcore.db.config.DatabaseConfig;
import su.nightexpress.nightcore.db.statement.RowMapper;
import su.nightexpress.nightcore.db.table.Table;

@NullMarked
public class DataHandler extends AbstractDatabaseManager<ExcellentCratesPlugin> implements DatabaseClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataHandler.class);

    public DataHandler(ExcellentCratesPlugin plugin, DatabaseConfig config) {
        super(plugin, config);
    }

    @Override
    public void onSynchronize() {
        this.synchronizer.syncAll();
    }

    @Override
    protected void onInitialize() {

    }

    public <T> void addCustomSync(Table table,
                                  RowMapper<T> mapper,
                                  Consumer<T> consumer) {
        this.addTableSync(table, resultSet -> {
            try {
                T data = mapper.map(resultSet);
                consumer.accept(data);
            }
            catch (SQLException exception) {
                LOGGER.error("Failed to map row from table: " + table.getName(), exception);
            }
        });
    }

    @Override
    protected void onClose() {

    }

    @Override
    public void onPurge() {

    }
}
