package su.nightexpress.excellentcrates.reward.feature.commands.component.data;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentcrates.api.reward.commands.RewardCommandPool;
import su.nightexpress.excellentcrates.api.reward.commands.RewardCommandsComponent;
import su.nightexpress.excellentcrates.api.reward.commands.RewardCommandExecutionMode;

@NullMarked
public class DefaultRewardCommandsComponent implements RewardCommandsComponent {

    private boolean                      enabled;
    private int                          iterations;
    private RewardCommandExecutionMode   giveMode;
    private Map<UUID, RewardCommandPool> bundleMap;

    public DefaultRewardCommandsComponent(boolean enabled,
                                          int iterations,
                                          RewardCommandExecutionMode giveMode,
                                          Map<UUID, RewardCommandPool> bundleMap) {
        this.enabled = enabled;
        this.iterations = iterations;
        this.giveMode = giveMode;
        this.bundleMap = new LinkedHashMap<>(bundleMap);
    }

    public static DefaultRewardCommandsComponent createDefault() {
        return new DefaultRewardCommandsComponent(true, 1, RewardCommandExecutionMode.NORMAL, Map.of());
    }

    @Override
    public boolean isEmpty() {
        return this.bundleMap.isEmpty();
    }

    @Override
    public boolean isWeightEffective() {
        return this.giveMode == RewardCommandExecutionMode.WEIGHTED || this.giveMode == RewardCommandExecutionMode.CHANCE;
    }

    @Override
    public int countBundles() {
        return this.bundleMap.size();
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public int getIterations() {
        return this.iterations;
    }

    @Override
    public void setIterations(int amount) {
        this.iterations = Math.max(1, amount);
    }

    @Override
    public Map<UUID, RewardCommandPool> getBundleMap() {
        return Collections.unmodifiableMap(this.bundleMap);
    }

    @Override
    public RewardCommandExecutionMode getGiveMode() {
        return this.giveMode;
    }

    @Override
    public void setGiveMode(RewardCommandExecutionMode giveMode) {
        this.giveMode = giveMode;
    }

    @Override
    public void setBundleMap(Map<UUID, RewardCommandPool> bundleMap) {
        this.bundleMap = new LinkedHashMap<>(bundleMap);

        /* this.bundles = Lists.modify(bundleMap, str -> str
            // Legacy placeholder validation
            .replace("[CONSOLE]", "")
            .replace("%player%", CratesPlaceholders.PLAYER_NAME)
            .trim());
        this.bundles.removeIf(String::isBlank); */
    }

    @Override
    public void addBundle(RewardCommandPool bundle) {
        this.bundleMap.put(bundle.getId(), bundle);
    }

    @Override
    public void removeBundle(UUID id) {
        this.bundleMap.remove(id);
    }

    @Override
    public @Nullable RewardCommandPool getBundle(UUID id) {
        return this.bundleMap.get(id);
    }

    @Override
    public List<RewardCommandPool> getBundles() {
        return List.copyOf(this.bundleMap.values());
    }
}
