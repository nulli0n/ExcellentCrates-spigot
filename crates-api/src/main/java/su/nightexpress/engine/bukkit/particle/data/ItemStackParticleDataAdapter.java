package su.nightexpress.engine.bukkit.particle.data;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bukkit.particle.ParticleDataAdapter;
import su.nightexpress.nightcore.util.bukkit.NightItem;

@NullMarked
public class ItemStackParticleDataAdapter implements ParticleDataAdapter<ItemStack, NightItem> {

    @Override
    public ItemStack convertToSource(NightItem target) {
        return target.getItemStack();
    }

    @Override
    public NightItem convertToTarget(ItemStack source) {
        return NightItem.fromItemStack(source);
    }

    @Override
    public Class<ItemStack> getSourceType() {
        return ItemStack.class;
    }

    @Override
    public Class<NightItem> getTargetType() {
        return NightItem.class;
    }
}
