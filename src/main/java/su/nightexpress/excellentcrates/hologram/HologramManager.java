package su.nightexpress.excellentcrates.hologram;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.hologram.entity.FakeDisplay;
import su.nightexpress.excellentcrates.hologram.entity.FakeEntity;
import su.nightexpress.excellentcrates.hologram.entity.FakeEntityGroup;
import su.nightexpress.excellentcrates.hologram.handler.HologramPacketsHandler;
import su.nightexpress.excellentcrates.hologram.handler.HologramProtocolHandler;
import su.nightexpress.excellentcrates.hologram.listener.HologramListener;
import su.nightexpress.excellentcrates.hooks.HookId;
import su.nightexpress.excellentcrates.util.pos.WorldPos;
import su.nightexpress.nightcore.manager.AbstractManager;
import su.nightexpress.nightcore.util.LocationUtil;
import su.nightexpress.nightcore.util.Plugins;
import su.nightexpress.nightcore.util.placeholder.Replacer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class HologramManager extends AbstractManager<CratesPlugin> {

    private final Map<String, FakeDisplay> displayMap;

    private HologramHandler handler;

    public HologramManager(@NotNull CratesPlugin plugin) {
        super(plugin);
        this.displayMap = new ConcurrentHashMap<>();
    }

    @Override
    protected void onLoad() {
        if (this.detectHandler()) {
            this.addListener(new HologramListener(this.plugin, this));

            this.addTask(this::tickHolograms, Config.CRATE_HOLOGRAM_UPDATE_INTERVAL.get());
        }
    }

    @Override
    protected void onShutdown() {
        this.displayMap.values().forEach(this::discard);
        this.displayMap.clear();

        this.handler = null;
    }

    private boolean detectHandler() {
        if (Plugins.isInstalled(HookId.PACKET_EVENTS)) {
            this.handler = new HologramPacketsHandler();
        }
        else if (Plugins.isInstalled(HookId.PROTOCOL_LIB)) {
            this.handler = new HologramProtocolHandler();
        }
        else {
            this.plugin.warn("*".repeat(25));
            this.plugin.warn("You have no packet library plugins installed for the Holograms feature to work.");
            this.plugin.warn("Please install one of the following plugins to enable crate holograms: " + HookId.PACKET_EVENTS + " or " + HookId.PROTOCOL_LIB);
            this.plugin.warn("*".repeat(25));
        }

        return this.hasHandler();
    }

    private void tickHolograms() {
        this.plugin.getCrateManager().getCrates().forEach(crate -> {
            if (!crate.isHologramEnabled()) return;

            this.render(crate);
        });
    }

    public boolean hasHandler() {
        return this.handler != null;
    }

    @Nullable
    private FakeDisplay getDisplay(@NotNull Crate crate) {
        return this.displayMap.get(crate.getId());
    }

    public void disableBlockHologram(@NotNull Crate crate, @NotNull WorldPos blockPos) {
        this.toggleBlockHologram(crate, blockPos, false);
    }

    public void enableBlockHologram(@NotNull Crate crate, @NotNull WorldPos blockPos) {
        this.toggleBlockHologram(crate, blockPos, true);
    }

    private void toggleBlockHologram(@NotNull Crate crate, @NotNull WorldPos blockPos, boolean enabled) {
        FakeDisplay display = this.getDisplay(crate);
        if (display == null) return;

        FakeEntityGroup group = display.getGroup(blockPos);
        if (group == null) return;

        group.setDisabled(!enabled);

        if (group.isDisabled()) {
            this.discard(group);
        }
        else {
            this.render(crate);
        }
    }



    public void removeForViewer(@NotNull Player player) {
        this.displayMap.values().forEach(display -> this.removeForViewer(player, display));
    }

    public void removeForViewer(@NotNull Player player, @NotNull FakeDisplay display) {
        display.getGroups().forEach(group -> this.removeForViewer(player, group));
    }

    public void removeForViewer(@NotNull Player player, @NotNull FakeEntityGroup group) {
        group.removeViewer(player);
        this.handler.sendDestroyEntityPacket(player, group.getEntityIDs());
    }



    public void discard(@NotNull Crate crate) {
        FakeDisplay display = this.displayMap.remove(crate.getId());
        if (display == null) return;

        this.discard(display);
    }

    public void discard(@NotNull FakeDisplay display) {
        display.getGroups().forEach(this::discard);
    }

    public void discard(@NotNull FakeEntityGroup group) {
        group.clearViewers();
        this.handler.sendDestroyEntityPacket(group.getEntityIDs());
    }



    public void render(@NotNull Crate crate) {
        this.createIfAbsent(crate);

        FakeDisplay display = this.getDisplay(crate);
        if (display == null) return;

        List<String> text = Replacer.create().replace(crate.replacePlaceholders()).apply(crate.getHologramText().reversed());
        if (text.isEmpty()) return;

        double heightOffset = crate.getHologramYOffset() + 0.2;
        double lineGap = Config.CRATE_HOLOGRAM_LINE_GAP.get();
        double distance = Config.CRATE_EFFECTS_VISIBILITY_DISTANCE.get();

        for (FakeEntityGroup group : display.getGroups()) {
            if (group.isDisabled()) continue;

            WorldPos blockPosition = group.getBlockPosition();
            Location location = blockPosition.toLocation();

            if (location == null || !blockPosition.isChunkLoaded()) {
                this.discard(group);
                continue;
            }

            this.plugin.runOwned(location, () -> this.renderGroup(group, location, text, heightOffset, lineGap, distance));
        }
    }

    private void renderGroup(@NotNull FakeEntityGroup group, @NotNull Location location, @NotNull List<String> text,
                             double heightOffset, double lineGap, double distance) {
        if (group.isDisabled() || !this.hasHandler()) return;

        if (group.getEntities().isEmpty()) {
            double height = location.getBlock().getBoundingBox().getHeight() / 2D + heightOffset;
            Location origin = LocationUtil.setCenter3D(location.clone());

            for (int index = 0; index < text.size(); index++) {
                group.addEntity(FakeEntity.create(origin.clone().add(0, height + lineGap * index, 0)));
            }
        }

        Collection<Player> players = location.getNearbyPlayers(distance);

        for (UUID viewerId : group.getViewerIds()) {
            Player viewer = this.plugin.getServer().getPlayer(viewerId);
            if (viewer == null) {
                group.removeViewer(viewerId);
            }
            else if (!players.contains(viewer)) {
                this.removeForViewer(viewer, group);
            }
        }

        if (players.isEmpty()) {
            this.discard(group);
            return;
        }

        List<FakeEntity> holograms = group.getEntities();

        players.forEach(player -> {
            boolean needSpawn = !group.isViewer(player);

            List<String> hologramText = Replacer.create().replacePlaceholderAPI(player).apply(text);
            for (int index = 0; index < holograms.size(); index++) {
                String line = index >= hologramText.size() ? "" : hologramText.get(index);
                this.handler.sendHologramPackets(player, holograms.get(index), needSpawn, line);
            }

            group.addViewer(player);
        });
    }

    private void createIfAbsent(@NotNull Crate crate) {
        if (!this.hasHandler()) return;
        if (crate.getHologramText().isEmpty()) return;

        this.displayMap.computeIfAbsent(crate.getId(), crateId -> {
            FakeDisplay display = new FakeDisplay();
            crate.getBlockPositions().forEach(display::getGroupOrCreate);
            return display;
        });
    }
}
