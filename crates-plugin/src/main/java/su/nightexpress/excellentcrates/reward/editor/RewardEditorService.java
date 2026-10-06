package su.nightexpress.excellentcrates.reward.editor;

import java.util.List;
import java.util.function.Function;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.component.RewardComponentKeys;
import su.nightexpress.excellentcrates.api.reward.data.model.RewardPreview;
import su.nightexpress.excellentcrates.api.reward.placeholder.RewardPlaceholders;
import su.nightexpress.excellentcrates.reward.data.RewardDataService;
import su.nightexpress.excellentcrates.reward.data.reward.StandardRewardPreview;
import su.nightexpress.excellentcrates.reward.editor.context.RewardCreationContext;
import su.nightexpress.excellentcrates.reward.editor.lang.RewardEditorLang;
import su.nightexpress.excellentcrates.reward.lang.RewardsLang;
import su.nightexpress.excellentcrates.util.ItemHelper;
import su.nightexpress.nightcore.bridge.item.AdaptedItem;
import su.nightexpress.nightcore.util.ItemUtil;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

@NullMarked
public class RewardEditorService {

    private final RewardDataService  dataService;
    @Deprecated
    private final RewardPlaceholders rewardPlaceholders;

    public RewardEditorService(RewardDataService dataService, RewardPlaceholders rewardPlaceholders) {
        this.dataService = dataService;
        this.rewardPlaceholders = rewardPlaceholders;
    }

    public ActionResult editReward(Identifier id, Function<Reward, ActionResult> action) {
        Reward reward = this.dataService.getReward(id);
        if (reward == null) {
            return ActionResult.fail(RewardsLang.GENERIC_REWARD_NOT_FOUND, ctx -> ctx
                .with(CommonPlaceholders.GENERIC_VALUE, id::value)
            );
        }

        ActionResult result = action.apply(reward);
        if (result.success()) {
            this.dataService.markDirty(reward);
        }

        return result;
    }

    public ActionResult createReward(Player player, Identifier id, RewardCreationContext context) {
        if (this.dataService.hasReward(id)) {
            return ActionResult.fail(RewardEditorLang.CREATION_DUPLICATED_ID, ctx -> ctx
                .with(CommonPlaceholders.GENERIC_VALUE, id::value)
            );
        }

        ItemStack itemStack = context.itemStack();
        AdaptedItem item = context.useItemReference() ? ItemHelper.adapt(itemStack) : ItemHelper.vanilla(itemStack);

        this.dataService.createReward(id, builder -> {
            String name = ItemUtil.getNameSerialized(itemStack);
            List<String> lore = ItemUtil.getLoreSerialized(itemStack);

            builder.preview(new StandardRewardPreview(name, lore, item, true));
        }, reward -> {
            if (context.setItemContent()) {
                reward.getComponent(RewardComponentKeys.ITEMS).ifPresent(itemComponent -> {
                    itemComponent.addItem(item);
                });
            }
        });

        return ActionResult.ok();
    }

    public ActionResult deleteReward(Identifier id) {
        Reward reward = this.dataService.getReward(id);
        if (reward == null) {
            return ActionResult.fail(RewardsLang.GENERIC_REWARD_NOT_FOUND, ctx -> ctx
                .with(CommonPlaceholders.GENERIC_VALUE, id::value)
            );
        }

        boolean success = this.dataService.deleteReward(reward);
        return success ? ActionResult.ok() : ActionResult.fail(RewardEditorLang.DELETION_FAILURE, ctx -> ctx
            .apply(this.rewardPlaceholders.basePlaceholders(reward))
        );
    }

    public ActionResult setPreviewName(Identifier id, String name) {
        return this.editReward(id, reward -> {
            RewardPreview preview = reward.getPreview();
            preview.setName(name);

            return ActionResult.ok();
        });
    }

    public ActionResult setPreviewLore(Identifier id, List<String> lore) {
        return this.editReward(id, reward -> {
            RewardPreview preview = reward.getPreview();
            preview.setLore(lore);

            return ActionResult.ok();
        });
    }

    public ActionResult setPreviewIcon(Identifier id, ItemStack itemStack) {
        return this.editReward(id, reward -> {
            AdaptedItem item = ItemHelper.vanillaIfMixed(itemStack);

            RewardPreview preview = reward.getPreview();
            preview.setIcon(item);
            preview.setUseIconData(!ItemHelper.isVanillaOnly(itemStack));

            return ActionResult.ok();
        });
    }

    public ActionResult setPreviewAutoResolveFromIcon(Identifier id, boolean state) {
        return this.editReward(id, reward -> {
            RewardPreview preview = reward.getPreview();
            preview.setUseIconData(state);

            return ActionResult.ok();
        });
    }
}
