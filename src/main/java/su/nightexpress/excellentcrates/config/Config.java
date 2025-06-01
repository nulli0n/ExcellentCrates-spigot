package su.nightexpress.excellentcrates.config;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.hologram.HologramTemplate;
import su.nightexpress.excellentcrates.util.ClickType;
import su.nightexpress.excellentcrates.util.InteractType;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.util.Enums;
import su.nightexpress.nightcore.util.Plugins;
import su.nightexpress.nightcore.util.time.TimeFormatType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static su.nightexpress.excellentcrates.Placeholders.WIKI_PLACEHOLDERS;

public class Config {

    public static final String DIR_CRATES         = "/crates/";
    public static final String DIR_PREVIEWS       = "/previews/";
    public static final String DIR_KEYS           = "/keys/";
    public static final String DIR_MENUS          = "/menu/";
    public static final String DIR_OPENINGS_GUI   = "/openings/inventory/";
    public static final String DIR_OPENINGS_WORLD = "/openings/world/";

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



    public static final ConfigValue<Boolean> MILESTONES_ENABLED = ConfigValue.create("Milestones.Enabled",
        true,
        "Controls whether Milestones feature is enabled.");

    public static final ConfigValue<Boolean> CRATE_ALLOW_CRATES_IN_AIR_BLOCKS = ConfigValue.create("Crate.Allow_Crates_In_Air_Blocks",
        false,
        "When enabled allows crates to be assigned to 'air' blocks and disables block validation on crate load."
    );

    public static final ConfigValue<Integer> CRATE_EFFECTS_VISIBILITY_DISTANCE = ConfigValue.create("Crate.Effects.Visibility_Distance",
        24,
        "Sets max. distance where players can see crate particles and holograms."
    );

    public static final ConfigValue<Boolean> CRATE_HOLOGRAM_USE_DISPLAYS = ConfigValue.create("Crate.Holograms.DisplayEntities",
        true,
        "When enabled, uses Display Entities for holograms instead of Armor Stands."
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

    public static final ConfigValue<Boolean> CRATE_HOLD_KEY_TO_OPEN = ConfigValue.create("Crate.Hold_Key_To_Open",
        false,
        "Controls whether player must hold a key in the main hand in order to open crate.",
        "[*] Works only for physical (not virtual) keys."
    );

    public static final ConfigValue<Integer> CRATE_MASS_OPENING_LIMIT = ConfigValue.create("Crate.Mass_Opening_Limit",
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

    private static final ConfigValue<Map<ClickType, InteractType>> CRATE_CLICK_ACTIONS = ConfigValue.create("Crate.Click_Actions",
        (cfg, path, def) -> {
            Map<ClickType, InteractType> map = new HashMap<>();
            for (ClickType clickType : ClickType.values()) {
                InteractType clickAction = cfg.getEnum(path + "." + clickType.name(), InteractType.class);
                if (clickAction == null) continue;

                map.put(clickType, clickAction);
            }
            return map;
        },
        (cfg, path, map) -> map.forEach((click, action) -> cfg.set(path + "." + click.name(), action)),
        () -> Map.of(
            ClickType.LEFT, InteractType.CRATE_PREVIEW,
            ClickType.RIGHT, InteractType.CRATE_OPEN,
            ClickType.SHIFT_RIGHT, InteractType.CRATE_MASS_OPEN
        ),
        "Defines the crate behavior on certain clicks.",
        "Allowed click types: " + Enums.inline(ClickType.class),
        "Allowed crate actions: " + Enums.inline(InteractType.class));

    @Nullable
    public static InteractType getCrateClickAction(@NotNull ClickType clickType) {
        return CRATE_CLICK_ACTIONS.get().get(clickType);
    }

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

    public static boolean isKeyHoldRequired() {
        return CRATE_HOLD_KEY_TO_OPEN.get();
    }
}
