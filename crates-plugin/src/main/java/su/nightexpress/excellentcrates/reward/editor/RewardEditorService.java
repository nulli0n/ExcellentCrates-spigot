package su.nightexpress.excellentcrates.reward.editor;

import java.util.List;
import java.util.function.Function;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.component.RewardComponentKeys;
import su.nightexpress.excellentcrates.api.reward.data.model.RewardPreview;
import su.nightexpress.excellentcrates.api.reward.placeholder.RewardPlaceholders;
import su.nightexpress.excellentcrates.api.reward.registry.RewardId;
import su.nightexpress.excellentcrates.reward.crate.component.editor.RewardComponentHook;
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

    public ActionResult editReward(RewardId id, Function<Reward, ActionResult> action) {
        Reward reward = this.dataService.getReward(id);
        if (reward == null) {
            return ActionResult.fail(RewardsLang.GENERIC_REWARD_NOT_FOUND, ctx -> ctx
                .with(CommonPlaceholders.GENERIC_VALUE, () -> id.rewardId().value())
            );
        }

        ActionResult result = action.apply(reward);
        if (result.success()) {
            this.dataService.markDirty(reward);
        }

        return result;
    }

    public ActionResult createReward(Player player,
                                     RewardComponentHook hook,
                                     Crate crate,
                                     RewardId id,
                                     RewardCreationContext context) {
        if (this.dataService.hasReward(id)) {
            return ActionResult.fail(RewardEditorLang.CREATION_DUPLICATED_ID, ctx -> ctx
                .with(CommonPlaceholders.GENERIC_VALUE, () -> id.rewardId().value())
            );
        }

        ItemStack itemStack = context.itemStack();
        AdaptedItem item = context.useItemReference() ? ItemHelper.bukkitIfFromCrates(itemStack) : ItemHelper.bukkit(
            itemStack);

        this.dataService.createReward(id, builder -> {
            String name = ItemUtil.getNameSerialized(itemStack);
            List<String> lore = ItemUtil.getLoreSerialized(itemStack);

            builder.preview(new StandardRewardPreview(name, lore, item, true));
        }, reward -> {
            if (context.addToGivenItems()) {
                reward.getComponent(RewardComponentKeys.ITEMS).ifPresent(itemComponent -> {
                    itemComponent.addItem(item);
                });
            }

            hook.addReward(crate, reward.rawId());
        });

        return ActionResult.ok();
    }

    public ActionResult deleteReward(RewardComponentHook hook, Crate crate, RewardId id) {
        Reward reward = this.dataService.getReward(id);
        if (reward == null) {
            return ActionResult.fail(RewardsLang.GENERIC_REWARD_NOT_FOUND, ctx -> ctx
                .with(CommonPlaceholders.GENERIC_VALUE, () -> id.rewardId().value())
            );
        }

        boolean success = this.dataService.deleteReward(reward);
        if (success) {
            hook.removeReward(crate, reward.rawId());
            return ActionResult.ok();
        }
        else {
            return ActionResult.fail(RewardEditorLang.DELETION_FAILURE, ctx -> ctx
                .apply(this.rewardPlaceholders.basePlaceholders(reward))
            );
        }
    }

    public ActionResult setCrateRewardWeight(RewardId rewardId, double weight) {
        return this.editReward(rewardId, reward -> {
            reward.getBase().setWeight(weight);

            return ActionResult.ok();
        });
    }

    public ActionResult setPreviewName(RewardId id, String name) {
        return this.editReward(id, reward -> {
            RewardPreview preview = reward.getPreview();
            preview.setName(name);

            return ActionResult.ok();
        });
    }

    public ActionResult setPreviewLore(RewardId id, List<String> lore) {
        return this.editReward(id, reward -> {
            RewardPreview preview = reward.getPreview();
            preview.setLore(lore);

            return ActionResult.ok();
        });
    }

    public ActionResult setPreviewIcon(RewardId id, ItemStack itemStack) {
        return this.editReward(id, reward -> {
            AdaptedItem item = ItemHelper.bukkitIfMixed(itemStack);

            RewardPreview preview = reward.getPreview();
            preview.setIcon(item);
            preview.setInheritFromIcon(!ItemHelper.isBukkitOnly(itemStack));

            return ActionResult.ok();
        });
    }

    public ActionResult setPreviewAutoResolveFromIcon(RewardId id, boolean state) {
        return this.editReward(id, reward -> {
            RewardPreview preview = reward.getPreview();
            preview.setInheritFromIcon(state);

            return ActionResult.ok();
        });
    }
}
