package su.nightexpress.excellentcrates.editor.crate;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.config.EditorLang;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.crate.impl.LimitType;
import su.nightexpress.excellentcrates.crate.impl.LimitValues;
import su.nightexpress.excellentcrates.crate.impl.Reward;
import su.nightexpress.excellentcrates.data.impl.LimitData;
import su.nightexpress.nightcore.menu.MenuOptions;
import su.nightexpress.nightcore.menu.MenuSize;
import su.nightexpress.nightcore.menu.MenuViewer;
import su.nightexpress.nightcore.menu.impl.EditorMenu;
import su.nightexpress.nightcore.util.ItemReplacer;
import su.nightexpress.nightcore.util.ItemUtil;

public class RewardLimitEditor extends EditorMenu<CratesPlugin, RewardLimitEditor.Data> implements CrateEditor {

    private static final String CHEST = "ef221b33f5b39e99ee6fd343abaaa9abdf66d93d4306cf01cca9f202e8773fd6";
    private static final String CLOCK = "a77be664b48eb834c05a79cf2bcea4a0b49215211254b0b4d965ccb221dbedbb";
    private static final String STEP = "f3514f23d6b09e1840cdec7c0d6912dcd30f82110858c133a7f7778c728566dd";
    private static final String RESET = "cc35db4333a0069a9878f2bac1b8496398e24d9594f87e2ea69df7129f81a4c5";

    public record Data(Reward reward, LimitType type, LimitValues values){}

    public RewardLimitEditor(@NotNull CratesPlugin plugin) {
        super(plugin, Lang.EDITOR_TITLE_REWARD_LIMITS.getString(), MenuSize.CHEST_36);

        this.addReturn(31, (viewer, event, limit) -> {
            this.runNextTick(() -> plugin.getEditorManager().openReward(viewer.getPlayer(), limit.reward));
        });

        this.addItem(Material.LIME_DYE, EditorLang.REWARD_LIMIT_TOGGLE, 10, (viewer, event, data) -> {
            LimitValues values = data.values;
            values.setEnabled(!values.isEnabled());

            if (data.type == LimitType.GLOBAL && values.isEnabled()) {
                LimitData limitData = data.reward.getGlobalLimitData();
                if (limitData == null) limitData = plugin.getCrateManager().createRewardLimit(data.reward);

                data.reward.loadGlobalLimit(limitData);
            }

            this.saveReward(viewer, data.reward, true);
        }).getOptions().addDisplayModifier((viewer, itemStack) -> {
            if (!this.getLink(viewer).values.isEnabled()) itemStack.setType(Material.GRAY_DYE);
        });

        this.addItem(ItemUtil.getSkinHead(CHEST), EditorLang.REWARD_LIMIT_AMOUNT, 12, (viewer, event, data) -> {
            LimitValues values = data.values;
            if (event.isRightClick()) {
                values.setAmount(-1);
                this.saveReward(viewer, data.reward, true);
                return;
            }

            this.handleInput(viewer, Lang.EDITOR_ENTER_AMOUNT, (dialog, input) -> {
                values.setAmount(input.asAnyInt(-1));
                this.saveReward(viewer, data.reward, false);
                return true;
            });
        });

        this.addItem(ItemUtil.getSkinHead(CLOCK), EditorLang.REWARD_LIMIT_COOLDOWN, 14, (viewer, event, data) -> {
            LimitValues values = data.values;
            if (event.isLeftClick()) {
                this.handleInput(viewer, Lang.EDITOR_ENTER_SECONDS, (dialog, input) -> {
                    values.setCooldown(input.asAnyInt(0));
                    this.saveReward(viewer, data.reward, false);
                    return true;
                });
            }
            else if (event.isRightClick()) {
                values.setMidnightCooldown();
                this.saveReward(viewer, data.reward, true);
            }
            else if (event.getClick() == ClickType.DROP) {
                values.setCooldown(0);
                this.saveReward(viewer, data.reward, true);
            }
        });

        this.addItem(ItemUtil.getSkinHead(STEP), EditorLang.REWARD_LIMIT_COOLDOWN_STEP, 16, (viewer, event, data) -> {
            LimitValues values = data.values;
            this.handleInput(viewer, Lang.EDITOR_ENTER_AMOUNT, (dialog, input) -> {
                values.setCooldownStep(input.asInt(1));
                this.saveReward(viewer, data.reward, false);
                return true;
            });
        });

        this.addItem(ItemUtil.getSkinHead(RESET), EditorLang.REWARD_LIMIT_RESET, 4, (viewer, event, data) -> {
            LimitData limitData = data.reward.getGlobalLimitData();
            if (limitData != null) {
                limitData.reset();
                plugin.getData().scheduleLimitSave(data.reward);
            }
        }).getOptions().setVisibilityPolicy(viewer -> this.getLink(viewer).type == LimitType.GLOBAL);

        this.getItems().forEach(menuItem -> menuItem.getOptions().addDisplayModifier((viewer, item) -> {
            ItemReplacer.replace(item, this.getLink(viewer).values.getPlaceholders());
        }));
    }

    public void open(@NotNull Player player, @NotNull Reward reward, @NotNull LimitType type) {
        this.open(player, new Data(reward, type, reward.getLimitValues(type)));
    }

    @Override
    protected void onPrepare(@NotNull MenuViewer viewer, @NotNull MenuOptions options) {

    }

    @Override
    protected void onReady(@NotNull MenuViewer viewer, @NotNull Inventory inventory) {

    }
}
