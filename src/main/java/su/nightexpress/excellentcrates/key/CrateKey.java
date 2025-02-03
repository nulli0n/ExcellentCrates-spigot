package su.nightexpress.excellentcrates.key;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.api.item.ItemProvider;
import su.nightexpress.excellentcrates.config.Keys;
import su.nightexpress.excellentcrates.item.ItemTypes;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.manager.AbstractFileData;
import su.nightexpress.nightcore.util.ItemUtil;
import su.nightexpress.nightcore.util.PDCUtil;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.text.NightMessage;

import java.io.File;
import java.util.function.UnaryOperator;

public class CrateKey extends AbstractFileData<CratesPlugin> {

    private String       name;
    private boolean      virtual;
    private ItemProvider provider;

    public CrateKey(@NotNull CratesPlugin plugin, @NotNull File file) {
        super(plugin, file);
    }

    @Override
    protected boolean onLoad(@NotNull FileConfig config) {
        this.setName(config.getString("Name", this.getId()));
        this.setVirtual(config.getBoolean("Virtual"));

        if (config.contains("Item")) {
            NightItem item = config.getCosmeticItem("Item");
            ItemProvider provider = ItemTypes.vanilla(item.getItemStack());

            config.remove("Item");
            config.set("ItemData", provider);
        }

        this.setProvider(ItemTypes.read(config, "ItemData"));
        return true;
    }

    @Override
    protected void onSave(@NotNull FileConfig config) {
        config.set("Name", this.name);
        config.set("Virtual", this.virtual);
        config.set("ItemData", this.provider);
    }

    @NotNull
    public UnaryOperator<String> replacePlaceholders() {
        return Placeholders.KEY.replacer(this);
    }

    @NotNull
    public String getName() {
        return this.name;
    }

    @NotNull
    public String getNameTranslated() {
        return NightMessage.asLegacy(this.name);
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public boolean isVirtual() {
        return this.virtual;
    }

    public void setVirtual(boolean virtual) {
        this.virtual = virtual;
    }



    @NotNull
    public ItemStack getRawItem() {
        ItemStack itemStack = this.provider.getItemStack();
        ItemUtil.editMeta(itemStack, meta -> {
            meta.setDisplayName(this.getNameTranslated());
        });
        return itemStack;
    }

    @NotNull
    public ItemStack getItem() {
        ItemStack item = this.getRawItem();
        PDCUtil.set(item, Keys.keyId, this.getId());
        return item;
    }

    @NotNull
    public ItemProvider getProvider() {
        return this.provider;
    }

    public void setProvider(@NotNull ItemProvider provider) {
        this.provider = provider;
    }
}
