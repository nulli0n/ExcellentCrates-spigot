package su.nightexpress.excellentcrates.config;

import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.nightcore.util.wrapper.UniPermission;

import static org.bukkit.permissions.PermissionDefault.*;

public class Perms {

    public static final String PREFIX                  = "excellentcrates.";
    public static final String PREFIX_COMMAND          = PREFIX + "command.";
    public static final String PREFIX_BYPASS           = PREFIX + "bypass.";
    public static final String PREFIX_CRATE            = PREFIX + "crate.";

    public static final UniPermission PLUGIN  = new UniPermission(PREFIX + Placeholders.WILDCARD);
    public static final UniPermission COMMAND = new UniPermission(PREFIX_COMMAND + Placeholders.WILDCARD);
    public static final UniPermission BYPASS  = new UniPermission(PREFIX_BYPASS + Placeholders.WILDCARD);

    public static final UniPermission MASS_OPEN = new UniPermission(PREFIX + "massopen");

    public static final UniPermission INCLUDE_KEY_GIVEALL = new UniPermission(PREFIX + "include.giveall", "Includes the player in the crate key giveall command.", TRUE);

    public static final UniPermission COMMAND_RELOAD             = new UniPermission(PREFIX_COMMAND + "reload");
    public static final UniPermission COMMAND_EDITOR             = new UniPermission(PREFIX_COMMAND + "editor");
    public static final UniPermission COMMAND_DROP               = new UniPermission(PREFIX_COMMAND + "drop");
    public static final UniPermission COMMAND_DROP_KEY           = new UniPermission(PREFIX_COMMAND + "dropkey");
    public static final UniPermission COMMAND_OPEN               = new UniPermission(PREFIX_COMMAND + "open");
    public static final UniPermission COMMAND_OPEN_FOR           = new UniPermission(PREFIX_COMMAND + "openfor");
    public static final UniPermission COMMAND_GIVE               = new UniPermission(PREFIX_COMMAND + "give");
    public static final UniPermission COMMAND_KEY                = new UniPermission(PREFIX_COMMAND + "key");
    public static final UniPermission COMMAND_KEY_GIVE           = new UniPermission(PREFIX_COMMAND + "key.give");
    public static final UniPermission COMMAND_KEY_TAKE           = new UniPermission(PREFIX_COMMAND + "key.take");
    public static final UniPermission COMMAND_KEY_SET            = new UniPermission(PREFIX_COMMAND + "key.set");
    public static final UniPermission COMMAND_KEY_INSPECT        = new UniPermission(PREFIX_COMMAND + "key.show");
    public static final UniPermission COMMAND_KEY_INSPECT_OTHERS = new UniPermission(PREFIX_COMMAND + "key.show.others");
    public static final UniPermission COMMAND_MENU               = new UniPermission(PREFIX_COMMAND + "menu");
    public static final UniPermission COMMAND_MENU_OTHERS        = new UniPermission(PREFIX_COMMAND + "menu.others");
    public static final UniPermission COMMAND_PREVIEW            = new UniPermission(PREFIX_COMMAND + "preview");
    public static final UniPermission COMMAND_PREVIEW_OTHERS     = new UniPermission(PREFIX_COMMAND + "preview.others");
    public static final UniPermission COMMAND_RESETCOOLDOWN      = new UniPermission(PREFIX_COMMAND + "resetcooldown");

    public static final UniPermission BYPASS_CRATE_COOLDOWN  = new UniPermission(PREFIX_BYPASS + "crate.opencooldown");

    static {
        PLUGIN.addChildren(COMMAND, BYPASS, MASS_OPEN, INCLUDE_KEY_GIVEALL);

        COMMAND.addChildren(
            COMMAND_RELOAD,
            COMMAND_EDITOR,
            COMMAND_DROP,
            COMMAND_DROP_KEY,
            COMMAND_OPEN,
            COMMAND_OPEN_FOR,
            COMMAND_GIVE,
            COMMAND_KEY,
            COMMAND_KEY_GIVE,
            COMMAND_KEY_SET,
            COMMAND_KEY_INSPECT,
            COMMAND_KEY_INSPECT_OTHERS,
            COMMAND_KEY_TAKE,
            COMMAND_MENU,
            COMMAND_MENU_OTHERS,
            COMMAND_PREVIEW,
            COMMAND_PREVIEW_OTHERS,
            COMMAND_RESETCOOLDOWN
        );

        BYPASS.addChildren(
            BYPASS_CRATE_COOLDOWN
        );
    }
}
