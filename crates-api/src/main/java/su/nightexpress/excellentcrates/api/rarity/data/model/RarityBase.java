package su.nightexpress.excellentcrates.api.rarity.data.model;

import org.bukkit.Color;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface RarityBase {

    String getName();

    void setName(String name);

    double getWeight();

    void setWeight(double weight);

    Color getColor();

    void setColor(Color color);
}
