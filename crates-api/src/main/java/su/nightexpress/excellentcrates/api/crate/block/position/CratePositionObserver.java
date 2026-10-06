package su.nightexpress.excellentcrates.api.crate.block.position;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;

@NullMarked
public interface CratePositionObserver {

    /**
     * Fired immediately after a crate position is registered in the mapper.
     */
    void onPositionAdded(Identifier crateId, CratePosition position);

    /**
     * Fired immediately after a crate position is removed from the mapper.
     */
    void onPositionRemoved(Identifier crateId, CratePosition position);
}