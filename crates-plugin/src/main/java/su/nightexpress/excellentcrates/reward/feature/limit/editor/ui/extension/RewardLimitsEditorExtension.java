package su.nightexpress.excellentcrates.reward.feature.limit.editor.ui.extension;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.component.RewardComponentKeys;
import su.nightexpress.excellentcrates.api.reward.editor.RewardEditorExtension;
import su.nightexpress.excellentcrates.api.reward.editor.RewardEditorHook;
import su.nightexpress.excellentcrates.api.reward.limit.RewardLimitComponent;
import su.nightexpress.excellentcrates.core.SharedPlaceholders;
import su.nightexpress.excellentcrates.reward.feature.limit.editor.ui.RewardLimitsEditorUIController;
import su.nightexpress.excellentcrates.reward.feature.limit.lang.RewardLimitsLang;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.ui.inventory.item.ItemState;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.util.bukkit.NightItem;

@NullMarked
public class RewardLimitsEditorExtension implements RewardEditorExtension {

    private static final Identifier ID = new Identifier("reward_limits");

    private final RewardLimitsEditorUIController controller;

    public RewardLimitsEditorExtension(RewardLimitsEditorUIController controller) {
        this.controller = controller;
    }

    @Override
    public Identifier getId() {
        return ID;
    }

    @Override
    public MenuItem createButton(Reward reward, RewardEditorHook hook, BackwardNavigator backwardNavigator, int slot) {
        RewardLimitComponent component = reward.getComponentOrNull(RewardComponentKeys.LIMIT);
        boolean enabled = component != null && component.isEnabled();

        return MenuItem.custom()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.REPEATER)
                    .hideAllComponents()
                    .localized(RewardLimitsLang.UI_EXTENSION_BUTTON)
                    .replace(ctx -> ctx
                        .with(SharedPlaceholders.STATE, () -> CoreLang.STATE_ENABLED_DISALBED.get(enabled))
                    )
                )
                .action(ctx -> this.click(ctx, reward, hook, backwardNavigator))
                .build()
            )
            .slots(slot)
            .build();
    }

    private void click(ActionContext context, Reward reward, RewardEditorHook hook,
                       BackwardNavigator backwardNavigator) {
        Player player = context.getPlayer();
        Identifier rewardId = reward.id();

        this.controller.onExtensionClick(player, rewardId, hook, backwardNavigator);
    }
}
