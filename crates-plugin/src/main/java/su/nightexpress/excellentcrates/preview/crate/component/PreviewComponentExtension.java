package su.nightexpress.excellentcrates.preview.crate.component;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.component.CrateComponentKeys;
import su.nightexpress.excellentcrates.api.crate.data.extension.CrateDataExtension;
import su.nightexpress.excellentcrates.api.crate.data.model.ICrateBuilder;
import su.nightexpress.excellentcrates.api.preview.crate.PreviewComponent;
import su.nightexpress.nightcore.config.FileConfig;

@NullMarked
public class PreviewComponentExtension implements CrateDataExtension {

    @Override
    public void onBuild(ICrateBuilder builder, Identifier crateId) {
        builder.component(CrateComponentKeys.PREVIEW, DefaultPreviewComponent.defaults());
    }

    @Override
    public void onRead(FileConfig config, ICrateBuilder builder, Identifier crateId) {
        DefaultPreviewComponent component = config.get("preview", DefaultPreviewComponent.class);
        if (component != null) {
            builder.component(CrateComponentKeys.PREVIEW, component);
        }
    }

    @Override
    public void onWrite(FileConfig config, Crate crate) {
        PreviewComponent component = crate.getComponentOrNull(CrateComponentKeys.PREVIEW);
        config.set("preview", component);
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
    public void onUnload(Crate crate) {

    }
}
