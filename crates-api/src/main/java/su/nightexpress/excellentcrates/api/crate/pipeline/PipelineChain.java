package su.nightexpress.excellentcrates.api.crate.pipeline;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.pipeline.context.PipelineContext;

@NullMarked
public interface PipelineChain {

    void proceed(Player player, PipelineContext context);

    void abort();
}
