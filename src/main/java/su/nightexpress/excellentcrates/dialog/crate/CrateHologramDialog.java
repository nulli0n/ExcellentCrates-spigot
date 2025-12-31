package su.nightexpress.excellentcrates.dialog.crate;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.dialog.Dialog;
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;
import su.nightexpress.nightcore.bridge.dialog.wrap.input.WrappedDialogInput;
import su.nightexpress.nightcore.bridge.dialog.wrap.input.single.WrappedSingleOptionEntry;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.DialogElementLocale;
import su.nightexpress.nightcore.locale.entry.TextLocale;
import su.nightexpress.nightcore.ui.dialog.Dialogs;
import su.nightexpress.nightcore.ui.dialog.build.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers.*;

public class CrateHologramDialog extends Dialog<Crate> {

    private static final int LINES_AMOUNT = 5;
    private static final String ID_CUSTOM = "custom";

    private static final TextLocale TITLE = LangEntry.builder("Dialog.Crate.Hologram.Title").text(title("Crate", "Hologram Settings"));

    private static final DialogElementLocale BODY = LangEntry.builder("Dialog.Crate.Hologram.Body").dialogElement(400,
            "Here you can configure the " + SOFT_YELLOW.wrap("hologram") + ".",
            "",
            "1. Select a " + SOFT_YELLOW.wrap("Template") + " from the config.",
            "2. OR select " + SOFT_YELLOW.wrap("'Custom'") + " to edit lines manually below.",
            "",
            SOFT_YELLOW.wrap("→ ") + "To disable crate hologram, uncheck the " + SOFT_YELLOW.wrap("Enabled") + " box."
    );

    private static final TextLocale INPUT_ENABLED = LangEntry.builder("Dialog.Crate.Hologram.Input.Enabled").text("Enabled");
    private static final TextLocale INPUT_TEMPLATE = LangEntry.builder("Dialog.Crate.Hologram.Input.Template").text("Template");
    private static final TextLocale INPUT_OFFSET  = LangEntry.builder("Dialog.Crate.Hologram.Input.YOffset").text(SOFT_YELLOW.wrap("Y Offset"));
    private static final TextLocale INPUT_LINE    = LangEntry.builder("Dialog.Crate.Hologram.Input.Line").text("Line " + SOFT_YELLOW.wrap("#%s"));

    private static final String JSON_ENABLED = "enabled";
    private static final String JSON_TEMPLATE = "template";
    private static final String JSON_OFFSET  = "offset";
    private static final Function<Integer, String> JSON_LINE = index -> "line_" + index;

    @Override
    @NotNull
    public WrappedDialog create(@NotNull Player player, @NotNull Crate crate) {
        List<WrappedDialogInput> inputs = new ArrayList<>();

        // 1. Enabled Checkbox
        inputs.add(DialogInputs.bool(JSON_ENABLED, INPUT_ENABLED)
                .initial(crate.isHologramEnabled())
                .build());

        // 2. Template Selector (Config Templates + "Custom")
        List<WrappedSingleOptionEntry> entries = new ArrayList<>();

        // Add "Custom" Option
        entries.add(new WrappedSingleOptionEntry(
                ID_CUSTOM,
                "Custom (Edit Lines Below)",
                crate.getHologramTemplateId().equalsIgnoreCase(ID_CUSTOM)
        ));

        // Add Config Template Options
        Config.getHologramTemplateIds().stream().sorted().forEach(id -> {
            boolean isSelected = crate.getHologramTemplateId().equalsIgnoreCase(id);
            entries.add(new WrappedSingleOptionEntry(id, id, isSelected));
        });

        inputs.add(DialogInputs.singleOption(JSON_TEMPLATE, INPUT_TEMPLATE, entries).build());

        // 3. Y Offset Field
        inputs.add(DialogInputs.text(JSON_OFFSET, INPUT_OFFSET)
                .initial(String.valueOf(crate.getHologramYOffset()))
                .maxLength(5)
                .build());

        // 4. Custom Line Fields (Always visible, but used only if "Custom" is selected)
        List<String> hologramLines = crate.getHologramLines();
        int size = Math.max(LINES_AMOUNT, hologramLines.size());

        for (int index = 0; index < size; index++) {
            inputs.add(DialogInputs.text(JSON_LINE.apply(index), INPUT_LINE.text().formatted(String.valueOf(index + 1)))
                    .initial(hologramLines.size() > index ? hologramLines.get(index) : "")
                    .maxLength(200)
                    .width(300)
                    .build());
        }

        return Dialogs.create(builder -> {
            builder.base(DialogBases.builder(TITLE)
                    .body(DialogBodies.plainMessage(BODY))
                    .inputs(inputs)
                    .build()
            );

            builder.type(DialogTypes.multiAction(DialogButtons.ok()).exitAction(DialogButtons.back()).build());

            builder.handleResponse(DialogActions.OK, (viewer, identifier, nbtHolder) -> {
                if (nbtHolder == null) return;

                boolean enabled = nbtHolder.getBoolean(JSON_ENABLED, false);
                // Get the selected template ID (will be "custom" or a real ID)
                String templateId = nbtHolder.getText(JSON_TEMPLATE, ID_CUSTOM);
                double offset = nbtHolder.getDouble(JSON_OFFSET, crate.getHologramYOffset());

                // Collect custom lines
                List<String> lines = new ArrayList<>();
                for (int index = 0; index < size; index++) {
                    nbtHolder.getText(JSON_LINE.apply(index))
                            .filter(Predicate.not(String::isBlank))
                            .ifPresent(lines::add);
                }

                crate.setHologramEnabled(enabled);
                crate.setHologramTemplateId(templateId); // Save the template choice
                crate.setHologramYOffset(offset);
                crate.setHologramLines(lines); // Save lines regardless (so they aren't lost if user switches back and forth)

                crate.recreateHologram();
                crate.markDirty();
                viewer.callback();
            });
        });
    }
}