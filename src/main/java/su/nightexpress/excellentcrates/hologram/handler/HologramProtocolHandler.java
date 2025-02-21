package su.nightexpress.excellentcrates.hologram.handler;

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
import su.nightexpress.excellentcrates.hologram.HologramHandler;
import su.nightexpress.nightcore.util.text.NightMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

public class HologramProtocolHandler implements HologramHandler {

    private final ProtocolManager protocolManager;

    public HologramProtocolHandler() {
        this.protocolManager = ProtocolLibrary.getProtocolManager();
    }

    private void sendPacket(@NotNull Player player, @NotNull PacketContainer container) {
        this.protocolManager.sendServerPacket(player, container);
    }

    private void broadcastPacket(@NotNull PacketContainer packet) {
        this.protocolManager.broadcastServerPacket(packet);
    }

    @Override
    public void displayHolograms(@NotNull Player player, int entityID, boolean create, @NotNull EntityType type, @NotNull Location location, @NotNull String textLine) {
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
                metadata.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(24, WrappedDataWatcher.Registry.get(Integer.class)), Integer.MAX_VALUE); // line width
                metadata.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(23, WrappedDataWatcher.Registry.getChatComponentSerializer()), component);
                metadata.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(27, WrappedDataWatcher.Registry.get(Byte.class)), (byte) 0x1); // shadow
            }
        });

        if (create) this.sendPacket(player, spawnPacket);
        this.sendPacket(player, dataPacket);
    }

    @Override
    public void destroyEntity(@NotNull Player player, @NotNull Set<Integer> idList) {
        this.sendPacket(player, this.createDestroyPacket(idList));
    }

    @Override
    public void destroyEntity(@NotNull Set<Integer> idList) {
        this.broadcastPacket(this.createDestroyPacket(idList));
    }

    @NotNull
    private PacketContainer createDestroyPacket(@NotNull Set<Integer> list) {
        PacketContainer container = new PacketContainer(PacketType.Play.Server.ENTITY_DESTROY);
        container.getIntLists().write(0, new ArrayList<>(list));

        return container;
    }

    @NotNull
    private PacketContainer createSpawnPacket(@NotNull EntityType entityType, @NotNull Location location, int entityID) {
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
