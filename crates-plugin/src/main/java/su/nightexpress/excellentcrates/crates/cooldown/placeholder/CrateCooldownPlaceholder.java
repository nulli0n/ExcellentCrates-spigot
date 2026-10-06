package su.nightexpress.excellentcrates.crates.cooldown.placeholder;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.placeholder.PlaceholderApplier;
import su.nightexpress.excellentcrates.api.common.cooldown.CooldownOptions;
import su.nightexpress.excellentcrates.api.common.cooldown.CooldownTimestamp;
import su.nightexpress.excellentcrates.api.common.cooldown.CooldownType;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.cooldown.component.CrateCooldownComponent;
import su.nightexpress.excellentcrates.api.crate.placeholder.CratePlaceholder;
import su.nightexpress.excellentcrates.core.SharedPlaceholders;
import su.nightexpress.excellentcrates.core.lang.Lang;
import su.nightexpress.excellentcrates.crates.cooldown.CrateCooldownService;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext.Builder;
import su.nightexpress.nightcore.util.time.TimeFormatType;
import su.nightexpress.nightcore.util.time.TimeFormats;

@NullMarked
public class CrateCooldownPlaceholder implements CratePlaceholder {

    private final CrateCooldownService cooldownService;

    public CrateCooldownPlaceholder(CrateCooldownService cooldownService) {
        this.cooldownService = cooldownService;
    }

    @Override
    public PlaceholderApplier apply(Crate crate) {
        return builder -> this.addInitialCooldownPlaceholders(builder, crate);
    }

    @Override
    public PlaceholderApplier apply(Crate crate, Player player) {
        return builder -> {
            this.addInitialCooldownPlaceholders(builder, crate);
            this.addEffectiveCooldownPlaceholder(builder, crate, player);
        };
    }

    private void addEffectiveCooldownPlaceholder(Builder ctx, Crate crate, Player player) {
        ctx.with(SharedPlaceholders.CRATE_EFFECTIVE_COOLDOWN, () -> {
            CooldownTimestamp timestamp = this.cooldownService.getExpirationTimestamp(player, crate);
            if (timestamp == null || timestamp.isExpired()) {
                return Lang.FORMAT_COOLDOWN_READY.text();
            }

            if (timestamp.isPermanent()) {
                return CoreLang.OTHER_NEVER.text();
            }

            return TimeFormats.formatDuration(timestamp.expirationTimestamp(), TimeFormatType.LITERAL);
        });
    }

    private void addInitialCooldownPlaceholders(Builder ctx, Crate crate) {
        CrateCooldownComponent cooldowns = this.cooldownService.getCrateCooldowns(crate);
        if (cooldowns == null) return;

        for (CooldownType type : CooldownType.values()) {
            CooldownOptions cooldown = cooldowns.getCooldown(type);
            if (!cooldown.isEffectivelyEnabled()) continue;

            String placeholder = switch (type) {
                case GLOBAL -> SharedPlaceholders.CRATE_INITIAL_GLOBAL_COOLDOWN;
                case INDIVIDUAL -> SharedPlaceholders.CRATE_INITIAL_INDIVIDUAL_COOLDOWN;
            };

            long cooldownTimestamp = cooldown.createCooldownTimestamp();

            ctx.with(placeholder, () -> TimeFormats.formatDuration(cooldownTimestamp, TimeFormatType.LITERAL));
        }
    }
}
