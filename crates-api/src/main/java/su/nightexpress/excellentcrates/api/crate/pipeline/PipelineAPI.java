package su.nightexpress.excellentcrates.api.crate.pipeline;

import java.util.function.Consumer;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.pipeline.context.PipelineContext;

@NullMarked
public interface PipelineAPI {

    void registerStage(PipelinePhase phase, int order, PipelineStage stage);

    void registerExecutor(PipelineExecutor executor);

    void registerProcessor(PipelineProcessor processor);

    void startPipeline(Player player, Crate crate);

    void startPipeline(Player player, Crate crate, Consumer<PipelineContext> consumer);
}
