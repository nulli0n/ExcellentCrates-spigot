package su.nightexpress.excellentcrates.config;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.hologram.HologramType;
import su.nightexpress.excellentcrates.hooks.HookId;
import su.nightexpress.excellentcrates.util.ClickType;
import su.nightexpress.excellentcrates.util.InteractType;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.util.Plugins;
import su.nightexpress.nightcore.util.StringUtil;
import su.nightexpress.nightcore.util.Version;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static su.nightexpress.excellentcrates.Placeholders.*;
import static su.nightexpress.nightcore.util.text.tag.Tags.*;

public class Config {

    public static final String DIR_CRATES = "/crates/";
    public static final String DIR_PREVIEWS = "/previews/";
    public static final String DIR_KEYS = "/keys/";
    public static final String DIR_MENUS = "/menu/";
    public static final String DIR_OPENINGS = "/openingsv2/";

    public static final String FILE_MILESTONES = "milestones.yml";
    public static final String FILE_LOGS = "openings.log";

    public static final ConfigValue<Boolean> DATABASE_SYNC_REWARDS_DATA = ConfigValue.create("Database.Sync_Reward_Data",
            true,
            "Sets whether or not reward global data (such as win limit & cooldown) will be syncrhonized along with user data.");

    public static final ConfigValue<Boolean> MILESTONES_ENABLED = ConfigValue.create("Milestones.Enabled",
            true,
            "Sets whether or not Milestones feature is enabled (globally)."
    );

    public static final ConfigValue<HologramType> CRATE_HOLOGRAM_HANDLER = ConfigValue.create("Crate.Holograms.Handler",
            HologramType.class, HologramType.INTERNAL,
            "Sets which hologram handler will be used to display crate holograms.",
            "Available values: " + StringUtil.inlineEnum(HologramType.class, ", "),
            "For all handlers, except the '" + HologramType.INTERNAL.name() + "' one, you will have to install respective holograms plugin.",
            "For '" + HologramType.INTERNAL.name() + "' hologram handler you must have " + HookId.PROTOCOL_LIB + " installed and to be on " + Version.V1_19_R3.getLocalized() + " or newer.");

    public static final ConfigValue<Boolean> CRATE_HOLOGRAM_USE_DISPLAYS = ConfigValue.create("Crate.Holograms.DisplayEntities",
            true,
            "[" + HologramType.INTERNAL.name() + " handler only]",
            "[" + Version.V1_20_R3.getLocalized() + "+ only]",
            "When enabled, uses Display Entities for holograms instead of Armor Stands."
    );

    public static final ConfigValue<Map<String, List<String>>> CRATE_HOLOGRAM_TEMPLATES = ConfigValue.forMap("Crate.Holograms.Templates",
            (cfg, path, key) -> cfg.getStringList(path + "." + key),
            (cfg, path, map) -> map.forEach((id, text) -> cfg.set(path + "." + id, text)),
            () -> {
                return Map.of(
                        DEFAULT, Arrays.asList(
                                LIGHT_YELLOW.enclose(BOLD.enclose(CRATE_NAME)),
                                LIGHT_GRAY.enclose("Get keys at " + LIGHT_YELLOW.enclose("put_your_site.com")),
                                LIGHT_GRAY.enclose("You have " + LIGHT_YELLOW.enclose("%excellentcrates_keys_" + CRATE_ID + "%") + " keys"))
                );
            },
            "Here you can create your own hologram text templates to use it in your crates.",
            "You can use 'Crate' placeholders: " + WIKI_PLACEHOLDERS,
            Plugins.PLACEHOLDER_API + " is also supported here (if hologram handler supports it)."
    );

    public static final ConfigValue<Double> CRATE_HOLOGRAM_LINE_GAP = ConfigValue.create("Crate.Holograms.LineGap",
            0.3D,
            "[For INTERNAL handler only]",
            "Sets the gap between hologram lines.");

    public static final ConfigValue<Integer> CRATE_HOLOGRAM_UPDATE_INTERVAL = ConfigValue.create("Crate.Holograms.Update_Interval",
            5,
            "[For INTERNAL handler only]",
            "Sets how often (in seconds) crate holograms will be updated.",
            "Basically, this just removes them and adding back with updated placeholders.",
            "Setting this to low values may result in flickering.",
            "Setting this to high values may result in some players missing them until updated.");

    public static final ConfigValue<String> CRATE_COOLDOWN_FORMATTER_TIME = ConfigValue.create("Crate.Cooldown_Formatter.Time",
            "hh:mm:ss",
            "Sets time formatter for crate opening cooldown.", "'hh' - hours, 'mm' - minutes, 'ss' - seconds.");

