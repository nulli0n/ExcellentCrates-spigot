package su.nightexpress.excellentcrates.config;

import org.bukkit.Sound;
import su.nexmedia.engine.api.lang.LangColors;
import su.nexmedia.engine.api.lang.LangKey;
import su.nexmedia.engine.lang.EngineLang;
import su.nightexpress.excellentcrates.Placeholders;


public class Lang extends EngineLang implements LangColors {

    public static final LangKey COMMAND_EDITOR_DESC  = LangKey.of("Command.Editor.Desc", "Open crates & keys editor.");

    public static final LangKey COMMAND_DROP_USAGE = LangKey.of("Command.Drop.Usage", "<crateId> <world> <x> <y> <z>");
    public static final LangKey COMMAND_DROP_DESC  = LangKey.of("Command.Drop.Desc", "Drop crate at specified location in the world.");
    public static final LangKey COMMAND_DROP_DONE  = LangKey.of("Command.Drop.Done", GRAY + "Dropped " + YELLOW + Placeholders.CRATE_NAME + GRAY + " at " + YELLOW + Placeholders.LOCATION_X + ", " + Placeholders.LOCATION_Y + ", " + Placeholders.LOCATION_Z + GRAY + " in " + YELLOW + Placeholders.LOCATION_WORLD + GRAY + ".");

    public static final LangKey COMMAND_OPEN_DESC   = LangKey.of("Command.Open.Desc", "Makes [player] to [force] open a crate.");
    public static final LangKey COMMAND_OPEN_USAGE  = LangKey.of("Command.Open.Usage", "<crate> [player] [-f] [-s]");
    public static final LangKey COMMAND_OPEN_DONE   = LangKey.of("Command.Open.Done", GRAY + "Forced " + YELLOW + Placeholders.PLAYER_NAME + GRAY + " to open " + YELLOW + Placeholders.CRATE_NAME + GRAY + ".");
    public static final LangKey COMMAND_OPEN_NOTIFY = LangKey.of("Command.Open.Notify", GRAY + "You have been forced to open " + YELLOW + Placeholders.CRATE_NAME + GRAY + ".");

    public static final LangKey COMMAND_GIVE_USAGE  = LangKey.of("Command.Give.Usage", "<player> <crate> [amount] [-s]");
    public static final LangKey COMMAND_GIVE_DESC   = LangKey.of("Command.Give.Desc", "Gives crate to a player.");
    public static final LangKey COMMAND_GIVE_DONE   = LangKey.of("Command.Give.Done", GRAY + "Given " + YELLOW + "x" + Placeholders.GENERIC_AMOUNT + GRAY +  " of " + YELLOW + Placeholders.CRATE_NAME + GRAY + " crate(s) to " + YELLOW + Placeholders.PLAYER_NAME + GRAY + ".");
    public static final LangKey COMMAND_GIVE_NOTIFY = LangKey.of("Command.Give.Notify", GRAY + "You recieved " + YELLOW + "x" + Placeholders.GENERIC_AMOUNT + GRAY + " of " + YELLOW + Placeholders.CRATE_NAME + GRAY + ".");

    public static final LangKey COMMAND_KEY_DESC         = LangKey.of("Command.Key.Desc", "Manage player's keys.");
    public static final LangKey COMMAND_KEY_USAGE         = LangKey.of("Command.Key.Usage", "[help]");

    public static final LangKey COMMAND_KEY_GIVE_USAGE  = LangKey.of("Command.Key.Give.Usage", "<player> <key> <amount> [-s]");
    public static final LangKey COMMAND_KEY_GIVE_DESC   = LangKey.of("Command.Key.Give.Desc", "Give key to a player.");
    public static final LangKey COMMAND_KEY_GIVE_DONE   = LangKey.of("Command.Key.Give.Done", GRAY + "Given " + YELLOW + "x" + Placeholders.GENERIC_AMOUNT + GRAY + " of " + YELLOW + Placeholders.KEY_NAME + GRAY + " key(s) to " + YELLOW + Placeholders.PLAYER_NAME + GRAY + ".");
    public static final LangKey COMMAND_KEY_GIVE_NOTIFY = LangKey.of("Command.Key.Give.Notify", GRAY + "You recieved " + YELLOW + "x" + Placeholders.GENERIC_AMOUNT + GRAY + " of " + YELLOW + Placeholders.KEY_NAME + GRAY + "!");

