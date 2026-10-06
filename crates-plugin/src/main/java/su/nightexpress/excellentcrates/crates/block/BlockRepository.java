package su.nightexpress.excellentcrates.crates.block;

import java.util.Set;
import java.util.stream.Collectors;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.id.IdentifiableRegistry;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.crate.block.BlockRegistry;
import su.nightexpress.excellentcrates.api.crate.block.CrateBlock;
import su.nightexpress.excellentcrates.api.crate.block.provider.BlockProvider;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;
import su.nightexpress.nightcore.bridge.key.KeyedRegistry;

@NullMarked
public class BlockRepository implements BlockRegistry {

    private final IdentifiableRegistry<BlockProvider<?>> blockProviders;
    private final KeyedRegistry<CrateBlock>              blocks;

    public BlockRepository() {
        this.blockProviders = new IdentifiableRegistry<>();
        this.blocks = new KeyedRegistry<>();
    }

    @Override
    public void clear() {
        this.blockProviders.clear();
        this.blocks.clear();
    }

    @Override
    public void registerProvider(BlockProvider<?> provider) {
        this.blockProviders.register(provider);
    }

    @Override
    public void registerBlock(CrateBlock block) {
        this.blocks.register(block);
    }

    @Override
    public Set<CrateBlock> unregisterBlocks(BlockProvider<?> provider) {
        Set<CrateBlock> blocksToRemove = this.blocks.values().stream()
            .filter(block -> block.key().namespace().equals(provider.idString()))
            .collect(Collectors.toSet());

        blocksToRemove.forEach(this.blocks::remove);

        return blocksToRemove;
    }

    @Override
    public @Nullable BlockProvider<?> getProvider(Identifier id) {
        return this.blockProviders.get(id);
    }

    @Override
    public Set<BlockProvider<?>> getProviders() {
        return this.blockProviders.values();
    }

    @Override
    public Set<Identifier> getProviderIds() {
        return this.blockProviders.ids();
    }

    @Override
    public @Nullable CrateBlock getBlock(AdaptedKey blockKey) {
        return this.blocks.get(blockKey);
    }

    @Override
    public Set<AdaptedKey> getBlockKeys() {
        return this.blocks.keys();
    }

    @Override
    public Set<CrateBlock> getBlocks() {
        return this.blocks.values();
    }
}
