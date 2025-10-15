package su.nightexpress.excellentcrates.crate.impl;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.util.StringUtil;

import java.util.Collection;
import java.util.function.UnaryOperator;

public class Rarity {

    private final CratesPlugin plugin;
    private final String       id;

    private String name;
    private double weight;

    public Rarity(@NotNull CratesPlugin plugin, @NotNull String id, @NotNull String name, double weight) {
        this.plugin = plugin;
        this.id = id.toLowerCase();
        this.setName(name);
        this.setWeight(weight);
    }

    @NotNull
    public static Rarity read(@NotNull CratesPlugin plugin, @NotNull FileConfig config, @NotNull String path, @NotNull String id) {
        String name = config.getString(path + ".Name", StringUtil.capitalizeUnderscored(id));
        double weight = config.getDouble(path + ".Weight", config.getDouble(path + ".Chance", 0D));

        return new Rarity(plugin, id, name, weight);
    }

    public void write(@NotNull FileConfig config, @NotNull String path) {
        config.set(path + ".Name", this.name);
        config.set(path + ".Weight", this.weight);
    }

    @NotNull
    public UnaryOperator<String> replacePlaceholders() {
        return Placeholders.RARITY.replacer(this);
    }

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
        return this.id;
    }

    @NotNull
    public String getName() {
        return this.name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public double getWeight() {
        return this.weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
