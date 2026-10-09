package su.nightexpress.excellentcrates.crates.block.permission;

import org.bukkit.permissions.Permission;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.core.permission.Perms;
import su.nightexpress.nightcore.bridge.permission.PermissionNamespace;

@NullMarked
public final class BlocksPerms {

    public static final PermissionNamespace ROOT    = Perms.ROOT.namespace("blocks");
    public static final PermissionNamespace COMMAND = ROOT.namespace("command");

    public static final Permission COMMAND_ASSIGN = COMMAND.create("assign");

    public static final Permission BLOCK_PLACE  = ROOT.create("block.place");
    public static final Permission BLOCK_REMOVE = ROOT.create("block.remove");

    private BlocksPerms() {
    }
}
