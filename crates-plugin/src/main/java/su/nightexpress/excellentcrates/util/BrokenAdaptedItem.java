package su.nightexpress.excellentcrates.util;

import java.util.Optional;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.nightcore.bridge.item.AdaptedItem;
import su.nightexpress.nightcore.bridge.item.ItemAdapter;
import su.nightexpress.nightcore.integration.item.adapter.impl.VanillaItemAdapter;

@NullMarked
public class BrokenAdaptedItem implements AdaptedItem {

    public static final BrokenAdaptedItem INSTANCE = new BrokenAdaptedItem();

    @Override
    public ItemAdapter<?> getAdapter() {
        return VanillaItemAdapter.INSTANCE;
    }

    @Override
    public int getAmount() {
        return 1;
    }

    @Override
    public @Nullable ItemStack getItemStack() {
        return ItemHelper.createPlaceholder();
    }

    @Override
    public boolean isSimilar(ItemStack other) {
        return false;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public Optional<ItemStack> itemStack() {
        return Optional.of(this.getItemStack());
    }
}
