package su.nightexpress.excellentcrates.config;

import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.Sound;
import su.nightexpress.nightcore.core.CoreLang;
import su.nightexpress.nightcore.language.entry.LangString;
import su.nightexpress.nightcore.language.entry.LangText;

import static su.nightexpress.nightcore.util.text.tag.Tags.*;
import static su.nightexpress.nightcore.language.tag.MessageTags.*;
import static su.nightexpress.excellentcrates.Placeholders.*;

public class Lang extends CoreLang {

    public static final LangString COMMAND_EDITOR_DESC = LangString.of("Command.Editor.Desc",
        "Open crates & keys editor.");


    public static final LangString COMMAND_DROP_USAGE = LangString.of("Command.Drop.Usage",
        "<crate> <x> <y> <z> [world]");

    public static final LangString COMMAND_DROP_DESC = LangString.of("Command.Drop.Desc",
        "Drop crate at specified location in the world.");

    public static final LangText COMMAND_DROP_DONE = LangText.of("Command.Drop.Done",
        LIGHT_GRAY.enclose("Dropped " + LIGHT_YELLOW.enclose(CRATE_NAME) + " at " + LIGHT_YELLOW.enclose(LOCATION_X + ", " + LOCATION_Y + ", " + LOCATION_Z) + " in " + LIGHT_YELLOW.enclose(LOCATION_WORLD) + "."));


    public static final LangString COMMAND_DROP_KEY_USAGE = LangString.of("Command.DropKey.Usage",
        "<key> <x> <y> <z> [world]");

    public static final LangString COMMAND_DROP_KEY_DESC = LangString.of("Command.DropKey.Desc",
        "Drop key at specified location.");

    public static final LangText COMMAND_DROP_KEY_DONE = LangText.of("Command.DropKey.Done",
        LIGHT_GRAY.enclose("Dropped " + LIGHT_YELLOW.enclose(KEY_NAME) + " at " + LIGHT_YELLOW.enclose(LOCATION_X + ", " + LOCATION_Y + ", " + LOCATION_Z) + " in " + LIGHT_YELLOW.enclose(LOCATION_WORLD) + "."));


    public static final LangString COMMAND_OPEN_DESC = LangString.of("Command.Open.Desc",
        "Open a crate.");

    public static final LangString COMMAND_OPEN_USAGE = LangString.of("Command.Open.Usage",
        "<crate>");


    public static final LangString COMMAND_OPEN_FOR_DESC = LangString.of("Command.OpenFor.Desc",
        "Open crate for a player.");

    public static final LangString COMMAND_OPEN_FOR_USAGE = LangString.of("Command.OpenFor.Usage",
        "<player> <crate> [-f] [-s]");

    public static final LangText COMMAND_OPEN_FOR_DONE = LangText.of("Command.OpenFor.Done",
        LIGHT_GRAY.enclose("Opened " + LIGHT_YELLOW.enclose(CRATE_NAME) + " for " + LIGHT_YELLOW.enclose(PLAYER_NAME) + "."));

    public static final LangText COMMAND_OPEN_FOR_NOTIFY = LangText.of("Command.OpenFor.Notify",
        LIGHT_GRAY.enclose("You have been forced to open " + LIGHT_YELLOW.enclose(CRATE_NAME) + "."));


    public static final LangString COMMAND_GIVE_USAGE = LangString.of("Command.Give.Usage",
        "<player> <crate> [amount] [-s]");

    public static final LangString COMMAND_GIVE_DESC = LangString.of("Command.Give.Desc",
        "Gives crate to a player.");

    public static final LangText COMMAND_GIVE_DONE = LangText.of("Command.Give.Done",
        LIGHT_GRAY.enclose("Given " + LIGHT_YELLOW.enclose("x" + GENERIC_AMOUNT) + " of " + LIGHT_YELLOW.enclose(CRATE_NAME) + " crate(s) to " + LIGHT_YELLOW.enclose(PLAYER_NAME) + "."));

    public static final LangText COMMAND_GIVE_NOTIFY = LangText.of("Command.Give.Notify",
        LIGHT_GRAY.enclose("You recieved " + LIGHT_YELLOW.enclose("x" + GENERIC_AMOUNT) + " of " + LIGHT_YELLOW.enclose(CRATE_NAME) + "."));


