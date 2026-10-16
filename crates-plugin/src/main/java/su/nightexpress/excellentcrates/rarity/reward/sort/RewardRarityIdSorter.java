package su.nightexpress.excellentcrates.rarity.reward.sort;

import java.util.Comparator;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.text.StringRef;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.rarity.reward.RarityComponent;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.component.RewardComponentKeys;
import su.nightexpress.excellentcrates.api.reward.editor.RewardSorter;
import su.nightexpress.excellentcrates.rarity.lang.RarityLang;

@NullMarked
public class RewardRarityIdSorter implements RewardSorter {

    private static final Identifier ID = new Identifier("reward_rarity_id");

    @Override
    public Comparator<Reward> getComparator(Crate crate) {
        return Comparator.comparing(reward -> {
            RarityComponent rarity = reward.getComponentOrNull(RewardComponentKeys.RARITY);
            return rarity != null ? rarity.getRarityId().value() : "";
        });
    }

    @Override
    public StringRef getName() {
        return StringRef.localized(RarityLang.REWARD_SORTER_RARITY_ID);
    }

    @Override
    public Identifier getId() {
        return ID;
    }
}
