package su.nightexpress.excellentcrates.api.crate.pipeline.component;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface CrateSourcePipelineComponent extends LoggablePipelineComponent {

    @Nullable
    Location getLocation();

    void setLocation(@Nullable Location location);

    @Nullable
    ItemStack getItemStack();

    void setItemStack(@Nullable ItemStack itemStack);
}
