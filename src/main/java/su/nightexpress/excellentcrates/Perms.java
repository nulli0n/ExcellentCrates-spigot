package su.nightexpress.excellentcrates;

public class Perms {

    private static final String PREFIX = "excellentcrates.";

    public static final String ADMIN = PREFIX + "admin";
    public static final String USER  = PREFIX + "user";
    public static final String CRATE = PREFIX + "crate.";

    public static final String COMMAND_DROP           = PREFIX + "command.drop";
    public static final String COMMAND_FORCEOPEN      = PREFIX + "command.forceopen";
    public static final String COMMAND_GIVE           = PREFIX + "command.give";
    public static final String COMMAND_GIVEKEY        = PREFIX + "command.givekey";
    public static final String COMMAND_KEYS           = PREFIX + "command.keys";
    public static final String COMMAND_KEYS_OTHERS    = COMMAND_KEYS + ".others";
    public static final String COMMAND_MENU           = PREFIX + "command.menu";
    public static final String COMMAND_MENU_OTHERS    = COMMAND_MENU + ".others";
    public static final String COMMAND_PREVIEW        = PREFIX + "command.preview";
    public static final String COMMAND_PREVIEW_OTHERS = COMMAND_PREVIEW + ".others";
    public static final String COMMAND_RESETCOOLDOWN  = PREFIX + "command.resetcooldown";
    public static final String COMMAND_RESETLIMIT     = PREFIX + "command.resetlimit";
    public static final String COMMAND_TAKEKEY        = PREFIX + "command.takekey";

    public static final String BYPASS_CRATE_COOLDOWN        = PREFIX + "bypass.crate.opencooldown";
    public static final String BYPASS_CRATE_OPEN_COST       = PREFIX + "bypass.crate.opencost.";
    public static final String BYPASS_REWARD_LIMIT_AMOUNT   = PREFIX + "bypass.reward.limit.amount";
    public static final String BYPASS_REWARD_LIMIT_COOLDOWN = PREFIX + "bypass.reward.limit.cooldown";
}
