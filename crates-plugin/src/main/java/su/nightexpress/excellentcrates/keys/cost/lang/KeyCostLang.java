package su.nightexpress.excellentcrates.keys.cost.lang;

import org.bukkit.Material;
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
public final class KeyCostLang implements LangContainer {

    public static final MessageLocale ERROR_NO_KEY_COST_COMPONENT = LangEntry
        .builder("keys.cost.error.no_key_cost_component")
        .chatMessage("Crate " + TagWrappers.WHITE.wrap(SharedPlaceholders.CRATE_NAME) +
            " has no required key component.");

    public static final MessageLocale ERROR_NO_KEY_ENTRY = LangEntry
        .builder("keys.cost.error.no_key_entry")
        .chatMessage("Key entry " + TagWrappers.WHITE.wrap(CommonPlaceholders.GENERIC_VALUE) + " does not exist.");

    public static final MessageLocale ERROR_KEY_ENTRY_ALREADY_EXISTS = LangEntry
        .builder("keys.cost.error.key_entry_already_exists")
        .chatMessage("Key entry " + TagWrappers.WHITE.wrap(CommonPlaceholders.GENERIC_VALUE) + " already exists.");

    public static final TextLocale DISPLAY_BALANCE_FORMAT = LangEntry
        .builder("keys.cost.display.balance.format")
        .text("x" + CommonPlaceholders.GENERIC_AMOUNT + " " + CommonPlaceholders.GENERIC_NAME);

    public static final IconLocale EDITOR_UI_EXTENSION_BUTTON = LangEntry
        .iconBuilder("keys.cost.editor.ui.extension.button")
        .accentColor(TagWrappers.GRADIENT.with("#f9d423", "#ff4e50"))
        .name("Required Keys")
        .appendCurrent("Enabled", SharedPlaceholders.STATE)
        .appendCurrent("Current", CommonPlaceholders.GENERIC_AMOUNT)
        .br()
        .appendInfo(
            "Manage the specific keys required",
            "to open this crate."
        )
        .br()
        .appendClick("Click to navigate")
        .build();

    public static final TextLocale EDITOR_UI_INVENTORY_ENTRIES_TITLE = LangEntry
        .builder("keys.cost.editor.ui.inventory.entries.title")
        .text("Crate Editor • Required Keys");

    public static final IconLocale EDITOR_UI_INVENTORY_ENTRIES_BUTTON_STATE = LangEntry
        .iconBuilder("keys.cost.editor.ui.inventory.entries.button.state")
        .name("State")
        .appendCurrent("Current", SharedPlaceholders.STATE)
        .br()
        .appendInfo("Toggles the required keys feature.")
        .br()
        .appendClick("Click to toggle")
        .build();

    public static final IconLocale EDITOR_UI_INVENTORY_ENTRIES_BUTTON_ADD_ALLOWED = LangEntry
        .iconBuilder("keys.cost.editor.ui.inventory.entries.button.add_allowed")
        .accentColor(TagWrappers.GREEN)
        .name("Add Key Requirement")
        .appendInfo("Requires the selected key and amount", "to unlock and open this crate.")
        .br()
        .appendInfo(
            TagWrappers.DARK_GRAY.wrap("If multiple keys are added, players can"),
            TagWrappers.DARK_GRAY.wrap("choose which one to unlock with.")
        )
        .br()
        .appendClick("Click to add")
        .build();

    public static final IconLocale EDITOR_UI_INVENTORY_ENTRIES_BUTTON_ADD_DISALLOWED = LangEntry
        .iconBuilder("keys.cost.editor.ui.inventory.entries.button.add_disallowed")
        .accentColor(TagWrappers.RED)
        .name("Add Key Requirement")
        .appendInfo(
            "All available keys are already added,",
            "or no keys have been created yet.",
            "",
            TagWrappers.SOFT_RED.wrap("Create new keys") + " in the Key Editor",
            "to add them as requirements here"
        )
        .build();

    public static final IconLocale EDITOR_UI_INVENTORY_ENTRIES_BUTTON_ENTRY_VALID = LangEntry
        .iconBuilder("keys.cost.editor.ui.inventory.entries.button.entry_valid")
        .rawName(SharedPlaceholders.KEY_NAME)
        .appendCurrent("Amount", CommonPlaceholders.GENERIC_AMOUNT)
        .rawLore(
            CommonPlaceholders.EMPTY_IF_BELOW,
            SharedPlaceholders.KEY_LORE,
            CommonPlaceholders.EMPTY_IF_ABOVE
        )
        .appendInfo("This key is required to", "open the crate.")
        .br()
        .appendClick("Click to edit")
        .build();

