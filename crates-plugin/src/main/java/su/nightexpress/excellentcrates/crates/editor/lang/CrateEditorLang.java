package su.nightexpress.excellentcrates.crates.editor.lang;

import org.bukkit.Material;
import org.bukkit.Sound;
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
public final class CrateEditorLang implements LangContainer {

    public static final MessageLocale CREATION_SUCCESS = LangEntry
        .builder("crates.editor.creation.success")
        .chatMessage(
            Sound.ENTITY_PLAYER_LEVELUP,
            "Crate " + TagWrappers.WHITE.wrap(CommonPlaceholders.GENERIC_VALUE) +
                " has been created successfully!"
        );

    public static final MessageLocale CREATION_INVALID_ID = LangEntry
        .builder("crates.editor.creation.invalid_id")
        .chatMessage(
            Sound.ENTITY_VILLAGER_NO,
            "Invalid crate ID provided: " +
                TagWrappers.WHITE.wrap(CommonPlaceholders.GENERIC_VALUE) + "."
        );

    public static final MessageLocale CREATION_DUPLICATED_ID = LangEntry
        .builder("crates.editor.creation.duplicated_id")
        .chatMessage(
            Sound.ENTITY_VILLAGER_NO,
            "Crate with the ID " +
                TagWrappers.WHITE.wrap(CommonPlaceholders.GENERIC_VALUE) + " already exists."
        );

    public static final MessageLocale DELETION_SUCCESS = LangEntry
        .builder("crates.editor.deletion.success")
        .chatMessage(
            Sound.ENTITY_GENERIC_EXPLODE,
            "Crate " + TagWrappers.WHITE.wrap(CommonPlaceholders.GENERIC_VALUE) +
                " has been deleted successfully!"
        );

    public static final TextLocale UI_INVENTORY_CRATES_TITLE = LangEntry
        .builder("crates.editor.ui.inventory.crates.title")
        .text("Crates Editor • All Crates");

    public static final IconLocale UI_INVENTORY_CRATES_BUTTON_CRATE = LangEntry
        .iconBuilder("crates.editor.ui.inventory.crates.button.crate")
        .rawName(SharedPlaceholders.CRATE_NAME)
        .appendCurrent("ID", SharedPlaceholders.CRATE_ID)
        .br()
        .appendClick("Click to edit")
        .build();

    public static final IconLocale UI_INVENTORY_CRATES_BUTTON_CREATE = LangEntry
        .iconBuilder("crates.editor.ui.inventory.crates.button.create")
        .accentColor(TagWrappers.GREEN)
        .name("New Crate")
        .appendInfo("Use this button to create", "new crates.")
        .br()
        .appendClick("Click to create")
        .build();

    public static final TextLocale UI_INVENTORY_CRATE_OPTIONS_TITLE = LangEntry
        .builder("crates.editor.ui.inventory.crate_options.title")
        .text("Crate Editor • Crate Options");

    public static final IconLocale UI_INVENTORY_CRATE_OPTIONS_BUTTON_DISPLAY = LangEntry
        .iconBuilder("crates.editor.ui.inventory.crate_options.button.display")
        .accentColor(TagWrappers.GRADIENT.with("#f6d365", "#fda085"))
        .name("Display Settings")
        .appendInfo(
            "Configure the crate's visual texts,",
            "such as its " + TagWrappers.WHITE.wrap("display name") + " and " + TagWrappers.WHITE.wrap("lore") + "."
        )
        .br()
        .appendClick("Click to edit")
        .build();

    public static final IconLocale UI_INVENTORY_CRATE_OPTIONS_BUTTON_ITEM = LangEntry
        .iconBuilder("crates.editor.ui.inventory.crate_options.button.item")
        .accentColor(TagWrappers.GRADIENT.with("#d4a373", "#bc6c25"))
        .name("Crate Item")
        .appendInfo(
            "Configure the physical crate item.",
            "Defines its appearance in GUIs and",
            "when held in player inventories."
        )
        .br()
        .appendClick("Click to edit")
        .build();

    public static final IconLocale UI_INVENTORY_CRATE_OPTIONS_BUTTON_DELETE = LangEntry
        .iconBuilder("crates.editor.ui.inventory.crate_options.button.delete")
        .accentColor(TagWrappers.GRADIENT.with("#ff4b4b", "#ff0844"))
        .name("Delete Crate")
        .appendInfo(
            "Permanently deletes this crate along with",
            "all of its associated configuration.",
            "",
            TagWrappers.RED.wrap("This action cannot be undone!")
        )
        .br()
        .appendClick("Click to delete")
        .build();

