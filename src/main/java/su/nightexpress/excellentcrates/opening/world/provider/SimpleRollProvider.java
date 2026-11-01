package su.nightexpress.excellentcrates.opening.world.provider;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.crate.cost.Cost;
import su.nightexpress.excellentcrates.crate.impl.CrateSource;
import su.nightexpress.excellentcrates.opening.AbstractProvider;
import su.nightexpress.excellentcrates.opening.world.impl.SimpleRollOpening;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.config.FileConfig;

public class SimpleRollProvider extends AbstractProvider {

    private int  spinsRequired = 15;
    private long spinInterval = 3;
    private long finishDelay  = 40;

    public SimpleRollProvider(@NotNull CratesPlugin plugin, @NotNull String id) {
        super(plugin, id);
    }

    @Override
    public void load(@NotNull FileConfig config) {
        this.spinsRequired = config.getOrSet("Settings.Steps_Amount", ConfigCodecs.INT, this.spinsRequired);
        this.spinInterval = config.getOrSet("Settings.Steps_Tick", ConfigCodecs.LONG, this.spinInterval);
        this.finishDelay = config.getOrSet("Settings.Complete_Pause", ConfigCodecs.LONG, this.finishDelay);
    }

    @Override
    @NotNull
    public SimpleRollOpening createOpening(@NotNull Player player, @NotNull CrateSource source, @Nullable Cost cost) {
        return new SimpleRollOpening(this.plugin, player, source, cost, this.spinsRequired, this.spinInterval, this.finishDelay);
    }

    public int getSpinsRequired() {
        return this.spinsRequired;
    }

    public void setSpinsRequired(int spinsRequired) {
        this.spinsRequired = Math.max(1, spinsRequired);
    }

    public long getSpinInterval() {
        return this.spinInterval;
    }

    public void setSpinInterval(long spinInterval) {
        this.spinInterval = Math.max(1, spinInterval);
    }

    public long getFinishDelay() {
        return this.finishDelay;
    }

    public void setFinishDelay(long finishDelay) {
        this.finishDelay = Math.max(0, finishDelay);
    }
}
