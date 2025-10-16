package su.nightexpress.excellentcrates.config;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.hologram.HologramTemplate;
import su.nightexpress.excellentcrates.hooks.HookId;
import su.nightexpress.excellentcrates.util.CrateUtils;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.util.Enums;
import su.nightexpress.nightcore.util.Plugins;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.time.TimeFormatType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static su.nightexpress.excellentcrates.Placeholders.WIKI_PLACEHOLDERS;

public class Config {

    public static final String DIR_CRATES   = "/crates/";
    public static final String DIR_PREVIEWS = "/previews/";
    public static final String DIR_KEYS     = "/keys/";
    public static final String DIR_MENUS    = "/menu/";
    public static final String DIR_UI       = "/ui/";

    public static final String DIR_OPENINGS             = "/openings/";
    public static final String DIR_OPENINGS_INVENTORY   = DIR_OPENINGS + "inventory/";
    public static final String DIR_OPENINGS_SELECTABLE  = DIR_OPENINGS + "selectable/";
    public static final String DIR_OPENINGS_SIMPLE_ROLL = DIR_OPENINGS + "simple_roll/";

    public static final String FILE_MILESTONES = "milestones.yml";
    public static final String FILE_LOGS       = "openings.log";

    public static final ConfigValue<String> LOGS_DATE_FORMAT = ConfigValue.create("Logs.DateFormat",
        "dd/MM/yyyy HH:mm:ss",
        "Sets date format for crate opening logs."
    );

    public static final ConfigValue<Boolean> LOGS_TO_CONSOLE = ConfigValue.create("Logs.Enabled.Console",
        false,
        "Sets whether or not all crate openings & reward wins will be logged to console."
    );

    public static final ConfigValue<Boolean> LOGS_TO_FILE = ConfigValue.create("Logs.Enabled.File",
        true,
        "Sets whether or not all crate openings & reward wins will be logged to a file."
    );



    public static final ConfigValue<Boolean> DATA_REWARD_LIMITS_SYNC_ENABLED = ConfigValue.create("Data.Rewards.Limits.Synchronize",
        true,
        "When enabled, synchronizes reward limits datas (both, global and player ones) in addition to player data sync.",
        "[*] CAUTION: Removes all reward limit entries from the database when a crate/reward gets deleted.",
        "[*] Useless for SQLite."
    );

    public static final ConfigValue<Integer> DATA_REWARD_LIMITS_SAVE_INTERVAL = ConfigValue.create("Data.Rewards.Limits.Save_Interval",
        3,
        "Sets auto-save interval for reward's limit datas.",
        "Data also saved on plugin reload and server reboot.",
        "[*] You can increase this value to improve performance if you don't sync/share reward limit data across multiple servers."
    );

    public static final ConfigValue<Boolean> DATA_CRATE_DATA_SYNC_ENABLED = ConfigValue.create("Data.Crates.GlobalData.Synchronize",
        true,
        "When enabled, synchronizes global crate datas in addition to player data sync.",
        "[*] CAUTION: Removes all crate data entries from the database when a crate gets deleted.",
        "[*] Useless for SQLite."
    );

    public static final ConfigValue<Integer> DATA_CRATE_DATA_SAVE_INTERVAL = ConfigValue.create("Data.Crates.GlobalData.Save_Interval",
        60,
        "Sets auto-save interval for crate's datas.",
        "Data also saved on plugin reload and server reboot.",
        "Crate data stores the following information:",
        "- Latest opener ID",
        "- Latest opener Name",
        "- Latest reward ID",
        "[*] You can increase this value to improve performance if you don't sync/share crate data across multiple servers."
    );


    public static final ConfigValue<Boolean> FEATURE_MASS_OPENING = ConfigValue.create("Features.MassOpening",
        true,
        "Whether Mass Opening feature is enabled."
    );


    public static final ConfigValue<Boolean> MILESTONES_ENABLED = ConfigValue.create("Milestones.Enabled",
        true,
        "Controls whether Milestones feature is enabled.");


    public static final ConfigValue<Integer> CRATE_SAVE_INTERVAL = ConfigValue.create("Crate.SaveInterval",
        300,
        "Sets save interval (in seconds) for crates that were changed using the in-game editor.",
        "[Default is 300 (5 min)]"
    );

    public static final ConfigValue<NightItem> CRATE_LINK_TOOL = ConfigValue.create("Crate.LinkTool",
        CrateUtils.getDefaultLinkTool(),
        "Sets the Link Tool item layout."
    );

    public static final ConfigValue<Boolean> CRATE_ALLOW_CRATES_IN_AIR_BLOCKS = ConfigValue.create("Crate.Allow_Crates_In_Air_Blocks",
        false,
        "When enabled allows crates to be assigned to 'air' blocks and disables block validation on crate load."
    );

    public static final ConfigValue<Integer> CRATE_EFFECTS_VISIBILITY_DISTANCE = ConfigValue.create("Crate.Effects.Visibility_Distance",
        24,
        "Sets max. distance where players can see crate particles and holograms."
    );

    public static final ConfigValue<Map<String, HologramTemplate>> CRATE_HOLOGRAM_TEMPLATES = ConfigValue.forMapById("Crate.Holograms.TemplateList",
        HologramTemplate::read,
        map -> map.putAll(HologramTemplate.getDefaultTemplates()),
        "Custom hologram templates to display above crate blocks.",
        "Allowed Placeholders:",
        "- " + Plugins.PLACEHOLDER_API + " placeholders.",
        "- Crate placeholders: " + WIKI_PLACEHOLDERS
    );

