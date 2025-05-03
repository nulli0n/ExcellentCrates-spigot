package su.nightexpress.excellentcrates.editor.crate;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.MenuType;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.economybridge.EconomyBridge;
import su.nightexpress.economybridge.api.Currency;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.config.EditorLang;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.crate.impl.Cost;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.nightcore.ui.UIUtils;
import su.nightexpress.nightcore.ui.dialog.Dialog;
import su.nightexpress.nightcore.ui.menu.MenuViewer;
import su.nightexpress.nightcore.ui.menu.confirmation.Confirmation;
import su.nightexpress.nightcore.ui.menu.data.Filled;
import su.nightexpress.nightcore.ui.menu.data.MenuFiller;
import su.nightexpress.nightcore.ui.menu.item.MenuItem;
import su.nightexpress.nightcore.ui.menu.type.LinkedMenu;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.bukkit.NightItem;

import java.util.Comparator;
import java.util.stream.IntStream;

public class CrateCostsMenu extends LinkedMenu<CratesPlugin, Crate> implements Filled<Cost> {

    public CrateCostsMenu(@NotNull CratesPlugin plugin) {
        super(plugin, MenuType.GENERIC_9X5, Lang.EDITOR_TITLE_CRATE_OPEN_COSTS.getString());

        this.addItem(MenuItem.buildReturn(this, 39, (viewer, event) -> {
            this.runNextTick(() -> plugin.getEditorManager().openOptionsMenu(viewer.getPlayer(), this.getLink(viewer)));
        }));

        this.addItem(Material.ANVIL, EditorLang.CRATE_EDIT_OPEN_COST_CREATE, 41, (viewer, event, crate) -> {
            this.handleInput(Dialog.builder(viewer, Lang.EDITOR_ENTER_CURRENCY, input -> {
                Currency currency = EconomyBridge.getCurrency(input.getTextRaw());
                if (currency == null) return true;

                crate.addOpenCost(new Cost(currency.getInternalId(), 0D));
                crate.saveSettings();
                return true;
            }).setSuggestions(EconomyBridge.getCurrencyIds(), true));
        });

        this.addItem(MenuItem.buildNextPage(this, 44));
        this.addItem(MenuItem.buildPreviousPage(this, 36));
    }

    @Override
    @NotNull
    public MenuFiller<Cost> createFiller(@NotNull MenuViewer viewer) {
        Player player = viewer.getPlayer();
        Crate crate = this.getLink(player);

        var autoFill = MenuFiller.builder(this);

        autoFill.setSlots(IntStream.range(0, 36).toArray());
        autoFill.setItems(this.getLink(viewer).getOpenCosts().stream().sorted(Comparator.comparing(Cost::getCurrencyId)).toList());
        autoFill.setItemCreator(cost -> {
            Currency currency = EconomyBridge.getCurrency(cost.getCurrencyId());
            NightItem item = currency == null ? NightItem.fromType(Material.BARRIER) : NightItem.fromItemStack(currency.getIcon());

            String currencyId = cost.getCurrencyId();
            String amountStr = NumberUtil.format(cost.getAmount());

            item.localized(EditorLang.CRATE_EDIT_OPEN_COST_OBJECT);
            item.setHideComponents(true);
            item.replacement(replacer -> replacer
                .replace(Placeholders.GENERIC_NAME, cost.format())
                .replace(Placeholders.GENERIC_ID, currency == null ? Lang.badEntry(currencyId) : Lang.goodEntry(currencyId))
                .replace(Placeholders.GENERIC_AMOUNT, cost.isValidAmount() ? Lang.goodEntry(amountStr) : Lang.badEntry(amountStr))
            );

            return item;
        });
        autoFill.setItemClick(cost -> (viewer1, event) -> {
            if (event.isRightClick()) {
                UIUtils.openConfirmation(player, Confirmation.builder()
                    .onAccept((viewer2, event1) -> {
                        crate.removeOpenCost(cost);
                        crate.saveSettings();
                    })
                    .onReturn((viewer2, event1) -> {
                        plugin.runTask(task -> plugin.getEditorManager().openCostsMenu(player, crate));
                    })
                    .returnOnAccept(true)
                    .build());
                return;
            }

            this.handleInput(Dialog.builder(viewer1, Lang.EDITOR_ENTER_AMOUNT, input -> {
                cost.setAmount(input.asDoubleAbs(0D));
                crate.saveSettings();
                return true;
            }));
        });

        return autoFill.build();
    }

    @Override
    protected void onPrepare(@NotNull MenuViewer viewer, @NotNull InventoryView view) {
        this.autoFill(viewer);
    }

    @Override
    protected void onReady(@NotNull MenuViewer viewer, @NotNull Inventory inventory) {

    }
}
