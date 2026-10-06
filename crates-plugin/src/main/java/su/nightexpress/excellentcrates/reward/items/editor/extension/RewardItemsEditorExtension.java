package su.nightexpress.excellentcrates.reward.items.editor.extension;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.component.RewardComponentKeys;
import su.nightexpress.excellentcrates.api.reward.editor.RewardEditorExtension;
import su.nightexpress.excellentcrates.api.reward.editor.RewardEditorHook;
import su.nightexpress.excellentcrates.api.reward.items.RewardItemsComponent;
import su.nightexpress.excellentcrates.reward.items.editor.ui.RewardItemsEditorUIController;
import su.nightexpress.excellentcrates.reward.items.lang.RewardItemsLang;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.ui.inventory.item.ItemState;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

@NullMarked
public class RewardItemsEditorExtension implements RewardEditorExtension {

    private static final Identifier ID = new Identifier("item_content");

    private final RewardItemsEditorUIController uiController;

    public RewardItemsEditorExtension(RewardItemsEditorUIController uiController) {
        this.uiController = uiController;
    }

    @Override
    public Identifier getId() {
        return ID;
    }

    @Override
    public MenuItem createButton(Reward reward, RewardEditorHook hook, BackwardNavigator navigator, int slot) {
        RewardItemsComponent component = reward.getComponentOrNull(RewardComponentKeys.ITEMS);
        int itemCount = component != null ? component.getItems().size() : 0;

        return MenuItem.custom()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.CHEST_MINECART)
                    .localized(RewardItemsLang.UI_INVENTORY_OPTIONS_BUTTON_ITEM_CONTENT)
                    .hideAllComponents()
                    .replace(ctx -> ctx
                        .with(CommonPlaceholders.GENERIC_AMOUNT, () -> NumberUtil.format(itemCount))
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
