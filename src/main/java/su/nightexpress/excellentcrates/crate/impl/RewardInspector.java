package su.nightexpress.excellentcrates.crate.impl;

import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.placeholder.PlaceholderMap;
import su.nexmedia.engine.utils.ItemUtil;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.util.Inspector;

import java.util.List;

public class RewardInspector extends Inspector {

    //private final ExcellentCratesPlugin plugin;
    private final Reward                reward;
    private final PlaceholderMap        placeholderMap;

    public RewardInspector(@NotNull Reward reward) {
        //this.plugin = reward.plugin();
        this.reward = reward;
        this.placeholderMap = Placeholders.forRewardInspector(this);
    }

    @Override
    @NotNull
    public PlaceholderMap getPlaceholders() {
        return this.placeholderMap;
    }

    @NotNull
    public Reward getReward() {
        return reward;
    }

    public boolean hasContent() {
        return !this.getReward().getItems().isEmpty() || !this.getReward().getCommands().isEmpty();
    }

    @NotNull
    public List<String> formatItemList() {
        return this.getReward().getItems().stream()
            .filter(item -> !item.getType().isAir())
            .map(item -> good(ItemUtil.getItemName(item))).toList();
    }

    @NotNull
    public List<String> formatCommandList() {
        return this.getReward().getCommands().stream().map(Inspector::good).toList();
    }
}
