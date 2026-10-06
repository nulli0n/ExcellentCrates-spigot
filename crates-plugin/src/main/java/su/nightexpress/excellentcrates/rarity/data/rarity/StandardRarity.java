package su.nightexpress.excellentcrates.rarity.data.rarity;

import org.bukkit.Color;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.rarity.Rarity;

@NullMarked
public class StandardRarity implements Rarity {

    private final Identifier         id;
    private final StandardRarityBase base;

    public StandardRarity(Identifier id, StandardRarityBase base) {
        this.id = id;
        this.base = base;
    }

    @Override
    public Identifier getId() {
        return this.id;
    }

    public StandardRarityBase getBase() {
        return this.base;
    }

    @Override
    public String getName() {
        return this.base.getName();
    }

    @Override
    public Color getColor() {
        return this.base.getColor();
    }

    @Override
    public double getWeight() {
        return this.base.getWeight();
    }
}