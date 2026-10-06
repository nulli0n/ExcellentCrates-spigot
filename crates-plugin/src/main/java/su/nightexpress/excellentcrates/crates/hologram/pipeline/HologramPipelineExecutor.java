package su.nightexpress.excellentcrates.crates.hologram.pipeline;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.hologram.HologramProvider;
import su.nightexpress.excellentcrates.api.crate.pipeline.PipelineExecutor;
import su.nightexpress.excellentcrates.api.crate.pipeline.component.PipelineComponentKeys;
import su.nightexpress.excellentcrates.api.crate.pipeline.context.PipelineContext;

@NullMarked
public class HologramPipelineExecutor implements PipelineExecutor {

    private final HologramProvider provider;

    public HologramPipelineExecutor(HologramProvider provider) {
        this.provider = provider;
    }

    @Override
    public void execute(Player player, Crate crate, PipelineContext context) {
        if (context.hasComponent(PipelineComponentKeys.ANIMATION)) {
            this.provider.enableFor(crate, player);
        }
    }
}
