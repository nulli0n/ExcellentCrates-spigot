package su.nightexpress.excellentcrates.opening.inventory.spinner.impl;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.opening.inventory.InventoryOpening;
import su.nightexpress.excellentcrates.opening.inventory.spinner.AbstractSpinner;
import su.nightexpress.excellentcrates.opening.inventory.spinner.SpinnerData;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.random.Rnd;
import su.nightexpress.nightcore.util.random.WeightedItem;

import java.util.ArrayList;
import java.util.List;

public class AnimationSpinner extends AbstractSpinner {

    private final List<WeightedItem<NightItem>> items;

    public AnimationSpinner(@NotNull SpinnerData data, @NotNull InventoryOpening opening, @NotNull List<WeightedItem<NightItem>> items) {
        super(data, opening);
        this.items = items;
    }

    @Override
    protected void onStop() {

    }

    @Override
    @NotNull
    public ItemStack createItem() {
        return this.items.isEmpty() ? new ItemStack(Material.AIR) : Rnd.getByWeight(new ArrayList<>(this.items)).getItemStack();
    }
}
