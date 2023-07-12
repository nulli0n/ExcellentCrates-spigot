package su.nightexpress.excellentcrates.config;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nexmedia.engine.api.config.JOption;
import su.nexmedia.engine.api.type.ClickType;
import su.nexmedia.engine.utils.CollectionsUtil;
import su.nexmedia.engine.utils.Colorizer;
import su.nightexpress.excellentcrates.api.CrateClickAction;

import java.util.HashMap;
import java.util.Map;

public class Config {

    public static final String DIR_CRATES   = "/crates/";
    public static final String DIR_PREVIEWS = "/previews/";
    public static final String DIR_KEYS     = "/keys/";
    public static final String DIR_MENUS    = "/menu/";
    public static final String DIR_OPENINGS = "/openings/";

    public static final String FILE_MILESTONES = "milestones.yml";
    public static final String FILE_RARITY = "rarity.yml";

    public static final JOption<String> EDITOR_TITLE_CRATE = JOption.create("Editor.Title.Crate", "Crate Editor",
        "Title for the Crate Editor GUIs.").mapReader(Colorizer::apply);

    public static final JOption<String> EDITOR_TITLE_KEY = JOption.create("Editor.Title.Key", "Key Editor",
        "Title for the Key Editor GUIs.").mapReader(Colorizer::apply);

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

    private static final JOption<Map<ClickType, CrateClickAction>> CRATE_CLICK_ACTIONS = new JOption<Map<ClickType, CrateClickAction>>("Crate.Click_Actions",
        (cfg, path, def) -> {
            Map<ClickType, CrateClickAction> map = new HashMap<>();
            for (ClickType clickType : ClickType.values()) {
                CrateClickAction clickAction = cfg.getEnum(path + "." + clickType.name(), CrateClickAction.class);
                if (clickAction == null) continue;

                map.put(clickType, clickAction);
            }
            return map;
        },
        () -> Map.of(
            ClickType.LEFT, CrateClickAction.CRATE_PREVIEW, ClickType.RIGHT, CrateClickAction.CRATE_OPEN,
            ClickType.SHIFT_RIGHT, CrateClickAction.CRATE_MASS_OPEN
        ),
        "Defines the crate behavior on certain clicks.",
        "Allowed click types: " + String.join(", ", CollectionsUtil.getEnumsList(ClickType.class)),
        "Allowed crate actions: " + String.join(", ", CollectionsUtil.getEnumsList(CrateClickAction.class)))
        .setWriter((cfg, path, map) -> map.forEach((click, action) -> cfg.set(path + "." + click.name(), action)));

    @Nullable
    public static CrateClickAction getCrateClickAction(@NotNull ClickType clickType) {
        return CRATE_CLICK_ACTIONS.get().get(clickType);
    }
}
