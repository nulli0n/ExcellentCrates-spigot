package su.nightexpress.excellentcrates.config;

import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.crate.impl.LimitType;
import su.nightexpress.nightcore.language.entry.LangItem;
import su.nightexpress.nightcore.util.Plugins;

import static su.nightexpress.excellentcrates.Placeholders.*;
import static su.nightexpress.nightcore.language.entry.LangItem.builder;
import static su.nightexpress.nightcore.util.text.tag.Tags.*;

public class EditorLang {

    private static final String PREFIX = "Editor.Item.";

    public static final LangItem CRATES_EDITOR = builder(PREFIX + "Crates")
            .name("Crates")
            .click("navigate").build();

    public static final LangItem KEYS_EDITOR = builder(PREFIX + "Keys")
            .name("Keys")
            .click("navigate").build();

    public static final LangItem CRATE_OBJECT = builder(PREFIX + "Crate.Object")
            .name(CRATE_NAME + RESET.enclose(LIGHT_GRAY.enclose(" (ID: " + WHITE.enclose(CRATE_ID) + ")")))
            .textRaw(CRATE_INSPECT)
            .emptyLine()
            .leftClick("edit")
            .shiftRight("delete " + LIGHT_RED.enclose("(no undo)")).build();

    public static final LangItem CRATE_CREATE = builder(PREFIX + "Crate.Create")
            .name("New Crate").build();

    public static final LangItem CRATE_DISPLAY_NAME = builder(PREFIX + "Crate.DisplayName")
            .name("Display Name")
            .current("Current", CRATE_NAME)
            .emptyLine()
            .text("Crate name for messages, placeholders", "and holograms.")
            .emptyLine()
            .click("change")
            .build();

    public static final LangItem CRATE_PERMISSION_REQUIREMENT = builder(PREFIX + "Crate.Permission")
            .name("Permission Requirement")
            .current("Enabled", CRATE_PERMISSION_REQUIRED)
            .current("Permission", CRATE_PERMISSION)
            .emptyLine()
            .text("Enables permission requirement", "to open this crate.").emptyLine()
            .emptyLine()
            .click("toggle")
            .build();

    public static final LangItem CRATE_OPEN_COOLDOWN = builder(PREFIX + "Crate.OpenCooldown")
            .name("Open Cooldown")
            .current("Cooldown", Placeholders.CRATE_OPEN_COOLDOWN)
            .emptyLine()
            .text("Per player cooldown between", "crate openings.")
            .emptyLine()
            .leftClick("set cooldown")
            .rightClick("remove")
            .dropKey("make one-timed")
            .build();

    public static final LangItem CRATE_PREVIEW_AND_OPENING = builder(PREFIX + "Crate.PreviewAndOpening")
            .name("Preview & Opening")
            .current("Preview", CRATE_PREVIEW_CONFIG)
            .current("Opening", CRATE_ANIMATION_CONFIG)
            .emptyLine()
            .text("Sets configurations used for:")
            .text(LIGHT_YELLOW.enclose("● ") + "Crate preview GUI (" + LIGHT_ORANGE.enclose(Config.DIR_PREVIEWS) + ")")
            .text(LIGHT_YELLOW.enclose("● ") + "Crate opening animation (" + LIGHT_ORANGE.enclose(Config.DIR_OPENINGS) + ")")
            .emptyLine()
            .leftClick("change preview")
            .rightClick("change opening")
            .shiftLeft("disable preview")
            .shiftRight("disable opening")
            .build();

    public static final LangItem CRATE_KEY_REQUIREMENT = builder(PREFIX + "Crate.KeyRequirement")
            .name("Key Requirements")
            .current("Enabled", CRATE_KEY_REQUIRED)
            .current("Keys", "")
            .textRaw(CRATE_KEYS)
            .emptyLine()
            .text("Player must have certain keys", "to open this crate.")
            .emptyLine()
            .leftClick("add key")
            .rightClick("remove all")
            .dropKey("toggle requirement")
            .build();

    public static final LangItem CRATE_OPEN_COST = builder(PREFIX + "Crate.OpenCost")
            .name("Open Cost")
            .current("Current", Placeholders.CRATE_OPEN_COST)
            .emptyLine()
            .text("Player have to pay certain currencies", "to open this crate.")
            .emptyLine()
            .leftClick("add cost")
            .dropKey("remove all")
            .build();

