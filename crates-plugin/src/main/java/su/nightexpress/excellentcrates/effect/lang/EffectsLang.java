package su.nightexpress.excellentcrates.effect.lang;

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
public class EffectsLang implements LangContainer {

    public static final MessageLocale GENERIC_NO_EFFECT_COMPONENT = LangEntry
        .builder("effect.component.generic.no_effect_component")
        .chatMessage("Crate " + TagWrappers.RED.wrap(SharedPlaceholders.CRATE_NAME) + " has no effect component.");

    public static final IconLocale EDITOR_UI_EXTENSION_BUTTON = LangEntry
        .iconBuilder("effect.component.editor.ui.extension.button")
        .accentColor(TagWrappers.GRADIENT.with("#FF4500", "#FF8C00"))
        .name("Block Effects")
        .appendCurrent("State", SharedPlaceholders.STATE)
        .br()
        .appendInfo(
            "Configure visual " + TagWrappers.ORANGE.wrap("particle effects") + " emitted",
            "by this crate's blocks in the world."
        )
        .br()
        .appendClick("Click to navigate")
        .build();

    public static final TextLocale EDITOR_UI_INVENTORY_COMPONENT_TITLE = LangEntry
        .builder("effect.component.editor.ui.inventory.component.title")
        .text("Crate Editor • Block Effects");

    public static final IconLocale EDITOR_UI_INVENTORY_COMPONENT_BUTTON_STATE = LangEntry
        .iconBuilder("effect.component.editor.ui.inventory.component.button.state")
        .name("State")
        .appendCurrent("Current", SharedPlaceholders.STATE)
        .br()
        .appendInfo("Toggles the block effects feature.")
        .br()
        .appendClick("Click to toggle")
        .build();

    public static final IconLocale EDITOR_UI_INVENTORY_COMPONENT_BUTTON_PROFILE = LangEntry
        .iconBuilder("effect.component.editor.ui.inventory.component.button.profile")
        .name("Effect Profile")
        .appendCurrent("Current", CommonPlaceholders.GENERIC_VALUE)
        .br()
        .appendInfo("Determines the visual appearance", "and particle styling of the effect.")
        .br()
        .appendClick("Click to change")
        .build();

    public static final TextLocale EDITOR_UI_DIALOG_PROFILE_SELECTION_TITLE = LangEntry
        .builder("effect.component.editor.ui.dialog.profile.selection.title")
        .text(TagWrappers.GOLD.and(TagWrappers.UNDERLINED).wrap("Select Effect Profile"));

    public static final DialogElementLocale EDITOR_UI_DIALOG_PROFILE_SELECTION_BODY = LangEntry
        .builder("effect.component.editor.ui.dialog.profile.selection.body")
        .dialogElement(
            "Select the desired " + TagWrappers.GOLD.wrap("effect profile") + ".",
            "",
            "The selected profile defines the overall visual style, motion, and particle composition.",
            "",
            TagWrappers.GRAY.wrap("Custom profiles for registered types can be created in:") + " " +
                TagWrappers.GOLD.wrap("/objects/effects")
        );

    public static final ButtonLocale EDITOR_UI_DIALOG_PROFILE_SELECTION_BUTTON_PROFILE_UNSELECTED = LangEntry
        .builder("effect.component.editor.ui.dialog.profile.selection.button.profile.unselected")
        .button(CommonPlaceholders.GENERIC_NAME, "Click to select.");

    public static final ButtonLocale EDITOR_UI_DIALOG_PROFILE_SELECTION_BUTTON_PROFILE_SELECTED = LangEntry
        .builder("effect.component.editor.ui.dialog.profile.selection.button.profile.selected")
        .button(
            TagWrappers.GREEN.and(TagWrappers.UNDERLINED).wrap(CommonPlaceholders.GENERIC_NAME),
            "This effect profile is currently selected."
        );
}
