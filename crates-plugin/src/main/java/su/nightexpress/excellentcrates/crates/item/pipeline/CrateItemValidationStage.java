package su.nightexpress.excellentcrates.crates.item.pipeline;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.dispatcher.CrateMessageDispatcher;
import su.nightexpress.excellentcrates.api.crate.pipeline.PipelineChain;
import su.nightexpress.excellentcrates.api.crate.pipeline.PipelineStage;
import su.nightexpress.excellentcrates.api.crate.pipeline.component.CrateSourcePipelineComponent;
import su.nightexpress.excellentcrates.api.crate.pipeline.component.PipelineComponentKeys;
import su.nightexpress.excellentcrates.api.crate.pipeline.context.PipelineContext;
import su.nightexpress.excellentcrates.crates.item.CrateItemService;
import su.nightexpress.excellentcrates.crates.item.lang.CrateItemLang;
import su.nightexpress.nightcore.util.Players;

@NullMarked
public class CrateItemValidationStage implements PipelineStage {

    private final CrateItemService       itemService;
    private final CrateMessageDispatcher dispatcher;

    public CrateItemValidationStage(CrateItemService itemService, CrateMessageDispatcher dispatcher) {
        this.itemService = itemService;
        this.dispatcher = dispatcher;
    }

    @Override
    public void intercept(Player player, Crate crate, PipelineContext context, PipelineChain chain) {
        CrateSourcePipelineComponent source = context.getComponentOrNull(PipelineComponentKeys.CRATE_SOURCE);
        if (source == null) {
            chain.proceed(player, context);
            return;
        }

        ItemStack itemStack = source.getItemStack();
        if (itemStack == null) {
            chain.proceed(player, context);
            return;
        }

        if (Players.countItem(player, inv -> this.itemService.isCrateItem(inv, crate)) == 0) {
            this.dispatcher.sendBase(player, crate, CrateItemLang.PIPELINE_NO_ITEM_FOUND);
            chain.abort();
            return;
        }

        chain.proceed(player, context);
    }
}
