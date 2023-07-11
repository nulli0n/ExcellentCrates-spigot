package su.nightexpress.excellentcrates.editor;

import su.nexmedia.engine.api.editor.EditorLocale;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.config.Config;

public class EditorLocales extends su.nexmedia.engine.api.editor.EditorLocales {

    private static final String PREFIX = "Editor.CrateEditorType."; // Old version compatibility

    public static final EditorLocale CRATES_EDITOR = builder(PREFIX + "EDITOR_CRATES")
        .name("Crates")
        .text("Create & manage your crates here!").breakLine()
        .actionsHeader().action("Left-Click", "Open").build();

    public static final EditorLocale KEYS_EDITOR = builder(PREFIX + "EDITOR_KEYS")
        .name("Keys")
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
        .text("Sets the crate display name.", "It's used in messages & GUIs.").breakLine()
        .currentHeader().current("Display Name", Placeholders.CRATE_NAME).breakLine()
        .warning("This is " + RED + "NOT" + GRAY + " crate item name!").breakLine()
        .actionsHeader().action("Left-Click", "Change").build();

    public static final EditorLocale CRATE_PERMISSION = builder(PREFIX + "CRATE_CHANGE_PERMISSION")
        .name("Permission Requirement")
        .text("Sets whether or not permission is required", "to open this crate.").breakLine()
        .currentHeader()
        .current("Required", Placeholders.CRATE_PERMISSION_REQUIRED)
        .current("Node", Placeholders.CRATE_PERMISSION).breakLine()
        .actionsHeader().action("Left-Click", "Toggle").build();

    public static final EditorLocale CRATE_OPEN_COOLDOWN = builder(PREFIX + "CRATE_CHANGE_COOLDOWN")
        .name("Open Cooldown")
        .text("Sets how much time player have to wait", "to open this crate again.").breakLine()
        .currentHeader().current("Cooldown", Placeholders.CRATE_OPENING_COOLDOWN).breakLine()
        .noteHeader().notes("Negative value will make crate one-timed!").breakLine()
        .actionsHeader()
        .action("Left-Click", "Change").action("Right-Click", "Disable")
        .action("[Q/Drop] Key", "One-Timed").build();

    public static final EditorLocale CRATE_CONFIG = builder(PREFIX + "CRATE_CHANGE_CONFIG")
        .name("Preview & Animation")
        .text("Defines the look of the crate", YELLOW + "preview " + GRAY + "and " + YELLOW + "animation " + GRAY + "GUIs.")
        .currentHeader()
        .current("Preview", Placeholders.CRATE_PREVIEW_CONFIG)
        .current("Opening", Placeholders.CRATE_ANIMATION_CONFIG).breakLine()
        .breakLine().noteHeader()
        .notes("Previews are located in " + ORANGE + Config.DIR_PREVIEWS + GRAY + " sub-folder.")
        .notes("Openings are located in " + ORANGE + Config.DIR_OPENINGS + GRAY + " sub-folder.")
        .breakLine().actionsHeader()
        .action("Left-Click", "Change Opening").action("Right-Click", "Disable Opening")
        .action("Shift-Left", "Change Preview").action("Shift-Right", "Disable Preview")
        .build();

    public static final EditorLocale CRATE_KEYS = builder(PREFIX + "CRATE_CHANGE_KEYS")
        .name("Attached Keys")
        .text("Sets which keys", "can be used to open this crate.").breakLine()
        .currentHeader().current("IDs", Placeholders.CRATE_KEY_IDS).breakLine()
        .warningHeader().warning("If no keys are set, crate can be opened without them!")
        .warning("If invalid keys provided, you will be unable to open crate!").breakLine()
        .actionsHeader().action("Left-Click", "Attach Key").action("Right-Click", "Clear List")
        .build();

    public static final EditorLocale CRATE_OPEN_COST = builder(PREFIX + "CRATE_CHANGE_OPEN_COST")
        .name("Open Cost")
        .text("Sets how much money/exp player", "have to pay in order to", "open this crate.").breakLine()
        .currentHeader()
        .current("Money", Placeholders.CRATE_OPENING_COST_MONEY)
        .current("Exp Levels", Placeholders.CRATE_OPENING_COST_EXP).breakLine()
        .actionsHeader()
        .action("Left-Click", "Change Money").action("Right-Click", "Change Exp")
        .action("[Q/Drop] Key", "Disable All")
        .build();

    public static final EditorLocale CRATE_ITEM = builder(PREFIX + "CRATE_CHANGE_ITEM")
        .name("Crate Item")
        .text("Sets the inventory crate item.", "It's used when you give crates to players", "and in crate menus.").breakLine()
        .noteHeader().notes("Use item with premade name, lore, model, etc.").breakLine()
        .warningHeader().warning("Use commands to give actual working crate item.").breakLine()
        .actionsHeader().action("Drag & Drop", "Replace").action("Right-Click", "Obtain")
        .build();

