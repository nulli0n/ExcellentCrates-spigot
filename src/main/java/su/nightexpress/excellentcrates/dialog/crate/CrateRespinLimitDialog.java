package su.nightexpress.excellentcrates.dialog.crate;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.dialog.Dialog;
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.DialogElementLocale;
import su.nightexpress.nightcore.locale.entry.TextLocale;
import su.nightexpress.nightcore.ui.dialog.Dialogs;
import su.nightexpress.nightcore.ui.dialog.build.*;

import static su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers.*;

public class CrateRespinLimitDialog extends Dialog<Crate> {

    private static final TextLocale TITLE = LangEntry.builder("Dialog.Crate.Respin.Limit.Title")
            .text(title("Crate", "Respin Limit"));

    private static final DialogElementLocale BODY = LangEntry.builder("Dialog.Crate.Respin.Limit.Body").dialogElement(400,
            "Set the maximum number of times a player can respin.",
            "Type a whole number (e.g. 1, 3, 5).",
            "",
            SOFT_YELLOW.wrap("→ ") + "Set to a high number for unlimited."
    );

    private static final TextLocale INPUT_LIMIT = LangEntry.builder("Dialog.Crate.Respin.Limit.Input").text("Limit Amount");
    private static final String JSON_LIMIT = "limit";

    @Override
    @NotNull
    public WrappedDialog create(@NotNull Player player, @NotNull Crate crate) {
        return Dialogs.create(builder -> {
            builder.base(DialogBases.builder(TITLE)
                    .body(DialogBodies.plainMessage(BODY))
                    .inputs(
                            // FIXED: Use 'text' instead of 'integer'
                            DialogInputs.text(JSON_LIMIT, INPUT_LIMIT)
                                    .initial(String.valueOf(crate.getRespinLimit()))
                                    .build()
                    )
                    .build()
            );

            builder.type(DialogTypes.multiAction(DialogButtons.ok()).exitAction(DialogButtons.back()).build());

            builder.handleResponse(DialogActions.OK, (viewer, identifier, nbtHolder) -> {
                if (nbtHolder == null) return;

                // FIXED: Parse the text to an integer manually
                String input = nbtHolder.getText(JSON_LIMIT, String.valueOf(crate.getRespinLimit()));
                int limit;
                try {
                    limit = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    player.sendMessage("§cInvalid number! Limit reset to 1.");
                    limit = 1;
                }

                crate.setRespinLimit(limit);
                crate.markDirty();
                viewer.callback();
            });
        });
    }
}