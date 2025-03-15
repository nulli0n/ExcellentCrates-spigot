package su.nightexpress.excellentcrates.config;

import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.Sound;
import su.nightexpress.nightcore.core.CoreLang;
import su.nightexpress.nightcore.language.entry.LangItem;
import su.nightexpress.nightcore.language.entry.LangString;
import su.nightexpress.nightcore.language.entry.LangText;
import su.nightexpress.nightcore.language.entry.LangUIButton;

import static su.nightexpress.excellentcrates.Placeholders.*;
import static su.nightexpress.nightcore.language.tag.MessageTags.OUTPUT;
import static su.nightexpress.nightcore.language.tag.MessageTags.SOUND;
import static su.nightexpress.nightcore.util.text.tag.Tags.*;

public class Lang extends CoreLang {

    //public static final LangKeyed<Particle> PARTICLE = LangKeyed.of("Particles", Registry.PARTICLE_TYPE);

    public static final LangString COMMAND_ARGUMENT_NAME_CRATE = LangString.of("Command.Argument.Name.Crate", "crate");
    public static final LangString COMMAND_ARGUMENT_NAME_KEY   = LangString.of("Command.Argument.Name.Key", "key");
    public static final LangString COMMAND_ARGUMENT_NAME_X     = LangString.of("Command.Argument.Name.X", "x");
    public static final LangString COMMAND_ARGUMENT_NAME_Y     = LangString.of("Command.Argument.Name.Y", "y");
    public static final LangString COMMAND_ARGUMENT_NAME_Z     = LangString.of("Command.Argument.Name.Z", "z");

    public static final LangText ERROR_COMMAND_INVALID_CRATE_ARGUMENT = LangText.of("Error.Command.Argument.InvalidCrate",
        LIGHT_GRAY.enclose(LIGHT_RED.enclose(GENERIC_VALUE) + " is not a valid crate!"));

    public static final LangText ERROR_COMMAND_INVALID_KEY_ARGUMENT = LangText.of("Error.Command.Argument.InvalidKey",
        LIGHT_GRAY.enclose(LIGHT_RED.enclose(GENERIC_VALUE) + " is not a valid key!"));

    public static final LangString COMMAND_EDITOR_DESC         = LangString.of("Command.Editor.Desc", "Open editor GUI.");
    public static final LangString COMMAND_DROP_DESC           = LangString.of("Command.Drop.Desc", "Spawn crate item in the world.");
    public static final LangString COMMAND_DROP_KEY_DESC       = LangString.of("Command.DropKey.Desc", "Spawn key item in the world.");
    public static final LangString COMMAND_OPEN_DESC           = LangString.of("Command.Open.Desc", "Open a crate.");
    public static final LangString COMMAND_OPEN_FOR_DESC       = LangString.of("Command.OpenFor.Desc", "Open crate for a player.");
    public static final LangString COMMAND_GIVE_DESC           = LangString.of("Command.Give.Desc", "Gives crate to a player.");
    public static final LangString COMMAND_KEY_DESC            = LangString.of("Command.Key.Desc", "Manage player's keys.");
    public static final LangString COMMAND_KEY_GIVE_DESC       = LangString.of("Command.Key.Give.Desc", "Give key to a player.");
    public static final LangString COMMAND_KEY_TAKE_DESC       = LangString.of("Command.Key.Take.Desc", "Take key from a player.");
    public static final LangString COMMAND_KEY_SET_DESC        = LangString.of("Command.Key.Set.Desc", "Set keys amount for a player.");
    public static final LangString COMMAND_KEY_INSPECT_DESC    = LangString.of("Command.Key.Show.Desc", "Inspect [player's] virtual keys.");
    public static final LangString COMMAND_PREVIEW_DESC        = LangString.of("Command.Preview.Desc", "Open crate preview.");
    public static final LangString COMMAND_RESET_COOLDOWN_DESC = LangString.of("Command.ResetCooldown.Desc", "Reset player's crate open cooldown.");
    public static final LangString COMMAND_MENU_DESC           = LangString.of("Command.Menu.Desc", "Open crate menu.");

