package su.nightexpress.excellentcrates.crate.editor;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.editor.EditorButtonType;
import su.nexmedia.engine.api.editor.EditorInput;
import su.nexmedia.engine.api.menu.MenuClick;
import su.nexmedia.engine.api.menu.MenuItemType;
import su.nexmedia.engine.editor.AbstractEditorMenuAuto;
import su.nexmedia.engine.editor.EditorManager;
import su.nexmedia.engine.utils.ItemUtil;
import su.nexmedia.engine.utils.StringUtil;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.crate.Crate;
import su.nightexpress.excellentcrates.crate.CrateReward;
import su.nightexpress.excellentcrates.editor.CrateEditorMenu;
import su.nightexpress.excellentcrates.editor.CrateEditorType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class EditorCrateRewardList extends AbstractEditorMenuAuto<ExcellentCrates, Crate, CrateReward> {

    public EditorCrateRewardList(@NotNull Crate crate) {
        super(crate.plugin(), crate, CrateEditorMenu.TITLE_CRATE, 45);

        EditorInput<Crate, CrateEditorType> input = (player, crate2, type, e) -> {
            String msg = StringUtil.color(e.getMessage());

            if (type == CrateEditorType.REWARD_CREATE) {
                String id = EditorManager.fineId(msg);
                if (crate2.getReward(id) != null) {
                    EditorManager.error(player, plugin.getMessage(Lang.EDITOR_REWARD_ERROR_CREATE_EXIST).getLocalized());
                    return false;
                }
                CrateReward reward = new CrateReward(crate2, id);
                crate2.addReward(reward);
            }

            crate2.save();
            return true;
        };

        MenuClick click = (player, type, e) -> {
            if (type instanceof MenuItemType type2) {
                if (type2 == MenuItemType.RETURN) {
                    crate.getEditor().open(player, 1);
                }
                else this.onItemClickDefault(player, type2);
            }
            else if (type instanceof CrateEditorType type2) {
                if (type2 == CrateEditorType.REWARD_CREATE) {
                    ItemStack cursor = e.getCursor();
                    if (cursor != null && !cursor.getType().isAir()) {
                        String id = EditorManager.fineId(ItemUtil.getItemName(cursor));
                        int count = 0;
                        while (crate.getReward(count == 0 ? id : id + count) != null) {
                            count++;
                        }
                        CrateReward reward = new CrateReward(this.parent, count == 0 ? id : id + count);
                        reward.setName(ItemUtil.getItemName(cursor));
                        reward.getItems().add(new ItemStack(cursor));
                        reward.setPreview(cursor);
                        crate.addReward(reward);
                        crate.save();
                        e.getView().setCursor(null);
                        this.open(player, this.getPage(player));
                        return;
                    }

                    EditorManager.startEdit(player, crate, type2, input);
                    EditorManager.tip(player, plugin.getMessage(Lang.EDITOR_REWARD_ENTER_ID).getLocalized());
                    player.closeInventory();
                }
                else if (type2 == CrateEditorType.REWARD_SORT) {
                    Comparator<CrateReward> comparator;
                    if (e.isShiftClick()) {
                        //if (e.isLeftClick()) {
                            comparator = Comparator.comparing(r -> ItemUtil.getItemName(r.getPreview()));
                        //}
                    }
                    else if (e.isRightClick()) {
                        comparator = Comparator.comparing(r -> r.getPreview().getType().name());
                    }
                    else {
                        comparator = Comparator.comparingDouble(CrateReward::getChance).reversed();
                    }
                    crate.setRewards(crate.getRewards().stream().sorted(comparator).toList());
                    crate.save();
                    this.open(player, this.getPage(player));
                }
            }
        };

        this.loadItems(click);
    }

    @Override
    public void setTypes(@NotNull Map<EditorButtonType, Integer> map) {
        map.put(MenuItemType.RETURN, 40);
        map.put(MenuItemType.PAGE_PREVIOUS, 36);
        map.put(MenuItemType.PAGE_NEXT, 44);
        map.put(CrateEditorType.REWARD_CREATE, 42);
        map.put(CrateEditorType.REWARD_SORT, 38);
    }

    @Override
    public int[] getObjectSlots() {
        return IntStream.range(0, 36).toArray();
    }

    @Override
    @NotNull
    protected List<CrateReward> getObjects(@NotNull Player player) {
        return new ArrayList<>(this.parent.getRewards());
    }

    @Override
    @NotNull
    protected ItemStack getObjectStack(@NotNull Player player, @NotNull CrateReward reward) {
        ItemStack item = new ItemStack(reward.getPreview());
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;

        ItemStack object = CrateEditorType.REWARD_OBJECT.getItem();
        meta.setDisplayName(ItemUtil.getItemName(object));
        meta.setLore(ItemUtil.getLore(object));
        meta.addItemFlags(ItemFlag.values());
        item.setItemMeta(meta);

        ItemUtil.replace(item, reward.replacePlaceholders());
        return item;
    }

    @Override
    @NotNull
    protected MenuClick getObjectClick(@NotNull Player player, @NotNull CrateReward reward) {
        return (player1, type, e) -> {
            if (e.getClick() == ClickType.DROP) {
                this.parent.removeReward(reward);
                this.parent.save();
                this.open(player1, this.getPage(player1));
                return;
            }

            if (e.isShiftClick()) {
                // Reward position move.
                List<CrateReward> all = new ArrayList<>(this.parent.getRewards());
                int index = all.indexOf(reward);
                int allSize = all.size();

                if (e.isLeftClick()) {
                    if (index + 1 >= allSize) return;

                    all.remove(index);
                    all.add(index + 1, reward);
                }
                else if (e.isRightClick()) {
                    if (index == 0) return;

                    all.remove(index);
                    all.add(index - 1, reward);
                }
                this.parent.setRewards(all);
                this.parent.save();
                this.open(player1, this.getPage(player1));
                return;
            }

            if (e.isLeftClick()) {
                reward.getEditor().open(player1, 1);
            }
        };
    }

    @Override
    public boolean cancelClick(@NotNull InventoryClickEvent e, @NotNull SlotType slotType) {
        return slotType != SlotType.EMPTY_PLAYER && slotType != SlotType.PLAYER;
    }

    @Override
    public boolean cancelClick(@NotNull InventoryDragEvent inventoryDragEvent) {
        return true;
    }
}