    public static final LangKey COMMAND_KEY_GIVE_ALL_USAGE  = LangKey.of("Command.Key.GiveAll.Usage", "<player> <key> <amount> [-s]");
    public static final LangKey COMMAND_KEY_GIVE_ALL_DESC   = LangKey.of("Command.Key.GiveAll.Desc", "Give key to all online players.");
    public static final LangKey COMMAND_KEY_GIVE_ALL_DONE   = LangKey.of("Command.Key.GiveAll.Done", GRAY + "Given " + YELLOW + "x" + Placeholders.GENERIC_AMOUNT + GRAY + " of " + YELLOW + Placeholders.KEY_NAME + GRAY + " key(s) to " + YELLOW + "All Players" + GRAY + ".");

    public static final LangKey COMMAND_KEY_TAKE_USAGE  = LangKey.of("Command.Key.Take.Usage", "<player> <key> <amount> [-s]");
    public static final LangKey COMMAND_KEY_TAKE_DESC   = LangKey.of("Command.Key.Take.Desc", "Take key from a player.");
    public static final LangKey COMMAND_KEY_TAKE_DONE   = LangKey.of("Command.Key.Take.Done", GRAY + "Taken " + YELLOW + "x" + Placeholders.GENERIC_AMOUNT + GRAY + " of " + YELLOW + Placeholders.KEY_NAME + GRAY + " key(s) from " + YELLOW + Placeholders.PLAYER_NAME + GRAY + ".");
    public static final LangKey COMMAND_KEY_TAKE_NOTIFY = LangKey.of("Command.Key.Take.Notify", GRAY + "You lost " + RED + "x" + Placeholders.GENERIC_AMOUNT + " " + Placeholders.KEY_NAME + GRAY + ".");

    public static final LangKey COMMAND_KEY_SET_USAGE  = LangKey.of("Command.Key.Set.Usage", "<player> <key> <amount> [-s]");
    public static final LangKey COMMAND_KEY_SET_DESC   = LangKey.of("Command.Key.Set.Desc", "Set keys amount for a player.");
    public static final LangKey COMMAND_KEY_SET_DONE   = LangKey.of("Command.Key.Set.Done", GRAY + "Set " + YELLOW + "x" + Placeholders.GENERIC_AMOUNT + GRAY + " of " + YELLOW + Placeholders.KEY_NAME + GRAY + " key(s) for " + YELLOW + Placeholders.PLAYER_NAME + GRAY + ".");
    public static final LangKey COMMAND_KEY_SET_NOTIFY = LangKey.of("Command.Key.Set.Notify", GRAY + "Your " + YELLOW + Placeholders.KEY_NAME + GRAY + "'s amount has been changed to " + YELLOW + "x" + Placeholders.GENERIC_AMOUNT + GRAY + ".");

    public static final LangKey COMMAND_KEY_INSPECT_DESC     = LangKey.of("Command.Key.Show.Desc", "Inspect [player's] virtual keys.");
    public static final LangKey COMMAND_KEY_INSPECT_USAGE = LangKey.of("Command.Key.Show.Usage", "[player]");
    public static final LangKey COMMAND_KEY_INSPECT_LIST  = LangKey.of("Command.Key.Show.Format.List",
        "<! prefix:\"false\" !>" +
        "\n" + GRAY +
        "\n" + YELLOW + "&l" + Placeholders.PLAYER_NAME + "'s Virtual Keys: " +
        "\n" + YELLOW + "▪ " + GRAY + Placeholders.KEY_NAME + ": " + YELLOW + "x" + Placeholders.GENERIC_AMOUNT +
        "\n" + GRAY);

    public static final LangKey COMMAND_PREVIEW_DESC        = LangKey.of("Command.Preview.Desc", "Open crate preview.");
    public static final LangKey COMMAND_PREVIEW_USAGE       = LangKey.of("Command.Preview.Usage", "<crate> [player]");
    public static final LangKey COMMAND_PREVIEW_DONE_OTHERS = LangKey.of("Command.Preview.Done.Others", GRAY + "Opened " + YELLOW + Placeholders.CRATE_NAME + GRAY + " preview for " + YELLOW + Placeholders.PLAYER_DISPLAY_NAME + GRAY + ".");