    public static final LangItem CRATE_ITEM = builder(PREFIX + "Crate.InventoryItem")
            .name("Inventory Item")
            .text("Item that represents crate in", "menus and player inventories.")
            .emptyLine()
            .text("You should use " + LIGHT_ORANGE.enclose("premade") + " items only -", "you can't edit it from here.")
            .emptyLine()
            .text(warning("NBT Tags are " + ORANGE.enclose("not") + " supported!"))
            .emptyLine()
            .dragAndDrop("replace")
            .leftClick("get item")
            .rightClick("get raw copy")
            .build();

    public static final LangItem CRATE_PLACEMENT_INFO = builder(PREFIX + "Crate.Placement.Info")
            .name("Placement")
            .text("Place crate anywhere in the world", "with particle effects and hologram!")
            .emptyLine()
            .click("navigate")
            .build();

    public static final LangItem CRATE_LOCATIONS = builder(PREFIX + "Crate.Placement.Locations")
            .name("Assigned Blocks")
            .text(Placeholders.CRATE_LOCATIONS)
            .emptyLine()
            .text("Assign crate to specific blocks", "in your world and " + LIGHT_ORANGE.enclose("open") + " and " + LIGHT_ORANGE.enclose("preview"), "crate when click them!")
            .emptyLine()
            .leftClick("assign block")
            .rightClick("remove all")
            .build();

    public static final LangItem CRATE_PUSHBACK = builder(PREFIX + "Crate.Placement.Pushback")
            .name("Pushback")
            .current("Status", CRATE_PUSHBACK_ENABLED).emptyLine()
            .text("Pushes players back from crate", "if they don't met the requirements.").emptyLine()
            .click("toggle")
            .build();

    public static final LangItem CRATE_HOLOGRAM = builder(PREFIX + "Crate.Placement.Hologram")
            .name("Hologram")
            .current("Status", CRATE_HOLOGRAM_ENABLED)
            .current("Template", CRATE_HOLOGRAM_TEMPLATE)
            .current("Y Offset", CRATE_HOLOGRAM_Y_OFFSET)
            .emptyLine()
            .text("Adds a hologram above crate", "block(s) with a text from selected template.").emptyLine()
            .text("Edit hologram templates in " + LIGHT_ORANGE.enclose("config.yml")).emptyLine()
            .leftClick("toggle")
            .rightClick("change template")
            .shiftLeft("change Y offset")
            .build();

    public static final LangItem CRATE_EFFECTS = builder(PREFIX + "Crate.Placement.Effects")
            .name("Particle Effects")
            .current("Model", CRATE_EFFECT_MODEL)
            .current("Particle", CRATE_EFFECT_PARTICLE_NAME)
            .current("Data", CRATE_EFFECT_PARTICLE_DATA)
            .emptyLine()
            .text("Display cool effects around the crate!")
            .emptyLine()
            .dropKey("toggle model")
            .leftClick("change particle")
            .rightClick("change data")
            .build();

    public static final LangItem CRATE_PARTICLE_DATA_RED = builder(PREFIX + "Crate.Particle.Data.Red")
            .name("Red")
            .current("Current", GENERIC_VALUE)
            .emptyLine()
            .click("change")
            .build();

    public static final LangItem CRATE_PARTICLE_DATA_GREEN = builder(PREFIX + "Crate.Particle.Data.Green")
            .name("Green")
            .current("Current", GENERIC_VALUE)
            .emptyLine()
            .click("change")
            .build();

    public static final LangItem CRATE_PARTICLE_DATA_BLUE = builder(PREFIX + "Crate.Particle.Data.Blue")
            .name("Blue")
            .current("Current", GENERIC_VALUE)
            .emptyLine()
            .click("change")
            .build();

    public static final LangItem CRATE_PARTICLE_DATA_SIZE = builder(PREFIX + "Crate.Particle.Data.Size")
            .name("Size")
            .current("Current", GENERIC_VALUE)
            .emptyLine()
            .click("change")
            .build();

    public static final LangItem CRATE_PARTICLE_DATA_MATERIAL = builder(PREFIX + "Crate.Particle.Data.Material")
            .name("Material")
            .current("Current", GENERIC_VALUE)
            .emptyLine()
            .dragAndDrop("change")
            .build();

