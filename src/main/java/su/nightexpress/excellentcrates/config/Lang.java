package su.nightexpress.excellentcrates.config;

import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.config.LangMessage;
import su.nexmedia.engine.core.config.CoreLang;
import su.nightexpress.excellentcrates.ExcellentCrates;


public class Lang extends CoreLang {

    public Lang(@NotNull ExcellentCrates plugin) {
        super(plugin);
    }

    public LangMessage Command_Drop_Usage = new LangMessage(this, "<crateId> <world> <x> <y> <z>");
    public LangMessage Command_Drop_Desc  = new LangMessage(this, "Drop crate at specified location in the world.");
    public LangMessage Command_Drop_Done  = new LangMessage(this, "Dropped &6%crate_name%&7 at &6%x%&7, &6%y%&7, &6%z%&7 in &6%world%&7.");

    public LangMessage Command_ForceOpen_Desc   = new LangMessage(this, "Force open a crate for a player.");
    public LangMessage Command_ForceOpen_Usage  = new LangMessage(this, "<crateId> [player]");
    public LangMessage Command_ForceOpen_Done   = new LangMessage(this, "Force opened &6%crate_name%&7 for &6%player%&7.");
    public LangMessage Command_ForceOpen_Notify = new LangMessage(this, "You have been forced to open &6%crate_name%&7.");

    public LangMessage Command_Give_Usage  = new LangMessage(this, "<player | *> <crateId> [amount]");
    public LangMessage Command_Give_Desc   = new LangMessage(this, "Gives crate(s) to a player.");
    public LangMessage Command_Give_Done   = new LangMessage(this, "Given &6x%amount% &7of &6%crate_name% &7crate(s) to &6%player%&7.");
    public LangMessage Command_Give_Notify = new LangMessage(this, "You recieved &6x%amount% &7of &6%crate_name%&7!");

    public LangMessage Command_Key_Desc         = new LangMessage(this, "Manage or view player's crate keys.");
    public LangMessage Command_Key_Error_Player = new LangMessage(this, "&cCould not proccess operation for offline/invalid player &e%player%&c!");

    public LangMessage Command_Key_Give_Usage = new LangMessage(this, "<player | *> <keyId> <amount>");
    public LangMessage Command_Key_Give_Desc  = new LangMessage(this, "Give crate key(s) to a player.");
    public LangMessage Command_Key_Give_Done   = new LangMessage(this, "Given &ax%amount% &7of &a%key_name% &7key(s) to &a%player%&7.");
    public LangMessage Command_Key_Give_Notify = new LangMessage(this, "You recieved &ax%amount% &7of &a%key_name%&7!");

    public LangMessage Command_Key_Take_Usage = new LangMessage(this, "<player | *> <keyId> <amount>");
    public LangMessage Command_Key_Take_Desc  = new LangMessage(this, "Take crate key(s) from a player.");
    public LangMessage Command_Key_Take_Done   = new LangMessage(this, "Taken &cx%amount% &c%key_name% &7key(s) from &c%player%");
    public LangMessage Command_Key_Take_Notify = new LangMessage(this, "You lost &cx%amount% &c%key_name%&7!");
    //public LangMessage Command_Key_Take_Error  = new LangMessage(this, "&cCould not take keys: &ePlayer does noet exist or do not have such amount of keys.");

    public LangMessage Command_Key_Set_Usage = new LangMessage(this, "<player | *> <keyId> <amount>");
    public LangMessage Command_Key_Set_Desc  = new LangMessage(this, "Set crate key(s) amount for a player.");
    public LangMessage Command_Key_Set_Done   = new LangMessage(this, "Set &ex%amount% &7of &e%key_name% &7key(s) for &e%player%&7.");
    public LangMessage Command_Key_Set_Notify = new LangMessage(this, "Your &e%key_name%&7 amount has been changed to &ex%amount%&7!");

    public LangMessage Command_Key_Show_Desc        = new LangMessage(this, "Show amount of your or other player keys.");
    public LangMessage Command_Key_Show_Usage       = new LangMessage(this, "[player]");
    public LangMessage Command_Key_Show_Format_List = new LangMessage(this, """
        {message: ~prefix: false;}
        &6&m              &6&l[ &a%player% &e&lCrate Keys &6&l]&6&m              &6
        &7
        &6▸ &e%key_name%: &6%amount%
        """);

    public LangMessage Command_Preview_Desc        = new LangMessage(this, "Open crate preview.");
    public LangMessage Command_Preview_Usage       = new LangMessage(this, "<crateId> [player]");
    public LangMessage Command_Preview_Done_Others = new LangMessage(this, "Opened &6%crate_name%&7 preview for &6%player%&7.");

    public LangMessage Command_ResetLimit_Desc        = new LangMessage(this, "Reset reward win limit for specified crate and reward.");
    public LangMessage Command_ResetLimit_Usage       = new LangMessage(this, "<player> <crateId> [rewardId]");
    public LangMessage Command_ResetLimit_Done_Crate  = new LangMessage(this, "Reset &6%player% &7win limit for all rewards of &6%crate_name%&7.");
    public LangMessage Command_ResetLimit_Done_Reward = new LangMessage(this, "Reset &6%player% &7win limit for &6%reward_name% &7reward of &6%crate_name%&7.");

