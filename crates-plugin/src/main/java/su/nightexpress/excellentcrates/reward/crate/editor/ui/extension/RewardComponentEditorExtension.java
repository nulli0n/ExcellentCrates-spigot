package su.nightexpress.excellentcrates.reward.crate.editor.ui.extension;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.component.CrateComponentKeys;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorExtension;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorHook;
import su.nightexpress.excellentcrates.api.reward.crate.CrateRewardsComponent;
import su.nightexpress.excellentcrates.reward.crate.component.lang.RewardComponentLang;
import su.nightexpress.excellentcrates.reward.crate.editor.ui.RewardComponentEditorUIController;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.ui.inventory.item.ItemState;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

@NullMarked
public class RewardComponentEditorExtension implements CrateEditorExtension {

    private static final Identifier ID = new Identifier("rewards");

    private final RewardComponentEditorUIController controller;

    public RewardComponentEditorExtension(RewardComponentEditorUIController controller) {
        this.controller = controller;
    }

    @Override
    public Identifier getId() {
        return ID;
    }

    @Override
    public MenuItem createButton(Crate crate, CrateEditorHook hook, BackwardNavigator navigator, int slot) {
        CrateRewardsComponent component = crate.getComponentOrNull(CrateComponentKeys.REWARDS);
        int total = component == null ? 0 : component.getRewardByIdMap().size();

        return MenuItem.button()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.DIAMOND)
                    .hideAllComponents()
                    .localized(RewardComponentLang.EDITOR_UI_EXTENSION_BUTTON)
                    .replace(ctx -> ctx
                        .with(CommonPlaceholders.GENERIC_AMOUNT, () -> NumberUtil.format(total))
                    )
                )
                .action(ctx -> this.click(ctx, crate, hook, navigator))
                .build()
            )
            .slots(slot)
            .build();
    }

    private void click(ActionContext context, Crate crate, CrateEditorHook hook, BackwardNavigator navigator) {
        Player player = context.getPlayer();

        this.controller.onExtensionClick(player, crate, hook, navigator);
    }
}
