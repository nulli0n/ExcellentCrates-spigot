package su.nightexpress.excellentcrates.crate.impl;

import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.placeholder.PlaceholderMap;
import su.nexmedia.engine.lang.LangManager;
import su.nexmedia.engine.utils.LocationUtil;
import su.nexmedia.engine.utils.NumberUtil;
import su.nightexpress.excellentcrates.ExcellentCratesPlugin;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.util.Inspector;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.key.CrateKey;

import java.util.ArrayList;
import java.util.List;

public class CrateInspector extends Inspector {

    private final ExcellentCratesPlugin plugin;
    private final Crate                 crate;
    private final PlaceholderMap placeholderMap;

    public CrateInspector(@NotNull Crate crate) {
        this.plugin = crate.plugin();
        this.crate = crate;
        this.placeholderMap = Placeholders.forCrateInspector(this);
    }

    @Override
    @NotNull
    public PlaceholderMap getPlaceholders() {
        return this.placeholderMap;
    }

    @NotNull
    public Crate getCrate() {
        return crate;
    }

    public boolean hasRewards() {
        return !this.crate.getRewards().isEmpty();
    }

    public boolean hasInvalidKeys() {
        return this.crate.getKeyIds().stream().anyMatch(id -> plugin.getKeyManager().getKeyById(id) == null);
    }

    public boolean hasValidKeys() {
        return this.crate.getKeyIds().stream().anyMatch(id -> plugin.getKeyManager().getKeyById(id) != null);
    }

    public boolean hasValidPreview() {
        String name = this.crate.getPreviewConfig();
        if (name == null) return true;

        return this.plugin.getCrateManager().getPreview(name) != null;
    }

    public boolean hasValidOpening() {
        String name = this.crate.getOpeningConfig();
        if (name == null) return true;

        return this.plugin.getCrateManager().getOpening(name) != null;
    }

    public boolean hasValidHologram() {
        String id = this.crate.getHologramTemplate();

        return Config.CRATE_HOLOGRAM_TEMPLATES.get().containsKey(id);
    }

    @NotNull
    public List<String> formatKeyList() {
        List<String> list = new ArrayList<>();

        this.crate.getKeyIds().forEach(id -> {
            CrateKey key = this.plugin.getKeyManager().getKeyById(id);
            if (key != null) {
                list.add(good(key.getName()));
            }
            else list.add(problem(id));
        });

        return list;
    }

    @NotNull
    public List<String> formatBlockList() {
        List<String> list = new ArrayList<>();

        this.crate.getBlockLocations().forEach(location -> {
            Block block = location.getBlock();
            String name = LangManager.getMaterial(block.getType());

            String x = NumberUtil.format(location.getX());
            String y = NumberUtil.format(location.getY());
            String z = NumberUtil.format(location.getZ());
            String world = LocationUtil.getWorldName(location);
            String coords = x + ", " + y + ", " + z + " in " + world;
            String line = coords + " (" + name + ")";

            if (!block.isEmpty()) {
                list.add(good(line));
            }
            else list.add(problem(line));
        });

        return list;
    }
}
