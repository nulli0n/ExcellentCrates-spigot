package su.nightexpress.excellentcrates.crates.editor;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.registry.TinyRegistry;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorExtension;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorAPI;

@NullMarked
public class DefaultEditorAPI implements CrateEditorAPI {

    private final TinyRegistry<CrateEditorExtension> extensions;

    public DefaultEditorAPI(TinyRegistry<CrateEditorExtension> extensions) {
        this.extensions = extensions;
    }

    @Override
    public void registerExtension(CrateEditorExtension extension) {
        this.extensions.register(extension);
    }
}
