package su.nightexpress.excellentcrates.integration.packetevents;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.player.PlayerManager;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.world.Location;
import com.github.retrooper.packetevents.util.Vector3f;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDestroyEntities;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnEntity;

import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import net.kyori.adventure.text.Component;
import su.nightexpress.engine.settings.ReadOnlySettings;
import su.nightexpress.excellentcrates.integration.packetevents.entity.Hologram;
import su.nightexpress.excellentcrates.integration.packetevents.settings.HologramSettings;
import su.nightexpress.nightcore.bridge.paper.PaperBridge;
import su.nightexpress.nightcore.util.Players;
import su.nightexpress.nightcore.util.bridge.Software;
import su.nightexpress.nightcore.util.bridge.wrapper.NightComponent;
import su.nightexpress.nightcore.util.text.night.NightMessage;

@NullMarked
final class HologramPacketManager {

    private final ReadOnlySettings<HologramSettings> settings;
    private final PlayerManager                      playerManager;

    HologramPacketManager(ReadOnlySettings<HologramSettings> settings) {
        this.settings = settings;
        this.playerManager = PacketEvents.getAPI().getPlayerManager();
    }

    private int getDisplayBackgroundColor() {
        int[] bgColor = this.settings.get().backgroundColor();
        int alpha = bgColor[0];
        int red = bgColor[1];
        int green = bgColor[2];
        int blue = bgColor[3];

        return ((alpha & 0xFF) << 24) | ((red & 0xFF) << 16) | ((green & 0xFF) << 8) | (blue & 0xFF);
    }

    private byte getDisplayBillboard() {
        return switch (this.settings.get().billboard()) {
            case FIXED -> 0;
            case VERTICAL -> 1;
            case HORIZONTAL -> 2;
            case CENTER -> 3;
        };
    }

    private byte getDisplayTextBitMask() {
        boolean shadow = this.settings.get().shadow();
        boolean seeThrough = this.settings.get().seeThrough();

        return (byte) ((shadow ? 0x01 : 0) | (seeThrough ? 0x02 : 0));
    }

    private void sendPacket(Player player, PacketWrapper<?> packet) {
        this.playerManager.sendPacket(player, packet);
    }

    private void broadcastPacket(PacketWrapper<?> packet) {
        Players.getOnline().forEach(player -> this.playerManager.sendPacket(player, packet));
    }

    public void sendHologramPackets(Player player, Hologram entity, boolean needSpawn,
                                    String textLine) {
        PaperBridge bridge = (PaperBridge) Software.get();
        NightComponent component = NightMessage.parse(textLine);
        Component textComponent = bridge.getTextComponentAdapter().adaptComponent(component);

        float scale = (float) this.settings.get().scale();

        PacketWrapper<?> dataPacket = this.createMetadataPacket(entity.getEntityId(), dataList -> {
            dataList.add(new EntityData<>(12, EntityDataTypes.VECTOR3F, new Vector3f(scale, scale, scale)));
            dataList.add(new EntityData<>(15, EntityDataTypes.BYTE, this.getDisplayBillboard()));
            dataList.add(new EntityData<>(23, EntityDataTypes.ADV_COMPONENT, textComponent));
            dataList.add(new EntityData<>(24, EntityDataTypes.INT, this.settings.get().lineWidth()));
            dataList.add(new EntityData<>(25, EntityDataTypes.INT, this.getDisplayBackgroundColor()));
            dataList.add(new EntityData<>(26, EntityDataTypes.BYTE, (byte) this.settings.get().textOpacity()));
            dataList.add(new EntityData<>(27, EntityDataTypes.BYTE, this.getDisplayTextBitMask()));
        });

        if (needSpawn) {
            this.sendPacket(player, this.createSpawnPacket(entity));
        }

        this.sendPacket(player, dataPacket);
    }

    public void broadcastDestroyEntityPacket(Collection<Integer> idList) {
        this.broadcastPacket(this.createDestroyPacket(idList));
    }

    public void sendDestroyEntityPacket(Player player, Collection<Integer> idList) {
        this.sendPacket(player, this.createDestroyPacket(idList));
    }


    private WrapperPlayServerDestroyEntities createDestroyPacket(Collection<Integer> list) {
        return new WrapperPlayServerDestroyEntities(list.stream().mapToInt(i -> i).toArray());
    }


    private WrapperPlayServerSpawnEntity createSpawnPacket(Hologram entity) {
        com.github.retrooper.packetevents.protocol.entity.type.EntityType type = SpigotConversionUtil
            .fromBukkitEntityType(EntityType.TEXT_DISPLAY);

        Location location = SpigotConversionUtil.fromBukkitLocation(entity.getDisplayLocation());

        return new WrapperPlayServerSpawnEntity(entity.getEntityId(), UUID.randomUUID(), type, location, 0F, 0, null);
    }


    private WrapperPlayServerEntityMetadata createMetadataPacket(int entityID,
                                                                 Consumer<List<EntityData<?>>> consumer) {
        List<EntityData<?>> dataList = new ArrayList<>();

        consumer.accept(dataList);

        return new WrapperPlayServerEntityMetadata(entityID, dataList);
    }
}