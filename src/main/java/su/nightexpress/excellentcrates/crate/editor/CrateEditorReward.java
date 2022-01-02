package su.nightexpress.excellentcrates.crate.editor;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.menu.AbstractMenu;
import su.nexmedia.engine.api.menu.IMenuClick;
import su.nexmedia.engine.api.menu.IMenuItem;
import su.nexmedia.engine.api.menu.MenuItemType;
import su.nexmedia.engine.utils.EditorUtils;
import su.nexmedia.engine.utils.ItemUtil;
import su.nexmedia.engine.utils.PlayerUtil;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.api.crate.ICrate;
import su.nightexpress.excellentcrates.api.crate.ICrateReward;
import su.nightexpress.excellentcrates.editor.CrateEditorHandler;
import su.nightexpress.excellentcrates.editor.CrateEditorType;

import java.util.Arrays;

public class CrateEditorReward extends AbstractMenu<ExcellentCrates> {

	private final ICrateReward reward;
	
	public CrateEditorReward(@NotNull ExcellentCrates plugin, @NotNull ICrateReward reward) {
		super(plugin, CrateEditorHandler.CRATE_REWARD_MAIN, "");
		this.reward = reward;
		ICrate crate = reward.getCrate();
		
		IMenuClick click = (player, type, e) -> {
			ClickType clickType = e.getClick();
			if (type instanceof MenuItemType type2) {
				if (type2 == MenuItemType.RETURN) {
					if (crate.getEditor() instanceof CrateEditorCrate editorCrate) {
						editorCrate.getEditorRewards().open(player, 1);
					}
				}
				return;
			}
			
			if (type instanceof CrateEditorType type2) {
				switch (type2) {
					case CRATE_REWARD_DELETE -> {
						if (!e.isShiftClick()) return;

						reward.clear();
						crate.removeReward(reward);
						crate.save();
						if (crate.getEditor() instanceof CrateEditorCrate editorCrate) {
							editorCrate.getEditorRewards().open(player, 1);
						}
						return;
					}
					case CRATE_REWARD_CHANGE_NAME -> {
						if (e.isRightClick()) {
							reward.setName(ItemUtil.getItemName(reward.getPreview()));
							break;
						}
						plugin.getEditorHandlerNew().startEdit(player, reward, type2);
						EditorUtils.tipCustom(player, plugin.lang().Editor_Reward_Enter_DisplayName.getMsg());
						player.closeInventory();
						return;
					}
					case CRATE_REWARD_CHANGE_PREVIEW -> {
						if (e.isRightClick()) {
							PlayerUtil.addItem(player, reward.getPreview());
							return;
						}
						ItemStack cursor = e.getCursor();
						if (cursor != null && !cursor.getType().isAir()) {
							reward.setPreview(cursor);
							e.getView().setCursor(null);
						}
					}
					case CRATE_REWARD_CHANGE_BROADCAST -> reward.setBroadcast(!reward.isBroadcast());
					case CRATE_REWARD_CHANGE_ITEMS -> {
						new ContentEditor(reward).open(player, 1);
						return;
					}
					case CRATE_REWARD_CHANGE_CHANCE -> {
						plugin.getEditorHandlerNew().startEdit(player, reward, type2);
						EditorUtils.tipCustom(player, plugin.lang().Editor_Reward_Enter_Chance.getMsg());
						player.closeInventory();
						return;
					}
					case CRATE_REWARD_CHANGE_COMMANDS -> {
						if (e.isRightClick()) {
							reward.getCommands().clear();
						}
						else {
							plugin.getEditorHandlerNew().startEdit(player, reward, type2);
							EditorUtils.tipCustom(player, plugin.lang().Editor_Reward_Enter_Command.getMsg());
							EditorUtils.sendCommandTips(player);
							player.closeInventory();
							return;
						}
					}
					case CRATE_REWARD_CHANGE_WIN_LIMITS -> {
						if (e.isLeftClick()) {
							plugin.getEditorHandlerNew().startEdit(player, reward, CrateEditorType.CRATE_REWARD_CHANGE_WIN_LIMITS_AMOUNT);
							EditorUtils.tipCustom(player, plugin.lang().Editor_Reward_Enter_WinLimit_Amount.getMsg());
						}
						else {
							plugin.getEditorHandlerNew().startEdit(player, reward, CrateEditorType.CRATE_REWARD_CHANGE_WIN_LIMITS_COOLDOWN);
							EditorUtils.tipCustom(player, plugin.lang().Editor_Reward_Enter_WinLimit_Cooldown.getMsg());
						}
						player.closeInventory();
						return;
					}
					default -> { }
				}
				crate.save();
				this.open(player, 1);
			}
		};

		for (String sId : cfg.getSection("Content")) {
			IMenuItem menuItem = cfg.getMenuItem("Content." + sId, MenuItemType.class);
			
			if (menuItem.getType() != null) {
				menuItem.setClick(click);
			}
			this.addItem(menuItem);
		}
		
		for (String sId : cfg.getSection("Editor")) {
			IMenuItem menuItem = cfg.getMenuItem("Editor." + sId, CrateEditorType.class);
			
			if (menuItem.getType() != null) {
				menuItem.setClick(click);
			}
			this.addItem(menuItem);
		}
	}

	@Override
	public void onItemPrepare(@NotNull Player player, @NotNull IMenuItem menuItem, @NotNull ItemStack item) {
		super.onItemPrepare(player, menuItem, item);

		if (menuItem.getType() == CrateEditorType.CRATE_REWARD_CHANGE_PREVIEW) {
			item.setType(this.reward.getPreview().getType());
			item.setAmount(this.reward.getPreview().getAmount());
		}

		ItemUtil.replace(item, this.reward.replacePlaceholders());
	}

	@Override
	public void onPrepare(@NotNull Player player, @NotNull Inventory inventory) {

	}

	@Override
	public void onReady(@NotNull Player player, @NotNull Inventory inventory) {

	}

	@Override
	public boolean cancelClick(@NotNull SlotType slotType, int slot) {
		return slotType != SlotType.PLAYER && slotType != SlotType.EMPTY_PLAYER;
	}

	static class ContentEditor extends AbstractMenu<ExcellentCrates> {

		private final ICrateReward reward;

		public ContentEditor(@NotNull ICrateReward reward) {
			super(reward.getCrate().plugin(), "Reward Content", 27);
			this.reward = reward;
		}

		@Override
		public void onPrepare(@NotNull Player player, @NotNull Inventory inventory) {
			inventory.setContents(this.reward.getItems().toArray(new ItemStack[0]));
		}

		@Override
		public void onReady(@NotNull Player player, @NotNull Inventory inventory) {

		}

		@Override
		public void onClose(@NotNull Player player, @NotNull InventoryCloseEvent e) {
			Inventory inventory = e.getInventory();
			ItemStack[] items = new ItemStack[this.getSize()];

			for (int slot = 0; slot < items.length; slot++) {
				ItemStack item = inventory.getItem(slot);
				if (item == null) continue;

				items[slot] = item;
			}

			this.reward.setItems(Arrays.asList(items));
			this.reward.getCrate().save();
			super.onClose(player, e);

			plugin.runTask(c -> this.reward.getEditor().open(player, 1), false);
		}

		@Override
		public boolean destroyWhenNoViewers() {
			return true;
		}

		@Override
		public boolean cancelClick(@NotNull SlotType slotType, int slot) {
			return false;
		}
	}
}