    public static final LangText COMMAND_DROP_DONE = LangText.of("Command.Drop.Done",
        LIGHT_GRAY.enclose("Dropped " + LIGHT_YELLOW.enclose(CRATE_NAME) + " at " + LIGHT_YELLOW.enclose(LOCATION_X + ", " + LOCATION_Y + ", " + LOCATION_Z) + " in " + LIGHT_YELLOW.enclose(LOCATION_WORLD) + "."));

    public static final LangText COMMAND_DROP_KEY_DONE = LangText.of("Command.DropKey.Done",
        LIGHT_GRAY.enclose("Dropped " + LIGHT_YELLOW.enclose(KEY_NAME) + " at " + LIGHT_YELLOW.enclose(LOCATION_X + ", " + LOCATION_Y + ", " + LOCATION_Z) + " in " + LIGHT_YELLOW.enclose(LOCATION_WORLD) + "."));



    public static final LangText COMMAND_OPEN_FOR_DONE = LangText.of("Command.OpenFor.Done",
        LIGHT_GRAY.enclose("Opened " + LIGHT_YELLOW.enclose(CRATE_NAME) + " for " + LIGHT_YELLOW.enclose(PLAYER_NAME) + "."));

    public static final LangText COMMAND_OPEN_FOR_NOTIFY = LangText.of("Command.OpenFor.Notify",
        LIGHT_GRAY.enclose("You have been forced to open " + LIGHT_YELLOW.enclose(CRATE_NAME) + "."));



    public static final LangText COMMAND_GIVE_DONE = LangText.of("Command.Give.Done",
        LIGHT_GRAY.enclose("Given " + LIGHT_YELLOW.enclose("x" + GENERIC_AMOUNT) + " of " + LIGHT_YELLOW.enclose(CRATE_NAME) + " crate(s) to " + LIGHT_YELLOW.enclose(PLAYER_NAME) + "."));

    public static final LangText COMMAND_GIVE_NOTIFY = LangText.of("Command.Give.Notify",
        LIGHT_GRAY.enclose("You recieved " + LIGHT_YELLOW.enclose("x" + GENERIC_AMOUNT) + " of " + LIGHT_YELLOW.enclose(CRATE_NAME) + "."));



    public static final LangText COMMAND_KEY_GIVE_DONE = LangText.of("Command.Key.Give.Done",
        LIGHT_GRAY.enclose("Given " + LIGHT_YELLOW.enclose("x" + GENERIC_AMOUNT) + " of " + LIGHT_YELLOW.enclose(KEY_NAME) + " key(s) to " + LIGHT_YELLOW.enclose(PLAYER_NAME) + "."));

    public static final LangText COMMAND_KEY_GIVE_NOTIFY = LangText.of("Command.Key.Give.Notify",
        LIGHT_GRAY.enclose("You recieved " + LIGHT_YELLOW.enclose("x" + GENERIC_AMOUNT) + " of " + LIGHT_YELLOW.enclose(KEY_NAME) + "!"));

    public static final LangString COMMAND_KEY_GIVE_ALL_DESC = LangString.of("Command.Key.GiveAll.Desc",
        "Give key to all online players.");

    public static final LangText COMMAND_KEY_GIVE_ALL_DONE = LangText.of("Command.Key.GiveAll.Done",
        LIGHT_GRAY.enclose("Given " + LIGHT_YELLOW.enclose("x" + GENERIC_AMOUNT) + " of " + LIGHT_YELLOW.enclose(KEY_NAME) + " key(s) to " + LIGHT_YELLOW.enclose("All Players") + "."));

    public static final LangText COMMAND_KEY_TAKE_DONE = LangText.of("Command.Key.Take.Done",
        LIGHT_GRAY.enclose("Taken " + LIGHT_YELLOW.enclose("x" + GENERIC_AMOUNT) + " of " + LIGHT_YELLOW.enclose(KEY_NAME) + " key(s) from " + LIGHT_YELLOW.enclose(PLAYER_NAME) + "."));

    public static final LangText COMMAND_KEY_TAKE_NOTIFY = LangText.of("Command.Key.Take.Notify",
        LIGHT_GRAY.enclose("You lost " + LIGHT_RED.enclose("x" + GENERIC_AMOUNT) + " " + LIGHT_RED.enclose(KEY_NAME) + "."));

