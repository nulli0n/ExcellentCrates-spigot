package su.nightexpress.excellentcrates.crate.task;

import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.server.AbstractTask;
import su.nightexpress.excellentcrates.ExcellentCratesPlugin;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.crate.impl.Crate;

public class HologramUpdateTask extends AbstractTask<ExcellentCratesPlugin> {

    public HologramUpdateTask(@NotNull ExcellentCratesPlugin plugin) {
        super(plugin, Config.CRATE_HOLOGRAM_UPDATE_INTERVAL.get(), true);
    }

    @Override
    public void action() {
        this.plugin.getCrateManager().getCrates().forEach(Crate::updateHologram);
    }
}
