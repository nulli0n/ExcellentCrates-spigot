package su.nightexpress.excellentcrates.crates.item.editor;

import java.util.List;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.crate.data.model.ICrateItem;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorHook;
import su.nightexpress.excellentcrates.crates.item.editor.ui.menu.context.CrateItemSetupContext;
import su.nightexpress.excellentcrates.util.ItemHelper;
import su.nightexpress.nightcore.bridge.item.AdaptedItem;
import su.nightexpress.nightcore.util.ItemUtil;

@NullMarked
public class CrateItemEditorService {

    public void setItemIcon(CrateEditorHook hook, Identifier id, CrateItemSetupContext setupContext) {
        hook.modify(crate -> {
            ItemStack itemStack = setupContext.itemStack();
            itemStack.setAmount(1);

            AdaptedItem item;
            if (setupContext.useItemRef()) {
                item = ItemHelper.bukkitIfFromCrates(itemStack);
            }
            else {
                item = ItemHelper.bukkit(itemStack);
            }

            if (setupContext.setDisplayName()) {
                String itemName = ItemUtil.getNameSerialized(itemStack);
                crate.getDisplay().setName(itemName);
            }

            if (setupContext.setDisplayLore()) {
                List<String> itemLore = ItemUtil.getLoreSerialized(itemStack);
                crate.getDisplay().setLore(itemLore);
            }

            ICrateItem display = crate.getItem();
            display.setItem(item);

            return ActionResult.ok();
        });
    }

    public void setItemStackable(CrateEditorHook hook, Identifier id, boolean state) {
        hook.modify(crate -> {
            ICrateItem display = crate.getItem();
            display.setStackable(state);

            return ActionResult.ok();
        });
    }

    public void setItemUseDisplay(CrateEditorHook hook, Identifier id, boolean state) {
        hook.modify(crate -> {
            ICrateItem display = crate.getItem();
            display.setUseDisplay(state);

            return ActionResult.ok();
        });
    }
}
