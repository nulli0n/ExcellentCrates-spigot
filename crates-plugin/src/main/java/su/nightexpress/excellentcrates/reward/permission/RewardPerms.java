package su.nightexpress.excellentcrates.reward.permission;

import org.bukkit.permissions.Permission;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.core.permission.Perms;
import su.nightexpress.nightcore.bridge.permission.PermissionNamespace;

@NullMarked
public final class RewardPerms {

    public static final PermissionNamespace ROOT    = Perms.ROOT.namespace("rewards");
    public static final PermissionNamespace COMMAND = ROOT.namespace("command");
    public static final PermissionNamespace BYPASS  = ROOT.namespace("bypass");

    public static final Permission COMMAND_ROOT   = COMMAND.create("root");
    public static final Permission COMMAND_EDITOR = COMMAND.create("editor");

    private RewardPerms() {
    }
}
