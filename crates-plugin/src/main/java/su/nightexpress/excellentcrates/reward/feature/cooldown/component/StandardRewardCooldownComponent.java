package su.nightexpress.excellentcrates.reward.feature.cooldown.component;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.common.cooldown.CooldownType;
import su.nightexpress.excellentcrates.api.reward.cooldown.RewardCooldownComponent;
import su.nightexpress.excellentcrates.api.common.cooldown.CooldownOptions;

@NullMarked
public class StandardRewardCooldownComponent implements RewardCooldownComponent {

    private CooldownOptions globalCooldown;
    private CooldownOptions individualCooldown;

    public StandardRewardCooldownComponent(CooldownOptions globalCooldown, CooldownOptions individualCooldown) {
        this.globalCooldown = globalCooldown;
        this.individualCooldown = individualCooldown;
    }

    public static StandardRewardCooldownComponent createDefault() {
        CooldownOptions globalCooldown = CooldownOptions.defaults();
        CooldownOptions individualCooldown = CooldownOptions.defaults();

        return new StandardRewardCooldownComponent(globalCooldown, individualCooldown);
    }

    @Override
    public CooldownOptions getCooldown(CooldownType type) {
        return switch (type) {
            case GLOBAL -> getGlobalCooldown();
            case INDIVIDUAL -> getIndividualCooldown();
        };
    }

    @Override
    public void setCooldown(CooldownType type, CooldownOptions cooldown) {
        switch (type) {
            case GLOBAL -> this.setGlobalCooldown(cooldown);
            case INDIVIDUAL -> this.setIndividualCooldown(cooldown);
        }
    }

    @Override
    public CooldownOptions getGlobalCooldown() {
        return globalCooldown;
    }

    @Override
    public void setGlobalCooldown(CooldownOptions globalCooldown) {
        this.globalCooldown = globalCooldown;
    }

    @Override
    public CooldownOptions getIndividualCooldown() {
        return individualCooldown;
    }

    @Override
    public void setIndividualCooldown(CooldownOptions individualCooldown) {
        this.individualCooldown = individualCooldown;
    }
}
