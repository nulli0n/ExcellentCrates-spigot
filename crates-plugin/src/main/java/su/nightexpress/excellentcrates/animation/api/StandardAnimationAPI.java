package su.nightexpress.excellentcrates.animation.api;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.animation.AnimationAPI;
import su.nightexpress.excellentcrates.api.animation.AnimationRegistry;
import su.nightexpress.excellentcrates.api.animation.session.AnimationSessionAPI;

@NullMarked
public class StandardAnimationAPI implements AnimationAPI {

    private final AnimationRegistry   registry;
    private final AnimationSessionAPI sessionAPI;

    public StandardAnimationAPI(AnimationRegistry registry, AnimationSessionAPI sessionAPI) {
        this.registry = registry;
        this.sessionAPI = sessionAPI;
    }

    @Override
    public AnimationRegistry getRegistry() {
        return this.registry;
    }

    @Override
    public AnimationSessionAPI getSessionAPI() {
        return this.sessionAPI;
    }
}
