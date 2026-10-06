package su.nightexpress.excellentcrates.keys.permission;

import org.bukkit.permissions.Permission;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.core.permission.Perms;
import su.nightexpress.nightcore.bridge.permission.PermissionNamespace;

@NullMarked
public final class KeyPerms {

    public static final PermissionNamespace ROOT    = Perms.ROOT.namespace("keys");
    public static final PermissionNamespace COMMAND = ROOT.namespace("command");
    public static final PermissionNamespace BYPASS  = ROOT.namespace("bypass");

    public static final Permission AUTO_REDEEM = ROOT.create("auto.redeem");

    public static final Permission COMMAND_ROOT              = COMMAND.create("root");
    public static final Permission COMMAND_EDITOR            = COMMAND.create("editor");
    public static final Permission COMMAND_BALANCE           = COMMAND.create("balance");
    public static final Permission COMMAND_BALANCE_OTHERS    = COMMAND.create("balance.others");
    public static final Permission COMMAND_GET               = COMMAND.create("get");
    public static final Permission COMMAND_GIVE              = COMMAND.create("give");
    public static final Permission COMMAND_GIVE_ALL          = COMMAND.create("giveall");
    public static final Permission COMMAND_GIVE_ALL_INCLUDED = COMMAND.create("giveall.included");
    public static final Permission COMMAND_DROP              = COMMAND.create("drop");
    public static final Permission COMMAND_REMOVE            = COMMAND.create("remove");
    public static final Permission COMMAND_REDEEM            = COMMAND.create("redeem");

    private KeyPerms() {
    }
}
