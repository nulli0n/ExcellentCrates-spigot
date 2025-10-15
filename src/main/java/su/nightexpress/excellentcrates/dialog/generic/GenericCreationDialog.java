package su.nightexpress.excellentcrates.dialog.generic;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.dialog.CrateDialog;
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;
import su.nightexpress.nightcore.locale.entry.TextLocale;
import su.nightexpress.nightcore.ui.dialog.Dialogs;
import su.nightexpress.nightcore.ui.dialog.build.*;
import su.nightexpress.nightcore.util.Strings;

public abstract class GenericCreationDialog<T> extends CrateDialog<T> {

    private static final String INPUT_ID = "id";

    @NotNull
    protected abstract TextLocale title();

    protected abstract boolean canCreate(@NotNull T source, @NotNull String id);

    protected abstract void create(@NotNull T source, @NotNull String id);

    @Override
    @NotNull
    public WrappedDialog create(@NotNull Player player, @NotNull T source) {
        return Dialogs.create(builder -> {
            builder.base(DialogBases.builder(this.title())
                .body(DialogBodies.plainMessage(Lang.DIALOG_GENERIC_CREATION_BODY))
                .inputs(DialogInputs.text(INPUT_ID, "").labelVisible(false).build())
                .build()
            );

            builder.type(DialogTypes.multiAction(DialogButtons.ok())
                .exitAction(DialogButtons.back())
                .build()
            );

            builder.handleResponse(DialogActions.OK, (user, identifier, nbtHolder) -> {
                if (nbtHolder == null) return;

                String name = nbtHolder.getText(INPUT_ID).orElse(null);
                if (name == null) return;

                String id = Strings.varStyle(name).orElse(null);
                if (id == null || !this.canCreate(source, id)) return;

                this.create(source, id);
                user.callback();
            });
        });
    }
}
