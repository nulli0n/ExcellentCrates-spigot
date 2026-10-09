package su.nightexpress.excellentcrates.crates.editor;

import java.util.List;
import java.util.function.Function;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.crates.data.CrateDataService;
import su.nightexpress.excellentcrates.crates.data.crate.StandardCrateDisplay;
import su.nightexpress.excellentcrates.crates.data.crate.StandardCrateItem;
import su.nightexpress.excellentcrates.crates.editor.lang.CrateEditorLang;
import su.nightexpress.excellentcrates.crates.lang.CratesLang;
import su.nightexpress.excellentcrates.util.ItemHelper;
import su.nightexpress.nightcore.bridge.item.AdaptedItem;
import su.nightexpress.nightcore.util.StringUtil;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

@NullMarked
public class CrateEditorService {

    private final CrateDataService dataService;

    public CrateEditorService(CrateDataService dataService) {
        this.dataService = dataService;
    }

    private ActionResult checkCrate(Identifier crateId, Function<Crate, ActionResult> consumer) {
        Crate crate = this.dataService.getCrate(crateId);
        if (crate == null) {
            return ActionResult.fail(CratesLang.GENERIC_CRATE_NOT_FOUND, ctx -> ctx
                .with(CommonPlaceholders.GENERIC_VALUE, crateId::value)
            );
        }

        return consumer.apply(crate);
    }

    public ActionResult createCrate(Player player, Identifier id) {
        if (this.dataService.hasCrate(id)) {
            return ActionResult.fail(CrateEditorLang.CREATION_DUPLICATED_ID, ctx -> ctx
                .with(CommonPlaceholders.GENERIC_VALUE, id::value)
            );
        }

        this.dataService.createCrate(id, builder -> {
            String name = StringUtil.capitalizeUnderscored(id.value());
            List<String> lore = List.of();
            AdaptedItem item = ItemHelper.bukkit(new ItemStack(Material.CHEST));

            builder.display(new StandardCrateDisplay(name, lore));
            builder.item(new StandardCrateItem(item, true, true));
        });

        return ActionResult.ok(CrateEditorLang.CREATION_SUCCESS, ctx -> ctx
            .with(CommonPlaceholders.GENERIC_VALUE, id::value)
        );
    }

    public ActionResult deleteCrate(Identifier crateId) {
        return this.checkCrate(crateId, crate -> {
            this.dataService.deleteCrate(crate);

            return ActionResult.ok(CrateEditorLang.DELETION_SUCCESS, ctx -> ctx
                .with(CommonPlaceholders.GENERIC_VALUE, crateId::value)
            );
        });
    }

    public ActionResult modifyByExtension(Identifier crateId, Function<Crate, ActionResult> modifier) {
        return this.checkCrate(crateId, crate -> {
            ActionResult result = modifier.apply(crate);
            if (result.success()) {
                this.dataService.markDirty(crate);
            }

            return result;
        });
    }
}