    public static final LangText COMMAND_KEY_SET_DONE = LangText.of("Command.Key.Set.Done",
        LIGHT_GRAY.enclose("Set " + LIGHT_YELLOW.enclose("x" + GENERIC_AMOUNT) + " of " + LIGHT_YELLOW.enclose(KEY_NAME) + " key(s) for " + LIGHT_YELLOW.enclose(PLAYER_NAME) + "."));

    public static final LangText COMMAND_KEY_SET_NOTIFY = LangText.of("Command.Key.Set.Notify",
        LIGHT_GRAY.enclose("Your " + LIGHT_YELLOW.enclose(KEY_NAME) + "'s amount has been changed to " + LIGHT_YELLOW.enclose("x" + GENERIC_AMOUNT) + "."));



    public static final LangText COMMAND_KEY_INSPECT_LIST = LangText.of("Command.Key.Show.Format.List",
        TAG_NO_PREFIX,
        " ",
        LIGHT_YELLOW.enclose(BOLD.enclose(PLAYER_NAME + "'s Virtual Keys: ")),
        GENERIC_ENTRY,
        " "
    );

    public static final LangString COMMAND_KEY_INSPECT_ENTRY = LangString.of("Command.Key.Show.Format.Entry",
        LIGHT_YELLOW.enclose("▪ " + LIGHT_GRAY.enclose(KEY_NAME + ": ") + "x" + GENERIC_AMOUNT)
    );

    public static final LangText COMMAND_PREVIEW_DONE_OTHERS = LangText.of("Command.Preview.Done.Others",
        LIGHT_GRAY.enclose("Opened " + LIGHT_YELLOW.enclose(CRATE_NAME) + " preview for " + LIGHT_YELLOW.enclose(PLAYER_DISPLAY_NAME) + "."));

    public static final LangText COMMAND_RESET_COOLDOWN_DONE = LangText.of("Command.ResetCooldown.Done",
        LIGHT_GRAY.enclose("Reset " + LIGHT_YELLOW.enclose(PLAYER_NAME) + "'s open cooldown for " + LIGHT_YELLOW.enclose(CRATE_NAME) + "."));

    public static final LangText COMMAND_MENU_DONE_OTHERS = LangText.of("Command.Menu.Done.Others",
        LIGHT_GRAY.enclose("Opened crates menu for " + LIGHT_YELLOW.enclose(PLAYER_DISPLAY_NAME) + "."));





    public static final LangText CRATE_OPEN_ERROR_INVENTORY_SPACE = LangText.of("Crate.Open.Error.InventorySpace",
        OUTPUT.enclose(20, 80) + SOUND.enclose(Sound.ENTITY_VILLAGER_NO),
        LIGHT_RED.enclose(BOLD.enclose("Inventory is Full!")),
        LIGHT_GRAY.enclose("Clean up inventory to open crates.")
    );

    public static final LangText CRATE_OPEN_ERROR_COOLDOWN_TEMPORARY = LangText.of("Crate.Open.Error.Cooldown.Temporary",
        OUTPUT.enclose(20, 80) + SOUND.enclose(Sound.ENTITY_VILLAGER_NO),
        LIGHT_RED.enclose(BOLD.enclose("Crate is on Cooldown!")),
        LIGHT_GRAY.enclose("You can open it in " + LIGHT_RED.enclose(GENERIC_TIME))
    );

    public static final LangText CRATE_OPEN_ERROR_COOLDOWN_ONE_TIMED = LangText.of("Crate.Open.Error.Cooldown.OneTimed",
        OUTPUT.enclose(20, 80) + SOUND.enclose(Sound.ENTITY_VILLAGER_NO),
        LIGHT_RED.enclose(BOLD.enclose("Whoops!")),
        LIGHT_GRAY.enclose("You already have opened this one-timed crate!")
    );

    public static final LangText CRATE_OPEN_ERROR_NO_KEY = LangText.of("Crate.Open.Error.NoKey",
        OUTPUT.enclose(20, 80) + SOUND.enclose(Sound.ENTITY_VILLAGER_NO),
        LIGHT_RED.enclose(BOLD.enclose("Whoops!")),
        LIGHT_GRAY.enclose("You don't have a key for this crate!")
    );

