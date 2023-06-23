package su.nightexpress.excellentcrates.crate.impl;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class OpenSettings {

    private boolean force;
    //private boolean bulk;
    private boolean skipAnimation;
    private ItemStack crateItem;
    private Block crateBlock;

    public OpenSettings() {

    }

    public boolean isForce() {
        return force;
    }

    public OpenSettings setForce(boolean force) {
        this.force = force;
        return this;
    }

    /*public boolean isBulk() {
        return bulk;
    }

    public OpenSettings setBulk(boolean bulk) {
        this.bulk = bulk;
        return this;
    }*/

    public boolean isSkipAnimation() {
        return skipAnimation;
    }

    public OpenSettings setSkipAnimation(boolean skipAnimation) {
        this.skipAnimation = skipAnimation;
        return this;
    }

    @Nullable
    public ItemStack getCrateItem() {
        return crateItem;
    }

    public OpenSettings setCrateItem(@Nullable ItemStack crateItem) {
        this.crateItem = crateItem;
        return this;
    }

    @Nullable
    public Block getCrateBlock() {
        return crateBlock;
    }

    public OpenSettings setCrateBlock(@Nullable Block crateBlock) {
        this.crateBlock = crateBlock;
        return this;
    }
}
