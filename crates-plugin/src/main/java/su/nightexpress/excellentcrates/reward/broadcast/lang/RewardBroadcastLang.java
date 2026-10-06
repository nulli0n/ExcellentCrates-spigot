package su.nightexpress.excellentcrates.reward.broadcast.lang;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.core.SharedPlaceholders;
import su.nightexpress.nightcore.locale.LangContainer;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.IconLocale;
import su.nightexpress.nightcore.locale.entry.MessageLocale;
import su.nightexpress.nightcore.locale.entry.TextLocale;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public final class RewardBroadcastLang implements LangContainer {

    public static final MessageLocale ERROR_NO_BROADCAST_COMPONENT = LangEntry
        .builder("rewards.broadcast.error.no_broadcast_component")
        .chatMessage("Reward " + TagWrappers.WHITE.wrap(SharedPlaceholders.REWARD_NAME) +
            " has no broadcast component."
        );

    public static final IconLocale EDITOR_UI_EXTENSION_BUTTON = LangEntry.iconBuilder(
        "rewards.broadcast.ui.extension.button")
        .name("Broadcast Settings")
        .appendCurrent("State", SharedPlaceholders.STATE)
        .br()
        .appendInfo("Configure reward broadcast settings.")
        .br()
        .appendClick("Click to edit")
        .build();

    public static final TextLocale EDITOR_UI_INVENTORY_SETTINGS_TITLE = LangEntry
        .builder("rewards.broadcast.editor.ui.inventory.settings.title")
        .text("Reward Options • Broadcast");

    public static final IconLocale EDITOR_UI_INVENTORY_SETTINGS_BUTTON_STATE = LangEntry
        .iconBuilder("rewards.broadcast.editor.ui.inventory.settings.button.state")
        .name("State")
        .appendCurrent("Current", SharedPlaceholders.STATE)
        .br()
        .appendInfo("Controls whether winning this reward", "is broadcasted to all players.")
        .br()
        .appendClick("Click to toggle")
        .build();

    private RewardBroadcastLang() {
    }
}
