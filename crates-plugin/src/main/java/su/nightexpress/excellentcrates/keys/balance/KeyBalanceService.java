package su.nightexpress.excellentcrates.keys.balance;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.cache.CacheStrategy;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.key.CrateKey;
import su.nightexpress.excellentcrates.api.key.balance.IKeyBalance;
import su.nightexpress.excellentcrates.keys.item.KeyItemService;
import su.nightexpress.excellentcrates.keys.lang.KeyLang;
import su.nightexpress.excellentcrates.keys.storage.db.KeyStorageCachedDataService;
import su.nightexpress.excellentcrates.keys.storage.model.StoredKey;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.Players;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

public final class KeyBalanceService {

    private final KeyStorageCachedDataService storageService;
    private final KeyItemService              itemService;

    public KeyBalanceService(KeyStorageCachedDataService storageService, KeyItemService itemService) {
        this.storageService = storageService;
        this.itemService = itemService;
    }

    public boolean hasKey(Player player, CrateKey key, int amount) {
        int absAmount = Math.abs(amount);
        return this.countKeys(player, key) >= absAmount;
    }

    public int countKeys(Player player, CrateKey key) {
        if (key.getBase().isVirtual()) {
            return this.countVirtualKeys(player, key);
        }

        return this.countPhysicalKeys(player, key);
    }

    public int countPhysicalKeys(Player player, CrateKey key) {
        return this.countPhysicalKeys(player, key.getId());
    }

    public int countPhysicalKeys(Player player, Identifier keyId) {
        int balance = 0;

        for (ItemStack item : player.getInventory().getContents()) {
            if (item == null) continue;

            Identifier id = this.itemService.getKeyIdFromItem(item);
            if (id != null && id.equals(keyId)) {
                balance += item.getAmount();
            }
        }

        return balance;
    }

    public int countVirtualKeys(Player player, CrateKey key) {
        return this.countVirtualKeys(player, key.getId());
    }

    public int countVirtualKeys(Player player, Identifier keyId) {
        StoredKey storedKey = this.storageService.getCached(player.getUniqueId(), keyId).orElse(null);
        return storedKey != null ? storedKey.getVirtualBalance() : 0;
    }

    public int countUnclaimedKeys(Player player, CrateKey key) {
        return this.countUnclaimedKeys(player, key.getId());
    }

    public int countUnclaimedKeys(Player player, Identifier keyId) {
        StoredKey storedKey = this.storageService.getCached(player.getUniqueId(), keyId).orElse(null);
        return storedKey != null ? storedKey.getUnclaimedAmount() : 0;
    }

    public IKeyBalance getKeyBalance(Player player, CrateKey key) {
        int physical = this.countPhysicalKeys(player, key);
        int virtual = this.countVirtualKeys(player, key);
        int unclaimed = this.countUnclaimedKeys(player, key);

        return new KeyBalance(physical, virtual, unclaimed);
    }

    public CompletableFuture<IKeyBalance> getKeyBalanceAsync(UUID playerId, CrateKey key) {
        Identifier keyId = key.getId();

        Player player = Players.getPlayer(playerId);
        if (player != null) {
            return CompletableFuture.completedFuture(this.getKeyBalance(player, key));
        }

        return this.storageService.loadAndCacheAsync(playerId, keyId, CacheStrategy.TEMPORARY)
            .thenApply(opt -> {
                if (opt.isEmpty()) {
                    return KeyBalance.empty();
                }

                StoredKey storedKey = opt.get();

                int inventory = 0;  // Player is offline, can't count physical keys
                int virtual = storedKey != null ? storedKey.getVirtualBalance() : 0;
                int unclaimed = storedKey != null ? storedKey.getUnclaimedAmount() : 0;

                return new KeyBalance(inventory, virtual, unclaimed);
            });
    }

    public ActionResult redeemKey(Player player, CrateKey key) {
        UUID playerId = player.getUniqueId();
        Identifier keyId = key.getId();

        StoredKey storedKey = this.storageService.getCached(playerId, keyId).orElse(null);
        if (storedKey == null || storedKey.getUnclaimedAmount() <= 0) {
            return ActionResult.fail(KeyLang.KEY_REDEEM_NO_UNCLAIMED);
        }

        int unclaimed = storedKey.getUnclaimedAmount();
        this.addKey(player, key, unclaimed);
        storedKey.setUnclaimedAmount(0);
        this.storageService.markDirty(storedKey);

        return ActionResult.ok(KeyLang.KEY_REDEEM_FEEDBACK, ctx -> ctx
            .with(CommonPlaceholders.GENERIC_AMOUNT, () -> NumberUtil.format(unclaimed))
        );
    }

    public ActionResult addKey(Player player, CrateKey key, int amount) {
        boolean virtual = key.getBase().isVirtual();
        int absAmount = Math.abs(amount);

        if (virtual) {
            Identifier keyId = key.getId();

            StoredKey storedKey = this.storageService.getCachedOrCreate(
                player.getUniqueId(), keyId, CacheStrategy.PERMANENT
            );
            storedKey.setVirtualBalance(storedKey.getVirtualBalance() + absAmount);
            this.storageService.markDirty(storedKey);
        }
        else {
            ItemStack keyItem = this.itemService.createTaggedItem(key).orElse(null);
            if (keyItem == null) {
                return ActionResult.fail(KeyLang.ERROR_ITEM_CREATION_FAILED);
            }

            keyItem.setAmount(absAmount);
            Players.addItem(player, keyItem);
        }

        return ActionResult.ok(KeyLang.KEY_GIVE_FEEDBACK, ctx -> ctx
            .with(CommonPlaceholders.GENERIC_AMOUNT, () -> NumberUtil.format(absAmount))
        );
    }