    public LangMessage Command_ResetCooldown_Desc  = new LangMessage(this, "Reset player cooldown for specified crate.");
    public LangMessage Command_ResetCooldown_Usage = new LangMessage(this, "<player> <crateId>");
    public LangMessage Command_ResetCooldown_Done  = new LangMessage(this, "Reset &6%player% &7cooldown for &6%crate_name%&7.");

    public LangMessage Command_Menu_Usage       = new LangMessage(this, "[menuId]");
    public LangMessage Command_Menu_Desc        = new LangMessage(this, "Open crate menu.");
    public LangMessage Command_Menu_Done_Others = new LangMessage(this, "Opened &6%menu_id%&7 crate menu for &6%player%&7.");

    public LangMessage Crate_Error_Invalid                 = new LangMessage(this, "&cInvalid crate!");
    public LangMessage Crate_Open_Error_InventorySpace     = new LangMessage(this, "&cPlease clean up your inventory to open the crate!");
    public LangMessage Crate_Open_Error_Cooldown_Temporary = new LangMessage(this, "&cYou have to wait &6%time% &7before you can open &6%crate_name%&7 again!");
    public LangMessage Crate_Open_Error_Cooldown_OneTimed  = new LangMessage(this, "&cYou already have opened this crate, you can not open it more!");
    public LangMessage Crate_Open_Error_NoKey              = new LangMessage(this, "&cYou don't have a key for this crate!");
    public LangMessage Crate_Open_Error_NoRewards          = new LangMessage(this, "&cThis crate does not contains any rewards for you!");
    public LangMessage Crate_Open_Error_Cost_Money         = new LangMessage(this, "&cYou don't have enough money to open this crate!");
    public LangMessage Crate_Open_Error_Cost_Exp           = new LangMessage(this, "&cYou don't have enough exp levels to open this crate!");
    public LangMessage Crate_Open_Reward_Info              = new LangMessage(this, "You got the &6%reward_name% &7reward from the &6%crate_name%&7!");
    public LangMessage Crate_Open_Reward_Broadcast         = new LangMessage(this, "&7Player &a%player% &7just got the &6%reward_name% &7reward from the &6%crate_name%&7!");
    public LangMessage Crate_Key_Error_Invalid             = new LangMessage(this, "&cInvalid key!");
    public LangMessage Crate_Placeholder_Cooldown_Blank = new LangMessage(this, "Ready to open!");

    public LangMessage Menu_Invalid = new LangMessage(this, "&cMenu does not exist!");

    public LangMessage Editor_Crate_Enter_Id                  = new LangMessage(this, "&7Enter &aunique &7crate &aidentifier&7...");
    public LangMessage Editor_Crate_Enter_DisplayName         = new LangMessage(this, "&7Enter crate &adisplay name&7...");
    public LangMessage Editor_Crate_Enter_Particle_Name       = new LangMessage(this, "&7Enter &aparticle &7name...");
    public LangMessage Editor_Crate_Enter_Particle_Data       = new LangMessage(this, "&7Enter &aparticle &7data...");
    public LangMessage Editor_Crate_Enter_KeyId               = new LangMessage(this, "&7Enter &akey &7identifier...");
    public LangMessage Editor_Crate_Enter_BlockLocation       = new LangMessage(this, "&7Click a &ablock &7to assign crate...");
    public LangMessage Editor_Crate_Enter_BlockHologramText   = new LangMessage(this, "&7Enter &atext &7line...");
    public LangMessage Editor_Crate_Enter_BlockHologramOffset = new LangMessage(this, "&7Enter &aoffset &7value...");
    public LangMessage Editor_Crate_Enter_Cooldown            = new LangMessage(this, "&7Enter &acooldown &7in seconds...");
    public LangMessage Editor_Crate_Enter_Citizens            = new LangMessage(this, "&7Enter &aCitizens NPC &7ID...");
    public LangMessage Editor_Crate_Enter_AnimationConfig     = new LangMessage(this, "&7Enter &aanimation config &7name...");
    public LangMessage Editor_Crate_Enter_PreviewConfig       = new LangMessage(this, "&7Enter &apreview config &7name...");
    public LangMessage Editor_Crate_Enter_OpenCost_Money      = new LangMessage(this, "&7Enter &amoney &7cost...");
    public LangMessage Editor_Crate_Enter_OpenCost_Exp        = new LangMessage(this, "&7Enter &aexp levels &7cost...");
    public LangMessage Editor_Crate_Error_Create_Exists       = new LangMessage(this, "&cCrate with such id is already exists!");

    public LangMessage Editor_Reward_Enter_Id                = new LangMessage(this, "&7Enter &aunique &7reward &aidentifier&7...");
    public LangMessage Editor_Reward_Enter_DisplayName       = new LangMessage(this, "&7Enter reward &adisplay name&7...");
    public LangMessage Editor_Reward_Enter_Chance            = new LangMessage(this, "&7Enter win &achance&7...");
    public LangMessage Editor_Reward_Enter_Command           = new LangMessage(this, "&7Enter a &acommand&7...");
    public LangMessage Editor_Reward_Enter_WinLimit_Amount   = new LangMessage(this, "&7Enter win limit &aamount&7...");
    public LangMessage Editor_Reward_Enter_WinLimit_Cooldown = new LangMessage(this, "&7Enter win limit &acooldown&7...");
    public LangMessage Editor_Reward_Error_Create_Exist      = new LangMessage(this, "&cReward with such id is already exists!");

    public LangMessage Editor_Key_Error_Create_Exist = new LangMessage(this, "Key with such id is already exists!");
}
