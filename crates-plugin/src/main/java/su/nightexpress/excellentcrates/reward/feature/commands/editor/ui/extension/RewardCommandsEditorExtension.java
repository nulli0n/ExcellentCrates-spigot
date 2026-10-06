package su.nightexpress.excellentcrates.reward.feature.commands.editor.ui.extension;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.commands.RewardCommandsComponent;
import su.nightexpress.excellentcrates.api.reward.component.RewardComponentKeys;
import su.nightexpress.excellentcrates.api.reward.editor.RewardEditorExtension;
import su.nightexpress.excellentcrates.api.reward.editor.RewardEditorHook;
import su.nightexpress.excellentcrates.reward.feature.commands.editor.ui.RewardCommandsEditorUIController;
import su.nightexpress.excellentcrates.reward.feature.commands.lang.RewardCommandsLang;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.ui.inventory.item.ItemState;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

@NullMarked
public class RewardCommandsEditorExtension implements RewardEditorExtension {

    private static final Identifier ID = new Identifier("commands");

    private final RewardCommandsEditorUIController controller;

    public RewardCommandsEditorExtension(RewardCommandsEditorUIController controller) {
        this.controller = controller;
    }

    @Override
    public Identifier getId() {
        return ID;
    }

    @Override
    public MenuItem createButton(Reward reward, RewardEditorHook hook, BackwardNavigator backwardNavigator, int slot) {
        RewardCommandsComponent component = reward.getComponentOrNull(RewardComponentKeys.COMMANDS);
        int commandCount = component != null ? component.getBundleMap().size() : 0;

        return MenuItem.custom()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.COMMAND_BLOCK)
                    .hideAllComponents()
                    .localized(RewardCommandsLang.UI_EXTENSION_BUTTON)
                    .replace(ctx -> ctx
                        .with(CommonPlaceholders.GENERIC_AMOUNT, () -> NumberUtil.format(commandCount))
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
        Identifier rewardId = reward.getId();

        this.controller.onExtensionClick(player, rewardId, hook, backwardNavigator);
    }
}
