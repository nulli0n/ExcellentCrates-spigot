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

    public static final EnumLocale<CooldownMode> COOLDOWN_MODE = LangEntry.builder("enums.cooldown_mode")
        .enumeration(CooldownMode.class);

    public static final RegistryLocale<Particle> PARTICLE = LangEntry.builder("assets.particle")
        .registry(RegistryType.PARTICLE_TYPE);

    public static final TextLocale PLUGIN_COMMAND_ROOT_DESCRIPTION = LangEntry
        .builder("plugin.command.root.description")
        .text("Main command for ExcellentCrates");

    public static final TextLocale PLUGIN_COMMAND_STATUS_DESCRIPTION = LangEntry
        .builder("plugin.command.status.description")
        .text("Show plugin status");

    public static final TextLocale PLUGIN_COMMAND_RELOAD_DESCRIPTION = LangEntry
        .builder("plugin.command.reload.description")
        .text("Reload the plugin");

    public static final MessageLocale CORE_UI_ERROR_MENU_NOT_FOUND = LangEntry
        .builder("core.ui.menu.not_found")
        .chatMessage(TagWrappers.RED.wrap("Menu " +
            TagWrappers.WHITE.wrap(CommonPlaceholders.GENERIC_VALUE) + " not found.")
        );

    public static final MessageLocale CORE_UI_ERROR_DIALOG_NOT_FOUND = LangEntry
        .builder("core.ui.dialog.not_found")
        .chatMessage(TagWrappers.RED.wrap("Dialog " +
            TagWrappers.WHITE.wrap(CommonPlaceholders.GENERIC_VALUE) + " not found.")
        );

    public static final TextLocale COMMAND_ARGUMENT_NAME_X = LangEntry.builder("command.argument.name.x").text("x");
    public static final TextLocale COMMAND_ARGUMENT_NAME_Y = LangEntry.builder("command.argument.name.y").text("y");
    public static final TextLocale COMMAND_ARGUMENT_NAME_Z = LangEntry.builder("command.argument.name.z").text("z");

    public static final MessageLocale COMMAND_SYNTAX_INVALID_ID = LangEntry
        .builder("command.syntax.invalid_id")
        .chatMessage(TagWrappers.RED.wrap(CommonPlaceholders.GENERIC_INPUT) + " is not a valid ID!");

    public static final TextLocale FORMAT_COOLDOWN_READY = LangEntry
        .builder("format.cooldown.ready")
        .text(TagWrappers.GREEN.wrap("Ready"));

    public static final TextLocale FORMAT_COOLDOWN_NONE = LangEntry
        .builder("format.cooldown.none")
        .text(TagWrappers.GREEN.wrap("None"));

    public static final TextLocale FORMAT_COOLDOWN_PERMANENT = LangEntry
        .builder("format.cooldown.permanent")
        .text(TagWrappers.RED.wrap("Permanent"));

    public static final TextLocale FORMAT_LOCATION = LangEntry.builder("format.location")
        .text("[" +
            SharedPlaceholders.X + ", " +
            SharedPlaceholders.Y + ", " +
            SharedPlaceholders.Z +
            "]"
        );

    public static final TextLocale UI_COMMAND_LIST_EMPTY = LangEntry
        .builder("ui.command.list.empty")
        .text(TagWrappers.RED.wrap("No commands defined."));

    public static final TextLocale UI_COMMAND_LIST_ENTRY = LangEntry
        .builder("ui.command.list.entry")
        .text(TagWrappers.WHITE.wrap(TagWrappers.SPRITE_BLOCKS.apply("block/command_block_back") + " /" +
            CommonPlaceholders.GENERIC_ENTRY)
        );

    public static final IconLocale UI_ITEM_PLACEHOLDER = LangEntry
        .iconBuilder("ui.item.placeholder")
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
        .builder("generic.internal.error")
        .chatMessage(TagWrappers.RED.wrap("An internal error has occurred. Check the server console for details."));

    public static final DialogElementLocale GENERIC_UI_DIALOG_COOLDOWN_BODY_MODES = LangEntry
        .builder("generic.ui.dialog.cooldown.body.modes")
        .dialogElement(
            TagWrappers.SPRITE_ITEMS.apply("item/clock_12") + " " + TagWrappers.GOLD.wrap("Daily Mode"),
            "Resets at " + TagWrappers.GOLD.wrap("midnight") + " after number of days set in " + TagWrappers.GOLD.wrap(
                "Duration field") + ".",
            "",
            TagWrappers.SPRITE_ITEM.apply(Material.REPEATER) + " " + TagWrappers.GREEN.wrap("Custom Mode"),
            "Resets after an " + TagWrappers.GREEN.wrap("exact") + " number of seconds set in " + TagWrappers.GREEN
                .wrap("Duration field") + "."
        );

    public static final TextLocale GENERIC_UI_DIALOG_COOLDOWN_INPUT_STATE = LangEntry
        .builder("generic.ui.dialog.cooldown.input.state")
        .text("State");

    public static final TextLocale GENERIC_UI_DIALOG_COOLDOWN_INPUT_MODE = LangEntry
        .builder("generic.ui.dialog.cooldown.input.mode")
        .text("Type");

    public static final TextLocale GENERIC_UI_DIALOG_COOLDOWN_INPUT_DURATION = LangEntry
        .builder("generic.ui.dialog.cooldown.input.duration")
        .text("Duration");

    public static final DialogElementLocale GENERIC_UI_DIALOG_ITEM_BODY_CUSTOM = LangEntry
        .builder("generic.ui.dialog.item.body.custom")
        .dialogElement(
            TagWrappers.GOLD.and(TagWrappers.BOLD).wrap("CUSTOM ITEM DETECTED:"),
            "It seems that the item you provided belongs to " + TagWrappers.GOLD.wrap(
                SharedPlaceholders.TYPE) + ".",
            "If this is correct, ensure the save method is set as " + TagWrappers.GREEN.wrap("Reference") + ".",
            "This ensures the item will reflect all changes you made in its configuration."
        );

    public static final DialogElementLocale GENERIC_UI_DIALOG_ITEM_BODY_MIXED = LangEntry
        .builder("generic.ui.dialog.item.body.mixed")
        .dialogElement(
            TagWrappers.RED.and(TagWrappers.BOLD).wrap("MIXED ITEM DETECTED:"),
            "It seems that the item you provided belongs to two or more custom item providers.",
            "It's recommended to keep the save method as " + TagWrappers.GOLD.wrap("SNBT") + ".",
            "This ensures the exact item data is saved correctly."
        );

    public static final TextLocale GENERIC_UI_DIALOG_ITEM_INPUT_SAVE_METHOD = LangEntry
        .builder("generic.ui.dialog.item.input.saveMethod")
        .text("Save Method");

    public static final TextLocale GENERIC_UI_DIALOG_ITEM_SAVE_METHOD_ITEM_REF = LangEntry
        .builder("generic.ui.dialog.item.saveMethod.auto")
        .text("Reference (" + TagWrappers.GOLD.wrap(SharedPlaceholders.TYPE) + ")");

    public static final TextLocale GENERIC_UI_DIALOG_ITEM_SAVE_METHOD_SNBT = LangEntry
        .builder("generic.ui.dialog.item.saveMethod.snbt")
        .text("SNBT (" + TagWrappers.YELLOW.wrap("Game Default") + ")");

    private Lang() {
    }
}
