package su.nightexpress.excellentcrates.core.lang;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.common.cooldown.CooldownMode;
import su.nightexpress.excellentcrates.core.SharedPlaceholders;
import su.nightexpress.nightcore.locale.LangContainer;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.DialogElementLocale;
import su.nightexpress.nightcore.locale.entry.EnumLocale;
import su.nightexpress.nightcore.locale.entry.IconLocale;
import su.nightexpress.nightcore.locale.entry.MessageLocale;
import su.nightexpress.nightcore.locale.entry.RegistryLocale;
import su.nightexpress.nightcore.locale.entry.TextLocale;
import su.nightexpress.nightcore.util.bridge.RegistryType;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public final class Lang implements LangContainer {

    public static final EnumLocale<CooldownMode> COOLDOWN_MODE = LangEntry.builder("Enums.CooldownMode")
        .enumeration(CooldownMode.class);

    public static final RegistryLocale<Particle> PARTICLE = LangEntry.builder("Assets.Particle")
        .registry(RegistryType.PARTICLE_TYPE);

    public static final TextLocale PLUGIN_COMMAND_ROOT_DESCRIPTION = LangEntry
        .builder("Plugin.Command.Root.Description")
        .text("Main command for ExcellentCrates");

    public static final TextLocale PLUGIN_COMMAND_STATUS_DESCRIPTION = LangEntry
        .builder("Plugin.Command.Status.Description")
        .text("Show plugin status");

    public static final TextLocale PLUGIN_COMMAND_RELOAD_DESCRIPTION = LangEntry
        .builder("Plugin.Command.Reload.Description")
        .text("Reload the plugin");

    public static final MessageLocale CORE_UI_ERROR_MENU_NOT_FOUND = LangEntry
        .builder("Core.UI.Menu.NotFound")
        .chatMessage(TagWrappers.RED.wrap("Menu " +
            TagWrappers.WHITE.wrap(CommonPlaceholders.GENERIC_VALUE) + " not found.")
        );

    public static final MessageLocale CORE_UI_ERROR_DIALOG_NOT_FOUND = LangEntry
        .builder("Core.UI.Dialog.NotFound")
        .chatMessage(TagWrappers.RED.wrap("Dialog " +
            TagWrappers.WHITE.wrap(CommonPlaceholders.GENERIC_VALUE) + " not found.")
        );

    public static final TextLocale COMMAND_ARGUMENT_NAME_X = LangEntry.builder("Command.Argument.Name.X").text("x");
    public static final TextLocale COMMAND_ARGUMENT_NAME_Y = LangEntry.builder("Command.Argument.Name.Y").text("y");
    public static final TextLocale COMMAND_ARGUMENT_NAME_Z = LangEntry.builder("Command.Argument.Name.Z").text("z");

    public static final MessageLocale COMMAND_SYNTAX_INVALID_ID = LangEntry
        .builder("Command.Syntax.InvalidId")
        .chatMessage(TagWrappers.RED.wrap(CommonPlaceholders.GENERIC_INPUT) + " is not a valid ID!");

    public static final TextLocale FORMAT_COOLDOWN_READY = LangEntry
        .builder("Format.Cooldown.Ready")
        .text(TagWrappers.GREEN.wrap("Ready"));

    public static final TextLocale FORMAT_COOLDOWN_NONE = LangEntry
        .builder("Format.Cooldown.None")
        .text(TagWrappers.GREEN.wrap("None"));

    public static final TextLocale FORMAT_COOLDOWN_PERMANENT = LangEntry
        .builder("Format.Cooldown.Permanent")
        .text(TagWrappers.RED.wrap("Permanent"));

    public static final TextLocale FORMAT_LOCATION = LangEntry.builder("Format.Location")
        .text("[" +
            SharedPlaceholders.X + ", " +
            SharedPlaceholders.Y + ", " +
            SharedPlaceholders.Z +
            "]"
        );

    public static final TextLocale UI_COMMAND_LIST_EMPTY = LangEntry
        .builder("UI.Command.List.Empty")
        .text(TagWrappers.RED.wrap("No commands defined."));

    public static final TextLocale UI_COMMAND_LIST_ENTRY = LangEntry
        .builder("UI.Command.List.Entry")
        .text(TagWrappers.WHITE.wrap(TagWrappers.SPRITE_BLOCKS.apply("block/command_block_back") + " /" +
            CommonPlaceholders.GENERIC_ENTRY)
        );

    public static final IconLocale UI_ITEM_PLACEHOLDER = LangEntry
        .iconBuilder("UI.Item.Placeholder")
        .accentColor(TagWrappers.RED)
        .name(TagWrappers.RED.wrap("Broken Item"))
        .appendInfo("Item data is missing or invalid.",
            "",
            "If it's a custom item, ensure", "that the item plugin is installed.",
            "",
            "If it's a vanilla item, you", "may need to replace it with a new one."
        )
        .build();

    public static final MessageLocale GENERIC_INTERNAL_ERROR = LangEntry
        .builder("Generic.Internal.Error")
        .chatMessage(TagWrappers.RED.wrap("An internal error has occurred. Check the server console for details."));

    public static final DialogElementLocale UI_GENERIC_DIALOG_COOLDOWN_BODY_MODES = LangEntry
        .builder("UI.Generic.Dialog.Cooldown.Body.Modes")
        .dialogElement(
            TagWrappers.SPRITE_ITEMS.apply("item/clock_12") + " " + TagWrappers.GOLD.wrap("Daily Mode"),
            "Resets at " + TagWrappers.GOLD.wrap("midnight") + " after number of days set in " + TagWrappers.GOLD.wrap(
                "Duration field") + ".",
            "",
            TagWrappers.SPRITE_ITEM.apply(Material.REPEATER) + " " + TagWrappers.GREEN.wrap("Custom Mode"),
            "Resets after an " + TagWrappers.GREEN.wrap("exact") + " number of seconds set in " + TagWrappers.GREEN
                .wrap("Duration field") + "."
        );

    public static final TextLocale UI_GENERIC_DIALOG_COOLDOWN_INPUT_STATE = LangEntry
        .builder("UI.Generic.Dialog.Cooldown.Input.State")
        .text("State");

    public static final TextLocale UI_GENERIC_DIALOG_COOLDOWN_INPUT_MODE = LangEntry
        .builder("UI.Generic.Dialog.Cooldown.Input.Mode")
        .text("Type");

    public static final TextLocale UI_GENERIC_DIALOG_COOLDOWN_INPUT_DURATION = LangEntry
        .builder("UI.Generic.Dialog.Cooldown.Input.Duration")
        .text("Duration");

    public static final DialogElementLocale GENERIC_UI_DIALOG_ITEM_BODY_CUSTOM = LangEntry
        .builder("Generic.UI.Dialog.Item.Body.Custom")
        .dialogElement(
            TagWrappers.GOLD.and(TagWrappers.BOLD).wrap("CUSTOM ITEM DETECTED:"),
            "It seems that the item you provided belongs to " + TagWrappers.GOLD.wrap(
                SharedPlaceholders.TYPE) + ".",
            "If this is correct, ensure the save method is set as " + TagWrappers.GREEN.wrap("Reference") + ".",
            "This ensures the item will reflect all changes you made in its configuration."
        );

    public static final DialogElementLocale GENERIC_UI_DIALOG_ITEM_BODY_MIXED = LangEntry
        .builder("Generic.UI.Dialog.Item.Body.Mixed")
        .dialogElement(
            TagWrappers.RED.and(TagWrappers.BOLD).wrap("MIXED ITEM DETECTED:"),
            "It seems that the item you provided belongs to two or more custom item providers.",
            "It's recommended to keep the save method as " + TagWrappers.GOLD.wrap("SNBT") + ".",
            "This ensures the exact item data is saved correctly."
        );

    public static final TextLocale GENERIC_UI_DIALOG_ITEM_INPUT_SAVE_METHOD = LangEntry
        .builder("Generic.UI.Dialog.Item.Input.SaveMethod")
        .text("Save Method");

    public static final TextLocale GENERIC_UI_DIALOG_ITEM_SAVE_METHOD_ITEM_REF = LangEntry
        .builder("Generic.UI.Dialog.Item.SaveMethod.Auto")
        .text("Reference (" + TagWrappers.GOLD.wrap(SharedPlaceholders.TYPE) + ")");

    public static final TextLocale GENERIC_UI_DIALOG_ITEM_SAVE_METHOD_SNBT = LangEntry
        .builder("Generic.UI.Dialog.Item.SaveMethod.SNBT")
        .text("SNBT (" + TagWrappers.YELLOW.wrap("Game Default") + ")");

    private Lang() {
    }
}
