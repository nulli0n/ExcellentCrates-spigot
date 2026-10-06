package su.nightexpress.excellentcrates.keys.item.lang;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.core.SharedPlaceholders;
import su.nightexpress.nightcore.locale.LangContainer;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.DialogElementLocale;
import su.nightexpress.nightcore.locale.entry.IconLocale;
import su.nightexpress.nightcore.locale.entry.TextLocale;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public class KeyItemLang implements LangContainer {

    public static final TextLocale UI_INVENTORY_ITEM_TITLE = LangEntry
        .builder("keys.editor.ui.inventory.item.title")
        .text("Key Editor • Item Settings");

    public static final IconLocale UI_INVENTORY_ITEM_BUTTON_ICON = LangEntry
        .iconBuilder("keys.editor.ui.inventory.item.button.icon")
        .name("Key Item")
        .appendInfo("Displayed as the key's icon in GUIs",
            "and given directly to player inventories.",
            "",
            TagWrappers.GOLD.wrap("Double-click") + " any item in your inventory",
            "to set it as the new key item."
        )
        .build();

    public static final IconLocale UI_INVENTORY_ITEM_BUTTON_ICON_INVALID = LangEntry
        .iconBuilder("keys.editor.ui.inventory.item.button.icon_invalid")
        .accentColor(TagWrappers.RED)
        .name("Key Item - Invalid")
        .appendInfo(
            "The current item data is invalid.",
            "Please replace the key item by",
            "double-clicking any item in",
            "your inventory."
        )
        .build();

    public static final IconLocale UI_INVENTORY_ITEM_BUTTON_STACKABLE = LangEntry
        .iconBuilder("keys.editor.ui.inventory.item.button.stackable")
        .name("Item Stackable")
        .appendCurrent("Current", CommonPlaceholders.GENERIC_VALUE)
        .br()
        .appendInfo("Controls whether this key item can be",
            "stacked in player inventories.",
            "",
            TagWrappers.DARK_GRAY.wrap("Note: Toggling this will not update items"),
            TagWrappers.DARK_GRAY.wrap("that were already given out previously.")
        )
        .br()
        .appendClick("Click to toggle")
        .build();

    public static final IconLocale UI_INVENTORY_ITEM_INHERIT_DISPLAY_SETTINGS = LangEntry
        .iconBuilder("keys.editor.ui.inventory.item.button.inherit_display_settings")
        .name("Inherit Display Settings")
        .appendCurrent("State", SharedPlaceholders.STATE)
        .br()
        .appendInfo("When enabled, inherits the " + TagWrappers.WHITE.wrap("display name"),
            "and " + TagWrappers.WHITE.wrap("lore") + " directly from Display Settings",
            "instead of the item's own meta.",
            "",
            TagWrappers.DARK_GRAY.wrap("Avoids re-saving the key item"),
            TagWrappers.DARK_GRAY.wrap("whenever text values are updated.")
        )
        .br()
        .appendClick("Click to toggle")
        .build();

    public static final TextLocale UI_DIALOG_ITEM_TITLE = LangEntry
        .builder("keys.editor.ui.dialog.item.title")
        .text(TagWrappers.GOLD.and(TagWrappers.UNDERLINED).wrap("Key Item Setup"));

    public static final TextLocale UI_DIALOG_ITEM_INPUT_SET_DISPLAY_NAME = LangEntry
        .builder("keys.editor.ui.dialog.item.input.set_display_name")
        .text("Replace Key Name");

    public static final TextLocale UI_DIALOG_ITEM_INPUT_SET_DISPLAY_LORE = LangEntry
        .builder("keys.editor.ui.dialog.item.input.set_display_lore")
        .text("Replace Key Lore");

    public static final DialogElementLocale UI_DIALOG_ITEM_BODY_NORMAL = LangEntry
        .builder("keys.editor.ui.dialog.item.body.normal")
        .dialogElement(
            TagWrappers.SOFT_YELLOW.and(TagWrappers.BOLD).wrap("Replace Key Name"),
            "When toggled, the key name in the Display settings will be overridden by the name of the provided item.",
            "",
            TagWrappers.SOFT_YELLOW.and(TagWrappers.BOLD).wrap("Replace Key Lore"),
            "When toggled, the key lore in the Display settings will be overridden by the lore of the provided item."
        );

}
