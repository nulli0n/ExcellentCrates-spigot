package su.nightexpress.excellentcrates.editor.crate;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.MenuType;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.Reward;
import su.nightexpress.excellentcrates.config.EditorLang;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.crate.limit.LimitValues;
import su.nightexpress.nightcore.ui.dialog.Dialog;
import su.nightexpress.nightcore.ui.menu.MenuViewer;
import su.nightexpress.nightcore.ui.menu.item.ItemOptions;
import su.nightexpress.nightcore.ui.menu.item.MenuItem;
import su.nightexpress.nightcore.ui.menu.type.LinkedMenu;
import su.nightexpress.nightcore.util.ItemUtil;
import su.nightexpress.nightcore.util.bukkit.NightItem;

public class RewardLimitsMenu extends LinkedMenu<CratesPlugin, RewardLimitsMenu.Data> {

    private static final String AMOUNT     = "8426f715b80dd90eb1e1929f6ec4bc3583de8f821bdd4f4c1722e69c98b50506";
    private static final String RESET_TIME = "2063dfa15c6d8da506a2d93414763cb1f819386d2cf6543c08e232f163fb2c1c";
    private static final String STEP       = "f3514f23d6b09e1840cdec7c0d6912dcd30f82110858c133a7f7778c728566dd";
    private static final String CLEAR      = "48179b175daa79f73c665b61163364f6627e3d02b7253d427ebd2ff6818de6ce";

    public record Data(Reward reward, LimitValues values){}

    public RewardLimitsMenu(@NotNull CratesPlugin plugin) {
        super(plugin, MenuType.GENERIC_9X5, Lang.EDITOR_TITLE_REWARD_LIMITS.getString());

        this.addItem(MenuItem.buildReturn(this, 40, (viewer, event) -> {
            this.runNextTick(() -> plugin.getEditorManager().openRewardOptions(viewer.getPlayer(), this.getLink(viewer).reward));
        }));

        this.addItem(Material.LIME_DYE, EditorLang.REWARD_EDIT_LIMIT_TOGGLE, 19, (viewer, event, data) -> {
            LimitValues values = data.values;
            values.setEnabled(!values.isEnabled());
            data.reward.save();
            this.runNextTick(() -> this.flush(viewer));

        }, ItemOptions.builder().setDisplayModifier((viewer, itemStack) -> {
            if (!this.getLink(viewer).values.isEnabled()) itemStack.setMaterial(Material.GRAY_DYE);
        }).build());

        this.addItem(ItemUtil.getCustomHead(AMOUNT), EditorLang.REWARD_EDIT_LIMIT_AMOUNT, 21, (viewer, event, data) -> {
            if (event.isRightClick()) {
                data.values.setAmount(-1);
                data.reward.save();
                this.runNextTick(() -> this.flush(viewer));
                return;
            }

            this.handleInput(Dialog.builder(viewer, Lang.EDITOR_ENTER_AMOUNT, input -> {
                data.values.setAmount(input.asInt(-1));
                data.reward.save();
                return true;
            }));
        });

        this.addItem(ItemUtil.getCustomHead(RESET_TIME), EditorLang.REWARD_EDIT_LIMIT_RESET_TIME, 23, (viewer, event, data) -> {
            this.handleInput(Dialog.builder(viewer, Lang.EDITOR_ENTER_SECONDS, input -> {
                data.values.setResetTime(input.asInt(0));
                data.reward.save();
                return true;
            }));
        });

        this.addItem(ItemUtil.getCustomHead(STEP), EditorLang.REWARD_EDIT_LIMIT_RESET_TIME_STEP, 25, (viewer, event, data) -> {
            this.handleInput(Dialog.builder(viewer, Lang.EDITOR_ENTER_AMOUNT, input -> {
                data.values.setResetStep(input.asInt(1));
                data.reward.save();
                return true;
            }));
        });

        this.addItem(ItemUtil.getCustomHead(CLEAR), EditorLang.REWARD_EDIT_LIMIT_RESET, 4, (viewer, event, data) -> {
            plugin.getDataManager().deleteRewardLimits(data.reward);
        });
    }

    public void open(@NotNull Player player, @NotNull Reward reward, @NotNull LimitValues values) {
        this.open(player, new Data(reward, values));
    }

    @Override
    protected void onItemPrepare(@NotNull MenuViewer viewer, @NotNull MenuItem menuItem, @NotNull NightItem item) {
        super.onItemPrepare(viewer, menuItem, item);

        item.replacement(replacer -> replacer.replace(this.getLink(viewer).values.replacePlaceholders()));
    }

    @Override
    protected void onPrepare(@NotNull MenuViewer viewer, @NotNull InventoryView view) {

    }

    @Override
    protected void onReady(@NotNull MenuViewer viewer, @NotNull Inventory inventory) {

    }
}
