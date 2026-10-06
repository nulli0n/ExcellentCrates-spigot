package su.nightexpress.excellentcrates.api;

import java.util.Optional;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.cost.CostAPI;
import su.nightexpress.excellentcrates.api.crate.CratesAPI;
import su.nightexpress.excellentcrates.api.key.KeysAPI;
import su.nightexpress.excellentcrates.api.preview.PreviewAPI;
import su.nightexpress.excellentcrates.api.rarity.RarityAPI;
import su.nightexpress.excellentcrates.api.reward.RewardsAPI;

@NullMarked
public interface ExcellentCratesAPI {

    CratesAPI getCratesAPI();

    Optional<CostAPI> getCostAPI();

    Optional<KeysAPI> getKeysAPI();

    Optional<PreviewAPI> getPreviewAPI();

    Optional<RarityAPI> getRarityAPI();

    Optional<RewardsAPI> getRewardAPI();
}
