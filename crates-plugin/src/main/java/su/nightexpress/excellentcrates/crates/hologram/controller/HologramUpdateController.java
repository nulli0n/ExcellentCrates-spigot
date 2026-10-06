package su.nightexpress.excellentcrates.crates.hologram.controller;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.BaseController;
import su.nightexpress.engine.settings.ReadOnlySettings;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.crates.hologram.HologramDisplayService;
import su.nightexpress.excellentcrates.crates.hologram.settings.HologramSettings;

@NullMarked
public class HologramUpdateController extends BaseController {

    private final HologramDisplayService             displayService;
    private final ReadOnlySettings<HologramSettings> settings;

    public HologramUpdateController(CratesPlugin plugin,
                                    HologramDisplayService displayService,
                                    ReadOnlySettings<HologramSettings> settings) {
        super(plugin);
        this.displayService = displayService;
        this.settings = settings;
    }

    @Override
    protected void onControllerReload() {
        this.displayService.removeAll();
        this.stopTasks();
        this.startHologramUpdateTask();
    }

    @Override
    protected void onControllerShutdown() {
        this.displayService.removeAll();
    }

    @Override
    protected void onControllerStart() {
        this.startHologramUpdateTask();
    }

    private void startHologramUpdateTask() {
        this.addTickTask(this::updateHolograms, this.settings.get().updateInterval());
    }

    private void updateHolograms() {
        this.displayService.updateHolograms();
    }
}