    /**
     * Gives a key to a player asynchronously.
     * If the key is virtual, it will update the player's virtual balance in the database.
     * If the key is physical and the player is online, it will add the key item to their inventory.
     * If the player is offline, it will add the unclaimed amount to their account for later retrieval.
     * 
     * @param playerId the UUID of the player to give the key to
     * @param key      the key to give
     * @param amount   the amount of keys to give (can be negative for removal)
     * @return the result of the action
     */
    public CompletableFuture<ActionResult> giveKeyAsync(UUID playerId, CrateKey key, int amount) {
        Identifier keyId = key.getId();
        int absAmount = Math.abs(amount);
        boolean virtual = key.getBase().isVirtual();

        ActionResult success = ActionResult.ok(KeyLang.KEY_GIVE_FEEDBACK, ctx -> ctx
            .with(CommonPlaceholders.GENERIC_AMOUNT, () -> NumberUtil.format(absAmount))
        );

        if (virtual) {
            return this.storageService.updateDataAndCreateIfAbsentAsync(playerId, keyId, CacheStrategy.TEMPORARY,
                storedKey -> {
                    storedKey.setVirtualBalance(storedKey.getVirtualBalance() + absAmount);
                    return success;
                });
        }
        else {
            Player player = Players.getPlayer(playerId);
            if (player != null) {
                return CompletableFuture.completedFuture(this.addKey(player, key, absAmount));
            }

            return this.storageService.updateDataAndCreateIfAbsentAsync(playerId, keyId, CacheStrategy.TEMPORARY,
                storedKey -> {
                    storedKey.setUnclaimedAmount(storedKey.getUnclaimedAmount() + absAmount);
                    return success;
                });
        }
    }

    public ActionResult removeKey(Player player, CrateKey key, int amount) {
        Identifier keyId = key.getId();
        int absAmount = Math.abs(amount);
        boolean virtual = key.getBase().isVirtual();

        if (virtual) {
            StoredKey storedKey = this.storageService.getCached(player.getUniqueId(), keyId).orElse(null);
            if (storedKey == null || storedKey.getVirtualBalance() < absAmount) {
                return ActionResult.fail(KeyLang.KEY_REMOVE_NOT_ENOUGH, ctx -> ctx
                    .with(CommonPlaceholders.GENERIC_AMOUNT, () -> NumberUtil.format(absAmount))
                );
            }

            storedKey.setVirtualBalance(Math.max(0, storedKey.getVirtualBalance() - absAmount));
            this.storageService.markDirty(storedKey);
        }
        else {
            this.removeFromInventory(player, key, absAmount);
        }

        return ActionResult.ok(KeyLang.KEY_REMOVE_FEEDBACK, ctx -> ctx
            .with(CommonPlaceholders.GENERIC_AMOUNT, () -> NumberUtil.format(absAmount))
        );
    }

    private void removeFromInventory(Player player, CrateKey key, int absAmount) {
        Identifier keyId = key.getId();

        int removed = 0;
        for (ItemStack itemStack : player.getInventory().getContents()) {
            if (itemStack == null || itemStack.getType().isAir()) continue;

            Identifier id = this.itemService.getKeyIdFromItem(itemStack);
            if (id == null || !id.equals(keyId)) continue;

            int toRemove = Math.min(itemStack.getAmount(), absAmount - removed);
            itemStack.setAmount(itemStack.getAmount() - toRemove);
            removed += toRemove;

            if (removed >= absAmount) {
                break;
            }
        }
    }

    public CompletableFuture<ActionResult> removeKeyAsync(UUID playerId, CrateKey key, int amount) {
        Identifier keyId = key.getId();
        int absAmount = Math.abs(amount);
        boolean virtual = key.getBase().isVirtual();

        ActionResult success = ActionResult.ok(KeyLang.KEY_REMOVE_FEEDBACK, ctx -> ctx
            .with(CommonPlaceholders.GENERIC_AMOUNT, () -> NumberUtil.format(absAmount))
        );

        ActionResult notEnough = ActionResult.fail(KeyLang.KEY_REMOVE_NOT_ENOUGH, ctx -> ctx
            .with(CommonPlaceholders.GENERIC_AMOUNT, () -> NumberUtil.format(absAmount))
        );

        if (virtual) {
            return this.storageService.updateDataIfExistsAsync(playerId, keyId, CacheStrategy.TEMPORARY, storedKey -> {
                if (storedKey == null || storedKey.getVirtualBalance() < absAmount) {
                    return notEnough;
                }

                storedKey.setVirtualBalance(Math.max(0, storedKey.getVirtualBalance() - absAmount));
                return success;
            });
        }
        else {
            Player player = Players.getPlayer(playerId);
            if (player != null) {
                return CompletableFuture.completedFuture(this.removeKey(player, key, amount));
            }

            return this.storageService.updateDataIfExistsAsync(playerId, keyId, CacheStrategy.TEMPORARY, storedKey -> {
                if (storedKey == null || storedKey.getUnclaimedAmount() < absAmount) {
                    return notEnough;
                }

                storedKey.setUnclaimedAmount(Math.max(0, storedKey.getUnclaimedAmount() - absAmount));
                return success;
            });
        }
    }
}
