package su.nightexpress.excellentcrates.api.crate.block.crate;

import java.util.Set;
import java.util.function.BiConsumer;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.component.CrateComponent;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;
import su.nightexpress.nightcore.util.geodata.pos.ExactPos;

@NullMarked
public interface BlockComponent extends CrateComponent {

    void clearBlockPositions();

    int countAllBlockPositions();

    void iterateBlockPositions(BiConsumer<AdaptedKey, Set<ExactPos>> consumer);

    Set<ExactPos> getBlockPositions(AdaptedKey worldKey);

    void addBlockPosition(AdaptedKey worldKey, ExactPos pos);

    void removeBlockPosition(AdaptedKey worldKey, ExactPos pos);
}