    public static final LangItem CRATE_PARTICLE_DATA_NUMBER = builder(PREFIX + "Crate.Particle.Data.Number")
            .name("Number")
            .current("Current", GENERIC_VALUE)
            .emptyLine()
            .click("change")
            .build();

    public static final LangItem CRATE_REWARDS = builder(PREFIX + "Crate.Rewards")
            .name("Rewards")
            .current("Amount", CRATE_REWARDS_AMOUNT)
            .emptyLine()
            .text("All possible crate rewards for both,", "winnings and milestones.")
            .emptyLine()
            .click("navigate")
            .build();

    public static final LangItem CRATE_MILESTONES = builder(PREFIX + "Crate.Milestones")
            .name("Milestones")
            .current("Milestones", CRATE_MILESTONES_AMOUNT)
            .current("Repeatable", CRATE_MILESTONES_REPEATABLE)
            .emptyLine()
            .text("Milestones are another way", "to " + LIGHT_ORANGE.enclose("reward") + " your players and", LIGHT_ORANGE.enclose("motivate") + " them to open even", "more crates!")
            .emptyLine()
            .text("Give them unique rewards", "every time they open this", "crate for " + LIGHT_ORANGE.enclose("X times") + ".")
            .emptyLine()
            .leftClick("navigate")
            .rightClick("toggle repeatable")
            .build();

    public static final LangItem REWARD_OBJECT = builder(PREFIX + "Reward.Object")
            .name(REWARD_NAME + RESET.enclose(REWARD_NAME + LIGHT_GRAY.enclose(" (ID: " + LIGHT_ORANGE.enclose(REWARD_ID) + ")")))
            .textRaw(REWARD_INSPECT_CONTENT)
            .emptyLine()
            .current("Weight", Placeholders.REWARD_WEIGHT)
            .current("Roll Chance", REWARD_ROLL_CHANCE + "%")
            .current("Rarity", REWARD_RARITY_NAME)
            .emptyLine()
            .leftClick("edit")
            .shiftLeft("move forward")
            .shiftRight("move backward")
            .dropKey("delete " + LIGHT_RED.enclose("(no undo)"))
            .build();

    public static final LangItem REWARD_CREATE = builder(PREFIX + "Reward.Create")
            .name("New Reward")
            .text("")
            .emptyLine()
            .text(LIGHT_YELLOW.enclose(BOLD.enclose("Easy Mode:")))
            .text("Click with item in cursor", "to quickly create a reward", LIGHT_GREEN.enclose("with") + " physical item.")
            .emptyLine()
            .text(LIGHT_YELLOW.enclose(BOLD.enclose("Expert Mode:")))
            .text("Click with empty cursor", "to create a reward", LIGHT_RED.enclose("without") + " physical item.")
            .build();

    public static final LangItem REWARD_SORT = builder(PREFIX + "Reward.Sort.Info")
            .name("Sort Rewards")
            .text("Automatically sorts rewards in", "specified order.")
            .emptyLine()
            .leftClick("navigate")
            .build();

    public static final LangItem REWARD_SORT_BY_WEIGHT = builder(PREFIX + "Reward.Sort.ByWeight")
            .name("By Weight")
            .build();

    public static final LangItem REWARD_SORT_BY_RARITY = builder(PREFIX + "Reward.Sort.ByRarity")
            .name("By Rarity")
            .build();

    public static final LangItem REWARD_SORT_BY_CHANCE = builder(PREFIX + "Reward.Sort.ByChance")
            .name("By Chance")
            .build();

    public static final LangItem REWARD_SORT_BY_NAME = builder(PREFIX + "Reward.Sort.ByName")
            .name("By Name")
            .build();

    public static final LangItem REWARD_SORT_BY_ITEM = builder(PREFIX + "Reward.Sort.ByItem")
            .name("By Item")
            .build();

    public static final LangItem REWARD_DISPLAY_NAME = builder(PREFIX + "Reward.DisplayName")
            .name("Display Name")
            .current("Current", REWARD_NAME)
            .emptyLine()
            .text("Reward name for messages and placeholders.")
            .emptyLine()
            .leftClick("change")
            .shiftLeft("inherit from preview")
            .shiftRight("set for preview")
            .build();

