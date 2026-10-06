package su.nightexpress.excellentcrates.crates.item.factory;

import java.util.Optional;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.data.model.ICrateDisplay;
import su.nightexpress.excellentcrates.api.crate.data.model.ICrateItem;
import su.nightexpress.excellentcrates.api.crate.item.ICrateItemFactory;
import su.nightexpress.excellentcrates.util.ItemHelper;
import su.nightexpress.nightcore.util.ItemUtil;
import su.nightexpress.nightcore.util.bukkit.NightItem;

@NullMarked
public class CrateItemFactory implements ICrateItemFactory {

    @Override
    public boolean hasValidItem(Crate crate) {
        return this.baseItem(crate).isPresent();
    }

    @Override
    public NightItem renderCrateIcon(Crate crate) {
        return NightItem.fromItemStack(this.createDisplayItem(crate));
    }

    @Override
    public ItemStack createDisplayItem(Crate crate) {
        ICrateItem crateItem = crate.getItem();
        ItemStack itemStack = this.createBaseItem(crate);

        if (crateItem.isUseDisplay()) {
            ICrateDisplay display = crate.getDisplay();
            ItemUtil.editMeta(itemStack, meta -> {
                ItemUtil.setCustomName(meta, display.getName());
                ItemUtil.setLore(meta, display.getLore());
            });
        }

        return itemStack;
    }

    @Override
    public ItemStack createBaseItem(Crate crate) {
        return this.baseItem(crate).orElse(ItemHelper.createPlaceholder());
    }

    @Override
    public Optional<ItemStack> baseItem(Crate crate) {
        ICrateItem crateItem = crate.getItem();
        return crateItem.getItem().itemStack();
    }
}
