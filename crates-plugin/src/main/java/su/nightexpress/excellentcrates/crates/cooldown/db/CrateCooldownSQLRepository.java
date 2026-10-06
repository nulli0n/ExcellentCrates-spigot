package su.nightexpress.excellentcrates.crates.cooldown.db;

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
import su.nightexpress.excellentcrates.api.crate.cooldown.CrateCooldownData;
import su.nightexpress.nightcore.db.statement.condition.Operator;
import su.nightexpress.nightcore.db.statement.condition.Wheres;
import su.nightexpress.nightcore.db.table.Table;

@NullMarked
public class CrateCooldownSQLRepository implements SQLRepository<UUID, Identifier, CrateCooldownData> {

    private final DatabaseClient databaseClient;
    private final Table          table;

    public CrateCooldownSQLRepository(DatabaseClient databaseClient, CrateCooldownDBSettings dbSettings) {
        this.databaseClient = databaseClient;

        this.table = Table.builder(dbSettings.tableName())
            .withColumn(CrateCooldownDBSchema.PLAYER_ID_COLUMN)
            .withColumn(CrateCooldownDBSchema.CRATE_ID_COLUMN)
            .withColumn(CrateCooldownDBSchema.PERMANENT_COLUMN)
            .withColumn(CrateCooldownDBSchema.EXPIRATION_TIMESTAMP_COLUMN)
            .build();
    }

    public void initTable() {
        this.databaseClient.createTable(this.table);
    }

    public void registerCooldownSync(CrateCooldownCachedDataService dataService) {
        this.databaseClient.addCustomSync(this.table, CrateCooldownDBSchema.COOLDOWN_DATA_MAPPER, data -> {
            dataService.ingestSyncData(data);
        });
    }

    @Override
    public CompletableFuture<Void> delete(Collection<RemoveContext<UUID, Identifier>> keys) {
        Wheres<RemoveContext<UUID, Identifier>> wheres = Wheres
            .whereUUID(CrateCooldownDBSchema.PLAYER_ID_COLUMN, (RemoveContext<UUID, Identifier> ctx) -> ctx
                .parentId())
            .and(CrateCooldownDBSchema.CRATE_ID_COLUMN, Operator.EQUALS_IGNORE_CASE, ctx -> ctx.key().value());

        return CompletableFuture.runAsync(() -> this.databaseClient.delete(this.table, keys, wheres));
    }

    @Override
    public CompletableFuture<List<CrateCooldownData>> loadAllByParent(UUID parentId) {
        Wheres<Object> wheres = Wheres
            .whereUUID(CrateCooldownDBSchema.PLAYER_ID_COLUMN, o -> parentId);

        return CompletableFuture.supplyAsync(() -> {
            return this.databaseClient.selectWhere(this.table, CrateCooldownDBSchema.COOLDOWN_SELECT, wheres);
        });
    }

    @Override
    public CompletableFuture<Optional<CrateCooldownData>> loadById(UUID parentId, Identifier key) {
        Wheres<Object> wheres = Wheres
            .whereUUID(CrateCooldownDBSchema.PLAYER_ID_COLUMN, o -> parentId)
            .and(CrateCooldownDBSchema.CRATE_ID_COLUMN, Operator.EQUALS_IGNORE_CASE, o -> key.value());

        return CompletableFuture.supplyAsync(() -> {
            return this.databaseClient.selectFirst(this.table, CrateCooldownDBSchema.COOLDOWN_SELECT, wheres);
        });
    }

    @Override
    public CompletableFuture<List<CrateCooldownData>> loadAll() {
        return CompletableFuture.supplyAsync(() -> {
            return this.databaseClient.selectAny(this.table, CrateCooldownDBSchema.COOLDOWN_SELECT);
        });
    }

    @Override
    public CompletableFuture<Void> upsert(Collection<CrateCooldownData> data) {
        return CompletableFuture.runAsync(() -> {
            this.databaseClient.insert(this.table, CrateCooldownDBSchema.COOLDOWN_UPSERT, data);
        });
    }
}