    public static final LangItem REWARD_PREVIEW = builder(PREFIX + "Reward.Preview")
            .name("Preview Item")
            .text("Item that represents this reward.")
            .emptyLine()
            .text("If reward has " + LIGHT_ORANGE.enclose("zero") + " or " + LIGHT_ORANGE.enclose("multiple"), "physical items, you probably want to set", "a specific item to identity the reward.")
            .emptyLine()
            .dragAndDrop("replace")
            .rightClick("get a copy")
            .build();

    public static final LangItem REWARD_SET_PLACEHOLDERS = builder(PREFIX + "Reward.SetPlaceholders")
            .name("Apply Placeholders")
            .current("Enabled", REWARD_PLACEHOLDER_APPLY)
            .emptyLine()
            .text("Applies crate, reward and player placeholders", "to all reward item(s) on win.")
            .emptyLine()
            .text("This option " + LIGHT_RED.enclose("might screw up") + " custom items.")
            .emptyLine()
            .click("toggle")
            .build();

    public static final LangItem REWARD_WEIGHT = builder(PREFIX + "Reward.Weight")
            .name("Rarity & Weight")
            .current("Rarity", REWARD_RARITY_NAME)
            .current("Weight", Placeholders.REWARD_WEIGHT)
            .current("Roll Chance", REWARD_ROLL_CHANCE + "%")
            .emptyLine()
            .text("Roll chance depends on reward", "and rarity " + LIGHT_ORANGE.enclose("weights") + ".")
            .text("The " + LIGHT_ORANGE.enclose("more") + " weight the " + LIGHT_ORANGE.enclose("bigger") + " is chance.")
            .emptyLine()
            .text("Read " + LIGHT_ORANGE.enclose("documentation") + " for details.", "It explains everything very well.")
            .emptyLine()
            .leftClick("set rarity")
            .rightClick("set weight")
            .build();

    public static final LangItem REWARD_COMMANDS = builder(PREFIX + "Reward.Commands")
            .name("Win Commands")
            .textRaw(Placeholders.REWARD_EDITOR_COMMANDS)
            .emptyLine()
            .text("Specified commands will be", "executed by " + LIGHT_ORANGE.enclose("server's console"), "when player wins this reward.")
            .emptyLine()
            .text("You can use all " + LIGHT_ORANGE.enclose(Plugins.PLACEHOLDER_API) + " placeholders.")
            .text("Use " + LIGHT_ORANGE.enclose(PLAYER_NAME) + " for player name.")
            .emptyLine()
            .leftClick("add command")
            .rightClick("remove all")
            .build();

    public static final LangItem REWARD_ITEMS = builder(PREFIX + "Reward.Items")
            .name("Win Items")
            .textRaw(Placeholders.REWARD_EDITOR_ITEMS)
            .emptyLine()
            .text("Specified items will be", "added to " + LIGHT_ORANGE.enclose("player's inventory"), "when player wins this reward.")
            .emptyLine()
            .text("You can use all " + LIGHT_ORANGE.enclose(Plugins.PLACEHOLDER_API) + " placeholders.")
            .text("Use " + LIGHT_ORANGE.enclose(PLAYER_NAME) + " for player name.")
            .emptyLine()
            .leftClick("navigate")
            .build();

    public static final LangItem REWARD_BROADCAST = builder(PREFIX + "Reward.Broadcast")
            .name("Win Broadcast")
            .current("Enabled", Placeholders.REWARD_BROADCAST)
            .emptyLine()
            .text("When enabled, broadcasts a message", "when someone wins this reward.")
            .emptyLine()
            .click("toggle")
            .build();

    public static final LangItem REWARD_GLOBAL_WIN_LIMIT = builder(PREFIX + "Reward.GlobalWinLimit")
            .name("Global Win Limits")
            .current("Enabled", REWARD_WIN_LIMIT_ENABLED.apply(LimitType.GLOBAL))
            .current("Amount", REWARD_WIN_LIMIT_AMOUNT.apply(LimitType.GLOBAL))
            .current("Cooldown", REWARD_WIN_LIMIT_COOLDOWN.apply(LimitType.GLOBAL))
            .current("Cooldown Step", REWARD_WIN_LIMIT_STEP.apply(LimitType.GLOBAL))
            .emptyLine()
            .text("Sets reward cooldown and amount", "of possible wins for the " + LIGHT_ORANGE.enclose("whole server") + ".")
            .emptyLine()
            .text("Read " + LIGHT_ORANGE.enclose("documentation") + " for more details.")
            .emptyLine()
            .dropKey("toggle")
            .leftClick("change amount")
            .rightClick("change cooldown")
            .shiftLeft("change cooldown step")
            .shiftRight("midnight cooldown")
            .swapKey("reset stored data")
            .build();

