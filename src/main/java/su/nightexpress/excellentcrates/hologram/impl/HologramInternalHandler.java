package su.nightexpress.excellentcrates.hologram.impl;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataValue;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.crate.impl.Reward;
import su.nightexpress.excellentcrates.hologram.HologramHandler;
import su.nightexpress.excellentcrates.util.pos.WorldPos;
import su.nightexpress.nightcore.util.*;
import su.nightexpress.nightcore.util.text.NightMessage;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class HologramInternalHandler implements HologramHandler {

    private final CratesPlugin        plugin;
    private final ProtocolManager     protocolManager;
    private final Map<String, IdList> entityIdMap;

    private boolean useDisplays;
    private double  lineGap;

    public HologramInternalHandler(@NotNull CratesPlugin plugin) {
        this.plugin = plugin;
        this.protocolManager = ProtocolLibrary.getProtocolManager();
        this.entityIdMap = new HashMap<>();
    }

    public static class IdList {

        private final Set<Integer> holoramIds;
        private final Set<Integer> itemIds;

        public IdList() {
            this.holoramIds = ConcurrentHashMap.newKeySet();
            this.itemIds = ConcurrentHashMap.newKeySet();
        }

        @NotNull
        public Set<Integer> getAll() {
            Set<Integer> set = new HashSet<>();
            set.addAll(this.holoramIds);
            set.addAll(this.itemIds);
            return set;
        }

        public void clear() {
            this.holoramIds.clear();
            this.itemIds.clear();
        }
    }

    private record PacketBundle(Player player, List<PacketContainer> containers) {

        public void add(PacketContainer container) {
            this.containers.add(container);
        }
    }

    private enum Action {
        REMOVE, ADD, REFRESH
    }

    @Override
    public void setup() {
        this.useDisplays = Config.CRATE_HOLOGRAM_USE_DISPLAYS.get() && Version.isAtLeast(Version.V1_20_R3);
        this.lineGap = Config.CRATE_HOLOGRAM_LINE_GAP.get();
    }

    @Override
    public void shutdown() {
        this.entityIdMap.values().forEach(this::destroyEntity);
        this.entityIdMap.clear();
    }

    @Override
    public void refresh(@NotNull Crate crate) {
        this.peform(crate, Action.REFRESH);
    }

    @Override
    public void create(@NotNull Crate crate) {
        this.peform(crate, Action.ADD);
//        Set<WorldPos> blockPositions = crate.getBlockPositions();
//        if (blockPositions.isEmpty()) return;
//
//        EntityIdList idList = this.entityIdMap.computeIfAbsent(crate.getId(), k -> new EntityIdList(Lists.newSet(), Lists.newSet()));
//
//        List<String> text = crate.getHologramText();
//        Collections.reverse(text);
//
//        double yOffset = crate.getHologramYOffset();
//        if (this.useDisplays) yOffset += 0.25;
//
//        for (WorldPos worldPos : blockPositions) {
//            if (!worldPos.isChunkLoaded()) continue;
//
//            World world = worldPos.getWorld();
//            Block block = worldPos.toBlock();
//            if (world == null || block == null || world.getPlayers().isEmpty()) continue;
//
//            double height = block.getBoundingBox().getHeight() / 2D + yOffset;
//            Location location = LocationUtil.setCenter3D(block.getLocation()).add(0, height, 0);
//
//            for (String line : text) {
//                standIds.add(this.spawnHologram(world, location.clone(), line));
//                location = location.add(0, this.lineGap, 0);
//            }
//        }
    }

    @Override
    public void remove(@NotNull Crate crate) {
        this.peform(crate, Action.REMOVE);
    }

    private void peform(@NotNull Crate crate, @NotNull Action action) {
        IdList idList = this.entityIdMap.computeIfAbsent(crate.getId(), k -> new IdList());
        Set<Player> players = new HashSet<>(this.plugin.getServer().getOnlinePlayers());

        List<String> originText = new ArrayList<>();
        List<Location> locations = new ArrayList<>();
        Collection<Integer> currentIds = new HashSet<>();

        if (action == Action.REMOVE || action == Action.REFRESH) {
            currentIds.addAll(idList.holoramIds);
            idList.holoramIds.clear();
        }

        if (action == Action.ADD || action == Action.REFRESH) {
            originText.addAll(crate.getHologramText());
            Collections.reverse(originText);

            double yOffset = crate.getHologramYOffset();
            if (this.useDisplays) yOffset += 0.25;

            for (WorldPos worldPos : crate.getBlockPositions()) {
                if (!worldPos.isChunkLoaded()) continue;

                World world = worldPos.getWorld();
                Block block = worldPos.toBlock();
                if (world == null || block == null) continue;

                double height = block.getBoundingBox().getHeight() / 2D + yOffset;
                locations.add(LocationUtil.setCenter3D(block.getLocation()).add(0, height, 0));
            }
        }

        List<PacketBundle> bundles = new ArrayList<>();

        players.forEach(player -> {
            PacketBundle bundle = new PacketBundle(player, new ArrayList<>());

            if (action == Action.REMOVE || action == Action.REFRESH) {
                bundle.add(this.createDestroyPacket(currentIds));
            }

            if (action == Action.ADD || action == Action.REFRESH) {
                List<String> text = new ArrayList<>(originText);
                if (Plugins.hasPlaceholderAPI()) {
                    text = PlaceholderAPI.setPlaceholders(player, text);
                }

                for (Location location : locations) {
                    World world = location.getWorld();
                    if (world == null || !world.getPlayers().contains(player)) continue;

                    Location clone = location.clone(); // Clone to keep the location in list unmodified for next players.
                    for (String line : text) {
                        bundle.containers.addAll(0, this.createHologramPackets(idList, clone, line));
                    }
                }
            }

            bundles.add(bundle);
        });

        // Synchronize all the packets sending with the main thread to reduce hologram flicker.
        if (action == Action.REFRESH && !this.plugin.getServer().isPrimaryThread()) {
            this.plugin.runTask(() -> this.sendBundles(bundles));
        }
        // Otherwise do it in the current thread.
        else {
            this.sendBundles(bundles);
        }
    }

    private void sendBundles(@NotNull List<PacketBundle> bundles) {
        bundles.forEach(bundle -> bundle.containers().forEach(container -> {
            this.protocolManager.sendServerPacket(bundle.player, container);
        }));
    }

    @Override
    public void createReward(@NotNull Player player, @NotNull Reward reward, @NotNull Location location) {

    }

    @Override
    public void removeReward(@NotNull Player player) {

    }

    @NotNull
    private List<PacketContainer> createHologramPackets(@NotNull IdList idList, @NotNull Location location, @NotNull String textLine) {
        List<PacketContainer> list = new ArrayList<>();
        int entityID = EntityUtil.nextEntityId();
        EntityType type = this.useDisplays ? EntityType.TEXT_DISPLAY : EntityType.ARMOR_STAND;

        PacketContainer spawnPacket = new PacketContainer(PacketType.Play.Server.SPAWN_ENTITY);
        spawnPacket.getIntegers().write(0, entityID);
        spawnPacket.getUUIDs().write(0, UUID.randomUUID());
        spawnPacket.getEntityTypeModifier().write(0, type);
        spawnPacket.getDoubles().write(0, location.getX());
        spawnPacket.getDoubles().write(1, location.getY());
        spawnPacket.getDoubles().write(2, location.getZ());

        // Metadata handle
        //        world.getPlayers().forEach(player -> {
//        String text = textLine;
//        if (Plugins.hasPlaceholderAPI()) {
//            text = PlaceholderAPI.setPlaceholders(player, text);
//        }

        Object component = WrappedChatComponent.fromJson(NightMessage.asJson(textLine)).getHandle();
        PacketContainer dataPacket = this.createMetadataPacket(entityID, metadata -> {

            // Armor Stands (legacy)
            if (type == EntityType.ARMOR_STAND) {
                metadata.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(0, WrappedDataWatcher.Registry.get(Byte.class)), (byte) 0x20); //invis
                metadata.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(2, WrappedDataWatcher.Registry.getChatComponentSerializer(true)), Optional.of(component)); //display name
                metadata.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(3, WrappedDataWatcher.Registry.get(Boolean.class)), true); //custom name visible
                metadata.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(5, WrappedDataWatcher.Registry.get(Boolean.class)), true); //no gravity
                metadata.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(15, WrappedDataWatcher.Registry.get(Byte.class)), (byte) (0x01 | 0x08 | 0x10)); //isSmall, noBasePlate, set Marker
            }
            // Displays (modern)
            else {
                metadata.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(15, WrappedDataWatcher.Registry.get(Byte.class)), (byte) 1); // billboard
                metadata.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(23, WrappedDataWatcher.Registry.getChatComponentSerializer()), component);
                metadata.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(27, WrappedDataWatcher.Registry.get(Byte.class)), (byte) 0x1); // shadow
            }
        });
