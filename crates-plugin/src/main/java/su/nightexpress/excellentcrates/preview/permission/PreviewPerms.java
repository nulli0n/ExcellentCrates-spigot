package su.nightexpress.excellentcrates.preview.permission;

import org.bukkit.permissions.Permission;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.core.permission.Perms;
import su.nightexpress.nightcore.bridge.permission.PermissionNamespace;

@NullMarked
public final class PreviewPerms {

    public static final PermissionNamespace ROOT    = Perms.ROOT.namespace("preview");
    public static final PermissionNamespace COMMAND = ROOT.namespace("command");

    public static final Permission COMMAND_PREVIEW        = COMMAND.create("preview");
    public static final Permission COMMAND_PREVIEW_OTHERS = COMMAND.create("preview.others");

    private PreviewPerms() {
    }
}
