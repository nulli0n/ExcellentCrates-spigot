package su.nightexpress.excellentcrates.api.crate.editor;

import org.jspecify.annotations.NullMarked;

@NullMarked
public interface CrateEditorAPI {

    void registerExtension(CrateEditorExtension extension);
}
