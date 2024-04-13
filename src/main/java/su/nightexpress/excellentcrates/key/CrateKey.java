package su.nightexpress.excellentcrates.key;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.config.Keys;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.manager.AbstractFileData;
import su.nightexpress.nightcore.util.PDCUtil;
import su.nightexpress.nightcore.util.placeholder.Placeholder;
import su.nightexpress.nightcore.util.placeholder.PlaceholderMap;
import su.nightexpress.nightcore.util.text.NightMessage;

import java.io.File;

public class CrateKey extends AbstractFileData<CratesPlugin> implements Placeholder {

    private String    name;
    private boolean   virtual;
    private ItemStack item;

    private final PlaceholderMap placeholderMap;

    public CrateKey(@NotNull CratesPlugin plugin, @NotNull File file) {
        super(plugin, file);
        this.placeholderMap = Placeholders.forKey(this);
    }

    @Override
    protected boolean onLoad(@NotNull FileConfig config) {
        this.setName(config.getString("Name", this.getId()));
        this.setVirtual(config.getBoolean("Virtual"));
        ItemStack item = config.getItem("Item");
        if (item.getType().isAir() && !this.isVirtual()) {
            item = new ItemStack(Material.TRIPWIRE_HOOK);
        }
        this.setItem(item);
        return true;
    }

    @Override
    protected void onSave(@NotNull FileConfig config) {
        config.set("Name", this.getName());
        config.set("Virtual", this.isVirtual());
        config.setItem("Item", this.getRawItem());
    }

    @Override
    @NotNull
    public PlaceholderMap getPlaceholders() {
        return this.placeholderMap;
    }

    @NotNull
    public String getName() {
        return name;
    }

    @NotNull
    public String getNameTranslated() {
        return NightMessage.asLegacy(this.getName());
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public boolean isVirtual() {
        return virtual;
    }

    public void setVirtual(boolean virtual) {
        this.virtual = virtual;
    }

    @NotNull
    public ItemStack getRawItem() {
        return new ItemStack(item);
    }

    @NotNull
    public ItemStack getItem() {
        ItemStack item = this.getRawItem();
        PDCUtil.set(item, Keys.keyId, this.getId());
        return item;
    }

    public void setItem(@NotNull ItemStack item) {
        this.item = new ItemStack(item);
    }
}
