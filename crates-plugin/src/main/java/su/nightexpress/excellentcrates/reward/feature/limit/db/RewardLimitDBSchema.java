package su.nightexpress.excellentcrates.reward.feature.limit.db;

import java.sql.SQLException;
import java.util.UUID;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.id.IdentifierParser;
import su.nightexpress.excellentcrates.reward.RewardsConstants;
import su.nightexpress.nightcore.db.column.Column;
import su.nightexpress.nightcore.db.statement.RowMapper;
import su.nightexpress.nightcore.db.statement.template.InsertStatement;
import su.nightexpress.nightcore.db.statement.template.SelectStatement;

@NullMarked
public final class RewardLimitDBSchema {

    public static final Column<UUID> PLAYER_ID_COLUMN = Column.uuidType("player_id").primaryKey().build();

    public static final Column<String> REWARD_ID_COLUMN = Column
        .stringType("reward_id", RewardsConstants.REWARD_ID_LENGTH)
        .primaryKey()
        .build();

    public static final Column<Integer> USES_COLUMN = Column.intType("uses").build();

    public static final RowMapper<RewardLimitData> LIMIT_DATA_MAPPER = resultSet -> {
        UUID playerId = PLAYER_ID_COLUMN.readOrThrow(resultSet);
        String rewardIdRaw = REWARD_ID_COLUMN.readOrThrow(resultSet);
        Identifier rewardId = IdentifierParser.parse(rewardIdRaw)
            .orElseThrow(() -> new SQLException("Corrupted reward ID: '" + rewardIdRaw + "'"));

        int uses = USES_COLUMN.readOrThrow(resultSet);

        RewardLimitData data = new RewardLimitData(playerId, rewardId);

        data.setRolls(uses);

        return data;
    };

    public static final InsertStatement<RewardLimitData> LIMIT_DATA_INSERT = InsertStatement
        .builder(RewardLimitData.class)
        .updateOnConflict()
        .setUUID(PLAYER_ID_COLUMN, RewardLimitData::getPlayerId)
        .setString(REWARD_ID_COLUMN, RewardLimitData::getRewardIdString)
        .setInt(USES_COLUMN, RewardLimitData::getRolls)
        .build();

    public static final SelectStatement<RewardLimitData> LIMIT_DATA_SELECT = SelectStatement
        .builder(LIMIT_DATA_MAPPER)
        .build();

    private RewardLimitDBSchema() {
    }
}
