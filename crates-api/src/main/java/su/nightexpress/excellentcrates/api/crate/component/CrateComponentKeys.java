package su.nightexpress.excellentcrates.api.crate.component;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.entity.EntityComponentKey;
import su.nightexpress.excellentcrates.api.animation.component.AnimationComponent;
import su.nightexpress.excellentcrates.api.crate.block.crate.BlockComponent;
import su.nightexpress.excellentcrates.api.crate.cooldown.component.CrateCooldownComponent;
import su.nightexpress.excellentcrates.api.crate.hologram.component.HologramComponent;
import su.nightexpress.excellentcrates.api.crate.open.OpenActionsComponent;
import su.nightexpress.excellentcrates.api.effect.crate.EffectComponent;
import su.nightexpress.excellentcrates.api.key.crate.KeyRequirementComponent;
import su.nightexpress.excellentcrates.api.preview.crate.PreviewComponent;
import su.nightexpress.excellentcrates.api.reward.crate.CrateRewardsComponent;
import su.nightexpress.excellentcrates.api.reward.selectable.SelectableRewardsComponent;

@NullMarked
public final class CrateComponentKeys {

    public static final EntityComponentKey<SelectableRewardsComponent> SELECTABLE_REWARDS = EntityComponentKey.of(
        "selectable_rewards",
        SelectableRewardsComponent.class
    );

    public static final EntityComponentKey<KeyRequirementComponent> KEY_REQUIREMENT = EntityComponentKey.of(
        "keys",
        KeyRequirementComponent.class
    );

    public static final EntityComponentKey<AnimationComponent> ANIMATION = EntityComponentKey.of("animation",
        AnimationComponent.class);

    public static final EntityComponentKey<CrateRewardsComponent> REWARDS = EntityComponentKey.of("rewards",
        CrateRewardsComponent.class);

    public static final EntityComponentKey<BlockComponent> BLOCK = EntityComponentKey.of(
        "block",
        BlockComponent.class
    );

    public static final EntityComponentKey<EffectComponent> EFFECT = EntityComponentKey.of("effect",
        EffectComponent.class);

    public static final EntityComponentKey<HologramComponent> HOLOGRAM = EntityComponentKey.of(
        "hologram",
        HologramComponent.class
    );

    public static final EntityComponentKey<OpenActionsComponent> OPEN_ACTIONS = EntityComponentKey.of("opening",
        OpenActionsComponent.class);

    public static final EntityComponentKey<CrateCooldownComponent> COOLDOWN = EntityComponentKey.of("crate_cooldowns",
        CrateCooldownComponent.class);

    public static final EntityComponentKey<PreviewComponent> PREVIEW = EntityComponentKey.of("preview",
        PreviewComponent.class);

    private CrateComponentKeys() {
    }
}
