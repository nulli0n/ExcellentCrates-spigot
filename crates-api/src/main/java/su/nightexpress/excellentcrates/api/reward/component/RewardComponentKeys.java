package su.nightexpress.excellentcrates.api.reward.component;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.entity.EntityComponentKey;
import su.nightexpress.excellentcrates.api.rarity.reward.RarityComponent;
import su.nightexpress.excellentcrates.api.reward.broadcast.RewardBroadcastComponent;
import su.nightexpress.excellentcrates.api.reward.commands.RewardCommandsComponent;
import su.nightexpress.excellentcrates.api.reward.cooldown.RewardCooldownComponent;
import su.nightexpress.excellentcrates.api.reward.items.RewardItemsComponent;
import su.nightexpress.excellentcrates.api.reward.limit.RewardLimitComponent;

@NullMarked
public final class RewardComponentKeys {

    public static final EntityComponentKey<RewardBroadcastComponent> BROADCAST = EntityComponentKey.of(
        "broadcast",
        RewardBroadcastComponent.class
    );

    public static final EntityComponentKey<RewardCommandsComponent> COMMANDS = EntityComponentKey.of(
        "commands",
        RewardCommandsComponent.class
    );

    public static final EntityComponentKey<RewardItemsComponent> ITEMS = EntityComponentKey.of(
        "items",
        RewardItemsComponent.class
    );

    public static final EntityComponentKey<RewardLimitComponent> LIMIT = EntityComponentKey.of("reward.limit",
        RewardLimitComponent.class);

    public static final EntityComponentKey<RewardCooldownComponent> COOLDOWN = EntityComponentKey.of("reward_cooldowns",
        RewardCooldownComponent.class);

    public static final EntityComponentKey<RarityComponent> RARITY = EntityComponentKey.of("rarity",
        RarityComponent.class);

    private RewardComponentKeys() {
    }
}
