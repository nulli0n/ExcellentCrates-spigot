package su.nightexpress.engine.bukkit.particle.data;

import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bukkit.particle.ParticleDataAdapter;

@NullMarked
public class BlockDataParticleDataAdapter implements ParticleDataAdapter<BlockData, Material> {

    @Override
    public Material convertToTarget(BlockData source) {
        return source.getMaterial();
    }

    @Override
    public Class<BlockData> getSourceType() {
        return BlockData.class;
    }

    @Override
    public Class<Material> getTargetType() {
        return Material.class;
    }

    @Override
    public BlockData convertToSource(Material target) {
        return target.createBlockData();
    }
}