    public static final TextLocale UI_INVENTORY_CRATE_DISPLAY_TITLE = LangEntry
        .builder("crates.editor.ui.inventory.crate_display.title")
        .text("Crate Editor • Display");

    public static final IconLocale UI_INVENTORY_CRATE_DISPLAY_BUTTON_NAME = LangEntry
        .iconBuilder("crates.editor.ui.inventory.crate_display.button.name")
        .name("Name")
        .appendCurrent("Current", CommonPlaceholders.GENERIC_VALUE)
        .br()
        .appendInfo("Sets crate display name.")
        .br()
        .appendClick("Click to edit")
        .build();

    public static final IconLocale UI_INVENTORY_CRATE_DISPLAY_BUTTON_LORE = LangEntry
        .iconBuilder("crates.editor.ui.inventory.crate_display.button.lore")
        .name("Lore")
        .rawLore(CommonPlaceholders.GENERIC_VALUE, CommonPlaceholders.EMPTY_IF_ABOVE)
        .appendInfo("Sets crate description.")
        .br()
        .appendClick("Click to edit")
        .build();

    public static final TextLocale UI_INVENTORY_CRATE_ITEM_TITLE = LangEntry
        .builder("crates.editor.ui.inventory.crate_item.title")
        .text("Crate Editor • Item");

    public static final IconLocale UI_INVENTORY_CRATE_ITEM_BUTTON_ICON = LangEntry
        .iconBuilder("crates.editor.ui.inventory.crate_item.button.icon")
        .accentColor(TagWrappers.GOLD)
        .name("Crate Item")
        .appendInfo("Displayed as the crate's icon in GUIs",
            "and given directly to player inventories.",
            "",
            TagWrappers.GOLD.wrap("Double-click") + " any item in your inventory",
            "to set it as the new crate item."
        )
        .build();

    public static final IconLocale UI_INVENTORY_CRATE_ITEM_BUTTON_ICON_INVALID = LangEntry
        .iconBuilder("crates.editor.ui.inventory.crate_item.button.icon_invalid")
        .accentColor(TagWrappers.RED)
        .name("Crate Item - Invalid")
        .appendInfo(
            "The current item data is invalid.",
            "Please replace the crate item by",
            "double-clicking any item in",
            "your inventory."
        )
        .build();

    public static final IconLocale UI_INVENTORY_CRATE_ITEM_BUTTON_STACKABLE = LangEntry
        .iconBuilder("crates.editor.ui.inventory.crate_item.button.stackable")
        .name("Item Stackable")
        .appendCurrent("Current", CommonPlaceholders.GENERIC_VALUE)
        .br()
        .appendInfo("Controls whether this crate item can be",
            "stacked in player inventories.",
            "",
            TagWrappers.DARK_GRAY.wrap("Note: Toggling this will not update items"),
            TagWrappers.DARK_GRAY.wrap("that were already given out previously.")
        )
        .br()
        .appendClick("Click to toggle")
        .build();

    public static final IconLocale UI_INVENTORY_CRATE_ITEM_BUTTON_USE_DISPLAY = LangEntry
        .iconBuilder("crates.editor.ui.inventory.crate_item.button.use_display")
        .name("Use Display Settings")
        .appendCurrent("Current", CommonPlaceholders.GENERIC_VALUE)
        .br()
        .appendInfo("When enabled, inherits the " + TagWrappers.WHITE.wrap("display name"),
            "and " + TagWrappers.WHITE.wrap("lore") + " directly from Display Settings",
            "instead of the item's own meta.",
            "",
            TagWrappers.DARK_GRAY.wrap("Avoids re-saving the physical item"),
            TagWrappers.DARK_GRAY.wrap("whenever text values are updated.")
        )
        .br()
        .appendClick("Click to toggle")
        .build();

    public static final TextLocale UI_DIALOG_CREATION_TITLE = LangEntry
        .builder("crates.editor.ui.dialog.creation.title")
        .text(TagWrappers.GOLD.and(TagWrappers.UNDERLINED).wrap("Crate Creation"));

