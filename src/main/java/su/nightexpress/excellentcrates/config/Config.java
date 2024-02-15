package su.nightexpress.excellentcrates.config;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nexmedia.engine.Version;
import su.nexmedia.engine.api.config.JOption;
import su.nexmedia.engine.utils.CollectionsUtil;
import su.nexmedia.engine.utils.Colorizer;
import su.nexmedia.engine.utils.EngineUtils;
import su.nightexpress.excellentcrates.util.InteractType;
import su.nightexpress.excellentcrates.hooks.HookId;
import su.nightexpress.excellentcrates.util.ClickType;
import su.nightexpress.excellentcrates.hologram.HologramType;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static su.nexmedia.engine.utils.Colors2.*;
import static su.nightexpress.excellentcrates.Placeholders.*;

public class Config {

    public static final String DIR_CRATES   = "/crates/";
    public static final String DIR_PREVIEWS = "/previews/";
    public static final String DIR_KEYS     = "/keys/";
    public static final String DIR_MENUS    = "/menu/";
    public static final String DIR_OPENINGS = "/openings/";

    public static final String FILE_MILESTONES = "milestones.yml";
    public static final String FILE_RARITY     = "rarity.yml";
    public static final String FILE_LOGS       = "openings.log";

    public static final JOption<String> EDITOR_TITLE_CRATE = JOption.create("Editor.Title.Crate", "Crate Editor",
        "Title for the Crate Editor GUIs.").mapReader(Colorizer::apply);

    public static final JOption<String> EDITOR_TITLE_KEY = JOption.create("Editor.Title.Key", "Key Editor",
        "Title for the Key Editor GUIs.").mapReader(Colorizer::apply);

    public static final JOption<HologramType> CRATE_HOLOGRAM_HANDLER = JOption.create("Crate.Holograms.Handler",
        HologramType.class, HologramType.INTERNAL,
        "Sets which hologram handler will be used to display crate holograms.",
        "Available values: " + String.join(", ", CollectionsUtil.getEnumsList(HologramType.class)),
        "For all handlers, except the '" + HologramType.INTERNAL.name() + "' one, you will have to install respective holograms plugin.",
        "For '" + HologramType.INTERNAL.name() + "' hologram handler you must have " + HookId.PROTOCOL_LIB + " installed and to be on " + Version.V1_19_R3.getLocalized() + " or newer.");

    public static final JOption<Map<String, List<String>>> CRATE_HOLOGRAM_TEMPLATES = JOption.forMap("Crate.Holograms.Templates",
        (cfg, path, key) -> Colorizer.apply(cfg.getStringList(path + "." + key)),
        () -> {
            return Map.of(
                DEFAULT, Arrays.asList(
                    YELLOW + BOLD + CRATE_NAME,
                    GRAY + "Purchase keys at " + YELLOW + "http://samplesmp.com/store",
                    GRAY + "You currently have " + YELLOW + "%excellentcrates_keys_" + CRATE_ID + "%" + GRAY + " keys")
            );
        },
        "Here you can create your own hologram text templates to use it in your crates.",
        "You can use 'Crate' placeholders: " + WIKI_PLACEHOLDERS,
        EngineUtils.PLACEHOLDER_API + " is also supported here (if hologram handler supports it)."
    ).setWriter((cfg, path, map) -> map.forEach((id, text) -> cfg.set(path + "." + id, text)));

    public static final JOption<Double> CRATE_HOLOGRAM_Y_OFFSET = JOption.create("Crate.Holograms.Y_Offset",
        1.5D,
        "[For EXTERNAL handlers only]", "Sets Y offset for hologram location.");

    public static final JOption<Double> CRATE_HOLOGRAM_LINE_GAP = JOption.create("Crate.Holograms.LineGap",
        0.3D,
        "[For INTERNAL handler only]", "Sets the gap between hologram lines.");

    public static final JOption<Integer> CRATE_HOLOGRAM_UPDATE_INTERVAL = JOption.create("Crate.Holograms.Update_Interval",
        5,
        "[For INTERNAL handler only]",
        "Sets how often (in seconds) crate holograms will be updated.",
        "Basically, this just removes them and adding back with updated placeholders.",
        "Setting this to low values may result in flickering.",
        "Setting this to high values may result in some players missing them until updated.");

