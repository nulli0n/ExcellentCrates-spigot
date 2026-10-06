package su.nightexpress.excellentcrates.effect.instance.controller;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.BaseController;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.effect.instance.EffectInstanceService;

@NullMarked
public class EffectInstanceTickController extends BaseController {

    private final EffectInstanceService instanceService;

    public EffectInstanceTickController(CratesPlugin plugin, EffectInstanceService instanceService) {
        super(plugin);
        this.instanceService = instanceService;
    }

    @Override
    protected void onControllerReload() {

    }

    @Override
    protected void onControllerShutdown() {

    }

    @Override
    protected void onControllerStart() {
        this.addTickTask(this::run, 1L);
    }

    private void run() {
        this.instanceService.tick();
    }
}
