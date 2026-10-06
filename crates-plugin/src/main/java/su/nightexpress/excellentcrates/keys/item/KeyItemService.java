package su.nightexpress.excellentcrates.keys.item;

import java.util.Optional;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.id.IdentifierParser;
import su.nightexpress.excellentcrates.api.key.CrateKey;
import su.nightexpress.nightcore.util.ItemUtil;
import su.nightexpress.nightcore.util.PDCUtil;

@NullMarked
public final class KeyItemService {

    private final KeyItemFactory itemFactory;
    private final NamespacedKey  itemKey;

    public KeyItemService(KeyItemFactory itemFactory, NamespacedKey itemKey) {
        this.itemFactory = itemFactory;
        this.itemKey = itemKey;
    }

    public Optional<ItemStack> createTaggedItem(CrateKey key) {
        return this.itemFactory.createDisplayItem(key).map(itemStack -> {
            ItemUtil.editMeta(itemStack, meta -> {
                PDCUtil.set(meta, this.itemKey, key.idString());

                // Aply max_stack_size here to override that value of the original key item, 
                // so that the key item is stackable or not depending on the key's configuration.
                if (key.getItem().isStackable()) {
                    // Only override original max_stack_size if it is 1 or less, otherwise leave it as is.
                    // This ensures custom max stack sizes are respected.
                    if (meta.hasMaxStackSize() && meta.getMaxStackSize() <= 1) {
                        meta.setMaxStackSize(itemStack.getType().getMaxStackSize());
                    }
                }
                else {
                    meta.setMaxStackSize(1);
                }
            });
            return itemStack;
        });
    }

    public @Nullable Identifier getKeyIdFromItem(ItemStack item) {
        String idRaw = PDCUtil.getString(item, this.itemKey).orElse(null);
        if (idRaw == null) {
            return null;
        }

        return IdentifierParser.parse(idRaw).orElse(null);
    }

    public boolean isKeyItem(ItemStack item) {
        return this.getKeyIdFromItem(item) != null;
    }
}
