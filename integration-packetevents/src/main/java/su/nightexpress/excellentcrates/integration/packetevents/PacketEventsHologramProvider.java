package su.nightexpress.excellentcrates.integration.packetevents;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.settings.ReadOnlySettings;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.block.position.CratePosition;
import su.nightexpress.excellentcrates.api.crate.hologram.HologramProvider;
import su.nightexpress.excellentcrates.api.crate.hologram.HologramTextProvider;
import su.nightexpress.excellentcrates.integration.packetevents.entity.Hologram;
import su.nightexpress.excellentcrates.integration.packetevents.entity.HologramRepository;
import su.nightexpress.excellentcrates.integration.packetevents.settings.HologramSettings;
import su.nightexpress.nightcore.util.EntityUtil;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public class PacketEventsHologramProvider implements HologramProvider {

    private static final Identifier ID = new Identifier("packetevents");

    private final ReadOnlySettings<HologramSettings>  settings;
    private final HologramPacketManager               handler;
    private final Map<Identifier, HologramRepository> repositoryMap;

    public PacketEventsHologramProvider(ReadOnlySettings<HologramSettings> settings, HologramPacketManager handler) {
        this.settings = settings;
        this.handler = handler;
        this.repositoryMap = new ConcurrentHashMap<>();
    }

    @Override
    public Identifier getId() {
        return ID;
    }

    @Override
    public void removeAll() {
        this.repositoryMap.values()
            .forEach(display -> this.handler.broadcastDestroyEntityPacket(display.getHologramEntityIds()));
        this.repositoryMap.clear();
    }

    @Override
    public void remove(Crate crate) {
        HologramRepository repository = this.repositoryMap.remove(crate.id());
        if (repository == null) return;

        this.handler.broadcastDestroyEntityPacket(repository.getHologramEntityIds());
    }

    @Override
    public void render(Crate crate, CratePosition position, Location location, World world,
                       HologramTextProvider textProvider) {
        Hologram hologram = this.createIfAbsent(crate, position, location, world);

        double visibilityDistance = this.settings.get().visibleDistance();

        Collection<Player> players = world.getNearbyPlayers(location, visibilityDistance);
        if (players.isEmpty()) {
            this.removeHologram(hologram);
            return;
        }

        Set<Player> toRemove = hologram.getHumanViewers().stream()
            .map(Bukkit::getPlayer)
            .filter(Objects::nonNull)
            .filter(player -> !players.contains(player))
            .collect(Collectors.toSet());

        Set<Player> toRender = players.stream()
            .filter(player -> !this.isDisabledFor(crate, player))
            .collect(Collectors.toSet());

        toRemove.forEach(player -> this.removeForViewer(player, hologram));
        toRender.forEach(player -> this.renderHologram(player, hologram, textProvider));
    }

    public boolean isDisabledFor(Crate crate, Player player) {
        HologramRepository repository = this.repositoryMap.get(crate.id());
        return repository != null && repository.isIgnoredViewer(player.getUniqueId());
    }

    @Override
    public void disableFor(Crate crate, Player player) {
        HologramRepository repository = this.repositoryMap.get(crate.id());
        if (repository == null) return;

        repository.addIgnoredViewer(player.getUniqueId());
        this.removeForViewer(player, repository);
    }

    @Override
    public void enableFor(Crate crate, Player player) {
        HologramRepository repository = this.repositoryMap.get(crate.id());
        if (repository == null) return;

        repository.removeIgnoredViewer(player.getUniqueId());
    }

    public void removeForViewer(Player player) {
        this.repositoryMap.values().forEach(display -> this.removeForViewer(player, display));
    }

    public void removeForViewer(Player player, HologramRepository repository) {
        repository.getAll().forEach(hologram -> this.removeForViewer(player, hologram));
    }

    public void removeForViewer(Player player, Hologram hologram) {
        hologram.removeViewer(player);
        this.handler.sendDestroyEntityPacket(player, Collections.singletonList(hologram.getEntityId()));
    }

    private void removeHologram(Hologram hologram) {
        hologram.clearViewers();

        this.handler.broadcastDestroyEntityPacket(Collections.singletonList(hologram.getEntityId()));
    }

    private void renderHologram(Player player, Hologram hologram, HologramTextProvider textProvider) {
        boolean needSpawn = !hologram.isViewer(player);

        List<String> hologramText = textProvider.getText(player);

        String text = String.join(TagWrappers.BR, hologramText);
        this.handler.sendHologramPackets(player, hologram, needSpawn, text);

        hologram.addViewer(player);
    }

    private Hologram createIfAbsent(Crate crate, CratePosition position, Location location, World world) {
        HologramRepository repository = this.repositoryMap.computeIfAbsent(crate.id(), k -> new HologramRepository());
        Hologram hologram = repository.getHologram(position);
        if (hologram != null) return hologram;

        int entityId = EntityUtil.nextEntityId(world);
        Hologram newHologram = new Hologram(entityId, location);
        repository.addHologram(position, newHologram);

        return newHologram;
    }
}
