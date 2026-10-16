package su.nightexpress.excellentcrates.reward.editor.sort;

import java.util.Comparator;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.text.StringRef;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.editor.RewardSorter;
import su.nightexpress.excellentcrates.reward.editor.lang.RewardEditorLang;

@NullMarked
public class RewardWeightSorter implements RewardSorter {

    private static final Identifier ID = new Identifier("reward_weight");

    @Override
    public Comparator<Reward> getComparator(Crate crate) {
        return Comparator.comparingDouble(Reward::getWeight);
    }

    @Override
    public StringRef getName() {
        return StringRef.localized(RewardEditorLang.SORTER_WEIGHT);
    }

    @Override
    public Identifier getId() {
        return ID;
    }
}
