package su.nightexpress.excellentcrates.editor;

import su.nexmedia.engine.api.editor.EditorLocale;
import su.nexmedia.engine.utils.EngineUtils;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.config.Config;

import static su.nexmedia.engine.utils.Colors2.*;

public class EditorLocales extends su.nexmedia.engine.api.editor.EditorLocales {

    private static final String PREFIX = "Editor.Item.";

    public static final EditorLocale CRATES_EDITOR = builder(PREFIX + "Crates")
        .name("Crates")
        .text("(" + WHITE + LMB + GRAY + " to navigate)").build();

    public static final EditorLocale KEYS_EDITOR = builder(PREFIX + "Keys")
        .name("Keys")
        .text("(" + WHITE + LMB + GRAY + " to navigate)").build();

    public static final EditorLocale CRATE_OBJECT = builder(PREFIX + "Crate.Object")
        .name(Placeholders.CRATE_NAME + GRAY + " (ID: " + WHITE + Placeholders.CRATE_ID + GRAY + ")")
        .text(Placeholders.CRATE_INSPECT_KEYS)
        .text(Placeholders.CRATE_INSPECT_REWARDS)
        .text(Placeholders.CRATE_INSPECT_OPENING)
        .text(Placeholders.CRATE_INSPECT_PREVIEW)
        .text(Placeholders.CRATE_INSPECT_HOLOGRAM)
        .breakLine()
        .text("(" + WHITE + LMB + GRAY + " to edit)")
        .text("(" + WHITE + "Shift-Right" + GRAY + " to delete " + RED + "(no undo)").build();

    public static final EditorLocale CRATE_CREATE = builder(PREFIX + "Crate.Create")
        .name("New Crate").build();

    public static final EditorLocale CRATE_NAME = builder(PREFIX + "Crate.DisplayName")
        .name("Display Name")
        .text("General crate name, which")
        .text("is " + RED + "not" + GRAY + " related to crate item name.").breakLine()
        .currentHeader().current("Name", Placeholders.CRATE_NAME + GRAY + " (" + WHITE + LMB + GRAY + ")")
        .build();

    public static final EditorLocale CRATE_PERMISSION = builder(PREFIX + "Crate.Permission")
        .name("Permission Requirement")
        .text("Sets whether or not permission is required", "to open this crate.").breakLine()
        .currentHeader()
        .current("Required", Placeholders.CRATE_PERMISSION_REQUIRED + GRAY + " (" + WHITE + LMB + GRAY + ")")
        .current("Node", Placeholders.CRATE_PERMISSION).build();

    public static final EditorLocale CRATE_OPEN_COOLDOWN = builder(PREFIX + "Crate.OpenCooldown")
        .name("Open Cooldown")
        .text("Sets how much time player have to wait", "to open this crate again.").breakLine()
        .currentHeader().current("Cooldown", Placeholders.CRATE_OPEN_COOLDOWN + GRAY + " (" + WHITE + LMB + GRAY + ")")
        .breakLine()
        .text("(" + WHITE + RMB + GRAY + " to disable)")
        .text("(" + WHITE + DROP_KEY + GRAY + " to make one-timed)").build();

    public static final EditorLocale CRATE_TEMPLATE = builder(PREFIX + "Crate.Template")
        .name("Preview & Animation")
        .text(Placeholders.CRATE_INSPECT_PREVIEW, Placeholders.CRATE_INSPECT_OPENING).breakLine()
        .text("Sets GUI template to preview", "rewards and animate reward rolls.").breakLine()
        .text("You can find (or create) them in", WHITE + Config.DIR_PREVIEWS + GRAY + " and " + WHITE + Config.DIR_OPENINGS + GRAY + " folders.")
        .breakLine().currentHeader()
        .current("Preview", Placeholders.CRATE_PREVIEW_CONFIG + GRAY + " (" + WHITE + LMB + GRAY + ")")
        .current("Opening", Placeholders.CRATE_ANIMATION_CONFIG + GRAY + " (" + WHITE + RMB + GRAY + ")")
        .breakLine().text("(Hold " + WHITE + "Shift" + GRAY + " on click to disable)")
        .build();

    public static final EditorLocale CRATE_KEYS = builder(PREFIX + "Crate.Keys")
        .name("Attached Keys")
        .text(Placeholders.CRATE_INSPECT_KEYS).breakLine()
        .text("Sets which keys can be", "used to open this crate.")
        .breakLine().currentHeader()
        .text(Placeholders.CRATE_KEY_IDS).breakLine()
        .text("(" + WHITE + LMB + GRAY + " to add)")
        .text("(" + WHITE + RMB + GRAY + " to remove all)")
        .build();

