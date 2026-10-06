package su.nightexpress.excellentcrates.api.key.editor;

import org.jspecify.annotations.NullMarked;

@NullMarked
public interface KeyEditorAPI {

    void registerExtension(KeyEditorExtension extension);
}
