package su.nightexpress.excellentcrates.keys.lang;

import org.bukkit.Sound;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.core.SharedPlaceholders;
import su.nightexpress.nightcore.locale.LangContainer;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.MessageLocale;
import su.nightexpress.nightcore.locale.entry.TextLocale;
import su.nightexpress.nightcore.locale.message.MessageData;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public final class KeyLang implements LangContainer {

    public static final TextLocale COMMAND_ROOT_DESCRIPTION = LangEntry
        .builder("keys.command.root.description")
        .text("The root command for crate keys.");

    public static final TextLocale COMMAND_BALANCE_DESCRIPTION = LangEntry
        .builder("keys.command.balance.description")
        .text("Shows the balance of your or another player's keys.");

    public static final TextLocale COMMAND_GIVE_DESCRIPTION = LangEntry
        .builder("keys.command.give.description")
        .text("Gives a key to a player.");

    public static final TextLocale COMMAND_GIVE_ALL_DESCRIPTION = LangEntry
        .builder("keys.command.giveall.description")
        .text("Gives a key to all players.");

    public static final TextLocale COMMAND_REMOVE_DESCRIPTION = LangEntry
        .builder("keys.command.remove.description")
        .text("Removes a key from a player.");

    public static final TextLocale COMMAND_DROP_DESCRIPTION = LangEntry
        .builder("keys.command.drop.description")
        .text("Drops a key at a specified location.");

    public static final TextLocale COMMAND_REDEEM_DESCRIPTION = LangEntry
        .builder("keys.command.redeem.description")
        .text("Redeems your unclaimed keys.");

    public static final MessageLocale COMMAND_SYNTAX_INVALID_KEY = LangEntry
        .builder("keys.command.syntax.invalid_key")
        .chatMessage("Invalid key specified: " + TagWrappers.RED.wrap(CommonPlaceholders.GENERIC_INPUT)
        );

    public static final MessageLocale BALANCE_EMPTY_SELF = LangEntry
        .builder("keys.balance.empty.self")
        .chatMessage("You have no keys in your balance.");

    public static final MessageLocale BALANCE_EMPTY_OTHERS = LangEntry
        .builder("keys.balance.empty.others")
        .chatMessage(TagWrappers.WHITE.wrap(CommonPlaceholders.PLAYER_NAME) + " has no keys in their balance.");

    public static final MessageLocale BALANCE_HEADER_SELF = LangEntry
        .builder("keys.balance.header.self")
        .message(MessageData.CHAT_NO_PREFIX,
            TagWrappers.YELLOW.and(TagWrappers.BOLD).wrap("YOUR KEYS BALANCE:"),
            " ",
            TagWrappers.GRAY.and(TagWrappers.ITALIC).wrap(
                TagWrappers.GREEN.wrap("I - Inventory") + " / " +
                    TagWrappers.AQUA.wrap("V - Virtual") + " / " +
                    TagWrappers.GOLD.wrap("U - Unclaimed")
            ),
            " "
        );

    public static final MessageLocale BALANCE_HEADER_OTHERS = LangEntry
        .builder("keys.balance.header.others")
        .message(MessageData.CHAT_NO_PREFIX,
            TagWrappers.YELLOW.and(TagWrappers.BOLD).wrap(
                "KEYS BALANCE FOR " + TagWrappers.WHITE.wrap(CommonPlaceholders.PLAYER_NAME) + ":"
            ),
            " ",
            TagWrappers.GRAY.and(TagWrappers.ITALIC).wrap(
                TagWrappers.GREEN.wrap("I - Inventory") + " / " +
                    TagWrappers.AQUA.wrap("V - Virtual") + " / " +
                    TagWrappers.GOLD.wrap("U - Unclaimed")
            ),
            " "
        );

    public static final MessageLocale BALANCE_ENTRY = LangEntry
        .builder("keys.balance.inventory.entry")
        .message(MessageData.CHAT_NO_PREFIX,
            TagWrappers.DARK_GRAY.wrap("»") + " " +
                TagWrappers.WHITE.wrap(SharedPlaceholders.KEY_NAME + ": ") +
                TagWrappers.GRAY.wrap(
                    TagWrappers.GREEN.wrap("I:") + " " + TagWrappers.WHITE.wrap("x%inventory%") + " / " +
                        TagWrappers.AQUA.wrap("V:") + " " + TagWrappers.WHITE.wrap("x%virtual%") + " / " +
                        TagWrappers.GOLD.wrap("U:") + " " + TagWrappers.WHITE.wrap("x%unclaimed%")
                )
        );

    public static final MessageLocale KEY_GIVE_FEEDBACK = LangEntry
        .builder("keys.key.give.feedback")
        .chatMessage("You gave " + TagWrappers.WHITE.wrap("x" + CommonPlaceholders.GENERIC_AMOUNT) + " " +
            TagWrappers.WHITE.wrap(SharedPlaceholders.KEY_NAME) + " to " +
            TagWrappers.WHITE.wrap(CommonPlaceholders.PLAYER_NAME) + "."
        );

    public static final MessageLocale KEY_GIVE_ALL_FEEDBACK = LangEntry
        .builder("keys.key.giveall.feedback")
        .chatMessage("You gave " + TagWrappers.WHITE.wrap("x" + CommonPlaceholders.GENERIC_AMOUNT) + " " +
            TagWrappers.WHITE.wrap(SharedPlaceholders.KEY_NAME) + " to " +
            TagWrappers.WHITE.wrap(SharedPlaceholders.TOTAL) + " eligible online players."
        );

    public static final MessageLocale KEY_GIVE_NOTIFY = LangEntry
        .builder("keys.key.give.notify")
        .chatMessage("You have been given " + TagWrappers.WHITE.wrap("x" + CommonPlaceholders.GENERIC_AMOUNT) + " " +
            TagWrappers.WHITE.wrap(SharedPlaceholders.KEY_NAME) + "."
        );

    public static final MessageLocale KEY_REMOVE_FEEDBACK = LangEntry
        .builder("keys.key.remove.feedback")
        .chatMessage("You removed " + TagWrappers.WHITE.wrap("x" + CommonPlaceholders.GENERIC_AMOUNT) + " " +
            TagWrappers.WHITE.wrap(SharedPlaceholders.KEY_NAME) + " from " +
            TagWrappers.WHITE.wrap(CommonPlaceholders.PLAYER_NAME) + "."
        );

    public static final MessageLocale KEY_REMOVE_NOTIFY = LangEntry
        .builder("keys.key.remove.notify")
        .chatMessage("You have had " + TagWrappers.WHITE.wrap("x" + CommonPlaceholders.GENERIC_AMOUNT) + " " +
            TagWrappers.WHITE.wrap(SharedPlaceholders.KEY_NAME) + " removed."
        );

    public static final MessageLocale KEY_REMOVE_NOT_ENOUGH = LangEntry
        .builder("keys.key.remove.not_enough")
        .chatMessage("Player " + TagWrappers.WHITE.wrap(CommonPlaceholders.PLAYER_NAME) + " does not have " +
            TagWrappers.WHITE.wrap("x" + CommonPlaceholders.GENERIC_AMOUNT) + " " +
            TagWrappers.WHITE.wrap(SharedPlaceholders.KEY_NAME) + " to remove."
        );

    public static final MessageLocale KEY_DROP_FEEDBACK = LangEntry
        .builder("keys.key.drop.feedback")
        .chatMessage(Sound.ENTITY_ITEM_PICKUP,
            "You have dropped " + TagWrappers.WHITE.wrap("x" + CommonPlaceholders.GENERIC_AMOUNT) + " " +
                TagWrappers.WHITE.wrap(SharedPlaceholders.KEY_NAME) + " at " +
                TagWrappers.WHITE.wrap(SharedPlaceholders.LOCATION) + " in " +
                TagWrappers.WHITE.wrap(SharedPlaceholders.WORLD) + "."
        );

    public static final MessageLocale KEY_REDEEM_FEEDBACK = LangEntry
        .builder("keys.key.redeem.feedback")
        .chatMessage("You have successfully redeemed " +
            TagWrappers.WHITE.wrap("x" + CommonPlaceholders.GENERIC_AMOUNT) + " " +
            TagWrappers.WHITE.wrap(SharedPlaceholders.KEY_NAME) + ".");

    public static final MessageLocale KEY_REDEEM_NO_KEYS = LangEntry
        .builder("keys.key.redeem.no_keys")
        .chatMessage("You have no keys to redeem.");

    public static final MessageLocale KEY_REDEEM_NO_UNCLAIMED = LangEntry
        .builder("keys.key.redeem.no_unclaimed")
        .chatMessage("You have no unclaimed " + TagWrappers.WHITE.wrap(SharedPlaceholders.KEY_NAME) + ".");

    public static final MessageLocale ERROR_ITEM_CREATION_FAILED = LangEntry
        .builder("keys.error.item_creation_failed")
        .chatMessage("Failed to create item for " + TagWrappers.WHITE.wrap(SharedPlaceholders.KEY_NAME) + ".");


    private KeyLang() {
    }
}
