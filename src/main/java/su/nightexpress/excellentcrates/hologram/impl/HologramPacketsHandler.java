package su.nightexpress.excellentcrates.hologram.impl;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.player.PlayerManager;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDestroyEntities;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnEntity;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.nightcore.util.text.NightMessage;

import java.util.*;
import java.util.function.Consumer;

public class HologramPacketsHandler extends AbstractHologramHandler<PacketWrapper<?>> {

    private final PlayerManager           playerManager;

    public HologramPacketsHandler(@NotNull CratesPlugin plugin) {
        super(plugin);
        this.playerManager = PacketEvents.getAPI().getPlayerManager();
    }

    @Override
    protected void sendPacket(@NotNull Player player, @NotNull PacketWrapper<?> packet) {
        this.playerManager.sendPacket(player, packet);
    }

    @Override
    protected void broadcastPacket(@NotNull PacketWrapper<?> packet) {
        this.plugin.getServer().getOnlinePlayers().forEach(player -> this.playerManager.sendPacket(player, packet));
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

    @SuppressWarnings("deprecation")
    @Override
    @NotNull
    protected List<PacketWrapper<?>> createHologramPackets(@NotNull Player player, int entityID, @NotNull EntityType type, @NotNull Location location, @NotNull String textLine) {
        List<PacketWrapper<?>> list = new ArrayList<>();

        PacketWrapper<?> spawnPacket = this.createSpawnPacket(type, location, entityID);
        PacketWrapper<?> dataPacket = this.createMetadataPacket(entityID, dataList -> {
            // Armor Stands (legacy)
            if (type == EntityType.ARMOR_STAND) {
                dataList.add(new EntityData(0, EntityDataTypes.BYTE, (byte) 0x20)); // invisible
                dataList.add(new EntityData(2, EntityDataTypes.OPTIONAL_COMPONENT, Optional.of(NightMessage.asJson(textLine)))); // display name
                dataList.add(new EntityData(3, EntityDataTypes.BOOLEAN, true)); // custom name visible
                dataList.add(new EntityData(5, EntityDataTypes.BOOLEAN, true)); // no gravity
                dataList.add(new EntityData(15, EntityDataTypes.BYTE, (byte) (0x01 | 0x08 | 0x10))); // isSmall noBasePlate setMarker
            }
            // Displays (modern)
            else {
                dataList.add(new EntityData(15, EntityDataTypes.BYTE, (byte) 1)); // billboard
                dataList.add(new EntityData(23, EntityDataTypes.COMPONENT, NightMessage.asJson(textLine))); // text
                dataList.add(new EntityData(27, EntityDataTypes.BYTE, (byte) 0x1)); // shadow
            }
        });

        list.add(spawnPacket);
        list.add(dataPacket);

        return list;
    }

    @Override
    @NotNull
    protected WrapperPlayServerDestroyEntities createDestroyPacket(@NotNull Set<Integer> list) {
        return new WrapperPlayServerDestroyEntities(list.stream().mapToInt(i -> i).toArray());
    }

    @Override
    @NotNull
    protected WrapperPlayServerSpawnEntity createSpawnPacket(@NotNull EntityType entityType, @NotNull Location location, int entityID) {
        com.github.retrooper.packetevents.protocol.entity.type.EntityType type = SpigotConversionUtil.fromBukkitEntityType(entityType);
        com.github.retrooper.packetevents.protocol.world.Location loc = SpigotConversionUtil.fromBukkitLocation(location);

        return new WrapperPlayServerSpawnEntity(entityID, UUID.randomUUID(), type, loc, 0F, 0, null);
    }

    @NotNull
    private WrapperPlayServerEntityMetadata createMetadataPacket(int entityID, @NotNull Consumer<List<EntityData>> consumer) {
        List<EntityData> dataList = new ArrayList<>();

        consumer.accept(dataList);

        return new WrapperPlayServerEntityMetadata(entityID, dataList);
    }
}
