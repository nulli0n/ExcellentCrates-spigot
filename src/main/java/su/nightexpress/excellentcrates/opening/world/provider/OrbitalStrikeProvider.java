package su.nightexpress.excellentcrates.opening.world.provider;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.crate.cost.Cost;
import su.nightexpress.excellentcrates.crate.impl.CrateSource;
import su.nightexpress.excellentcrates.opening.AbstractProvider;
import su.nightexpress.excellentcrates.opening.world.impl.OrbitalStrikeOpening;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;

public class OrbitalStrikeProvider extends AbstractProvider {

    private int duration = 100; // 5 Seconds total
    private double beamHeight = 20.0; // How high the laser starts

    public OrbitalStrikeProvider(@NotNull CratesPlugin plugin, @NotNull String id) {
        super(plugin, id);
    }

    @Override
    public void load(@NotNull FileConfig config) {
        this.duration = ConfigValue.create("Settings.Duration_Ticks", this.duration).read(config);
        this.beamHeight = ConfigValue.create("Settings.Beam_Height", this.beamHeight).read(config);
    }

    @Override
    @NotNull
    public OrbitalStrikeOpening createOpening(@NotNull Player player, @NotNull CrateSource source, @Nullable Cost cost) {
        return new OrbitalStrikeOpening(this.plugin, player, source, cost, this.duration, this.beamHeight);
    }
}