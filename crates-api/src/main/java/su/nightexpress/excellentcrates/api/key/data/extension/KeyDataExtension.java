package su.nightexpress.excellentcrates.api.key.data.extension;

import su.nightexpress.excellentcrates.api.key.CrateKey;
import su.nightexpress.excellentcrates.api.key.data.model.KeyBuilder;
import su.nightexpress.nightcore.config.FileConfig;

public interface KeyDataExtension {

    void onRead(FileConfig config, KeyBuilder builder);

    void onWrite(FileConfig config, CrateKey key);

    void onBuild(KeyBuilder builder);

    void onLoad(CrateKey key);

    void onUnload(CrateKey key);

    void onCreate(CrateKey key);

    void onDelete(CrateKey key);
}
