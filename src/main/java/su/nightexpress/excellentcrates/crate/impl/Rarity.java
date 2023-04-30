package su.nightexpress.excellentcrates.crate.impl;

import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.config.JYML;
import su.nexmedia.engine.api.placeholder.Placeholder;
import su.nexmedia.engine.api.placeholder.PlaceholderMap;
import su.nexmedia.engine.utils.Colorizer;
import su.nexmedia.engine.utils.NumberUtil;
import su.nexmedia.engine.utils.StringUtil;
import su.nightexpress.excellentcrates.Placeholders;

public class Rarity implements Placeholder {

    private final String id;
    private final PlaceholderMap placeholderMap;

    private String name;
    private double chance;

    public Rarity(@NotNull String id, @NotNull String name, double chance) {
        this.id = id.toLowerCase();
        this.setName(name);
        this.setChance(chance);

        this.placeholderMap = new PlaceholderMap()
            .add(Placeholders.RARITY_ID, this::getId)
            .add(Placeholders.RARITY_NAME, this::getName)
            .add(Placeholders.RARITY_CHANCE, () -> NumberUtil.format(this.getChance()))
        ;
    }

    @NotNull
    public static Rarity read(@NotNull JYML cfg, @NotNull String path, @NotNull String id) {
        String name = cfg.getString(path + ".Name", StringUtil.capitalizeUnderscored(id));
        double chance = cfg.getDouble(path + ".Chance", 0D);

        return new Rarity(id, name, chance);
    }

    @Override
    @NotNull
    public PlaceholderMap getPlaceholders() {
        return this.placeholderMap;
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
        this.name = Colorizer.apply(name);
    }

    public double getChance() {
        return chance;
    }

    public void setChance(double chance) {
        this.chance = chance;
    }
}
