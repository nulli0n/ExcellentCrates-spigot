package su.nightexpress.excellentcrates.effect.data;

import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import su.nightexpress.excellentcrates.api.effect.EffectRegistry;
import su.nightexpress.excellentcrates.effect.data.io.EffectIOService;

@NullMarked
public class EffectDataService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EffectDataService.class);

    private final EffectRegistry  registry;
    private final EffectIOService ioService;

    public EffectDataService(EffectRegistry registry, EffectIOService ioService) {
        this.registry = registry;
        this.ioService = ioService;
    }

    public void loadProfiles() {
        this.registry.getModels().forEach(model -> {
            this.ioService.loadProfiles(model).forEach(profile -> {
                this.registry.registerProfile(profile);
                LOGGER.info("Loaded profile '{}' from '{}' model", profile.key(), model.getId());
            });
        });

        LOGGER.info("Loaded {} profile(s) from {} model(s)",
            this.registry.getProfiles().size(),
            this.registry.getModels().size()
        );
    }
}
