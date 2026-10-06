package su.nightexpress.excellentcrates.reward.feature.limit.lang;

import org.bukkit.Material;
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
public class RewardLimitsLang implements LangContainer {

    public static final MessageLocale GENERIC_NO_LIMIT_COMPONENT = LangEntry
        .builder("rewards.limits.generic.no_limit_component")
        .chatMessage("Reward " + TagWrappers.RED.wrap(SharedPlaceholders.REWARD_ID) + " has no limit component.");

    public static final MessageLocale ALTERNATIVE_REWARD_EVALUATION_FAILED = LangEntry
        .builder("rewards.limits.alternative_reward_evaluation_failed")
        .chatMessage("We were unable to evaluate some of the rewards. Please try again later.");

    public static final IconLocale UI_EXTENSION_BUTTON = LangEntry.iconBuilder(
        "rewards.limits.ui.extension.button.limits")
        .name("Win Limits")
        .appendCurrent("State", SharedPlaceholders.STATE)
        .br()
        .appendInfo("Configure " + TagWrappers.AQUA.wrap("global") + " and " +
            TagWrappers.AQUA.wrap("player") + " win caps,",
            "permanently restricting the reward",
            "to a maximum number of claims."
        )
        .br()
        .appendClick("Click to navigate")
        .build();

    public static final TextLocale EDITOR_UI_INVENTORY_LIMITS_TITLE = LangEntry
        .builder("rewards.limits.editor.ui.inventory.limits.title")
        .text("Reward Options • Limits");

    public static final IconLocale EDITOR_UI_INVENTORY_LIMITS_BUTTON_STATE = LangEntry
        .iconBuilder("rewards.limits.editor.ui.inventory.limits.button.state")
        .name("State")
        .appendCurrent("State", SharedPlaceholders.STATE)
        .br()
        .appendInfo("Toggles the limits feature.")
        .br()
        .appendClick("Click to toggle")
        .build();

    public static final IconLocale EDITOR_UI_INVENTORY_LIMITS_BUTTON_GLOBAL = LangEntry
        .iconBuilder("rewards.limits.editor.ui.inventory.limits.button.global")
        .accentColor(TagWrappers.AQUA)
        .name("Global Limit")
        .appendCurrent("State", SharedPlaceholders.STATE)
        .br()
        .appendInfo("Controls the global limit", "for this reward.")
        .br()
        .appendClick("Click to edit")
        .build();

    public static final IconLocale EDITOR_UI_INVENTORY_LIMITS_BUTTON_INDIVIDUAL = LangEntry
        .iconBuilder("rewards.limits.editor.ui.inventory.limits.button.individual")
        .accentColor(TagWrappers.ORANGE)
        .name("Individual Limit")
        .appendCurrent("State", SharedPlaceholders.STATE)
        .br()
        .appendInfo("Controls the per-player limit", "for this reward.")
        .br()
        .appendClick("Click to edit")
        .build();

    public static final IconLocale EDITOR_UI_INVENTORY_LIMITS_BUTTON_ALTERNATIVE_STATE = LangEntry
        .iconBuilder("rewards.limits.editor.ui.inventory.limits.button.alternative_state")
        .name("Alt. Reward State")
        .appendCurrent("State", SharedPlaceholders.STATE)
        .br()
        .appendInfo("When enabled, players will receive",
            "the alternative reward if the main",
            "reward's limit has been reached."
        )
        .br()
        .appendClick("Click to toggle")
        .build();

    public static final IconLocale EDITOR_UI_INVENTORY_LIMITS_BUTTON_ALTERNATIVE_REWARD_ID = LangEntry
        .iconBuilder("rewards.limits.editor.ui.inventory.limits.button.alternative_reward_id")
        .name("Alt. Reward ID")
        .appendCurrent("Current", CommonPlaceholders.GENERIC_VALUE)
        .br()
        .appendInfo("Specifies the ID of the",
            "alternative reward to be given",
            "when the main reward's limit is reached."
        )
        .br()
        .appendClick("Click to edit")
        .build();

