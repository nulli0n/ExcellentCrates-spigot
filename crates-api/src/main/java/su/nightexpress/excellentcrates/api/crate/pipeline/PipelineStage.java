package su.nightexpress.excellentcrates.api.crate.pipeline;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.pipeline.context.PipelineContext;

@NullMarked
public interface PipelineStage {

    /**
     * @param player  The player opening the crate.
     * @param crate   The crate being opened.
     * @param context The shared context.
     * @param chain   The callback to trigger the next stage in the pipeline.
     */
    void intercept(Player player, Crate crate, PipelineContext context, PipelineChain chain);
}