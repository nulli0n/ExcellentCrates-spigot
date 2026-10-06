package su.nightexpress.excellentcrates.api.crate.data.extension;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.data.model.ICrateBuilder;
import su.nightexpress.nightcore.config.FileConfig;

@NullMarked
public interface CrateDataExtension {

    void onRead(FileConfig config, ICrateBuilder builder, Identifier crateId);

    void onWrite(FileConfig config, Crate crate);

    void onBuild(ICrateBuilder builder, Identifier crateId);

    void onLoad(Crate crate);

    void onUnload(Crate crate);

    void onCreate(Crate crate);

    void onDelete(Crate crate);
}
