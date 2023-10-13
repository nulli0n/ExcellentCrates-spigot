package su.nightexpress.excellentcrates.crate.editor;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nexmedia.engine.api.menu.AutoPaged;
import su.nexmedia.engine.api.menu.click.ItemClick;
import su.nexmedia.engine.api.menu.impl.EditorMenu;
import su.nexmedia.engine.api.menu.impl.MenuOptions;
import su.nexmedia.engine.api.menu.impl.MenuViewer;
import su.nexmedia.engine.api.menu.item.MenuItem;
import su.nexmedia.engine.editor.EditorManager;
import su.nexmedia.engine.utils.ItemUtil;
import su.nexmedia.engine.utils.StringUtil;
import su.nightexpress.excellentcrates.ExcellentCratesPlugin;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.crate.impl.Reward;
import su.nightexpress.excellentcrates.crate.menu.PreviewMenu;
import su.nightexpress.excellentcrates.editor.EditorLocales;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

public class RewardListEditor extends EditorMenu<ExcellentCratesPlugin, Crate> implements AutoPaged<Reward> {

    public RewardListEditor(@NotNull Crate crate) {
        super(crate.plugin(), crate, Config.EDITOR_TITLE_CRATE.get(), 54);

        PreviewMenu previewMenu = this.plugin().getCrateManager().getPreview(crate);
        if (previewMenu != null) {
            previewMenu.getItems().forEach(menuItem -> {
                MenuItem clone = menuItem.copy().setPriority(-1);
                this.addItem(clone);
            });
        }

        this.addReturn(49).setClick((viewer, event) -> {
            crate.getEditor().openNextTick(viewer, 1);
        });
        this.addNextPage(53);
        this.addPreviousPage(45);

        this.addCreation(EditorLocales.REWARD_CREATE, 51).setClick((viewer, event) -> {
            ItemStack cursor = event.getCursor();
            if (cursor != null && !cursor.getType().isAir()) {
                String id = StringUtil.lowerCaseUnderscore(ItemUtil.getItemName(cursor));
                int count = 0;
                while (crate.getReward(count == 0 ? id : id + count) != null) {
                    count++;
                }
                Reward reward = new Reward(crate, count == 0 ? id : id + count);
                reward.setName(ItemUtil.getItemName(cursor));
                reward.getItems().add(new ItemStack(cursor));
                reward.setPreview(cursor);
                crate.addReward(reward);
                event.getView().setCursor(null);
                this.save(viewer);
                return;
            }

            this.handleInput(viewer, Lang.EDITOR_REWARD_ENTER_ID, wrapper -> {
                String id = StringUtil.lowerCaseUnderscore(wrapper.getTextRaw());
                if (crate.getReward(id) != null) {
                    EditorManager.error(viewer.getPlayer(), plugin.getMessage(Lang.EDITOR_REWARD_ERROR_CREATE_EXIST).getLocalized());
                    return false;
                }
                Reward reward = new Reward(crate, id);
                crate.addReward(reward);
                return true;
            });
        });

        this.addItem(Material.HOPPER, EditorLocales.REWARD_SORT, 47).setClick((viewer, event) -> {
            Comparator<Reward> comparator;
            su.nexmedia.engine.api.menu.click.ClickType type = su.nexmedia.engine.api.menu.click.ClickType.from(event);
            if (type == su.nexmedia.engine.api.menu.click.ClickType.NUMBER_1) {
                comparator = Comparator.comparingDouble(Reward::getWeight).reversed();
            }
            else if (type == su.nexmedia.engine.api.menu.click.ClickType.NUMBER_2) {
                comparator = Comparator.comparing(r -> r.getPreview().getType().name());
            }
            else if (type == su.nexmedia.engine.api.menu.click.ClickType.NUMBER_3) {
                comparator = Comparator.comparing(r -> ItemUtil.getItemName(r.getPreview()));
            }
            else if (type == su.nexmedia.engine.api.menu.click.ClickType.NUMBER_4) {
                comparator = Comparator.comparingDouble((Reward r) -> r.getRarity().getChance()).reversed();
            }
            else return;
            crate.setRewards(crate.getRewards().stream().sorted(comparator).toList());
            this.save(viewer);
        });
    }

    private void save(@NotNull MenuViewer viewer) {
        this.object.save();
        this.openNextTick(viewer, viewer.getPage());
    }

    @Override
    public void onPrepare(@NotNull MenuViewer viewer, @NotNull MenuOptions options) {
        super.onPrepare(viewer, options);

        this.getItemsForPage(viewer).forEach(this::addItem);
    }

    @Override
    public int[] getObjectSlots() {
        PreviewMenu previewMenu = this.plugin().getCrateManager().getPreview(this.object);
        if (previewMenu != null) return previewMenu.getObjectSlots();

        return IntStream.range(0, 36).toArray();
    }

    @Override
    @NotNull
    public List<Reward> getObjects(@NotNull Player player) {
        return new ArrayList<>(this.object.getRewards());
    }

    @Override
    @NotNull
    public ItemStack getObjectStack(@NotNull Player player, @NotNull Reward reward) {
        ItemStack item = new ItemStack(reward.getPreview());
        ItemUtil.mapMeta(item, meta -> {
            meta.setDisplayName(EditorLocales.REWARD_OBJECT.getLocalizedName());
            meta.setLore(EditorLocales.REWARD_OBJECT.getLocalizedLore());
            meta.addItemFlags(ItemFlag.values());
            ItemUtil.replace(meta, Placeholders.forRewardAll(reward).replacer());
        });
        return item;
    }

    @Override
    @NotNull
    public ItemClick getObjectClick(@NotNull Reward reward) {
        return (viewer, event) -> {
            Player player = viewer.getPlayer();
            if (event.getClick() == ClickType.DROP) {
                this.object.removeReward(reward);
                this.save(viewer);
                return;
            }

            if (event.isShiftClick()) {
                // Reward position move.
                List<Reward> all = new ArrayList<>(this.object.getRewards());
                int index = all.indexOf(reward);
                int allSize = all.size();

                if (event.isLeftClick()) {
                    if (index + 1 >= allSize) return;

                    all.remove(index);
                    all.add(index + 1, reward);
                }
                else if (event.isRightClick()) {
                    if (index == 0) return;

                    all.remove(index);
                    all.add(index - 1, reward);
                }
                this.object.setRewards(all);
                this.save(viewer);
                return;
            }

            if (event.isLeftClick()) {
                reward.getEditor().openNextTick(player, 1);
            }
        };
    }

    @Override
    public void onClick(@NotNull MenuViewer viewer, @Nullable ItemStack item, @NotNull SlotType slotType, int slot, @NotNull InventoryClickEvent event) {
        super.onClick(viewer, item, slotType, slot, event);
        if (slotType == SlotType.PLAYER || slotType == SlotType.PLAYER_EMPTY) {
            event.setCancelled(false);
        }
    }
}
