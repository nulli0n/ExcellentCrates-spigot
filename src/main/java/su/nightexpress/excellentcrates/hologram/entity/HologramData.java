package su.nightexpress.excellentcrates.hologram.entity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class HologramData {

    private final List<HologramEntity> entities;

    public HologramData() {
        this.entities = new ArrayList<>();
    }

    @NotNull
    public List<HologramEntity> getEntities() {
        return this.entities;
    }

    @NotNull
    public Set<Integer> getEntityIDs() {
        return this.entities.stream().map(HologramEntity::entityID).collect(Collectors.toSet());

    }
}
