package su.nightexpress.excellentcrates.opening.world.provider;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.crate.cost.Cost;
import su.nightexpress.excellentcrates.crate.impl.CrateSource;
import su.nightexpress.excellentcrates.opening.AbstractProvider;
import su.nightexpress.excellentcrates.opening.world.impl.CosmicOpening;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;

public class CosmicProvider extends AbstractProvider {

    private int duration = 100; // 5 seconds
    private double radius = 1.2;
    private double riseSpeed = 0.05;

    public CosmicProvider(@NotNull CratesPlugin plugin, @NotNull String id) {
        super(plugin, id);
    }

    @Override
    public void load(@NotNull FileConfig config) {
        this.duration = ConfigValue.create("Settings.Duration_Ticks", this.duration).read(config);
        this.radius = ConfigValue.create("Settings.Spiral_Radius", this.radius).read(config);
        this.riseSpeed = ConfigValue.create("Settings.Rise_Speed", this.riseSpeed).read(config);
    }

    @Override
    @NotNull
    public CosmicOpening createOpening(@NotNull Player player, @NotNull CrateSource source, @Nullable Cost cost) {
        return new CosmicOpening(this.plugin, player, source, cost, this.duration, this.radius, this.riseSpeed);
    }
}