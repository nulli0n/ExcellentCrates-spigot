package su.nightexpress.excellentcrates.hologram.entity;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import java.util.concurrent.atomic.AtomicInteger;

public class FakeEntity {

    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(Integer.MAX_VALUE);

    private final int      id;
    private final Location location;

    public FakeEntity(int id, @NotNull Location location) {
        this.id = id;
        this.location = location;
    }

    @NotNull
    public static FakeEntity create(@NotNull Location location) {
        return new FakeEntity(ID_GENERATOR.decrementAndGet(), location);
    }

    public int getId() {
        return this.id;
    }

    @NotNull
    public Location getLocation() {
        return this.location;
    }
}
