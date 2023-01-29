package su.nightexpress.excellentcrates.config;

import su.nexmedia.engine.api.lang.LangKey;
import su.nexmedia.engine.lang.EngineLang;
import su.nightexpress.excellentcrates.Placeholders;


public class Lang extends EngineLang {

    public static final LangKey COMMAND_DROP_USAGE = new LangKey("Command.Drop.Usage", "<crateId> <world> <x> <y> <z>");
    public static final LangKey COMMAND_DROP_DESC  = new LangKey("Command.Drop.Desc", "Drop crate at specified location in the world.");
    public static final LangKey COMMAND_DROP_DONE  = new LangKey("Command.Drop.Done", "Dropped &6" + Placeholders.CRATE_NAME + "&7 at &6" + Placeholders.Location.X + "&7, &6" + Placeholders.Location.Y + "&7, &6" + Placeholders.Location.Z + "&7 in &6" + Placeholders.Location.WORLD + "&7.");

    public static final LangKey COMMAND_FORCE_OPEN_DESC   = new LangKey("Command.ForceOpen.Desc", "Force open a crate for a player.");
    public static final LangKey COMMAND_FORCE_OPEN_USAGE  = new LangKey("Command.ForceOpen.Usage", "<crateId> [player]");
    public static final LangKey COMMAND_FORCE_OPEN_DONE   = new LangKey("Command.ForceOpen.Done", "Force opened &6" + Placeholders.CRATE_NAME + "&7 for &6" + Placeholders.Player.NAME + "&7.");
    public static final LangKey COMMAND_FORCE_OPEN_NOTIFY = new LangKey("Command.ForceOpen.Notify", "You have been forced to open &6" + Placeholders.CRATE_NAME + "&7.");

    public static final LangKey COMMAND_GIVE_USAGE  = new LangKey("Command.Give.Usage", "<player | *> <crateId> [amount]");
    public static final LangKey COMMAND_GIVE_DESC   = new LangKey("Command.Give.Desc", "Gives crate(s) to a player.");
    public static final LangKey COMMAND_GIVE_DONE   = new LangKey("Command.Give.Done", "Given &6x" + Placeholders.GENERIC_AMOUNT + " &7of &6" + Placeholders.CRATE_NAME + " &7crate(s) to &6" + Placeholders.Player.DISPLAY_NAME + "&7.");
    public static final LangKey COMMAND_GIVE_NOTIFY = new LangKey("Command.Give.Notify", "You recieved &6x" + Placeholders.GENERIC_AMOUNT + " &7of &6" + Placeholders.CRATE_NAME + "&7!");

    public static final LangKey COMMAND_KEY_DESC         = new LangKey("Command.Key.Desc", "Manage or view player's crate keys.");
    public static final LangKey COMMAND_KEY_ERROR_PLAYER = new LangKey("Command.Key.Error.Player", "&cCould not proccess operation for offline/invalid player &e" + Placeholders.Player.NAME + "&c!");

    public static final LangKey COMMAND_KEY_GIVE_USAGE  = new LangKey("Command.Key.Give.Usage", "<player | *> <keyId> <amount>");
    public static final LangKey COMMAND_KEY_GIVE_DESC   = new LangKey("Command.Key.Give.Desc", "Give crate key(s) to a player.");
    public static final LangKey COMMAND_KEY_GIVE_DONE   = new LangKey("Command.Key.Give.Done", "Given &ax" + Placeholders.GENERIC_AMOUNT + " &7of &a" + Placeholders.KEY_NAME + " &7key(s) to &a" + Placeholders.Player.NAME + "&7.");
    public static final LangKey COMMAND_KEY_GIVE_NOTIFY = new LangKey("Command.Key.Give.Notify", "You recieved &ax" + Placeholders.GENERIC_AMOUNT + " &7of &a" + Placeholders.KEY_NAME + "&7!");

