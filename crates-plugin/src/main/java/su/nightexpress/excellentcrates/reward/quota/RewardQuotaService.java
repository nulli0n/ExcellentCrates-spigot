package su.nightexpress.excellentcrates.reward.quota;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.registry.TinyRegistry;
import su.nightexpress.excellentcrates.api.common.quota.QuotaThreshold;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.quota.RegisteredQuotaProcessor;
import su.nightexpress.excellentcrates.api.reward.quota.RewardQuotaProcessor;

@NullMarked
public class RewardQuotaService {

    private final TinyRegistry<RegisteredQuotaProcessor> processors;

    public RewardQuotaService(TinyRegistry<RegisteredQuotaProcessor> processors) {
        this.processors = processors;
    }

    private List<RewardQuotaProcessor> createPipeline() {
        return this.processors.getEntries().stream()
            .sorted(Comparator.comparingInt(RegisteredQuotaProcessor::priority))
            .map(RegisteredQuotaProcessor::processor)
            .toList();
    }

    public ActionResult testQuotas(Player player, Crate crate, Reward reward) {
        for (RewardQuotaProcessor processor : this.createPipeline()) {
            ActionResult result = processor.test(player, crate, reward);
            if (!result.success()) {
                return result;
            }
        }
        return ActionResult.ok();
    }

    public QuotaThreshold getLeastThreshold(Player player, Crate crate, Reward reward) {
        return this.processors.getEntries().stream()
            .map(RegisteredQuotaProcessor::processor)
            .map(processor -> processor.getThreshold(player, crate, reward))
            .filter(Predicate.not(QuotaThreshold::isAbsent))
            .min(Comparator.comparingInt(QuotaThreshold::threshold))
            .orElse(QuotaThreshold.absent());
    }

    public void applyQuotas(Player player, Crate crate, Reward reward) {
        for (RewardQuotaProcessor processor : this.createPipeline()) {
            processor.apply(player, crate, reward);
        }
    }
}