    public static final LangItem REWARD_PLAYER_WIN_LIMIT = builder(PREFIX + "Reward.PlayerWinLimit")
            .name("Player Win Limits")
            .current("Enabled", REWARD_WIN_LIMIT_ENABLED.apply(LimitType.PLAYER))
            .current("Amount", REWARD_WIN_LIMIT_AMOUNT.apply(LimitType.PLAYER))
            .current("Cooldown", REWARD_WIN_LIMIT_COOLDOWN.apply(LimitType.PLAYER))
            .current("Cooldown Step", REWARD_WIN_LIMIT_STEP.apply(LimitType.PLAYER))
            .emptyLine()
            .text("Sets reward cooldown and amount", "of possible wins for " + LIGHT_ORANGE.enclose("each player") + ".")
            .emptyLine()
            .text("Read " + LIGHT_ORANGE.enclose("documentation") + " for more details.")
            .emptyLine()
            .dropKey("toggle")
            .leftClick("change amount")
            .rightClick("change cooldown")
            .shiftLeft("change cooldown step")
            .shiftRight("midnight cooldown")
            .build();

    public static final LangItem REWARD_IGNORED_PERMISSIONS = builder(PREFIX + "Reward.IgnoredPermissions")
            .name("Permission Restrictions")
            .textRaw(REWARD_IGNORED_FOR_PERMISSIONS)
            .emptyLine()
            .text("Players with any of specified permissions", "will never wins this reward.").emptyLine()
            .emptyLine()
            .leftClick("add")
            .rightClick("to remove all")
            .build();

    public static final LangItem MILESTONE_CREATE = builder(PREFIX + "Milestone.Create")
            .name("New Milestone")
            .build();

    public static final LangItem MILESTONE_OBJECT = builder(PREFIX + "Milestone.Object")
            .name("Milestone: " + MILESTONE_OPENINGS)
            .textRaw(MILESTONE_INSPECT_REWARD)
            .emptyLine()
            .current("Openings", MILESTONE_OPENINGS)
            .current("Reward Id", MILESTONE_REWARD_ID)
            .emptyLine()
            .leftClick("change openings")
            .rightClick("change reward")
            .shiftRight("delete " + LIGHT_RED.enclose("(no undo)"))
            .build();

    public static final LangItem KEY_OBJECT = builder(PREFIX + "Key.Object")
            .name(KEY_NAME + RESET.enclose(LIGHT_GRAY.enclose(" (ID: " + WHITE.enclose(KEY_ID) + ")")))
            .current("Virtual", Placeholders.KEY_VIRTUAL)
            .emptyLine()
            .leftClick("to edit")
            .shiftRight("delete " + LIGHT_RED.enclose("(no undo)"))
            .build();

    public static final LangItem KEY_CREATE = builder(PREFIX + "Key.Create")
            .name("New Key").build();

    public static final LangItem KEY_DISPLAY_NAME = builder(PREFIX + "Key.DisplayName")
            .name("Display Name")
            .current("Current", KEY_NAME)
            .emptyLine()
            .text("Key name for messages, placeholders", "and holograms.")
            .emptyLine()
            .click("change")
            .build();

    public static final LangItem KEY_ITEM = builder(PREFIX + "Key.Item")
            .name("Key Item")
            .text("Item that represents key in", "player inventories.")
            .emptyLine()
            .text("You should use " + LIGHT_ORANGE.enclose("premade") + " items only -", "you can't edit it from here.")
            .emptyLine()
            .text(warning("NBT Tags are " + ORANGE.enclose("not") + " supported!"))
            .emptyLine()
            .dragAndDrop("replace")
            .leftClick("get key item")
            .rightClick("get raw copy")
            .build();

    public static final LangItem KEY_VIRTUAL = builder(PREFIX + "Key.Virtual")
            .name("Virtual")
            .current("Enabled", Placeholders.KEY_VIRTUAL)
            .emptyLine()
            .text("Sets whether or not the key is virtual.")
            .emptyLine()
            .click("toggle")
            .build();
}
