package su.nightexpress.excellentcrates.api.crate.quota;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.excellentcrates.api.common.quota.QuotaThreshold;
import su.nightexpress.excellentcrates.api.crate.Crate;

@NullMarked
public interface CrateQuotaProcessor {

    int getPriority();

    QuotaThreshold getThreshold(Player player, Crate crate);

    ActionResult test(Player player, Crate crate);

    void apply(Player player, Crate crate);
}
