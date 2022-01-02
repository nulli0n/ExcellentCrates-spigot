package su.nightexpress.excellentcrates.editor;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.config.JYML;
import su.nexmedia.engine.api.menu.AbstractMenu;
import su.nexmedia.engine.api.menu.IMenuClick;
import su.nexmedia.engine.api.menu.IMenuItem;
import su.nexmedia.engine.api.menu.MenuItemType;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.crate.editor.CrateEditorList;
import su.nightexpress.excellentcrates.key.editor.KeyEditorList;

public class CrateEditorHub extends AbstractMenu<ExcellentCrates> {

	private CrateEditorList crateEditorList;
	private KeyEditorList keyEditorKeys;
	
	public CrateEditorHub(@NotNull ExcellentCrates plugin, @NotNull JYML cfg) {
		super(plugin, cfg, "");
		
		IMenuClick click = (player, type, e) -> {
			if (type instanceof MenuItemType type2) {
				if (type2 == MenuItemType.CLOSE) {
					player.closeInventory();
				}
			}
			else if (type instanceof CrateEditorType type2) {
				if (type2 == CrateEditorType.EDITOR_CRATES) {
					this.getCratesEditor().open(player, 1);
					return;
				}
				if (type2 == CrateEditorType.EDITOR_KEYS) {
					this.getKeysEditor().open(player, 1);
				}
			}
		};
		
		for (String id : cfg.getSection("Content")) {
			IMenuItem menuItem = cfg.getMenuItem("Content." + id, MenuItemType.class);
			
			if (menuItem.getType() != null) {
				menuItem.setClick(click);
			}
			this.addItem(menuItem);
		}
		
		for (String id : cfg.getSection("Editor")) {
			IMenuItem menuItem = cfg.getMenuItem("Editor." + id, CrateEditorType.class);
			
			if (menuItem.getType() != null) {
				menuItem.setClick(click);
			}
			this.addItem(menuItem);
		}
	}
	
	@NotNull
	public CrateEditorList getCratesEditor() {
		if (this.crateEditorList == null) {
			this.crateEditorList = new CrateEditorList(this.plugin);
		}
		return this.crateEditorList;
	}
	
	@NotNull
	public KeyEditorList getKeysEditor() {
		if (this.keyEditorKeys == null) {
			this.keyEditorKeys = new KeyEditorList(this.plugin);
		}
		return this.keyEditorKeys;
	}

	@Override
	public void onPrepare(@NotNull Player player, @NotNull Inventory inventory) {

	}

	@Override
	public void onReady(@NotNull Player player, @NotNull Inventory inventory) {

	}

	@Override
	public boolean cancelClick(@NotNull SlotType slotType, int slot) {
		return true;
	}
}
