package su.nightexpress.excellentcrates.api.crate.hologram;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.Crate;

@NullMarked
public interface HologramsAPI {

    void registerProvider(HologramProvider provider);

    void removeAll();

    void removeAll(Crate crate);

    void updateHolograms();

    void update(Crate crate);
}
