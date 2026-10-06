package su.nightexpress.excellentcrates.api.key.balance;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.key.CrateKey;

@NullMarked
public interface KeyBalanceAPI {

    boolean hasKey(Player player, CrateKey key, int amount);

    int countKeys(Player player, CrateKey key);

    int countPhysicalKeys(Player player, CrateKey key);

    int countPhysicalKeys(Player player, Identifier keyId);

    int countVirtualKeys(Player player, CrateKey key);

    int countVirtualKeys(Player player, Identifier keyId);

    int countUnclaimedKeys(Player player, CrateKey key);

    int countUnclaimedKeys(Player player, Identifier keyId);

    IKeyBalance getKeyBalance(Player player, CrateKey key);

    CompletableFuture<IKeyBalance> getKeyBalanceAsync(UUID playerId, CrateKey key);

    ActionResult redeemKey(Player player, CrateKey key);

    ActionResult addKey(Player player, CrateKey key, int amount);

    CompletableFuture<ActionResult> giveKeyAsync(UUID playerId, CrateKey key, int amount);

    ActionResult removeKey(Player player, CrateKey key, int amount);

    CompletableFuture<ActionResult> removeKeyAsync(UUID playerId, CrateKey key, int amount);
}
