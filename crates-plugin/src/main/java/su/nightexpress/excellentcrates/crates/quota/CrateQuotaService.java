package su.nightexpress.excellentcrates.crates.quota;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.registry.TinyRegistry;
import su.nightexpress.excellentcrates.api.common.quota.QuotaThreshold;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.quota.CrateQuotaProcessor;

@NullMarked
public class CrateQuotaService {

    private final TinyRegistry<CrateQuotaProcessor> quotaProcessors;

    public CrateQuotaService(TinyRegistry<CrateQuotaProcessor> quotaProcessors) {
        this.quotaProcessors = quotaProcessors;
    }

    private List<CrateQuotaProcessor> createPipeline() {
        return this.quotaProcessors.getEntries().stream()
            .sorted(Comparator.comparingInt(CrateQuotaProcessor::getPriority))
            .toList();
    }

    public ActionResult testQuotas(Player player, Crate crate) {
        for (CrateQuotaProcessor processor : this.createPipeline()) {
            ActionResult result = processor.test(player, crate);
            if (!result.success()) {
                return result;
            }
        }
        return ActionResult.ok();
    }

    public QuotaThreshold getLeastThreshold(Player player, Crate crate) {
        return this.quotaProcessors.getEntries().stream()
            .map(processor -> processor.getThreshold(player, crate))
            .filter(Predicate.not(QuotaThreshold::isAbsent))
            .min(Comparator.comparingInt(QuotaThreshold::threshold))
            .orElse(QuotaThreshold.absent());
    }

    public void applyQuotas(Player player, Crate crate) {
        for (CrateQuotaProcessor processor : this.createPipeline()) {
            processor.apply(player, crate);
        }
    }
}
