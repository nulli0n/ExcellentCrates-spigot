package su.nightexpress.excellentcrates.crates.block.vanilla.codec;

import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.crates.block.vanilla.block.VanillaBlockDefinition;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;

@NullMarked
public class VanillaBlockDefinitionCodec implements ConfigCodec<VanillaBlockDefinition> {

    public static final VanillaBlockDefinitionCodec INSTANCE = new VanillaBlockDefinitionCodec();

    @Override
    public VanillaBlockDefinition read(FileConfig config, String path) throws CodecReadException {
        Material blockType = config.getOrSet(path + ".block", ConfigCodecs.MATERIAL, Material.CHEST);
        Material itemType = config.getOrSet(path + ".item", ConfigCodecs.MATERIAL, Material.CHEST);

        return new VanillaBlockDefinition(blockType, itemType);
    }

    @Override
    public void write(FileConfig config, String path, VanillaBlockDefinition value) {
        config.set(path + ".block", value.getBlockType());
        config.set(path + ".item", value.getItemType());
    }
}
