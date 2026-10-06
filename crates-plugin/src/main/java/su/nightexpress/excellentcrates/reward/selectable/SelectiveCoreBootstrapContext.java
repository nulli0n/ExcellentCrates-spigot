package su.nightexpress.excellentcrates.reward.selectable;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.reward.registry.RewardRegistry;
import su.nightexpress.excellentcrates.reward.quota.RewardQuotaService;
import su.nightexpress.excellentcrates.reward.selectable.lang.SelectableLang;

@NullMarked
public class SelectiveCoreBootstrapContext {

    public final SelectivePickService pickService;

    public SelectiveCoreBootstrapContext(CratesPlugin plugin, RewardRegistry rewards, RewardQuotaService quotaService) {
        plugin.injectLang(SelectableLang.class);

        this.pickService = new SelectivePickService(rewards, quotaService);
    }
}
