package su.nightexpress.excellentcrates.core.util;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

import su.nightexpress.nightcore.util.Randomizer;

public final class FireworkUtil {

    private FireworkUtil() {
    }

    public static Firework createRandom(Location location) {
        World world = location.getWorld();
        if (world == null) return null;

        Firework firework = world.spawn(location, Firework.class);
        FireworkMeta meta = firework.getFireworkMeta();
        FireworkEffect.Type type = Randomizer.pick(FireworkEffect.Type.values());
        FireworkEffect effect = FireworkEffect.builder()
            .flicker(Randomizer.nextBoolean())
            .withColor(Color.fromBGR(random256(), random256(), random256()))
            .withFade(Color.fromBGR(random256(), random256(), random256()))
            .with(type)
            .trail(Randomizer.nextBoolean())
            .build();

        meta.addEffect(effect);
        meta.setPower(Randomizer.nextInt(4));
        firework.setFireworkMeta(meta);
        return firework;
    }

    private static int random256() {
        return Randomizer.nextInt(256);
    }
}
