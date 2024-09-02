package su.nightexpress.excellentcrates.hologram.impl;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataValue;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.nightcore.util.text.NightMessage;

import java.util.*;
import java.util.function.Consumer;

public class HologramProtocolHandler extends AbstractHologramHandler<PacketContainer> {

    private final ProtocolManager protocolManager;

    public HologramProtocolHandler(@NotNull CratesPlugin plugin) {
        super(plugin);
        this.protocolManager = ProtocolLibrary.getProtocolManager();
    }

    @Override
    protected void sendPacket(@NotNull Player player, @NotNull PacketContainer container) {
        this.protocolManager.sendServerPacket(player, container);
    }

    @Override
    protected void broadcastPacket(@NotNull PacketContainer packet) {
        this.protocolManager.broadcastServerPacket(packet);
    }


//    @NotNull
//    private List<PacketWrapper<?>> getItemPackets(int entityID, @NotNull EntityType type, @NotNull Location location, @NotNull ItemStack item) {
//        List<PacketWrapper<?>> list = new ArrayList<>();
//
//        PacketWrapper<?> spawnPacket = this.createSpawnPacket(type, location, entityID);
//        PacketWrapper<?> dataPacket = this.createMetadataPacket(entityID, dataList -> {
//            dataList.add(new EntityData(5, EntityDataTypes.BOOLEAN, true)); // no gravity
//            dataList.add(new EntityData(8, EntityDataTypes.ITEMSTACK, SpigotConversionUtil.fromBukkitItemStack(item))); // item
//        });
//
//        list.add(spawnPacket);
//        list.add(dataPacket);
//
//        return list;
//    }

    @Override
    @NotNull
    protected List<PacketContainer> createHologramPackets(@NotNull Player player, int entityID, @NotNull EntityType type, @NotNull Location location, @NotNull String textLine) {
        List<PacketContainer> list = new ArrayList<>();

        Object component = WrappedChatComponent.fromJson(NightMessage.asJson(textLine)).getHandle();

        PacketContainer spawnPacket = this.createSpawnPacket(type, location, entityID);
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

        list.add(spawnPacket);
        list.add(dataPacket);

        return list;
    }

    @NotNull
    @Override
    protected PacketContainer createDestroyPacket(@NotNull Set<Integer> list) {
        PacketContainer container = new PacketContainer(PacketType.Play.Server.ENTITY_DESTROY);
        container.getIntLists().write(0, new ArrayList<>(list));

        return container;
    }

    @Override
    @NotNull
    protected PacketContainer createSpawnPacket(@NotNull EntityType entityType, @NotNull Location location, int entityID) {
        PacketContainer container = new PacketContainer(PacketType.Play.Server.SPAWN_ENTITY);
        container.getIntegers().write(0, entityID);
        container.getUUIDs().write(0, UUID.randomUUID());
        container.getEntityTypeModifier().write(0, entityType);
        container.getDoubles().write(0, location.getX());
        container.getDoubles().write(1, location.getY());
        container.getDoubles().write(2, location.getZ());

        return container;
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
}