    public static final LangKey COMMAND_KEY_TAKE_USAGE  = new LangKey("Command.Key.Take.Usage", "<player | *> <keyId> <amount>");
    public static final LangKey COMMAND_KEY_TAKE_DESC   = new LangKey("Command.Key.Take.Desc", "Take crate key(s) from a player.");
    public static final LangKey COMMAND_KEY_TAKE_DONE   = new LangKey("Command.Key.Take.Done", "Taken &cx" + Placeholders.GENERIC_AMOUNT + " &c" + Placeholders.KEY_NAME + " &7key(s) from &c" + Placeholders.Player.NAME);
    public static final LangKey COMMAND_KEY_TAKE_NOTIFY = new LangKey("Command.Key.Take.Notify", "You lost &cx" + Placeholders.GENERIC_AMOUNT + " &c" + Placeholders.KEY_NAME + "&7!");
    //public static final LangKey Command_Key_Take_Error  = new LangKey(this, "&cCould not take keys: &ePlayer does noet exist or do not have such amount of keys.");

    public static final LangKey COMMAND_KEY_SET_USAGE  = new LangKey("Command.Key.Set.Usage", "<player | *> <keyId> <amount>");
    public static final LangKey COMMAND_KEY_SET_DESC   = new LangKey("Command.Key.Set.Desc", "Set crate key(s) amount for a player.");
    public static final LangKey COMMAND_KEY_SET_DONE   = new LangKey("Command.Key.Set.Done", "Set &ex" + Placeholders.GENERIC_AMOUNT + " &7of &e" + Placeholders.KEY_NAME + " &7key(s) for &e" + Placeholders.Player.NAME + "&7.");
    public static final LangKey COMMAND_KEY_SET_NOTIFY = new LangKey("Command.Key.Set.Notify", "Your &e" + Placeholders.KEY_NAME + "&7 amount has been changed to &ex" + Placeholders.GENERIC_AMOUNT + "&7!");

    public static final LangKey COMMAND_KEY_SHOW_DESC        = new LangKey("Command.Key.Show.Desc", "Show amount of your or other player keys.");
    public static final LangKey COMMAND_KEY_SHOW_USAGE       = new LangKey("Command.Key.Show.Usage", "[player]");
    public static final LangKey COMMAND_KEY_SHOW_FORMAT_LIST = new LangKey("Command.Key.Show.Format.List", """
        <!prefix:"false"!>
        &6&m              &6&l[ &a%player_name% &e&lCrate Keys &6&l]&6&m              &6
        &7
        &6▸ &e%key_name%: &6%amount%
        """);

    public static final LangKey COMMAND_PREVIEW_DESC        = new LangKey("Command.Preview.Desc", "Open crate preview.");
    public static final LangKey COMMAND_PREVIEW_USAGE       = new LangKey("Command.Preview.Usage", "<crateId> [player]");
    public static final LangKey COMMAND_PREVIEW_DONE_OTHERS = new LangKey("Command.Preview.Done.Others", "Opened &6" + Placeholders.CRATE_NAME + "&7 preview for &6" + Placeholders.Player.DISPLAY_NAME + "&7.");

    public static final LangKey COMMAND_RESET_LIMIT_DESC        = new LangKey("Command.ResetLimit.Desc", "Reset reward win limit for specified crate and reward.");
    public static final LangKey COMMAND_RESET_LIMIT_USAGE       = new LangKey("Command.ResetLimit.Usage", "<player> <crateId> [rewardId]");
    public static final LangKey COMMAND_RESET_LIMIT_DONE_CRATE  = new LangKey("Command.ResetLimit.Done.Crate", "Reset &6" + Placeholders.Player.NAME + " &7win limit for all rewards of &6" + Placeholders.CRATE_NAME + "&7.");
    public static final LangKey COMMAND_RESET_LIMIT_DONE_REWARD = new LangKey("Command.ResetLimit.Done.Reward", "Reset &6" + Placeholders.Player.NAME + " &7win limit for &6" + Placeholders.REWARD_NAME + " &7reward of &6" + Placeholders.CRATE_NAME + "&7.");

    public static final LangKey COMMAND_RESET_COOLDOWN_DESC  = new LangKey("Command.ResetCooldown.Desc", "Reset player cooldown for specified crate.");
    public static final LangKey COMMAND_RESET_COOLDOWN_USAGE = new LangKey("Command.ResetCooldown.Usage", "<player> <crateId>");
    public static final LangKey COMMAND_RESET_COOLDOWN_DONE  = new LangKey("Command.ResetCooldown.Done", "Reset &6" + Placeholders.Player.NAME + " &7cooldown for &6" + Placeholders.CRATE_NAME + "&7.");

