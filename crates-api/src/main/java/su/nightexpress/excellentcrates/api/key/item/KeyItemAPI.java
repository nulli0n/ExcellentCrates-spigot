package su.nightexpress.excellentcrates.api.key.item;

import java.util.Optional;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.key.CrateKey;
import su.nightexpress.nightcore.util.bukkit.NightItem;

@NullMarked
public interface KeyItemAPI {

    Optional<ItemStack> createTaggedItem(CrateKey key);

    @Nullable
    Identifier getKeyIdFromItem(ItemStack item);

    boolean isKeyItem(ItemStack item);

    NightItem createDisplayIcon(CrateKey key);

    Optional<ItemStack> createDisplayItem(CrateKey key);

    Optional<ItemStack> createBaseItem(CrateKey key);
}
