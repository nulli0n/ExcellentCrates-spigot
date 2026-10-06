package su.nightexpress.excellentcrates.keys.editor.lang;

import org.bukkit.Material;
import org.bukkit.Sound;

import su.nightexpress.excellentcrates.core.SharedPlaceholders;
import su.nightexpress.nightcore.locale.LangContainer;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.DialogElementLocale;
import su.nightexpress.nightcore.locale.entry.IconLocale;
import su.nightexpress.nightcore.locale.entry.MessageLocale;
import su.nightexpress.nightcore.locale.entry.TextLocale;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

public final class KeyEditorLang implements LangContainer {

    public static final TextLocale COMMAND_EDITOR_DESCRIPTION = LangEntry
        .builder("keys.editor.command.editor.description")
        .text("Opens the keys editor.");

    public static final MessageLocale CREATION_INVALID_ID = LangEntry
        .builder("keys.editor.creation.invalid_id")
        .chatMessage(
            Sound.ENTITY_VILLAGER_NO,
            TagWrappers.GRAY.wrap("Invalid key ID provided: " +
                TagWrappers.WHITE.wrap(CommonPlaceholders.GENERIC_VALUE) + ".")
        );

    public static final MessageLocale CREATION_DUPLICATED_ID = LangEntry
        .builder("keys.editor.creation.duplicated_id")
        .chatMessage(
            Sound.ENTITY_VILLAGER_NO,
            TagWrappers.GRAY.wrap("Key with the ID " +
                TagWrappers.WHITE.wrap(CommonPlaceholders.GENERIC_VALUE) + " already exists.")
        );

    public static final MessageLocale CREATION_ID_GENERATION_FAILED = LangEntry
        .builder("keys.editor.creation.id_generation.failed")
        .chatMessage(
            Sound.ENTITY_VILLAGER_NO,
            TagWrappers.GRAY.wrap("Can not generate unique key ID for the given item context: " +
                TagWrappers.WHITE.wrap(SharedPlaceholders.ITEM) + ".")
        );

    public static final MessageLocale DELETION_FAILURE = LangEntry
        .builder("keys.editor.deletion.failure")
        .chatMessage(
            Sound.ENTITY_VILLAGER_NO,
            TagWrappers.GRAY.wrap("Failed to delete key with ID " +
                TagWrappers.WHITE.wrap(CommonPlaceholders.GENERIC_VALUE) + "."
            )
        );

    public static final TextLocale UI_INVENTORY_BROWSE_TITLE = LangEntry
        .builder("keys.editor.ui.inventory.browse.title")
        .text("Key Editor • All Keys");

    public static final IconLocale UI_INVENTORY_BROWSE_KEY = LangEntry
        .iconBuilder("keys.editor.ui.inventory.browse.key")
        .rawName(SharedPlaceholders.KEY_NAME)
        .rawLore(SharedPlaceholders.KEY_LORE)
        .rawLore(CommonPlaceholders.EMPTY_IF_ABOVE)
        .appendCurrent("ID", SharedPlaceholders.KEY_ID)
        .br()
        .appendClick("Click to edit")
        .build();

    public static final IconLocale UI_INVENTORY_BROWSE_BUTTON_MANUAL_CREATION = LangEntry
        .iconBuilder("keys.editor.ui.inventory.browse.button.manual_creation")
        .accentColor(TagWrappers.GREEN)
        .name("Create Key")
        .appendInfo("To configure and create a key",
            "manually, " + TagWrappers.GREEN.wrap("click this button") + "."
        )
        .build();

    public static final IconLocale UI_INVENTORY_BROWSE_BUTTON_AUTOMATIC_CREATION = LangEntry
        .iconBuilder("keys.editor.ui.inventory.browse.button.automatic_creation")
        .accentColor(TagWrappers.PURPLE)
        .name("Quick Mode")
        .appendInfo("You can rapidly",
            "create new keys by " + TagWrappers.PURPLE.wrap("double-clicking"),
            "items inside your inventory."
        )
        .build();

    public static final TextLocale UI_INVENTORY_SETTINGS_TITLE = LangEntry
        .builder("keys.editor.ui.inventory.settings.title")
        .text("Key Editor • Settings");

    public static final IconLocale UI_INVENTORY_SETTINGS_BUTTON_DELETE = LangEntry
        .iconBuilder("keys.editor.ui.inventory.settings.button.delete")
        .accentColor(TagWrappers.RED)
        .name("Delete Key")
        .appendInfo("Permanently deletes the key.")
        .br()
        .appendClick("Click to delete")
        .build();

    public static final IconLocale UI_INVENTORY_SETTINGS_BUTTON_ITEM = LangEntry
        .iconBuilder("keys.editor.ui.inventory.settings.button.item")
        .name("Item")
        .appendInfo("Key item settings.")
        .br()
        .appendClick("Click to edit")
        .build();

    public static final TextLocale UI_DIALOG_CREATION_TITLE = LangEntry
        .builder("keys.editor.ui.dialog.creation.title")
        .text(TagWrappers.GOLD.and(TagWrappers.UNDERLINED).wrap("Key Creation"));

    public static final DialogElementLocale UI_DIALOG_CREATION_BODY_MAIN = LangEntry
        .builder("keys.editor.ui.dialog.creation.body.main")
        .dialogElement(
            "Enter a " + TagWrappers.GOLD.wrap("unique identifier") + " for the new key.",
            "",
            TagWrappers.GRAY.wrap("Choose carefully - this ID is required for"),
            TagWrappers.GRAY.wrap("configuration and " + TagWrappers.RED.wrap("cannot be changed") + " later."),
            "",
            TagWrappers.RED.wrap("Warning:") + " Special characters are not allowed."
        );

    public static final DialogElementLocale UI_DIALOG_CREATION_BODY_ID_CONFLICT = LangEntry
        .builder("keys.editor.ui.dialog.creation.body.id_conflict")
        .dialogElement(
            TagWrappers.RED.and(TagWrappers.UNDERLINED).wrap("ID Conflict Notice"),
            "During the automatic key creation process, it was detected that the automatically generated ID is already in use.",
            "To ensure the uniqueness of key identifiers, please choose a different ID for the new key."
        );

    public static final TextLocale UI_DIALOG_CREATION_INPUT_ID = LangEntry
        .builder("keys.editor.ui.dialog.creation.input.id")
        .text(TagWrappers.SPRITE_ITEM.apply(Material.NAME_TAG) + " Key ID");

    public static final TextLocale UI_DIALOG_DELETION_TITLE = LangEntry
        .builder("keys.editor.ui.dialog.deletion.title")
        .text(TagWrappers.RED.and(TagWrappers.UNDERLINED).wrap("Key Deletion"));

    public static final DialogElementLocale UI_DIALOG_DELETION_BODY = LangEntry
        .builder("keys.editor.ui.dialog.deletion.body")
        .dialogElement(
            "Are you sure you want to delete key " + TagWrappers.RED.wrap(SharedPlaceholders.KEY_ID) + " ?",
            "",
            TagWrappers.GRAY.wrap("This action is " + TagWrappers.RED.wrap("irreversible") + "."),
            TagWrappers.GRAY.wrap("Think twice before proceeding.")
        );
}
