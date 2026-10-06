package su.nightexpress.excellentcrates.crates.block.component;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.block.crate.BlockComponent;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;
import su.nightexpress.nightcore.util.geodata.pos.ExactPos;

@NullMarked
public class DefaultBlockComponent implements BlockComponent {

    private final Map<AdaptedKey, Set<ExactPos>> blockPositions;

    public DefaultBlockComponent(Map<AdaptedKey, Set<ExactPos>> blockPositions) {
        this.blockPositions = new HashMap<>(blockPositions);
    }

    public static DefaultBlockComponent empty() {
        return new DefaultBlockComponent(Map.of());
    }

    @Override
    public void clearBlockPositions() {
        this.blockPositions.clear();
    }

    @Override
    public int countAllBlockPositions() {
        return this.blockPositions.values().stream().mapToInt(Set::size).sum();
    }

    @Override
    public void iterateBlockPositions(BiConsumer<AdaptedKey, Set<ExactPos>> consumer) {
        this.blockPositions.forEach(consumer);
    }

    @Override
    public Set<ExactPos> getBlockPositions(AdaptedKey worldKey) {
        return Collections.unmodifiableSet(this.blockPositions.getOrDefault(worldKey, Set.of()));
    }

    @Override
    public void addBlockPosition(AdaptedKey worldKey, ExactPos pos) {
        Set<ExactPos> positions = this.blockPositions.computeIfAbsent(worldKey, k -> new HashSet<>());
        positions.add(pos);
    }

    @Override
    public void removeBlockPosition(AdaptedKey worldKey, ExactPos pos) {
        Set<ExactPos> positions = this.blockPositions.get(worldKey);
        if (positions == null) return;

        positions.remove(pos);
    }
}
