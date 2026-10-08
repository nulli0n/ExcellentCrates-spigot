package su.nightexpress.excellentcrates.keys.editor;

import java.util.List;
import java.util.function.Function;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.key.CrateKey;
import su.nightexpress.excellentcrates.keys.data.KeyDataService;
import su.nightexpress.excellentcrates.keys.data.key.StandardKeyDisplay;
import su.nightexpress.excellentcrates.keys.data.key.StandardKeyItem;
import su.nightexpress.excellentcrates.keys.editor.context.KeyCreationContext;
import su.nightexpress.excellentcrates.keys.editor.lang.KeyEditorLang;
import su.nightexpress.excellentcrates.util.ItemHelper;
import su.nightexpress.nightcore.bridge.item.AdaptedItem;
import su.nightexpress.nightcore.util.ItemUtil;
import su.nightexpress.nightcore.util.StringUtil;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

@NullMarked
public class KeyEditorService {

    private final KeyDataService dataService;

    public KeyEditorService(KeyDataService dataService) {
        this.dataService = dataService;
    }

    public ActionResult modifyKey(Identifier keyId, Function<CrateKey, ActionResult> consumer) {
        CrateKey key = this.dataService.getKey(keyId);
        if (key == null) {
            return ActionResult.fail();
        }

        ActionResult result = consumer.apply(key);
        if (result.success()) {
            this.dataService.markDirty(key);
        }
        return result;
    }

    public ActionResult createKey(KeyCreationContext context) {
        Identifier id = context.id();

        if (this.dataService.hasKey(id)) {
            return ActionResult.fail(KeyEditorLang.CREATION_DUPLICATED_ID, ctx -> ctx
                .with(CommonPlaceholders.GENERIC_VALUE, id::value)
            );
        }

        this.dataService.createKey(id, builder -> {
            String name = StringUtil.capitalizeUnderscored(id.value());
            List<String> lore = List.of();
            AdaptedItem item;
            boolean stackable = true;

            ItemStack itemStack = context.itemStack();
            if (itemStack != null) {
                ItemMeta meta = itemStack.getItemMeta();

                if (meta != null) {
                    String metaName = ItemUtil.getNameSerialized(meta);
                    name = metaName == null ? name : metaName;
                    lore = ItemUtil.getLoreSerialized(meta);
                    stackable = !meta.hasMaxStackSize() || meta.getMaxStackSize() > 1;
                }

                item = ItemHelper.bukkitIfCrates(itemStack);
            }
            else {
                item = ItemHelper.bukkit(new ItemStack(Material.TRIAL_KEY));
            }

            builder.display(new StandardKeyDisplay(name, lore));
            builder.item(new StandardKeyItem(item, stackable, true));
        });

        return ActionResult.ok();
    }

    public ActionResult deleteKey(Identifier keyId) {
        CrateKey key = this.dataService.getKey(keyId);
        if (key == null) {
            return ActionResult.fail();
        }

        boolean success = this.dataService.deleteKey(key);
        return success ? ActionResult.ok() : ActionResult.fail(KeyEditorLang.DELETION_FAILURE, ctx -> ctx
            .with(CommonPlaceholders.GENERIC_VALUE, keyId::value)
        );
    }
}
