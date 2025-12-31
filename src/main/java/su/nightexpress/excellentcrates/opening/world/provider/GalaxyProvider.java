package su.nightexpress.excellentcrates.opening.world.provider;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.crate.cost.Cost;
import su.nightexpress.excellentcrates.crate.impl.CrateSource;
import su.nightexpress.excellentcrates.opening.AbstractProvider;
import su.nightexpress.excellentcrates.opening.world.impl.GalaxyOpening;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;

public class GalaxyProvider extends AbstractProvider {

    private int duration = 140; // 7 seconds
    private double size = 3.0;  // Radius of the galaxy

    public GalaxyProvider(@NotNull CratesPlugin plugin, @NotNull String id) {
        super(plugin, id);
    }

    @Override
    public void load(@NotNull FileConfig config) {
        this.duration = ConfigValue.create("Settings.Duration_Ticks", this.duration).read(config);
        this.size = ConfigValue.create("Settings.Galaxy_Size", this.size).read(config);
    }

    @Override
    @NotNull
    public GalaxyOpening createOpening(@NotNull Player player, @NotNull CrateSource source, @Nullable Cost cost) {
        return new GalaxyOpening(this.plugin, player, source, cost, this.duration, this.size);
    }
}