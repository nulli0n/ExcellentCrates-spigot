package su.nightexpress.excellentcrates.opening.inventory.spinner.provider;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.opening.inventory.InventoryOpening;
import su.nightexpress.excellentcrates.opening.inventory.spinner.SpinnerProvider;
import su.nightexpress.excellentcrates.opening.inventory.spinner.SpinnerData;
import su.nightexpress.excellentcrates.opening.inventory.spinner.impl.AnimationSpinner;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.config.Writeable;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.random.WeightedItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AnimationProvider implements SpinnerProvider, Writeable {

    private final Map<String, WeightedItem<NightItem>> itemMap;

    public AnimationProvider(@NotNull Map<String, WeightedItem<NightItem>> itemMap) {
        this.itemMap = new HashMap<>(itemMap);
    }

    @NotNull
    public static AnimationProvider read(@NotNull FileConfig config, @NotNull String path) {
        Map<String, WeightedItem<NightItem>> itemsMap = new HashMap<>();
        config.getSection(path + ".Items").forEach(sId -> {
            double weight = config.getDouble(path + ".Items." + sId + ".Chance", 100D);
            NightItem item = config.getCosmeticItem(path + ".Items." + sId);
            itemsMap.put(sId.toLowerCase(), new WeightedItem<>(item, weight));
        });

        return new AnimationProvider(itemsMap);
    }

    @Override
    public void write(@NotNull FileConfig config, @NotNull String path) {
        config.remove(path + ".Items");

        this.itemMap.forEach((id, witem) -> {
            config.set(path + ".Items." + id + ".Chance", witem.getWeight());
            config.set(path + ".Items." + id, witem.getItem());
        });
    }

    @Override
    @NotNull
    public AnimationSpinner createSpinner(@NotNull CratesPlugin plugin, @NotNull SpinnerData data, @NotNull InventoryOpening opening) {
        return new AnimationSpinner(data, opening, new ArrayList<>(this.itemMap.values()));
    }

    @NotNull
    public Map<String, WeightedItem<NightItem>> getItemMap() {
        return this.itemMap;
    }
}
