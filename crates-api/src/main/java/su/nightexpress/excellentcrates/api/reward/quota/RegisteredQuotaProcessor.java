package su.nightexpress.excellentcrates.api.reward.quota;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record RegisteredQuotaProcessor(int priority, RewardQuotaProcessor processor) {

}
