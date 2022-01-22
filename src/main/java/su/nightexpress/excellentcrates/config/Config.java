package su.nightexpress.excellentcrates.config;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nexmedia.engine.api.config.ConfigTemplate;
import su.nexmedia.engine.api.type.ClickType;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.api.CrateClickAction;

import java.util.HashMap;
import java.util.Map;

public class Config extends ConfigTemplate {

    public static final String DIR_CRATES     = "/crates/";
    public static final String DIR_PREVIEWS   = "/previews/";
    public static final String DIR_KEYS       = "/keys/";
    public static final String DIR_MENUS      = "/menu/";
    public static final String DIR_ANIMATIONS = "/animations/";

    public static double CRATE_PUSHBACK_Y;
    public static double CRATE_PUSHBACK_MULTIPLY;
    private static Map<ClickType, CrateClickAction> CRATE_CLICK_ACTIONS;

    public Config(@NotNull ExcellentCrates plugin) {
        super(plugin);
    }

    @Nullable
    public static CrateClickAction getCrateClickAction(@NotNull ClickType clickType) {
        return CRATE_CLICK_ACTIONS.get(clickType);
    }

    @Override
    public void load() {

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
