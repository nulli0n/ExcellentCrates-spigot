package su.nightexpress.excellentcrates.dialog.crate;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.dialog.CrateDialog;
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.DialogElementLocale;
import su.nightexpress.nightcore.locale.entry.TextLocale;
import su.nightexpress.nightcore.ui.dialog.Dialogs;
import su.nightexpress.nightcore.ui.dialog.build.*;

import static su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers.*;

public class CrateCooldownDialog extends CrateDialog<Crate> {

    private static final TextLocale TITLE = LangEntry.builder("Dialog.Crate.OpeningCooldown.Title").text(title("Crate", "Opening Cooldown"));

    private static final DialogElementLocale BODY = LangEntry.builder("Dialog.Crate.OpeningCooldown.Body").dialogElement(400,
        "Enter the crate " + SOFT_YELLOW.wrap("opening cooldown") + " time.",
        "The cooldown applies to " + SOFT_YELLOW.wrap("the player") + " who opened the crate.",
        "",
        SOFT_YELLOW.wrap("→") + " To disable the cooldown, uncheck the " + SOFT_YELLOW.wrap("Enabled") + " box."
    );

    private static final TextLocale INPUT_ENABLED = LangEntry.builder("Dialog.Crate.Preview.Input.Enabled").text("Enabled");
    private static final TextLocale INPUT_COOLDOWN = LangEntry.builder("Dialog.Crate.OpeningCooldown.Input.Cooldown").text("Opening Cooldown " + GRAY.wrap("(in seconds)"));

    private static final String JSON_ENABLED = "enabled";
    private static final String JSON_COOLDOWN = "cooldown";

    @Override
    @NotNull
    public WrappedDialog create(@NotNull Player player, @NotNull Crate crate) {
        return Dialogs.create(builder -> {
            builder.base(DialogBases.builder(TITLE)
                .body(DialogBodies.plainMessage(BODY))
                .inputs(
                    DialogInputs.bool(JSON_ENABLED, INPUT_ENABLED).initial(crate.isOpeningCooldownEnabled()).build(),
                    DialogInputs.text(JSON_COOLDOWN, INPUT_COOLDOWN).initial(String.valueOf(crate.getOpeningCooldownTime())).build()
                )
                .build()
            );

            builder.type(DialogTypes.multiAction(DialogButtons.ok()).exitAction(DialogButtons.back()).build());

            builder.handleResponse(DialogActions.OK, (viewer, identifier, nbtHolder) -> {
                if (nbtHolder == null) return;

                boolean enabled = nbtHolder.getBoolean(JSON_ENABLED, crate.isOpeningCooldownEnabled());
                int cooldown = nbtHolder.getInt(JSON_COOLDOWN, crate.getOpeningCooldownTime());

                crate.setOpeningCooldownEnabled(enabled);
                crate.setOpeningCooldownTime(cooldown);
                crate.markDirty();
                viewer.callback();
            });
        });
    }
}
