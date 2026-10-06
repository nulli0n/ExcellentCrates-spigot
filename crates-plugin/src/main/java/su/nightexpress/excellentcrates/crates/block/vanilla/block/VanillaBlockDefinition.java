package su.nightexpress.excellentcrates.crates.block.vanilla.block;

import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class VanillaBlockDefinition {

    private final Material blockType;
    private final Material itemType;

    public VanillaBlockDefinition(Material blockType, Material itemType) {
        this.blockType = blockType;
        this.itemType = itemType;
    }

    public Material getBlockType() {
        return blockType;
    }

    public Material getItemType() {
        return itemType;
    }
}
