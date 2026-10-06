package su.nightexpress.excellentcrates.animation.data;

import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import su.nightexpress.excellentcrates.animation.data.io.AnimationIOService;
import su.nightexpress.excellentcrates.api.animation.AnimationRegistry;

@NullMarked
public class AnimationDataService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnimationDataService.class);

    private final AnimationRegistry  registry;
    private final AnimationIOService ioService;

    public AnimationDataService(AnimationRegistry registry, AnimationIOService ioService) {
        this.registry = registry;
        this.ioService = ioService;
    }

    public void loadInstantiators() {
        this.registry.getProviders().forEach(provider -> {
            this.ioService.loadProfiles(provider).forEach(config -> {
                this.registry.registerProfile(config);
            });
        });

        LOGGER.info("Loaded {} animation profiles.", this.registry.getProfiles().size());
    }
}
