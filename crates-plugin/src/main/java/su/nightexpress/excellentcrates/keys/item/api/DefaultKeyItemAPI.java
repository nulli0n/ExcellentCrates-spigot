package su.nightexpress.excellentcrates.keys.item.api;

import java.util.Optional;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.key.CrateKey;
import su.nightexpress.excellentcrates.api.key.item.KeyItemAPI;
import su.nightexpress.excellentcrates.keys.item.KeyItemFactory;
import su.nightexpress.excellentcrates.keys.item.KeyItemService;
import su.nightexpress.nightcore.util.bukkit.NightItem;

@NullMarked
public class DefaultKeyItemAPI implements KeyItemAPI {

    private final KeyItemFactory itemFactory;
    private final KeyItemService itemService;

    public DefaultKeyItemAPI(KeyItemFactory itemFactory, KeyItemService itemService) {
        this.itemFactory = itemFactory;
        this.itemService = itemService;
    }

    @Override
    public NightItem createDisplayIcon(CrateKey key) {
        return itemFactory.createDisplayIcon(key);
    }

    @Override
    public Optional<ItemStack> createDisplayItem(CrateKey key) {
        return itemFactory.createDisplayItem(key);
    }

    @Override
    public Optional<ItemStack> createBaseItem(CrateKey key) {
        return itemFactory.createBaseItem(key);
    }

    @Override
    public Optional<ItemStack> createTaggedItem(CrateKey key) {
        return itemService.createTaggedItem(key);
    }

    @Override
    public @Nullable Identifier getKeyIdFromItem(ItemStack item) {
        return itemService.getKeyIdFromItem(item);
    }

    @Override
    public boolean isKeyItem(ItemStack item) {
        return itemService.isKeyItem(item);
    }
}
