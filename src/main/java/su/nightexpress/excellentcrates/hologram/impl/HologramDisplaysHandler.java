package su.nightexpress.excellentcrates.hologram.impl;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
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

public class HologramDisplaysHandler implements HologramHandler {

    private final ExcellentCratesPlugin      plugin;
    private final Map<String, Set<Hologram>> holoCrates;
    private final Map<Player, Hologram>      holoRewards;

    public HologramDisplaysHandler(@NotNull ExcellentCratesPlugin plugin) {
        this.plugin = plugin;
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
        String id = crate.getId();

        crate.getBlockLocations().forEach(location -> {
            Hologram hologram = HologramsAPI.createHologram(this.plugin, this.fineLocation(location));
            for (String line : crate.getHologramText()) {
                hologram.appendTextLine(line);
            }
            this.holoCrates.computeIfAbsent(id, set -> new HashSet<>()).add(hologram);
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
        Hologram hologram = HologramsAPI.createHologram(this.plugin, location);
        hologram.appendTextLine(reward.getName());
        hologram.appendItemLine(reward.getPreview());
        hologram.getVisibilityManager().setVisibleByDefault(false);
        hologram.getVisibilityManager().showTo(player);

        this.holoRewards.put(player, hologram);
    }

    @Override
    public void removeReward(@NotNull Player player) {
        Hologram hologram = this.holoRewards.remove(player);
        if (hologram != null) hologram.delete();
    }
}
