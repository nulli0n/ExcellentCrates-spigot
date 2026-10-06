package su.nightexpress.excellentcrates.crates.open.pipeline;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.pipeline.PipelineProcessor;
import su.nightexpress.excellentcrates.api.crate.pipeline.context.PipelineContext;

@NullMarked
public class DefaultPipelineProcessor implements PipelineProcessor {

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public boolean shouldHandle(Crate crate, PipelineContext context) {
        return true;
    }

    @Override
    public void process(Player player, PipelineContext context, Runnable onComplete) {
        onComplete.run();
    }
}
