package su.nightexpress.excellentcrates.reward.feature.cooldown.db;

import java.sql.SQLException;
import java.util.UUID;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.id.IdentifierParser;
import su.nightexpress.excellentcrates.api.reward.cooldown.RewardCooldownData;
import su.nightexpress.excellentcrates.reward.RewardsConstants;
import su.nightexpress.nightcore.db.column.Column;
import su.nightexpress.nightcore.db.statement.RowMapper;
import su.nightexpress.nightcore.db.statement.template.InsertStatement;
import su.nightexpress.nightcore.db.statement.template.SelectStatement;

public final class RewardCooldownDBSchema {

    public static final Column<UUID> PLAYER_ID_COLUMN = Column.uuidType("player_id").primaryKey().build();

    public static final Column<String> REWARD_ID_COLUMN = Column
        .stringType("reward_id", RewardsConstants.REWARD_ID_LENGTH)
        .primaryKey()
        .build();

    public static final Column<Boolean> PERMANENT_COLUMN = Column.booleanType("permanent")
        .defaultValue(false)
        .build();

    public static final Column<Long> EXPIRATION_TIMESTAMP_COLUMN = Column.longType("expiration_timestamp")
        .defaultValue(0L)
        .build();

    public static final RowMapper<RewardCooldownData> COOLDOWN_DATA_MAPPER = resultSet -> {
        UUID playerId = PLAYER_ID_COLUMN.readOrThrow(resultSet);
        String rewardIdRaw = REWARD_ID_COLUMN.readOrThrow(resultSet);
        Identifier rewardId = IdentifierParser.parse(rewardIdRaw)
            .orElseThrow(() -> new SQLException("Corrupted reward ID: '" + rewardIdRaw + "'"));

        boolean permanent = PERMANENT_COLUMN.readOrThrow(resultSet);
        long cooldownTimestamp = EXPIRATION_TIMESTAMP_COLUMN.readOrThrow(resultSet);

        return new DefaultRewardCooldownData(playerId, rewardId, permanent, cooldownTimestamp);
    };

    public static final InsertStatement<RewardCooldownData> COOLDOWN_UPSERT = InsertStatement
        .builder(RewardCooldownData.class)
        .updateOnConflict()
        .setUUID(PLAYER_ID_COLUMN, RewardCooldownData::getParentId)
        .setString(REWARD_ID_COLUMN, RewardCooldownData::getRewardIdString)
        .setBoolean(PERMANENT_COLUMN, RewardCooldownData::isPermanent)
        .setLong(EXPIRATION_TIMESTAMP_COLUMN, RewardCooldownData::getExpirationTimestamp)
        .build();

    public static final SelectStatement<RewardCooldownData> COOLDOWN_SELECT = SelectStatement
        .builder(COOLDOWN_DATA_MAPPER)
        .build();

    private RewardCooldownDBSchema() {
    }
}