    public static final LangKey COMMAND_RESET_LIMIT_DESC        = LangKey.of("Command.ResetLimit.Desc", "Reset reward win limit for specified crate and reward.");
    public static final LangKey COMMAND_RESET_LIMIT_USAGE       = LangKey.of("Command.ResetLimit.Usage", "<player> <crate> [reward]");
    public static final LangKey COMMAND_RESET_LIMIT_DONE_CRATE  = LangKey.of("Command.ResetLimit.Done.Crate", GRAY + "Reset " + YELLOW + Placeholders.PLAYER_NAME + GRAY + " win limit for all rewards of " + YELLOW + Placeholders.CRATE_NAME + GRAY + ".");
    public static final LangKey COMMAND_RESET_LIMIT_DONE_REWARD = LangKey.of("Command.ResetLimit.Done.Reward", GRAY + "Reset " + YELLOW + Placeholders.PLAYER_NAME + GRAY + " win limit for " + YELLOW + Placeholders.REWARD_NAME + GRAY + " reward of " + YELLOW + Placeholders.CRATE_NAME + GRAY + ".");

    public static final LangKey COMMAND_RESET_COOLDOWN_DESC  = LangKey.of("Command.ResetCooldown.Desc", "Reset player's crate open cooldown.");
    public static final LangKey COMMAND_RESET_COOLDOWN_USAGE = LangKey.of("Command.ResetCooldown.Usage", "<player> <crate>");
    public static final LangKey COMMAND_RESET_COOLDOWN_DONE  = LangKey.of("Command.ResetCooldown.Done", GRAY + "Reset " + YELLOW + Placeholders.PLAYER_NAME + GRAY + "'s open cooldown for " + YELLOW + Placeholders.CRATE_NAME + GRAY + ".");

    public static final LangKey COMMAND_MENU_USAGE       = LangKey.of("Command.Menu.Usage", "[menu]");
    public static final LangKey COMMAND_MENU_DESC        = LangKey.of("Command.Menu.Desc", "Open crate menu.");
    public static final LangKey COMMAND_MENU_DONE_OTHERS = LangKey.of("Command.Menu.Done.Others", GRAY + "Opened " + YELLOW + Placeholders.MENU_ID + GRAY + " crate menu for " + YELLOW + Placeholders.PLAYER_DISPLAY_NAME + GRAY + ".");

    public static final LangKey CRATE_ERROR_INVALID = LangKey.of("Crate.Error.Invalid", RED + "Invalid crate!");
    public static final LangKey CRATE_ERROR_EXISTS  = LangKey.of("Crate.Error.Exists", RED + "Crate with such id is already exists!");

    public static final LangKey CRATE_OPEN_ERROR_INVENTORY_SPACE = LangKey.of("Crate.Open.Error.InventorySpace",
        "<! type:\"titles:20:80:20\" sound:\"" + Sound.ENTITY_VILLAGER_NO.name() + "\" !>" +
            "\n" + RED + "&lWhoops!" +
            "\n" + GRAY + "Clean up inventory slots to open the crate!");

    public static final LangKey CRATE_OPEN_ERROR_COOLDOWN_TEMPORARY = LangKey.of("Crate.Open.Error.Cooldown.Temporary",
        "<! type:\"titles:20:80:20\" sound:\"" + Sound.ENTITY_VILLAGER_NO.name() + "\" !>" +
            "\n" + RED + "&lCrate on Cooldown!" +
            "\n" + GRAY + "You can open in " + RED + Placeholders.GENERIC_TIME);

    public static final LangKey CRATE_OPEN_ERROR_COOLDOWN_ONE_TIMED = LangKey.of("Crate.Open.Error.Cooldown.OneTimed",
        "<! type:\"titles:20:80:20\" sound:\"" + Sound.ENTITY_VILLAGER_NO.name() + "\" !>" +
            "\n" + RED + "&lWhoops!" +
            "\n" + GRAY + "You already have opened this one-timed crate!");