    public static final LangKey COMMAND_MENU_USAGE       = new LangKey("Command.Menu.Usage", "[menuId]");
    public static final LangKey COMMAND_MENU_DESC        = new LangKey("Command.Menu.Desc", "Open crate menu.");
    public static final LangKey COMMAND_MENU_DONE_OTHERS = new LangKey("Command.Menu.Done.Others", "Opened &6" + Placeholders.MENU_ID + "&7 crate menu for &6" + Placeholders.Player.DISPLAY_NAME + "&7.");

    public static final LangKey CRATE_ERROR_INVALID                 = new LangKey("Crate.Error.Invalid", "&cInvalid crate!");
    public static final LangKey CRATE_OPEN_ERROR_INVENTORY_SPACE    = new LangKey("Crate.Open.Error.InventorySpace", "&cPlease clean up your inventory to open the crate!");
    public static final LangKey CRATE_OPEN_ERROR_COOLDOWN_TEMPORARY = new LangKey("Crate.Open.Error.Cooldown.Temporary", "&cYou have to wait &6" + Placeholders.GENERIC_TIME + " &7before you can open &6" + Placeholders.CRATE_NAME + "&7 again!");
    public static final LangKey CRATE_OPEN_ERROR_COOLDOWN_ONE_TIMED = new LangKey("Crate.Open.Error.Cooldown.OneTimed", "&cYou already have opened this crate, you can not open it more!");
    public static final LangKey CRATE_OPEN_ERROR_NO_KEY             = new LangKey("Crate.Open.Error.NoKey", "&cYou don't have a key for this crate!");
    public static final LangKey CRATE_OPEN_ERROR_NO_REWARDS         = new LangKey("Crate.Open.Error.NoRewards", "&cThis crate does not contains any rewards for you!");
    public static final LangKey CRATE_OPEN_ERROR_COST_MONEY         = new LangKey("Crate.Open.Error.Cost.Money", "&cYou don't have enough money to open this crate!");
    public static final LangKey CRATE_OPEN_ERROR_COST_EXP           = new LangKey("Crate.Open.Error.Cost.Exp", "&cYou don't have enough exp levels to open this crate!");
    public static final LangKey CRATE_OPEN_REWARD_INFO              = new LangKey("Crate.Open.Reward.Info", "You got the &6" + Placeholders.REWARD_NAME + " &7reward from the &6" + Placeholders.CRATE_NAME + "&7!");
    public static final LangKey CRATE_OPEN_REWARD_BROADCAST         = new LangKey("Crate.Open.Reward.Broadcast", "&7Player &a" + Placeholders.Player.DISPLAY_NAME + " &7just got the &6" + Placeholders.REWARD_NAME + " &7reward from the &6" + Placeholders.CRATE_NAME + "&7!");
    public static final LangKey CRATE_KEY_ERROR_INVALID             = new LangKey("Crate.Key.Error.Invalid", "&cInvalid key!");
    public static final LangKey CRATE_PLACEHOLDER_COOLDOWN_BLANK    = new LangKey("Crate.Placeholder.Cooldown.Blank", "Ready to open!");

    public static final LangKey MENU_INVALID = new LangKey("Menu.Invalid", "&cMenu does not exist!");

