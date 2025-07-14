package su.nightexpress.excellentcrates.opening.world.impl;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.Reward;
import su.nightexpress.excellentcrates.crate.impl.CrateSource;
import su.nightexpress.excellentcrates.key.CrateKey;
import su.nightexpress.excellentcrates.opening.AbstractOpening;

public class DummyOpening extends AbstractOpening {

    private boolean rolled;

    public DummyOpening(@NotNull CratesPlugin plugin, @NotNull Player player, @NotNull CrateSource source, @Nullable CrateKey key) {
        super(plugin, player, source, key);
    }

    @Override
    public void instaRoll() {
        this.roll();
        this.stop();
    }

    @Override
    public boolean isCompleted() {
        return this.rolled;
    }

    @Override
    public long getInterval() {
        return 1L;
    }

    @Override
    protected void onStart() {

    }

    @Override
    protected void onTick() {
        this.roll();
    }

    @Override
    protected void onComplete() {

    }

    private void roll() {
        this.setRefundable(false);

        Reward reward = this.getCrate().rollReward(this.player);
        reward.give(this.player);

        this.rolled = true;
    }
}
