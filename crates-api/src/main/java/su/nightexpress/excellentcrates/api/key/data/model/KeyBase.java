package su.nightexpress.excellentcrates.api.key.data.model;

import org.jspecify.annotations.NullMarked;

@NullMarked
public interface KeyBase {

    boolean isVirtual();

    void setVirtual(boolean virtual);
}
