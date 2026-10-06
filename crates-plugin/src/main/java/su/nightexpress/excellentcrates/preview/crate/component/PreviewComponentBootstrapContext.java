package su.nightexpress.excellentcrates.preview.crate.component;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.preview.crate.component.codec.PreviewComponentCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;

@NullMarked
public final class PreviewComponentBootstrapContext {

    private final PreviewComponentExtension dataExtension;

    public PreviewComponentBootstrapContext() {
        ConfigCodecs.register(DefaultPreviewComponent.class, PreviewComponentCodec.INSTANCE);

        this.dataExtension = new PreviewComponentExtension();
    }

    public PreviewComponentExtension getDataExtension() {
        return this.dataExtension;
    }
}
