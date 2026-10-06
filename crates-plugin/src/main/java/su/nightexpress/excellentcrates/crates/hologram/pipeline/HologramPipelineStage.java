package su.nightexpress.excellentcrates.crates.hologram.pipeline;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.hologram.HologramProvider;
import su.nightexpress.excellentcrates.api.crate.pipeline.PipelineChain;
import su.nightexpress.excellentcrates.api.crate.pipeline.PipelineStage;
import su.nightexpress.excellentcrates.api.crate.pipeline.component.PipelineComponentKeys;
import su.nightexpress.excellentcrates.api.crate.pipeline.context.PipelineContext;

@NullMarked
public class HologramPipelineStage implements PipelineStage {

    private final HologramProvider provider;

    public HologramPipelineStage(HologramProvider provider) {
        this.provider = provider;
    }

    @Override
    public void intercept(Player player, Crate crate, PipelineContext context, PipelineChain chain) {
        if (context.hasComponent(PipelineComponentKeys.ANIMATION)) {
            this.provider.disableFor(crate, player);
        }
        chain.proceed(player, context);
    }
}