    public static final LangKey CRATE_OPEN_ERROR_NO_KEY = LangKey.of("Crate.Open.Error.NoKey",
        "<! type:\"titles:20:80:20\" sound:\"" + Sound.ENTITY_VILLAGER_NO.name() + "\" !>" +
            "\n" + RED + "&lWhoops!" +
            "\n" + GRAY + "You don't have a key for this crate!");

    public static final LangKey CRATE_OPEN_ERROR_NO_HOLD_KEY = LangKey.of("Crate.Open.Error.NoHoldKey",
        "<! type:\"titles:20:80:20\" sound:\"" + Sound.ENTITY_VILLAGER_NO.name() + "\" !>" +
            "\n" + RED + "&lWhoops!" +
            "\n" + GRAY + "You must hold a key to open crates!");

    public static final LangKey CRATE_OPEN_ERROR_NO_REWARDS = LangKey.of("Crate.Open.Error.NoRewards",
        "<! type:\"titles:20:80:20\" sound:\"" + Sound.ENTITY_VILLAGER_NO.name() + "\" !>" +
            "\n" + RED + "&lWhoops!" +
            "\n" + GRAY + "There are no rewards for you! Try later.");

    public static final LangKey CRATE_OPEN_ERROR_COST_MONEY = LangKey.of("Crate.Open.Error.Cost.Money",
        "<! type:\"titles:20:80:20\" sound:\"" + Sound.ENTITY_VILLAGER_NO.name() + "\" !>" +
            "\n" + RED + "&lWhoops!" +
            "\n" + GRAY + "You need $" + RED + Placeholders.CRATE_OPENING_COST_MONEY + GRAY + " to open it!");

    public static final LangKey CRATE_OPEN_ERROR_COST_EXP = LangKey.of("Crate.Open.Error.Cost.Exp",
        "<! type:\"titles:20:80:20\" sound:\"" + Sound.ENTITY_VILLAGER_NO.name() + "\" !>" +
            "\n" + RED + "&lWhoops!" +
            "\n" + GRAY + "You need " + RED + Placeholders.CRATE_OPENING_COST_EXP + " Levels " + GRAY + " to open it!");

    public static final LangKey CRATE_OPEN_REWARD_INFO = LangKey.of("Crate.Open.Reward.Info",
        "<! prefix:\"false\" !>" +
            GRAY + "You won " + GREEN + Placeholders.REWARD_NAME + GRAY + " from the " + GREEN + Placeholders.CRATE_NAME + GRAY + "!");

    public static final LangKey CRATE_OPEN_REWARD_BROADCAST         = LangKey.of("Crate.Open.Reward.Broadcast",
        "<! prefix:\"false\" sound:\"" + Sound.BLOCK_NOTE_BLOCK_BELL.name() + "\" !>" +
            "\n" + GRAY +
            "\n" + GREEN + "&lWOW!" + GRAY + " Player " + GREEN + Placeholders.PLAYER_DISPLAY_NAME + GRAY + " just won " + GREEN + Placeholders.REWARD_NAME + GRAY + " from " + GREEN + Placeholders.CRATE_NAME + GRAY + "!" +
            "\n" + GRAY +
            "\n" + GRAY + "Do you wanna too? Purchase keys now: <? open_url:\"http://samplesmp.com/store\" ?>" + GREEN + "[Open Store]</>" +
            "\n" + GRAY);

    public static final LangKey CRATE_KEY_ERROR_INVALID             = LangKey.of("Crate.Key.Error.Invalid", RED + "Invalid key!");
    public static final LangKey CRATE_KEY_ERROR_EXISTS              = LangKey.of("Crate.Key.Error.Exists", RED + "Key with such id is already exists!");

    public static final LangKey MENU_INVALID = LangKey.of("Menu.Invalid", RED + "Menu does not exist!");

    public static final LangKey EDITOR_ENTER_DISPLAY_NAME  = LangKey.of("Editor.Enter.DisplayName", GRAY + "Enter " + GREEN + "[Display Name]");

