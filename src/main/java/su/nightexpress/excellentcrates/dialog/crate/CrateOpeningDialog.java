package su.nightexpress.excellentcrates.dialog.crate;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.dialog.CrateDialog;
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;
import su.nightexpress.nightcore.bridge.dialog.wrap.input.single.WrappedSingleOptionEntry;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.DialogElementLocale;
import su.nightexpress.nightcore.locale.entry.TextLocale;
import su.nightexpress.nightcore.ui.dialog.Dialogs;
import su.nightexpress.nightcore.ui.dialog.build.*;

import java.util.ArrayList;
import java.util.List;

import static su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers.*;

public class CrateOpeningDialog extends CrateDialog<Crate> {

    private static final TextLocale TITLE = LangEntry.builder("Dialog.Crate.Opening.Title").text(title("Crate", "Opening Animation"));

    private static final DialogElementLocale BODY = LangEntry.builder("Dialog.Crate.Opening.Body").dialogElement(400,
        "Select an opening animation for the crate.",
        "You can create and edit animations in the " + SOFT_YELLOW.wrap(Config.DIR_OPENINGS) + " directory.",
        "",
        SOFT_YELLOW.wrap("→ ") + "To disable crate opening animation, uncheck the " + SOFT_YELLOW.wrap("Enabled") + " box."
    );

    private static final TextLocale INPUT_ENABLED = LangEntry.builder("Dialog.Crate.Opening.Input.Enabled").text("Enabled");
    private static final TextLocale INPUT_OPENING = LangEntry.builder("Dialog.Crate.Opening.Input.Opening").text(SOFT_YELLOW.wrap("Opening"));

    private static final String JSON_ENABLED = "enabled";
    private static final String JSON_ID      = "id";

    private final CratesPlugin plugin;

    public CrateOpeningDialog(@NotNull CratesPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    @NotNull
    public WrappedDialog create(@NotNull Player player, @NotNull Crate crate) {
        List<WrappedSingleOptionEntry> entries = new ArrayList<>();

        this.plugin.getOpeningManager().getProviderIds().stream().sorted(String::compareTo).forEach(id -> {
            entries.add(new WrappedSingleOptionEntry(id, id, crate.getOpeningId().equalsIgnoreCase(id)));
        });

        return Dialogs.create(builder -> {
            builder.base(DialogBases.builder(TITLE)
                .body(DialogBodies.plainMessage(BODY))
                .inputs(
                    DialogInputs.bool(JSON_ENABLED, INPUT_ENABLED).initial(crate.isOpeningEnabled()).build(),
                    DialogInputs.singleOption(JSON_ID, INPUT_OPENING, entries).build()
                )
                .build()
            );

            builder.type(DialogTypes.multiAction(DialogButtons.ok()).exitAction(DialogButtons.back()).build());

            builder.handleResponse(DialogActions.OK, (viewer, identifier, nbtHolder) -> {
                if (nbtHolder == null) return;

                boolean enabled = nbtHolder.getBoolean(JSON_ENABLED, false);
                String id = nbtHolder.getText(JSON_ID, crate.getOpeningId());

                crate.setOpeningEnabled(enabled);
                crate.setOpeningId(id);
                crate.markDirty();
                viewer.callback();
            });
        });
    }
}
