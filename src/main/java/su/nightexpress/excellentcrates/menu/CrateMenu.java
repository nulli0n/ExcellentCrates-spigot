package su.nightexpress.excellentcrates.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.config.JYML;
import su.nexmedia.engine.api.manager.AbstractLoadableItem;
import su.nexmedia.engine.api.manager.ICleanable;
import su.nexmedia.engine.api.manager.IPlaceholder;
import su.nexmedia.engine.api.menu.AbstractMenu;
import su.nexmedia.engine.api.menu.MenuClick;
import su.nexmedia.engine.api.menu.MenuItem;
import su.nexmedia.engine.api.menu.MenuItemType;
import su.nexmedia.engine.api.type.ClickType;
import su.nexmedia.engine.utils.ItemUtil;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.api.CrateClickAction;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.crate.impl.Crate;

import java.util.function.UnaryOperator;

public class CrateMenu extends AbstractLoadableItem<ExcellentCrates> implements ICleanable, IPlaceholder {

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

    @NotNull
    public AbstractMenu<?> getMenu() {
        return this.menu;
    }

    public void open(@NotNull Player player) {
        if (this.menu == null) {
            this.menu = new Menu(this.plugin(), this.getConfig(), "");
        }
        this.menu.open(player, 1);
    }

    class Menu extends AbstractMenu<ExcellentCrates> {

        Menu(@NotNull ExcellentCrates plugin, @NotNull JYML cfg, @NotNull String path) {
            super(plugin, cfg, path);

            MenuClick click = (p, type, e) -> {
                if (!(type instanceof MenuItemType type2)) return;

                if (type2 == MenuItemType.CLOSE) {
                    p.closeInventory();
                }
            };

            for (String id : cfg.getSection("Content")) {
                MenuItem menuItem = cfg.getMenuItem("Content." + id, MenuItemType.class);

                if (menuItem.getType() != null) {
                    menuItem.setClickHandler(click);
                }
                this.addItem(menuItem);
            }

            MenuClick clickCrate = (player, type, e) -> {
                int slot = e.getRawSlot();

                MenuItem menuItem = this.getItem(player, slot);
                if (menuItem == null) return;

                String crateId = menuItem.getId();
                Crate crate = plugin.getCrateManager().getCrateById(crateId);
                if (crate == null) return;

                ClickType clickType = ClickType.from(e);
                CrateClickAction clickAction = Config.getCrateClickAction(clickType);
                if (clickAction == null) return;

                player.closeInventory();
                plugin.getCrateManager().interactCrate(player, crate, clickAction, null, null);
            };

            for (String id : cfg.getSection("Crates")) {
                Crate crate = plugin.getCrateManager().getCrateById(id);
                if (crate == null) {
                    plugin.error("Invalid crate '" + id + "' in '" + CrateMenu.this.getId() + "' menu!");
                    continue;
                }

                MenuItem menuItem = cfg.getMenuItem("Crates." + id);
                menuItem.setClickHandler(clickCrate);
                this.addItem(menuItem);
            }
        }

        @Override
        public void onItemPrepare(@NotNull Player player, @NotNull MenuItem menuItem, @NotNull ItemStack item) {
            super.onItemPrepare(player, menuItem, item);

            ItemMeta meta = item.getItemMeta();
            if (meta == null) return;

            Crate crate = plugin.getCrateManager().getCrateById(menuItem.getId());
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
