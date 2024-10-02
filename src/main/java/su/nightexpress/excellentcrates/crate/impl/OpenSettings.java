package su.nightexpress.excellentcrates.crate.impl;

import org.jetbrains.annotations.NotNull;

public class OpenSettings {

    private boolean force;
    private boolean skipAnimation;

    public OpenSettings() {
        this.setForce(false);
        this.setSkipAnimation(false);
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
}
