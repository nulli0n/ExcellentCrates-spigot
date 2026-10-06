package su.nightexpress.excellentcrates.rarity.reward.component.lang;

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
public class RarityComponentLang implements LangContainer {

    public static final MessageLocale GENERIC_NO_RARITY_COMPONENT = LangEntry
        .builder("rarity.component.generic.no_rarity_component")
        .chatMessage("Reward " + TagWrappers.RED.wrap(SharedPlaceholders.REWARD_ID) + " has no rarity component.");

    public static final IconLocale EDITOR_UI_EXTENSION_BUTTON = LangEntry
        .iconBuilder("rarity.component.editor.ui.extension.button")
        .name("Rarity")
        .appendCurrent("State", SharedPlaceholders.STATE)
        .br()
        .appendInfo("Configure cosmetic rarity styling.",
            "",
            TagWrappers.DARK_GRAY.wrap("Cosmetic only - does not affect drop chance.")
        )
        .br()
        .appendClick("Click to navigate")
        .build();

    public static final TextLocale EDITOR_UI_INVENTORY_COMPONENT_TITLE = LangEntry
        .builder("rarity.component.editor.ui.inventory.component.title")
        .text("Reward Options • Rarity");

    public static final IconLocale EDITOR_UI_INVENTORY_COMPONENT_BUTTON_STATE = LangEntry
        .iconBuilder("rarity.component.editor.ui.inventory.component.button.state")
        .name("State")
        .appendCurrent("Current", SharedPlaceholders.STATE)
        .br()
        .appendInfo("Toggles the rarity feature.")
        .br()
        .appendClick("Click to toggle")
        .build();

    public static final IconLocale EDITOR_UI_INVENTORY_COMPONENT_BUTTON_RARITY = LangEntry
        .iconBuilder("rarity.component.editor.ui.inventory.component.button.rarity")
        .name("Rarity")
        .appendCurrent("Current", CommonPlaceholders.GENERIC_VALUE)
        .br()
        .appendInfo("Controls which rarity to use", "for the reward.")
        .br()
        .appendClick("Click to change")
        .build();

    public static final TextLocale EDITOR_UI_DIALOG_RARITY_SELECTION_TITLE = LangEntry
        .builder("rarity.component.editor.ui.dialog.rarity.selection.title")
        .text(TagWrappers.GOLD.and(TagWrappers.UNDERLINED).wrap("Reward Rarity Selection"));

    public static final DialogElementLocale EDITOR_UI_DIALOG_RARITY_SELECTION_BODY = LangEntry
        .builder("rarity.component.editor.ui.dialog.rarity.selection.body")
        .dialogElement(
            "Select the desired " + TagWrappers.GOLD.wrap("rarity") + " for the reward."
        );

    public static final ButtonLocale EDITOR_UI_DIALOG_RARITY_SELECTION_BUTTON_RARITY_UNSELECTED = LangEntry
        .builder("rarity.component.editor.ui.dialog.rarity.selection.button.rarity.unselected")
        .button(CommonPlaceholders.GENERIC_NAME, "Click to select!");

    public static final ButtonLocale EDITOR_UI_DIALOG_RARITY_SELECTION_BUTTON_RARITY_SELECTED = LangEntry
        .builder("rarity.component.editor.ui.dialog.rarity.selection.button.rarity.selected")
        .button(
            TagWrappers.GREEN.and(TagWrappers.UNDERLINED).wrap(CommonPlaceholders.GENERIC_NAME),
            "This rarity is currently selected."
        );
}
