package su.nightexpress.excellentcrates.hologram.impl;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataValue;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.crate.impl.Reward;
import su.nightexpress.excellentcrates.hologram.HologramHandler;
import su.nightexpress.excellentcrates.hooks.HookId;
import su.nightexpress.nightcore.util.EntityUtil;
import su.nightexpress.nightcore.util.LocationUtil;
import su.nightexpress.nightcore.util.Pair;
import su.nightexpress.nightcore.util.Plugins;

import java.util.*;
import java.util.stream.IntStream;

public class HologramInternalHandler implements HologramHandler {

    private final CratesPlugin                               plugin;
    private final Map<String, Pair<Set<Integer>, Set<Integer>>> entityIdMap;

    private boolean disabled;

    public HologramInternalHandler(@NotNull CratesPlugin plugin) {
        this.plugin = plugin;
        this.entityIdMap = new HashMap<>();
    }

    @Override
    public void setup() {
        this.disabled = false;
    }

    @Override
    public void shutdown() {
        new HashSet<>(this.entityIdMap.keySet()).forEach(this::remove);
        this.entityIdMap.clear();
        this.disabled = true;
    }

    @Override
    public void create(@NotNull Crate crate) {
        if (this.disabled) return;

        this.remove(crate);

        Set<Integer> standIds = new HashSet<>();
        Set<Integer> itemIds = new HashSet<>();
        List<String> text = crate.getHologramText();
        Collections.reverse(text);

        double yOffset = crate.getHologramYOffset();

        for (Location location : crate.getBlockLocations()) {
            World world = location.getWorld();
            if (world == null || world.getPlayers().isEmpty()) continue;

            double height = location.getBlock().getBoundingBox().getHeight() / 2D + yOffset;
            Location pos = LocationUtil.getCenter(location.clone()).add(0, height, 0);

            for (String line : text) {
                try {
                    standIds.add(this.spawnHologram(world, pos.clone(), line));
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                    this.plugin.error("An error occured when trying to create crate hologram!");
                    this.plugin.error("THIS IS AN ISSUE CAUSED BY " + HookId.PROTOCOL_LIB + " PLUGIN!");
                    this.plugin.error("DO NOT REPORT THIS TO ExcellentCrates!");
                    this.plugin.error("Holograms will be disabled until server reboot or a plugin reload.");
                    this.destroyEntity(standIds);
                    this.disabled = true;
                    return;
                }
                pos = pos.add(0, Config.CRATE_HOLOGRAM_LINE_GAP.get(), 0);
            }
        }

        this.entityIdMap.put(crate.getId(), Pair.of(standIds, itemIds));
    }

    @Override
    public void remove(@NotNull Crate crate) {
        this.remove(crate.getId());
    }

    private void remove(@NotNull String id) {
        var pair = this.entityIdMap.remove(id);
        if (pair == null) return;

        pair.getFirst().forEach(this::destroyEntity);
        pair.getSecond().forEach(this::destroyEntity);
    }

    @Override
    public void createReward(@NotNull Player player, @NotNull Reward reward, @NotNull Location location) {

    }

    @Override
    public void removeReward(@NotNull Player player) {

    }

    private int spawnHologram(@NotNull World world, @NotNull Location location, @NotNull String name) {
        int entityID = EntityUtil.nextEntityId();

        PacketContainer spawnPacket = new PacketContainer(PacketType.Play.Server.SPAWN_ENTITY);
        spawnPacket.getIntegers().write(0, entityID);
        spawnPacket.getUUIDs().write(0, UUID.randomUUID());
        spawnPacket.getEntityTypeModifier().write(0, org.bukkit.entity.EntityType.ARMOR_STAND);
        spawnPacket.getDoubles().write(0, location.getX());
        spawnPacket.getDoubles().write(1, location.getY());
        spawnPacket.getDoubles().write(2, location.getZ());

        // Metadata handle
        world.getPlayers().forEach(player -> {
            PacketContainer dataPacket = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
            WrappedDataWatcher metadata = new WrappedDataWatcher();

            String text = name;
            if (Plugins.hasPlaceholderAPI()) {
                text = PlaceholderAPI.setPlaceholders(player, text);
            }

            Optional<?> opt = Optional.of(WrappedChatComponent.fromChatMessage(text)[0].getHandle());
            metadata.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(0, WrappedDataWatcher.Registry.get(Byte.class)), (byte) 0x20); //invis
            metadata.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(2, WrappedDataWatcher.Registry.getChatComponentSerializer(true)), opt); //display name
            metadata.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(3, WrappedDataWatcher.Registry.get(Boolean.class)), true); //custom name visible
            metadata.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(5, WrappedDataWatcher.Registry.get(Boolean.class)), true); //no gravity
            metadata.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(15, WrappedDataWatcher.Registry.get(Byte.class)), (byte) (0x01 | 0x08 | 0x10)); //isSmall, noBasePlate, set Marker

            List<WrappedDataValue> wrappedDataValueList = new ArrayList<>();
            metadata.getWatchableObjects().stream().filter(Objects::nonNull).forEach(entry -> {
                WrappedDataWatcher.WrappedDataWatcherObject dataWatcherObject = entry.getWatcherObject();
                wrappedDataValueList.add(new WrappedDataValue(dataWatcherObject.getIndex(), dataWatcherObject.getSerializer(), entry.getRawValue()));
            });

            dataPacket.getDataValueCollectionModifier().write(0, wrappedDataValueList);
            dataPacket.getIntegers().write(0, entityID);

            ProtocolLibrary.getProtocolManager().sendServerPacket(player, spawnPacket);
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, dataPacket);
        });

        return entityID;
    }

    private void destroyEntity(int... ids) {
        this.destroyEntity(IntStream.of(ids).boxed().toList());
    }

    private void destroyEntity(Collection<Integer> ids) {
        PacketContainer destroyPacket = new PacketContainer(PacketType.Play.Server.ENTITY_DESTROY);
        destroyPacket.getIntLists().write(0, new ArrayList<>(ids));
        ProtocolLibrary.getProtocolManager().broadcastServerPacket(destroyPacket);
    }
}
