package su.nightexpress.excellentcrates.crate.editor;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.editor.EditorButtonType;
import su.nexmedia.engine.api.editor.EditorInput;
import su.nexmedia.engine.api.menu.AbstractMenu;
import su.nexmedia.engine.api.menu.MenuClick;
import su.nexmedia.engine.api.menu.MenuItem;
import su.nexmedia.engine.api.menu.MenuItemType;
import su.nexmedia.engine.editor.AbstractEditorMenu;
import su.nexmedia.engine.editor.EditorManager;
import su.nexmedia.engine.utils.ItemUtil;
import su.nexmedia.engine.utils.PlayerUtil;
import su.nexmedia.engine.utils.StringUtil;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.crate.Crate;
import su.nightexpress.excellentcrates.crate.CrateReward;
import su.nightexpress.excellentcrates.editor.CrateEditorType;

import java.util.Map;
import java.util.stream.Stream;

public class EditorCrateReward extends AbstractEditorMenu<ExcellentCrates, CrateReward> {

    public EditorCrateReward(@NotNull CrateReward reward) {
        super(reward.plugin(), reward, Config.EDITOR_TITLE_CRATE.get(), 45);
        Crate crate = reward.getCrate();

        EditorInput<CrateReward, CrateEditorType> input = (player, reward2, type, e) -> {
            String msg = StringUtil.colorOff(e.getMessage());
            switch (type) {
                case REWARD_CHANGE_CHANCE -> {
                    double chance = StringUtil.getDouble(msg, -1);
                    if (chance < 0) {
                        EditorManager.error(player, plugin.getMessage(Lang.EDITOR_ERROR_NUMBER_GENERIC).getLocalized());
                        return false;
                    }
                    reward.setChance(chance);
                }
                case REWARD_CHANGE_COMMANDS -> reward.getCommands().add(msg);
                case REWARD_CHANGE_IGNORED_FOR_PERMISSIONS -> reward.getIgnoredForPermissions().add(msg);
                case REWARD_CHANGE_NAME -> reward.setName(msg);
                case REWARD_CHANGE_WIN_LIMITS_AMOUNT -> reward.setWinLimitAmount(StringUtil.getInteger(msg, -1, true));
                case REWARD_CHANGE_WIN_LIMITS_COOLDOWN -> reward.setWinLimitCooldown(StringUtil.getInteger(msg, 0, true));
                default -> { }
            }

            reward.getCrate().save();
            return true;
        };

        MenuClick click = (player, type, e) -> {
            ClickType clickType = e.getClick();
            if (type instanceof MenuItemType type2) {
                if (type2 == MenuItemType.RETURN) {
                    crate.getEditor().getEditorRewards().open(player, 1);
                }
                return;
            }

            if (type instanceof CrateEditorType type2) {
                switch (type2) {
                    case REWARD_CHANGE_NAME -> {
                        if (e.isRightClick()) {
                            reward.setName(ItemUtil.getItemName(reward.getPreview()));
                            break;
                        }
                        EditorManager.startEdit(player, reward, type2, input);
                        EditorManager.tip(player, plugin.getMessage(Lang.EDITOR_REWARD_ENTER_DISPLAY_NAME).getLocalized());
                        player.closeInventory();
                        return;
                    }
                    case REWARD_CHANGE_PREVIEW -> {
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
                    case REWARD_CHANGE_BROADCAST -> reward.setBroadcast(!reward.isBroadcast());
                    case REWARD_CHANGE_ITEMS -> {
                        new ContentEditor(reward).open(player, 1);
                        return;
                    }
                    case REWARD_CHANGE_CHANCE -> {
                        EditorManager.startEdit(player, reward, type2, input);
                        EditorManager.tip(player, plugin.getMessage(Lang.EDITOR_REWARD_ENTER_CHANCE).getLocalized());
                        player.closeInventory();
                        return;
                    }
                    case REWARD_CHANGE_COMMANDS -> {
                        if (e.isRightClick()) {
                            reward.getCommands().clear();
                        }
                        else {
                            EditorManager.startEdit(player, reward, type2, input);
                            EditorManager.tip(player, plugin.getMessage(Lang.EDITOR_REWARD_ENTER_COMMAND).getLocalized());
                            EditorManager.sendCommandTips(player);
                            player.closeInventory();
                            return;
                        }
                    }
                    case REWARD_CHANGE_IGNORED_FOR_PERMISSIONS -> {
                        if (e.isRightClick()) {
                            reward.getIgnoredForPermissions().clear();
                        }
                        else {
                            EditorManager.startEdit(player, reward, type2, input);
                            EditorManager.tip(player, plugin.getMessage(Lang.EDITOR_REWARD_ENTER_COMMAND).getLocalized());
                            player.closeInventory();
                            return;
                        }
                    }
                    case REWARD_CHANGE_WIN_LIMITS -> {
                        if (e.getClick() == ClickType.DROP) {
                            reward.setWinLimitAmount(-1);
                            reward.setWinLimitCooldown(0);
                            break;
                        }
                        if (e.isLeftClick()) {
                            EditorManager.startEdit(player, reward, CrateEditorType.REWARD_CHANGE_WIN_LIMITS_AMOUNT, input);
                            EditorManager.tip(player, plugin.getMessage(Lang.EDITOR_REWARD_ENTER_WIN_LIMIT_AMOUNT).getLocalized());
                        }
                        else {
                            EditorManager.startEdit(player, reward, CrateEditorType.REWARD_CHANGE_WIN_LIMITS_COOLDOWN, input);
                            EditorManager.tip(player, plugin.getMessage(Lang.EDITOR_REWARD_ENTER_WIN_LIMIT_COOLDOWN).getLocalized());
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

        this.loadItems(click);
    }

    @Override
    public void setTypes(@NotNull Map<EditorButtonType, Integer> map) {
        map.put(MenuItemType.RETURN, 40);

        map.put(CrateEditorType.REWARD_CHANGE_PREVIEW, 4);

        map.put(CrateEditorType.REWARD_CHANGE_NAME, 11);
        map.put(CrateEditorType.REWARD_CHANGE_BROADCAST, 12);
        map.put(CrateEditorType.REWARD_CHANGE_CHANCE, 13);
        map.put(CrateEditorType.REWARD_CHANGE_COMMANDS, 14);
        map.put(CrateEditorType.REWARD_CHANGE_ITEMS, 15);
        map.put(CrateEditorType.REWARD_CHANGE_IGNORED_FOR_PERMISSIONS, 16);

        map.put(CrateEditorType.REWARD_CHANGE_WIN_LIMITS, 22);
    }

    @Override
    public void onItemPrepare(@NotNull Player player, @NotNull MenuItem menuItem, @NotNull ItemStack item) {
        super.onItemPrepare(player, menuItem, item);

        if (menuItem.getType() == CrateEditorType.REWARD_CHANGE_PREVIEW) {
            item.setType(this.object.getPreview().getType());
            item.setAmount(this.object.getPreview().getAmount());
        }

        ItemUtil.replace(item, this.object.replacePlaceholders());
    }

    @Override
    public boolean cancelClick(@NotNull InventoryClickEvent e, @NotNull SlotType slotType) {
        return slotType != SlotType.PLAYER && slotType != SlotType.EMPTY_PLAYER;
    }

    @Override
    public boolean cancelClick(@NotNull InventoryDragEvent inventoryDragEvent) {
        return true;
    }

    static class ContentEditor extends AbstractMenu<ExcellentCrates> {

        private final CrateReward reward;

        public ContentEditor(@NotNull CrateReward reward) {
            super(reward.getCrate().plugin(), "Reward Content", 27);
            this.reward = reward;
        }

        @Override
        public boolean destroyWhenNoViewers() {
            return true;
        }

        @Override
        public boolean cancelClick(@NotNull InventoryClickEvent e, @NotNull SlotType slotType) {
            return false;
        }

        @Override
        public boolean cancelClick(@NotNull InventoryDragEvent inventoryDragEvent) {
            return false;
        }

        @Override
        public boolean onPrepare(@NotNull Player player, @NotNull Inventory inventory) {
            inventory.setContents(this.reward.getItems().stream().map(ItemStack::new).toList().toArray(new ItemStack[0]));
            return true;
        }

        @Override
        public boolean onReady(@NotNull Player player, @NotNull Inventory inventory) {
            return true;
        }

        @Override
        public void onClose(@NotNull Player player, @NotNull InventoryCloseEvent e) {
            Inventory inventory = e.getInventory();
            this.reward.setItems(Stream.of(inventory.getContents()).toList());
            this.reward.getCrate().save();
            super.onClose(player, e);

            plugin.runTask(c -> this.reward.getEditor().open(player, 1), false);
        }
    }
}