    public static final EditorLocale CRATE_BLOCK_LOCATIONS = builder(PREFIX + "CRATE_CHANGE_BLOCK_LOCATION")
        .name("Attached Blocks")
        .text("A set of blocks attached to", "this crate to open or preview", "it when interacted.").breakLine()
        .currentHeader().text(YELLOW + Placeholders.CRATE_BLOCK_LOCATIONS).breakLine()
        .actionsHeader().action("Left-Click", "Attach Block").action("Right-Click", "Clear List")
        .build();

    public static final EditorLocale CRATE_BLOCK_PUSHBACK = builder("Editor.Crate.BlockPushback")
        .name("Block Pushback")
        .text("Sets whether player will be pushed", "back from the crate block when", "unable to open it.").breakLine()
        .currentHeader().current("Enabled", Placeholders.CRATE_BLOCK_PUSHBACK_ENABLED).breakLine()
        .actionsHeader().action("Left-Click", "Toggle")
        .build();

    public static final EditorLocale CRATE_BLOCK_HOLOGRAM = builder(PREFIX + "CRATE_CHANGE_BLOCK_HOLOGRAM")
        .name("Block Hologram")
        .text("Sets whether hologram will be added", "above the crate block with specified text.", "Sets the Y offset for", "hologram location").breakLine()
        .currentHeader().current("Enabled", Placeholders.CRATE_BLOCK_HOLOGRAM_ENABLED)
        .current("Y Offset", Placeholders.CRATE_BLOCK_HOLOGRAM_OFFSET_Y)
        .current("Text", "").text(Placeholders.CRATE_BLOCK_HOLOGRAM_TEXT).breakLine()
        .actionsHeader().action("Left-Click", "Add Text").action("Right-Click", "Clear Text")
        .action("Shift-Left", "Toggle").action("Shift-Right", "Change Y Offset")
        .build();

    public static final EditorLocale CRATE_BLOCK_EFFECT = builder(PREFIX + "CRATE_CHANGE_BLOCK_EFFECT")
        .name("Block Effects")
        .text("Sets a particle effect to play", "around crate blocks.").breakLine()
        .currentHeader().current("Model", Placeholders.CRATE_BLOCK_EFFECT_MODEL)
        .current("Particle", Placeholders.CRATE_BLOCK_EFFECT_PARTICLE_NAME)
        .current("Data", Placeholders.CRATE_BLOCK_EFFECT_PARTICLE_DATA).breakLine()
        .actionsHeader().action("Left-Click", "Change Particle").action("Right-Click", "Change Data")
        .action("[Q/Drop] Key", "Toggle Model")
        .build();

    public static final EditorLocale CRATE_REWARDS = builder(PREFIX + "CRATE_CHANGE_REWARDS")
        .name("Rewards")
        .text("Create & manage rewards here!").breakLine()
        .actionsHeader().action("Left-Click", "Open")
        .build();

    public static final EditorLocale REWARD_OBJECT = builder(PREFIX + "REWARD_OBJECT")
        .name(Placeholders.REWARD_NAME + " &7(ID: &f" + Placeholders.REWARD_ID + "&7)")
        .text("Chance: &f" + Placeholders.REWARD_CHANCE + "%")
        .text("Rarity: &f" + Placeholders.REWARD_RARITY_NAME).breakLine()
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
        .action("[Num 1]", "By Chance").action("[Num 2]", "By Type")
        .action("[Num 3]", "By Name").action("[Num 4]", "By Rarity")
        .build();

    public static final EditorLocale REWARD_NAME = builder(PREFIX + "REWARD_CHANGE_NAME")
        .name("Display Name")
        .text("Sets the reward display name.", "It's used in GUIs & messages.").breakLine()
        .currentHeader().current("Display Name", Placeholders.REWARD_NAME).breakLine()
        .warningHeader().warning("This is " + RED + "NOT" + GRAY + " reward item name!").breakLine()
        .actionsHeader().action("Left-Click", "Change").action("Right-Click", "Sync from Preview")
        .action("Shift-Left", "Set for Preview")
        .build();

    public static final EditorLocale REWARD_PREVIEW = builder(PREFIX + "REWARD_CHANGE_PREVIEW")
        .name("Preview Item")
        .text("This item will represent the reward", "when previewing and opening crate.").breakLine()
        .warningHeader().warning("This item is " + RED + "NOT" + GRAY + " given to players!").breakLine()
        .actionsHeader().action("Drag & Drop", "Replace").action("Right-Click", "Get Copy")
        .build();

