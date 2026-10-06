package su.nightexpress.excellentcrates.effect.data;

import java.nio.file.Path;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bootstrap.context.NamedBootstrapContext;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.effect.EffectRegistry;
import su.nightexpress.excellentcrates.effect.data.codec.EffectBaseSettingsCodec;
import su.nightexpress.excellentcrates.effect.data.controller.EffectDataLoadController;
import su.nightexpress.excellentcrates.effect.data.io.EffectIOService;
import su.nightexpress.excellentcrates.effect.data.model.DefaultEffectBaseSettings;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;

@NullMarked
public class EffectDataBootstrapContext extends NamedBootstrapContext {

    private static final Identifier ID   = new Identifier("effects.data");
    private static final String     NAME = "IO/Data";

    private static final String EFFECTS_DIRECTORY = "effects";

    public final EffectIOService   ioService;
    public final EffectDataService dataService;

    public EffectDataBootstrapContext(CratesPlugin plugin, EffectRegistry registry) {
        super(ID, NAME);

        ConfigCodecs.register(DefaultEffectBaseSettings.class, EffectBaseSettingsCodec.INSTANCE);

        Path effectsDirectory = plugin.objectsPath().resolve(EFFECTS_DIRECTORY);

        this.ioService = new EffectIOService(effectsDirectory);
        this.dataService = new EffectDataService(registry, this.ioService);

        this.addComponent(new EffectDataLoadController(registry, dataService));
    }
}