    public static final LangKey EDITOR_CRATE_ENTER_ID                    = new LangKey("Editor.Crate.Enter.Id", "&7Enter &aunique &7crate &aidentifier&7...");
    public static final LangKey EDITOR_CRATE_ENTER_DISPLAY_NAME          = new LangKey("Editor.Crate.Enter.DisplayName", "&7Enter crate &adisplay name&7...");
    public static final LangKey EDITOR_CRATE_ENTER_PARTICLE_NAME         = new LangKey("Editor.Crate.Enter.Particle.Name", "&7Enter &aparticle &7name...");
    public static final LangKey EDITOR_CRATE_ENTER_PARTICLE_DATA         = new LangKey("Editor.Crate.Enter.Particle.Data", "&7Enter &aparticle &7data...");
    public static final LangKey EDITOR_CRATE_ENTER_KEY_ID                = new LangKey("Editor.Crate.Enter.KeyId", "&7Enter &akey &7identifier...");
    public static final LangKey EDITOR_CRATE_ENTER_BLOCK_LOCATION        = new LangKey("Editor.Crate.Enter.Block.Location", "&7Click a &ablock &7to assign crate...");
    public static final LangKey EDITOR_CRATE_ENTER_BLOCK_HOLOGRAM_TEXT   = new LangKey("Editor.Crate.Enter.Block.Hologram.Text", "&7Enter &atext &7line...");
    public static final LangKey EDITOR_CRATE_ENTER_BLOCK_HOLOGRAM_OFFSET = new LangKey("Editor.Crate.Enter.Block.Hologram.Offset", "&7Enter &aoffset &7value...");
    public static final LangKey EDITOR_CRATE_ENTER_COOLDOWN              = new LangKey("Editor.Crate.Enter.Cooldown", "&7Enter &acooldown &7in seconds...");
    public static final LangKey EDITOR_CRATE_ENTER_CITIZENS              = new LangKey("Editor.Crate.Enter.Citizens", "&7Enter &aCitizens NPC &7ID...");
    public static final LangKey EDITOR_CRATE_ENTER_ANIMATION_CONFIG      = new LangKey("Editor.Crate.Enter.AnimationConfig", "&7Enter &aanimation config &7name...");
    public static final LangKey EDITOR_CRATE_ENTER_PREVIEW_CONFIG        = new LangKey("Editor.Crate.Enter.PreviewConfig", "&7Enter &apreview config &7name...");
    public static final LangKey EDITOR_CRATE_ENTER_OPEN_COST_MONEY       = new LangKey("Editor.Crate.Enter.OpenCost.Money", "&7Enter &amoney &7cost...");
    public static final LangKey EDITOR_CRATE_ENTER_OPEN_COST_EXP         = new LangKey("Editor.Crate.Enter.OpenCost.Exp", "&7Enter &aexp levels &7cost...");
    public static final LangKey EDITOR_CRATE_ERROR_CREATE_EXISTS         = new LangKey("Editor.Crate.Error.Create.Exists", "&cCrate with such id is already exists!");

    public static final LangKey EDITOR_REWARD_ENTER_ID                 = new LangKey("Editor.Reward.Enter.Id", "&7Enter &aunique &7reward &aidentifier&7...");
    public static final LangKey EDITOR_REWARD_ENTER_DISPLAY_NAME       = new LangKey("Editor.Reward.Enter.DisplayName", "&7Enter reward &adisplay name&7...");
    public static final LangKey EDITOR_REWARD_ENTER_CHANCE             = new LangKey("Editor.Reward.Enter.Chance", "&7Enter win &achance&7...");
    public static final LangKey EDITOR_REWARD_ENTER_COMMAND            = new LangKey("Editor.Reward.Enter.Command", "&7Enter a &acommand&7...");
    public static final LangKey EDITOR_REWARD_ENTER_PERMISSION         = new LangKey("Editor.Reward.Enter.Permissions", "&7Enter a &apermission&7...");
    public static final LangKey EDITOR_REWARD_ENTER_WIN_LIMIT_AMOUNT   = new LangKey("Editor.Reward.Enter.WinLimit.Amount", "&7Enter win limit &aamount&7...");
    public static final LangKey EDITOR_REWARD_ENTER_WIN_LIMIT_COOLDOWN = new LangKey("Editor.Reward.Enter.WinLimit.Cooldown", "&7Enter win limit &acooldown&7...");
    public static final LangKey EDITOR_REWARD_ERROR_CREATE_EXIST       = new LangKey("Editor.Reward.Error.Create.Exist", "&cReward with such id is already exists!");

    public static final LangKey EDITOR_KEY_ERROR_CREATE_EXIST = new LangKey("Editor.Key.Error.Create.Exist", "Key with such id is already exists!");
}
