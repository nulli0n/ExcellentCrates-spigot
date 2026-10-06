package su.nightexpress.excellentcrates.keys.common.base.lang;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.locale.LangContainer;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.IconLocale;
import su.nightexpress.nightcore.locale.entry.TextLocale;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public class KeyBaseLang implements LangContainer {

    public static final IconLocale EDITOR_UI_EXTENSION_BUTTON = LangEntry
        .iconBuilder("keys.base.editor.ui.extension.button")
        .name("Base")
        .appendInfo("Basic (common) key settings.")
        .br()
        .appendClick("Click to navigate")
        .build();

    public static final TextLocale EDITOR_UI_INVENTORY_MAIN_TITLE = LangEntry
        .builder("keys.base.editor.ui.inventory.main.title")
        .text("Key Editor • Base Settings");

    public static final IconLocale UI_INVENTORY_MAIN_BUTTON_VIRTUAL_OFF = LangEntry
        .iconBuilder("keys.base.editor.ui.inventory.main.button.virtual.disabled")
        .accentColor(TagWrappers.GOLD)
        .name("Physical Key")
        .appendInfo("This key is " + TagWrappers.GOLD.wrap("physical") + ".")
        .br()
        .appendInfo("Physical keys have", "a physical item")
        .appendInfo("and are stored in", "normal containers like chests", "and player inventories.")
        .br()
        .appendClick("Click to make it virtual")
        .build();

    public static final IconLocale UI_INVENTORY_MAIN_BUTTON_VIRTUAL_ON = LangEntry
        .iconBuilder("keys.base.editor.ui.inventory.main.button.virtual.enabled")
        .accentColor(TagWrappers.AQUA)
        .name("Virtual Key")
        .appendInfo("This key is " + TagWrappers.AQUA.wrap("virtual") + ".")
        .br()
        .appendInfo("Virtual keys do not", "have a physical item")
        .appendInfo("and are stored in", "the database instead.")
        .br()
        .appendClick("Click to make it physical")
        .build();


}
