package su.nightexpress.excellentcrates.reward.feature.limit.component;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.common.limit.LimitOptions;
import su.nightexpress.excellentcrates.api.common.limit.LimitType;
import su.nightexpress.excellentcrates.api.reward.limit.RewardLimitComponent;
import su.nightexpress.excellentcrates.api.reward.registry.RewardId;
import su.nightexpress.excellentcrates.core.common.limit.DefaultLimitOptions;

@NullMarked
public class DefaultRewardLimitComponent implements RewardLimitComponent {

    private boolean      enabled;
    private LimitOptions globalOptions;
    private LimitOptions individualOptions;
    private boolean      alternativeEnabled;
    private RewardId     alternativeRewardId;

    DefaultRewardLimitComponent(Builder builder) {
        this.enabled = builder.enabled;
        this.globalOptions = builder.globalOptions;
        this.individualOptions = builder.individualOptions;
        this.alternativeEnabled = builder.alternativeEnabled;
        this.alternativeRewardId = builder.alternativeRewardId;
    }

    public static DefaultRewardLimitComponent createDefault() {
        return new Builder().build();
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public LimitOptions getOptions(LimitType type) {
        return switch (type) {
            case GLOBAL -> this.globalOptions;
            case INDIVIDUAL -> this.individualOptions;
        };
    }

    @Override
    public void setOptions(LimitType type, LimitOptions options) {
        switch (type) {
            case GLOBAL -> this.globalOptions = options;
            case INDIVIDUAL -> this.individualOptions = options;
        }
    }

    @Override
    public LimitOptions getGlobalOptions() {
        return this.globalOptions;
    }

    @Override
    public void setGlobalOptions(LimitOptions options) {
        this.globalOptions = options;
    }

    @Override
    public LimitOptions getIndividualOptions() {
        return this.individualOptions;
    }

    @Override
    public void setIndividualOptions(LimitOptions options) {
        this.individualOptions = options;
    }

    @Override
    public boolean isAlternativeEnabled() {
        return this.alternativeEnabled;
    }

    @Override
    public void setAlternativeEnabled(boolean enabled) {
        this.alternativeEnabled = enabled;
    }

    @Override
    public RewardId getAlternativeRewardId() {
        return this.alternativeRewardId;
    }

    @Override
    public void setAlternativeRewardId(RewardId rewardId) {
        this.alternativeRewardId = rewardId;
    }

    public static class Builder {

        private boolean      enabled;
        private LimitOptions globalOptions;
        private LimitOptions individualOptions;
        private boolean      alternativeEnabled;
        private RewardId     alternativeRewardId;

        public Builder() {
            this.enabled = false;
            this.globalOptions = DefaultLimitOptions.createDefault();
            this.individualOptions = DefaultLimitOptions.createDefault();
            this.alternativeEnabled = false;
            this.alternativeRewardId = new RewardId(new Identifier("none"), new Identifier("none"));
        }

        public Builder setEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public Builder setGlobalOptions(LimitOptions options) {
            this.globalOptions = options;
            return this;
        }

        public Builder setIndividualOptions(LimitOptions options) {
            this.individualOptions = options;
            return this;
        }

        public Builder setAlternativeEnabled(boolean enabled) {
            this.alternativeEnabled = enabled;
            return this;
        }

        public Builder setAlternativeRewardId(RewardId rewardId) {
            this.alternativeRewardId = rewardId;
            return this;
        }

        public DefaultRewardLimitComponent build() {
            return new DefaultRewardLimitComponent(this);
        }
    }
}
