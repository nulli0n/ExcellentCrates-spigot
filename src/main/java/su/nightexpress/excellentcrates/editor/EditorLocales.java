package su.nightexpress.excellentcrates.editor;

import su.nexmedia.engine.api.editor.EditorLocale;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.config.Config;

public class EditorLocales extends su.nexmedia.engine.api.editor.EditorLocales {

    private static final String PREFIX = "Editor.CrateEditorType."; // Old version compatibility

    public static final EditorLocale CRATES_EDITOR = builder(PREFIX + "EDITOR_CRATES")
        .name("Crates Editor")
        .text("Create & manage your crates here!").breakLine()
        .actionsHeader().action("Left-Click", "Open").build();

    public static final EditorLocale KEYS_EDITOR = builder(PREFIX + "EDITOR_KEYS")
        .name("Keys Editor")
        .text("Create & manage your keys here!").breakLine()
        .actionsHeader().action("Left-Click", "Open").build();

    public static final EditorLocale CRATE_OBJECT = builder(PREFIX + "CRATE_OBJECT")
        .name(Placeholders.CRATE_NAME + " &7(ID: &f" + Placeholders.CRATE_ID + "&7)")
        .actionsHeader()
        .action("Left-Click","Edit")
        .action( "Shift-Right","Delete " + RED + "(No Undo)").build();

    public static final EditorLocale CRATE_CREATE = builder(PREFIX + "CRATE_CREATE")
        .name("Create a Crate")
        .text("Creates a new crate.")
        .actionsHeader().action("Left-Click", "Create").build();

    public static final EditorLocale CRATE_NAME = builder(PREFIX + "CRATE_CHANGE_NAME")
        .name("Display Name")
        .currentHeader().current("Display Name", Placeholders.CRATE_NAME).breakLine()
        .text("Sets the crate display name.", "It's used in messages & GUIs.").breakLine()
        .warning("This is " + RED + "NOT" + GRAY + " crate item name!").breakLine()
        .actionsHeader().action("Left-Click", "Change").build();

    public static final EditorLocale CRATE_PERMISSION = builder(PREFIX + "CRATE_CHANGE_PERMISSION")
        .name("Permission Requirement")
        .currentHeader()
        .current("Required", Placeholders.CRATE_PERMISSION_REQUIRED)
        .current("Node", Placeholders.CRATE_PERMISSION).breakLine()
        .text("Sets whether permission is required", "to open this crate.").breakLine()
        .actionsHeader().action("Left-Click", "Toggle").build();

    public static final EditorLocale CRATE_OPEN_COOLDOWN = builder(PREFIX + "CRATE_CHANGE_COOLDOWN")
        .name("Open Cooldown")
        .currentHeader().current("Cooldown", Placeholders.CRATE_OPENING_COOLDOWN).breakLine()
        .text("Sets how often player can", "open this crate again.").breakLine()
        .noteHeader().notes("Negative value will make crate one-timed!").breakLine()
        .actionsHeader()
        .action("Left-Click", "Change").action("Right-Click", "Disable")
        .action("[Q/Drop] Key", "One-Timed").build();

    public static final EditorLocale CRATE_CONFIG = builder(PREFIX + "CRATE_CHANGE_CONFIG")
        .name("Preview & Animation")
        .currentHeader()
        .current("Preview", Placeholders.CRATE_PREVIEW_CONFIG)
        .current("Opening", Placeholders.CRATE_ANIMATION_CONFIG).breakLine()
        .text("Defines the look of the crate", YELLOW + "preview " + GRAY + "and " + YELLOW + "animation " + GRAY + "GUIs.")
        .breakLine().noteHeader()
        .notes("Previews are located in " + ORANGE + Config.DIR_PREVIEWS + GRAY + " sub-folder.")
        .notes("Openings are located in " + ORANGE + Config.DIR_OPENINGS + GRAY + " sub-folder.")
        .breakLine().actionsHeader()
        .action("Left-Click", "Change Opening").action("Right-Click", "Disable Opening")
        .action("Shift-Left", "Change Preview").action("Shift-Right", "Disable Preview")
        .build();

