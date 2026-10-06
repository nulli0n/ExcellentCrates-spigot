package su.nightexpress.excellentcrates.crates.cooldown.quota;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.excellentcrates.api.common.cooldown.CooldownTimestamp;
import su.nightexpress.excellentcrates.api.common.quota.QuotaThreshold;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.quota.CrateQuotaProcessor;
import su.nightexpress.excellentcrates.crates.cooldown.CrateCooldownService;
import su.nightexpress.excellentcrates.crates.cooldown.lang.CrateCooldownsLang;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.time.TimeFormatType;
import su.nightexpress.nightcore.util.time.TimeFormats;

@NullMarked
public class CrateCooldownQuotaProcessor implements CrateQuotaProcessor {

    private final CrateCooldownService cooldownService;

    public CrateCooldownQuotaProcessor(CrateCooldownService cooldownService) {
        this.cooldownService = cooldownService;
    }

    @Override
    public int getPriority() {
        return 10;
    }

    @Override
    public QuotaThreshold getThreshold(Player player, Crate crate) {
        if (!this.test(player, crate).success()) {
            return QuotaThreshold.of(0);
        }

        return this.cooldownService.hasCooldownConfigured(crate) ? QuotaThreshold.of(1) : QuotaThreshold.absent();
    }

    @Override
    public ActionResult test(Player player, Crate crate) {
        CooldownTimestamp timestamp = this.cooldownService.getExpirationTimestamp(player, crate);
        if (timestamp == null || timestamp.isExpired()) {
            return ActionResult.ok();
        }

        return ActionResult.fail(CrateCooldownsLang.QUOTA_COOLDOWN, ctx -> ctx
            .with(CommonPlaceholders.GENERIC_TIME, () -> {
                return TimeFormats.formatDuration(timestamp.expirationTimestamp(), TimeFormatType.LITERAL);
            })
        );
    }

    @Override
    public void apply(Player player, Crate crate) {
        this.cooldownService.applyCooldowns(player, crate);
    }
}
