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

public class CrateOpeningLimitsDialog extends Dialog<Crate> {

    private static final TextLocale TITLE = LangEntry.builder("Dialog.Crate.OpeningCooldown.Title").text(title("Crate", "Opening Limits"));

    private static final DialogElementLocale BODY = LangEntry.builder("Dialog.Crate.OpeningCooldown.Body").dialogElement(400,
        "Here you can set the crate opening limits per player.",
        "",
        SOFT_YELLOW.wrap("Cooldown") + " sets the time period (in seconds) during which a player can open the crate the number of times specified in " + SOFT_YELLOW.wrap("Amount") + ".",
        "",
        "When this time period expires, the player's crate opening counter is reset, making the crate available again.",
        "",
        "The cooldown timer is activated with the first crate opened after the counter is reset.",
        "",
        SOFT_YELLOW.wrap("→") + " To make the crate one-timed (never reset), set " + SOFT_YELLOW.wrap("Cooldown") + " to " + SOFT_YELLOW.wrap("-1") + ".",
        "",
        SOFT_YELLOW.wrap("→") + " To disable this feature, uncheck the " + SOFT_YELLOW.wrap("Enabled") + " box."
    );

    private static final TextLocale INPUT_ENABLED  = LangEntry.builder("Dialog.Crate.Preview.Input.Enabled").text("Enabled");
    private static final TextLocale INPUT_COOLDOWN = LangEntry.builder("Dialog.Crate.OpeningCooldown.Input.Cooldown").text("Cooldown " + GRAY.wrap("(in seconds)"));
    private static final TextLocale INPUT_AMOUNT   = LangEntry.builder("Dialog.Crate.OpeningCooldown.Input.Amount").text("Amount " + GRAY.wrap("(min. 1)"));

    private static final String JSON_ENABLED  = "enabled";
    private static final String JSON_COOLDOWN = "cooldown";
    private static final String JSON_AMOUNT   = "amount";

    @Override
    @NotNull
    public WrappedDialog create(@NotNull Player player, @NotNull Crate crate) {
        return Dialogs.create(builder -> {
            builder.base(DialogBases.builder(TITLE)
                .body(DialogBodies.plainMessage(BODY))
                .inputs(
                    DialogInputs.bool(JSON_ENABLED, INPUT_ENABLED).initial(crate.isOpeningCooldownEnabled()).build(),
                    DialogInputs.text(JSON_COOLDOWN, INPUT_COOLDOWN).initial(String.valueOf(crate.getOpeningCooldownTime())).build(),
                    DialogInputs.text(JSON_AMOUNT, INPUT_AMOUNT).initial(String.valueOf(crate.getOpeningLimitAmount())).build()
                )
                .build()
            );

            builder.type(DialogTypes.multiAction(DialogButtons.ok()).exitAction(DialogButtons.back()).build());

            builder.handleResponse(DialogActions.OK, (viewer, identifier, nbtHolder) -> {
                if (nbtHolder == null) return;

                boolean enabled = nbtHolder.getBoolean(JSON_ENABLED, crate.isOpeningCooldownEnabled());
                int cooldown = nbtHolder.getInt(JSON_COOLDOWN, crate.getOpeningCooldownTime());
                int amount = nbtHolder.getInt(JSON_AMOUNT, crate.getOpeningLimitAmount());

                crate.setOpeningCooldownEnabled(enabled);
                crate.setOpeningCooldownTime(cooldown);
                crate.setOpeningLimitAmount(amount);
                crate.markDirty();
                viewer.callback();
            });
        });
    }
}
