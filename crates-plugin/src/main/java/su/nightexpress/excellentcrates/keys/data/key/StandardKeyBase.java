package su.nightexpress.excellentcrates.keys.data.key;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.key.data.model.KeyBase;

@NullMarked
public class StandardKeyBase implements KeyBase {

    private boolean virtual;

    public StandardKeyBase(boolean virtual) {
        this.virtual = virtual;
    }

    public static StandardKeyBase createDefault() {
        return new StandardKeyBase(false);
    }

    @Override
    public boolean isVirtual() {
        return virtual;
    }

    @Override
    public void setVirtual(boolean virtual) {
        this.virtual = virtual;
    }
}