    public static final EditorLocale REWARD_RARITY = builder(PREFIX + "Reward.Rarity")
        .name("Rarity")
        .text("Sets the reward rarity.")
        .text("Learn more about it on the plugin wiki.").breakLine()
        .currentHeader().current("Rarity", Placeholders.REWARD_RARITY_NAME).breakLine()
        .actionsHeader().action("Left-Click", "Change")
        .build();

    public static final EditorLocale REWARD_CHANCE = builder(PREFIX + "REWARD_CHANGE_CHANCE")
        .name("Chance")
        .text("Sets the reward win chance (weight).")
        .text("Learn more about reward chances on the plugin wiki.").breakLine()
        .currentHeader().current("Chance", Placeholders.REWARD_CHANCE + "%").breakLine()
        .actionsHeader().action("Left-Click", "Change")
        .build();

    public static final EditorLocale REWARD_COMMANDS = builder(PREFIX + "REWARD_CHANGE_COMMANDS")
        .name("Run Commands")
        .text("A list of commands to run", "when player obtains this reward.").breakLine()
        .currentHeader().text(Placeholders.REWARD_COMMANDS).breakLine()
        .noteHeader().notes("Use " + ORANGE + Placeholders.PLAYER_NAME + GRAY + " placeholder for player name.").breakLine()
        .actionsHeader().action("Left-Click", "Add Command").action("Right-Click", "Clear List")
        .build();

    public static final EditorLocale REWARD_ITEMS = builder(PREFIX + "REWARD_CHANGE_ITEMS")
        .name("Given Items")
        .text("A list of items given", "when player obtains this reward.").breakLine()
        .noteHeader().notes("Use " + ORANGE + "ESC" + GRAY + " to save & return here.").breakLine()
        .actionsHeader().action("Left-Click", "Open")
        .build();

    public static final EditorLocale REWARD_BROADCAST = builder(PREFIX + "REWARD_CHANGE_BROADCAST")
        .name("Win Broadcast")
        .text("Sets whether or not a broadcast message will", "be sent to everyone when", "someone obtains this reward.").breakLine()
        .currentHeader().current("Enabled", Placeholders.REWARD_BROADCAST).breakLine()
        .actionsHeader().action("Left-Click", "Toggle")
        .build();

    public static final EditorLocale REWARD_WIN_LIMITS = builder(PREFIX + "REWARD_CHANGE_WIN_LIMITS")
        .name("Win Limits")
        .text("Sets how often & how many times player", "can obtain this reward again.").breakLine()
        .currentHeader()
        .current("Amount", Placeholders.REWARD_WIN_LIMIT_AMOUNT)
        .current("Cooldown", Placeholders.REWARD_WIN_LIMIT_COOLDOWN).breakLine()
        .noteHeader().notes("Set amount to " + ORANGE + "-1" + GRAY + " for unlimit.")
        .notes("Set cooldown to " + ORANGE + "-1" + GRAY + " for one-timed.").breakLine()
        .actionsHeader()
        .action("Left-Click", "Change Amount").action("Right-Click", "Change Cooldown")
        .action("Shift-Left", "One-Timed").action("Shift-Right", "Disable All")
        .build();

    public static final EditorLocale REWARD_IGNORED_PERMISSIONS = builder(PREFIX + "REWARD_CHANGE_IGNORED_FOR_PERMISSIONS")
        .name("Permission Restrictions")
        .text("Players having any permission", "from the list won't be able", "to obtain this reward.").breakLine()
        .currentHeader().text(Placeholders.REWARD_IGNORED_FOR_PERMISSIONS).breakLine()
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
        .text("Sets the key display name.", "It's used in GUIs & messages.").breakLine()
        .currentHeader().current("Display Name", Placeholders.KEY_NAME).breakLine()
        .warningHeader().warning("This is " + RED + "NOT" + GRAY + " actual key item name!").breakLine()
        .actionsHeader().action("Left-Click", "Change")
        .build();

    public static final EditorLocale KEY_ITEM = builder(PREFIX + "KEY_CHANGE_ITEM")
        .name("Key Item")
        .text("Sets the physical key item.").breakLine()
        .noteHeader().notes("This option is useless for virtual keys.", "Use item with premade name, lore, etc.").breakLine()
        .warningHeader().warning("Use commands to give actual working crate key.").breakLine()
        .actionsHeader().action("Drag & Drop", "Replace").action("Right-Click", "Obtain")
        .build();

    public static final EditorLocale KEY_VIRTUAL = builder(PREFIX + "KEY_VIRTUAL")
        .name("Virtual")
        .text("Sets whether or not the key is virtual.").breakLine()
        .currentHeader().current("Is Virtual", Placeholders.KEY_VIRTUAL).breakLine()
        .noteHeader().notes("Virtual keys stored in database, not in inventories.").breakLine()
        .actionsHeader().action("Left-Click", "Toggle")
        .build();
}
