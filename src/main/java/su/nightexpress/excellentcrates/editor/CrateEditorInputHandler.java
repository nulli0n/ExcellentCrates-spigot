package su.nightexpress.excellentcrates.editor;

import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.editor.EditorInputHandler;
import su.nightexpress.excellentcrates.ExcellentCrates;

public abstract class CrateEditorInputHandler<T> implements EditorInputHandler<CrateEditorType, T> {

    protected ExcellentCrates plugin;

    public CrateEditorInputHandler(@NotNull ExcellentCrates plugin) {
        this.plugin = plugin;
    }

}
