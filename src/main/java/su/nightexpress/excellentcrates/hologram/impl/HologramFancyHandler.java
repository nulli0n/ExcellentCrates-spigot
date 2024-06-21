package su.nightexpress.excellentcrates.hologram.impl;

import de.oliver.fancyholograms.api.FancyHologramsPlugin;
import de.oliver.fancyholograms.api.HologramManager;
import de.oliver.fancyholograms.api.data.ItemHologramData;
import de.oliver.fancyholograms.api.data.TextHologramData;
import de.oliver.fancyholograms.api.hologram.Hologram;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.crate.impl.Reward;
import su.nightexpress.excellentcrates.hologram.HologramHandler;
import su.nightexpress.nightcore.util.LocationUtil;
import su.nightexpress.nightcore.util.Pair;

import java.util.*;

public class HologramFancyHandler implements HologramHandler {

    private HologramManager                                  hologramManager;
    private LegacyComponentSerializer                        legacyComponentSerializer;
    private final Map<String, Set<Hologram>>                 holoCrates;
    private final Map<Player, Pair<Hologram, Hologram>>      holoRewards;

    public HologramFancyHandler(@NotNull CratesPlugin plugin) {
        this.holoCrates = new HashMap<>();
        this.holoRewards = new WeakHashMap<>();
    }

    @Override
    public void setup() {
        hologramManager = FancyHologramsPlugin.get().getHologramManager();
        legacyComponentSerializer = LegacyComponentSerializer.builder()
                .useUnusualXRepeatedCharacterHexFormat()
                .hexColors()
                .build();
    }

    @Override
    public void shutdown() {
        this.holoCrates.values().forEach(set -> set.forEach(hologramManager::removeHologram));
        this.holoCrates.clear();
        this.holoRewards.values().forEach(pair -> {
            hologramManager.removeHologram(pair.getFirst());
            hologramManager.removeHologram(pair.getSecond());
        });
        this.holoRewards.clear();
    }

    @Override
    public void create(@NotNull Crate crate) {
        crate.getBlockLocations().forEach(location -> {
            Set<Hologram> holograms = this.holoCrates.computeIfAbsent(crate.getId(), set -> new HashSet<>());

            double height = location.getBlock().getBoundingBox().getHeight() + crate.getHologramYOffset();
            Location pos = LocationUtil.getCenter(location.clone()).add(0, height, 0);

            TextHologramData hologramData = new TextHologramData(UUID.randomUUID().toString(), pos);
            List<String> text = crate.getHologramText().stream()
                    .map(legacyComponentSerializer::deserialize)
                    .map(MiniMessage.miniMessage()::serialize)
                    .toList();
            hologramData.setText(text);
            hologramData.setBackground(Hologram.TRANSPARENT);
            hologramData.setPersistent(false);
            Hologram hologram = hologramManager.create(hologramData); // create hologram object
            hologram.createHologram();                                // spawn hologram entity
            hologramManager.addHologram(hologram);                    // add hologram object to manager
            holograms.add(hologram);
        });
    }

    @Override
    public void remove(@NotNull Crate crate) {
        Set<Hologram> set = this.holoCrates.remove(crate.getId());
        if (set == null) return;

        set.forEach(hologramManager::removeHologram);
    }

    @Override
    public void createReward(@NotNull Player player, @NotNull Reward reward, @NotNull Location location) {
        this.removeReward(player);

        TextHologramData textHologramData = new TextHologramData(UUID.randomUUID().toString(), location);
        textHologramData.setText(List.of(reward.getName()));
        textHologramData.setBackground(Hologram.TRANSPARENT);
        textHologramData.setPersistent(false);
        Hologram textHologram = hologramManager.create(textHologramData);
        textHologram.createHologram();
        textHologram.showHologram(player);

        ItemHologramData itemHologramData = new ItemHologramData(UUID.randomUUID().toString(), location);
        itemHologramData.setItemStack(reward.getPreview());
        itemHologramData.setPersistent(false);
        Hologram itemHologram = hologramManager.create(itemHologramData);
        itemHologram.createHologram();
        itemHologram.showHologram(player);

        this.holoRewards.put(player, new Pair<>(textHologram, itemHologram));
    }

    @Override
    public void removeReward(@NotNull Player player) {
        Pair<Hologram, Hologram> holograms = this.holoRewards.remove(player);
        if (holograms == null) return;
        hologramManager.removeHologram(holograms.getFirst());
        hologramManager.removeHologram(holograms.getSecond());
    }

}
