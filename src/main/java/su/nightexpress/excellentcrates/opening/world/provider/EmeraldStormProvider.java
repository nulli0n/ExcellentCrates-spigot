package su.nightexpress.excellentcrates.opening.world.provider;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.crate.cost.Cost;
import su.nightexpress.excellentcrates.crate.impl.CrateSource;
import su.nightexpress.excellentcrates.opening.AbstractProvider;
import su.nightexpress.excellentcrates.opening.world.impl.EmeraldStormOpening;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;

public class EmeraldStormProvider extends AbstractProvider {

    private int duration = 120; // 6 seconds for a "Grand" feel
    private double radius = 1.5;

    public EmeraldStormProvider(@NotNull CratesPlugin plugin, @NotNull String id) {
        super(plugin, id);
    }

    @Override
    public void load(@NotNull FileConfig config) {
        this.duration = ConfigValue.create("Settings.Duration_Ticks", this.duration).read(config);
        this.radius = ConfigValue.create("Settings.Storm_Radius", this.radius).read(config);
    }

    @Override
    @NotNull
    public EmeraldStormOpening createOpening(@NotNull Player player, @NotNull CrateSource source, @Nullable Cost cost) {
        return new EmeraldStormOpening(this.plugin, player, source, cost, this.duration, this.radius);
    }
}