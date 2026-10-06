package su.nightexpress.excellentcrates.reward.broadcast.editor.extension;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.broadcast.RewardBroadcastComponent;
import su.nightexpress.excellentcrates.api.reward.component.RewardComponentKeys;
import su.nightexpress.excellentcrates.api.reward.editor.RewardEditorExtension;
import su.nightexpress.excellentcrates.api.reward.editor.RewardEditorHook;
import su.nightexpress.excellentcrates.core.SharedPlaceholders;
import su.nightexpress.excellentcrates.reward.broadcast.editor.ui.RewardBroadcastEditorUIController;
import su.nightexpress.excellentcrates.reward.broadcast.lang.RewardBroadcastLang;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.ui.inventory.item.ItemState;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.util.bukkit.NightItem;

@NullMarked
public class RewardBroadcastEditorExtension implements RewardEditorExtension {

    private static final Identifier ID = new Identifier("broadcast");

    private final RewardBroadcastEditorUIController uiController;

    public RewardBroadcastEditorExtension(RewardBroadcastEditorUIController uiController) {
        this.uiController = uiController;
    }

    @Override
    public Identifier getId() {
        return ID;
    }

    @Override
    public MenuItem createButton(Reward reward, RewardEditorHook hook, BackwardNavigator navigator, int slot) {
        RewardBroadcastComponent component = reward.getComponentOrNull(RewardComponentKeys.BROADCAST);
        boolean enabled = component != null && component.isEnabled();

        return MenuItem.custom()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.BELL)
                    .hideAllComponents()
                    .localized(RewardBroadcastLang.EDITOR_UI_EXTENSION_BUTTON)
                    .replace(ctx -> ctx
                        .with(SharedPlaceholders.STATE, () -> CoreLang.STATE_ENABLED_DISALBED.get(enabled))
                    )
                )
                .action(ctx -> this.click(ctx, reward, hook, navigator))
                .build()
            )
            .slots(slot)
            .build();
    }

    private void click(ActionContext context, Reward reward, RewardEditorHook hook, BackwardNavigator navigator) {
        Player player = context.getPlayer();

        this.uiController.onExtensionClick(player, reward, hook, navigator);
    }
}
