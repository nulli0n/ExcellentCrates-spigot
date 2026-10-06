package su.nightexpress.excellentcrates.api.crate.pipeline;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.pipeline.context.PipelineContext;

@NullMarked
public interface PipelineExecutor {

    void execute(Player player, Crate crate, PipelineContext context);
}