    public static final LangText CRATE_OPEN_ERROR_NO_HOLD_KEY = LangText.of("Crate.Open.Error.NoHoldKey",
        OUTPUT.enclose(20, 80) + SOUND.enclose(Sound.ENTITY_VILLAGER_NO),
        LIGHT_RED.enclose(BOLD.enclose("Whoops!")),
        LIGHT_GRAY.enclose("You must hold a key to open a crate!")
    );

    public static final LangText CRATE_OPEN_ERROR_NO_REWARDS = LangText.of("Crate.Open.Error.NoRewards",
        OUTPUT.enclose(20, 80) + SOUND.enclose(Sound.ENTITY_VILLAGER_NO),
        RED.enclose(BOLD.enclose("Whoops!")),
        LIGHT_GRAY.enclose("There are no rewards for you! Try later.")
    );

    public static final LangText CRATE_OPEN_ERROR_TOO_EXPENSIVE = LangText.of("Crate.Open.Error.TooExpensive",
        OUTPUT.enclose(20, 80) + SOUND.enclose(Sound.ENTITY_VILLAGER_NO),
        LIGHT_RED.enclose(BOLD.enclose("Whoops!")),
        LIGHT_GRAY.enclose("You need " + LIGHT_RED.enclose(CRATE_OPEN_COST) + " to open it!")
    );

    public static final LangText CRATE_OPEN_ERROR_ALREADY = LangText.of("Crate.Open.Error.Already",
        OUTPUT.enclose(20, 80) + SOUND.enclose(Sound.ENTITY_VILLAGER_NO),
        RED.enclose(BOLD.enclose("Whoops!")),
        LIGHT_GRAY.enclose("You're already opening a crate!")
    );

    public static final LangText CRATE_OPEN_REWARD_INFO = LangText.of("Crate.Open.Reward.Info",
        TAG_NO_PREFIX,
        LIGHT_GRAY.enclose("You won " + LIGHT_GREEN.enclose(REWARD_NAME) + " from the " + LIGHT_GREEN.enclose(CRATE_NAME) + "!"));

    public static final LangText CRATE_OPEN_MILESTONE_COMPLETED = LangText.of("Crate.Open.Milestone.Completed",
        TAG_NO_PREFIX,
        SOUND.enclose(Sound.ENTITY_PLAYER_LEVELUP),
        LIGHT_GRAY.enclose("You completed " + LIGHT_GREEN.enclose(MILESTONE_OPENINGS + " Openings ") + "milestone and got " + LIGHT_GREEN.enclose(REWARD_NAME) + " as reward!")
    );

    public static final LangText CRATE_OPEN_REWARD_BROADCAST = LangText.of("Crate.Open.Reward.Broadcast",
        TAG_NO_PREFIX + SOUND.enclose(Sound.BLOCK_NOTE_BLOCK_BELL),
        " ",
        GRAY.enclose(LIGHT_PURPLE.enclose(PLAYER_DISPLAY_NAME) + " opened " + LIGHT_PURPLE.enclose(CRATE_NAME) + " and received " + LIGHT_PURPLE.enclose(REWARD_NAME) + "!"),
        " ",
        GRAY.enclose("Purchase keys: " + CLICK.enclose(LIGHT_PURPLE.enclose("[Click to open Store]"), ClickEvent.Action.OPEN_URL, "https://spigotmc.org/")),
        " "
    );

    public static final LangText CRATE_PREVIEW_ERROR_COOLDOWN = LangText.of("Crate.Preview.Error.Cooldown",
        LIGHT_GRAY.enclose("You can preview this crate again in " + LIGHT_RED.enclose(GENERIC_TIME))
    );

    public static final LangText CRATE_CREATE_ERROR_DUPLICATED = LangText.of("Crate.Create.Error.Duplicated",
        LIGHT_RED.enclose("Crate with such ID already exists!")
    );

    public static final LangText KEY_CREATE_ERROR_DUPLICATED = LangText.of("Key.Create.Error.Duplicated",
        LIGHT_RED.enclose("Key with such ID already exists!")
    );