    public static final EditorLocale CRATE_KEYS = builder(PREFIX + "CRATE_CHANGE_KEYS")
        .name("Attached Keys")
        .currentHeader().current("IDs", Placeholders.CRATE_KEY_IDS).breakLine()
        .text("Sets a list of keys that", "can be used to open this crate.").breakLine()
        .warningHeader().warning("If no keys are set, crate can be opened without them!")
        .warning("If invalid keys provided, you will be unable to open crate!").breakLine()
        .actionsHeader().action("Left-Click", "Attach Key").action("Right-Click", "Clear List")
        .build();

    public static final EditorLocale CRATE_OPEN_COST = builder(PREFIX + "CRATE_CHANGE_OPEN_COST")
        .name("Open Cost")
        .currentHeader()
        .current("Money", Placeholders.CRATE_OPENING_COST_MONEY)
        .current("Exp Levels", Placeholders.CRATE_OPENING_COST_EXP).breakLine()
        .text("Sets how many money/exp player", "have to pay in order to", "open this crate.").breakLine()
        .actionsHeader()
        .action("Left-Click", "Change Money").action("Right-Click", "Change Exp")
        .action("[Q/Drop] Key", "Disable All")
        .build();

    public static final EditorLocale CRATE_ITEM = builder(PREFIX + "CRATE_CHANGE_ITEM")
        .name("Crate Item")
        .text("Sets the inventory crate item.", "It's used when you give crates to players", "and in crate menus.").breakLine()
        .noteHeader().notes("Use item with premade name, lore, model, etc.").breakLine()
        .actionsHeader().action("Drag & Drop", "Replace").action("Right-Click", "Obtain")
        .build();

    public static final EditorLocale CRATE_BLOCK_LOCATIONS = builder(PREFIX + "CRATE_CHANGE_BLOCK_LOCATION")
        .name("Attached Blocks")
        .currentHeader().text(YELLOW + Placeholders.CRATE_BLOCK_LOCATIONS).breakLine()
        .text("A set of blocks attached to", "this crate to open or preview", "it when interacted.").breakLine()
        .actionsHeader().action("Left-Click", "Attach Block").action("Right-Click", "Clear List")
        .build();

    public static final EditorLocale CRATE_BLOCK_PUSHBACK = builder("Editor.Crate.BlockPushback")
        .name("Block Pushback")
        .currentHeader().current("Enabled", Placeholders.CRATE_BLOCK_PUSHBACK_ENABLED).breakLine()
        .text("Sets whether player will be pushed", "back from the crate block when", "trying to open it without a key.").breakLine()
        .actionsHeader().action("Left-Click", "Toggle")
        .build();

    public static final EditorLocale CRATE_BLOCK_HOLOGRAM = builder(PREFIX + "CRATE_CHANGE_BLOCK_HOLOGRAM")
        .name("Block Hologram")
        .currentHeader().current("Enabled", Placeholders.CRATE_BLOCK_HOLOGRAM_ENABLED)
        .current("Y Offset", Placeholders.CRATE_BLOCK_HOLOGRAM_OFFSET_Y)
        .current("Text", Placeholders.CRATE_BLOCK_HOLOGRAM_TEXT).breakLine()
        .text("Sets whether hologram will be added", "above the crate block with specified text.", "Sets the Y offset for", "hologram location").breakLine()
        .actionsHeader().action("Left-Click", "Add Text").action("Right-Click", "Clear Text")
        .action("Shift-Left", "Toggle").action("Shift-Right", "Change Y Offset")
        .build();

    public static final EditorLocale CRATE_BLOCK_EFFECT = builder(PREFIX + "CRATE_CHANGE_BLOCK_EFFECT")
        .name("Block Effects")
        .currentHeader().current("Model", Placeholders.CRATE_BLOCK_EFFECT_MODEL)
        .current("Particle", Placeholders.CRATE_BLOCK_EFFECT_PARTICLE_NAME)
        .current("Data", Placeholders.CRATE_BLOCK_EFFECT_PARTICLE_DATA).breakLine()
        .text("Sets a particle effect to play", "around crate blocks.").breakLine()
        .actionsHeader().action("Left-Click", "Change Particle").action("Right-Click", "Change Data")
        .action("[Q/Drop] Key", "Toggle Model")
        .build();