    public static final JOption<String> CRATE_COOLDOWN_FORMATTER_TIME = JOption.create("Crate.Cooldown_Formatter.Time",
        "hh:mm:ss",
        "Sets the time formatter for crate opening cooldown.", "'hh' - hours, 'mm' - minutes, 'ss' - seconds.");

    public static final JOption<String> CRATE_COOLDOWN_FORMATTER_READY = JOption.create("Crate.Cooldown_Formatter.Ready",
        "&aReady to Open!",
        "Sets the custom message instead of empty counter when there is no cooldown for a crate.");

    public static final JOption<Boolean> CRATE_PLACEHOLDER_API_FOR_REWARDS = JOption.create("Crate.PlaceholderAPI_For_Rewards", false,
        "When 'true' applies PlaceholderAPI placeholders for crate reward items when they're about to be given to a player.");

    public static final JOption<Boolean> CRATE_DISPLAY_REWARD_ABOVE_BLOCK = JOption.create("Crate.Display_Reward_Above_Block", true,
        "When 'true' and if crate has NO opening animation, holographic reward will appear above crate block for a short amount of time.",
        "This option will have effect only when interacted with crate blocks.");

    public static final JOption<Boolean> CRATE_HOLD_KEY_TO_OPEN = JOption.create("Crate.Hold_Key_To_Open", false,
        "Sets whether player must hold key in main hand in order to open crate.");

    public static final JOption<Boolean> CRATE_PREVENT_OPENING_SKIP = JOption.create("Crate.Prevent_Opening_Skip", false,
    "When 'true' prevents force closing opening animations to get reward instantly.");

    public static final JOption<Integer> CRATE_OPENING_CLOSE_TIME = JOption.create("Crate.Opening_Close_Time", 20,
        "Sets how soon (in ticks) crate opening animation GUI will be closed when completed.",
        "1 second = 20 ticks. 20 ticks by default.");

    public static final JOption<Double> CRATE_PUSHBACK_Y = JOption.create("Crate.Block_Pushback.Y", -0.4D,
        "Sets the Y offset for crate block pushback.");

    public static final JOption<Double> CRATE_PUSHBACK_MULTIPLY = JOption.create("Crate.Block_Pushback.Multiply", -1.25D,
        "Vector multiplier for crate block pushback. The higher value - the harder pushback.");

    private static final JOption<Map<ClickType, InteractType>> CRATE_CLICK_ACTIONS = new JOption<Map<ClickType, InteractType>>("Crate.Click_Actions",
        (cfg, path, def) -> {
            Map<ClickType, InteractType> map = new HashMap<>();
            for (ClickType clickType : ClickType.values()) {
                InteractType clickAction = cfg.getEnum(path + "." + clickType.name(), InteractType.class);
                if (clickAction == null) continue;

                map.put(clickType, clickAction);
            }
            return map;
        },
        () -> Map.of(
            ClickType.LEFT, InteractType.CRATE_PREVIEW, ClickType.RIGHT, InteractType.CRATE_OPEN,
            ClickType.SHIFT_RIGHT, InteractType.CRATE_MASS_OPEN
        ),
        "Defines the crate behavior on certain clicks.",
        "Allowed click types: " + String.join(", ", CollectionsUtil.getEnumsList(ClickType.class)),
        "Allowed crate actions: " + String.join(", ", CollectionsUtil.getEnumsList(InteractType.class)))
        .setWriter((cfg, path, map) -> map.forEach((click, action) -> cfg.set(path + "." + click.name(), action)));

    public static final JOption<DateTimeFormatter> LOGS_DATE_FORMAT = new JOption<DateTimeFormatter>("Logs.DateFormat",
        (cfg, path, def) -> DateTimeFormatter.ofPattern(cfg.getString(path, "dd/MM/yyyy HH:mm:ss")),
        () -> DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
        .setWriter((cfg, path, formatter) -> cfg.set(path, "dd/MM/yyyy HH:mm:ss"));

    public static final JOption<Boolean> LOGS_TO_CONSOLE = JOption.create("Logs.Enabled.Console",
        false,
        "Sets whether or not all crate openings & reward wins will be logged to console.");

    public static final JOption<Boolean> LOGS_TO_FILE = JOption.create("Logs.Enabled.File",
        true,
        "Sets whether or not all crate openings & reward wins will be logged to a file.");

    @Nullable
    public static InteractType getCrateClickAction(@NotNull ClickType clickType) {
        return CRATE_CLICK_ACTIONS.get().get(clickType);
    }
}