    public static final EditorLocale CRATE_OPEN_COST = builder(PREFIX + "Crate.OpenCost")
        .name("Open Cost")
        .text("Sets what and how much a player", "have to pay in order to", "open this crate.").breakLine()
        .currentHeader()
        .current("Cost(s)", Placeholders.CRATE_OPEN_COST + GRAY + " (" + WHITE + LMB + GRAY + ")")
        .breakLine()
        .text("(" + WHITE + DROP_KEY + GRAY + " to remove all)").build();

    public static final EditorLocale CRATE_ITEM = builder(PREFIX + "Crate.InventoryItem")
        .name("Inventory Item")
        .text("This item is used to display", "crate in crate menus, and", "when you give crate to players.").breakLine()
        .text("You should " + RED + "premade" + GRAY + " item " + YELLOW + "name", "and " + YELLOW + "lore" + GRAY + " before drop it here.").breakLine()
        .text("(" + WHITE + "Drag'n'Drop" + GRAY + " to replace)")
        .text("(" + WHITE + LMB + GRAY + " to get crate item)")
        .text("(" + WHITE + RMB + GRAY + " to get raw copy)")
        .build();

    public static final EditorLocale CRATE_PLACEMENT_INFO = builder(PREFIX + "Crate.Placement.Info")
        .name("Placement")
        .text("Place crate at any location(s)", "in the world with awesome", WHITE + "particle effects" + GRAY + " and " + WHITE + "hologram" + GRAY + "!")
        .breakLine()
        .text("(" + WHITE + LMB + GRAY + " to navigate)")
        .build();

    public static final EditorLocale CRATE_LOCATIONS = builder(PREFIX + "Crate.Placement.Locations")
        .name("Assigned Blocks")
        .text("Players can interact with blocks", "from list below to open", "or preview the crate.").breakLine()
        .currentHeader().text(Placeholders.CRATE_LOCATIONS).breakLine()
        .text("(" + WHITE + LMB + GRAY + " to add)")
        .text("(" + WHITE + RMB + GRAY + " to remove all)")
        .build();

    public static final EditorLocale CRATE_PUSHBACK = builder(PREFIX + "Crate.Placement.Pushback")
        .name("Pushback")
        .text("Sets whether or not player will be pushed", "back from the crate block", "if unable to open it.").breakLine()
        .currentHeader().current("Enabled", Placeholders.CRATE_PUSHBACK_ENABLED + GRAY + " (" + WHITE + LMB + GRAY + ")")
        .build();

    public static final EditorLocale CRATE_HOLOGRAM = builder(PREFIX + "Crate.Placement.Hologram")
        .name("Hologram")
        .text(Placeholders.CRATE_INSPECT_HOLOGRAM).breakLine()
        .text("Creates hologram above crate", "block(s) with certain text template.")
        .breakLine().currentHeader()
        .current("Enabled", Placeholders.CRATE_HOLOGRAM_ENABLED + GRAY + " (" + WHITE + LMB + GRAY + ")")
        .current("Template", Placeholders.CRATE_HOLOGRAM_TEMPLATE + GRAY + " (" + WHITE + RMB + GRAY + ")")
        .breakLine().build();

    public static final EditorLocale CRATE_EFFECTS = builder(PREFIX + "Crate.Placement.Effects")
        .name("Particle Effects")
        .text("Build awesome crate particle effect!")
        .breakLine().currentHeader()
        .current("Model", Placeholders.CRATE_EFFECT_MODEL + GRAY + " (" + WHITE + DROP_KEY + GRAY + ")")
        .current("Particle", Placeholders.CRATE_EFFECT_PARTICLE_NAME + GRAY + " (" + WHITE + LMB + GRAY + ")")
        .current("Data", Placeholders.CRATE_EFFECT_PARTICLE_DATA + GRAY + " (" + WHITE + RMB + GRAY + ")")
        .build();

    public static final EditorLocale CRATE_REWARDS = builder(PREFIX + "Crate.Rewards")
        .name("Rewards")
        .text(Placeholders.CRATE_INSPECT_REWARDS).breakLine()
        .text("Here you can create generic", "crate rewards, as well as", "milestone ones.").breakLine()
        .currentHeader().current("Rewards", Placeholders.CRATE_REWARDS_AMOUNT + GRAY + " (" + WHITE + LMB + GRAY + ")")
        .build();

    public static final EditorLocale CRATE_MILESTONES = builder(PREFIX + "Crate.Milestones")
        .name("Milestones")
        .text("Here you can create crate", "milestones - a unique feature to", "give " + WHITE + "extra rewards" + GRAY + " for", "certain amount of " + WHITE + "openings" + GRAY + ".")
        .breakLine().currentHeader()
        .current("Milestones", Placeholders.CRATE_MILESTONES_AMOUNT + GRAY + " (" + WHITE + LMB + GRAY + ")")
        .current("Repeatable", Placeholders.CRATE_MILESTONES_REPEATABLE + GRAY + " (" + WHITE + RMB + GRAY + ")")
        .build();

