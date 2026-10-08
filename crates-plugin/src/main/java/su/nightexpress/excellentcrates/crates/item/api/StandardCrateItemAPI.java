package su.nightexpress.excellentcrates.crates.item.api;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.item.CrateItemAPI;
import su.nightexpress.excellentcrates.crates.item.CrateItemService;

@NullMarked
public class StandardCrateItemAPI implements CrateItemAPI {

    private final CrateItemService itemService;

    public StandardCrateItemAPI(CrateItemService itemService) {
        this.itemService = itemService;
    }

    @Override
    public ItemStack createTaggedItem(Crate crate) {
        return itemService.createTaggedItem(crate);
    }

    @Override
    public @Nullable Identifier getCrateId(ItemStack item) {
        return itemService.getCrateId(item);
    }

    @Override
    public boolean isCrateItem(ItemStack item) {
        return itemService.isCrateItem(item);
    }

    @Override
    public boolean isCrateItem(ItemStack item, Crate crate) {
        return itemService.isCrateItem(item, crate);
    }
}
