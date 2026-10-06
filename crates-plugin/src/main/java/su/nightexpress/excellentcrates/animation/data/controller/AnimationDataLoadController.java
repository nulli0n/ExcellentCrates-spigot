package su.nightexpress.excellentcrates.animation.data.controller;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.BasePluginComponent;
import su.nightexpress.excellentcrates.animation.data.AnimationDataService;
import su.nightexpress.excellentcrates.api.animation.AnimationRegistry;

@NullMarked
public class AnimationDataLoadController extends BasePluginComponent {

    private final AnimationRegistry    animations;
    private final AnimationDataService dataService;

    public AnimationDataLoadController(AnimationRegistry animations, AnimationDataService dataService) {
        super();
        this.animations = animations;
        this.dataService = dataService;
    }

    @Override
    protected void onReload() {
        this.animations.clearProfiles();
        this.dataService.loadInstantiators();
    }

    @Override
    protected void onShutdown() {
        this.animations.clear();
    }

    @Override
    protected void onStart() {
        this.dataService.loadInstantiators();
    }
}
