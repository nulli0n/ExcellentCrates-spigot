package su.nightexpress.excellentcrates.api.reward.commands;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentcrates.api.reward.component.RewardComponent;

@NullMarked
public interface RewardCommandsComponent extends RewardComponent {

    boolean isEmpty();

    boolean isWeightEffective();

    int countBundles();

    boolean isEnabled();

    int getIterations();

    void setIterations(int iterations);

    void setEnabled(boolean enabled);

    RewardCommandExecutionMode getGiveMode();

    void setGiveMode(RewardCommandExecutionMode giveMode);

    Map<UUID, RewardCommandPool> getBundleMap();

    void setBundleMap(Map<UUID, RewardCommandPool> bundleMap);

    @Nullable
    RewardCommandPool getBundle(UUID id);

    List<RewardCommandPool> getBundles();

    void addBundle(RewardCommandPool bundle);

    void removeBundle(UUID id);
}