    public static final LangKey EDITOR_CRATE_ENTER_ID                    = LangKey.of("Editor.Crate.Enter.Id", GRAY + "Enter " + GREEN + "[Crate Identifier]");
    public static final LangKey EDITOR_CRATE_ENTER_PARTICLE_NAME         = LangKey.of("Editor.Crate.Enter.Particle.Name", GRAY + "Enter " + GREEN + "[Particle Name]");
    public static final LangKey EDITOR_CRATE_ENTER_PARTICLE_DATA         = LangKey.of("Editor.Crate.Enter.Particle.Data", GRAY + "Enter " + GREEN + "[Particle Options]");
    public static final LangKey EDITOR_CRATE_ENTER_KEY_ID                = LangKey.of("Editor.Crate.Enter.KeyId", GRAY + "Enter " + GREEN + "[Key Identifier]");
    public static final LangKey EDITOR_CRATE_ENTER_BLOCK_LOCATION        = LangKey.of("Editor.Crate.Enter.Block.Location", GRAY + "Click a " + GREEN + "[Block] " + GRAY + " to make it crate.");
    public static final LangKey EDITOR_CRATE_ENTER_BLOCK_HOLOGRAM_TEXT   = LangKey.of("Editor.Crate.Enter.Block.Hologram.Text", GRAY + "Enter " + GREEN + "[Text]");
    public static final LangKey EDITOR_CRATE_ENTER_BLOCK_HOLOGRAM_OFFSET = LangKey.of("Editor.Crate.Enter.Block.Hologram.Offset", GRAY + "Enter " + GREEN + "[Offset Value]");
    public static final LangKey EDITOR_CRATE_ENTER_COOLDOWN              = LangKey.of("Editor.Crate.Enter.Cooldown", GRAY + "Enter " + GREEN + "[Seconds Amount]");
    public static final LangKey EDITOR_CRATE_ENTER_ANIMATION_CONFIG      = LangKey.of("Editor.Crate.Enter.AnimationConfig", GRAY + "Enter " + GREEN + "[Animation Name]");
    public static final LangKey EDITOR_CRATE_ENTER_PREVIEW_CONFIG        = LangKey.of("Editor.Crate.Enter.PreviewConfig", GRAY + "Enter " + GREEN + "[Preview Name]");
    public static final LangKey EDITOR_CRATE_ENTER_OPEN_COST_MONEY       = LangKey.of("Editor.Crate.Enter.OpenCost.Money", GRAY + "Enter " + GREEN + "[Money Amount]");
    public static final LangKey EDITOR_CRATE_ENTER_OPEN_COST_EXP         = LangKey.of("Editor.Crate.Enter.OpenCost.Exp", GRAY + "Enter " + GREEN + "[Levels Amount]");

    public static final LangKey EDITOR_REWARD_ENTER_ID                 = LangKey.of("Editor.Reward.Enter.Id", GRAY + "Enter " + GREEN + "[Reward Identifier]");
    public static final LangKey EDITOR_REWARD_ENTER_CHANCE             = LangKey.of("Editor.Reward.Enter.Chance", GRAY + "Enter " + GREEN + "[Chance]");
    public static final LangKey EDITOR_REWARD_ENTER_RARITY             = LangKey.of("Editor.Reward.Enter.Rarity", GRAY + "Enter " + GREEN + "[Rarity]");
    public static final LangKey EDITOR_REWARD_ENTER_COMMAND            = LangKey.of("Editor.Reward.Enter.Command", GRAY + "Enter " + GREEN + "[Command]");
    public static final LangKey EDITOR_REWARD_ENTER_PERMISSION         = LangKey.of("Editor.Reward.Enter.Permissions", GRAY + "Enter " + GREEN + "[Permission Node]");
    public static final LangKey EDITOR_REWARD_ENTER_WIN_LIMIT_AMOUNT   = LangKey.of("Editor.Reward.Enter.WinLimit.Amount", GRAY + "Enter " + GREEN + "[Limit Amount]");
    public static final LangKey EDITOR_REWARD_ENTER_WIN_LIMIT_COOLDOWN = LangKey.of("Editor.Reward.Enter.WinLimit.Cooldown", GRAY + "Enter " + GREEN + "[Seconds Amount]");
    public static final LangKey EDITOR_REWARD_ERROR_CREATE_EXIST       = LangKey.of("Editor.Reward.Error.Create.Exist", RED + "Reward with such id is already exists!");

}
