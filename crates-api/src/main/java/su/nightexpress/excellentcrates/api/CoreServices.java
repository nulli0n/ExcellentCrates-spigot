package su.nightexpress.excellentcrates.api;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.service.ServiceKey;
import su.nightexpress.excellentcrates.api.cost.CostAPI;
import su.nightexpress.excellentcrates.api.crate.CratesAPI;
import su.nightexpress.excellentcrates.api.key.KeysAPI;
import su.nightexpress.excellentcrates.api.preview.PreviewAPI;
import su.nightexpress.excellentcrates.api.rarity.RarityAPI;
import su.nightexpress.excellentcrates.api.reward.RewardsAPI;

@NullMarked
public final class CoreServices {

    public static final ServiceKey<CratesAPI>  CRATES  = new ServiceKey<>(CratesAPI.class, "crates");
    public static final ServiceKey<KeysAPI>    KEYS    = new ServiceKey<>(KeysAPI.class, "keys");
    public static final ServiceKey<RarityAPI>  RARITY  = new ServiceKey<>(RarityAPI.class, "rarity");
    public static final ServiceKey<RewardsAPI> REWARDS = new ServiceKey<>(RewardsAPI.class, "rewards");
    public static final ServiceKey<CostAPI>    COST    = new ServiceKey<>(CostAPI.class, "cost");
    public static final ServiceKey<PreviewAPI> PREVIEW = new ServiceKey<>(PreviewAPI.class, "preview");

    private CoreServices() {
    }
}
