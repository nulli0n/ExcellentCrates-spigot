package su.nightexpress.excellentcrates.crates.block.position;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.block.position.CratePosition;
import su.nightexpress.nightcore.bridge.BukkitKeys;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;
import su.nightexpress.nightcore.util.geodata.pos.ExactPos;

@NullMarked
public record DefaultCratePosition(AdaptedKey worldKey, ExactPos position) implements CratePosition {

    public static DefaultCratePosition of(Location location) {
        return of(location.getWorld(), ExactPos.from(location));
    }

    public static DefaultCratePosition of(Block block) {
        return of(block.getWorld(), ExactPos.from(block));
    }

    public static DefaultCratePosition of(World world, ExactPos position) {
        return new DefaultCratePosition(BukkitKeys.getKey(world), position);
    }

}
