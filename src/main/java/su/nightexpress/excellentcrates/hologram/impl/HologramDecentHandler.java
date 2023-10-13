package su.nightexpress.excellentcrates.hologram.impl;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.utils.LocationUtil;
import su.nightexpress.excellentcrates.ExcellentCratesPlugin;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.crate.impl.Reward;
import su.nightexpress.excellentcrates.hologram.HologramHandler;

import java.util.*;

public class HologramDecentHandler implements HologramHandler {

    private final Map<String, Set<Hologram>> holoCrates;
    private final Map<Player, Hologram>      holoRewards;

    public HologramDecentHandler(@NotNull ExcellentCratesPlugin plugin) {
        this.holoCrates = new HashMap<>();
        this.holoRewards = new WeakHashMap<>();
    }

    @Override
    public void setup() {

    }

    @Override
    public void shutdown() {
        this.holoCrates.values().forEach(set -> set.forEach(Hologram::delete));
        this.holoCrates.clear();
        this.holoRewards.values().forEach(Hologram::delete);
        this.holoRewards.clear();
    }

    @Override
    public void create(@NotNull Crate crate) {
        crate.getBlockLocations().forEach(location -> {
            Set<Hologram> holograms = this.holoCrates.computeIfAbsent(crate.getId(), set -> new HashSet<>());

            int size = holograms.size();
            Hologram hologram = DHAPI.createHologram(UUID.randomUUID().toString(), this.fineLocation(location), crate.getHologramText());
            holograms.add(hologram);
        });
    }

    @NotNull
    private Location fineLocation(@NotNull Location location) {
        return LocationUtil.getCenter(location.clone()).add(0D, Config.CRATE_HOLOGRAM_Y_OFFSET.get(), 0D);
    }

    @Override
    public void remove(@NotNull Crate crate) {
        Set<Hologram> set = this.holoCrates.remove(crate.getId());
        if (set == null) return;

        set.forEach(Hologram::delete);
    }

    @Override
    public void createReward(@NotNull Player player, @NotNull Reward reward, @NotNull Location location) {
        this.removeReward(player);

        Crate crate = reward.getCrate();
        Hologram hologram = DHAPI.createHologram(UUID.randomUUID().toString(), location);
        DHAPI.addHologramLine(hologram, reward.getName());
        DHAPI.addHologramLine(hologram, "#ICON: " + reward.getPreview().getType().name());
        hologram.hideAll();
        hologram.show(player, 0);

        this.holoRewards.put(player, hologram);
    }

    @Override
    public void removeReward(@NotNull Player player) {
        Hologram hologram = this.holoRewards.remove(player);
        if (hologram != null) hologram.delete();
    }
}
