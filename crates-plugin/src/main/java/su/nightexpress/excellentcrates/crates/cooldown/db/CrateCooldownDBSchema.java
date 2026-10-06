package su.nightexpress.excellentcrates.crates.cooldown.db;

import java.sql.SQLException;
import java.util.UUID;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.id.IdentifierParser;
import su.nightexpress.excellentcrates.api.crate.cooldown.CrateCooldownData;
import su.nightexpress.nightcore.db.column.Column;
import su.nightexpress.nightcore.db.statement.RowMapper;
import su.nightexpress.nightcore.db.statement.template.InsertStatement;
import su.nightexpress.nightcore.db.statement.template.SelectStatement;

public final class CrateCooldownDBSchema {

    private static final int CRATE_ID_LENGTH = 128;

    public static final Column<UUID> PLAYER_ID_COLUMN = Column.uuidType("player_id").primaryKey().build();

    public static final Column<String> CRATE_ID_COLUMN = Column
        .stringType("crate_id", CRATE_ID_LENGTH)
        .primaryKey()
        .build();

    public static final Column<Boolean> PERMANENT_COLUMN = Column.booleanType("permanent")
        .defaultValue(false)
        .build();

    public static final Column<Long> EXPIRATION_TIMESTAMP_COLUMN = Column.longType("expiration_timestamp")
        .defaultValue(0L)
        .build();

    public static final RowMapper<CrateCooldownData> COOLDOWN_DATA_MAPPER = resultSet -> {
        UUID playerId = PLAYER_ID_COLUMN.readOrThrow(resultSet);
        String crateIdRaw = CRATE_ID_COLUMN.readOrThrow(resultSet);
        Identifier crateId = IdentifierParser.parse(crateIdRaw)
            .orElseThrow(() -> new SQLException("Corrupted crate ID: '" + crateIdRaw + "'"));

        boolean permanent = PERMANENT_COLUMN.readOrThrow(resultSet);
        long cooldownTimestamp = EXPIRATION_TIMESTAMP_COLUMN.readOrThrow(resultSet);

        return new DefaultCrateCooldownData(playerId, crateId, permanent, cooldownTimestamp);
    };

    public static final InsertStatement<CrateCooldownData> COOLDOWN_UPSERT = InsertStatement
        .builder(CrateCooldownData.class)
        .updateOnConflict()
        .setUUID(PLAYER_ID_COLUMN, CrateCooldownData::getParentId)
        .setString(CRATE_ID_COLUMN, CrateCooldownData::getCrateIdString)
        .setBoolean(PERMANENT_COLUMN, CrateCooldownData::isPermanent)
        .setLong(EXPIRATION_TIMESTAMP_COLUMN, CrateCooldownData::getExpirationTimestamp)
        .build();

    public static final SelectStatement<CrateCooldownData> COOLDOWN_SELECT = SelectStatement
        .builder(COOLDOWN_DATA_MAPPER)
        .build();

    private CrateCooldownDBSchema() {
    }
}
