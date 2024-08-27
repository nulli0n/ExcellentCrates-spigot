package su.nightexpress.excellentcrates.hologram.impl;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.crate.impl.Reward;
import su.nightexpress.excellentcrates.hologram.HologramHandler;
import su.nightexpress.nightcore.util.LocationUtil;
import su.nightexpress.nightcore.util.text.NightMessage;

import java.util.*;

public class HologramDecentHandler implements HologramHandler {

    private final Map<String, Set<Hologram>> holoCrates;
    private final Map<Player, Hologram> holoRewards;

    public HologramDecentHandler(@NotNull CratesPlugin plugin) {
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
    public void refresh(@NotNull Crate crate) {
        this.remove(crate);
        this.create(crate);
    }

    @Override
    public void create(@NotNull Crate crate) {
        double yOffset = crate.getHologramYOffset();
        Set<Hologram> holograms = this.holoCrates.computeIfAbsent(crate.getId(), set -> new HashSet<>());

        crate.getBlockPositions().forEach(worldPos -> {
            if (!worldPos.isChunkLoaded()) return;

            Block block = worldPos.toBlock();
            if (block == null) return;

            double height = block.getBoundingBox().getHeight() + yOffset;
            Location location = LocationUtil.setCenter3D(block.getLocation()).add(0, height, 0);

            Hologram hologram = DHAPI.createHologram(UUID.randomUUID().toString(), location, NightMessage.asLegacy(crate.getHologramText()));
            holograms.add(hologram);
        });
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

        //Crate crate = reward.getCrate();
        Hologram hologram = DHAPI.createHologram(UUID.randomUUID().toString(), location);
        DHAPI.addHologramLine(hologram, reward.getName());
        DHAPI.addHologramLine(hologram, reward.getPreview());
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
