package su.nightexpress.excellentcrates.rarity.reward.sort;

import java.util.Comparator;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.text.StringRef;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.rarity.Rarity;
import su.nightexpress.excellentcrates.api.rarity.registry.RarityRegistry;
import su.nightexpress.excellentcrates.api.rarity.reward.RarityComponent;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.component.RewardComponentKeys;
import su.nightexpress.excellentcrates.api.reward.editor.RewardSorter;
import su.nightexpress.excellentcrates.rarity.lang.RarityLang;

@NullMarked
public class RewardRarityWeightSorter implements RewardSorter {

    private static final Identifier ID = new Identifier("reward_rarity_weight");

    private final RarityRegistry rarityRegistry;

    public RewardRarityWeightSorter(RarityRegistry rarityRegistry) {
        this.rarityRegistry = rarityRegistry;
    }

    @Override
    public Comparator<Reward> getComparator(Crate crate) {
        return Comparator.comparingDouble(reward -> {
            RarityComponent rewardRarity = reward.getComponentOrNull(RewardComponentKeys.RARITY);
            if (rewardRarity == null) return 0D;

            Identifier rarityId = rewardRarity.getRarityId();
            Rarity rarity = this.rarityRegistry.get(rarityId);

            return rarity != null ? rarity.getWeight() : 0;
        });
    }

    @Override
    public StringRef getName() {
        return StringRef.localized(RarityLang.REWARD_SORTER_RARITY_WEIGHT);
    }

    @Override
    public Identifier getId() {
        return ID;
    }
}