    public static final TextLocale EDITOR_UI_INVENTORY_ALTERNATIVE_REWARD_TITLE = LangEntry
        .builder("rewards.limits.editor.ui.inventory.alternative_reward.title")
        .text("Reward Limits • Alt. Reward Selection");

    public static final IconLocale EDITOR_UI_INVENTORY_ALTERNATIVE_REWARD_BUTTON_REWARD = LangEntry
        .iconBuilder("rewards.limits.editor.ui.inventory.alternative_reward.button.reward")
        .rawName(SharedPlaceholders.REWARD_NAME)
        .rawLore(SharedPlaceholders.REWARD_DESCRIPTION)
        .br()
        .appendClick("Click to select")
        .build();

    public static final TextLocale EDITOR_UI_DIALOG_LIMIT_OPTIONS_TITLE = LangEntry
        .builder("rewards.limits.editor.ui.dialog.limit_options.title")
        .text(TagWrappers.GOLD.and(TagWrappers.UNDERLINED).wrap("Reward Limit Options"));

    public static final DialogElementLocale EDITOR_UI_DIALOG_LIMIT_OPTIONS_BODY_MAIN = LangEntry
        .builder("rewards.limits.editor.ui.dialog.limit_options.body.main")
        .dialogElement(
            TagWrappers.GOLD.and(TagWrappers.BOLD).wrap("Amount"),
            "Specifies the number of times the reward can be obtained."
        );

    public static final DialogElementLocale EDITOR_UI_DIALOG_LIMIT_OPTIONS_BODY_GLOBAL = LangEntry
        .builder("rewards.limits.editor.ui.dialog.limit_options.body.global")
        .dialogElement(
            TagWrappers.AQUA.and(TagWrappers.BOLD).wrap("Global Limit"),
            "Applies to " + TagWrappers.AQUA.wrap("all players") + ".",
            "Once the reward rolled the specified number of times by any player, nobody will be able to receive it."
        );

    public static final DialogElementLocale EDITOR_UI_DIALOG_LIMIT_OPTIONS_BODY_PLAYER = LangEntry
        .builder("rewards.limits.editor.ui.dialog.limit_options.body.player")
        .dialogElement(
            TagWrappers.ORANGE.and(TagWrappers.BOLD).wrap("Player Limit"),
            "Applies to " + TagWrappers.ORANGE.wrap("individual players") + ".",
            "Once the reward rolled the specified number of times by the player, they will no longer be able to receive it.",
            TagWrappers.GRAY.wrap("Other players are unaffected.")
        );

    public static final DialogElementLocale EDITOR_UI_DIALOG_LIMIT_OPTIONS_BODY_ALTERNATIVE = LangEntry
        .builder("rewards.limits.editor.ui.dialog.limit_options.body.alternative")
        .dialogElement(
            TagWrappers.PURPLE.and(TagWrappers.BOLD).wrap("Alternative Reward"),
            "You can set an alternative reward that will be given if the main reward's limit is reached.",
            "The alternative reward will ignore all of its own limits and requirements.",
            "",
            TagWrappers.GRAY.wrap(
                "This allows you to provide a fallback reward for players, allowing them to roll limited rewards over and over again."
            )
        );

    public static final TextLocale EDITOR_UI_DIALOG_LIMIT_OPTIONS_INPUT_ENABLED = LangEntry
        .builder("rewards.limits.editor.ui.dialog.limit_options.input.enabled")
        .text(TagWrappers.SPRITE_ITEM.apply(Material.LANTERN) + " Enabled");

    public static final TextLocale EDITOR_UI_DIALOG_LIMIT_OPTIONS_INPUT_AMOUNT = LangEntry
        .builder("rewards.limits.editor.ui.dialog.limit_options.input.amount")
        .text(TagWrappers.SPRITE_ITEM.apply(Material.REPEATER) + " Amount");
}
