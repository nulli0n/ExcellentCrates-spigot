package su.nightexpress.excellentcrates.dialog.crate;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.dialog.Dialog;
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;
import su.nightexpress.nightcore.bridge.dialog.wrap.input.text.WrappedMultilineOptions;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.DialogElementLocale;
import su.nightexpress.nightcore.locale.entry.TextLocale;
import su.nightexpress.nightcore.ui.dialog.Dialogs;
import su.nightexpress.nightcore.ui.dialog.build.*;

import java.util.Arrays;

import static su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers.*;

public class CrateHologramDialog extends Dialog<Crate> {

    private static final TextLocale TITLE = LangEntry.builder("Dialog.Crate.Hologram.Title").text(title("Crate", "Hologram Settings"));

    private static final DialogElementLocale BODY = LangEntry.builder("Dialog.Crate.Hologram.Body").dialogElement(400,
        "Here you can edit the " + SOFT_YELLOW.wrap("hologram text") + " and adjust it's " + SOFT_YELLOW.wrap("Y offset") + " to match the block height.",
        "",
        SOFT_YELLOW.wrap("→ ") + "To disable crate hologram, uncheck the " + SOFT_YELLOW.wrap("Enabled") + " box."
    );

    private static final TextLocale INPUT_ENABLED = LangEntry.builder("Dialog.Crate.Hologram.Input.Enabled").text("Enabled");
    private static final TextLocale INPUT_TEXT    = LangEntry.builder("Dialog.Crate.Hologram.Input.Text").text(SOFT_YELLOW.wrap("Text"));
    private static final TextLocale INPUT_OFFSET  = LangEntry.builder("Dialog.Crate.Hologram.Input.YOffset").text(SOFT_YELLOW.wrap("Y Offset"));

    private static final String JSON_ENABLED = "enabled";
    private static final String JSON_TEXT    = "text";
    private static final String JSON_OFFSET  = "offset";

    @Override
    @NotNull
    public WrappedDialog create(@NotNull Player player, @NotNull Crate crate) {
        return Dialogs.create(builder -> {
            builder.base(DialogBases.builder(TITLE)
                .body(DialogBodies.plainMessage(BODY))
                .inputs(
                    DialogInputs.bool(JSON_ENABLED, INPUT_ENABLED).initial(crate.isHologramEnabled()).build(),
                    DialogInputs.text(JSON_TEXT, INPUT_TEXT)
                        .initial(String.join("\n", crate.getHologramText()))
                        .maxLength(600)
                        .width(300)
                        .multiline(new WrappedMultilineOptions(10, 150))
                        .build(),
                    DialogInputs.text(JSON_OFFSET, INPUT_OFFSET).initial(String.valueOf(crate.getHologramYOffset())).maxLength(5).build()
                )
                .build()
            );

            builder.type(DialogTypes.multiAction(DialogButtons.ok()).exitAction(DialogButtons.back()).build());

            builder.handleResponse(DialogActions.OK, (viewer, identifier, nbtHolder) -> {
                if (nbtHolder == null) return;

                boolean enabled = nbtHolder.getBoolean(JSON_ENABLED, false);
                String raw = nbtHolder.getText(JSON_TEXT).orElse(null);
                double offset = nbtHolder.getDouble(JSON_OFFSET, crate.getHologramYOffset());

                crate.setHologramEnabled(enabled);
                if (raw != null) {
                    crate.setHologramText(Arrays.asList(raw.split("\n")));
                }
                crate.setHologramYOffset(offset);
                crate.recreateHologram();
                crate.markDirty();
                viewer.callback();
            });
        });
    }
}
