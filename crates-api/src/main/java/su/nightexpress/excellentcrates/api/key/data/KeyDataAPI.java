package su.nightexpress.excellentcrates.api.key.data;

import java.util.function.Consumer;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.key.CrateKey;
import su.nightexpress.excellentcrates.api.key.data.extension.KeyDataExtension;
import su.nightexpress.excellentcrates.api.key.data.model.KeyBuilder;

@NullMarked
public interface KeyDataAPI {

    void registerExtension(KeyDataExtension extension);

    void loadKeys();

    void loadKey(CrateKey key);

    void unloadKey(CrateKey key);

    void unloadKeys();

    CrateKey createKey(Identifier id, Consumer<KeyBuilder> consumer);

    @Nullable
    CrateKey getKey(Identifier id);

    boolean hasKey(Identifier id);

    void saveKey(CrateKey key);

    void deleteKey(CrateKey key);

    void markDirty(CrateKey key);

    void saveDirty();

    void saveAll();
}
