package su.nightexpress.excellentcrates.crates.data.crate;

import su.nightexpress.excellentcrates.api.crate.data.model.CrateBase;

public class StandardCrateBase implements CrateBase {

    private boolean permissionRequired;

    public StandardCrateBase(boolean permissionRequired) {
        this.permissionRequired = permissionRequired;
    }

    public static StandardCrateBase createDefault() {
        return new StandardCrateBase(false);
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
