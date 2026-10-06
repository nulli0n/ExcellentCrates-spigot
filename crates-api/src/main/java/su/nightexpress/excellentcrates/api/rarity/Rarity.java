package su.nightexpress.excellentcrates.api.rarity;

import org.bukkit.Color;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifiable;
import su.nightexpress.excellentcrates.api.rarity.data.model.RarityBase;

@NullMarked
public interface Rarity extends Identifiable {

    RarityBase getBase();

    String getName();

    Color getColor();

    double getWeight();
}
