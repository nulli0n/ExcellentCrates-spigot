package su.nightexpress.excellentcrates.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.config.JYML;
import su.nexmedia.engine.api.manager.AbstractLoadableItem;
import su.nexmedia.engine.api.menu.*;
import su.nexmedia.engine.api.type.ClickType;
import su.nexmedia.engine.utils.ItemUtil;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.api.CrateClickAction;
import su.nightexpress.excellentcrates.api.crate.ICrate;
import su.nightexpress.excellentcrates.api.crate.ICrateMenu;
import su.nightexpress.excellentcrates.config.Config;

import java.util.function.UnaryOperator;

public class CrateMenu extends AbstractLoadableItem<ExcellentCrates> implements ICrateMenu {

    private Menu menu;

    public CrateMenu(@NotNull ExcellentCrates plugin, @NotNull JYML cfg) {
        super(plugin, cfg);
    }

    @Override
    @NotNull
    public UnaryOperator<String> replacePlaceholders() {
        return str -> str
            .replace(Placeholders.MENU_ID, this.getId())
            ;
    }

    @Override
    public void onSave() {

    }

    @Override
    public void clear() {
        if (this.menu != null) {
            this.menu.clear();
            this.menu = null;
        }
    }

    @Override
    @NotNull
    public IMenu getMenu() {
        return this.menu;
    }

    @Override
    public void open(@NotNull Player player) {
        if (this.menu == null) {
            this.menu = new Menu(this.plugin(), this.getConfig(), "");
        }
        this.menu.open(player, 1);
    }

    class Menu extends AbstractMenu<ExcellentCrates> {

        Menu(@NotNull ExcellentCrates plugin, @NotNull JYML cfg, @NotNull String path) {
            super(plugin, cfg, path);

            IMenuClick click = (p, type, e) -> {
                if (!(type instanceof MenuItemType type2)) return;

                if (type2 == MenuItemType.CLOSE) {
                    p.closeInventory();
                }
            };

            for (String id : cfg.getSection("Content")) {
                IMenuItem menuItem = cfg.getMenuItem("Content." + id, MenuItemType.class);

                if (menuItem.getType() != null) {
                    menuItem.setClick(click);
                }
                this.addItem(menuItem);
            }

            IMenuClick clickCrate = (player, type, e) -> {
                int slot = e.getRawSlot();

                IMenuItem menuItem = this.getItem(player, slot);
                if (menuItem == null) return;

                String crateId = menuItem.getId();
                ICrate crate = plugin.getCrateManager().getCrateById(crateId);
                if (crate == null) return;

                ClickType clickType = ClickType.from(e);
                CrateClickAction clickAction = Config.getCrateClickAction(clickType);
                if (clickAction == null) return;

                player.closeInventory();
                plugin.getCrateManager().interactCrate(player, crate, clickAction, null, null);
            };

            for (String id : cfg.getSection("Crates")) {
                ICrate crate = plugin.getCrateManager().getCrateById(id);
                if (crate == null) {
                    plugin.error("Invalid crate '" + id + "' in '" + CrateMenu.this.getId() + "' menu!");
                    continue;
                }

                IMenuItem menuItem = cfg.getMenuItem("Crates." + id);
                menuItem.setClick(clickCrate);
                this.addItem(menuItem);
            }
        }

        @Override
        public void onPrepare(@NotNull Player player, @NotNull Inventory inventory) {

        }

        @Override
        public void onReady(@NotNull Player player, @NotNull Inventory inventory) {

        }

        @Override
        public void onItemPrepare(@NotNull Player player, @NotNull IMenuItem menuItem, @NotNull ItemStack item) {
            super.onItemPrepare(player, menuItem, item);

            ItemMeta meta = item.getItemMeta();
            if (meta == null) return;

            ICrate crate = plugin.getCrateManager().getCrateById(menuItem.getId());
            if (crate == null) return;

            ItemUtil.replace(item, crate.replacePlaceholders());
            ItemUtil.replace(item, str -> str.replace("%user_keys%", String.valueOf(plugin.getKeyManager().getKeysAmount(player, crate))));
        }

        @Override
        public boolean cancelClick(@NotNull InventoryClickEvent e, @NotNull SlotType slotType) {
            return true;
        }
    }
}
