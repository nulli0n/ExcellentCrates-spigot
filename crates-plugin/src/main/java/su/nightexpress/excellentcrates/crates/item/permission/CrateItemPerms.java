package su.nightexpress.excellentcrates.crates.item.permission;

import org.bukkit.permissions.Permission;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.core.crate.permission.CratePerms;
import su.nightexpress.nightcore.bridge.permission.PermissionNamespace;

@NullMarked
public final class CrateItemPerms {

    public static final PermissionNamespace ROOT    = CratePerms.ROOT.namespace("item");
    public static final PermissionNamespace COMMAND = ROOT.namespace("command");

    public static final Permission COMMAND_GET  = COMMAND.create("get");
    public static final Permission COMMAND_GIVE = COMMAND.create("give");
    public static final Permission COMMAND_DROP = COMMAND.create("drop");

    private CrateItemPerms() {
    }
}
