package su.nightexpress.excellentcrates.data.legacy;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class LegacyCrateData {

    private final Map<String, LegacyLimitData> rewardDataMap;

    public LegacyCrateData(@NotNull Map<String, LegacyLimitData> rewardDataMap) {
        this.rewardDataMap = rewardDataMap;
    }

    @NotNull
    public Map<String, LegacyLimitData> getRewardDataMap() {
        return rewardDataMap;
    }
}
