package su.nightexpress.excellentcrates.crates.item;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.id.IdentifierParser;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.item.ICrateItemFactory;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;
import su.nightexpress.nightcore.util.ItemUtil;
import su.nightexpress.nightcore.util.PDCUtil;

@NullMarked
public class CrateItemService {

    private final ICrateItemFactory itemFactory;
    private final AdaptedKey        itemKey;

    public CrateItemService(ICrateItemFactory itemFactory, AdaptedKey itemKey) {
        this.itemFactory = itemFactory;
        this.itemKey = itemKey;
    }

    public ItemStack createTaggedItem(Crate crate) {
        ItemStack itemStack = this.itemFactory.createDisplayItem(crate);
        ItemUtil.editMeta(itemStack, meta -> {
            PDCUtil.set(meta, this.itemKey.bukkit(), crate.idString());

            // Aply max_stack_size here to override that value of the original key item, 
            // so that the key item is stackable or not depending on the key's configuration.
            if (crate.getItem().isStackable()) {
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
    }

    public @Nullable Identifier getCrateId(ItemStack item) {
        String idRaw = PDCUtil.getString(item, this.itemKey.bukkit()).orElse(null);
        if (idRaw == null) {
            return null;
        }

        return IdentifierParser.parse(idRaw).orElse(null);
    }

    public boolean isCrateItem(ItemStack item) {
        return this.getCrateId(item) != null;
    }

    public boolean isCrateItem(ItemStack item, Crate crate) {
        Identifier itemCrateId = this.getCrateId(item);
        return itemCrateId != null && itemCrateId.equals(crate.id());
    }
}
