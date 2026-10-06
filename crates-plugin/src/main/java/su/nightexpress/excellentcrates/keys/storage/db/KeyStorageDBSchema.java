package su.nightexpress.excellentcrates.keys.storage.db;

import java.util.UUID;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.id.IdentifierParser;
import su.nightexpress.excellentcrates.keys.KeyConstants;
import su.nightexpress.excellentcrates.keys.storage.model.StoredKey;
import su.nightexpress.nightcore.db.column.Column;
import su.nightexpress.nightcore.db.statement.RowMapper;
import su.nightexpress.nightcore.db.statement.template.InsertStatement;
import su.nightexpress.nightcore.db.statement.template.SelectStatement;

@NullMarked
public final class KeyStorageDBSchema {

    public static final Column<UUID>   OWNER_ID = Column.uuidType("owner_id").primaryKey().build();
    public static final Column<String> KEY_ID   = Column.stringType("key_id", KeyConstants.KEY_ID_MAX_LENGTH)
        .primaryKey().build();

    public static final Column<Integer> VIRTUAL_BALANCE  = Column.intType("virtual_balance").defaultValue(0).build();
    public static final Column<Integer> UNCLAIMED_AMOUNT = Column.intType("unclaimed_amount").defaultValue(0).build();

    public static final RowMapper<StoredKey> STORED_KEY_MAPPER = resultSet -> {
        UUID ownerId = OWNER_ID.readOrThrow(resultSet);
        String keyIdRaw = KEY_ID.readOrThrow(resultSet);
        Identifier keyId = IdentifierParser.parse(keyIdRaw)
            .orElseThrow(() -> new IllegalStateException("Corrupted key ID: '" + keyIdRaw + "'"));

        int virtualBalance = VIRTUAL_BALANCE.readOrThrow(resultSet);
        int unclaimedAmount = UNCLAIMED_AMOUNT.readOrThrow(resultSet);

        return new StoredKey(ownerId, keyId, virtualBalance, unclaimedAmount);
    };

    public static final InsertStatement<StoredKey> STORED_KEY_INSERT = InsertStatement.builder(StoredKey.class)
        .setUUID(OWNER_ID, StoredKey::getOwnerId)
        .setString(KEY_ID, data -> data.getKeyId().value())
        .setInt(VIRTUAL_BALANCE, StoredKey::getVirtualBalance)
        .setInt(UNCLAIMED_AMOUNT, StoredKey::getUnclaimedAmount)
        .build();

    public static final SelectStatement<StoredKey> STORED_KEY_SELECT = SelectStatement.builder(STORED_KEY_MAPPER)
        .build();

    private KeyStorageDBSchema() {
    }
}
