package su.nightexpress.excellentcrates.reward.feature.limit.db;

import java.sql.SQLException;
import java.util.UUID;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.id.IdentifierParser;
import su.nightexpress.excellentcrates.api.reward.registry.RewardId;
import su.nightexpress.excellentcrates.core.SharedConstants;
import su.nightexpress.nightcore.db.column.Column;
import su.nightexpress.nightcore.db.statement.RowMapper;
import su.nightexpress.nightcore.db.statement.template.InsertStatement;
import su.nightexpress.nightcore.db.statement.template.SelectStatement;

@NullMarked
public final class RewardLimitDBSchema {

    public static final Column<UUID> PLAYER_ID_COLUMN = Column.uuidType("player_id").primaryKey().build();

    public static final Column<String> CRATE_ID_COLUMN = Column
        .stringType("crate_id", SharedConstants.MAX_CRATE_ID_LENGTH)
        .defaultValue("none")
        .primaryKey()
        .build();

    public static final Column<String> REWARD_ID_COLUMN = Column
        .stringType("reward_id", SharedConstants.MAX_REWARD_ID_LENGTH)
        .primaryKey()
        .build();

    public static final Column<Integer> USES_COLUMN = Column.intType("uses").build();

    public static final RowMapper<RewardLimitData> LIMIT_DATA_MAPPER = resultSet -> {
        UUID playerId = PLAYER_ID_COLUMN.readOrThrow(resultSet);

        String crateIdRaw = CRATE_ID_COLUMN.readOrThrow(resultSet);
        Identifier crateId = IdentifierParser.parse(crateIdRaw)
            .orElseThrow(() -> new SQLException("Corrupted crate ID: '" + crateIdRaw + "'"));

        String rewardIdRaw = REWARD_ID_COLUMN.readOrThrow(resultSet);
        Identifier rewardId = IdentifierParser.parse(rewardIdRaw)
            .orElseThrow(() -> new SQLException("Corrupted reward ID: '" + rewardIdRaw + "'"));

        int uses = USES_COLUMN.readOrThrow(resultSet);

        RewardId id = new RewardId(crateId, rewardId);
        RewardLimitData data = new RewardLimitData(playerId, id);

        data.setRolls(uses);

        return data;
    };

    public static final InsertStatement<RewardLimitData> LIMIT_DATA_INSERT = InsertStatement
        .builder(RewardLimitData.class)
        .updateOnConflict()
        .setUUID(PLAYER_ID_COLUMN, RewardLimitData::getPlayerId)
        .setString(CRATE_ID_COLUMN, RewardLimitData::getCrateIdString)
        .setString(REWARD_ID_COLUMN, RewardLimitData::getRewardIdString)
        .setInt(USES_COLUMN, RewardLimitData::getRolls)
        .build();

    public static final SelectStatement<RewardLimitData> LIMIT_DATA_SELECT = SelectStatement
        .builder(LIMIT_DATA_MAPPER)
        .build();

    private RewardLimitDBSchema() {
    }
}
