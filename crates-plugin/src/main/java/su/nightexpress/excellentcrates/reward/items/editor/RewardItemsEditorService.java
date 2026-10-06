package su.nightexpress.excellentcrates.reward.items.editor;

import java.util.function.Function;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.excellentcrates.api.reward.component.RewardComponentKeys;
import su.nightexpress.excellentcrates.api.reward.editor.RewardEditorHook;
import su.nightexpress.excellentcrates.api.reward.items.RewardItemsComponent;
import su.nightexpress.excellentcrates.reward.items.lang.RewardItemsLang;
import su.nightexpress.excellentcrates.util.ItemHelper;
import su.nightexpress.nightcore.bridge.item.AdaptedItem;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

@NullMarked
public class RewardItemsEditorService {

    private ActionResult modifyComponent(RewardEditorHook hook, Function<RewardItemsComponent, ActionResult> editor) {
        return hook.modify(reward -> {
            RewardItemsComponent items = reward.getComponentOrNull(RewardComponentKeys.ITEMS);
            if (items == null) {
                return ActionResult.fail(RewardItemsLang.ERROR_NO_ITEMS_COMPONENT);
            }

            return editor.apply(items);
        });
    }

    public ActionResult addItemContent(RewardEditorHook hook, ItemStack itemStack, boolean useItemReference) {
        return this.modifyComponent(hook, items -> {
            AdaptedItem item = useItemReference ? ItemHelper.adapt(itemStack) : ItemHelper.vanilla(itemStack);

            items.addItem(item);
            return ActionResult.ok();
        });
    }

    public ActionResult removeItemContent(RewardEditorHook hook, int index) {
        return this.modifyComponent(hook, items -> {
            AdaptedItem removed = items.removeItem(index);
            if (removed == null) {
                return ActionResult.fail(RewardItemsLang.ITEMS_REMOVE_INVALID_INDEX, ctx -> ctx
                    .with(CommonPlaceholders.GENERIC_VALUE, () -> String.valueOf(index))
                );
            }

            return ActionResult.ok();
        });
    }
}
