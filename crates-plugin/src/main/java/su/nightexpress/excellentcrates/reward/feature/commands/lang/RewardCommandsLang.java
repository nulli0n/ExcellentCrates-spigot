package su.nightexpress.excellentcrates.reward.feature.commands.lang;

import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.reward.commands.RewardCommandExecutionMode;
import su.nightexpress.excellentcrates.core.SharedLinks;
import su.nightexpress.excellentcrates.core.SharedPlaceholders;
import su.nightexpress.nightcore.locale.LangContainer;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.DialogElementLocale;
import su.nightexpress.nightcore.locale.entry.EnumLocale;
import su.nightexpress.nightcore.locale.entry.IconLocale;
import su.nightexpress.nightcore.locale.entry.TextLocale;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public class RewardCommandsLang implements LangContainer {

    public static final EnumLocale<RewardCommandExecutionMode> GIVE_MODE = LangEntry
        .builder("rewards.commands.give_mode")
        .enumeration(RewardCommandExecutionMode.class);

    public static final IconLocale UI_EXTENSION_BUTTON = LangEntry
        .iconBuilder("rewards.commands.editor.ui.extension.button")
        .name("Commands")
        .appendCurrent("Pools", CommonPlaceholders.GENERIC_AMOUNT)
        .br()
        .appendInfo("Configure command pools executed upon",
            "winning, with custom " + TagWrappers.GOLD.wrap("weights") + ", " +
                TagWrappers.GOLD.wrap("modes") + ",",
            "and execution iterations."
        )
        .br()
        .appendClick("Click to navigate")
        .build();

    public static final TextLocale UI_INVENTORY_COMMANDS_TITLE = LangEntry
        .builder("rewards.commands.editor.ui.inventory.commands.title")
        .text("Reward Options • Commands");

    public static final IconLocale UI_INVENTORY_COMMANDS_BUNDLE = LangEntry
        .iconBuilder("rewards.commands.editor.ui.inventory.commands.bundle")
        .name("Pool #" + CommonPlaceholders.GENERIC_VALUE)
        .appendCurrent("Weight", SharedPlaceholders.WEIGHT)
        .appendCurrent("Commands", "")
        .rawLore(SharedPlaceholders.VALUES)
        .br()
        .appendInfo("Press " + TagWrappers.RED.wrap("[" + TagWrappers.KEY.apply("key.drop") + "]") +
            " to delete this pool.")
        .build();

    public static final IconLocale UI_INVENTORY_COMMANDS_BUTTON_STATE = LangEntry
        .iconBuilder("rewards.commands.editor.ui.inventory.commands.button.state")
        .name("State")
        .appendCurrent("State", CommonPlaceholders.GENERIC_VALUE)
        .br()
        .appendInfo("Toggles the reward commands feature.")
        .br()
        .appendClick("Click to toggle")
        .build();

    public static final IconLocale UI_INVENTORY_COMMANDS_BUTTON_ITERATIONS = LangEntry
        .iconBuilder("rewards.commands.editor.ui.inventory.commands.button.iterations")
        .name("Pool Iterations")
        .appendCurrent("Current", CommonPlaceholders.GENERIC_VALUE)
        .br()
        .appendInfo("Sets how many roll cycles are run",
            "for command pools upon winning.",
            "",
            TagWrappers.DARK_GRAY.wrap("Best used with " + TagWrappers.WHITE.wrap("Weighted") + " mode to pick"),
            TagWrappers.DARK_GRAY.wrap("a random pool on each iteration.")
        )
        .br()
        .appendClick("Click to edit")
        .build();

    public static final IconLocale UI_INVENTORY_COMMANDS_BUTTON_GIVE_MODE = LangEntry
        .iconBuilder("rewards.commands.editor.ui.inventory.commands.button.give_mode")
        .name("Execution Mode")
        .appendCurrent("Current", CommonPlaceholders.GENERIC_VALUE)
        .br()
        .appendInfo("Determines how command pools", "are chosen and executed.")
        .br()
        .appendClick("Click to edit")
        .build();

    public static final IconLocale UI_INVENTORY_COMMANDS_BUTTON_ADD_AVAILABLE = LangEntry
        .iconBuilder("rewards.commands.editor.ui.inventory.commands.button.add.available")
        .accentColor(TagWrappers.GREEN)
        .name("Add Command Pool")
        .appendCurrent("Pools Added", SharedPlaceholders.CURRENT + "/" + SharedPlaceholders.MAX)
        .br()
        .appendInfo(
            TagWrappers.GREEN.wrap("Click") + " this button to add",
            "a new command pool to the reward.",
            "",
            "Press " + TagWrappers.RED.wrap("[" + TagWrappers.KEY.apply("key.drop") + "]") + " on the pool",
            "to delete it from the reward."
        )
        .build();

    public static final IconLocale UI_INVENTORY_COMMANDS_BUTTON_ADD_UNAVAILABLE = LangEntry
        .iconBuilder("rewards.commands.editor.ui.inventory.commands.button.add.unavailable")
        .accentColor(TagWrappers.RED)
        .name("Add Command Pool")
        .appendCurrent("Pools Added", SharedPlaceholders.CURRENT + "/" + SharedPlaceholders.MAX)
        .br()
        .appendInfo(
            "There are maximum pools added.",
            "",
            "Press " + TagWrappers.RED.wrap("[" + TagWrappers.KEY.apply("key.drop") + "]") + " on the pool",
            "to delete it from the reward."
        )
        .build();

    public static final TextLocale UI_DIALOG_COMMANDS_ITERATIONS_TITLE = LangEntry
        .builder("rewards.commands.editor.ui.dialog.commands.iterations.title")
        .text(TagWrappers.GOLD.and(TagWrappers.UNDERLINED).wrap("Commands Pool Iterations"));

    public static final DialogElementLocale UI_DIALOG_COMMANDS_ITERATIONS_BODY = LangEntry
        .builder("rewards.commands.editor.ui.dialog.commands.iterations.body")
        .dialogElement(
            "Set the desired " + TagWrappers.GOLD.wrap("pool iterations") + ".",
            "",
            "Controls how many roll passes are executed for command pools upon winning.",
            "",
            TagWrappers.GRAY.wrap(
                "Most effective in " + TagWrappers.WHITE.wrap("Weighted") + " mode, where each " +
                    "iteration selects one random pool based on its weight."
            )
        );

    public static final TextLocale UI_DIALOG_COMMANDS_ITERATIONS_INPUT_AMOUNT = LangEntry
        .builder("rewards.commands.editor.ui.dialog.commands.iterations.input.amount")
        .text(TagWrappers.SPRITE_ITEM.apply(Material.REPEATER) + " Pool Iterations");

    public static final TextLocale UI_DIALOG_COMMANDS_GIVE_MODE_TITLE = LangEntry
        .builder("rewards.commands.editor.ui.dialog.commands.give_mode.title")
        .text(TagWrappers.GOLD.and(TagWrappers.UNDERLINED).wrap("Commands Execution Mode"));

    public static final DialogElementLocale UI_DIALOG_COMMANDS_GIVE_MODE_BODY = LangEntry
        .builder("rewards.commands.editor.ui.dialog.commands.give_mode.body")
        .dialogElement(
            "Select the " + TagWrappers.GOLD.wrap("execution mode") + " for command pools.",
            "",
            TagWrappers.WHITE.and(TagWrappers.BOLD).wrap("Normal:"),
            TagWrappers.GRAY.wrap("All pools execute sequentially in order."),
            "",
            TagWrappers.WHITE.and(TagWrappers.BOLD).wrap("Weighted:"),
            TagWrappers.GRAY.wrap("Only one pool is selected based on its relative weight."),
            "",
            TagWrappers.WHITE.and(TagWrappers.BOLD).wrap("Chance:"),
            TagWrappers.GRAY.wrap("All pools run in order, each rolling against its individual chance."),
            TagWrappers.GRAY.wrap("The " + TagWrappers.WHITE.wrap("Weight") + " property functions as " +
                TagWrappers.WHITE.wrap("Chance %") + " here."
            )
        );

    public static final TextLocale UI_DIALOG_COMMANDS_GIVE_MODE_INPUT_MODE = LangEntry
        .builder("rewards.commands.editor.ui.dialog.commands.give_mode.input.mode")
        .text(TagWrappers.SPRITE_BLOCKS.apply("block/observer_back_on") + " Execution Mode");

    public static final TextLocale UI_DIALOG_COMMANDS_BUNDLE_TITLE = LangEntry
        .builder("rewards.commands.editor.ui.dialog.commands.pool.title")
        .text(TagWrappers.GOLD.and(TagWrappers.UNDERLINED).wrap("Command Pool Configuration"));

    public static final DialogElementLocale UI_DIALOG_COMMANDS_BUNDLE_BODY_MAIN = LangEntry
        .builder("rewards.commands.editor.ui.dialog.commands.pool.body.main")
        .dialogElement(
            "Configuring " + TagWrappers.GOLD.wrap("Command Pool") + ".",
            "",
            "Enter console commands in the " + TagWrappers.WHITE.wrap("Commands") + " field.",
            TagWrappers.GRAY.wrap("Supported placeholders: " +
                TagWrappers.AQUA.wrap("Crate") + ", " +
                TagWrappers.AQUA.wrap("Reward") + ", " +
                TagWrappers.AQUA.wrap("Player") + ", and " +
                TagWrappers.AQUA.wrap("PlaceholderAPI") + "."
            ),
            TagWrappers.GRAY.wrap("Visit " + TagWrappers.GREEN.and(TagWrappers.UNDERLINED)
                .wrap(TagWrappers.OPEN_URL.with(SharedLinks.DOCUMENTATION).wrap("documentation")) + " for details."
            )
        );

    public static final DialogElementLocale UI_DIALOG_COMMANDS_BUNDLE_BODY_WEIGHT = LangEntry
        .builder("rewards.commands.editor.ui.dialog.commands.pool.body.weight")
        .dialogElement(
            TagWrappers.GRAY.wrap(TagWrappers.WHITE.wrap("Weight / Chance") + " is utilized only in " +
                TagWrappers.WHITE.wrap("Weighted") + " and " +
                TagWrappers.WHITE.wrap("Chance") + " execution modes"
            )
        );

    public static final TextLocale UI_DIALOG_COMMANDS_BUNDLE_INPUT_COMMANDS = LangEntry
        .builder("rewards.commands.editor.ui.dialog.commands.pool.input.commands")
        .text(TagWrappers.SPRITE_BLOCKS.apply("block/command_block_back") + " Commands");

    public static final TextLocale UI_DIALOG_COMMANDS_BUNDLE_INPUT_WEIGHT = LangEntry
        .builder("rewards.commands.editor.ui.dialog.commands.pool.input.weight")
        .text(TagWrappers.SPRITE_ITEM.apply(Material.IRON_INGOT) + " Weight / Chance");

    public static final TextLocale UI_DIALOG_COMMANDS_BUNDLE_DELETE_TITLE = LangEntry
        .builder("rewards.commands.editor.ui.dialog.commands.pool.delete.title")
        .text(TagWrappers.RED.and(TagWrappers.UNDERLINED).wrap("Command Pool Deletion"));

    public static final DialogElementLocale UI_DIALOG_COMMANDS_BUNDLE_DELETE_BODY = LangEntry
        .builder("rewards.commands.editor.ui.dialog.commands.pool.delete.body")
        .dialogElement(
            "Are you sure you want to delete this command pool?",
            "",
            TagWrappers.GRAY.wrap("This action is " + TagWrappers.RED.wrap("irreversible") + "."),
            TagWrappers.GRAY.wrap("Think twice before proceeding.")
        );
}
