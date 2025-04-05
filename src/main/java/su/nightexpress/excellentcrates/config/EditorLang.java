package su.nightexpress.excellentcrates.config;

import su.nightexpress.excellentcrates.crate.limit.LimitValues;
import su.nightexpress.excellentcrates.util.inspect.Inspections;
import su.nightexpress.nightcore.language.entry.LangItem;
import su.nightexpress.nightcore.language.entry.LangUIButton;
import su.nightexpress.nightcore.util.Players;
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

    public static final LangUIButton CRATE_OBJECT = LangUIButton.builder(PREFIX + "Crate.Object", CRATE_NAME)
        .current("ID", CRATE_ID)
        .description(INSPECTION_PROBLEMS)
        .click("navigate")
        .build();

    public static final LangItem CRATE_CREATE = builder(PREFIX + "Crate.Create")
        .name("New Crate").build();

    public static final LangItem CRATE_EDIT_DELETE = builder(PREFIX + "Crate.Delete")
        .name(LIGHT_RED.enclose("Delete Crate"))
        .text("Permanently deletes the crate.")
        .emptyLine()
        .click("delete")
        .build();

    public static final LangItem CRATE_EDIT_NAME = builder(PREFIX + "Crate.DisplayName")
        .name("Name")
        .current("Current", CRATE_NAME)
        .emptyLine()
        .text("Sets crate name.")
        .emptyLine()
        .click("change")
        .build();

    public static final LangItem CRATE_EDIT_DESCRIPTION = builder(PREFIX + "Crate.Description")
        .name("Description")
        .textRaw(CRATE_DESCRIPTION)
        .emptyLine()
        .text("Sets crate description (lore).")
        .emptyLine()
        .leftClick("add line")
        .rightClick("remove all")
        .build();

    public static final LangItem CRATE_EDIT_PERMISSION_REQUIREMENT = builder(PREFIX + "Crate.Permission")
        .name("Permission Requirement")
        .current("Enabled", CRATE_PERMISSION_REQUIRED)
        .current("Permission", CRATE_PERMISSION)
        .emptyLine()
        .text("Controls whether permission is", "required to open this crate.")
        .emptyLine()
        .click("toggle")
        .build();

    public static final LangItem CRATE_EDIT_OPEN_COOLDOWN = builder(PREFIX + "Crate.OpenCooldown")
        .name("Open Cooldown")
        .current("Cooldown", CRATE_OPEN_COOLDOWN)
        .emptyLine()
        .text("Sets per player cooldown", "for crate openings.")
        .emptyLine()
        .leftClick("set cooldown")
        .rightClick("remove")
        .dropKey("make one-timed")
        .build();

    public static final LangItem CRATE_EDIT_PREVIEW = builder(PREFIX + "Crate.Preview")
        .name("Preview")
        .textRaw(INSPECTION_TYPE.apply(Inspections.CRATE_PREVIEW))
        .emptyLine()
        .current("Enabled", CRATE_PREVIEW_ENABLED)
        .current("Id", CRATE_PREVIEW_ID)
        .emptyLine()
        .text("Sets preview GUI for this crate.")
        .emptyLine()
        .leftClick("change ID")
        .rightClick("toggle")
        .build();

    public static final LangItem CRATE_EDIT_ANIMATION = builder(PREFIX + "Crate.Animation")
        .name("Animation")
        .textRaw(INSPECTION_TYPE.apply(Inspections.CRATE_ANIMATION))
        .emptyLine()
        .current("Enabled", CRATE_ANIMATION_ENABLED)
        .current("Id", CRATE_ANIMATION_ID)
        .emptyLine()
        .text("Sets opening animation for this crate.")
        .emptyLine()
        .leftClick("change ID")
        .rightClick("toggle")
        .build();

    public static final LangItem CRATE_KEY_REQUIREMENT = builder(PREFIX + "Crate.KeyRequirement")
        .name("Key Requirements")
        .textRaw(INSPECTION_TYPE.apply(Inspections.CRATE_KEY_REQUIREMENT))
        .emptyLine()
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

    public static final LangItem CRATE_EDIT_OPEN_COST = builder(PREFIX + "Crate.OpenCost")
        .name("Open Cost")
        .textRaw(INSPECTION_TYPE.apply(Inspections.CRATE_OPEN_COST))
        .emptyLine()
        .text("Player have to pay certain currencies", "to open this crate.")
        .emptyLine()
        .click("navigate")
        .build();

    public static final LangItem CRATE_EDIT_OPEN_COST_OBJECT = builder(PREFIX + "Crate.OpenCost.Object")
        .name(GENERIC_NAME)
        .current("Currency ID", GENERIC_ID)
        .current("Amount", GENERIC_AMOUNT)
        .emptyLine()
        .leftClick("change amount")
        .rightClick("remove")
        .build();

    public static final LangItem CRATE_EDIT_OPEN_COST_CREATE = builder(PREFIX + "Crate.OpenCost.Create")
        .name("Add Cost")
        .build();

    public static final LangItem CRATE_EDIT_ITEM = builder(PREFIX + "Crate.InventoryStack")
        .name("Inventory Item")
        .textRaw(INSPECTION_TYPE.apply(Inspections.CRATE_ITEM))
        .emptyLine()
        .text("Sets item for this crate.")
        .emptyLine()
        .dragAndDrop("replace")
        .leftClick("get item")
        .rightClick("get raw copy")
        .build();

    public static final LangItem CRATE_EDIT_PLACEMENT = builder(PREFIX + "Crate.Placement.Info")
        .name("Placement")
        .text("Place crate anywhere in the world", "with particle effects and hologram!")
        .emptyLine()
        .click("navigate")
        .build();

    public static final LangItem CRATE_EDIT_BLOCKS = builder(PREFIX + "Crate.Placement.Locations")
        .name("Assigned Blocks")
        .text(CRATE_LOCATIONS)
        .emptyLine()
        .text("Clicking an assigned block will", "preview or open a crate.")
        .emptyLine()
        .leftClick("assign block")
        .rightClick("remove all")
        .build();

    public static final LangItem CRATE_EDIT_PUSHBACK = builder(PREFIX + "Crate.Placement.Pushback")
        .name("Block Pushback")
        .current("Status", CRATE_PUSHBACK_ENABLED)
        .emptyLine()
        .text("Pushes players back from the crate", "if they don't met the requirements.")
        .emptyLine()
        .click("toggle")
        .build();

    public static final LangItem CRATE_EDIT_HOLOGRAM_TOGGLE = builder(PREFIX + "Crate.Placement.HologramToggle")
        .name("Hologram State")
        .current("Enabled", CRATE_HOLOGRAM_ENABLED)
        .emptyLine()
        .text("Enables a hologram above crate block(s).")
        .emptyLine()
        .click("toggle")
        .build();

    public static final LangItem CRATE_EDIT_HOLOGRAM_TEMPLATE = builder(PREFIX + "Crate.Placement.HologramTemplate")
        .name("Hologram Template")
        .textRaw(INSPECTION_TYPE.apply(Inspections.CRATE_HOLOGRAM))
        .emptyLine()
        .current("Current", CRATE_HOLOGRAM_TEMPLATE)
        .emptyLine()
        .text("Hologram text depends on", "selected hologram template.")
        .emptyLine()
        .click("change")
        .build();

    public static final LangItem CRATE_EDIT_HOLOGRAM_OFFSET = builder(PREFIX + "Crate.Placement.HologramYOffset")
        .name("Hologram Y Offset")
        .current("Current", CRATE_HOLOGRAM_Y_OFFSET)
        .emptyLine()
        .text("Offsets a hologram by Y", "axis by defined value.")
        .emptyLine()
        .click("change")
        .build();

    public static final LangItem CRATE_EDIT_EFFECT_MODEL = builder(PREFIX + "Crate.Placement.Effect.Model")
        .name("Effect Model")
        .current("Current", CRATE_EFFECT_MODEL)
        .emptyLine()
        .text("Sets effect model or", "disables the effects.")
        .emptyLine()
        .leftClick("change")
        .rightClick("disable")
        .build();

    public static final LangItem CRATE_EDIT_EFFECT_PARTICLE = builder(PREFIX + "Crate.Placement.Effect.Particle")
        .name("Effect Particle")
        .current("Current", CRATE_EFFECT_PARTICLE_NAME)
        .emptyLine()
        .text("Sets effect particles.")
        .emptyLine()
        .click("change")
        .build();

    public static final LangItem CRATE_EDIT_EFFECT_PARTICLE_DATA = builder(PREFIX + "Crate.Placement.Effect.ParticleData")
        .name("Particle Data")
        .textRaw(INSPECTION_TYPE.apply(Inspections.CRATE_PARTICLE_DATA))
        .emptyLine()
        .text("Sets particle options.")
        .emptyLine()
        .click("edit")
        .build();

    public static final LangItem CRATE_EDIT_PARTICLE_DATA_RED = builder(PREFIX + "Crate.Particle.Data.Red")
        .name("Red")
        .current("Current", GENERIC_VALUE)
        .emptyLine()
        .click("change")
        .build();

    public static final LangItem CRATE_EDIT_PARTICLE_DATA_GREEN = builder(PREFIX + "Crate.Particle.Data.Green")
        .name("Green")
        .current("Current", GENERIC_VALUE)
        .emptyLine()
        .click("change")
        .build();

    public static final LangItem CRATE_EDIT_PARTICLE_DATA_BLUE = builder(PREFIX + "Crate.Particle.Data.Blue")
        .name("Blue")
        .current("Current", GENERIC_VALUE)
        .emptyLine()
        .click("change")
        .build();

    public static final LangItem CRATE_EDIT_PARTICLE_DATA_SIZE = builder(PREFIX + "Crate.Particle.Data.Size")
        .name("Size")
        .current("Current", GENERIC_VALUE)
        .emptyLine()
        .click("change")
        .build();

    public static final LangItem CRATE_EDIT_PARTICLE_DATA_MATERIAL = builder(PREFIX + "Crate.Particle.Data.Material")
        .name("Material")
        .current("Current", GENERIC_VALUE)
        .emptyLine()
        .dragAndDrop("change")
        .build();

    public static final LangItem CRATE_EDIT_PARTICLE_DATA_NUMBER = builder(PREFIX + "Crate.Particle.Data.Number")
        .name("Number")
        .current("Current", GENERIC_VALUE)
        .emptyLine()
        .click("change")
        .build();

    public static final LangItem CRATE_EDIT_REWARDS = builder(PREFIX + "Crate.Rewards")
        .name("Rewards")
        .textRaw(INSPECTION_TYPE.apply(Inspections.CRATE_REWARDS))
        .emptyLine()
        .current("Amount", CRATE_REWARDS_AMOUNT)
        .emptyLine()
        .click("navigate")
        .build();

    public static final LangItem CRATE_EDIT_MILESTONES = builder(PREFIX + "Crate.Milestones")
        .name("Milestones")
        .current("Milestones", CRATE_MILESTONES_AMOUNT)
        .current("Repeatable", CRATE_MILESTONES_REPEATABLE)
        .emptyLine()
        .text("Reward players for opening", "certain amount of crates", "in a row!")
        .emptyLine()
        .leftClick("navigate")
        .rightClick("toggle repeatable")
        .build();

    public static final LangItem REWARD_OBJECT = builder(PREFIX + "Reward.Object")
        .name(REWARD_NAME + RESET.getBracketsName() + LIGHT_GRAY.enclose(" (ID: " + LIGHT_ORANGE.enclose(REWARD_ID) + ")"))
        .textRaw(INSPECTION_PROBLEMS)
        .textRaw(INSPECTION_TYPE.apply(Inspections.REWARD_CONTENT))
        .emptyLine()
        .current("Weight", REWARD_WEIGHT)
        .current("Roll Chance", REWARD_ROLL_CHANCE + "%")
        .current("Rarity", REWARD_RARITY_NAME)
        .emptyLine()
        .leftClick("edit")
        .shiftLeft("move forward")
        .shiftRight("move backward")
        .build();

    public static final LangItem REWARD_CREATE = builder(PREFIX + "Reward.Creation.Button")
        .name("Create Reward")
        .click("navigate")
        .build();

    public static final LangItem REWARD_CREATION_INFO = builder(PREFIX + "Reward.Creation.Info")
        .name("Creation Wizard")
        .emptyLine()
        .text(LIGHT_YELLOW.enclose(BOLD.enclose("Step #1")))
        .text("Click an item in your inventory")
        .text("to select it as a base for new reward.")
        .emptyLine()
        .text(LIGHT_YELLOW.enclose(BOLD.enclose("Step #2")))
        .text("Select and click reward type button")
        .text("to create a new reward.")
        .build();

    public static final LangItem REWARD_CREATION_ITEM = builder(PREFIX + "Reward.Creation.Item")
        .name("Item Reward")
        .text("Gives item(s) directly to player's", "inventory when rolled out.")
        .emptyLine()
        .text(GREEN.enclose("✔") + " Custom Items")
        .text(GREEN.enclose("✔") + " NBT Support")
        .text(GREEN.enclose("✔") + " Placeholders")
        .text(GREEN.enclose("✔") + " Up to 27 items")
        .emptyLine()
        .click("create")
        .build();

    public static final LangItem REWARD_CREATION_COMMAND = builder(PREFIX + "Reward.Creation.Command")
        .name("Command Reward")
        .text("Runs specified command(s) with", "placeholders when rolled out.")
        .emptyLine()
        .text(GREEN.enclose("✔") + " Custom Name")
        .text(GREEN.enclose("✔") + " Custom Lore")
        .text(GREEN.enclose("✔") + " Placeholders")
        .text(GREEN.enclose("✔") + " Unlimited Commands")
        .emptyLine()
        .click("create")
        .build();

    public static final LangItem REWARD_SORT = builder(PREFIX + "Reward.Sort.Info")
        .name("Sort Rewards")
        .text("Automatically sorts rewards in", "certain order.")
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

    public static final LangItem REWARD_EDIT_DELETE = builder(PREFIX + "Reward.Delete")
        .name(LIGHT_RED.enclose("Delete Reward"))
        .text("Permanently deletes the reward.")
        .emptyLine()
        .click("delete")
        .build();

    public static final LangItem REWARD_EDIT_NAME = builder(PREFIX + "Reward.Name")
        .name("Display Name")
        .current("Current", REWARD_NAME)
        .emptyLine()
        .text("Sets reward name.")
        .emptyLine()
        .click("change")
        .build();

    public static final LangItem REWARD_EDIT_DESCRIPTION = builder(PREFIX + "Reward.Description")
        .name("Description")
        .textRaw(REWARD_DESCRIPTION)
        .emptyLine()
        .text("Sets reward description (lore).")
        .emptyLine()
        .leftClick("add line")
        .rightClick("remove all")
        .build();

    public static final LangUIButton REWARD_EDIT_CUSTOM_ICON = LangUIButton.builder(PREFIX + "Reward.CustomPreview", "Custom Icon")
        .current("Enabled", REWARD_CUSTOM_PREVIEW)
        .description("Controls whether reward", "should have a custom icon.")
        .click("toggle")
        .build();

    public static final LangItem REWARD_EDIT_ICON = builder(PREFIX + "Reward.Icon")
        .name("Icon")
        .textRaw(INSPECTION_TYPE.apply(Inspections.REWARD_PREVIEW))
        .emptyLine()
        .text("Sets reward icon.")
        .emptyLine()
        .dragAndDrop("replace")
        .rightClick("get a copy")
        .build();

    public static final LangItem REWARD_EDIT_PLACEHOLDERS = builder(PREFIX + "Reward.SetPlaceholders")
        .name("Apply Placeholders?")
        .current("Enabled", REWARD_PLACEHOLDER_APPLY)
        .emptyLine()
        .text("When enabled, allows to use", "the following placeholders in", "reward's items/commands:")
        .text(LIGHT_YELLOW.enclose("✔") + " Crate placeholders " + DARK_GRAY.enclose("(see wiki)"))
        .text(LIGHT_YELLOW.enclose("✔") + " Reward placeholders " + DARK_GRAY.enclose("(see wiki)"))
        .text(LIGHT_YELLOW.enclose("✔") + " " + Plugins.PLACEHOLDER_API + " placeholders")
        .emptyLine()
        .text(LIGHT_RED.enclose(BOLD.enclose("(!)")) + " This option " + LIGHT_RED.enclose("might screw up") + " custom items.")
        .emptyLine()
        .click("toggle")
        .build();

    public static final LangItem REWARD_EDIT_RARITY = builder(PREFIX + "Reward.Rarity")
        .name("Rarity")
        .current("Current", REWARD_RARITY_NAME)
        .current("Roll Chance", REWARD_RARITY_ROLL_CHANCE + "%")
        .emptyLine()
        .text("Sets rarity for this reward", "affecting reward's roll chance.")
        .emptyLine()
        .click("change")
        .build();

    public static final LangItem REWARD_EDIT_WEIGHT = builder(PREFIX + "Reward.Weight")
        .name("Weight")
        .current("Current", REWARD_WEIGHT)
        .current("Roll Chance", REWARD_ROLL_CHANCE + "%")
        .emptyLine()
        .text("Sets weight for this reward", "affecting reward's roll chance.")
        .emptyLine()
        .text("When disabled " + LIGHT_YELLOW.enclose("(-1)") + ", reward", "won't be rolled.")
        .emptyLine()
        .leftClick("change")
        .rightClick("disable")
        .build();

    public static final LangItem REWARD_EDIT_COMMANDS = builder(PREFIX + "Reward.CommandsContent")
        .name("Commands Content")
        .textRaw(INSPECTION_TYPE.apply(Inspections.REWARD_CONTENT))
        .textRaw(REWARD_COMMANDS_CONTENT)
        .emptyLine()
        .text("Runs listed commands when rolled out.")
        .emptyLine()
        .text("Use " + LIGHT_ORANGE.enclose(PLAYER_NAME) + " for player name.")
        .text("Use " + LIGHT_ORANGE.enclose(Players.PLAYER_COMMAND_PREFIX) + " prefix to run as player.")
        .text("Enable " + LIGHT_ORANGE.enclose("Apply Placeholders") + " for more placeholders.")
        .emptyLine()
        .leftClick("add command")
        .rightClick("remove all")
        .build();

    public static final LangItem REWARD_EDIT_ITEMS = builder(PREFIX + "Reward.ItemsContent")
        .name("Items Content")
        .textRaw(INSPECTION_TYPE.apply(Inspections.REWARD_CONTENT))
        .textRaw(REWARD_ITEMS_CONTENT)
        .emptyLine()
        .text("Gives listed items when rolled out.")
        .emptyLine()
        .text("Enable " + LIGHT_ORANGE.enclose("Apply Placeholders") + " to allow placeholders.")
        .emptyLine()
        .click("open")
        .build();

    public static final LangItem REWARD_EDIT_BROADCAST = builder(PREFIX + "Reward.Broadcast")
        .name("Roll Broadcast")
        .current("Enabled", REWARD_BROADCAST)
        .emptyLine()
        .text("Broadcasts a message when rolled out.")
        .emptyLine()
        .click("toggle")
        .build();

    public static final LangItem REWARD_EDIT_GLOBAL_LIMIT = builder(PREFIX + "Reward.Limit.Global")
        .name("Global Roll Limits")
        .text("Limits amount of rolls " + LIGHT_ORANGE.enclose("for all players") + ".")
        .emptyLine()
        .click("navigate")
        .build();

    public static final LangItem REWARD_EDIT_PLAYER_LIMIT = builder(PREFIX + "Reward.Limit.Player")
        .name("Player Roll Limits")
        .text("Limits amount of rolls " + LIGHT_ORANGE.enclose("per player") + ".")
        .emptyLine()
        .click("navigate")
        .build();

    public static final LangItem REWARD_EDIT_LIMIT_TOGGLE = builder(PREFIX + "Reward.Limit.Toggle")
        .name("Enabled")
        .current("Current", LIMIT_ENABLED)
        .emptyLine()
        .text("Controls whether limit is enabled.")
        .emptyLine()
        .click("toggle")
        .build();

    public static final LangItem REWARD_EDIT_LIMIT_AMOUNT = builder(PREFIX + "Reward.Limit.Amount")
        .name("Amount")
        .current("Current", LIMIT_AMOUNT)
        .emptyLine()
        .text("Sets amount of available rolls.")
        .emptyLine()
        .leftClick("change")
        .rightClick("unlimited")
        .build();

    public static final LangItem REWARD_EDIT_LIMIT_RESET_TIME = builder(PREFIX + "Reward.Limit.Cooldown")
        .name("Reset Time")
        .current("Current", LIMIT_RESET_TIME)
        .emptyLine()
        .text("Sets cooldown for rolls amount")
        .text("to reset to initial value.")
        .emptyLine()
        .text("Use " + LIGHT_YELLOW.enclose(String.valueOf(LimitValues.NEVER_RESET)) + " to never reset.")
        .text("Use " + LIGHT_YELLOW.enclose(String.valueOf(LimitValues.MIDNIGHT_RESET)) + " to reset at midnight.")
        .emptyLine()
        .leftClick("change")
        .build();

    public static final LangItem REWARD_EDIT_LIMIT_RESET_TIME_STEP = builder(PREFIX + "Reward.Limit.CooldownStep")
        .name("Reset Time Step")
        .current("Current", LIMIT_RESET_TIME_STEP)
        .emptyLine()
        .text("Sets amount of rolls required", "for the reset time to apply.")
        .emptyLine()
        .click("change")
        .build();

    public static final LangItem REWARD_EDIT_LIMIT_RESET = builder(PREFIX + "Reward.Limit.Reset")
        .name("Clear Data")
        .text("Clears both, global and personal,", "limits data for this reward.")
        .emptyLine()
        .click("reset")
        .build();

    public static final LangItem REWARD_EDIT_IGNORED_PERMISSIONS = builder(PREFIX + "Reward.IgnoredPermissions")
        .name("Ignored Permissions")
        .textRaw(REWARD_IGNORED_PERMISSIONS)
        .emptyLine()
        .text("Reward can be rolled out only if player", LIGHT_RED.enclose("do not") + " have " + LIGHT_RED.enclose("all") + " listed permissions.")
        .emptyLine()
        .leftClick("add permission")
        .rightClick("remove all")
        .build();

    public static final LangItem REWARD_EDIT_REQUIRED_PERMISSIONS = builder(PREFIX + "Reward.RequiredPermissions")
        .name("Required Permissions")
        .textRaw(REWARD_REQUIRED_PERMISSIONS)
        .emptyLine()
        .text("Reward can be rolled out only if player", LIGHT_GREEN.enclose("has any") + " of listed permissions.")
        .emptyLine()
        .leftClick("add permission")
        .rightClick("remove all")
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
        .name(KEY_NAME + RESET.getBracketsName() + LIGHT_GRAY.enclose(" (ID: " + WHITE.enclose(KEY_ID) + ")"))
        .textRaw(INSPECTION_PROBLEMS)
        .emptyLine()
        .current("Virtual", KEY_VIRTUAL)
        .emptyLine()
        .click("edit")
        .build();

    public static final LangItem KEY_CREATE = builder(PREFIX + "Key.Create")
        .name("New Key").build();

    public static final LangItem KEY_EDIT_DELETE = builder(PREFIX + "Key.Delete")
        .name(LIGHT_RED.enclose("Delete Key"))
        .text("Permanently deletes the key.")
        .emptyLine()
        .click("delete")
        .build();

    public static final LangItem KEY_EDIT_NAME = builder(PREFIX + "Key.DisplayName")
        .name("Display Name")
        .current("Current", KEY_NAME)
        .emptyLine()
        .text("Key name for messages, placeholders", "and holograms.")
        .emptyLine()
        .click("change")
        .build();

    public static final LangItem KEY_EDIT_ITEM = builder(PREFIX + "Key.Item")
        .name("Inventory Item")
        .textRaw(INSPECTION_TYPE.apply(Inspections.KEY_ITEM))
        .emptyLine()
        .text("Sets item for this key.")
        .emptyLine()
        .dragAndDrop("replace")
        .leftClick("get key item")
        .rightClick("get raw copy")
        .build();

    public static final LangItem KEY_EDIT_VIRTUAL = builder(PREFIX + "Key.Virtual")
        .name("Virtual")
        .current("Enabled", KEY_VIRTUAL)
        .emptyLine()
        .text("Sets whether or not the key is virtual.")
        .emptyLine()
        .click("toggle")
        .build();
}
