package su.nightexpress.excellentcrates.opening.spinner;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.opening.inventory.InventoryOpening;
import su.nightexpress.excellentcrates.opening.inventory.InventorySpinner;
import su.nightexpress.nightcore.util.random.Rnd;

import java.util.HashMap;
import java.util.Map;

public class AnimationSpinner extends InventorySpinner {

    private final AnimationSpinSettings settings;

    public AnimationSpinner(@NotNull CratesPlugin plugin,
                            @NotNull String id,
                            @NotNull AnimationSpinSettings settings,
                            @NotNull InventoryOpening opening,
                            @NotNull SpinMode mode, int[] slots) {
        super(plugin, id, opening, mode, slots);
        this.settings = settings;
    }

    @Override
    @NotNull
    protected AnimationSpinSettings getSettings() {
        return this.settings;
    }

    @Override
    protected void onStop() {

    }

    @Override
    @NotNull
    public ItemStack createItem() {
        Map<ItemStack, Double> map = new HashMap<>();
        this.getSettings().getItemsMap().values().forEach(pair -> {
            map.put(pair.getFirst(), pair.getSecond());
        });

        if (map.isEmpty()) return new ItemStack(Material.AIR);

        return Rnd.getByWeight(map);
    }
}
