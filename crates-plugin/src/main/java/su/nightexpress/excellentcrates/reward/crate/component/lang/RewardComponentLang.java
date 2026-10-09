package su.nightexpress.excellentcrates.reward.crate.component.lang;

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
public final class RewardComponentLang implements LangContainer {

    public static final MessageLocale ERROR_NO_REWARDS_COMPONENT = LangEntry
        .builder("rewards.error.no_rewards_component")
        .chatMessage("Crate " + TagWrappers.WHITE.wrap(SharedPlaceholders.CRATE_NAME) +
            " has no rewards component."
        );

    public static final IconLocale EDITOR_UI_EXTENSION_BUTTON = LangEntry
        .iconBuilder("rewards.component.editor.ui.extension.button.reward_settings")
        .accentColor(TagWrappers.GRADIENT.with("#48cae4", "#0096c7"))
        .name("Reward Settings")
        .appendCurrent("Rewards", CommonPlaceholders.GENERIC_AMOUNT)
        .br()
        .appendInfo(
            "Manage potential crate drops,",
            "including " + TagWrappers.AQUA.wrap("amount") + ", " + TagWrappers.AQUA.wrap("chances") + ",",
            "and individual reward properties."
        )
        .br()
        .appendClick("Click to edit")
        .build();

    public static final TextLocale EDITOR_UI_INVENTORY_SETTINGS_TITLE = LangEntry
        .builder("rewards.component.editor.ui.inventory.settings.title")
        .text("Crate Editor • Reward Settings");

    public static final IconLocale EDITOR_UI_INVENTORY_SETTINGS_BUTTON_REWARDS = LangEntry
        .iconBuilder("rewards.component.editor.ui.inventory.settings.button.rewards")
        .accentColor(TagWrappers.ORANGE)
        .name("Rewards")
        .appendInfo("Create, configure, and manage all", "crate rewards here.")
        .br()
        .appendClick("Click to navigate")
        .build();

    public static final IconLocale EDITOR_UI_INVENTORY_SETTINGS_BUTTON_ROLL_COUNT = LangEntry
        .iconBuilder("rewards.component.editor.ui.inventory.settings.button.roll_count")
        .accentColor(TagWrappers.GOLD)
        .name("Roll Count")
        .appendCurrent("Current", CommonPlaceholders.GENERIC_VALUE)
        .br()
        .appendInfo("Determines the number of rewards drawn", "and given per single crate opening.")
        .br()
        .appendClick("Click to edit")
        .build();

    public static final TextLocale EDITOR_UI_DIALOG_ROLL_COUNT_TITLE = LangEntry
        .builder("rewards.component.editor.ui.dialog.roll_count.title")
        .text(TagWrappers.GOLD.and(TagWrappers.UNDERLINED).wrap("Reward Roll Count"));

    public static final DialogElementLocale EDITOR_UI_DIALOG_ROLL_COUNT_BODY = LangEntry
        .builder("rewards.component.editor.ui.dialog.roll_count.body")
        .dialogElement(
            "Set the desired " + TagWrappers.GOLD.wrap("reward roll count") + ".",
            "",
            TagWrappers.GRAY.wrap("Specifies how many rewards are drawn per opening."),
            "",
            TagWrappers.RED.wrap("Note:") + " " +
                TagWrappers.GRAY.wrap("This quantity is not guaranteed if rewards are unavailable " +
                    "due to cooldowns, roll limits, or other restrictions."
                )
        );

    public static final TextLocale EDITOR_UI_DIALOG_ROLL_COUNT_INPUT_AMOUNT = LangEntry
        .builder("rewards.component.editor.ui.dialog.roll_count.input.amount")
        .text(TagWrappers.SPRITE_ITEM.apply(Material.COMPARATOR) + " Roll Count");

    private RewardComponentLang() {
    }
}
