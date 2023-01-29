package su.nightexpress.excellentcrates.config;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nexmedia.engine.api.config.JOption;
import su.nexmedia.engine.api.config.JYML;
import su.nexmedia.engine.api.type.ClickType;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.api.CrateClickAction;

import java.util.HashMap;
import java.util.Map;

public class Config {

    public static final String DIR_CRATES   = "/crates/";
    public static final String DIR_PREVIEWS = "/previews/";
    public static final String DIR_KEYS     = "/keys/";
    public static final String DIR_MENUS    = "/menu/";
    public static final String DIR_OPENINGS = "/openings/";

    public static final JOption<String> EDITOR_TITLE_CRATE = JOption.create("Editor.Title.Crate", "Crate Editor",
        "Title for the Crate Editor GUIs.");
    public static final JOption<String> EDITOR_TITLE_KEY = JOption.create("Editor.Title.Key", "Key Editor",
        "Title for the Key Editor GUIs.");

    public static final JOption<Boolean> CRATE_PLACEHOLDER_API_FOR_REWARDS = JOption.create("Crate.PlaceholderAPI_For_Rewards", false,
        "When 'true' applies PlaceholderAPI placeholders for crate reward items when they're about to be given to a player.");

    public static final JOption<Boolean> CRATE_PREVENT_OPENING_SKIP = JOption.create("Crate.Prevent_Opening_Skip", false,
    "When 'true' prevents force closing opening animations to get reward instantly.");

    public static double                 CRATE_PUSHBACK_Y;
    public static double CRATE_PUSHBACK_MULTIPLY;
    private static Map<ClickType, CrateClickAction> CRATE_CLICK_ACTIONS;

    @Nullable
    public static CrateClickAction getCrateClickAction(@NotNull ClickType clickType) {
        return CRATE_CLICK_ACTIONS.get(clickType);
    }

    public static void load(@NotNull ExcellentCrates plugin) {
        JYML cfg = plugin.getConfig();

        String path = "Crate.Click_Actions.";
        CRATE_CLICK_ACTIONS = new HashMap<>();
        for (ClickType clickType : ClickType.values()) {
            CrateClickAction clickAction = cfg.getEnum(path + clickType.name(), CrateClickAction.class);
            if (clickAction == null) continue;

            CRATE_CLICK_ACTIONS.put(clickType, clickAction);
        }

        path = "Crate.Block_Pushback.";
        CRATE_PUSHBACK_Y = cfg.getDouble(path + "Y", -0.4D);
        CRATE_PUSHBACK_MULTIPLY = cfg.getDouble(path + "Multiply", -1.25D);
    }
}
