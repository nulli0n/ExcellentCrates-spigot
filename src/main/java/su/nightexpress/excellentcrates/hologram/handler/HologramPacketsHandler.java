package su.nightexpress.excellentcrates.hologram.handler;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.player.PlayerManager;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDestroyEntities;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnEntity;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.hologram.HologramHandler;
import su.nightexpress.excellentcrates.hologram.entity.FakeEntity;
import su.nightexpress.nightcore.util.Players;
import su.nightexpress.nightcore.util.text.NightMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

public class HologramPacketsHandler implements HologramHandler {

    private final PlayerManager playerManager;

    public HologramPacketsHandler() {
        this.playerManager = PacketEvents.getAPI().getPlayerManager();
    }

    private void sendPacket(@NotNull Player player, @NotNull PacketWrapper<?> packet) {
        this.playerManager.sendPacket(player, packet);
    }

    private void broadcastPacket(@NotNull PacketWrapper<?> packet) {
        Players.getOnline().forEach(player -> this.playerManager.sendPacket(player, packet));
    }

    @SuppressWarnings("deprecation")
    @Override
    public void sendHologramPackets(@NotNull Player player, @NotNull FakeEntity entity, boolean needSpawn, @NotNull String textLine) {
        PacketWrapper<?> dataPacket = this.createMetadataPacket(entity.getId(), dataList -> {
            dataList.add(new EntityData(15, EntityDataTypes.BYTE, (byte) 1)); // billboard
            dataList.add(new EntityData(23, EntityDataTypes.COMPONENT, NightMessage.asJson(textLine))); // text
            dataList.add(new EntityData(24, EntityDataTypes.INT, Integer.MAX_VALUE)); // line width
            dataList.add(new EntityData(27, EntityDataTypes.BYTE, (byte) 0x1)); // shadow
        });

        if (needSpawn) {
            this.sendPacket(player, this.createSpawnPacket(entity));
        }

        this.sendPacket(player, dataPacket);
    }

    @Override
    public void sendDestroyEntityPacket(@NotNull Set<Integer> idList) {
        this.broadcastPacket(this.createDestroyPacket(idList));
    }

    @Override
    public void sendDestroyEntityPacket(@NotNull Player player, @NotNull Set<Integer> idList) {
        this.sendPacket(player, this.createDestroyPacket(idList));
    }

    @NotNull
    private WrapperPlayServerDestroyEntities createDestroyPacket(@NotNull Set<Integer> list) {
        return new WrapperPlayServerDestroyEntities(list.stream().mapToInt(i -> i).toArray());
    }

    @NotNull
    private WrapperPlayServerSpawnEntity createSpawnPacket(@NotNull FakeEntity entity) {
        com.github.retrooper.packetevents.protocol.entity.type.EntityType type = SpigotConversionUtil.fromBukkitEntityType(EntityType.TEXT_DISPLAY);
        com.github.retrooper.packetevents.protocol.world.Location location = SpigotConversionUtil.fromBukkitLocation(entity.getLocation());

        return new WrapperPlayServerSpawnEntity(entity.getId(), UUID.randomUUID(), type, location, 0F, 0, null);
    }

    @NotNull
    private WrapperPlayServerEntityMetadata createMetadataPacket(int entityID, @NotNull Consumer<List<EntityData>> consumer) {
        List<EntityData> dataList = new ArrayList<>();

        consumer.accept(dataList);

        return new WrapperPlayServerEntityMetadata(entityID, dataList);
    }
}
