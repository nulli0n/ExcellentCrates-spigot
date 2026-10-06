package su.nightexpress.excellentcrates.crates.block;

import java.util.Comparator;
import java.util.Optional;

import org.bukkit.Location;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.block.BlockRegistry;
import su.nightexpress.excellentcrates.api.crate.block.provider.BlockProvider;

@NullMarked
public class BlockResolveService {

    private final BlockRegistry registry;

    public BlockResolveService(BlockRegistry registry) {
        this.registry = registry;
    }

    public Optional<BlockProvider<?>> resolveProvider(Location location) {
        return this.registry.getProviders().stream()
            .filter(provider -> provider.canHandle(location))
            .sorted(Comparator.comparingInt((BlockProvider<?> b) -> b.getPriority()).reversed())
            .findFirst();
    }
}
