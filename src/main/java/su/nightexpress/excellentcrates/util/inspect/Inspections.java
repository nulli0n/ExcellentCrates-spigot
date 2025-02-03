package su.nightexpress.excellentcrates.util.inspect;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.api.crate.Reward;
import su.nightexpress.excellentcrates.api.item.ItemProvider;
import su.nightexpress.excellentcrates.crate.impl.Cost;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.crate.reward.impl.CommandReward;
import su.nightexpress.excellentcrates.crate.reward.impl.ItemReward;
import su.nightexpress.excellentcrates.key.CrateKey;
import su.nightexpress.excellentcrates.util.CrateUtils;
import su.nightexpress.nightcore.util.wrapper.UniParticle;

import java.util.Set;
import java.util.function.Function;

public class Inspections {

    @NotNull
    public static <T> Inspection<T> named(@NotNull String name, @NotNull Function<T, InspectionInfo> function) {
        return new Inspection<>() {

            @Override
            @NotNull
            public String name() {
                return name.toLowerCase();
            }

            @Override
            @NotNull
            public InspectionInfo inspect(@NotNull T object) {
                return function.apply(object);
            }
        };
    }

    public static final Inspection<Crate> CRATE_ITEM = named("crate_item", crate -> {
        ItemProvider provider = crate.getItemProvider();
        if (!provider.canProduceItem()) return InspectionInfo.bad("Invalid item data!");

        return InspectionInfo.good("Item data is alright.");
    });

    public static final Inspection<Crate> CRATE_PREVIEW = named("crate_preview", crate -> {
        if (!crate.isPreviewEnabled()) return InspectionInfo.good("Preview is disabled.");

        return crate.isPreviewValid() ? InspectionInfo.good("Preview is valid.") : InspectionInfo.bad("Preview is invalid!");
    });

    public static final Inspection<Crate> CRATE_ANIMATION = named("crate_animation", crate -> {
        if (!crate.isAnimationEnabled()) return InspectionInfo.good("Animation is disabled.");

        return crate.isAnimationValid() ? InspectionInfo.good("Animation is valid.") : InspectionInfo.bad("Animation is invalid!");
    });

    public static final Inspection<Crate> CRATE_HOLOGRAM = named("crate_hologram", crate -> {
        if (!crate.isHologramEnabled()) return InspectionInfo.good("Hologram is disabled.");

        return crate.isHologramTemplateValid() ? InspectionInfo.good("Hologram Template is valid.") : InspectionInfo.bad("Hologram Template is invalid!");
    });

    public static final Inspection<Crate> CRATE_PARTICLE_DATA = named("crate_particle_data", crate -> {
        if (!crate.isEffectEnabled()) return InspectionInfo.good("Crate effect is disabled.");

        UniParticle uniParticle = crate.getEffectParticle();
        if (!CrateUtils.isSupportedParticleData(uniParticle)) return InspectionInfo.good("No particle data required.");

        return uniParticle.getData() == null ? InspectionInfo.bad("No particle data set!") : InspectionInfo.good("Particle data is alright.");
    });

    public static final Inspection<Crate> CRATE_KEY_REQUIREMENT = named("crate_key_requirement", crate -> {
        if (!crate.isKeyRequired()) return InspectionInfo.good("Key requirement is disabled.");

        if (crate.getKeyIds().isEmpty()) {
            return InspectionInfo.bad("No key(s) defined!");
        }
        if (crate.getRequiredKeys().size() != crate.getKeyIds().size()) {
            return InspectionInfo.bad("Some keys are invalid!");
        }

        return InspectionInfo.good("All keys are valid.");
    });

    public static final Inspection<Crate> CRATE_OPEN_COST = named("crate_open_cost", crate -> {
        Set<Cost> costs = crate.getOpenCosts();

        if (costs.isEmpty()) {
            return InspectionInfo.good("No cost(s) defined.");
        }

        if (costs.stream().anyMatch(cost -> !cost.isValid())) {
            return InspectionInfo.bad("Some costs has problems!");
        }

        return InspectionInfo.good("All costs are valid.");
    });

    public static final Inspection<Crate> CRATE_REWARDS = named("crate_rewards", crate -> {
        if (!crate.hasRewards()) return InspectionInfo.bad("No rewards defined!");

        if (crate.getRewards().stream().anyMatch(Reward::hasProblems)) {
            return InspectionInfo.bad("Some rewards has problems!");
        }

        return InspectionInfo.good("All rewards are alright.");
    });

    public static final Inspection<Reward> REWARD_CONTENT = named("reward_content", reward -> {
        if (reward instanceof CommandReward) {
            if (!reward.hasContent()) {
                return InspectionInfo.bad("No commands to run!");
            }
        }
        else if (reward instanceof ItemReward itemReward) {
            if (!reward.hasContent()) {
                return InspectionInfo.bad("No items to give!");
            }
            if (itemReward.hasInvalidItems()) {
                return InspectionInfo.bad("Has invalid item(s)!");
            }
        }

        return InspectionInfo.good("Reward content is alright.");
    });

    public static final Inspection<Reward> REWARD_PREVIEW = named("reward_preview", reward -> {
        if (reward instanceof CommandReward commandReward) {
            ItemProvider provider = commandReward.getPreview();
            if (!provider.canProduceItem()) return InspectionInfo.bad("Invalid preview data!");
        }

        return InspectionInfo.good("Preview data is alright.");
    });

    public static final Inspection<CrateKey> KEY_ITEM = named("key_item", key -> {
        ItemProvider provider = key.getProvider();
        if (!key.isVirtual() && !provider.canProduceItem()) return InspectionInfo.bad("Invalid item data!");

        return InspectionInfo.good("Item data is alright.");
    });
}
