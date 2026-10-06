package su.nightexpress.excellentcrates.crates.open.lang;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.core.SharedLinks;
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
public final class CrateOpeningLang implements LangContainer {

    public static final MessageLocale EDITOR_NO_COMPONENT = LangEntry
        .builder("crates.opening.editor.no_component")
        .chatMessage("Crate " + TagWrappers.WHITE.wrap(SharedPlaceholders.CRATE_NAME) +
            " has no open actions component."
        );

    public static final IconLocale EDITOR_UI_EXTENSION_BUTTON = LangEntry
        .iconBuilder("crates.opening.editor.ui.extension.button")
        .accentColor(TagWrappers.GRADIENT.with("#ff7e5f", "#feb47b"))
        .name("Opening Actions")
        .appendInfo(
            "Configure additional actions executed",
            "upon opening, such as " + TagWrappers.ORANGE.wrap("custom commands") + "."
        )
        .br()
        .appendClick("Click to navigate")
        .build();

    public static final TextLocale EDITOR_UI_INVENTORY_SETTINGS_TITLE = LangEntry
        .builder("crates.opening.editor.ui.inventory.settings.title")
        .text("Crates Editor • Opening Actions");

    public static final IconLocale EDITOR_UI_INVENTORY_SETTINGS_BUTTON_STATE = LangEntry
        .iconBuilder("crates.opening.editor.ui.inventory.settings.button.state")
        .name("State")
        .appendCurrent("Current", SharedPlaceholders.STATE)
        .br()
        .appendInfo("Toggles the opening actions feature.")
        .br()
        .appendClick("Click to toggle")
        .build();

    public static final IconLocale EDITOR_UI_INVENTORY_SETTINGS_BUTTON_COMMANDS = LangEntry
        .iconBuilder("crates.opening.editor.ui.inventory.settings.button.commands")
        .name("Commands")
        .rawLore(CommonPlaceholders.GENERIC_VALUE)
        .br()
        .appendInfo("Configures commands executed directly", "by the console upon crate opening.")
        .br()
        .appendClick("Click to edit")
        .build();

    public static final TextLocale EDITOR_UI_DIALOG_COMMANDS_TITLE = LangEntry
        .builder("crates.opening.editor.ui.dialog.commands.title")
        .text(TagWrappers.GOLD.and(TagWrappers.UNDERLINED).wrap("Opening Commands"));

    public static final DialogElementLocale EDITOR_UI_DIALOG_COMMANDS_BODY = LangEntry
        .builder("crates.opening.editor.ui.dialog.commands.body")
        .dialogElement(
            "Enter the desired " + TagWrappers.GOLD.wrap("commands") + ".",
            "",
            TagWrappers.GRAY.wrap(
                "These commands will execute automatically from the server console after a successful crate opening."
            ),
            "",
            TagWrappers.GRAY.wrap("Supported placeholders: " +
                TagWrappers.AQUA.wrap("Crate") + ", " +
                TagWrappers.AQUA.wrap("Player") + ", and " +
                TagWrappers.AQUA.wrap("PlaceholderAPI") + "."),
            TagWrappers.GRAY.wrap("Visit " +
                TagWrappers.GREEN.and(TagWrappers.UNDERLINED).wrap(TagWrappers.OPEN_URL.with(SharedLinks.DOCUMENTATION)
                    .wrap("documentation")) +
                " for details."
            )
        );

    public static final TextLocale EDITOR_UI_DIALOG_COMMANDS_INPUT_COMMANDS = LangEntry
        .builder("crates.opening.editor.ui.dialog.commands.input.commands")
        .text(TagWrappers.SPRITE_BLOCKS.apply("block/command_block_back") + " Commands");

    private CrateOpeningLang() {
    }
}
