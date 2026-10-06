package su.nightexpress.excellentcrates.api.crate.block;

import java.util.Set;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.crate.block.provider.BlockProvider;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;

@NullMarked
public interface BlockRegistry {

    void clear();

    void registerProvider(BlockProvider<?> provider);

    void registerBlock(CrateBlock block);

    Set<CrateBlock> unregisterBlocks(BlockProvider<?> provider);

    @Nullable
    BlockProvider<?> getProvider(Identifier id);

    Set<BlockProvider<?>> getProviders();

    Set<Identifier> getProviderIds();

    @Nullable
    CrateBlock getBlock(AdaptedKey blockKey);

    Set<CrateBlock> getBlocks();

    Set<AdaptedKey> getBlockKeys();
}
