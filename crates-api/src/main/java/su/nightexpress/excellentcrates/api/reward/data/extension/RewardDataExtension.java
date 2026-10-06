package su.nightexpress.excellentcrates.api.reward.data.extension;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.data.RewardBuilder;
import su.nightexpress.nightcore.config.FileConfig;

@NullMarked
public interface RewardDataExtension {

    void onRead(FileConfig config, RewardBuilder builder);

    void onWrite(FileConfig config, Reward reward);

    void onBuild(RewardBuilder builder);

    void onLoad(Reward reward);

    void onUnload(Reward reward);

    void onCreate(Reward reward);

    void onDelete(Reward reward);
}
