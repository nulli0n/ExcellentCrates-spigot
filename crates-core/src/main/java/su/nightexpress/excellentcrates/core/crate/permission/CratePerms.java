package su.nightexpress.excellentcrates.core.crate.permission;

import org.bukkit.permissions.Permission;

import su.nightexpress.excellentcrates.core.permission.Perms;
import su.nightexpress.nightcore.bridge.permission.PermissionNamespace;

public final class CratePerms {

    public static final PermissionNamespace ROOT    = Perms.ROOT.namespace("crates");
    public static final PermissionNamespace COMMAND = ROOT.namespace("command");
    public static final PermissionNamespace BYPASS  = ROOT.namespace("bypass");

    public static final PermissionNamespace EDITOR = ROOT.namespace("editor");

    public static final Permission COMMAND_ROOT        = COMMAND.create("root");
    public static final Permission COMMAND_EDITOR      = COMMAND.create("editor");
    public static final Permission COMMAND_OPEN        = COMMAND.create("open");
    public static final Permission COMMAND_OPEN_OTHERS = COMMAND.create("open.others");

    public static final Permission EDITOR_CORE = EDITOR.create("core");

    private CratePerms() {
    }
}