    public static final LangString OTHER_MIDNIGHT = LangString.of("Other.Midnight", "Midnight");
    public static final LangString OTHER_FREE     = LangString.of("Other.Free", "Free");

    public static final LangItem OTHER_BROKEN_ITEM = LangItem.of("Other.BrokenItem",
        LIGHT_RED.enclose(BOLD.enclose("< Invalid Item >")),
        LIGHT_GRAY.enclose("This item wasn't parsed properly."),
        LIGHT_GRAY.enclose("Check console logs for details.")
    );

    public static final LangString INSPECTION_PROBLEMS    = LangString.of("Inspection.Problems", GENERIC_AMOUNT + " problem(s)");
    public static final LangString INSPECTION_NO_PROBLEMS = LangString.of("Inspection.NoProblems", "No problems found");

    public static final LangString EDITOR_TITLE_MAIN             = LangString.of("Editor.Title.Main", BLACK.enclose("ExcellentCrates Editor"));
    public static final LangString EDITOR_TITLE_CRATE_LIST       = LangString.of("Editor.Title.Crates", BLACK.enclose("Crates Editor"));
    public static final LangString EDITOR_TITLE_CRATE_SETTINGS   = LangString.of("Editor.Title.Crate.Settings", BLACK.enclose("Crate Settings"));
    public static final LangString EDITOR_TITLE_CRATE_MILESTONES = LangString.of("Editor.Title.Crate.Milestones", BLACK.enclose("Crate Milestones"));
    public static final LangString EDITOR_TITLE_CRATE_EFFECT     = LangString.of("Editor.Title.Crate.Effect", BLACK.enclose("Crate Effect"));
    public static final LangString EDITOR_TITLE_CRATE_PLACEMENT  = LangString.of("Editor.Title.Crate.Placement", BLACK.enclose("Crate Placement"));
    public static final LangString EDITOR_TITLE_REWARD_LIST      = LangString.of("Editor.Title.Reward.List", BLACK.enclose("Crate Rewards"));
    public static final LangString EDITOR_TITLE_REWARD_CREATION  = LangString.of("Editor.Title.Reward.Creation", BLACK.enclose("Reward Creator"));
    public static final LangString EDITOR_TITLE_REWARD_SETTINGS  = LangString.of("Editor.Title.Reward.Settings", BLACK.enclose("Reward Settings"));
    public static final LangString EDITOR_TITLE_REWARD_LIMITS    = LangString.of("Editor.Title.Reward.Limits", BLACK.enclose("Reward Limits"));
    public static final LangString EDITOR_TITLE_REWARD_SORT      = LangString.of("Editor.Title.Reward.Sort", BLACK.enclose("Reward Sorting"));
    public static final LangString EDITOR_TITLE_KEY_LIST         = LangString.of("Editor.Title.Keys", BLACK.enclose("Keys Editor"));
    public static final LangString EDITOR_TITLE_KEY_SETTINGS     = LangString.of("Editor.Title.Key.Settings", BLACK.enclose("Key Settings"));

    public static final LangString EDITOR_ENTER_DISPLAY_NAME = LangString.of("Editor.Enter.DisplayName",
        LIGHT_GRAY.enclose("Enter " + LIGHT_GREEN.enclose("[Display Name]")));

    public static final LangString EDITOR_ENTER_TEXT = LangString.of("Editor.Enter.Text",
        LIGHT_GRAY.enclose("Enter " + LIGHT_GREEN.enclose("[Text]")));

    public static final LangString EDITOR_ENTER_AMOUNT = LangString.of("Editor.Enter.Amount",
        LIGHT_GRAY.enclose("Enter " + LIGHT_GREEN.enclose("[Amount]")));

    public static final LangString EDITOR_ENTER_VALUE = LangString.of("Editor.Enter.Value",
        LIGHT_GRAY.enclose("Enter " + LIGHT_GREEN.enclose("[Value]")));

    public static final LangString EDITOR_ENTER_SECONDS = LangString.of("Editor.Crate.Enter.Seconds",
        LIGHT_GRAY.enclose("Enter " + LIGHT_GREEN.enclose("[Seconds Amount]")));

