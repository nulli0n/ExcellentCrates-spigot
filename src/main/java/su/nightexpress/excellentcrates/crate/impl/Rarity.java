package su.nightexpress.excellentcrates.crate.impl;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.api.opening.Weighted;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.util.StringUtil;
import su.nightexpress.nightcore.util.placeholder.Placeholder;
import su.nightexpress.nightcore.util.placeholder.PlaceholderMap;
import su.nightexpress.nightcore.util.text.NightMessage;

import java.util.Collection;

public class Rarity implements Weighted, Placeholder {

    private final CratesPlugin plugin;
    private final String id;
    private final PlaceholderMap placeholderMap;

    private String name;
    private double weight;
    private boolean isDefault;

    public Rarity(@NotNull CratesPlugin plugin, @NotNull String id, @NotNull String name, double weight, boolean isDefault) {
        this.plugin = plugin;
        this.id = id.toLowerCase();
        this.setName(name);
        this.setWeight(weight);
        this.setDefault(isDefault);

        this.placeholderMap = Placeholders.forRarity(this);
    }

    @NotNull
    public static Rarity read(@NotNull CratesPlugin plugin, @NotNull FileConfig config, @NotNull String path, @NotNull String id) {
        String name = config.getString(path + ".Name", StringUtil.capitalizeUnderscored(id));
        double weight = config.getDouble(path + ".Weight", config.getDouble(path + ".Chance", 0D));
        boolean isDefault = ConfigValue.create(path + ".Default", false).read(config);

        return new Rarity(plugin, id, name, weight, isDefault);
    }

    @NotNull
    public static Rarity dummy(@NotNull CratesPlugin plugin) {
        return new Rarity(plugin, "dummy", "Dummy", 100, true);
    }

    public void write(@NotNull FileConfig config, @NotNull String path) {
        config.set(path + ".Name", this.getName());
        config.set(path + ".Weight", this.getWeight());
        config.set(path + ".Default", this.isDefault());
    }

    @Override
    @NotNull
    public PlaceholderMap getPlaceholders() {
        return this.placeholderMap;
    }

    @Override
    public double getRollChance() {
        return this.getRollChance(this.plugin.getCrateManager().getRarities());
    }

    public double getRollChance(@NotNull Crate crate) {
        return this.getRollChance(crate.getRarities());
    }

    public double getRollChance(@NotNull Collection<Rarity> rarities) {
        double sum = rarities.stream().mapToDouble(Rarity::getWeight).sum();
        return (this.getWeight() / sum) * 100D;
    }

    @NotNull
    public String getId() {
        return id;
    }

    @NotNull
    public String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    @NotNull
    public String getNameTranslated() {
        return NightMessage.asLegacy(this.getName());
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public void setWeight(double weight) {
        this.weight = weight;
    }

    public boolean isDefault() {
        return this.isDefault;
    }

    public void setDefault(boolean aDefault) {
        this.isDefault = aDefault;
    }
}
