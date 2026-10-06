package su.nightexpress.excellentcrates.reward.feature.cooldown.lang;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.core.SharedPlaceholders;
import su.nightexpress.nightcore.locale.LangContainer;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.IconLocale;
import su.nightexpress.nightcore.locale.entry.MessageLocale;
import su.nightexpress.nightcore.locale.entry.TextLocale;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public final class RewardCooldownsLang implements LangContainer {

    public static final MessageLocale ERROR_NO_COOLDOWN_COMPONENT = LangEntry
        .builder("rewards.cooldowns.error.no_cooldown_component")
        .chatMessage("Reward " + TagWrappers.WHITE.wrap(SharedPlaceholders.REWARD_NAME) +
            " has no cooldown component."
        );

    public static final IconLocale UI_EXTENSION_BUTTON = LangEntry.iconBuilder(
        "rewards.cooldowns.ui.extension.button.cooldowns")
        .name("Cooldowns")
        .appendInfo("Configure " + TagWrappers.YELLOW.wrap("global") + " and " +
            TagWrappers.YELLOW.wrap("player") + " cooldowns",
            "to restrict how frequently this reward",
            "can be won over time."
        )
        .br()
        .appendClick("Click to edit")
        .build();

    public static final TextLocale EDITOR_UI_INVENTORY_COOLDOWNS_TITLE = LangEntry
        .builder("rewards.cooldowns.editor.ui.inventory.cooldowns.title")
        .text("Reward Options • Cooldowns");

    public static final IconLocale EDITOR_UI_INVENTORY_COOLDOWNS_GLOBAL_BUTTON = LangEntry
        .iconBuilder("rewards.cooldowns.editor.ui.inventory.cooldowns.global.button")
        .accentColor(TagWrappers.AQUA)
        .name("Global Cooldown")
        .appendCurrent("State", SharedPlaceholders.STATE)
        .br()
        .appendInfo("Controls the global cooldown", "for this reward.")
        .br()
        .appendClick("Click to edit")
        .build();

    public static final IconLocale EDITOR_UI_INVENTORY_COOLDOWNS_PLAYER_BUTTON = LangEntry
        .iconBuilder("rewards.cooldowns.editor.ui.inventory.cooldowns.player.button")
        .accentColor(TagWrappers.ORANGE)
        .name("Player Cooldown")
        .appendCurrent("State", SharedPlaceholders.STATE)
        .br()
        .appendInfo("Controls the per-player cooldown", "for this reward.")
        .br()
        .appendClick("Click to edit")
        .build();

    public static final TextLocale EDITOR_UI_DIALOG_COOLDOWN_SETTINGS_TITLE = LangEntry
        .builder("rewards.cooldowns.editor.ui.dialog.cooldown.settings.title")
        .text(TagWrappers.GOLD.and(TagWrappers.UNDERLINED).wrap("Reward Cooldown Options"));

    public static final TextLocale EDITOR_UI_DIALOG_COOLDOWN_SETTINGS_BODY_MAIN = LangEntry
        .builder("rewards.cooldowns.editor.ui.dialog.cooldown.settings.body.main")
        .text(
            "Cooldown configuration."
        );

    public static final TextLocale EDITOR_UI_DIALOG_COOLDOWN_SETTINGS_BODY_GLOBAL = LangEntry
        .builder("rewards.cooldowns.editor.ui.dialog.cooldown.settings.body.global")
        .text(
            TagWrappers.AQUA.and(TagWrappers.BOLD).wrap("Global Cooldown"),
            "Applies to " + TagWrappers.AQUA.wrap("all players") + ".",
            "If a player wins the reward, all players must wait for the cooldown to expire before they can win the reward again."
        );

    public static final TextLocale EDITOR_UI_DIALOG_COOLDOWN_SETTINGS_BODY_PLAYER = LangEntry
        .builder("rewards.cooldowns.editor.ui.dialog.cooldown.settings.body.player")
        .text(
            TagWrappers.ORANGE.and(TagWrappers.BOLD).wrap("Player Cooldown"),
            "Applies to " + TagWrappers.ORANGE.wrap("individual players") + ".",
            "If a player wins the reward, they must wait for the cooldown to expire before they can win the reward again.",
            TagWrappers.GRAY.wrap("Other players are unaffected.")
        );

    private RewardCooldownsLang() {
    }
}
