package su.nightexpress.excellentcrates.crates.block;

import java.util.Set;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.id.IdentifiableRegistry;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.crate.block.BlockRegistry;
import su.nightexpress.excellentcrates.api.crate.block.provider.BlockProvider;

@NullMarked
public class BlockRepository implements BlockRegistry {

    private final IdentifiableRegistry<BlockProvider> blockProviders;

    public BlockRepository() {
        this.blockProviders = new IdentifiableRegistry<>();
    }

    @Override
    public void clear() {
        this.blockProviders.clear();
    }

    @Override
    public void registerProvider(BlockProvider provider) {
        this.blockProviders.register(provider);
    }

    @Override
    public @Nullable BlockProvider getProvider(Identifier id) {
        return this.blockProviders.get(id);
    }

    @Override
    public Set<BlockProvider> getProviders() {
        return this.blockProviders.values();
    }

    @Override
    public Set<Identifier> getProviderIds() {
        return this.blockProviders.ids();
    }
}
