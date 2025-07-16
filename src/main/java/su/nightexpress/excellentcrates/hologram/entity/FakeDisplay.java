package su.nightexpress.excellentcrates.hologram.entity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.util.pos.WorldPos;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FakeDisplay {

    private final Map<WorldPos, FakeEntityGroup> entityGroups;

    public FakeDisplay() {
        this.entityGroups = new HashMap<>();
    }

    @NotNull
    public FakeEntityGroup getGroupOrCreate(@NotNull WorldPos blockPos) {
        return this.entityGroups.computeIfAbsent(blockPos, k -> new FakeEntityGroup(blockPos));
    }

    @Nullable
    public FakeEntityGroup getGroup(@NotNull WorldPos blockPos) {
        return this.entityGroups.get(blockPos);
    }

    @NotNull
    public Set<FakeEntityGroup> getGroups() {
        return new HashSet<>(this.entityGroups.values());
    }
}
