package su.nightexpress.excellentcrates.reward.preview.extension;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.data.RewardBuilder;
import su.nightexpress.excellentcrates.api.reward.data.extension.RewardDataExtension;
import su.nightexpress.excellentcrates.reward.preview.RewardPreviewResolver;
import su.nightexpress.nightcore.config.FileConfig;

@NullMarked
public class RewardPreviewDataExtension implements RewardDataExtension {

    private final RewardPreviewResolver previewResolver;

    public RewardPreviewDataExtension(RewardPreviewResolver previewResolver) {
        this.previewResolver = previewResolver;
    }

    @Override
    public void onBuild(RewardBuilder builder) {

    }

    @Override
    public void onCreate(Reward reward) {
        this.previewResolver.invalidateCache(reward.id());
    }

    @Override
    public void onDelete(Reward reward) {
        this.previewResolver.invalidateCache(reward.id());
    }

    @Override
    public void onLoad(Reward reward) {
        this.previewResolver.invalidateCache(reward.id());
    }

    @Override
    public void onRead(FileConfig config, RewardBuilder builder) {

    }

    @Override
    public void onUnload(Reward reward) {
        this.previewResolver.invalidateCache(reward.id());
    }

    @Override
    public void onWrite(FileConfig config, Reward reward) {
        this.previewResolver.invalidateCache(reward.id());
    }
}
