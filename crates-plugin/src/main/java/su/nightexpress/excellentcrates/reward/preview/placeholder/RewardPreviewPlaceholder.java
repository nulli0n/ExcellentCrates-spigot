package su.nightexpress.excellentcrates.reward.preview.placeholder;

import java.util.function.Consumer;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.crate.CrateRewardEntry;
import su.nightexpress.excellentcrates.api.reward.placeholder.RewardPlaceholder;
import su.nightexpress.excellentcrates.core.SharedPlaceholders;
import su.nightexpress.excellentcrates.reward.preview.RewardPreviewResolver;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext.Builder;

@NullMarked
public class RewardPreviewPlaceholder implements RewardPlaceholder {

    private final RewardPreviewResolver previewResolver;

    public RewardPreviewPlaceholder(RewardPreviewResolver previewResolver) {
        this.previewResolver = previewResolver;
    }

    @Override
    public Consumer<Builder> applyBase(Reward reward, @Nullable Player player) {
        return ctx -> {
            ctx.with(SharedPlaceholders.REWARD_NAME, () -> {
                return this.previewResolver.getDisplayInfo(reward).name();
            });
            ctx.with(SharedPlaceholders.REWARD_DESCRIPTION, () -> {
                return String.join("\n", this.previewResolver.getDisplayInfo(reward).lore());
            });
        };
    }

    @Override
    public Consumer<Builder> applyInCrate(CrateRewardEntry entry, Crate crate, Reward reward, @Nullable Player player) {
        return ctx -> {

        };
    }
}
