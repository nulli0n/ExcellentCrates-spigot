package su.nightexpress.excellentcrates.core.animation;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.animation.AnimationContext;
import su.nightexpress.excellentcrates.api.animation.AnimationInstance;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.pipeline.reward.PipelineReward;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.preview.RewardPreviewAPI;

@NullMarked
public abstract class BaseAnimationInstance implements AnimationInstance {

    protected final Player           player;
    protected final Location         location;
    protected final Crate            crate;
    protected final PipelineReward   reward;
    protected final List<Reward>     availableRewards;
    protected final RewardPreviewAPI rewardPreview;

    protected final Runnable onComplete;

    private boolean running;

    public BaseAnimationInstance(AnimationContext context, Runnable onComplete) {
        this.player = context.player();
        this.location = context.location();
        this.crate = context.crate();
        this.reward = context.reward();
        this.availableRewards = context.availableRewards();
        this.rewardPreview = context.rewardPreview();

        this.onComplete = onComplete;
    }

    public void complete() {
        this.onComplete.run();
    }


    @Override
    public boolean isRunning() {
        return this.running;
    }

    @Override
    public void start() {
        this.running = true;
        this.onStart();
    }

    @Override
    public void stop() {
        this.running = false;
        this.onStop();
        this.complete();
    }

    @Override
    public boolean tick() {
        return this.running && this.onTick();
    }

    protected abstract void onStart();

    protected abstract void onStop();

    protected abstract boolean onTick();
}
