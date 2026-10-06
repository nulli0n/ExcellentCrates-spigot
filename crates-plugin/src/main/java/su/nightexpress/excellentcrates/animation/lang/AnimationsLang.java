package su.nightexpress.excellentcrates.animation.lang;

import org.bukkit.Sound;
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
public class AnimationsLang implements LangContainer {

    public static final MessageLocale GENERIC_NO_ANIMATION_COMPONENT = LangEntry
        .builder("animations.generic.no_animation_component")
        .chatMessage("Crate " + TagWrappers.WHITE.wrap(SharedPlaceholders.CRATE_NAME) + " has no animation component.");

    public static final MessageLocale GENERIC_ANIMATION_SESSION_ACTIVE = LangEntry
        .builder("animations.generic.animation_session_active")
        .chatMessage(Sound.ENTITY_VILLAGER_NO,
            "Please wait for the current animation to finish."
        );

    public static final MessageLocale PIPELINE_INVALID_ANIMATION_INSTANTIATOR = LangEntry
        .builder("animations.pipeline.invalid_animation_instantiator")
        .chatMessage("Can not play animation for crate " + TagWrappers.WHITE.wrap(SharedPlaceholders.CRATE_NAME) +
            " due to invalid animation config."
        );

    public static final MessageLocale PIPELINE_NO_REWARDS_COMPONENT = LangEntry
        .builder("animations.pipeline.no_rewards_component")
        .chatMessage("Can not play animation for crate " + TagWrappers.WHITE.wrap(SharedPlaceholders.CRATE_NAME) +
            " due to missing rewards component."
        );

    public static final MessageLocale PIPELINE_NO_ANIMATION_COMPONENT = LangEntry
        .builder("animations.pipeline.no_animation_component")
        .chatMessage("Can not play animation for crate " + TagWrappers.WHITE.wrap(SharedPlaceholders.CRATE_NAME) +
            " due to missing animation component."
        );

    public static final IconLocale EDITOR_UI_EXTENSION_BUTTON = LangEntry
        .iconBuilder("animations.editor.ui.extension.button")
        .accentColor(TagWrappers.GRADIENT.with("#FFCC99", "#FF9A76"))
        .name("Opening Animations")
        .appendCurrent("State", SharedPlaceholders.STATE)
        .br()
        .appendInfo(
            "Configure crate opening sequences,",
            "such as CS:GO roulettes and rolls.",
            TagWrappers.DARK_GRAY.wrap("Cosmetic only - does not affect outcomes.")
        )
        .br()
        .appendClick("Click to navigate")
        .build();

    public static final TextLocale EDITOR_UI_INVENTORY_COMPONENT_TITLE = LangEntry
        .builder("animations.editor.ui.inventory.component.title")
        .text("Crate Editor • Animation");

    public static final IconLocale EDITOR_UI_INVENTORY_COMPONENT_BUTTON_STATE = LangEntry
        .iconBuilder("animations.editor.ui.inventory.component.button.state")
        .name("State")
        .appendCurrent("Current", SharedPlaceholders.STATE)
        .br()
        .appendInfo("Toggles the opening animation feature.")
        .br()
        .appendClick("Click to toggle")
        .build();

    public static final IconLocale EDITOR_UI_INVENTORY_COMPONENT_BUTTON_ANIMATION = LangEntry
        .iconBuilder("animations.editor.ui.inventory.component.button.animation")
        .name("Animation Profile")
        .appendCurrent("Current", CommonPlaceholders.GENERIC_VALUE)
        .br()
        .appendInfo("Determines which reward animation", "plays when opening the crate.")
        .br()
        .appendClick("Click to change")
        .build();

    public static final TextLocale EDITOR_UI_DIALOG_PROFILE_SELECTION_TITLE = LangEntry
        .builder("animations.editor.ui.dialog.profile_selection.title")
        .text(TagWrappers.GOLD.and(TagWrappers.UNDERLINED).wrap("Select Animation Profile"));

    public static final DialogElementLocale EDITOR_UI_DIALOG_PROFILE_SELECTION_BODY = LangEntry
        .builder("animations.editor.ui.dialog.profile_selection.body")
        .dialogElement(
            "Select the desired " + TagWrappers.GOLD.wrap("animation profile") + ".",
            "",
            "The selected profile controls which animation sequence plays and its specific visual settings.",
            "",
            TagWrappers.GRAY.wrap("Custom profiles for registered types can be created in:") + " " +
                TagWrappers.GOLD.wrap("/objects/animations")
        );

    public static final ButtonLocale EDITOR_UI_DIALOG_PROFILE_SELECTION_BUTTON_UNSELECTED = LangEntry
        .builder("animations.editor.ui.dialog.profile_selection.button.unselected")
        .button(CommonPlaceholders.GENERIC_NAME, "Click to select!");

    public static final ButtonLocale EDITOR_UI_DIALOG_PROFILE_SELECTION_BUTTON_SELECTED = LangEntry
        .builder("animations.editor.ui.dialog.profile_selection.button.selected")
        .button(
            TagWrappers.GREEN.and(TagWrappers.UNDERLINED).wrap(CommonPlaceholders.GENERIC_NAME),
            "This profile is currently selected."
        );
}