    public static final EditorLocale REWARD_OBJECT = builder(PREFIX + "Reward.Object")
        .name(Placeholders.REWARD_NAME + GRAY + " (ID: " + WHITE + Placeholders.REWARD_ID + GRAY + ")")
        .text(Placeholders.REWARD_INSPECT_CONTENT).breakLine()
        .current("Chance", Placeholders.REWARD_WEIGHT + "%")
        .current("Actual Chance", Placeholders.REWARD_REAL_CHANCE + "%")
        .current("Rarity", Placeholders.REWARD_RARITY_NAME)
        .breakLine()
        .text("(" + WHITE + LMB + GRAY + " to edit)")
        .text("(" + WHITE + "Shift-Left" + GRAY + " to move forward)")
        .text("(" + WHITE + "Shift-Right" + GRAY + " move backward)")
        .text("(" + WHITE + DROP_KEY + GRAY + " to delete " + RED + "(no undo)")
        .build();

    public static final EditorLocale REWARD_CREATE = builder(PREFIX + "Reward.Create")
        .name("New Reward")
        .text("Well, as title says...").breakLine()
        .text(GREEN + "[!]" + GRAY + " Drop item right on " + GREEN + "this button", "for a quick creation!").breakLine()
        .text("(" + WHITE + LMB + GRAY + " for manual creation)")
        .build();

    public static final EditorLocale REWARD_SORT = builder(PREFIX + "Reward.Sort")
        .name("Sort Rewards")
        .text("Automatically sorts rewards in", "specified order.").breakLine()
        .action("[Num 1]", "By Chance").action("[Num 2]", "By Type")
        .action("[Num 3]", "By Name").action("[Num 4]", "By Rarity")
        .build();

    public static final EditorLocale REWARD_NAME = builder(PREFIX + "Reward.DisplayName")
        .name("Display Name")
        .text("General reward name, which")
        .text("is " + RED + "not" + GRAY + " related to reward item or preview.")
        .breakLine().currentHeader()
        .current("Display Name", Placeholders.REWARD_NAME)
        .breakLine()
        .text("(" + WHITE + LMB + GRAY + " to edit)")
        .text("(" + WHITE + "Shift-Left" + GRAY + " to inherit from preview)")
        .text("(" + WHITE + "Shift-Right" + GRAY + " to set for preview)")
        .build();

    public static final EditorLocale REWARD_PREVIEW = builder(PREFIX + "Reward.Preview")
        .name("Preview Item")
        .text("Visual reward " + WHITE + "representation" + GRAY + " for", "preview and opening GUIs.").breakLine()
        .text(RED + "[!]" + GRAY + " It has " + RED + "nothing" + GRAY + " to what", "players get from this reward.").breakLine()
        .text("(" + WHITE + "Drag & Drop" + GRAY + " to replace)")
        .text("(" + WHITE + RMB + GRAY + " to get a copy)")
        .build();

    public static final EditorLocale REWARD_WEIGHT = builder(PREFIX + "Reward.Weight")
        .name("Rarity & Weight")
        .text("Sets the reward rarity & weight.")
        .text(RED + "(Read documentation for details)")
        .breakLine().currentHeader()
        .current("Rarity", Placeholders.REWARD_RARITY_NAME + GRAY + " (" + WHITE + LMB + GRAY + ")")
        .current("Raw Chance", Placeholders.REWARD_WEIGHT + "%" + GRAY + " (" + WHITE + RMB + GRAY + ")")
        .current("Real Chance", Placeholders.REWARD_REAL_CHANCE + "%")
        .build();

    public static final EditorLocale REWARD_COMMANDS = builder(PREFIX + "Reward.Commands")
        .name("Commands")
        .text("All commands listed below will", "be runned from " + WHITE + "console", "when winning this reward.")
        .breakLine().currentHeader()
        .text(Placeholders.REWARD_EDITOR_COMMANDS)
        .breakLine()
        .text(YELLOW + BOLD + "Placeholders:")
        .current(EngineUtils.PLACEHOLDER_API, "All of them.")
        .current(Placeholders.PLAYER_NAME, "For player name.")
        .breakLine()
        .text("(" + WHITE + LMB + GRAY + " to add)")
        .text("(" + WHITE + RMB + GRAY + " to remove all)")
        .build();

