package su.nightexpress.excellentcrates.keys.display.lang;

import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.locale.LangContainer;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.DialogElementLocale;
import su.nightexpress.nightcore.locale.entry.IconLocale;
import su.nightexpress.nightcore.locale.entry.TextLocale;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public class KeyDisplayLang implements LangContainer {

    public static final IconLocale EDITOR_UI_EXTENSION_BUTTON = LangEntry
        .iconBuilder("keys.display.editor.ui.extension.button")
        .name("Display Settings")
        .appendInfo("Key display settings.")
        .br()
        .appendClick("Click to navigate")
        .build();

    public static final TextLocale EDITOR_UI_INVENTORY_MAIN_TITLE = LangEntry
        .builder("keys.display.editor.ui.inventory.main.title")
        .text("Key Editor • Display Settings");

    public static final IconLocale EDITOR_UI_INVENTORY_MAIN_BUTTON_NAME = LangEntry
        .iconBuilder("keys.display.editor.ui.inventory.main.button.name")
        .name("Display Name")
        .appendCurrent("Current", CommonPlaceholders.GENERIC_VALUE)
        .br()
        .appendInfo("Configure the visible display name", "shown for this key in menus", "and messages.")
        .br()
        .appendClick("Click to change")
        .build();

    public static final IconLocale EDITOR_UI_INVENTORY_MAIN_BUTTON_LORE = LangEntry
        .iconBuilder("keys.display.editor.ui.inventory.main.button.lore")
        .name("Lore")
        .rawLore(CommonPlaceholders.GENERIC_VALUE, CommonPlaceholders.EMPTY_IF_ABOVE)
        .appendInfo("Configure descriptive lore lines",
            "displayed beneath the key name."
        )
        .br()
        .appendClick("Click to change")
        .build();

    public static final TextLocale EDITOR_UI_DIALOG_NAME_TITLE = LangEntry
        .builder("keys.display.editor.ui.dialog.name.title")
        .text(TagWrappers.GOLD.and(TagWrappers.UNDERLINED).wrap("Key Display Name"));

    public static final DialogElementLocale EDITOR_UI_DIALOG_NAME_BODY = LangEntry
        .builder("keys.display.editor.ui.dialog.name.body")
        .dialogElement(
            "Set the desired " + TagWrappers.GOLD.wrap("display name") + "."
        );

    public static final TextLocale EDITOR_UI_DIALOG_NAME_INPUT_NAME = LangEntry
        .builder("keys.display.editor.ui.dialog.name.input.name")
        .text(TagWrappers.SPRITE_ITEM.apply(Material.NAME_TAG) + " Display Name");

    public static final TextLocale EDITOR_UI_DIALOG_LORE_TITLE = LangEntry
        .builder("keys.display.editor.ui.dialog.lore.title")
        .text(TagWrappers.GOLD.and(TagWrappers.UNDERLINED).wrap("Key Lore / Description"));

    public static final DialogElementLocale EDITOR_UI_DIALOG_LORE_BODY = LangEntry
        .builder("keys.display.editor.ui.dialog.lore.body")
        .dialogElement(
            "Set the desired " + TagWrappers.GOLD.wrap("lore / description") + "."
        );

    public static final TextLocale EDITOR_UI_DIALOG_LORE_INPUT_LORE = LangEntry
        .builder("keys.display.editor.ui.dialog.lore.input.lore")
        .text(TagWrappers.SPRITE_ITEM.apply(Material.WRITABLE_BOOK) + " Lore / Description");
}
