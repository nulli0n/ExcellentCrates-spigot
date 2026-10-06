package su.nightexpress.excellentcrates.preview.crate.editor.lang;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.core.SharedPlaceholders;
import su.nightexpress.nightcore.locale.LangContainer;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.ButtonLocale;
import su.nightexpress.nightcore.locale.entry.DialogElementLocale;
import su.nightexpress.nightcore.locale.entry.IconLocale;
import su.nightexpress.nightcore.locale.entry.MessageLocale;
import su.nightexpress.nightcore.locale.entry.TextLocale;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public class PreviewComponentLang implements LangContainer {

    public static final MessageLocale GENERIC_NO_PREVIEW_COMPONENT = LangEntry
        .builder("preview.component.generic.no_preview_component")
        .chatMessage("Crate " + TagWrappers.WHITE.wrap(SharedPlaceholders.CRATE_NAME) + " has no preview component.");

    public static final IconLocale EDITOR_UI_EXTENSION_BUTTON = LangEntry
        .iconBuilder("preview.component.editor.ui.extension.button")
        .accentColor(TagWrappers.GRADIENT.with("#00c6ff", "#0072ff"))
        .name("Reward Preview")
        .appendCurrent("State", SharedPlaceholders.STATE)
        .br()
        .appendInfo(
            "Configure the GUI preview allowing",
            "players to inspect potential rewards."
        )
        .br()
        .appendClick("Click to navigate")
        .build();

    public static final TextLocale EDITOR_UI_INVENTORY_COMPONENT_TITLE = LangEntry
        .builder("preview.component.editor.ui.inventory.component.title")
        .text("Crate Editor • Preview");

    public static final IconLocale EDITOR_UI_INVENTORY_COMPONENT_BUTTON_STATE = LangEntry
        .iconBuilder("preview.component.editor.ui.inventory.component.button.state")
        .name("State")
        .appendCurrent("Current", SharedPlaceholders.STATE)
        .br()
        .appendInfo("Toggles the reward preview feature.")
        .br()
        .appendClick("Click to toggle")
        .build();

    public static final IconLocale EDITOR_UI_INVENTORY_COMPONENT_BUTTON_PREVIEW = LangEntry
        .iconBuilder("preview.component.editor.ui.inventory.component.button.preview")
        .name("Preview")
        .appendCurrent("Current", CommonPlaceholders.GENERIC_VALUE)
        .br()
        .appendInfo("Determines the visual appearance", "and styling of the preview.")
        .br()
        .appendClick("Click to change")
        .build();

    public static final TextLocale EDITOR_UI_DIALOG_PREVIEW_SELECTION_TITLE = LangEntry
        .builder("preview.component.editor.ui.dialog.preview_selection.title")
        .text(TagWrappers.GOLD.and(TagWrappers.UNDERLINED).wrap("Select Preview Profile"));

    public static final DialogElementLocale EDITOR_UI_DIALOG_PREVIEW_SELECTION_BODY = LangEntry
        .builder("preview.component.editor.ui.dialog.preview_selection.body")
        .dialogElement(
            "Select the desired " + TagWrappers.GOLD.wrap("preview profile") + ".",
            "",
            "The selected profile defines the overall visual style, and reward composition.",
            "",
            TagWrappers.GRAY.wrap("Custom profiles for registered types can be created in:") + " " +
                TagWrappers.GOLD.wrap("/objects/preview")
        );

    public static final ButtonLocale EDITOR_UI_DIALOG_PREVIEW_SELECTION_BUTTON_PREVIEW_UNSELECTED = LangEntry
        .builder("preview.component.editor.ui.dialog.preview_selection.button.preview.unselected")
        .button(CommonPlaceholders.GENERIC_NAME, "Click to select!");

    public static final ButtonLocale EDITOR_UI_DIALOG_PREVIEW_SELECTION_BUTTON_PREVIEW_SELECTED = LangEntry
        .builder("preview.component.editor.ui.dialog.preview_selection.button.preview.selected")
        .button(
            TagWrappers.GREEN.and(TagWrappers.UNDERLINED).wrap(CommonPlaceholders.GENERIC_NAME),
            "This profile is currently selected."
        );
}
