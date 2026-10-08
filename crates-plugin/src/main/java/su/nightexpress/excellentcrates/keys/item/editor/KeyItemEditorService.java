package su.nightexpress.excellentcrates.keys.item.editor;

import java.util.List;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.excellentcrates.api.key.data.model.KeyItem;
import su.nightexpress.excellentcrates.api.key.editor.KeyEditorHook;
import su.nightexpress.excellentcrates.keys.item.editor.ui.context.KeyItemSetupContext;
import su.nightexpress.excellentcrates.util.ItemHelper;
import su.nightexpress.nightcore.bridge.item.AdaptedItem;
import su.nightexpress.nightcore.util.ItemUtil;

@NullMarked
public class KeyItemEditorService {

    public ActionResult setItemIcon(KeyEditorHook hook, KeyItemSetupContext setupContext) {
        return hook.modify(key -> {
            ItemStack itemStack = setupContext.itemStack();
            itemStack.setAmount(1);

            AdaptedItem item;
            if (setupContext.useItemRef()) {
                item = ItemHelper.bukkitIfCrates(itemStack);
            }
            else {
                item = ItemHelper.bukkit(itemStack);
            }

            if (setupContext.setDisplayName()) {
                String itemName = ItemUtil.getNameSerialized(itemStack);
                key.getDisplay().setName(itemName);
            }

            if (setupContext.setDisplayLore()) {
                List<String> itemLore = ItemUtil.getLoreSerialized(itemStack);
                key.getDisplay().setLore(itemLore);
            }

            KeyItem display = key.getItem();
            display.setItem(item);

            return ActionResult.ok();
        });
    }

    public ActionResult setItemStackable(KeyEditorHook hook, boolean stackable) {
        return hook.modify(key -> {
            key.getItem().setStackable(stackable);

            return ActionResult.ok();
        });
    }

    public ActionResult setInheritDisplaySettings(KeyEditorHook hook, boolean state) {
        return hook.modify(key -> {
            key.getItem().setInheritDisplaySettings(state);

            return ActionResult.ok();
        });
    }
}
