package su.nightexpress.excellentcrates.reward.editor.sort;

import java.util.Comparator;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.text.StringRef;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.editor.RewardSorter;
import su.nightexpress.excellentcrates.reward.editor.lang.RewardEditorLang;
import su.nightexpress.excellentcrates.reward.evaluation.RewardEvaluationService;

@NullMarked
public class RewardChanceSorter implements RewardSorter {

    private static final Identifier ID = new Identifier("reward_chance");

    private final RewardEvaluationService evaluationService;

    public RewardChanceSorter(RewardEvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    @Override
    public Comparator<Reward> getComparator(Crate crate) {
        return Comparator.comparing(reward -> evaluationService.calculateRollChance(crate, reward));
    }

    @Override
    public StringRef getName() {
        return StringRef.localized(RewardEditorLang.SORTER_CHANCE);
    }

    @Override
    public Identifier getId() {
        return ID;
    }
}
