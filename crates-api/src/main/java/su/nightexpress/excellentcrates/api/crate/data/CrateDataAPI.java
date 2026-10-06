package su.nightexpress.excellentcrates.api.crate.data;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.data.extension.CrateDataExtension;

@NullMarked
public interface CrateDataAPI {

    void registerExtension(CrateDataExtension extension);

    void markDirty(Crate crate);
}