    public static final EditorLocale REWARD_ITEMS = builder(PREFIX + "Reward.Items")
        .name("Items")
        .text("All items listed below will", "be added to player's inventory", "when winning this reward.").breakLine()
        .text("You can use " + WHITE + EngineUtils.PLACEHOLDER_API + GRAY + " placeholders")
        .text("in item's name and lore.")
        .breakLine().currentHeader()
        .text(Placeholders.REWARD_EDITOR_ITEMS).breakLine()
        .text("(" + WHITE + LMB + GRAY + " to edit)")
        .build();

    public static final EditorLocale REWARD_BROADCAST = builder(PREFIX + "Reward.Broadcast")
        .name("Broadcast")
        .text("Sets whether or not plugin", "will broadcast a message when", "someone wins this reward.")
        .breakLine().currentHeader()
        .current("Enabled", Placeholders.REWARD_BROADCAST + GRAY + " (" + WHITE + LMB + GRAY + ")")
        .build();

    public static final EditorLocale REWARD_WIN_LIMITS = builder(PREFIX + "Reward.WinLimits")
        .name("Win Limits")
        .text("Sets how often & how many times each", "player can win this reward again.").breakLine()
        .currentHeader()
        .current("Amount", Placeholders.REWARD_WIN_LIMIT_AMOUNT + GRAY + " (" + WHITE + LMB + GRAY + ")")
        .current("Cooldown", Placeholders.REWARD_WIN_LIMIT_COOLDOWN + GRAY + " (" + WHITE + RMB + GRAY + ")")
        .breakLine()
        .text("(" + WHITE + "Shift-Left" + GRAY + " to make one-timed)")
        .text("(" + WHITE + "Shift-Right" + GRAY + " to disable)")
        .build();

    public static final EditorLocale REWARD_IGNORED_PERMISSIONS = builder(PREFIX + "Reward.IgnoredPermissions")
        .name("Permission Restrictions")
        .text("Players having any of permissions", "listed below won't be able", "to win this reward.").breakLine()
        .currentHeader().text(Placeholders.REWARD_IGNORED_FOR_PERMISSIONS).breakLine()
        .text("(" + WHITE + LMB + GRAY + " to add)")
        .text("(" + WHITE + RMB + GRAY + " to remove all)")
        .build();

    public static final EditorLocale MILESTONE_CREATE = builder(PREFIX + "Milestone.Create")
        .name("New Milestone")
        .build();

    public static final EditorLocale MILESTONE_OBJECT = builder(PREFIX + "Milestone.Object")
        .name("Milestone: " + Placeholders.MILESTONE_OPENINGS)
        .current("Openings", Placeholders.MILESTONE_OPENINGS + GRAY + " (" + WHITE + LMB + GRAY + ")")
        .current("Reward Id", Placeholders.MILESTONE_REWARD_ID + GRAY + " (" + WHITE + RMB + GRAY + ")")
        .breakLine()
        .text("(" + WHITE + "Shift-Right" + GRAY + " to delete " + RED + "(no undo)" + GRAY + ")")
        .build();

    public static final EditorLocale KEY_OBJECT = builder(PREFIX + "Key.Object")
        .name(Placeholders.KEY_NAME + GRAY + " (ID: " + WHITE + Placeholders.KEY_ID + GRAY + ")")
        .current("Virtual", Placeholders.KEY_VIRTUAL).breakLine()
        .text("(" + WHITE + LMB + GRAY + " to edit)")
        .text("(" + WHITE + "Shift-Right" + GRAY + " to delete " + RED + "(no undo)").build();

    public static final EditorLocale KEY_CREATE = builder(PREFIX + "Key.Create")
        .name("New Key").build();

    public static final EditorLocale KEY_NAME = builder(PREFIX + "Key.DisplayName")
        .name("Display Name")
        .text("General key name, which")
        .text("is " + RED + "not" + GRAY + " related to key item name.").breakLine()
        .currentHeader().current("Name", Placeholders.KEY_NAME + GRAY + " (" + WHITE + LMB + GRAY + ")")
        .build();

    public static final EditorLocale KEY_ITEM = builder(PREFIX + "Key.Item")
        .name("Key Item")
        .text("Sets physical key item.").breakLine()
        .text("You should " + RED + "premade" + GRAY + " item " + YELLOW + "name", "and " + YELLOW + "lore" + GRAY + " before drop it here.").breakLine()
        .text("(" + WHITE + "Drag'n'Drop" + GRAY + " to replace)")
        .text("(" + WHITE + LMB + GRAY + " to get key item)")
        .text("(" + WHITE + RMB + GRAY + " to get raw copy)")
        .build();

    public static final EditorLocale KEY_VIRTUAL = builder(PREFIX + "Key.Virtual")
        .name("Virtual")
        .text("Sets whether or not the key is virtual.").breakLine()
        .currentHeader().current("Enabled", Placeholders.KEY_VIRTUAL + GRAY + " (" + WHITE + LMB + GRAY + ")")
        .build();
}
