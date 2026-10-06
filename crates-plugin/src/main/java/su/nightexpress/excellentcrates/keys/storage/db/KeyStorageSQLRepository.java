package su.nightexpress.excellentcrates.keys.storage.db;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.DatabaseClient;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.sql.RemoveContext;
import su.nightexpress.engine.sql.SQLRepository;
import su.nightexpress.excellentcrates.keys.storage.model.StoredKey;
import su.nightexpress.nightcore.db.statement.condition.Operator;
import su.nightexpress.nightcore.db.statement.condition.Wheres;
import su.nightexpress.nightcore.db.table.Table;

@NullMarked
public class KeyStorageSQLRepository implements SQLRepository<UUID, Identifier, StoredKey> {

    private final DatabaseClient databaseClient;
    private final Table          keyDataTable;

    public KeyStorageSQLRepository(DatabaseClient databaseClient, String tableName) {
        this.databaseClient = databaseClient;

        this.keyDataTable = Table.builder(tableName)
            .withColumn(KeyStorageDBSchema.OWNER_ID)
            .withColumn(KeyStorageDBSchema.KEY_ID)
            .withColumn(KeyStorageDBSchema.VIRTUAL_BALANCE)
            .withColumn(KeyStorageDBSchema.UNCLAIMED_AMOUNT)
            .build();
    }

    public void initTable() {
        this.databaseClient.createTable(this.keyDataTable);
    }

    public void registerKeyDataSync(KeyStorageCachedDataService dataService) {
        this.databaseClient.addCustomSync(this.keyDataTable, KeyStorageDBSchema.STORED_KEY_MAPPER, data -> {
            dataService.ingestSyncData(data);
        });
    }

    @Override
    public CompletableFuture<Void> delete(Collection<RemoveContext<UUID, Identifier>> data) {
        Wheres<RemoveContext<UUID, Identifier>> wheres = Wheres
            .whereUUID(KeyStorageDBSchema.OWNER_ID, (RemoveContext<UUID, Identifier> ctx) -> ctx.parentId())
            .and(KeyStorageDBSchema.KEY_ID, Operator.EQUALS_IGNORE_CASE, ctx -> ctx.key().value());

        return CompletableFuture.runAsync(() -> this.databaseClient.delete(this.keyDataTable, data, wheres));
    }

    @Override
    public CompletableFuture<List<StoredKey>> loadAll() {
        return CompletableFuture.supplyAsync(() -> {
            return this.databaseClient.selectAny(this.keyDataTable, KeyStorageDBSchema.STORED_KEY_SELECT);
        });
    }

    @Override
    public CompletableFuture<List<StoredKey>> loadAllByParent(UUID key) {
        Wheres<Object> wheres = Wheres
            .whereUUID(KeyStorageDBSchema.OWNER_ID, o -> key);

        return CompletableFuture.supplyAsync(() -> {
            return this.databaseClient.selectWhere(this.keyDataTable, KeyStorageDBSchema.STORED_KEY_SELECT, wheres);
        });
    }

    @Override
    public CompletableFuture<Optional<StoredKey>> loadById(UUID parent, Identifier key) {
        Wheres<Object> wheres = Wheres
            .whereUUID(KeyStorageDBSchema.OWNER_ID, o -> parent)
            .and(KeyStorageDBSchema.KEY_ID, Operator.EQUALS_IGNORE_CASE, k -> key.value());

        return CompletableFuture.supplyAsync(() -> {
            return this.databaseClient.selectFirst(this.keyDataTable, KeyStorageDBSchema.STORED_KEY_SELECT, wheres);
        });
    }

    @Override
    public CompletableFuture<Void> upsert(Collection<StoredKey> data) {
        return CompletableFuture.runAsync(() -> {
            this.databaseClient.insert(this.keyDataTable, KeyStorageDBSchema.STORED_KEY_INSERT, data);
        });
    }
}