    public static final DialogElementLocale UI_DIALOG_CREATION_BODY = LangEntry
        .builder("crates.editor.ui.dialog.creation.body")
        .dialogElement(
            "Enter a " + TagWrappers.GOLD.wrap("unique identifier") + " for the new crate.",
            "",
            TagWrappers.GRAY.wrap("Choose carefully - this ID is required for"),
            TagWrappers.GRAY.wrap("configuration and " + TagWrappers.RED.wrap("cannot be changed") + " later."),
            "",
            TagWrappers.RED.wrap("Warning:") + " Special characters are not allowed."
        );

    public static final TextLocale UI_DIALOG_CREATION_INPUT_ID = LangEntry
        .builder("crates.editor.ui.dialog.creation.input.id")
        .text(TagWrappers.SPRITE_ITEM.apply(Material.NAME_TAG) + " Crate ID");

    public static final TextLocale UI_DIALOG_DELETE_CONFIRM_TITLE = LangEntry
        .builder("crates.editor.ui.dialog.delete.confirm.title")
        .text(TagWrappers.RED.and(TagWrappers.UNDERLINED).wrap("Crate Deletion"));

    public static final DialogElementLocale UI_DIALOG_DELETE_CONFIRM_BODY = LangEntry
        .builder("crates.editor.ui.dialog.delete.confirm.body")
        .dialogElement(
            "Are you sure you want to delete this crate?",
            "",
            TagWrappers.GRAY.wrap("This action is " + TagWrappers.RED.wrap("irreversible") + "."),
            TagWrappers.GRAY.wrap("Think twice before proceeding.")
        );

    public static final DialogElementLocale UI_DIALOG_DISPLAY_NAME_BODY = LangEntry
        .builder("crates.editor.ui.dialog.display.name.body")
        .dialogElement(
            "Enter the " + TagWrappers.SOFT_YELLOW.wrap("display name") + "."
        );

    public static final TextLocale UI_DIALOG_DISPLAY_NAME_TITLE = LangEntry
        .builder("crates.editor.ui.dialog.display.name.title")
        .text("Crate Name");

    public static final TextLocale UI_DIALOG_DISPLAY_NAME_INPUT_NAME = LangEntry
        .builder("crates.editor.ui.dialog.display.name.input.name")
        .text(TagWrappers.SPRITE_ITEM.apply(Material.NAME_TAG) + " Display Name");

    public static final TextLocale UI_DIALOG_DISPLAY_LORE_TITLE = LangEntry
        .builder("crates.editor.ui.dialog.display.lore.title")
        .text("Crate Lore");

    public static final DialogElementLocale UI_DIALOG_DISPLAY_LORE_BODY = LangEntry
        .builder("crates.editor.ui.dialog.display.lore.body")
        .dialogElement(
            "Enter the " + TagWrappers.SOFT_YELLOW.wrap("lore") + "."
        );

    public static final TextLocale UI_DIALOG_DISPLAY_LORE_INPUT_LORE = LangEntry
        .builder("crates.editor.ui.dialog.display.lore.input.lore")
        .text(TagWrappers.SPRITE_ITEM.apply(Material.WRITABLE_BOOK) + " Lore / Description");


    public static final TextLocale UI_DIALOG_CRATE_ITEM_TITLE = LangEntry
        .builder("crates.editor.ui.dialog.crate.item.title")
        .text("Crate Item Setup");

    public static final TextLocale UI_DIALOG_CRATE_ITEM_INPUT_SET_DISPLAY_NAME = LangEntry
        .builder("crates.editor.ui.dialog.crate.item.input.setdisplayname")
        .text("Replace Crate Name");

    public static final TextLocale UI_DIALOG_CRATE_ITEM_INPUT_SET_DISPLAY_LORE = LangEntry
        .builder("crates.editor.ui.dialog.crate.item.input.setdisplaylore")
        .text("Replace Crate Lore");

    public static final DialogElementLocale UI_DIALOG_CRATE_ITEM_BODY_NORMAL = LangEntry
        .builder("crates.editor.ui.dialog.crate.item.body.normal")
        .dialogElement(
            TagWrappers.SOFT_YELLOW.and(TagWrappers.BOLD).wrap("Replace Crate Name"),
            "When toggled, the crate name in the Display settings will be overridden by the name of the provided item.",
            "",
            TagWrappers.SOFT_YELLOW.and(TagWrappers.BOLD).wrap("Replace Crate Lore"),
            "When toggled, the crate lore in the Display settings will be overridden by the lore of the provided item."
        );
}
