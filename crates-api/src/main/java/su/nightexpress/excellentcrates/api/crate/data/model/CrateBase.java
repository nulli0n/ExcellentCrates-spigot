package su.nightexpress.excellentcrates.api.crate.data.model;

import org.jspecify.annotations.NullMarked;

@NullMarked
public interface CrateBase {

    boolean isPermissionRequired();

    void setPermissionRequired(boolean permissionRequired);
}
