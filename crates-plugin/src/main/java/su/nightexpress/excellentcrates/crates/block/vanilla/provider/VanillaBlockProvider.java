package su.nightexpress.excellentcrates.crates.block.vanilla.provider;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.crate.block.provider.BlockProvider;

@NullMarked
public class VanillaBlockProvider implements BlockProvider {

    public static final Identifier ID = new Identifier("vanilla");

    @Override
    public boolean canHandle(Location location) {
        return true;
    }

    @Override
    public boolean isBlock(ItemStack itemStack) {
        return itemStack.getType().isBlock();
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public Identifier getId() {
        return ID;
    }
}
