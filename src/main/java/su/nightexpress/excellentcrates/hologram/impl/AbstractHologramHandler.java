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
import su.nightexpress.nightcore.util.*;

import java.util.*;

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

        public EntityList() {
            this.holograms = new ArrayList<>();
        }

        @NotNull
        public List<HologramEntity> getHolograms() {
            return this.holograms;
        }

        @NotNull
        public Set<Integer> getIDs() {
            Set<Integer> idList = new HashSet<>();
            this.holograms.forEach(entity -> idList.add(entity.entityID));
            return idList;
        }

        public void clear() {
            this.holograms.clear();
        }
    }

    protected record HologramEntity(int entityID, WorldPos position, String text, double gap, Set<UUID> players) {

        public void addPlayer(@NotNull Player player) {
            this.players.add(player.getUniqueId());
        }

        public void removePlayer(@NotNull Player player) {
            this.players.remove(player.getUniqueId());
        }

        public boolean isCreated(@NotNull Player player) {
            return this.players.contains(player.getUniqueId());
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

        for (HologramEntity entity : entityList.getHolograms()) {
            WorldPos position = entity.position;
            if (!position.isChunkLoaded()) return;

            World world = position.getWorld();
            Block block = position.toBlock();
            if (world == null || block == null) return;

            double height = block.getBoundingBox().getHeight() / 2D + yOffset;
            Location location = LocationUtil.setCenter3D(block.getLocation()).add(0, height + entity.gap, 0);

            new HashSet<>(plugin.getServer().getOnlinePlayers()).forEach(player -> {
                if (!CrateUtils.isInEffectRange(player, location)) {
                    entity.removePlayer(player);
                    this.sendPacket(player, this.createDestroyPacket(Lists.newSet(entity.entityID)));
                    return;
                }

                String text = crate.replacePlaceholders().apply(entity.text);
                if (Plugins.hasPlaceholderAPI()) {
                    text = PlaceholderAPI.setPlaceholders(player, text);
                }

                boolean create = !entity.isCreated(player);
                if (create) {
                    entity.addPlayer(player);
                }

                this.sendHologramPackets(player, entity.entityID, create, location, text);
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

                list.holograms.add(new HologramEntity(entityID, pos, text, currentGap, new HashSet<>()));
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

    private void sendHologramPackets(@NotNull Player player, int entityID, boolean create, @NotNull Location location, @NotNull String textLine) {
        EntityType type = this.useDisplays ? EntityType.TEXT_DISPLAY : EntityType.ARMOR_STAND;

        this.createHologramPackets(player, entityID, create, type, location, textLine).forEach(packet -> this.sendPacket(player, packet));
    }

    protected abstract void sendPacket(@NotNull Player player, @NotNull T packet);

    protected abstract void broadcastPacket(@NotNull T packet);

    @NotNull
    protected abstract List<T> createHologramPackets(@NotNull Player player, int entityID, boolean create, @NotNull EntityType type, @NotNull Location location, @NotNull String textLine);

    @NotNull
    protected abstract T createDestroyPacket(@NotNull Set<Integer> list);

    @NotNull
    protected abstract T createSpawnPacket(@NotNull EntityType entityType, @NotNull Location location, int entityID);
}