    public static final LangString COMMAND_KEY_DESC = LangString.of("Command.Key.Desc",
        "Manage player's keys.");

    public static final LangString COMMAND_KEY_USAGE = LangString.of("Command.Key.Usage",
        "[help]");


    public static final LangString COMMAND_KEY_GIVE_USAGE = LangString.of("Command.Key.Give.Usage",
        "<player> <key> <amount> [-s] [-nosave]");

    public static final LangString COMMAND_KEY_GIVE_DESC = LangString.of("Command.Key.Give.Desc",
        "Give key to a player.");

    public static final LangText COMMAND_KEY_GIVE_DONE = LangText.of("Command.Key.Give.Done",
        LIGHT_GRAY.enclose("Given " + LIGHT_YELLOW.enclose("x" + GENERIC_AMOUNT) + " of " + LIGHT_YELLOW.enclose(KEY_NAME) + " key(s) to " + LIGHT_YELLOW.enclose(PLAYER_NAME) + "."));

    public static final LangText COMMAND_KEY_GIVE_NOTIFY = LangText.of("Command.Key.Give.Notify",
        LIGHT_GRAY.enclose("You recieved " + LIGHT_YELLOW.enclose("x" + GENERIC_AMOUNT) + " of " + LIGHT_YELLOW.enclose(KEY_NAME) + "!"));


    public static final LangString COMMAND_KEY_GIVE_ALL_USAGE = LangString.of("Command.Key.GiveAll.Usage",
        "<player> <key> <amount> [-s]");

    public static final LangString COMMAND_KEY_GIVE_ALL_DESC = LangString.of("Command.Key.GiveAll.Desc",
        "Give key to all online players.");

    public static final LangText COMMAND_KEY_GIVE_ALL_DONE = LangText.of("Command.Key.GiveAll.Done",
        LIGHT_GRAY.enclose("Given " + LIGHT_YELLOW.enclose("x" + GENERIC_AMOUNT) + " of " + LIGHT_YELLOW.enclose(KEY_NAME) + " key(s) to " + LIGHT_YELLOW.enclose("All Players") + "."));


    public static final LangString COMMAND_KEY_TAKE_USAGE = LangString.of("Command.Key.Take.Usage",
        "<player> <key> <amount> [-s] [-nosave]");

    public static final LangString COMMAND_KEY_TAKE_DESC = LangString.of("Command.Key.Take.Desc",
        "Take key from a player.");

    public static final LangText COMMAND_KEY_TAKE_DONE = LangText.of("Command.Key.Take.Done",
        LIGHT_GRAY.enclose("Taken " + LIGHT_YELLOW.enclose("x" + GENERIC_AMOUNT) + " of " + LIGHT_YELLOW.enclose(KEY_NAME) + " key(s) from " + LIGHT_YELLOW.enclose(PLAYER_NAME) + "."));

    public static final LangText COMMAND_KEY_TAKE_NOTIFY = LangText.of("Command.Key.Take.Notify",
        LIGHT_GRAY.enclose("You lost " + LIGHT_RED.enclose("x" + GENERIC_AMOUNT) + " " + LIGHT_RED.enclose(KEY_NAME) + "."));


    public static final LangString COMMAND_KEY_SET_USAGE = LangString.of("Command.Key.Set.Usage",
        "<player> <key> <amount> [-s] [-nosave]");

    public static final LangString COMMAND_KEY_SET_DESC = LangString.of("Command.Key.Set.Desc",
        "Set keys amount for a player.");

    public static final LangText COMMAND_KEY_SET_DONE = LangText.of("Command.Key.Set.Done",
        LIGHT_GRAY.enclose("Set " + LIGHT_YELLOW.enclose("x" + GENERIC_AMOUNT) + " of " + LIGHT_YELLOW.enclose(KEY_NAME) + " key(s) for " + LIGHT_YELLOW.enclose(PLAYER_NAME) + "."));

    public static final LangText COMMAND_KEY_SET_NOTIFY = LangText.of("Command.Key.Set.Notify",
        LIGHT_GRAY.enclose("Your " + LIGHT_YELLOW.enclose(KEY_NAME) + "'s amount has been changed to " + LIGHT_YELLOW.enclose("x" + GENERIC_AMOUNT) + "."));


