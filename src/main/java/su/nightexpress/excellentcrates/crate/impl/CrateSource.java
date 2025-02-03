package su.nightexpress.excellentcrates.crate.impl;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.util.pos.WorldPos;

public class CrateSource {

    private final Crate     crate;
    private final ItemStack item;
    private final WorldPos  blockPos;

    public CrateSource(@NotNull Crate crate) {
        this(crate, null, (WorldPos) null);
    }

    public CrateSource(@NotNull Crate crate, @Nullable ItemStack item, @Nullable Block block) {
        this(crate, item, block == null ? null : WorldPos.from(block));
    }

    public CrateSource(@NotNull Crate crate, @Nullable ItemStack item, @Nullable WorldPos blockPos) {
        this.crate = crate;
        this.item = item;
        this.blockPos = blockPos;
    }

    public boolean hasItem() {
        return this.item != null;
    }

    public boolean hasBlock() {
        return this.blockPos != null;
    }

    @NotNull
    public Crate getCrate() {
        return this.crate;
    }

    @Nullable
    public ItemStack getItem() {
        return this.item;
    }

    @Nullable
    public WorldPos getBlockPos() {
        return this.blockPos;
    }

    @Nullable
    public Block getBlock() {
        return this.blockPos == null ? null : this.blockPos.toBlock();
    }

    @Override
    public String toString() {
        return "CrateSource{" +
            "crate=" + crate +
            ", item=" + item +
            ", block=" + blockPos +
            '}';
    }
}