    public static final EditorLocale CRATE_REWARDS = builder(PREFIX + "CRATE_CHANGE_REWARDS")
        .name("Crate Rewards")
        .text("Create & manage rewards here!").breakLine()
        .actionsHeader().action("Left-Click", "Open")
        .build();

    public static final EditorLocale REWARD_OBJECT = builder(PREFIX + "REWARD_OBJECT")
        .name(Placeholders.REWARD_NAME + " &7(ID: &f" + Placeholders.REWARD_ID + "&7)")
        .text("Chance: &f" + Placeholders.REWARD_CHANCE + "%").breakLine()
        .actionsHeader().action("Left-Click", "Edit")
        .action("Shift-Left", "Move Forward").action("Shift-Right", "Move Backward")
        .action("[Q/Drop] Key", "Delete " + RED + "(No Undo)")
        .build();

    public static final EditorLocale REWARD_CREATE = builder(PREFIX + "REWARD_CREATE")
        .name("Create Reward")
        .text("Creates a new reward for crate.").breakLine()
        .actionsHeader().action("Left-Click", "Manual Creation")
        .action("Drag & Drop", "Fast Creation")
        .build();

    public static final EditorLocale REWARD_SORT = builder(PREFIX + "REWARD_SORT")
        .name("Sort Rewards")
        .text("Automatically sorts rewards in", "specified order.").breakLine()
        .actionsHeader()
        .action("[Num 1]", "By Chance").action("[Num 2]", "By Type").action("[Num 3]", "By Name")
        .build();

    public static final EditorLocale REWARD_NAME = builder(PREFIX + "REWARD_CHANGE_NAME")
        .name("Display Name")
        .currentHeader().current("Display Name", Placeholders.REWARD_NAME).breakLine()
        .text("Sets the reward display name.", "It's used in GUIs & messages.").breakLine()
        .warningHeader().warning("This is " + RED + "NOT" + GRAY + " reward item name!").breakLine()
        .actionsHeader().action("Left-Click", "Change").action("Right-Click", "Sync from Preview")
        .build();

    public static final EditorLocale REWARD_PREVIEW = builder(PREFIX + "REWARD_CHANGE_PREVIEW")
        .name("Preview Item")
        .text("Sets the reward preview item.", "It's used in crate opening animations.").breakLine()
        .warningHeader().warning("This is " + RED + "NOT" + GRAY + " reward actual item!").breakLine()
        .actionsHeader().action("Drag & Drop", "Replace").action("Right-Click", "Obtain")
        .build();

    public static final EditorLocale REWARD_CHANCE = builder(PREFIX + "REWARD_CHANGE_CHANCE")
        .name("Chance")
        .currentHeader().current("Chance", Placeholders.REWARD_CHANCE + "%").breakLine()
        .text("Sets the chance (weight) for this", "reward to be rolled.").breakLine()
        .noteHeader().notes("Learn about reward chances on the plugin wiki.").breakLine()
        .actionsHeader().action("Left-Click", "Change")
        .build();

    public static final EditorLocale REWARD_COMMANDS = builder(PREFIX + "REWARD_CHANGE_COMMANDS")
        .name("Run Commands")
        .currentHeader().text(Placeholders.REWARD_COMMANDS).breakLine()
        .text("Sets a list of commands to run", "when player receives this reward.").breakLine()
        .noteHeader().notes("Use " + ORANGE + Placeholders.Player.NAME + GRAY + " placeholder for player name.")
        .notes("Add " + ORANGE + "[CONSOLE]" + GRAY + " prefix to run it from server.").breakLine()
        .actionsHeader().action("Left-Click", "Add Command").action("Right-Click", "Clear List")
        .build();

    public static final EditorLocale REWARD_ITEMS = builder(PREFIX + "REWARD_CHANGE_ITEMS")
        .name("Given Items")
        .text("A list of items will be given", "to player when receive this reward.").breakLine()
        .noteHeader().notes("Use " + ORANGE + "ESC" + GRAY + " to save & return here.").breakLine()
        .actionsHeader().action("Left-Click", "Open")
        .build();

