package su.nightexpress.excellentcrates.animation.data;

import java.nio.file.Path;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bootstrap.context.NamedBootstrapContext;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.animation.data.controller.AnimationDataLoadController;
import su.nightexpress.excellentcrates.animation.data.io.AnimationIOService;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.animation.AnimationRegistry;

@NullMarked
public class AnimationDataBootstrapContext extends NamedBootstrapContext {

    private static final Identifier ID   = new Identifier("animations.data");
    private static final String     NAME = "IO/Data";

    private static final String DIR_ANIMATIONS = "animations";

    public final AnimationIOService   ioService;
    public final AnimationDataService dataService;

    public AnimationDataBootstrapContext(CratesPlugin plugin, AnimationRegistry registry) {
        super(ID, NAME);

        Path animationsPath = plugin.objectsPath().resolve(DIR_ANIMATIONS);

        this.ioService = new AnimationIOService(animationsPath);
        this.dataService = new AnimationDataService(registry, this.ioService);

        this.addComponent(new AnimationDataLoadController(registry, dataService));
    }
}
