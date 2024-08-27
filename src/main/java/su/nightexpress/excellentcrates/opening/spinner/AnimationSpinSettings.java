package su.nightexpress.excellentcrates.opening.spinner;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.util.Pair;
import su.nightexpress.nightcore.util.wrapper.UniSound;

import java.util.HashMap;
import java.util.Map;

public class AnimationSpinSettings extends SpinSettings {

    private final Map<String, Pair<ItemStack, Double>> itemsMap;

    public AnimationSpinSettings(int spinTimes,
                                 long spinTickInterval,
                                 long spinSlowdownStep,
                                 long spinSlowdownAmount,
                                 @Nullable UniSound spinSound,
                                 @NotNull Map<String, Pair<ItemStack, Double>> itemsMap) {
        super(spinTimes, spinTickInterval, spinSlowdownStep, spinSlowdownAmount, spinSound);
        this.itemsMap = new HashMap<>(itemsMap);
    }

    @NotNull
    public static AnimationSpinSettings read(@NotNull FileConfig cfg, @NotNull String path, @NotNull String id) {
        SpinSettings settings = SpinSettings.read(cfg, path);

        Map<String, Pair<ItemStack, Double>> itemsMap = new HashMap<>();
        cfg.getSection(path + ".Items").forEach(sId -> {
            double weight = cfg.getDouble(path + ".Items." + sId + ".Chance", 100D);
            ItemStack item = cfg.getItem(path + ".Items." + sId);
            itemsMap.put(sId.toLowerCase(), Pair.of(item, weight));
        });

        return new AnimationSpinSettings(
                settings.getSpinTimes(),
                settings.getSpinTickInterval(),
                settings.getSpinSlowdownStep(),
                settings.getSpinSlowdownAmount(),
                settings.getSpinSound(),
                itemsMap
        );
    }

    @NotNull
    public Map<String, Pair<ItemStack, Double>> getItemsMap() {
        return itemsMap;
    }
}
