package su.nightexpress.excellentcrates.hologram;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.hologram.entity.HologramData;
import su.nightexpress.excellentcrates.hologram.entity.HologramEntity;
import su.nightexpress.excellentcrates.hologram.listener.HologramListener;
import su.nightexpress.excellentcrates.util.CrateUtils;
import su.nightexpress.excellentcrates.util.pos.WorldPos;
import su.nightexpress.nightcore.manager.AbstractManager;
import su.nightexpress.nightcore.util.EntityUtil;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.LocationUtil;
import su.nightexpress.nightcore.util.Players;
import su.nightexpress.nightcore.util.placeholder.Replacer;

import java.util.*;

public class HologramManager extends AbstractManager<CratesPlugin> {

    private final Map<String, HologramData> hologramDataMap;
    private final Set<String>               hidden;
    private final HologramHandler           handler;

    private boolean useDisplays;
    private double  lineGap;

    public HologramManager(@NotNull CratesPlugin plugin, @NotNull HologramHandler handler) {
        super(plugin);
        this.hologramDataMap = new HashMap<>();
        this.hidden = new HashSet<>();
        this.handler = handler;
    }

    @Override
    protected void onLoad() {
        this.useDisplays = Config.CRATE_HOLOGRAM_USE_DISPLAYS.get();
        this.lineGap = Config.CRATE_HOLOGRAM_LINE_GAP.get();

        this.addListener(new HologramListener(this.plugin, this));

        this.addAsyncTask(this::tickHolograms, Config.CRATE_HOLOGRAM_UPDATE_INTERVAL.get());
    }

    @Override
    protected void onShutdown() {
        this.hologramDataMap.values().forEach(hologramData -> {
            this.handler.destroyEntity(hologramData.getEntityIDs());
        });
        this.hologramDataMap.clear();
        this.hidden.clear();
    }

    public void tickHolograms() {
        this.plugin.getCrateManager().getCrates().forEach(crate -> {
            if (!crate.isHologramEnabled()) return;

            this.refresh(crate);
        });
    }

    public void handleQuit(@NotNull Player player) {
        this.hologramDataMap.values().forEach(hologramData -> hologramData.getEntities().forEach(holo -> holo.removePlayer(player)));
    }

    public void hide(@NotNull Crate crate) {
        if (this.hidden.add(crate.getId())) {
            this.remove(crate);
        }
    }

    public void show(@NotNull Crate crate) {
        if (this.hidden.remove(crate.getId())) {
            this.refresh(crate);
        }
    }

    public void refresh(@NotNull Crate crate) {
        this.createIfAbsent(crate);

        HologramData hologramData = this.hologramDataMap.get(crate.getId());
        if (hologramData == null || this.hidden.contains(crate.getId())) return;

        double yOffset = crate.getHologramYOffset();
        if (this.useDisplays) yOffset += 0.2;

        for (HologramEntity entity : hologramData.getEntities()) {
            WorldPos position = entity.position();
            if (!position.isChunkLoaded()) return;

            World world = position.getWorld();
            Block block = position.toBlock();
            if (world == null || block == null) return;

            double height = block.getBoundingBox().getHeight() / 2D + yOffset;
            Location location = LocationUtil.setCenter3D(block.getLocation()).add(0, height + entity.gap(), 0);

            Players.getOnline().forEach(player -> {
                if (!CrateUtils.isInEffectRange(player, location)) {
                    entity.removePlayer(player);
                    this.handler.destroyEntity(player, Lists.newSet(entity.entityID()));
                    return;
                }

                String text = Replacer.create().replace(crate.replacePlaceholders()).replacePlaceholderAPI(player).apply(entity.text());

                boolean create = !entity.isCreated(player);
                if (create) {
                    entity.addPlayer(player);
                }

                this.sendHologramPackets(player, entity.entityID(), create, location, text);
            });
        }
    }

    private void createIfAbsent(@NotNull Crate crate) {
        if (this.hologramDataMap.containsKey(crate.getId())) return;

        List<String> originText = crate.getHologramText();
        if (originText.isEmpty()) return;

        HologramData hologramData = this.hologramDataMap.computeIfAbsent(crate.getId(), k -> new HologramData());

        Collections.reverse(originText);

        crate.getBlockPositions().forEach(pos -> {
            double currentGap = 0;

            for (String text : originText) {
                int entityID = EntityUtil.nextEntityId();

                hologramData.getEntities().add(new HologramEntity(entityID, pos, text, currentGap, new HashSet<>()));
                currentGap += this.lineGap;
            }
        });
    }

    public void create(@NotNull Crate crate) {
        this.createIfAbsent(crate);
    }

    public void remove(@NotNull Crate crate) {
        HologramData hologramData = this.hologramDataMap.remove(crate.getId());
        if (hologramData == null) return;

        this.handler.destroyEntity(hologramData.getEntityIDs());
    }

    private void sendHologramPackets(@NotNull Player player, int entityID, boolean create, @NotNull Location location, @NotNull String textLine) {
        EntityType type = this.useDisplays ? EntityType.TEXT_DISPLAY : EntityType.ARMOR_STAND;

        this.handler.displayHolograms(player, entityID, create, type, location, textLine);
    }
}