    public static final IconLocale EDITOR_UI_INVENTORY_ENTRIES_BUTTON_ENTRY_INVALID = LangEntry
        .iconBuilder("keys.cost.editor.ui.inventory.entries.button.entry_invalid")
        .accentColor(TagWrappers.RED)
        .name(SharedPlaceholders.KEY_ID)
        .appendCurrent("Amount", CommonPlaceholders.GENERIC_AMOUNT)
        .br()
        .appendInfo(
            TagWrappers.RED.wrap("The key this entry refers to"), TagWrappers.RED.wrap("is invalid."),
            "",
            "Create a new key with this ID", "or delete this entry."
        )
        .br()
        .appendClick("Click to edit")
        .build();

    public static final TextLocale EDITOR_UI_INVENTORY_ENTRY_TITLE = LangEntry
        .builder("keys.cost.editor.ui.inventory.entry.title")
        .text("Required Keys • Key Settings");

    public static final IconLocale EDITOR_UI_INVENTORY_ENTRY_BUTTON_AMOUNT = LangEntry
        .iconBuilder("keys.cost.editor.ui.inventory.entry.button.amount")
        .name("Required Amount")
        .appendCurrent("Current", CommonPlaceholders.GENERIC_VALUE)
        .br()
        .appendInfo("Specifies how many copies of this key", "a player must hold to open the crate.")
        .br()
        .appendClick("Click to edit")
        .build();

    public static final IconLocale EDITOR_UI_INVENTORY_ENTRY_BUTTON_REMOVE = LangEntry
        .iconBuilder("keys.cost.editor.ui.inventory.entry.button.remove")
        .accentColor(TagWrappers.RED)
        .name("Remove Requirement")
        .appendInfo("Permanently removes this key requirement", "from the crate's opening costs.")
        .br()
        .appendInfo(TagWrappers.DARK_GRAY.wrap("Does not delete or alter the key itself."))
        .br()
        .appendClick("Click to remove")
        .build();

    public static final TextLocale EDITOR_UI_DIALOG_ADD_ENTRY_TITLE = LangEntry
        .builder("keys.cost.editor.ui.dialog.add.entry.title")
        .text(TagWrappers.GOLD.and(TagWrappers.UNDERLINED).wrap("Add Key Requirement"));

    public static final DialogElementLocale EDITOR_UI_DIALOG_ADD_ENTRY_BODY = LangEntry
        .builder("keys.cost.editor.ui.dialog.add.entry.body")
        .dialogElement(
            "Select the desired " + TagWrappers.GOLD.wrap("key") + ".",
            "",
            TagWrappers.GRAY.wrap("You can create your own keys in:") + " " +
                TagWrappers.GOLD.wrap("/cratekey editor")
        );

    public static final ButtonLocale EDITOR_UI_DIALOG_ADD_ENTRY_BUTTON_KEY = LangEntry
        .builder("keys.cost.editor.ui.dialog.add.entry.button.key")
        .button(SharedPlaceholders.KEY_NAME, "Click to select!");

    public static final TextLocale EDITOR_UI_DIALOG_REMOVE_ENTRY_TITLE = LangEntry
        .builder("keys.cost.editor.ui.dialog.remove.entry.title")
        .text(TagWrappers.RED.and(TagWrappers.UNDERLINED).wrap("Remove Key Requirement"));

    public static final DialogElementLocale EDITOR_UI_DIALOG_REMOVE_ENTRY_BODY = LangEntry
        .builder("keys.cost.editor.ui.dialog.remove.entry.body")
        .dialogElement(
            "Are you sure you want to remove this key entry?",
            "This action " + TagWrappers.RED.wrap("cannot be undone") + ".",
            "",
            TagWrappers.GRAY.wrap("Does not delete or alter the key itself.")
        );

    public static final TextLocale EDITOR_UI_DIALOG_ENTRY_AMOUNT_TITLE = LangEntry
        .builder("keys.cost.editor.ui.dialog.entry.amount.title")
        .text(TagWrappers.GOLD.and(TagWrappers.UNDERLINED).wrap("Required Key Amount"));

    public static final DialogElementLocale EDITOR_UI_DIALOG_ENTRY_AMOUNT_BODY = LangEntry
        .builder("keys.cost.editor.ui.dialog.entry.amount.body")
        .dialogElement(
            "Enter the desired " + TagWrappers.GOLD.wrap("key amount") + ".",
            "",
            TagWrappers.GRAY.wrap(
                "Players must possess at least this quantity of the selected key to unlock and open the crate."
            )
        );

    public static final TextLocale EDITOR_UI_DIALOG_ENTRY_AMOUNT_INPUT_AMOUNT = LangEntry
        .builder("keys.cost.editor.ui.dialog.entry.amount.input.amount")
        .text(TagWrappers.SPRITE_ITEM.apply(Material.REPEATER) + " Required Amount");
}
