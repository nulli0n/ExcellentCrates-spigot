package su.nightexpress.excellentcrates.crates.item.lang;

import org.bukkit.Sound;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.core.SharedPlaceholders;
import su.nightexpress.nightcore.locale.LangContainer;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.MessageLocale;
import su.nightexpress.nightcore.locale.entry.TextLocale;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public final class CrateItemLang implements LangContainer {

    public static final TextLocale COMMAND_GET_DESCRIPTION = LangEntry
        .builder("crates.item.command.get.description")
        .text("Get crate item.");

    public static final MessageLocale COMMAND_GET_FEEDBACK = LangEntry
        .builder("crates.item.command.get.feedback")
        .chatMessage(Sound.ENTITY_ITEM_PICKUP,
            "You have received " + TagWrappers.WHITE.wrap("x" + CommonPlaceholders.GENERIC_AMOUNT) + " " +
                TagWrappers.WHITE.wrap(SharedPlaceholders.CRATE_NAME) + "."
        );

    public static final TextLocale COMMAND_GIVE_DESCRIPTION = LangEntry
        .builder("crates.item.command.give.description")
        .text("Give crate item to a player.");

    public static final MessageLocale COMMAND_GIVE_FEEDBACK = LangEntry
        .builder("crates.item.command.give.feedback")
        .chatMessage(Sound.ENTITY_ITEM_PICKUP,
            "You gave " + TagWrappers.WHITE.wrap("x" + CommonPlaceholders.GENERIC_AMOUNT) + " " +
                TagWrappers.WHITE.wrap(SharedPlaceholders.CRATE_NAME) + " to " +
                TagWrappers.WHITE.wrap(CommonPlaceholders.PLAYER_NAME) + "."
        );

    public static final TextLocale COMMAND_DROP_DESCRIPTION = LangEntry
        .builder("crates.item.command.drop.description")
        .text("Drop crate item in the world.");

    public static final MessageLocale COMMAND_DROP_FEEDBACK = LangEntry
        .builder("crates.item.command.drop.feedback")
        .chatMessage(Sound.ENTITY_ITEM_PICKUP,
            "You have dropped " + TagWrappers.WHITE.wrap("x" + CommonPlaceholders.GENERIC_AMOUNT) + " " +
                TagWrappers.WHITE.wrap(SharedPlaceholders.CRATE_NAME) + " at " +
                TagWrappers.WHITE.wrap(SharedPlaceholders.LOCATION) + " in " +
                TagWrappers.WHITE.wrap(SharedPlaceholders.WORLD) + "."
        );

    public static final MessageLocale PIPELINE_NO_ITEM_FOUND = LangEntry
        .builder("crates.item.pipeline.no_item_found")
        .chatMessage(Sound.ENTITY_VILLAGER_NO,
            "You must hold a crate item."
        );

    public static final MessageLocale PIPELINE_ITEM_TAKEN = LangEntry
        .builder("crates.item.pipeline.item_taken")
        .chatMessage(Sound.ENTITY_ITEM_PICKUP,
            "You used " + TagWrappers.WHITE.wrap(SharedPlaceholders.ITEM) + "."
        );

    private CrateItemLang() {
    }
}