    public static final LangString COMMAND_KEY_INSPECT_DESC = LangString.of("Command.Key.Show.Desc",
        "Inspect [player's] virtual keys.");

    public static final LangString COMMAND_KEY_INSPECT_USAGE = LangString.of("Command.Key.Show.Usage",
        "[player]");

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


    public static final LangString COMMAND_PREVIEW_DESC = LangString.of("Command.Preview.Desc",
        "Open crate preview.");

    public static final LangString COMMAND_PREVIEW_USAGE = LangString.of("Command.Preview.Usage",
        "<crate> [player]");

    public static final LangText COMMAND_PREVIEW_DONE_OTHERS = LangText.of("Command.Preview.Done.Others",
        LIGHT_GRAY.enclose("Opened " + LIGHT_YELLOW.enclose(CRATE_NAME) + " preview for " + LIGHT_YELLOW.enclose(PLAYER_DISPLAY_NAME) + "."));


    public static final LangString COMMAND_RESET_LIMIT_DESC = LangString.of("Command.ResetLimit.Desc",
        "Reset reward win limit for specified crate and reward.");

    public static final LangString COMMAND_RESET_LIMIT_USAGE = LangString.of("Command.ResetLimit.Usage",
        "<player> <crate> [reward]");

    public static final LangText COMMAND_RESET_LIMIT_DONE_CRATE = LangText.of("Command.ResetLimit.Done.Crate",
        LIGHT_GRAY.enclose("Reset " + LIGHT_YELLOW.enclose(PLAYER_NAME) + " win limit for all rewards of " + LIGHT_YELLOW.enclose(CRATE_NAME) + "."));

    public static final LangText COMMAND_RESET_LIMIT_DONE_REWARD = LangText.of("Command.ResetLimit.Done.Reward",
        LIGHT_GRAY.enclose("Reset " + LIGHT_YELLOW.enclose(PLAYER_NAME) + " win limit for " + LIGHT_YELLOW.enclose(REWARD_NAME) + " reward of " + LIGHT_YELLOW.enclose(CRATE_NAME) + "."));


    public static final LangString COMMAND_RESET_COOLDOWN_DESC = LangString.of("Command.ResetCooldown.Desc",
        "Reset player's crate open cooldown.");

    public static final LangString COMMAND_RESET_COOLDOWN_USAGE = LangString.of("Command.ResetCooldown.Usage",
        "<player> <crate>");

    public static final LangText COMMAND_RESET_COOLDOWN_DONE = LangText.of("Command.ResetCooldown.Done",
        LIGHT_GRAY.enclose("Reset " + LIGHT_YELLOW.enclose(PLAYER_NAME) + "'s open cooldown for " + LIGHT_YELLOW.enclose(CRATE_NAME) + "."));


    public static final LangString COMMAND_MENU_USAGE = LangString.of("Command.Menu.Usage",
        "[menu]");

    public static final LangString COMMAND_MENU_DESC = LangString.of("Command.Menu.Desc",
        "Open crate menu.");

    public static final LangText COMMAND_MENU_DONE_OTHERS = LangText.of("Command.Menu.Done.Others",
        LIGHT_GRAY.enclose("Opened crates menu for " + LIGHT_YELLOW.enclose(PLAYER_DISPLAY_NAME) + "."));


    public static final LangText CRATE_OPEN_ERROR_INVENTORY_SPACE = LangText.of("Crate.Open.Error.InventorySpace",
        OUTPUT.enclose(20, 80) + SOUND.enclose(Sound.ENTITY_VILLAGER_NO),
        LIGHT_RED.enclose(BOLD.enclose("Whoops!")),
        LIGHT_GRAY.enclose("Clean up inventory slots to open the crate!")
    );

    public static final LangText CRATE_OPEN_ERROR_COOLDOWN_TEMPORARY = LangText.of("Crate.Open.Error.Cooldown.Temporary",
        OUTPUT.enclose(20, 80) + SOUND.enclose(Sound.ENTITY_VILLAGER_NO),
        LIGHT_RED.enclose(BOLD.enclose("Crate Cooldown!")),
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
        LIGHT_GRAY.enclose("You must hold a key to open crate!")
    );

