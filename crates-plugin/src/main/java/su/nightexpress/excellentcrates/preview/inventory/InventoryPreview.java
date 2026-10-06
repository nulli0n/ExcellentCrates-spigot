package su.nightexpress.excellentcrates.preview.inventory;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.excellentcrates.api.preview.Preview;
import su.nightexpress.excellentcrates.api.preview.PreviewContext;
import su.nightexpress.excellentcrates.preview.inventory.menu.InventoryMenuContext;
import su.nightexpress.excellentcrates.preview.inventory.menu.InventoryPreviewMenu;
import su.nightexpress.excellentcrates.preview.lang.PreviewLang;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;

@NullMarked
public class InventoryPreview implements Preview {

    private final AdaptedKey           key;
    private final String               name;
    private final InventoryPreviewMenu menu;

    public InventoryPreview(AdaptedKey key, String name, InventoryPreviewMenu menu) {
        this.key = key;
        this.name = name;
        this.menu = menu;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public AdaptedKey getKey() {
        return this.key;
    }

    @Override
    public ActionResult show(Player player, PreviewContext context) {
        InventoryMenuContext menuContext = new InventoryMenuContext(context.crate().id(), context.backwardNavigator());

        if (!this.menu.show(player, menuContext)) {
            return ActionResult.fail(PreviewLang.ERROR_PREVIEW_INVENTORY_NOT_OPENED);
        }

        return ActionResult.ok();
    }
}
