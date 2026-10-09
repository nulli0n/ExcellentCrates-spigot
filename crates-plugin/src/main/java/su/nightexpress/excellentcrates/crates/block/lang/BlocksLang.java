package su.nightexpress.excellentcrates.crates.block.lang;

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
public final class BlocksLang implements LangContainer {

    public static final TextLocale COMMAND_GET_BLOCK_DESCRIPTION = LangEntry
        .builder("crates.blocks.command.get_block.description")
        .text("Get a crate block item.");

    public static final TextLocale COMMAND_ASSIGN_DESCRIPTION = LangEntry
        .builder("crates.blocks.command.assign.description")
        .text("Assign a crate to a block in hand.");

    public static final MessageLocale COMMAND_SYNTAX_INVALID_BLOCK_KEY = LangEntry
        .builder("crates.blocks.command.syntax.invalid_block_key")
        .chatMessage("Invalid block key: " + TagWrappers.RED.wrap(CommonPlaceholders.GENERIC_INPUT) + ".");

    public static final MessageLocale COMMAND_SYNTAX_INVALID_BLOCK_ARGUMENT = LangEntry
        .builder("crates.blocks.command.syntax.invalid_block_argument")
        .chatMessage("Invalid block specified: " + TagWrappers.RED.wrap(CommonPlaceholders.GENERIC_INPUT) + ".");

    public static final MessageLocale ERROR_NO_BLOCKS_COMPONENT = LangEntry
        .builder("crates.blocks.error.no_blocks_component")
        .chatMessage(
            Sound.ENTITY_VILLAGER_NO,
            "The crate " + TagWrappers.WHITE.wrap(SharedPlaceholders.CRATE_NAME) +
                " has no blocks component."
        );

    public static final MessageLocale ASSIGN_NO_ITEM_IN_HAND = LangEntry
        .builder("crates.blocks.assign.no_item_in_hand")
        .chatMessage(
            Sound.ENTITY_VILLAGER_NO,
            "No block item in hand to assign the crate to."
        );

    public static final MessageLocale ASSIGN_NOT_A_BLOCK = LangEntry
        .builder("crates.blocks.assign.not_a_block")
        .chatMessage(
            Sound.ENTITY_VILLAGER_NO,
            "The given item is not a valid/supported block or a furniture."
        );

    public static final MessageLocale ASSIGN_ALREADY_ASSIGNED = LangEntry
        .builder("crates.blocks.assign.already_assigned")
        .chatMessage(
            Sound.ENTITY_VILLAGER_NO,
            "The given block is already assigned to a crate."
        );

    public static final MessageLocale ASSIGN_SUCCESS = LangEntry
        .builder("crates.blocks.assign.success")
        .chatMessage(
            Sound.BLOCK_NOTE_BLOCK_PLING,
            "The crate " + TagWrappers.WHITE.wrap(SharedPlaceholders.CRATE_NAME) +
                " has been successfully assigned to the given block."
        );

    public static final MessageLocale LINK_SUCCESS = LangEntry
        .builder("crates.blocks.link.success")
        .chatMessage(
            Sound.ENTITY_EXPERIENCE_ORB_PICKUP,
            "The crate " + TagWrappers.WHITE.wrap(SharedPlaceholders.CRATE_NAME) +
                " has been successfully linked to the block."
        );

    public static final MessageLocale UNLINK_SUCCESS = LangEntry
        .builder("crates.blocks.unlink.success")
        .chatMessage(
            Sound.ENTITY_ITEM_BREAK,
            "The crate " + TagWrappers.WHITE.wrap(SharedPlaceholders.CRATE_NAME) +
                " has been successfully unlinked from the block."
        );

    public static final MessageLocale ITEM_ERROR_NO_ITEM_STACK = LangEntry
        .builder("crates.blocks.item.error.no_item_stack")
        .chatMessage(
            Sound.ENTITY_VILLAGER_NO,
            "No item stack could be generated for the " +
                TagWrappers.RED.wrap(CommonPlaceholders.GENERIC_VALUE) + " crate block."
        );

