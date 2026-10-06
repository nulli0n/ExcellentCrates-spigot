package su.nightexpress.excellentcrates.api;

import java.util.Optional;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentcrates.api.cost.CostAPI;
import su.nightexpress.excellentcrates.api.crate.CratesAPI;
import su.nightexpress.excellentcrates.api.key.KeysAPI;
import su.nightexpress.excellentcrates.api.preview.PreviewAPI;
import su.nightexpress.excellentcrates.api.rarity.RarityAPI;
import su.nightexpress.excellentcrates.api.reward.RewardsAPI;

@NullMarked
public class StandardExcellentCratesAPI implements ExcellentCratesAPI {

    private final CratesAPI cratesAPI;

    private @Nullable CostAPI    costAPI;
    private @Nullable KeysAPI    keysAPI;
    private @Nullable PreviewAPI previewAPI;
    private @Nullable RarityAPI  rarityAPI;
    private @Nullable RewardsAPI rewardsAPI;

    private StandardExcellentCratesAPI(Builder builder) {
        this.cratesAPI = builder.cratesAPI;
        this.costAPI = builder.costAPI;
        this.keysAPI = builder.keysAPI;
        this.previewAPI = builder.previewAPI;
        this.rarityAPI = builder.rarityAPI;
        this.rewardsAPI = builder.rewardsAPI;
    }

    @Override
    public Optional<CostAPI> getCostAPI() {
        return Optional.ofNullable(this.costAPI);
    }

    @Override
    public CratesAPI getCratesAPI() {
        return this.cratesAPI;
    }

    @Override
    public Optional<KeysAPI> getKeysAPI() {
        return Optional.ofNullable(this.keysAPI);
    }

    @Override
    public Optional<PreviewAPI> getPreviewAPI() {
        return Optional.ofNullable(this.previewAPI);
    }

    @Override
    public Optional<RarityAPI> getRarityAPI() {
        return Optional.ofNullable(this.rarityAPI);
    }

    @Override
    public Optional<RewardsAPI> getRewardAPI() {
        return Optional.ofNullable(this.rewardsAPI);
    }

    public static final class Builder {

        private final CratesAPI cratesAPI;

        private @Nullable CostAPI    costAPI;
        private @Nullable KeysAPI    keysAPI;
        private @Nullable PreviewAPI previewAPI;
        private @Nullable RarityAPI  rarityAPI;
        private @Nullable RewardsAPI rewardsAPI;

        public Builder(CratesAPI cratesAPI) {
            this.cratesAPI = cratesAPI;
        }

        public Builder setCostAPI(@Nullable CostAPI costAPI) {
            this.costAPI = costAPI;
            return this;
        }

        public Builder setKeysAPI(@Nullable KeysAPI keysAPI) {
            this.keysAPI = keysAPI;
            return this;
        }

        public Builder setPreviewAPI(@Nullable PreviewAPI previewAPI) {
            this.previewAPI = previewAPI;
            return this;
        }

        public Builder setRarityAPI(@Nullable RarityAPI rarityAPI) {
            this.rarityAPI = rarityAPI;
            return this;
        }

        public Builder setRewardsAPI(@Nullable RewardsAPI rewardsAPI) {
            this.rewardsAPI = rewardsAPI;
            return this;
        }

        public StandardExcellentCratesAPI build() {
            return new StandardExcellentCratesAPI(this);
        }
    }


}
