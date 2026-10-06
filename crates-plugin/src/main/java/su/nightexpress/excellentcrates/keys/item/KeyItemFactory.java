package su.nightexpress.excellentcrates.keys.item;

import java.util.Optional;
import java.util.function.Consumer;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.key.CrateKey;
import su.nightexpress.excellentcrates.api.key.data.model.KeyDisplay;
import su.nightexpress.excellentcrates.api.key.data.model.KeyItem;
import su.nightexpress.excellentcrates.api.key.placeholder.KeyPlaceholders;
import su.nightexpress.excellentcrates.util.ItemHelper;
import su.nightexpress.nightcore.bridge.item.AdaptedItem;
import su.nightexpress.nightcore.util.ItemUtil;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext;

@NullMarked
public final class KeyItemFactory {

    private final KeyPlaceholders placeholders;

    public KeyItemFactory(KeyPlaceholders placeholders) {
        this.placeholders = placeholders;
    }

    public NightItem createDisplayIcon(CrateKey key) {
        return this.createDisplayIcon(key, ctx -> {
            // Basic display icon creation without extra placeholders
        });
    }

    public NightItem createDisplayIcon(CrateKey key, Consumer<PlaceholderContext.Builder> extra) {
        ItemStack itemStack = this.createBaseItem(key).orElse(ItemHelper.createPlaceholder());

        return NightItem.fromItemStack(itemStack)
            .replace(ctx -> ctx
                .apply(this.placeholders.allPlaceholders(key))
                .apply(extra)
            );
    }

    public boolean hasValidItem(CrateKey key) {
        return this.createBaseItem(key).isPresent();
    }

    public Optional<ItemStack> createDisplayItem(CrateKey key) {
        return this.createBaseItem(key).map(itemStack -> {
            if (key.getItem().isInheritDisplaySettings()) {
                KeyDisplay display = key.getDisplay();
                ItemUtil.editMeta(itemStack, meta -> {
                    ItemUtil.setCustomName(meta, display.getName());
                    ItemUtil.setLore(meta, display.getLore());
                });
            }

            return itemStack;
        });
    }

    public Optional<ItemStack> createBaseItem(CrateKey key) {
        KeyItem keyItem = key.getItem();
        AdaptedItem item = keyItem.getItem();

        return item.itemStack();
    }
}