    public static final LangText CRATE_OPEN_ERROR_NO_REWARDS = LangText.of("Crate.Open.Error.NoRewards",
        OUTPUT.enclose(20, 80) + SOUND.enclose(Sound.ENTITY_VILLAGER_NO),
        RED.enclose(BOLD.enclose("Whoops!")),
        LIGHT_GRAY.enclose("There are no rewards for you! Try later.")
    );

    public static final LangText CRATE_OPEN_ERROR_CANT_AFFORD = LangText.of("Crate.Open.Error.CantAfford",
        OUTPUT.enclose(20, 80) + SOUND.enclose(Sound.ENTITY_VILLAGER_NO),
        LIGHT_RED.enclose(BOLD.enclose("Whoops!")),
        LIGHT_GRAY.enclose("You need " + LIGHT_RED.enclose(GENERIC_AMOUNT) + " to open it!")
    );

    public static final LangText CRATE_OPEN_ERROR_ALREADY = LangText.of("Crate.Open.Error.Already",
        OUTPUT.enclose(20, 80) + SOUND.enclose(Sound.ENTITY_VILLAGER_NO),
        RED.enclose(BOLD.enclose("Whoops!")),
        LIGHT_GRAY.enclose("You're already opening crate!")
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
        LIGHT_GRAY.enclose(" Player " + LIGHT_GREEN.enclose(PLAYER_DISPLAY_NAME) + " just won " + LIGHT_GREEN.enclose(REWARD_NAME) + " from " + LIGHT_GREEN.enclose(CRATE_NAME) + "!"),
        " ",
        LIGHT_GRAY.enclose("Do you wanna too? Purchase keys now: " + CLICK.enclose(LIGHT_GREEN.enclose("[OPEN STORE]"), ClickEvent.Action.OPEN_URL, "https://store.examplecraft.com/")),
        " "
    );

    public static final LangText CRATE_PREVIEW_ERROR_COOLDOWN = LangText.of("Crate.Preview.Error.Cooldown",
        LIGHT_GRAY.enclose("You can preview this crate again in " + LIGHT_RED.enclose(GENERIC_TIME))
    );


    public static final LangText ERROR_INVALID_CRATE = LangText.of("Crate.Error.Invalid",
        LIGHT_RED.enclose("Invalid crate!"));

    public static final LangText ERROR_INVALID_KEY = LangText.of("Crate.Key.Error.Invalid",
        LIGHT_RED.enclose("Invalid key!"));

    public static final LangText ERROR_INVALID_MENU = LangText.of("Menu.Invalid",
        LIGHT_RED.enclose("Menu does not exist!"));


    public static final LangString OTHER_MIDNIGHT = LangString.of("Other.Midnight", "Midnight");
    public static final LangString OTHER_FREE     = LangString.of("Other.Free", "Free");


    public static final LangString EDITOR_ERROR_BAD_NAME = LangString.of("Editor.Error.BadName",
        LIGHT_RED.enclose("Only latin letters & numbers allowed."));

    public static final LangString EDITOR_ERROR_DUPLICATED_CRATE = LangString.of("Crate.Error.Exists",
        LIGHT_RED.enclose("Crate already exists!"));

    public static final LangString EDITOR_ERROR_DUPLICATED_KEY = LangString.of("Crate.Key.Error.Exists",
        LIGHT_RED.enclose("Key already exists!"));

    public static final LangString EDITOR_ERROR_DUPLICATED_REWARD = LangString.of("Editor.Reward.Error.Create.Exist",
        LIGHT_GRAY.enclose("Reward already exists!"));

