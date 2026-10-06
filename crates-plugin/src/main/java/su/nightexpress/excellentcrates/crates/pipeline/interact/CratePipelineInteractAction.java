package su.nightexpress.excellentcrates.crates.pipeline.interact;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.interact.action.InteractAction;
import su.nightexpress.excellentcrates.api.crate.interact.context.CrateInteractContext;
import su.nightexpress.excellentcrates.api.crate.interact.context.ItemCrateInteractContext;
import su.nightexpress.excellentcrates.api.crate.interact.context.LocatedCrateInteractContext;
import su.nightexpress.excellentcrates.api.crate.pipeline.component.CrateSourcePipelineComponent;
import su.nightexpress.excellentcrates.api.crate.pipeline.component.FastOpenPipelineComponent;
import su.nightexpress.excellentcrates.api.crate.pipeline.component.PipelineComponentKeys;
import su.nightexpress.excellentcrates.crates.pipeline.CratePipelineService;
import su.nightexpress.excellentcrates.crates.pipeline.component.DefaultCrateSourcePipelineComponent;
import su.nightexpress.excellentcrates.crates.pipeline.component.DefaultFastOpenPipelineComponent;

@NullMarked
public class CratePipelineInteractAction implements InteractAction {

    private static final Identifier ID = new Identifier("open_crate");

    private final CratePipelineService pipeline;

    public CratePipelineInteractAction(CratePipelineService pipeline) {
        this.pipeline = pipeline;
    }

    @Override
    public void perform(CrateInteractContext context) {
        Player player = context.player();
        Crate crate = context.crate();

        boolean isSneaking = player.isSneaking();

        this.pipeline.startPipeline(player, crate, pipelineContext -> {
            CrateSourcePipelineComponent source = new DefaultCrateSourcePipelineComponent();

            if (context instanceof LocatedCrateInteractContext located) {
                source.setLocation(located.location());
            }
            if (context instanceof ItemCrateInteractContext itemContext) {
                source.setItemStack(itemContext.item());
            }

            if (isSneaking) {
                FastOpenPipelineComponent fastOpen = new DefaultFastOpenPipelineComponent();
                pipelineContext.putComponent(PipelineComponentKeys.FAST_OPEN, fastOpen);
            }

            pipelineContext.putComponent(PipelineComponentKeys.CRATE_SOURCE, source);
        });
    }

    @Override
    public Identifier getId() {
        return ID;
    }
}
