package su.nightexpress.excellentcrates.dialog.crate;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.dialog.Dialog;
import su.nightexpress.nightcore.bridge.common.NightNbtHolder; // Import added
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;
import su.nightexpress.nightcore.bridge.dialog.wrap.button.WrappedActionButton; // Import added
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.DialogElementLocale;
import su.nightexpress.nightcore.locale.entry.TextLocale;
import su.nightexpress.nightcore.ui.dialog.Dialogs;
import su.nightexpress.nightcore.ui.dialog.build.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers.*;

public class CrateOpeningDialog extends Dialog<Crate> {

    private static final TextLocale TITLE = LangEntry.builder("Dialog.Crate.Opening.Title").text(title("Crate", "Opening Animation"));

    private static final DialogElementLocale BODY = LangEntry.builder("Dialog.Crate.Opening.Body").dialogElement(400,
            "Select an opening animation for the crate.",
            "You can create and edit animations in the " + SOFT_YELLOW.wrap(Config.DIR_OPENINGS) + " directory.",
            "",
            "Current Opening: %1$s" // Modified to show current selection in text
    );

    private static final String JSON_ENABLED = "enabled";
    private static final String JSON_ID      = "id";

    private final CratesPlugin plugin;

    public CrateOpeningDialog(@NotNull CratesPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    @NotNull
    public WrappedDialog create(@NotNull Player player, @NotNull Crate crate) {
        List<WrappedActionButton> buttons = new ArrayList<>();

        // 1. Add a button to DISABLE the animation
        NightNbtHolder disableNbt = NightNbtHolder.builder()
                .put(JSON_ENABLED, false)
                .put(JSON_ID, "")
                .build();

        buttons.add(DialogButtons.action(SOFT_RED.wrap("Disable Animation"))
                .action(DialogActions.customClick(DialogActions.OK, disableNbt))
                .build());

        // 2. Loop through all animations and create a button for each
        this.plugin.getOpeningManager().getProviderIds().stream()
                .sorted(String::compareTo)
                .forEach(id -> {
                    // Highlight the button if it is the currently selected one
                    boolean isCurrent = crate.isOpeningEnabled() && id.equalsIgnoreCase(crate.getOpeningId());
                    String label = isCurrent ? SOFT_GREEN.wrap(id + " (Selected)") : SOFT_YELLOW.wrap(id);

                    // Create data holder for this specific button
                    NightNbtHolder nbt = NightNbtHolder.builder()
                            .put(JSON_ENABLED, true)
                            .put(JSON_ID, id)
                            .build();

                    // Add the button to the list
                    buttons.add(DialogButtons.action(label)
                            .action(DialogActions.customClick(DialogActions.OK, nbt))
                            .build());
                });

        return Dialogs.create(builder -> {
            // Display current status in the body text
            String currentStatus = crate.isOpeningEnabled() ? SOFT_GREEN.wrap(crate.getOpeningId()) : SOFT_RED.wrap("Disabled");

            builder.base(DialogBases.builder(TITLE)
                    .body(DialogBodies.plainMessage(BODY))
                    // We removed the .inputs(...) section here because we are using buttons now
                    .build()
            );

            // 3. Set the type to multiAction with our list of buttons
            // .columns(3) makes it look like a grid (chest menu style)
            builder.type(DialogTypes.multiAction(buttons)
                    .exitAction(DialogButtons.back())
                    .columns(3)
                    .build());

            // 4. Handle the click
            builder.handleResponse(DialogActions.OK, (viewer, identifier, nbtHolder) -> {
                if (nbtHolder == null) return;

                // Get data from the clicked button
                boolean enabled = nbtHolder.getBoolean(JSON_ENABLED, false);
                String id = nbtHolder.getText(JSON_ID).orElse(crate.getOpeningId());

                crate.setOpeningEnabled(enabled);
                if (enabled && id != null && !id.isEmpty()) {
                    crate.setOpeningId(id);
                }

                crate.markDirty();
                viewer.callback(); // Close menu
            });
        });
    }
}