    public static final LangString EDITOR_TITLE_MAIN             = LangString.of("Editor.Title.Main", BLACK.enclose("ExcellentCrates Editor"));
    public static final LangString EDITOR_TITLE_CRATE_LIST       = LangString.of("Editor.Title.Crates", BLACK.enclose("Crates Editor"));
    public static final LangString EDITOR_TITLE_CRATE_SETTINGS   = LangString.of("Editor.Title.Crate.Settings", BLACK.enclose("Crate Settings"));
    public static final LangString EDITOR_TITLE_CRATE_MILESTONES = LangString.of("Editor.Title.Crate.Milestones", BLACK.enclose("Crate Milestones"));
    public static final LangString EDITOR_TITLE_CRATE_EFFECT     = LangString.of("Editor.Title.Crate.Effect", BLACK.enclose("Crate Effect"));
    public static final LangString EDITOR_TITLE_CRATE_PLACEMENT  = LangString.of("Editor.Title.Crate.Placement", BLACK.enclose("Crate Placement"));
    public static final LangString EDITOR_TITLE_REWARD_LIST      = LangString.of("Editor.Title.Reward.List", BLACK.enclose("Crate Rewards"));
    public static final LangString EDITOR_TITLE_REWARD_SETTINGS  = LangString.of("Editor.Title.Reward.Settings", BLACK.enclose("Reward Settings"));
    public static final LangString EDITOR_TITLE_REWARD_SORT      = LangString.of("Editor.Title.Reward.Sort", BLACK.enclose("Reward Sorting"));
    public static final LangString EDITOR_TITLE_KEY_LIST         = LangString.of("Editor.Title.Keys", BLACK.enclose("Keys Editor"));
    public static final LangString EDITOR_TITLE_KEY_SETTINGS     = LangString.of("Editor.Title.Key.Settings", BLACK.enclose("Key Settings"));

    public static final LangString EDITOR_ENTER_DISPLAY_NAME = LangString.of("Editor.Enter.DisplayName",
        LIGHT_GRAY.enclose("Enter " + LIGHT_GREEN.enclose("[Display Name]")));

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

    public static final LangString EDITOR_ENTER_PARTICLE_NAME = LangString.of("Editor.Crate.Enter.Particle.Name",
        LIGHT_GRAY.enclose("Enter " + LIGHT_GREEN.enclose("[Particle Name]")));

    public static final LangString EDITOR_ENTER_KEY_ID = LangString.of("Editor.Crate.Enter.KeyId",
        LIGHT_GRAY.enclose("Enter " + LIGHT_GREEN.enclose("[Key Identifier]")));

    public static final LangString EDITOR_ENTER_BLOCK_LOCATION = LangString.of("Editor.Crate.Enter.BlockLocation",
        LIGHT_GRAY.enclose("Click a " + LIGHT_GREEN.enclose("[Block] ") + " to assign crate."));

    public static final LangString EDITOR_ENTER_HOLOGRAM_TEMPLATE = LangString.of("Editor.Crate.Enter.HologramTemplate",
        LIGHT_GRAY.enclose("Enter " + LIGHT_GREEN.enclose("[Hologram Template]")));

    public static final LangString EDITOR_ENTER_ANIMATION_CONFIG = LangString.of("Editor.Crate.Enter.AnimationConfig",
        LIGHT_GRAY.enclose("Enter " + LIGHT_GREEN.enclose("[Animation Name]")));

    public static final LangString EDITOR_ENTER_PREVIEW_CONFIG = LangString.of("Editor.Crate.Enter.PreviewConfig",
        LIGHT_GRAY.enclose("Enter " + LIGHT_GREEN.enclose("[Preview Name]")));

    public static final LangString EDITOR_ENTER_OPEN_COST = LangString.of("Editor.Crate.Enter.Open_Cost",
        LIGHT_GRAY.enclose("Enter " + LIGHT_GREEN.enclose("[Currency] [Amount]")));

    public static final LangString EDITOR_ENTER_REWARD_ID = LangString.of("Editor.Reward.Enter.Id",
        LIGHT_GRAY.enclose("Enter " + LIGHT_GREEN.enclose("[Reward Identifier]")));

    public static final LangString EDITOR_ENTER_RARITY = LangString.of("Editor.Reward.Enter.Rarity",
        LIGHT_GRAY.enclose("Enter " + LIGHT_GREEN.enclose("[Rarity]")));

    public static final LangString EDITOR_ENTER_PERMISSION = LangString.of("Editor.Reward.Enter.Permissions",
        LIGHT_GRAY.enclose("Enter " + LIGHT_GREEN.enclose("[Permission Node]")));
}
