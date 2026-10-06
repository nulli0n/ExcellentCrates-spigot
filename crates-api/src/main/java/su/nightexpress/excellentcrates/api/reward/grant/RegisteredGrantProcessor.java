package su.nightexpress.excellentcrates.api.reward.grant;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record RegisteredGrantProcessor(int priority, RewardGrantProcessor processor) {

}
