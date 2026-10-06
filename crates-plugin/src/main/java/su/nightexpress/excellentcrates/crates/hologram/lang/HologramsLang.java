package su.nightexpress.excellentcrates.crates.hologram.lang;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.core.SharedPlaceholders;
import su.nightexpress.nightcore.locale.LangContainer;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.DialogElementLocale;
import su.nightexpress.nightcore.locale.entry.IconLocale;
import su.nightexpress.nightcore.locale.entry.MessageLocale;
import su.nightexpress.nightcore.locale.entry.TextLocale;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public class HologramsLang implements LangContainer {

    public static final MessageLocale EDITOR_NO_COMPONENT = LangEntry
        .builder("crates.holograms.editor.no_component")
        .chatMessage("Crate " + TagWrappers.RED.wrap(SharedPlaceholders.CRATE_NAME) +
            " does not have a hologram component."
        );

    public static final MessageLocale EDITOR_HOLOGRAM_TOGGLE = LangEntry
        .builder("crates.holograms.editor.toggle")
        .chatMessage(TagWrappers.WHITE.wrap(SharedPlaceholders.CRATE_NAME) + " crate hologram has been " +
            TagWrappers.WHITE.wrap(SharedPlaceholders.STATE) + "."
        );

    public static final MessageLocale EDITOR_HOLOGRAM_TEXT = LangEntry
        .builder("crates.holograms.editor.text")
        .chatMessage(
            TagWrappers.WHITE.wrap(SharedPlaceholders.CRATE_NAME) + " crate hologram text has been updated."
        );

    public static final MessageLocale EDITOR_HOLOGRAM_OFFSET = LangEntry
        .builder("crates.holograms.editor.offset")
        .chatMessage(
            TagWrappers.WHITE.wrap(SharedPlaceholders.CRATE_NAME) + " crate hologram offset has been updated."
        );

    public static final IconLocale UI_EXTENSION_BUTTON = LangEntry
        .iconBuilder("crates.holograms.editor.ui.extension.button")
        .accentColor(TagWrappers.GRADIENT.with("#a8ff78", "#78ffd6"))
        .name("Holograms")
        .appendInfo(
            "Configure floating " + TagWrappers.AQUA.wrap("hologram displays"),
            "above this crate's placed blocks."
        )
        .br()
        .appendClick("Click to edit")
        .build();

    public static final TextLocale UI_INVENTORY_OPTIONS_TITLE = LangEntry
        .builder("crates.holograms.editor.ui.inventory.options.title")
        .text("Hologram Options");

    public static final IconLocale UI_INVENTORY_OPTIONS_BUTTON_STATE = LangEntry
        .iconBuilder("crates.holograms.editor.ui.inventory.options.button.state")
        .name("State")
        .appendCurrent("Enabled", SharedPlaceholders.STATE)
        .br()
        .appendClick("Click to toggle")
        .build();

    public static final IconLocale UI_INVENTORY_OPTIONS_BUTTON_TEXT = LangEntry
        .iconBuilder("crates.holograms.editor.ui.inventory.options.button.text")
        .name("Text")
        .rawLore(CommonPlaceholders.GENERIC_VALUE)
        .br()
        .appendInfo("Hologram text to display", "above the crate.")
        .br()
        .appendClick("Click to edit")
        .build();

    public static final IconLocale UI_INVENTORY_OPTIONS_BUTTON_OFFSET = LangEntry
        .iconBuilder("crates.holograms.editor.ui.inventory.options.button.offset")
        .name("Offset")
        .appendCurrent("X", SharedPlaceholders.X)
        .appendCurrent("Y", SharedPlaceholders.Y)
        .appendCurrent("Z", SharedPlaceholders.Z)
        .br()
        .appendInfo("Hologram offset used to", "position the hologram relative", "to the crate.")
        .br()
        .appendClick("Click to edit")
        .build();

    public static final TextLocale UI_DIALOG_HOLOGRAM_TEXT_TITLE = LangEntry
        .builder("crates.holograms.editor.ui.dialog.hologram_text.title")
        .text("Edit Hologram Text");

    public static final DialogElementLocale UI_DIALOG_HOLOGRAM_TEXT_BODY = LangEntry
        .builder("crates.holograms.editor.ui.dialog.hologram_text.body")
        .dialogElement(
            "The text to display in the crate hologram.",
            "Each line of text should be separated by a new line.",
            "You can use placeholders in the text."
        );

    public static final TextLocale UI_DIALOG_HOLOGRAM_TEXT_INPUT_TEXT = LangEntry
        .builder("crates.holograms.editor.ui.dialog.hologram_text.input.text")
        .text("Hologram Text");

    public static final TextLocale UI_DIALOG_HOLOGRAM_OFFSET_TITLE = LangEntry
        .builder("crates.holograms.editor.ui.dialog.hologram_offset.title")
        .text("Edit Hologram Offset");

    public static final DialogElementLocale UI_DIALOG_HOLOGRAM_OFFSET_BODY = LangEntry
        .builder("crates.holograms.editor.ui.dialog.hologram_offset.body")
        .dialogElement(
            "The offset to apply to the crate hologram.",
            "This offset is applied relative to the crate's position."
        );

    public static final TextLocale UI_DIALOG_HOLOGRAM_OFFSET_INPUT_X = LangEntry
        .builder("crates.holograms.editor.ui.dialog.hologram_offset.input.x")
        .text("X Offset");

    public static final TextLocale UI_DIALOG_HOLOGRAM_OFFSET_INPUT_Y = LangEntry
        .builder("crates.holograms.editor.ui.dialog.hologram_offset.input.y")
        .text("Y Offset");

    public static final TextLocale UI_DIALOG_HOLOGRAM_OFFSET_INPUT_Z = LangEntry
        .builder("crates.holograms.editor.ui.dialog.hologram_offset.input.z")
        .text("Z Offset");
}
