package su.nightexpress.excellentcrates.key;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.config.JYML;
import su.nexmedia.engine.api.manager.AbstractLoadableItem;
import su.nexmedia.engine.utils.ItemUtil;
import su.nexmedia.engine.utils.PDCUtil;
import su.nexmedia.engine.utils.StringUtil;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.Keys;
import su.nightexpress.excellentcrates.api.crate.ICrateKey;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.key.editor.KeyEditorKey;

import java.util.function.UnaryOperator;

public class CrateKey extends AbstractLoadableItem<ExcellentCrates> implements ICrateKey {

	private String name;
	private boolean isVirtual;
	private ItemStack item;
	
	private KeyEditorKey editor;
	
	public CrateKey(@NotNull ExcellentCrates plugin, @NotNull String id) {
		super(plugin, plugin.getDataFolder() + Config.DIR_KEYS + id.toLowerCase() + ".yml");
		
		this.setName("&6" + StringUtil.capitalizeFully(this.getId()) + " Crate Key");
		this.setVirtual(false);
		
		ItemStack item = new ItemStack(Material.TRIPWIRE_HOOK);
		ItemMeta meta = item.getItemMeta();
		if (meta != null) {
			meta.setDisplayName(this.getName());
			item.setItemMeta(meta);
		}
		this.setItem(item);
	}
	
	public CrateKey(@NotNull ExcellentCrates plugin, @NotNull JYML cfg) {
		super(plugin, cfg);
		
		this.setName(cfg.getString("Name", this.getId()));
		this.setVirtual(cfg.getBoolean("Virtual"));
		ItemStack item = cfg.getItemNew("Item", this.getId());
		if (item.getType().isAir() && !this.isVirtual()) {
			throw new IllegalStateException("Key item can not be AIR!");
		}
		this.setItem(item);
	}

	@Override
	@NotNull
	public UnaryOperator<String> replacePlaceholders() {
		return str -> str
			.replace(PLACEHOLDER_ID, this.getId())
			.replace(PLACEHOLDER_NAME, this.getName())
			.replace(PLACEHOLDER_VIRTUAL, plugin().lang().getBoolean(this.isVirtual()))
			.replace(PLACEHOLDER_ITEM_NAME, ItemUtil.getItemName(this.getItem()))
			;
	}

	@Override
	public void onSave() {
		cfg.set("Name", this.getName());
		cfg.set("Virtual", this.isVirtual());
		cfg.setItemNew("Item", this.getItem());
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
	public KeyEditorKey getEditor() {
		if (this.editor == null) {
			this.editor = new KeyEditorKey(plugin, this);
		}
		return this.editor;
	}

	@NotNull
	public String getName() {
		return name;
	}
	
	public void setName(@NotNull String name) {
		this.name = StringUtil.color(name);
	}
	
	public boolean isVirtual() {
		return isVirtual;
	}
	
	public void setVirtual(boolean isVirtual) {
		this.isVirtual = isVirtual;
	}
	
	@NotNull
	public ItemStack getItem() {
		return new ItemStack(item);
	}
	
	public void setItem(@NotNull ItemStack item) {
		this.item = new ItemStack(item);
		PDCUtil.setData(this.item, Keys.CRATE_KEY_ID, this.getId());
	}
}
