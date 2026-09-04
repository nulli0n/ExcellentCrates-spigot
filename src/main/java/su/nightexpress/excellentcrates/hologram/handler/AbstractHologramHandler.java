package su.nightexpress.excellentcrates.hologram.handler;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.hologram.HologramHandler;
import su.nightexpress.excellentcrates.hologram.entity.FakeDisplay;
import su.nightexpress.excellentcrates.hologram.entity.FakeEntity;
import su.nightexpress.excellentcrates.hologram.entity.FakeEntityGroup;
import su.nightexpress.excellentcrates.util.CrateUtils;
import su.nightexpress.excellentcrates.util.pos.WorldPos;
import su.nightexpress.nightcore.util.LocationUtil;
import su.nightexpress.nightcore.util.placeholder.Replacer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class AbstractHologramHandler implements HologramHandler {

    protected final CratesPlugin plugin;

    private final Map<String, FakeDisplay> displayMap;

    protected final byte billboard;
    protected final int  lineWidth;
    protected final byte textOpacity;
    protected final byte textBitmask;
    protected final int  backgroundColor;

    public AbstractHologramHandler(@NotNull CratesPlugin plugin) {
        this.plugin = plugin;
        this.displayMap = new HashMap<>();

        this.billboard = translateBillboard(Config.CRATE_HOLOGRAM_BILLBOARD.get());
        this.lineWidth = Integer.MAX_VALUE;
        this.textOpacity = Config.CRATE_HOLOGRAM_TEXT_OPACITY.get().byteValue();
        int[] bgColor = Config.CRATE_HOLOGRAM_BACKGROUND_COLOR.get();
        this.backgroundColor = toARGB(bgColor[0], bgColor[1], bgColor[2], bgColor[3]);

        this.textBitmask = (byte) ((Config.CRATE_HOLOGRAM_SHADOW.get() ? 0x01 : 0) | (Config.CRATE_HOLOGRAM_SEE_THROUGH.get() ? 0x02 : 0));
    }

    @Override
    public void create(@NotNull Crate crate) {
        this.delete(crate);
        this.render(crate);
    }

    @Override
    public void delete(@NotNull Crate crate) {
        FakeDisplay display = this.displayMap.remove(crate.getId());
        if (display == null) return;

        display.getGroups().forEach(this::discard);
    }

    @Override
    public void update(@NotNull Crate crate) {
        if (!crate.isHologramEnabled()) return;

        this.render(crate);
    }

    @Override
    public void toggle(@NotNull Crate crate, @NotNull WorldPos blockPos, boolean enabled) {
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

    @Override
    public void purge(@NotNull Player player) {
        this.displayMap.values().forEach(display -> this.removeForViewer(player, display));
    }

    @Nullable
    private FakeDisplay getDisplay(@NotNull Crate crate) {
        return this.displayMap.get(crate.getId());
    }

    private void removeForViewer(@NotNull Player player, @NotNull FakeDisplay display) {
        display.getGroups().forEach(group -> this.removeForViewer(player, group));
    }

    private void removeForViewer(@NotNull Player player, @NotNull FakeEntityGroup group) {
        group.removeViewer(player);
        this.sendDestroyEntityPacket(player, group.getEntityIDs());
    }

    private void discard(@NotNull FakeDisplay display) {
        display.getGroups().forEach(this::discard);
    }

    private void discard(@NotNull FakeEntityGroup group) {
        group.clearViewers();
        this.sendDestroyEntityPacket(group.getEntityIDs());
    }

    private void render(@NotNull Crate crate) {
        this.createIfAbsent(crate);

        FakeDisplay display = this.getDisplay(crate);
        if (display == null) return;

        if (display.getGroups().isEmpty()) {
            this.displayMap.remove(crate.getId());
            return;
        }

        List<String> text = Replacer.create().replace(crate.replacePlaceholders()).apply(crate.getHologramText().reversed());
        if (text.isEmpty()) return;

        for (FakeEntityGroup group : display.getGroups()) {
            if (group.isDisabled()) continue;

            WorldPos blockPosition = group.getBlockPosition();
            World world = blockPosition.getWorld();
            Location location = blockPosition.toLocation();

            if (!blockPosition.isChunkLoaded() || world == null || location == null) {
                this.discard(group);
                continue;
            }

            List<Player> players;
            try {
                players = new ArrayList<>(world.getPlayers());
            }
            catch (Exception exception) {
                this.plugin.error("Could not get players from world '" + world.getName() + "' for hologram rendering!");
                exception.printStackTrace();
                continue;
            }

            players.removeIf(player -> {
                if (CrateUtils.isInEffectRange(player, location)) return false;

                this.removeForViewer(player, group);
                return true;
            });

            if (players.isEmpty()) {
                this.discard(group);
                continue;
            }

            players.forEach(player -> {
                try {
                    boolean needSpawn = !group.isViewer(player);

                    List<String> hologramText = Replacer.create().replacePlaceholderAPI(player).apply(text);
                    if (!group.updateTextCache(player, hologramText) && !needSpawn) return;

                    List<FakeEntity> holograms = group.getEntities();
                    for (int index = 0; index < holograms.size(); index++) {
                        String line = index >= hologramText.size() ? "" : hologramText.get(index);
                        FakeEntity entity = holograms.get(index);
                        this.sendHologramPackets(player, entity, needSpawn, line);
                    }

                    group.addViewer(player);
                }
                catch (Exception exception) {
                    this.plugin.error("Could not send hologram packets for player '" + player.getName() + "'!");
                    exception.printStackTrace();
                }
            });
        }
    }

    private void createIfAbsent(@NotNull Crate crate) {
        if (this.displayMap.containsKey(crate.getId())) return;

        List<String> originText = crate.getHologramText();
        if (originText.isEmpty()) return;

        FakeDisplay display = new FakeDisplay();

        double yOffset = crate.getHologramYOffset() + 0.2;
        double lineGap = Config.CRATE_HOLOGRAM_LINE_GAP.get();

        crate.getBlockPositions().forEach(blockPos -> {
            try {
                World world = blockPos.getWorld();
                if (world == null) return;

                double blockHeight = 1.0;
                double height = blockHeight / 2D + yOffset;

                FakeEntityGroup group = display.getGroupOrCreate(blockPos);

                for (int index = 0; index < originText.size(); index++) {
                    double gap = lineGap * index;

                    Location location = LocationUtil.setCenter3D(new Location(world, blockPos.getX(), blockPos.getY(), blockPos.getZ())).add(0, height + gap, 0);
                    group.addEntity(FakeEntity.create(location));
                }
            }
            catch (Exception exception) {
                this.plugin.error("Could not create hologram for block at '" + blockPos + "'!");
                exception.printStackTrace();
            }
        });

        if (display.getGroups().isEmpty()) return;

        this.displayMap.put(crate.getId(), display);
    }

    protected abstract void sendHologramPackets(@NotNull Player player, @NotNull FakeEntity entity, boolean needSpawn, @NotNull String textLine);

    protected abstract void sendDestroyEntityPacket(@NotNull Player player, @NotNull Set<Integer> idList);

    protected abstract void sendDestroyEntityPacket(@NotNull Set<Integer> idList);

    private static int toARGB(int alpha, int red, int green, int blue) {
        return ((alpha & 0xFF) << 24)
            | ((red & 0xFF) << 16)
            | ((green & 0xFF) << 8)
            | (blue & 0xFF);
    }

    private static byte translateBillboard(@NotNull Display.Billboard billboard) {
        return switch (billboard) {
            case FIXED -> 0;
            case VERTICAL -> 1;
            case HORIZONTAL -> 2;
            case CENTER -> 3;
        };
    }
}
