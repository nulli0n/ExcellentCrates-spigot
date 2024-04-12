package su.nightexpress.excellentcrates.crate.impl;

import org.jetbrains.annotations.NotNull;

public class OpenSettings {

    private boolean force;
    private boolean skipAnimation;
    private boolean saveData;

    public OpenSettings() {
        this.setForce(false);
        this.setSkipAnimation(false);
        this.setSaveData(true);
    }

    public boolean isForce() {
        return force;
    }

    @NotNull
    public OpenSettings setForce(boolean force) {
        this.force = force;
        return this;
    }

    public boolean isSkipAnimation() {
        return skipAnimation;
    }

    @NotNull
    public OpenSettings setSkipAnimation(boolean skipAnimation) {
        this.skipAnimation = skipAnimation;
        return this;
    }

    public boolean isSaveData() {
        return saveData;
    }

    @NotNull
    public OpenSettings setSaveData(boolean saveData) {
        this.saveData = saveData;
        return this;
    }
}
