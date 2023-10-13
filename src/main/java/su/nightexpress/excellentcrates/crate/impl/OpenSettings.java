package su.nightexpress.excellentcrates.crate.impl;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OpenSettings {

    private boolean force;
    private boolean skipAnimation;
    private ItemStack crateItem;
    private Block crateBlock;

    public OpenSettings() {

    }

    @NotNull
    public static OpenSettings create(@Nullable Block block, @Nullable ItemStack item) {
        return new OpenSettings().setCrateBlock(block).setCrateItem(item);
    }

    public boolean isForce() {
        return force;
    }

    @NotNull
    public OpenSettings setForce(boolean force) {
        this.force = force;
        return this;
    }

    public boolean isSkipAnimation() {
        return skipAnimation;
    }

    @NotNull
    public OpenSettings setSkipAnimation(boolean skipAnimation) {
        this.skipAnimation = skipAnimation;
        return this;
    }

    @Nullable
    public ItemStack getCrateItem() {
        return crateItem;
    }

    @NotNull
    public OpenSettings setCrateItem(@Nullable ItemStack crateItem) {
        this.crateItem = crateItem;
        return this;
    }

    @Nullable
    public Block getCrateBlock() {
        return crateBlock;
    }

    @NotNull
    public OpenSettings setCrateBlock(@Nullable Block crateBlock) {
        this.crateBlock = crateBlock;
        return this;
    }
}
