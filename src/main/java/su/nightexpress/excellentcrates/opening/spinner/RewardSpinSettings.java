package su.nightexpress.excellentcrates.opening.spinner;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.util.wrapper.UniSound;

import java.util.HashSet;
import java.util.Set;

public class RewardSpinSettings extends SpinSettings {

    private final Set<String> rarities;

    public RewardSpinSettings(int spinTimes,
                              long spinTickInterval,
                              long spinSlowdownStep,
                              long spinSlowdownAmount,
                              @Nullable UniSound spinSound,
                              @NotNull Set<String> rarities) {
        super(spinTimes, spinTickInterval, spinSlowdownStep, spinSlowdownAmount, spinSound);
        this.rarities = new HashSet<>(rarities);
    }

    @NotNull
    public static RewardSpinSettings read(@NotNull FileConfig cfg, @NotNull String path, @NotNull String id) {
        SpinSettings settings = SpinSettings.read(cfg, path);
        Set<String> rarities = ConfigValue.create(path + ".Rarities", Set.of(Placeholders.WILDCARD)).read(cfg);

        return new RewardSpinSettings(
            settings.getSpinTimes(),
            settings.getSpinTickInterval(),
            settings.getSpinSlowdownStep(),
            settings.getSpinSlowdownAmount(),
            settings.getSpinSound(),
            rarities);
    }

    @NotNull
    public Set<String> getRarities() {
        return rarities;
    }
}