    public static final ConfigValue<String> CRATE_COOLDOWN_FORMATTER_READY = ConfigValue.create("Crate.Cooldown_Formatter.Ready",
            LIGHT_GREEN.enclose("Ready to Open!"),
            "Sets the custom message instead of empty counter when there is no cooldown for a crate.");

    public static final ConfigValue<Boolean> CRATE_PLACEHOLDER_API_FOR_REWARDS = ConfigValue.create("Crate.PlaceholderAPI_For_Rewards",
            false,
            "When 'true' replaces " + Plugins.PLACEHOLDER_API + " placeholders in reward item's name and lore when obtained by players.",
            "NOTE: This setting may 'corrupt' item's lore layout in some cases. Ask " + Plugins.PLACEHOLDER_API + " devs if you're experiencing issues.");

    public static final ConfigValue<Boolean> CRATE_DISPLAY_REWARD_ABOVE_BLOCK = ConfigValue.create("Crate.Display_Reward_Above_Block",
            true,
            "When 'true' and if crate has NO opening animation, holographic reward will appear above crate block for a short amount of time.",
            "This option will have effect only when interacted with crate blocks.");

    public static final ConfigValue<Boolean> CRATE_HOLD_KEY_TO_OPEN = ConfigValue.create("Crate.Hold_Key_To_Open",
            false,
            "Sets whether player must hold key in main hand in order to open crate.",
            "NOTE: This setting will not work if crate has virtual keys assigned.");

    public static final ConfigValue<Integer> CRATE_MASS_OPENING_LIMIT = ConfigValue.create("Crate.Mass_Opening_Limit",
            30,
            "Sets how many crates player can open at once when doing mass opening.",
            "Setting this to high values may result in lags, stutters, errors and bugs.",
            "Especially if crate contains rewards with commands of other plugins.");

    public static final ConfigValue<Long> CRATE_PREVIEW_COOLDOWN = ConfigValue.create("Crate.Preview_Cooldown",
            2500L,
            "Sets cooldown (in milliseconds) for crate preview by clicking crate block(s).",
            "The main purpose of this setting is to prevent exploit using hacked clients by sending a lot of crate interaction packets causing server overload by GUI generation.",
            "Resets on player quit.",
            "[Default is 2500]"
    );

    public static final ConfigValue<Integer> CRATE_OPENING_CLOSE_TIME = ConfigValue.create("Crate.Opening_Close_Time",
            20,
            "Sets how soon (in ticks) crate opening animation GUI will be closed when completed.",
            "1 second = 20 ticks. 20 ticks by default.");

    public static final ConfigValue<Boolean> CRATE_OPENING_ALLOW_SKIP = ConfigValue.create("Crate.Opening_Allow_Skip",
            false,
            "Sets whether or not players can skip crate opening animations.",
            "When enabled, make sure the check out the 'Max_Ticks_To_Skip' setting in the " + DIR_OPENINGS + " configs."
    );

    public static final ConfigValue<Double> CRATE_PUSHBACK_Y = ConfigValue.create("Crate.Block_Pushback.Y",
            -0.4D,
            "Sets the Y offset for crate block pushback.");

    public static final ConfigValue<Double> CRATE_PUSHBACK_MULTIPLY = ConfigValue.create("Crate.Block_Pushback.Multiply",
            -1.25D,
            "Vector multiplier for crate block pushback. The higher value - the harder pushback.");
    public static final ConfigValue<DateTimeFormatter> LOGS_DATE_FORMAT = ConfigValue.create("Logs.DateFormat",
            (cfg, path, def) -> DateTimeFormatter.ofPattern(cfg.getString(path, "dd/MM/yyyy HH:mm:ss")),
            (cfg, path, formatter) -> cfg.set(path, "dd/MM/yyyy HH:mm:ss"),
            () -> DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    public static final ConfigValue<Boolean> LOGS_TO_CONSOLE = ConfigValue.create("Logs.Enabled.Console",
            false,
            "Sets whether or not all crate openings & reward wins will be logged to console.");
    public static final ConfigValue<Boolean> LOGS_TO_FILE = ConfigValue.create("Logs.Enabled.File",
            true,
            "Sets whether or not all crate openings & reward wins will be logged to a file.");
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
            "Allowed click types: " + StringUtil.inlineEnum(ClickType.class, ", "),
            "Allowed crate actions: " + StringUtil.inlineEnum(InteractType.class, ", "));

    @Nullable
    public static InteractType getCrateClickAction(@NotNull ClickType clickType) {
        return CRATE_CLICK_ACTIONS.get().get(clickType);
    }

    public static boolean isMilestonesEnabled() {
        return MILESTONES_ENABLED.get();
    }

    @NotNull
    public static HologramType getHologramType() {
        return CRATE_HOLOGRAM_HANDLER.get();
    }
}
