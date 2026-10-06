package su.nightexpress.excellentcrates.api.crate.cooldown.component;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.common.cooldown.CooldownType;
import su.nightexpress.excellentcrates.api.common.cooldown.CooldownOptions;
import su.nightexpress.excellentcrates.api.crate.component.CrateComponent;

@NullMarked
public interface CrateCooldownComponent extends CrateComponent {

    CooldownOptions getCooldown(CooldownType type);

    void setCooldown(CooldownType type, CooldownOptions cooldown);

    CooldownOptions getGlobalCooldown();

    void setGlobalCooldown(CooldownOptions globalCooldown);

    CooldownOptions getIndividualCooldown();

    void setIndividualCooldown(CooldownOptions individualCooldown);
}
