package su.nightexpress.excellentcrates.rarity.reward.component.editor.extension;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.excellentcrates.api.rarity.reward.RarityComponent;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.component.RewardComponentKeys;
import su.nightexpress.excellentcrates.api.reward.editor.RewardEditorExtension;
import su.nightexpress.excellentcrates.api.reward.editor.RewardEditorHook;
import su.nightexpress.excellentcrates.core.SharedPlaceholders;
import su.nightexpress.excellentcrates.rarity.reward.component.editor.ui.RarityComponentEditorUIController;
import su.nightexpress.excellentcrates.rarity.reward.component.lang.RarityComponentLang;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.ui.inventory.item.ItemState;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.util.bukkit.NightItem;

@NullMarked
public class RarityComponentEditorExtension implements RewardEditorExtension {

    private static final Identifier ID = new Identifier("rarity");

    private final RarityComponentEditorUIController uiController;

    public RarityComponentEditorExtension(RarityComponentEditorUIController uiController) {
        this.uiController = uiController;
    }

    @Override
    public Identifier getId() {
        return ID;
    }

    @Override
    public MenuItem createButton(Reward reward, RewardEditorHook hook, BackwardNavigator backwardNavigator, int slot) {
        RarityComponent component = reward.getComponentOrNull(RewardComponentKeys.RARITY);
        boolean enabled = component != null && component.isEnabled();

        return MenuItem.custom()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.REDSTONE)
                    .hideAllComponents()
                    .localized(RarityComponentLang.EDITOR_UI_EXTENSION_BUTTON)
                    .replace(ctx -> ctx
                        .with(SharedPlaceholders.STATE, () -> CoreLang.STATE_ENABLED_DISALBED.get(enabled))
                    )
                )
                .action(ctx -> this.click(ctx.getPlayer(), reward, hook, backwardNavigator))
                .build()
            )
            .slots(slot)
            .build();
    }

    private void click(Player player, Reward reward, RewardEditorHook hook, BackwardNavigator backwardNavigator) {
        this.uiController.onExtensionClick(player, reward, hook, backwardNavigator);
    }
}
