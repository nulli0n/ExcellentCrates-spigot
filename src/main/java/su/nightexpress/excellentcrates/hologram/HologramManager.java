package su.nightexpress.excellentcrates.hologram;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.hologram.handler.HologramFancyHandler;
import su.nightexpress.excellentcrates.hologram.handler.HologramPacketsHandler;
import su.nightexpress.excellentcrates.hologram.handler.HologramProtocolHandler;
import su.nightexpress.excellentcrates.hologram.listener.HologramListener;
import su.nightexpress.excellentcrates.hooks.HookId;
import su.nightexpress.excellentcrates.util.pos.WorldPos;
import su.nightexpress.nightcore.manager.AbstractManager;
import su.nightexpress.nightcore.util.Plugins;

public class HologramManager extends AbstractManager<CratesPlugin> {

    private HologramHandler handler;

    public HologramManager(@NotNull CratesPlugin plugin) {
        super(plugin);
    }

    @Override
    protected void onLoad() {
        if (this.detectHandler()) {
            this.addListener(new HologramListener(this.plugin, this));
            this.addTask(this::tickHolograms, Config.CRATE_HOLOGRAM_UPDATE_INTERVAL.get());
        }
    }

    @Override
    protected void onShutdown() {
        if (this.handler != null) {
            try {
                this.plugin.getCrateManager().getCrates().forEach(this.handler::delete);
            }
            catch (Exception exception) {
                this.plugin.error("Could not properly remove crate holograms during shutdown!");
                exception.printStackTrace();
            }
        }
        this.handler = null;
    }

    private boolean detectHandler() {
        if (Plugins.isInstalled(HookId.FANCY_HOLOGRAMS)) {
            this.handler = new HologramFancyHandler(this.plugin);
        }
        else if (Plugins.isInstalled(HookId.PACKET_EVENTS)) {
            this.handler = new HologramPacketsHandler(this.plugin);
        }
        else if (Plugins.isInstalled(HookId.PROTOCOL_LIB)) {
            this.handler = new HologramProtocolHandler(this.plugin);
        }
        else {
            this.plugin.warn("*".repeat(25));
            this.plugin.warn("You have no hologram plugins installed for the Holograms feature to work.");
            this.plugin.warn("Please install one of the following plugins to enable crate holograms: " + HookId.FANCY_HOLOGRAMS + ", " + HookId.PACKET_EVENTS + " or " + HookId.PROTOCOL_LIB);
            this.plugin.warn("*".repeat(25));
        }

        return this.hasHandler();
    }

    private void tickHolograms() {
        if (this.handler == null) return;

        this.plugin.getCrateManager().getCrates().forEach(crate -> {
            if (!crate.isHologramEnabled()) return;

            try {
                this.handler.update(crate);
            }
            catch (Exception exception) {
                this.plugin.error("Could not update hologram for '" + crate.getId() + "' crate!");
                exception.printStackTrace();
            }
        });
    }

    public void render(@NotNull Crate crate) {
        if (this.handler == null) return;

        this.handler.create(crate);
    }

    public void discard(@NotNull Crate crate) {
        if (this.handler == null) return;

        this.handler.delete(crate);
    }

    public void enableBlockHologram(@NotNull Crate crate, @NotNull WorldPos blockPos) {
        if (this.handler == null) return;

        this.handler.toggle(crate, blockPos, true);
    }

    public void disableBlockHologram(@NotNull Crate crate, @NotNull WorldPos blockPos) {
        if (this.handler == null) return;

        this.handler.toggle(crate, blockPos, false);
    }

    public void removeForViewer(@NotNull Player player) {
        if (this.handler == null) return;

        this.handler.purge(player);
    }

    public boolean hasHandler() {
        return this.handler != null;
    }
}
