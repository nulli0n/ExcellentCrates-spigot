package su.nightexpress.excellentcrates.reward.feature.cooldown.editor.ui.extension;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.editor.RewardEditorExtension;
import su.nightexpress.excellentcrates.api.reward.editor.RewardEditorHook;
import su.nightexpress.excellentcrates.reward.feature.cooldown.editor.ui.RewardCooldownsEditorUIController;
import su.nightexpress.excellentcrates.reward.feature.cooldown.lang.RewardCooldownsLang;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.ui.inventory.item.ItemState;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.util.bukkit.NightItem;

@NullMarked
public class RewardCooldownsEditorExtension implements RewardEditorExtension {

    private static final Identifier ID = new Identifier("cooldown");

    private final RewardCooldownsEditorUIController controller;

    public RewardCooldownsEditorExtension(RewardCooldownsEditorUIController controller) {
        this.controller = controller;
    }

    @Override
    public Identifier getId() {
        return ID;
    }

    @Override
    public MenuItem createButton(Reward reward, RewardEditorHook hook, BackwardNavigator navigator, int slot) {
        /* RewardCooldownComponent component = reward.getComponentOrNull(RewardComponentKeys.COOLDOWN);
        boolean enabled = component != null && component.isEnabled(); */

        return MenuItem.custom()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.CLOCK)
                    .hideAllComponents()
                    .localized(RewardCooldownsLang.UI_EXTENSION_BUTTON)
                /* .replace(ctx -> ctx
                    .with(SharedPlaceholders.STATE, () -> CoreLang.STATE_ENABLED_DISALBED.get(enabled))
                ) */
                )
                .action(ctx -> this.click(ctx, reward, hook, navigator))
                .build()
            )
            .slots(slot)
            .build();
    }

    private void click(ActionContext context, Reward reward, RewardEditorHook hook, BackwardNavigator navigator) {
        Player player = context.getPlayer();

        this.controller.onExtensionClick(player, reward, hook, navigator);
    }
}
