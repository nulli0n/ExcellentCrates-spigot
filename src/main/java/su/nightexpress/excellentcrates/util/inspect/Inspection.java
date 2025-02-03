package su.nightexpress.excellentcrates.util.inspect;

import org.jetbrains.annotations.NotNull;

public interface Inspection<T> {

    @NotNull String name();

    @NotNull InspectionInfo inspect(@NotNull T object);
}
