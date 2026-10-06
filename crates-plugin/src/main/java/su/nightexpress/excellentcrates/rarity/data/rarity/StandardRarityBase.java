package su.nightexpress.excellentcrates.rarity.data.rarity;

import org.bukkit.Color;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.rarity.data.model.RarityBase;

@NullMarked
public class StandardRarityBase implements RarityBase {

    private String name;
    private double weight;
    private Color  color;

    public StandardRarityBase(String name, double weight, Color color) {
        this.name = name;
        this.weight = weight;
        this.color = color;
    }

    public static StandardRarityBase createDefault() {
        return new StandardRarityBase("Unnamed", 100, Color.WHITE);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public double getWeight() {
        return this.weight;
    }

    @Override
    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public Color getColor() {
        return this.color;
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }
}
