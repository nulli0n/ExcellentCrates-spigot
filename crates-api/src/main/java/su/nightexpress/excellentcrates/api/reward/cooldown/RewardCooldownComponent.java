package su.nightexpress.excellentcrates.api.reward.cooldown;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.common.cooldown.CooldownType;
import su.nightexpress.excellentcrates.api.common.cooldown.CooldownOptions;
import su.nightexpress.excellentcrates.api.reward.component.RewardComponent;

@NullMarked
public interface RewardCooldownComponent extends RewardComponent {

    CooldownOptions getCooldown(CooldownType type);

    void setCooldown(CooldownType type, CooldownOptions cooldown);

    CooldownOptions getGlobalCooldown();

    void setGlobalCooldown(CooldownOptions globalCooldown);

    CooldownOptions getIndividualCooldown();

    void setIndividualCooldown(CooldownOptions individualCooldown);
}
