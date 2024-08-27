package su.nightexpress.excellentcrates.config;

import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.nightcore.util.wrapper.UniPermission;

import static org.bukkit.permissions.PermissionDefault.TRUE;

public class Perms {

    public static final String PREFIX = "excellentcrates.";
    public static final String PREFIX_COMMAND = PREFIX + "command.";
    public static final UniPermission COMMAND = new UniPermission(PREFIX_COMMAND + Placeholders.WILDCARD, "Access to all the plugin commands.");
    public static final UniPermission COMMAND_RELOAD = new UniPermission(PREFIX_COMMAND + "reload", "Access to the '/crate reload' sub-command.");
    public static final UniPermission COMMAND_EDITOR = new UniPermission(PREFIX_COMMAND + "editor", "Access to the '/crate editor' sub-command.");
    public static final UniPermission COMMAND_DROP = new UniPermission(PREFIX_COMMAND + "drop", "Access to the '/crate drop' sub-command.");
    public static final UniPermission COMMAND_DROP_KEY = new UniPermission(PREFIX_COMMAND + "dropkey", "Access to the '/crate dropkey' sub-command.");
    public static final UniPermission COMMAND_OPEN = new UniPermission(PREFIX_COMMAND + "open", "Access to the '/crate open' sub-command.");
    public static final UniPermission COMMAND_OPEN_FOR = new UniPermission(PREFIX_COMMAND + "openfor", "Access to the '/crate openfor' sub-command.");
    public static final UniPermission COMMAND_GIVE = new UniPermission(PREFIX_COMMAND + "give", "Access to the '/crate give' sub-command.");
    public static final UniPermission COMMAND_KEY = new UniPermission(PREFIX_COMMAND + "key", "Access to the '/crate key' sub-command (without sub-commands).");
    public static final UniPermission COMMAND_KEY_GIVE = new UniPermission(PREFIX_COMMAND + "key.give", "Access to the '/crate key give' sub-command.");
    public static final UniPermission COMMAND_KEY_TAKE = new UniPermission(PREFIX_COMMAND + "key.take", "Access to the '/crate key take' sub-command.");
    public static final UniPermission COMMAND_KEY_SET = new UniPermission(PREFIX_COMMAND + "key.set", "Access to the '/crate key set' sub-command.");
    public static final UniPermission COMMAND_KEY_INSPECT = new UniPermission(PREFIX_COMMAND + "key.show", "Access to the '/crate key inspect' sub-command.");
    public static final UniPermission COMMAND_KEY_INSPECT_OTHERS = new UniPermission(PREFIX_COMMAND + "key.show.others", "Access to the '/crate key inspect' sub-command of other players.");
    public static final UniPermission COMMAND_MENU = new UniPermission(PREFIX_COMMAND + "menu", "Access to the '/crate menu' sub-command.");
    public static final UniPermission COMMAND_MENU_OTHERS = new UniPermission(PREFIX_COMMAND + "menu.others", "Access to the '/crate menu' sub-command on other players.");
    public static final UniPermission COMMAND_PREVIEW = new UniPermission(PREFIX_COMMAND + "preview", "Access to the '/crate preview' sub-command.");
    public static final UniPermission COMMAND_PREVIEW_OTHERS = new UniPermission(PREFIX_COMMAND + "preview.others", "Access to the '/crate preview' sub-command on other players.");
    public static final UniPermission COMMAND_RESETCOOLDOWN = new UniPermission(PREFIX_COMMAND + "resetcooldown", "Access to the '/crate resetcooldown' sub-command.");
    public static final UniPermission COMMAND_RESETLIMIT = new UniPermission(PREFIX_COMMAND + "resetlimit", "Access to the '/crate resetlimit' sub-command.");
    public static final String PREFIX_BYPASS = PREFIX + "bypass.";
    public static final String PREFIX_BYPASS_OPEN_COST = PREFIX_BYPASS + "open.cost.";
    public static final UniPermission BYPASS_CRATE_OPEN_COST = new UniPermission(PREFIX_BYPASS_OPEN_COST + Placeholders.WILDCARD, "Bypasses all crate open costs.");
    public static final UniPermission BYPASS = new UniPermission(PREFIX_BYPASS + Placeholders.WILDCARD, "Bypass all the plugin restrictions.");
    public static final UniPermission BYPASS_CRATE_COOLDOWN = new UniPermission(PREFIX_BYPASS + "crate.opencooldown", "Bypasses crate open cooldown.");
    public static final UniPermission BYPASS_REWARD_LIMIT_AMOUNT = new UniPermission(PREFIX_BYPASS + "reward.limit.amount", "Bypasses reward's win limit amount.");
    public static final UniPermission BYPASS_REWARD_LIMIT_COOLDOWN = new UniPermission(PREFIX_BYPASS + "reward.limit.cooldown", "Bypasses reward's win limit cooldown.");
    public static final String PREFIX_CRATE = PREFIX + "crate.";
    public static final UniPermission PLUGIN = new UniPermission(PREFIX + Placeholders.WILDCARD, "Access to all the plugin functions.");
    public static final UniPermission MASS_OPEN = new UniPermission(PREFIX + "massopen", "Allows to use mass open feature.", TRUE);
    public static final UniPermission INCLUDE_KEY_GIVEALL = new UniPermission(PREFIX + "include.giveall", "Includes the player in the crate key giveall command.", TRUE);

    static {
        PLUGIN.addChildren(COMMAND, BYPASS, MASS_OPEN, INCLUDE_KEY_GIVEALL);

        COMMAND.addChildren(
                COMMAND_RELOAD,
                COMMAND_EDITOR,
                COMMAND_DROP,
                COMMAND_DROP_KEY,
                COMMAND_OPEN, COMMAND_OPEN_FOR,
                COMMAND_GIVE,
                COMMAND_KEY,
                COMMAND_KEY_GIVE,
                COMMAND_KEY_SET,
                COMMAND_KEY_INSPECT, COMMAND_KEY_INSPECT_OTHERS,
                COMMAND_KEY_TAKE,
                COMMAND_MENU, COMMAND_MENU_OTHERS,
                COMMAND_PREVIEW, COMMAND_PREVIEW_OTHERS,
                COMMAND_RESETCOOLDOWN,
                COMMAND_RESETLIMIT
        );

        BYPASS.addChildren(
                BYPASS_CRATE_COOLDOWN,
                BYPASS_CRATE_OPEN_COST,
                BYPASS_REWARD_LIMIT_AMOUNT,
                BYPASS_REWARD_LIMIT_COOLDOWN
        );
    }
}
