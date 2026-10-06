package su.nightexpress.excellentcrates.core.permission;

import org.bukkit.permissions.Permission;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.bridge.permission.PermissionNamespace;

@NullMarked
public final class Perms {

    public static final PermissionNamespace ROOT    = PermissionNamespace.root("excellentcrates");
    public static final PermissionNamespace COMMAND = ROOT.namespace("command");

    public static final Permission COMMAND_RELOAD = COMMAND.create("reload");
    public static final Permission COMMAND_STATUS = COMMAND.create("status");

    private Perms() {
    }
}
