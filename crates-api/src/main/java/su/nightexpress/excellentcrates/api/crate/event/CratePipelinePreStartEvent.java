package su.nightexpress.excellentcrates.api.crate.event;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.pipeline.PipelineStage;
import su.nightexpress.excellentcrates.api.crate.pipeline.context.PipelineContext;

@NullMarked
public class CratePipelinePreStartEvent extends Event implements Cancellable {

    public static final HandlerList HANDLERS = new HandlerList();

    private final Player              player;
    private final Crate               crate;
    private final PipelineContext     context;
    private final List<PipelineStage> stages;

    private boolean cancelled;

    public CratePipelinePreStartEvent(Player player, Crate crate, PipelineContext context, List<PipelineStage> stages) {
        super();
        this.player = player;
        this.crate = crate;
        this.context = context;
        this.stages = stages;
    }

    public Player getPlayer() {
        return player;
    }

    public Crate getCrate() {
        return crate;
    }

    public PipelineContext getContext() {
        return context;
    }

    public List<PipelineStage> getStages() {
        return stages;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
