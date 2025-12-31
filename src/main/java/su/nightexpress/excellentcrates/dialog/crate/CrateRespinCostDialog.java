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

public class CrateRespinCostDialog extends Dialog<Crate> {

    private static final TextLocale TITLE = LangEntry.builder("Dialog.Crate.Respin.Cost.Title")
            .text(title("Crate", "Respin Cost"));

    private static final DialogElementLocale BODY = LangEntry.builder("Dialog.Crate.Respin.Cost.Body").dialogElement(400,
            "Set the cost for the Respin feature.",
            "Type a number (e.g. 1000.0) or 0 for free.",
            "",
            SOFT_YELLOW.wrap("→ ") + "This amount will be taken from the player's balance."
    );

    private static final TextLocale INPUT_COST = LangEntry.builder("Dialog.Crate.Respin.Cost.Input").text("Cost Amount");
    private static final String JSON_COST = "cost";

    @Override
    @NotNull
    public WrappedDialog create(@NotNull Player player, @NotNull Crate crate) {
        return Dialogs.create(builder -> {
            builder.base(DialogBases.builder(TITLE)
                    .body(DialogBodies.plainMessage(BODY))
                    .inputs(
                            // FIXED: Use 'text' instead of 'decimal'
                            DialogInputs.text(JSON_COST, INPUT_COST)
                                    .initial(String.valueOf(crate.getRespinCost()))
                                    .build()
                    )
                    .build()
            );

            builder.type(DialogTypes.multiAction(DialogButtons.ok()).exitAction(DialogButtons.back()).build());

            builder.handleResponse(DialogActions.OK, (viewer, identifier, nbtHolder) -> {
                if (nbtHolder == null) return;

                // FIXED: Parse the text to a double manually
                String input = nbtHolder.getText(JSON_COST, String.valueOf(crate.getRespinCost()));
                double cost;
                try {
                    cost = Double.parseDouble(input);
                } catch (NumberFormatException e) {
                    player.sendMessage("§cInvalid number! Cost reset to 0.");
                    cost = 0;
                }

                crate.setRespinCost(cost);
                crate.markDirty();
                viewer.callback();
            });
        });
    }
}