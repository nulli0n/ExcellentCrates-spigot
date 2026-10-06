package su.nightexpress.excellentcrates.crates.data.crate;

import su.nightexpress.excellentcrates.api.crate.data.model.ICrateBase;

public class CrateBase implements ICrateBase {

    private boolean permissionRequired;

    public CrateBase(boolean permissionRequired) {
        this.permissionRequired = permissionRequired;
    }

    public static CrateBase createDefault() {
        return new CrateBase(false);
    }

    @Override
    public boolean isPermissionRequired() {
        return permissionRequired;
    }

    @Override
    public void setPermissionRequired(boolean permissionRequired) {
        this.permissionRequired = permissionRequired;
    }
}