    public static final LangString EDITOR_ENTER_WEIGHT = LangString.of("Editor.Reward.Enter.Chance",
        LIGHT_GRAY.enclose("Enter " + LIGHT_GREEN.enclose("[Weight]")));

    public static final LangString EDITOR_ENTER_COMMAND = LangString.of("Editor.Reward.Enter.Command",
        LIGHT_GRAY.enclose("Enter " + LIGHT_GREEN.enclose("[Command]")));

    public static final LangString EDITOR_ENTER_CRATE_ID = LangString.of("Editor.Crate.Enter.Id",
        LIGHT_GRAY.enclose("Enter " + LIGHT_GREEN.enclose("[Crate Identifier]")));

    public static final LangString EDITOR_ENTER_MODEL_NAME = LangString.of("Editor.Crate.Enter.ModelName",
        LIGHT_GRAY.enclose("Enter " + LIGHT_GREEN.enclose("[Model Name]")));

    public static final LangString EDITOR_ENTER_PARTICLE_NAME = LangString.of("Editor.Crate.Enter.Particle.Name",
        LIGHT_GRAY.enclose("Enter " + LIGHT_GREEN.enclose("[Particle Name]")));

    public static final LangString EDITOR_ENTER_KEY_ID = LangString.of("Editor.Crate.Enter.KeyId",
        LIGHT_GRAY.enclose("Enter " + LIGHT_GREEN.enclose("[Key Identifier]")));

    public static final LangString EDITOR_ENTER_BLOCK_LOCATION = LangString.of("Editor.Crate.Enter.BlockLocation",
        LIGHT_GRAY.enclose("Click a " + LIGHT_GREEN.enclose("[Block] ") + " to assign crate."));

    public static final LangString EDITOR_ENTER_HOLOGRAM_TEMPLATE = LangString.of("Editor.Crate.Enter.HologramTemplate",
        LIGHT_GRAY.enclose("Enter " + LIGHT_GREEN.enclose("[Hologram Template]")));

    public static final LangString EDITOR_ENTER_ANIMATION_ID = LangString.of("Editor.Crate.Enter.AnimationConfig",
        LIGHT_GRAY.enclose("Enter " + LIGHT_GREEN.enclose("[Animation Name]")));

    public static final LangString EDITOR_ENTER_PREVIEW_ID = LangString.of("Editor.Crate.Enter.PreviewConfig",
        LIGHT_GRAY.enclose("Enter " + LIGHT_GREEN.enclose("[Preview Name]")));

    public static final LangString EDITOR_ENTER_CURRENCY = LangString.of("Editor.Crate.Enter.Currency",
        LIGHT_GRAY.enclose("Enter " + LIGHT_GREEN.enclose("[Currency ID]")));

    public static final LangString EDITOR_ENTER_REWARD_ID = LangString.of("Editor.Reward.Enter.Id",
        LIGHT_GRAY.enclose("Enter " + LIGHT_GREEN.enclose("[Reward Identifier]")));

    public static final LangString EDITOR_ENTER_RARITY = LangString.of("Editor.Reward.Enter.Rarity",
        LIGHT_GRAY.enclose("Enter " + LIGHT_GREEN.enclose("[Rarity]")));

    public static final LangString EDITOR_ENTER_PERMISSION = LangString.of("Editor.Reward.Enter.Permissions",
        LIGHT_GRAY.enclose("Enter " + LIGHT_GREEN.enclose("[Permission Node]")));

    public static final LangUIButton EDITOR_BUTTON_CRATE_ITEM_STACKABLE = LangUIButton.builder("Editor.Button.Crate.ItemStackable", "Item Stackable")
        .current(CRATE_ITEM_STACKABLE)
        .description("Controls whether crate item is stackable.")
        .leftClick("toggle")
        .build();

    public static final LangUIButton EDITOR_BUTTON_KEY_ITEM_STACKABLE = LangUIButton.builder("Editor.Button.Key.ItemStackable", "Item Stackable")
        .current(KEY_ITEM_STACKABLE)
        .description("Controls whether key item is stackable.")
        .leftClick("toggle")
        .build();
}
