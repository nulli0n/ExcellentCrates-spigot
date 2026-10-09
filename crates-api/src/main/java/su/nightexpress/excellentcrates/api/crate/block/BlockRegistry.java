package su.nightexpress.excellentcrates.api.crate.block;

import java.util.Set;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.crate.block.provider.BlockProvider;

@NullMarked
public interface BlockRegistry {

    void clear();

    void registerProvider(BlockProvider provider);

    @Nullable
    BlockProvider getProvider(Identifier id);

    Set<BlockProvider> getProviders();

    Set<Identifier> getProviderIds();
}
