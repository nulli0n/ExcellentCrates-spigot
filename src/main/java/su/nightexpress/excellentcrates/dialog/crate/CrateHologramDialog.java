package su.nightexpress.excellentcrates.dialog.crate;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
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

public class CrateHologramDialog extends CrateDialog<Crate> {

    private static final TextLocale TITLE = LangEntry.builder("Dialog.Crate.Hologram.Title").text(title("Crate", "Hologram Settings"));

    private static final DialogElementLocale BODY = LangEntry.builder("Dialog.Crate.Hologram.Body").dialogElement(400,
        "Here you can select " + SOFT_YELLOW.wrap("hologram template") + " for the crate and adjust it's " + SOFT_YELLOW.wrap("Y offset") + " to match the block height.",
        "",
        "You can create and edit hologram templates in the " + SOFT_YELLOW.wrap("config.yml") + ".",
        "",
        SOFT_YELLOW.wrap("→ ") + "To disable crate hologram, uncheck the " + SOFT_YELLOW.wrap("Enabled") + " box."
    );

    private static final TextLocale INPUT_ENABLED = LangEntry.builder("Dialog.Crate.Hologram.Input.Enabled").text("Enabled");
    private static final TextLocale INPUT_TEMPLATE = LangEntry.builder("Dialog.Crate.Hologram.Input.Template").text(SOFT_YELLOW.wrap("Template"));
    private static final TextLocale INPUT_OFFSET  = LangEntry.builder("Dialog.Crate.Hologram.Input.YOffset").text(SOFT_YELLOW.wrap("Y Offset"));

    private static final String JSON_ENABLED = "enabled";
    private static final String JSON_TEMPLATE = "template";
    private static final String JSON_OFFSET  = "offset";

    @Override
    @NotNull
    public WrappedDialog create(@NotNull Player player, @NotNull Crate crate) {
        List<WrappedSingleOptionEntry> entries = new ArrayList<>();

        Config.getHologramTemplateIds().stream().sorted(String::compareTo).forEach(id -> {
            entries.add(new WrappedSingleOptionEntry(id, id, crate.getHologramTemplateId().equalsIgnoreCase(id)));
        });

        return Dialogs.create(builder -> {
            builder.base(DialogBases.builder(TITLE)
                .body(DialogBodies.plainMessage(BODY))
                .inputs(
                    DialogInputs.bool(JSON_ENABLED, INPUT_ENABLED).initial(crate.isOpeningEnabled()).build(),
                    DialogInputs.singleOption(JSON_TEMPLATE, INPUT_TEMPLATE, entries).build(),
                    DialogInputs.text(JSON_OFFSET, INPUT_OFFSET).initial(String.valueOf(crate.getHologramYOffset())).maxLength(5).build()
                )
                .build()
            );

            builder.type(DialogTypes.multiAction(DialogButtons.ok()).exitAction(DialogButtons.back()).build());

            builder.handleResponse(DialogActions.OK, (viewer, identifier, nbtHolder) -> {
                if (nbtHolder == null) return;

                boolean enabled = nbtHolder.getBoolean(JSON_ENABLED, false);
                String id = nbtHolder.getText(JSON_TEMPLATE, crate.getOpeningId());
                double offset = nbtHolder.getDouble(JSON_OFFSET, crate.getHologramYOffset());

                crate.setHologramEnabled(enabled);
                crate.setHologramTemplateId(id);
                crate.setHologramYOffset(offset);
                crate.recreateHologram();
                crate.markDirty();
                viewer.callback();
            });
        });
    }
}
