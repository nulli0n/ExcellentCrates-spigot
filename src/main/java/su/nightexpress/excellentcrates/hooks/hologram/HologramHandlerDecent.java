package su.nightexpress.excellentcrates.hooks.hologram;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.hook.AbstractHook;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.api.crate.ICrate;
import su.nightexpress.excellentcrates.api.crate.ICrateReward;
import su.nightexpress.excellentcrates.api.hook.HologramHandler;

import java.util.*;

public class HologramHandlerDecent extends AbstractHook<ExcellentCrates> implements HologramHandler {

    private Map<String, Set<Hologram>> holoCrates;
    private Map<Player, Hologram>      holoRewards;

    public HologramHandlerDecent(@NotNull ExcellentCrates plugin, @NotNull String pluginName) {
        super(plugin, pluginName);
    }

    @Override
    public boolean setup() {
        this.holoCrates = new HashMap<>();
        this.holoRewards = new WeakHashMap<>();
        return true;
    }

    @Override
    public void shutdown() {
        if (this.holoCrates != null) {
            this.holoCrates.values().forEach(set -> set.forEach(Hologram::delete));
            this.holoCrates.clear();
            this.holoCrates = null;
        }
        if (this.holoRewards != null) {
            this.holoRewards.values().forEach(Hologram::delete);
            this.holoRewards.clear();
            this.holoRewards = null;
        }
    }

    @Override
    public void create(@NotNull ICrate crate) {
        String id = "crate_" + crate.getId();

        crate.getBlockLocations().forEach(loc -> {
            Set<Hologram> holograms = this.holoCrates.computeIfAbsent(crate.getId(), set -> new HashSet<>());

            int size = holograms.size();
            Hologram hologram = DHAPI.createHologram(id + size, crate.getBlockHologramLocation(loc), crate.getBlockHologramText());
            holograms.add(hologram);
        });
    }

    @Override
    public void remove(@NotNull ICrate crate) {
        Set<Hologram> set = this.holoCrates.remove(crate.getId());
        if (set == null) return;

        set.forEach(Hologram::delete);
    }

    @Override
    public void createReward(@NotNull Player player, @NotNull ICrateReward reward, @NotNull Location location) {
        this.removeReward(player);

        ICrate crate = reward.getCrate();
        Hologram hologram = DHAPI.createHologram(crate.getId() + "_" + reward.getId(), location);
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
