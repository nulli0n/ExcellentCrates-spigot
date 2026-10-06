package su.nightexpress.excellentcrates.reward.crate.component.lang;

import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.core.SharedPlaceholders;
import su.nightexpress.nightcore.locale.LangContainer;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.DialogElementLocale;
import su.nightexpress.nightcore.locale.entry.IconLocale;
import su.nightexpress.nightcore.locale.entry.TextLocale;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public final class RewardComponentLang implements LangContainer {

    public static final IconLocale EDITOR_UI_EXTENSION_BUTTON = LangEntry
        .iconBuilder("rewards.editor.ui.extension.button.rewards")
        .accentColor(TagWrappers.GRADIENT.with("#48cae4", "#0096c7"))
        .name("Rewards")
        .appendCurrent("Current", CommonPlaceholders.GENERIC_AMOUNT)
        .br()
        .appendInfo(
            "Manage potential crate drops,",
            "including " + TagWrappers.AQUA.wrap("amount") + ", " + TagWrappers.AQUA.wrap("chances") + ",",
            "and individual reward properties."
        )
        .br()
        .appendClick("Click to edit")
        .build();

    public static final TextLocale EDITOR_UI_INVENTORY_BROWSE_TITLE = LangEntry
        .builder("rewards.editor.ui.inventory.browse.title")
        .text("Crate Editor • Rewards");

    public static final IconLocale EDITOR_UI_INVENTORY_BROWSE_BUTTON_REWARD = LangEntry
        .iconBuilder("rewards.editor.ui.inventory.browse.button.reward")
        .rawName(SharedPlaceholders.REWARD_NAME)
        .rawLore(SharedPlaceholders.REWARD_DESCRIPTION, CommonPlaceholders.EMPTY_IF_ABOVE)
        .appendCurrent("ID", SharedPlaceholders.REWARD_ID)
        .appendCurrent("Weight", SharedPlaceholders.REWARD_WEIGHT)
        .appendCurrent("Rarity", SharedPlaceholders.REWARD_RARITY)
        .appendCurrent("Roll Chance", SharedPlaceholders.REWARD_ROLL_CHANCE + "%")
        .br()
        .appendClick("Click to edit")
        .build();

    public static final IconLocale EDITOR_UI_INVENTORY_BROWSE_BUTTON_EDITOR = LangEntry
        .iconBuilder("rewards.editor.ui.inventory.browse.button.reward_editor")
        .accentColor(TagWrappers.GOLD)
        .name("Reward Editor")
        .appendInfo("Open the main reward editor to create,", "configure, and manage all rewards.")
        .br()
        .appendClick("Click to navigate")
        .build();

    public static final IconLocale EDITOR_UI_INVENTORY_BROWSE_BUTTON_ADD = LangEntry
        .iconBuilder("rewards.editor.ui.inventory.browse.button.add")
        .accentColor(TagWrappers.GREEN)
        .name("Add Reward")
        .appendInfo("Select an existing registered reward", "to link and add into this crate.")
        .br()
        .appendClick("Click to add")
        .build();

    public static final IconLocale EDITOR_UI_INVENTORY_BROWSE_BUTTON_REQUIRED_AMOUNT = LangEntry
        .iconBuilder("rewards.editor.ui.inventory.browse.button.required_amount")
        .accentColor(TagWrappers.GOLD)
        .name("Reward Roll Count")
        .appendCurrent("Current", CommonPlaceholders.GENERIC_VALUE)
        .br()
        .appendInfo("Determines the number of rewards drawn", "and given per single crate opening.")
        .br()
        .appendClick("Click to edit")
        .build();

    public static final TextLocale EDITOR_UI_INVENTORY_SELECTION_TITLE = LangEntry
        .builder("rewards.editor.ui.inventory.selection.title")
        .text("Crate Rewards • Reward Selection");

    public static final IconLocale EDITOR_UI_INVENTORY_SELECTION_BUTTON_REWARD = LangEntry
        .iconBuilder("rewards.editor.ui.inventory.selection.button.reward")
        .rawName(SharedPlaceholders.REWARD_NAME)
        .rawLore(SharedPlaceholders.REWARD_DESCRIPTION, CommonPlaceholders.EMPTY_IF_ABOVE)
        .appendCurrent("ID", SharedPlaceholders.REWARD_ID)
        .appendCurrent("Rarity", SharedPlaceholders.REWARD_RARITY)
        .br()
        .appendClick("Click to select")
        .build();

    public static final TextLocale EDITOR_UI_INVENTORY_OPTIONS_TITLE = LangEntry
        .builder("rewards.editor.ui.inventory.options.title")
        .text("Crate Rewards • Reward Options");

    public static final IconLocale EDITOR_UI_INVENTORY_OPTIONS_BUTTON_ID = LangEntry
        .iconBuilder("rewards.editor.ui.inventory.options.button.reward_id")
        .accentColor(TagWrappers.GOLD)
        .name("Target Reward")
        .appendCurrent("Current", CommonPlaceholders.GENERIC_VALUE)
        .br()
        .appendInfo("Defines which specific reward is", "assigned to this reward entry.")
        .br()
        .appendClick("Click to edit")
        .build();

    public static final IconLocale EDITOR_UI_INVENTORY_OPTIONS_BUTTON_WEIGHT = LangEntry
        .iconBuilder("rewards.editor.ui.inventory.options.button.weight")
        .accentColor(TagWrappers.AQUA)
        .name("Weight")
        .appendCurrent("Weight", SharedPlaceholders.REWARD_WEIGHT)
        .appendCurrent("Roll Chance", SharedPlaceholders.REWARD_ROLL_CHANCE + "%")
        .br()
        .appendInfo("Drop weight determining the roll chance", "relative to the sum of all reward weights.")
        .br()
        .appendClick("Click to edit")
        .build();

    public static final IconLocale EDITOR_UI_INVENTORY_OPTIONS_BUTTON_EDITOR = LangEntry
        .iconBuilder("rewards.editor.ui.inventory.options.button.editor")
        .accentColor(TagWrappers.GOLD)
        .name("Reward Editor")
        .appendInfo("Open the core reward editor to modify", "this reward's global properties and meta.")
        .br()
        .appendClick("Click to navigate")
        .build();

    public static final TextLocale EDITOR_UI_DIALOG_WEIGHT_TITLE = LangEntry
        .builder("rewards.editor.ui.dialog.weight.title")
        .text(TagWrappers.GOLD.and(TagWrappers.UNDERLINED).wrap("Reward Weight"));

    public static final DialogElementLocale EDITOR_UI_DIALOG_WEIGHT_BODY = LangEntry
        .builder("rewards.editor.ui.dialog.weight.body")
        .dialogElement(
            "Set the desired " + TagWrappers.GOLD.wrap("reward weight") + ".",
            "",
            TagWrappers.GRAY.wrap(
                "For simplicity, it is recommended to keep the total weight at " + TagWrappers.WHITE.wrap("100") +
                    " so each weight directly corresponds to its roll chance."
            ),
            "",
            TagWrappers.GRAY.wrap("Current total crate weight:") + " " +
                TagWrappers.AQUA.wrap(SharedPlaceholders.TOTAL),
            TagWrappers.GRAY.wrap("Current reward roll chance:") + " " +
                TagWrappers.AQUA.wrap(SharedPlaceholders.REWARD_ROLL_CHANCE + "%")
        );

    public static final TextLocale EDITOR_UI_DIALOG_WEIGHT_INPUT_WEIGHT = LangEntry
        .builder("rewards.editor.ui.dialog.weight.input.weight")
        .text(TagWrappers.SPRITE_ITEM.apply(Material.IRON_INGOT) + " Weight");

    public static final TextLocale EDITOR_UI_DIALOG_REQUIRED_AMOUNT_TITLE = LangEntry
        .builder("rewards.editor.ui.dialog.required_amount.title")
        .text(TagWrappers.GOLD.and(TagWrappers.UNDERLINED).wrap("Reward Roll Count"));

    public static final DialogElementLocale EDITOR_UI_DIALOG_REQUIRED_AMOUNT_BODY = LangEntry
        .builder("rewards.editor.ui.dialog.required_amount.body")
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

    public static final TextLocale EDITOR_UI_DIALOG_REQUIRED_AMOUNT_INPUT_AMOUNT = LangEntry
        .builder("rewards.editor.ui.dialog.required_amount.input.amount")
        .text(TagWrappers.SPRITE_ITEM.apply(Material.COMPARATOR) + " Roll Count");

    private RewardComponentLang() {
    }
}
