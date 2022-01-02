package su.nightexpress.excellentcrates.key.editor.handler;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.api.crate.ICrateKey;
import su.nightexpress.excellentcrates.editor.CrateEditorInputHandler;
import su.nightexpress.excellentcrates.editor.CrateEditorType;

public class EditorHandlerKey extends CrateEditorInputHandler<ICrateKey> {

    public EditorHandlerKey(@NotNull ExcellentCrates plugin) {
        super(plugin);
    }

    @Override
    public boolean onType(@NotNull Player player, @NotNull ICrateKey key, @NotNull CrateEditorType type, @NotNull String msg) {
        switch (type) {
            case KEY_CHANGE_NAME -> key.setName(msg);
            default -> { }
        }

        key.save();
        return true;
    }
}