    public static final MessageLocale ITEM_OBTAIN_SUCCESS = LangEntry
        .builder("crates.blocks.item.obtain.success")
        .chatMessage(
            Sound.ENTITY_ITEM_PICKUP,
            "You obtained " + TagWrappers.WHITE.wrap(CommonPlaceholders.GENERIC_ITEM) +
                " crate block item."
        );

    public static final MessageLocale ITEM_GIVE_SUCCESS = LangEntry
        .builder("crates.blocks.item.give.success")
        .chatMessage(
            Sound.ITEM_ARMOR_EQUIP_LEATHER,
            "You gave " + TagWrappers.WHITE.wrap(CommonPlaceholders.GENERIC_ITEM) +
                " crate block item to " + TagWrappers.WHITE.wrap(CommonPlaceholders.PLAYER_NAME) + "."
        );

    public static final MessageLocale ITEM_GIVE_NOTIFY = LangEntry
        .builder("crates.blocks.item.give.notify")
        .chatMessage(
            Sound.ENTITY_ITEM_PICKUP,
            "You received " + TagWrappers.WHITE.wrap(CommonPlaceholders.GENERIC_ITEM) +
                " crate block item."
        );

    public static final MessageLocale EDITOR_POSITION_UNLINK_SUCCESS = LangEntry
        .builder("crates.blocks.editor.position.unlink.success")
        .chatMessage(
            Sound.ENTITY_ITEM_BREAK,
            "All blocks have been unlinked from the crate " +
                TagWrappers.WHITE.wrap(SharedPlaceholders.CRATE_NAME) + "."
        );

    public static final IconLocale EDITOR_UI_EXTENSION_BUTTON = LangEntry
        .iconBuilder("crates.blocks.editor.ui.extension.button")
        .accentColor(TagWrappers.GRADIENT.with("#15803D", "#4ADE80"))
        .name("World Blocks")
        .appendInfo(
            "Obtain placeable blocks to interact",
            "with this crate in the world, or",
            TagWrappers.WHITE.wrap("unlink") + " all existing placed blocks."
        )
        .br()
        .appendClick("Click to navigate")
        .build();

    public static final TextLocale EDITOR_UI_INVENTORY_COMPONENT_TITLE = LangEntry
        .builder("crates.blocks.editor.ui.inventory.component.title")
        .text("Crate Editor • Blocks");

    public static final IconLocale EDITOR_UI_INVENTORY_COMPONENT_BUTTON_ASSIGN = LangEntry
        .iconBuilder("crates.blocks.editor.ui.inventory.component.button.assign")
        .accentColor(TagWrappers.GREEN)
        .name("Assign Crate")
        .appendInfo("Create a placeable block for this crate",
            "by " + TagWrappers.GREEN.wrap("dragging an item") + " onto this button."
        )
        .build();

    public static final IconLocale EDITOR_UI_INVENTORY_COMPONENT_BUTTON_UNLINK = LangEntry
        .iconBuilder("crates.blocks.editor.ui.inventory.component.button.unlink")
        .accentColor(TagWrappers.RED)
        .name("Unlink Blocks")
        .appendCurrent("Active Links", CommonPlaceholders.GENERIC_AMOUNT)
        .br()
        .appendInfo("Click to unlink all blocks", "from the crate.")
        .br()
        .appendClick("Click to unlink")
        .build();

    public static final TextLocale EDITOR_UI_DIALOG_UNLINK_TITLE = LangEntry
        .builder("crates.blocks.editor.ui.dialog.unlink.title")
        .text("Unlink Confirmation");

    public static final DialogElementLocale EDITOR_UI_DIALOG_UNLINK_CONTENT = LangEntry
        .builder("crates.blocks.editor.ui.dialog.unlink.content")
        .dialogElement("Are you sure you want to unlink " + TagWrappers.RED.wrap("all blocks") + " from this crate?");
}
