package su.nightexpress.excellentcrates.crates.pipeline.component;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentcrates.api.crate.pipeline.component.CrateSourcePipelineComponent;

@NullMarked
public class DefaultCrateSourcePipelineComponent implements CrateSourcePipelineComponent {

    private @Nullable Location  location;
    private @Nullable ItemStack itemStack;

    public DefaultCrateSourcePipelineComponent() {
        this(null, null);
    }

    public DefaultCrateSourcePipelineComponent(@Nullable Location location, @Nullable ItemStack itemStack) {
        this.location = location;
        this.itemStack = itemStack;
    }

    @Override
    public Map<String, String> getLogData() {
        Map<String, String> logData = new HashMap<>();
        if (this.location != null) {
            logData.put("location", formatLocation(this.location));
        }
        if (this.itemStack != null) {
            logData.put("itemStack", this.itemStack.toString());
        }
        return logData;
    }

    private String formatLocation(Location location) {
        return String.format("World: %s, X: %d, Y: %d, Z: %d",
            location.getWorld().getName(),
            location.getBlockX(),
            location.getBlockY(),
            location.getBlockZ()
        );
    }

    @Override
    public @Nullable Location getLocation() {
        return this.location;
    }

    @Override
    public void setLocation(@Nullable Location location) {
        this.location = location;
    }

    @Override
    public @Nullable ItemStack getItemStack() {
        return this.itemStack;
    }

    @Override
    public void setItemStack(@Nullable ItemStack itemStack) {
        this.itemStack = itemStack;
    }
}