//            this.protocolManager.sendServerPacket(player, spawnPacket);
//            this.protocolManager.sendServerPacket(player, dataPacket);
//      });

        list.add(spawnPacket);
        list.add(dataPacket);

        location.add(0, this.lineGap, 0);
        idList.holoramIds.add(entityID);

        return list;
    }

    @NotNull
    private PacketContainer createMetadataPacket(int entityID, @NotNull Consumer<WrappedDataWatcher> consumer) {
        PacketContainer dataPacket = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
        WrappedDataWatcher metadata = new WrappedDataWatcher();

        consumer.accept(metadata);

        List<WrappedDataValue> wrappedDataValueList = new ArrayList<>();
        metadata.getWatchableObjects().stream().filter(Objects::nonNull).forEach(entry -> {
            WrappedDataWatcher.WrappedDataWatcherObject dataWatcherObject = entry.getWatcherObject();
            wrappedDataValueList.add(new WrappedDataValue(dataWatcherObject.getIndex(), dataWatcherObject.getSerializer(), entry.getRawValue()));
        });

        dataPacket.getDataValueCollectionModifier().write(0, wrappedDataValueList);
        dataPacket.getIntegers().write(0, entityID);

        return dataPacket;
    }

    private void destroyEntity(@NotNull IdList idList) {
        this.protocolManager.broadcastServerPacket(this.createDestroyPacket(idList));
    }

    @NotNull
    private PacketContainer createDestroyPacket(@NotNull IdList idList) {
        return this.createDestroyPacket(idList.getAll());
    }

    @NotNull
    private PacketContainer createDestroyPacket(@NotNull Collection<Integer> list) {
        PacketContainer container = new PacketContainer(PacketType.Play.Server.ENTITY_DESTROY);
        container.getIntLists().write(0, new ArrayList<>(list));

        return container;
    }
}