    public static final ConfigValue<Double> CRATE_HOLOGRAM_LINE_GAP = ConfigValue.create("Crate.Holograms.LineGap",
        0.3D,
        "Sets the gap between hologram lines."
    );

    public static final ConfigValue<Integer> CRATE_HOLOGRAM_UPDATE_INTERVAL = ConfigValue.create("Crate.Holograms.Update_Interval",
        5,
        "Sets how often (in seconds) crate holograms will update."
    );

    public static final ConfigValue<TimeFormatType> CRATE_COOLDOWN_FORMAT_TYPE = ConfigValue.create("Crate.Cooldown_Format_Type",
        TimeFormatType.class,
        TimeFormatType.DIGITAL,
        "Sets crate cooldown format type.",
        "Available values: [" + Enums.inline(TimeFormatType.class) + "]"
    );

    public static final ConfigValue<Boolean> OPENING_CONFIRM_FOR_SINGLE_COST = ConfigValue.create("Crate.Opening.Confirmation.ForSingleCost",
        false,
        "Controls whether the Costs GUI will appear even if there is only cost option available."
    );

    public static final ConfigValue<Boolean> MASS_OPENING_ALLOW_FOR_NO_COST = ConfigValue.create("Crate.MassOpening.AllowForNoCost",
        false,
        "Controls whether players can do Mass Opening for crates with no cost options defined."
    );

    public static final ConfigValue<Boolean> MASS_OPENING_SNEAK_TO_USE = ConfigValue.create("Crate.MassOpening.SneakToUse",
        true,
        "Controls whether players can do Mass Opening by opening crates while sneaking."
    );

    public static final ConfigValue<Integer> MASS_OPENING_LIMIT = ConfigValue.create("Crate.Mass_Opening_Limit",
        30,
        "Limits amount of crate openings for the Mass Opening feature to this value.",
        "[*] STABILITY NOTICE:",
        "  Stability highly depends on reward's content.",
        "  Using high values may result in freezes, stutters, bugs and errors."
    );

    public static final ConfigValue<Long> CRATE_PREVIEW_COOLDOWN = ConfigValue.create("Crate.Preview_Cooldown",
        2500L,
        "Sets cooldown (in milliseconds) for crate preview by clicking crate block(s).",
        "The main purpose of this setting is to prevent exploit using hacked clients by sending a lot of crate interaction packets causing server overload by GUI generation.",
        "Resets on player quit.",
        "[Default is 2500]"
    );

    public static final ConfigValue<Double> CRATE_PUSHBACK_Y = ConfigValue.create("Crate.Block_Pushback.Y",
        -0.4D,
        "Sets the Y offset for crate block pushback.");

    public static final ConfigValue<Double> CRATE_PUSHBACK_MULTIPLY = ConfigValue.create("Crate.Block_Pushback.Multiply",
        -1.25D,
        "Vector multiplier for crate block pushback. The higher value - the harder pushback.");

    public static final ConfigValue<Boolean> CRATE_REVERSE_CLICK_ACTIONS = ConfigValue.create("Crate.Reverse_Click_Actions",
        false,
        "Controls whether click actions, when interacting with crate blocks, should be reversed.",
        "By default it uses Left Click to preview crates, and Right Click to open them."
    );

    public static final ConfigValue<Boolean> HOLOGRAMS_ENABLED = ConfigValue.create("Holograms.Enabled",
        true,
        "Controls whether the Holograms feature is available.",
        "[*] One of the following plugins is required for holograms to work: " + HookId.PACKET_EVENTS + " or " + HookId.PROTOCOL_LIB
    );

    public static final ConfigValue<Boolean> OPENINGS_GUI_SIMULATE_REAL_CHANCES = ConfigValue.create("Openings.GUI.Simulate_Real_Chances",
        false,
        "[ THIS SETTING DOES NOT AFFECT THE FINAL REWARD, IT IS PREDICTED WHEN PLAYER OPENED A CRATE ]",
        "Controls whether reward's weight and rarity should be respected when displaying rewards during GUI opening animation.",
        "When disabled, rewards choosen by a blind random.",
        "[Default is false]"
    );

    public static boolean isMilestonesEnabled() {
        return MILESTONES_ENABLED.get();
    }

    public static boolean isCrateDataSynchronized() {
        return DATA_CRATE_DATA_SYNC_ENABLED.get();
    }

    public static boolean isRewardLimitsSynchronized() {
        return DATA_REWARD_LIMITS_SYNC_ENABLED.get();
    }

    @NotNull
    public static List<String> getHologramTemplateIds() {
        return new ArrayList<>(CRATE_HOLOGRAM_TEMPLATES.get().keySet());
    }

    @Nullable
    public static HologramTemplate getHologramTemplate(@NotNull String id) {
        return CRATE_HOLOGRAM_TEMPLATES.get().get(id.toLowerCase());
    }

    public static boolean isCrateInAirBlocksAllowed() {
        return CRATE_ALLOW_CRATES_IN_AIR_BLOCKS.get();
    }

    public static boolean isMassOpenEnabled() {
        return FEATURE_MASS_OPENING.get();
    }
}
