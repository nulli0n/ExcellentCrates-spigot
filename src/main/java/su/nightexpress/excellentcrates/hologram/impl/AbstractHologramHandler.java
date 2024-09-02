package su.nightexpress.excellentcrates.hologram.impl;

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
import su.nightexpress.excellentcrates.util.CrateUtils;
import su.nightexpress.excellentcrates.util.pos.WorldPos;
import su.nightexpress.nightcore.util.EntityUtil;
import su.nightexpress.nightcore.util.LocationUtil;
import su.nightexpress.nightcore.util.Plugins;
import su.nightexpress.nightcore.util.Version;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractHologramHandler<T> implements HologramHandler {

    protected final CratesPlugin            plugin;
    protected final Map<String, EntityList> entityMap;

    private boolean useDisplays;
    private double  lineGap;

    public AbstractHologramHandler(@NotNull CratesPlugin plugin) {
        this.plugin = plugin;
        this.entityMap = new HashMap<>();
    }

    protected static class EntityList {

        private final List<HologramEntity> holograms;
        private final Set<Integer>                                itemIds;

        public EntityList() {
            this.holograms = new ArrayList<>();
            this.itemIds = ConcurrentHashMap.newKeySet();
        }

        @NotNull
        public List<HologramEntity> getHolograms() {
            return holograms;
        }

        @NotNull
        public Set<Integer> getIDs() {
            Set<Integer> idList = new HashSet<>();
            holograms.forEach(hologramEntity -> idList.add(hologramEntity.entityID));
            return idList;
        }

        public void clear() {
            this.holograms.clear();
            this.itemIds.clear();
        }
    }

    protected static class HologramEntity {

        private final int      entityID;
        private final WorldPos position;
        private final String   text;
        private final double   gap;

        public HologramEntity(int entityID, WorldPos position, String text, double gap) {
            this.entityID = entityID;
            this.position = position;
            this.text = text;
            this.gap = gap;
        }
    }

    @Override
    public void setup() {
        this.useDisplays = Config.CRATE_HOLOGRAM_USE_DISPLAYS.get() && Version.isAtLeast(Version.V1_20_R3);
        this.lineGap = Config.CRATE_HOLOGRAM_LINE_GAP.get();
    }

    @Override
    public void shutdown() {
        this.entityMap.values().forEach(list -> {
            this.broadcastPacket(this.createDestroyPacket(list.getIDs()));
        });
        this.entityMap.clear();
    }

    @Override
    public void refresh(@NotNull Crate crate) {
        this.createIfAbsent(crate);

        EntityList entityList = this.entityMap.get(crate.getId());
        if (entityList == null) return;

        double yOffset = crate.getHologramYOffset();
        if (this.useDisplays) yOffset += 0.2;

        for (HologramEntity hologramEntity : entityList.getHolograms()) {
            WorldPos pos = hologramEntity.position;
            if (!pos.isChunkLoaded()) return;

            World world = pos.getWorld();
            Block block = pos.toBlock();
            if (world == null || block == null) return;

            double height = block.getBoundingBox().getHeight() / 2D + yOffset;
            Location location = LocationUtil.setCenter3D(block.getLocation()).add(0, height + hologramEntity.gap, 0);

            CrateUtils.getPlayersForEffects(location).forEach(player -> {
                //if (player.getWorld() != world) return;
                //if (player.getLocation().distance(location) > 32D) return;

                String text = crate.replacePlaceholders().apply(hologramEntity.text);
                if (Plugins.hasPlaceholderAPI()) {
                    text = PlaceholderAPI.setPlaceholders(player, text);
                }

                this.sendHologramPackets(player, hologramEntity.entityID, location, text);
            });
        }
    }

    private void createIfAbsent(@NotNull Crate crate) {
        if (this.entityMap.containsKey(crate.getId())) return;

        EntityList list = this.entityMap.computeIfAbsent(crate.getId(), k -> new EntityList());

        List<String> originText = crate.getHologramText();
        Collections.reverse(originText);

        crate.getBlockPositions().forEach(pos -> {
            double currentGap = 0;

            for (String text : originText) {
                int entityID = EntityUtil.nextEntityId();

                list.holograms.add(new HologramEntity(entityID, pos, text, currentGap));
                currentGap += this.lineGap;
            }
        });
    }

    @Override
    public void create(@NotNull Crate crate) {
        this.createIfAbsent(crate);
    }

    @Override
    public void remove(@NotNull Crate crate) {
        EntityList list = this.entityMap.remove(crate.getId());
        if (list == null) return;

        this.broadcastPacket(this.createDestroyPacket(list.getIDs()));
    }

    @Override
    public void createReward(@NotNull Player player, @NotNull Reward reward, @NotNull Location location) {

    }

    @Override
    public void removeReward(@NotNull Player player) {

    }

    private void sendHologramPackets(@NotNull Player player, int entityID, @NotNull Location location, @NotNull String textLine) {
        EntityType type = this.useDisplays ? EntityType.TEXT_DISPLAY : EntityType.ARMOR_STAND;

        this.createHologramPackets(player, entityID, type, location, textLine).forEach(packet -> this.sendPacket(player, packet));
    }

    protected abstract void sendPacket(@NotNull Player player, @NotNull T packet);

    protected abstract void broadcastPacket(@NotNull T packet);

    @NotNull
    protected abstract List<T> createHologramPackets(@NotNull Player player, int entityID, @NotNull EntityType type, @NotNull Location location, @NotNull String textLine);

    @NotNull
    protected abstract T createDestroyPacket(@NotNull Set<Integer> list);

    @NotNull
    protected abstract T createSpawnPacket(@NotNull EntityType entityType, @NotNull Location location, int entityID);
}
