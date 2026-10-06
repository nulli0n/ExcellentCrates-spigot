package su.nightexpress.excellentcrates.reward.feature.cooldown.db;

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
import su.nightexpress.excellentcrates.api.reward.cooldown.RewardCooldownData;
import su.nightexpress.nightcore.db.statement.condition.Operator;
import su.nightexpress.nightcore.db.statement.condition.Wheres;
import su.nightexpress.nightcore.db.table.Table;

@NullMarked
public class RewardCooldownSQLRepository implements SQLRepository<UUID, Identifier, RewardCooldownData> {

    private final DatabaseClient databaseClient;
    private final Table          table;

    public RewardCooldownSQLRepository(DatabaseClient databaseClient, RewardCooldownDBSettings dbSettings) {
        this.databaseClient = databaseClient;

        this.table = Table.builder(dbSettings.tableName())
            .withColumn(RewardCooldownDBSchema.PLAYER_ID_COLUMN)
            .withColumn(RewardCooldownDBSchema.REWARD_ID_COLUMN)
            .withColumn(RewardCooldownDBSchema.PERMANENT_COLUMN)
            .withColumn(RewardCooldownDBSchema.EXPIRATION_TIMESTAMP_COLUMN)
            .build();
    }

    public void initTable() {
        this.databaseClient.createTable(this.table);
    }

    public void registerCooldownSync(RewardCooldownCachedDataService dataService) {
        this.databaseClient.addCustomSync(this.table, RewardCooldownDBSchema.COOLDOWN_DATA_MAPPER, data -> {
            dataService.ingestSyncData(data);
        });
    }

    @Override
    public CompletableFuture<Void> delete(Collection<RemoveContext<UUID, Identifier>> keys) {
        Wheres<RemoveContext<UUID, Identifier>> wheres = Wheres
            .whereUUID(RewardCooldownDBSchema.PLAYER_ID_COLUMN, (RemoveContext<UUID, Identifier> ctx) -> ctx
                .parentId())
            .and(RewardCooldownDBSchema.REWARD_ID_COLUMN, Operator.EQUALS_IGNORE_CASE, ctx -> ctx.key().value());

        return CompletableFuture.runAsync(() -> this.databaseClient.delete(this.table, keys, wheres));
    }

    @Override
    public CompletableFuture<List<RewardCooldownData>> loadAllByParent(UUID parentId) {
        Wheres<Object> wheres = Wheres
            .whereUUID(RewardCooldownDBSchema.PLAYER_ID_COLUMN, o -> parentId);

        return CompletableFuture.supplyAsync(() -> {
            return this.databaseClient.selectWhere(this.table, RewardCooldownDBSchema.COOLDOWN_SELECT, wheres);
        });
    }

    @Override
    public CompletableFuture<Optional<RewardCooldownData>> loadById(UUID parentId, Identifier key) {
        Wheres<Object> wheres = Wheres
            .whereUUID(RewardCooldownDBSchema.PLAYER_ID_COLUMN, o -> parentId)
            .and(RewardCooldownDBSchema.REWARD_ID_COLUMN, Operator.EQUALS_IGNORE_CASE, o -> key.value());

        return CompletableFuture.supplyAsync(() -> {
            return this.databaseClient.selectFirst(this.table, RewardCooldownDBSchema.COOLDOWN_SELECT, wheres);
        });
    }

    @Override
    public CompletableFuture<List<RewardCooldownData>> loadAll() {
        return CompletableFuture.supplyAsync(() -> {
            return this.databaseClient.selectAny(this.table, RewardCooldownDBSchema.COOLDOWN_SELECT);
        });
    }

    @Override
    public CompletableFuture<Void> upsert(Collection<RewardCooldownData> data) {
        return CompletableFuture.runAsync(() -> {
            this.databaseClient.insert(this.table, RewardCooldownDBSchema.COOLDOWN_UPSERT, data);
        });
    }
}