    public static final EditorLocale REWARD_BROADCAST = builder(PREFIX + "REWARD_CHANGE_BROADCAST")
        .name("Win Broadcast")
        .currentHeader().current("Enabled", Placeholders.REWARD_BROADCAST).breakLine()
        .text("Sets whether a broadcast message should", "be sent to everyone when this", "reward is received.").breakLine()
        .actionsHeader().action("Left-Click", "Toggle")
        .build();

    public static final EditorLocale REWARD_WIN_LIMITS = builder(PREFIX + "REWARD_CHANGE_WIN_LIMITS")
        .name("Win Limits")
        .currentHeader()
        .current("Amount", Placeholders.REWARD_WIN_LIMIT_AMOUNT)
        .current("Cooldown", Placeholders.REWARD_WIN_LIMIT_COOLDOWN).breakLine()
        .text("Sets how often & how many times player", "can receive this reward again.").breakLine()
        .noteHeader().notes("Set amount to " + ORANGE + "-1" + GRAY + " for unlimit.")
        .notes("Set cooldown to " + ORANGE + "-1" + GRAY + " for one-timed.").breakLine()
        .actionsHeader()
        .action("Left-Click", "Change Amount").action("Right-Click", "Change Cooldown")
        .action("Shift-Left", "One-Timed").action("Shift-Right", "Disable All")
        .build();

    public static final EditorLocale REWARD_IGNORED_PERMISSIONS = builder(PREFIX + "REWARD_CHANGE_IGNORED_FOR_PERMISSIONS")
        .name("Permission Restrictions")
        .currentHeader().text(Placeholders.REWARD_IGNORED_FOR_PERMISSIONS).breakLine()
        .text("Players having any permission", "from the list won't be able", "to obtain this reward.").breakLine()
        .actionsHeader().action("Left-Click", "Add Permission").action("Right-Click", "Clear List")
        .build();

    public static final EditorLocale KEY_OBJECT = builder(PREFIX + "KEY_OBJECT")
        .name(Placeholders.KEY_NAME + GRAY + " (ID: " + BLUE + Placeholders.KEY_ID + GRAY + ")")
        .current("Virtual", Placeholders.KEY_VIRTUAL).breakLine()
        .actionsHeader().action("Left-Click", "Edit")
        .action("Shift-Right", "Delete " + RED + "(No Undo)")
        .build();

    public static final EditorLocale KEY_CREATE = builder(PREFIX + "KEY_CREATE")
        .name("Create Key")
        .text("Creates a new crates key.").breakLine()
        .actionsHeader().action("Left-Click", "Create")
        .build();

    public static final EditorLocale KEY_NAME = builder(PREFIX + "KEY_CHANGE_NAME")
        .name("Display Name")
        .currentHeader().current("Display Name", Placeholders.KEY_NAME).breakLine()
        .text("Sets the key display name.", "It's used in GUIs & messages.").breakLine()
        .warningHeader().warning("This is " + RED + "NOT" + GRAY + " actual key item name!").breakLine()
        .actionsHeader().action("Left-Click", "Change")
        .build();

    public static final EditorLocale KEY_ITEM = builder(PREFIX + "KEY_CHANGE_ITEM")
        .name("Key Item")
        .text("Sets the physical key item.").breakLine()
        .noteHeader().notes("This option is useless for virtual keys.", "Use item with premade name, lore, etc.").breakLine()
        .actionsHeader().action("Drag & Drop", "Replace").action("Right-Click", "Obtain")
        .build();

    public static final EditorLocale KEY_VIRTUAL = builder(PREFIX + "KEY_VIRTUAL")
        .name("Virtual")
        .currentHeader().current("Is Virtual", Placeholders.KEY_VIRTUAL).breakLine()
        .text("Sets whether this key is virtual one.").breakLine()
        .noteHeader().notes("Virtual keys stored in database, not in inventories.").breakLine()
        .actionsHeader().action("Left-Click", "Toggle")
        .build();
}
