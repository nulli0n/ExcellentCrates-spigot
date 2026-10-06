package su.nightexpress.excellentcrates.crates.open.component.extension;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.component.CrateComponentKeys;
import su.nightexpress.excellentcrates.api.crate.data.extension.CrateDataExtension;
import su.nightexpress.excellentcrates.api.crate.data.model.ICrateBuilder;
import su.nightexpress.excellentcrates.api.crate.open.OpenActionsComponent;
import su.nightexpress.excellentcrates.crates.open.component.DefaultOpeningComponent;
import su.nightexpress.excellentcrates.crates.open.component.codec.OpeningComponentCodec;
import su.nightexpress.nightcore.config.FileConfig;

@NullMarked
public class CrateOpeningComponentDataExtension implements CrateDataExtension {

    @Override
    public void onBuild(ICrateBuilder builder, Identifier crateId) {
        builder.component(CrateComponentKeys.OPEN_ACTIONS, DefaultOpeningComponent.createDefault());
    }

    @Override
    public void onCreate(Crate crate) {

    }

    @Override
    public void onDelete(Crate crate) {

    }

    @Override
    public void onLoad(Crate crate) {

    }

    @Override
    public void onRead(FileConfig config, ICrateBuilder builder, Identifier crateId) {
        OpenActionsComponent component = config.getOrSet("opening",
            OpeningComponentCodec.INSTANCE,
            DefaultOpeningComponent.createDefault()
        );
        builder.component(CrateComponentKeys.OPEN_ACTIONS, component);
    }

    @Override
    public void onUnload(Crate crate) {

    }

    @Override
    public void onWrite(FileConfig config, Crate crate) {
        OpenActionsComponent component = crate.getComponentOrNull(CrateComponentKeys.OPEN_ACTIONS);
        config.set("opening", component);
    }
}
