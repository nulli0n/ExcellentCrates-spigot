package su.nightexpress.excellentcrates.reward.feature.limit.db;

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
import su.nightexpress.nightcore.db.statement.condition.Operator;
import su.nightexpress.nightcore.db.statement.condition.Wheres;
import su.nightexpress.nightcore.db.table.Table;

@NullMarked
public class RewardLimitSQLRepository implements SQLRepository<UUID, Identifier, RewardLimitData> {

    private final DatabaseClient databaseClient;
    private final Table          table;

    public RewardLimitSQLRepository(DatabaseClient databaseClient, String tableName) {
        this.databaseClient = databaseClient;

        this.table = Table.builder(tableName)
            .withColumn(RewardLimitDBSchema.PLAYER_ID_COLUMN)
            .withColumn(RewardLimitDBSchema.REWARD_ID_COLUMN)
            .withColumn(RewardLimitDBSchema.USES_COLUMN)
            .build();
    }

    public void initTable() {
        this.databaseClient.createTable(this.table);
    }

    public void registerSync(RewardLimitCachedDataService dataService) {
        this.databaseClient.addCustomSync(this.table, RewardLimitDBSchema.LIMIT_DATA_MAPPER, data -> {
            dataService.ingestSyncData(data);
        });
    }

    @Override
    public CompletableFuture<Void> delete(Collection<RemoveContext<UUID, Identifier>> data) {
        Wheres<RemoveContext<UUID, Identifier>> wheres = Wheres
            .whereUUID(RewardLimitDBSchema.PLAYER_ID_COLUMN, (RemoveContext<UUID, Identifier> ctx) -> ctx.parentId())
            .and(RewardLimitDBSchema.REWARD_ID_COLUMN, Operator.EQUALS_IGNORE_CASE, ctx -> ctx.key().value());

        return CompletableFuture.runAsync(() -> this.databaseClient.delete(this.table, wheres));
    }

    @Override
    public CompletableFuture<List<RewardLimitData>> loadAll() {
        return CompletableFuture.supplyAsync(() -> {
            return this.databaseClient.selectAny(this.table, RewardLimitDBSchema.LIMIT_DATA_SELECT);
        });
    }

    @Override
    public CompletableFuture<List<RewardLimitData>> loadAllByParent(UUID key) {
        Wheres<Object> wheres = Wheres.whereUUID(RewardLimitDBSchema.PLAYER_ID_COLUMN, o -> key);

        return CompletableFuture.supplyAsync(() -> {
            return this.databaseClient.selectWhere(this.table, RewardLimitDBSchema.LIMIT_DATA_SELECT, wheres);
        });
    }

    @Override
    public CompletableFuture<Optional<RewardLimitData>> loadById(UUID parent, Identifier key) {
        Wheres<Object> wheres = Wheres
            .whereUUID(RewardLimitDBSchema.PLAYER_ID_COLUMN, o -> parent)
            .and(RewardLimitDBSchema.REWARD_ID_COLUMN, Operator.EQUALS_IGNORE_CASE, o -> key.value());

        return CompletableFuture.supplyAsync(() -> {
            return this.databaseClient.selectFirst(this.table, RewardLimitDBSchema.LIMIT_DATA_SELECT, wheres);
        });
    }

    @Override
    public CompletableFuture<Void> upsert(Collection<RewardLimitData> data) {
        return CompletableFuture.runAsync(() -> {
            this.databaseClient.insert(this.table, RewardLimitDBSchema.LIMIT_DATA_INSERT, data);
        });
    }
}
