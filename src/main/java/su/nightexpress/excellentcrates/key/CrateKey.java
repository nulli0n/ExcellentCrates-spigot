package su.nightexpress.excellentcrates.key;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.config.JYML;
import su.nexmedia.engine.api.manager.AbstractConfigHolder;
import su.nexmedia.engine.api.manager.ICleanable;
import su.nexmedia.engine.api.placeholder.Placeholder;
import su.nexmedia.engine.api.placeholder.PlaceholderMap;
import su.nexmedia.engine.lang.LangManager;
import su.nexmedia.engine.utils.Colorizer;
import su.nexmedia.engine.utils.ItemUtil;
import su.nexmedia.engine.utils.PDCUtil;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.Keys;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.key.editor.KeyMainEditor;

public class CrateKey extends AbstractConfigHolder<ExcellentCrates> implements ICleanable, Placeholder {

    private String    name;
    private boolean   isVirtual;
    private ItemStack item;

    private KeyMainEditor editor;

    private final PlaceholderMap placeholderMap;

    public CrateKey(@NotNull ExcellentCrates plugin, @NotNull JYML cfg) {
        super(plugin, cfg);

        this.placeholderMap = new PlaceholderMap()
            .add(Placeholders.KEY_ID, this::getId)
            .add(Placeholders.KEY_NAME, this::getName)
            .add(Placeholders.KEY_VIRTUAL, () -> LangManager.getBoolean(this.isVirtual()))
            .add(Placeholders.KEY_ITEM_NAME, () -> ItemUtil.getItemName(this.getItem()))
        ;
    }

    @Override
    public boolean load() {
        this.setName(cfg.getString("Name", this.getId()));
        this.setVirtual(cfg.getBoolean("Virtual"));
        ItemStack item = cfg.getItem("Item");
        if (item.getType().isAir() && !this.isVirtual()) {
            item = new ItemStack(Material.TRIPWIRE_HOOK);
        }
        this.setItem(item);
        return true;
    }

    @Override
    public void onSave() {
        cfg.set("Name", this.getName());
        cfg.set("Virtual", this.isVirtual());
        cfg.setItem("Item", this.getRawItem());
    }

    @Override
    public void clear() {
        if (this.editor != null) {
            this.editor.clear();
            this.editor = null;
        }
    }

    @Override
    @NotNull
    public PlaceholderMap getPlaceholders() {
        return this.placeholderMap;
    }

    @NotNull
    public KeyMainEditor getEditor() {
        if (this.editor == null) {
            this.editor = new KeyMainEditor(this);
        }
        return this.editor;
    }

    @NotNull
    public String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = Colorizer.apply(name);
    }

    public boolean isVirtual() {
        return isVirtual;
    }

    public void setVirtual(boolean isVirtual) {
        this.isVirtual = isVirtual;
    }

    @NotNull
    public ItemStack getRawItem() {
        return new ItemStack(item);
    }

    @NotNull
    public ItemStack getItem() {
        ItemStack item = this.getRawItem();
        PDCUtil.set(item, Keys.CRATE_KEY_ID, this.getId());
        return item;
    }

    public void setItem(@NotNull ItemStack item) {
        this.item = new ItemStack(item);
    }
}
