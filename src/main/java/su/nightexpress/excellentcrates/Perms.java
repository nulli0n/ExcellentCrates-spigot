package su.nightexpress.excellentcrates;

import su.nexmedia.engine.api.server.JPermission;

public class Perms {

    private static final String PREFIX         = "excellentcrates.";
    private static final String PREFIX_COMMAND = PREFIX + "command.";
    private static final String PREFIX_BYPASS  = PREFIX + "bypass.";
    public static final String PREFIX_CRATE = PREFIX + "crate.";

    public static final JPermission PLUGIN  = new JPermission(PREFIX + Placeholders.WILDCARD, "Access to all the plugin functions.");
    public static final JPermission COMMAND = new JPermission(PREFIX_COMMAND + Placeholders.WILDCARD, "Access to all the plugin commands.");
    public static final JPermission BYPASS  = new JPermission(PREFIX_BYPASS + Placeholders.WILDCARD, "Bypass all the plugin restrictions.");

    public static final JPermission COMMAND_RELOAD          = new JPermission(PREFIX_COMMAND + "reload", "Access to the 'reload' sub-command.");
    public static final JPermission COMMAND_EDITOR          = new JPermission(PREFIX_COMMAND + "editor", "Access to the 'editor' sub-command.");
    public static final JPermission COMMAND_DROP            = new JPermission(PREFIX_COMMAND + "drop", "Access to the 'drop' sub-command.");
    public static final JPermission COMMAND_OPEN            = new JPermission(PREFIX_COMMAND + "open", "Access to the 'open' sub-command.");
    public static final JPermission COMMAND_GIVE            = new JPermission(PREFIX_COMMAND + "give", "Access to the 'give' sub-command.");
    public static final JPermission COMMAND_KEY             = new JPermission(PREFIX_COMMAND + "key", "Access to the 'key' sub-command (without sub-commands).");
    public static final JPermission COMMAND_KEY_GIVE        = new JPermission(PREFIX_COMMAND + "key.give", "Access to the 'key give' command.");
    public static final JPermission COMMAND_KEY_TAKE        = new JPermission(PREFIX_COMMAND + "key.take", "Access to the 'key take' command.");
    public static final JPermission COMMAND_KEY_SET         = new JPermission(PREFIX_COMMAND + "key.set", "Access to the 'key set' command.");
    public static final JPermission COMMAND_KEY_SHOW        = new JPermission(PREFIX_COMMAND + "key.show", "Access to the 'key show' command.");
    public static final JPermission COMMAND_KEY_SHOW_OTHERS = new JPermission(PREFIX_COMMAND + "key.show.others", "Access to the 'key show' command of other players.");
    public static final JPermission COMMAND_MENU            = new JPermission(PREFIX_COMMAND + "menu", "Access to the 'menu' sub-command.");
    public static final JPermission COMMAND_MENU_OTHERS     = new JPermission(PREFIX_COMMAND + "menu.others", "Access to the 'menu' sub-command on other players.");
    public static final JPermission COMMAND_PREVIEW         = new JPermission(PREFIX_COMMAND + "preview", "Access to the 'preview' sub-command.");
    public static final JPermission COMMAND_PREVIEW_OTHERS  = new JPermission(PREFIX_COMMAND + "preview.others", "Access to the 'preview' sub-command on other players.");
    public static final JPermission COMMAND_RESETCOOLDOWN   = new JPermission(PREFIX_COMMAND + "resetcooldown", "Access to the 'resetcooldown' sub-command.");
    public static final JPermission COMMAND_RESETLIMIT      = new JPermission(PREFIX_COMMAND + "resetlimit", "Access to the 'resetlimit' sub-command.");

    public static final JPermission BYPASS_CRATE_COOLDOWN        = new JPermission(PREFIX_BYPASS + "crate.opencooldown", "Bypasses crate open cooldown.");
    public static final JPermission BYPASS_CRATE_OPEN_COST_MONEY = new JPermission(PREFIX_BYPASS + "crate.opencost.money", "Bypasses crate money open cost.");
    public static final JPermission BYPASS_CRATE_OPEN_COST_EXP   = new JPermission(PREFIX_BYPASS + "crate.opencost.exp", "Bypasses crate exp open cost.");
    public static final JPermission BYPASS_REWARD_LIMIT_AMOUNT   = new JPermission(PREFIX_BYPASS + "reward.limit.amount", "Bypasses reward's win limit amount.");
    public static final JPermission BYPASS_REWARD_LIMIT_COOLDOWN = new JPermission(PREFIX_BYPASS + "reward.limit.cooldown", "Bypasses reward's win limit cooldown.");

    static {
        PLUGIN.addChildren(COMMAND, BYPASS);

        COMMAND.addChildren(COMMAND_RELOAD, COMMAND_EDITOR, COMMAND_DROP, COMMAND_OPEN, COMMAND_GIVE,
            COMMAND_KEY, COMMAND_KEY_GIVE, COMMAND_KEY_SET, COMMAND_KEY_SHOW, COMMAND_KEY_SHOW_OTHERS, COMMAND_KEY_TAKE,
            COMMAND_MENU, COMMAND_MENU_OTHERS, COMMAND_PREVIEW, COMMAND_PREVIEW_OTHERS, COMMAND_RESETCOOLDOWN,
            COMMAND_RESETLIMIT);

        BYPASS.addChildren(BYPASS_CRATE_COOLDOWN, BYPASS_CRATE_OPEN_COST_EXP, BYPASS_CRATE_OPEN_COST_MONEY,
            BYPASS_REWARD_LIMIT_AMOUNT, BYPASS_REWARD_LIMIT_COOLDOWN);
    }
}
