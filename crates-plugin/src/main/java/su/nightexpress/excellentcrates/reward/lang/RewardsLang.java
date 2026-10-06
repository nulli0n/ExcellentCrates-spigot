package su.nightexpress.excellentcrates.reward.lang;

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
public class RewardsLang implements LangContainer {

    public static final TextLocale COMMAND_ROOT_DESCRIPTION = LangEntry
        .builder("rewards.command.root.description")
        .text("Root command for managing rewards.");

    public static final TextLocale COMMAND_EDITOR_DESCRIPTION = LangEntry
        .builder("rewards.command.editor.description")
        .text("Editor command for managing rewards.");

    public static final MessageLocale GENERIC_REWARD_NOT_FOUND = LangEntry
        .builder("rewards.generic.reward_not_found")
        .chatMessage("Reward with ID " + TagWrappers.RED.wrap(CommonPlaceholders.GENERIC_VALUE) + " not found.");

    public static final MessageLocale GENERIC_REWARD_COMMAND_BUNDLE_NOT_FOUND = LangEntry
        .builder("rewards.generic.reward_command_bundle_not_found")
        .chatMessage("Reward command bundle with ID " + TagWrappers.RED.wrap(CommonPlaceholders.GENERIC_VALUE) +
            " not found."
        );

    public static final MessageLocale ERROR_NO_REWARDS_COMPONENT = LangEntry
        .builder("rewards.error.no_rewards_component")
        .chatMessage("No rewards component found for crate " + TagWrappers.WHITE.wrap(SharedPlaceholders.CRATE_NAME) +
            ".");

    public static final MessageLocale ERROR_REWARD_NOT_FOUND = LangEntry
        .builder("rewards.error.reward_not_found")
        .chatMessage("Reward with ID " + TagWrappers.RED.wrap(CommonPlaceholders.GENERIC_VALUE) + " not found.");

    public static final MessageLocale ERROR_REWARD_ALREADY_ADDED = LangEntry
        .builder("rewards.error.reward_already_added")
        .chatMessage("Reward with ID " + TagWrappers.RED.wrap(CommonPlaceholders.GENERIC_VALUE) + " is already added.");

    public static final MessageLocale EVALUATION_NO_REWARDS = LangEntry
        .builder("rewards.evaluation.no_rewards")
        .chatMessage(Sound.ENTITY_VILLAGER_NO,
            "Crate " + TagWrappers.WHITE.wrap(SharedPlaceholders.CRATE_NAME) +
                " has no rewards available for you."
        );

    public static final MessageLocale GRANT_NOTIFY = LangEntry
        .builder("rewards.grant.notify")
        .chatMessage("You won " + TagWrappers.WHITE.wrap(SharedPlaceholders.REWARD_NAME) + " from " +
            TagWrappers.WHITE.wrap(SharedPlaceholders.CRATE_NAME) + "!"
        );
}
