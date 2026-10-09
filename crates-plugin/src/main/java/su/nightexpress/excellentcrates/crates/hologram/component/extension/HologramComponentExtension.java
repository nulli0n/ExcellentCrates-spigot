package su.nightexpress.excellentcrates.crates.hologram.component.extension;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.component.CrateComponentKeys;
import su.nightexpress.excellentcrates.api.crate.data.extension.CrateDataExtension;
import su.nightexpress.excellentcrates.api.crate.data.model.CrateBuilder;
import su.nightexpress.excellentcrates.api.crate.hologram.component.HologramComponent;
import su.nightexpress.excellentcrates.crates.hologram.HologramDisplayService;
import su.nightexpress.excellentcrates.crates.hologram.component.StandardHologramComponent;
import su.nightexpress.nightcore.config.FileConfig;

@NullMarked
public class HologramComponentExtension implements CrateDataExtension {

    private final HologramDisplayService displayService;

    public HologramComponentExtension(HologramDisplayService displayService) {
        this.displayService = displayService;
    }

    @Override
    public void onBuild(CrateBuilder builder, Identifier crateId) {
        builder.component(CrateComponentKeys.HOLOGRAM, StandardHologramComponent.createDefault());
    }

    @Override
    public void onRead(FileConfig config, CrateBuilder builder, Identifier crateId) {
        StandardHologramComponent component = config.getOrSet("hologram",
            StandardHologramComponent.class,
            StandardHologramComponent.createDefault()
        );

        builder.component(CrateComponentKeys.HOLOGRAM, component);
    }

    @Override
    public void onWrite(FileConfig config, Crate crate) {
        HologramComponent component = crate.getComponentOrNull(CrateComponentKeys.HOLOGRAM);
        config.set("hologram", component);
    }

    @Override
    public void onCreate(Crate crate) {

    }

    @Override
    public void onDelete(Crate crate) {

    }

    @Override
    public void onLoad(Crate crate) {
        this.displayService.update(crate);
    }

    @Override
    public void onUnload(Crate crate) {
        this.displayService.removeAll(crate);
    }
}
