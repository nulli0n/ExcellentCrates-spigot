package su.nightexpress.excellentcrates.keys.storage.model;

import java.util.UUID;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.sql.OwnableData;

@NullMarked
public class StoredKey implements OwnableData<UUID, Identifier> {

    private final UUID       ownerId;
    private final Identifier keyId;

    private int virtualBalance;
    private int unclaimedAmount;

    public StoredKey(UUID ownerId, Identifier id, int virtualBalance, int unclaimedAmount) {
        this.ownerId = ownerId;
        this.keyId = id;
        this.virtualBalance = virtualBalance;
        this.unclaimedAmount = unclaimedAmount;
    }

    /**
     * Creates a new StoredKey with default values for virtualBalance and unclaimedAmount.
     * 
     * @param ownerId the ID of the owner
     * @param id      the ID of the key
     */
    public StoredKey(UUID ownerId, Identifier id) {
        this(ownerId, id, 0, 0);
    }

    @Override
    public Identifier getKey() {
        return this.keyId;
    }

    @Override
    public UUID getParentId() {
        return this.ownerId;
    }

    public Identifier getKeyId() {
        return keyId;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public int getVirtualBalance() {
        return virtualBalance;
    }

    public void setVirtualBalance(int virtualBalance) {
        this.virtualBalance = virtualBalance;
    }

    public int getUnclaimedAmount() {
        return unclaimedAmount;
    }

    public void setUnclaimedAmount(int unclaimedAmount) {
        this.unclaimedAmount = unclaimedAmount;
    }
}
