package su.nightexpress.excellentcrates.crate.impl;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CrateSource {

    private final Crate     crate;
    private final ItemStack item;
    private final Block     block;

    public CrateSource(@NotNull Crate crate) {
        this(crate, null, null);
    }

    public CrateSource(@NotNull Crate crate, @Nullable ItemStack item, @Nullable Block block) {
        this.crate = crate;
        this.item = item;
        this.block = block;
    }

    public boolean hasItem() {
        return this.item != null;
    }

    public boolean hasBlock() {
        return this.block != null;
    }

    @NotNull
    public Crate getCrate() {
        return crate;
    }

    @Nullable
    public ItemStack getItem() {
        return item;
    }

    @Nullable
    public Block getBlock() {
        return block;
    }
}
