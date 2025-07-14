package su.nightexpress.excellentcrates.opening.inventory.spinner;

import org.bukkit.Sound;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.bridge.wrap.NightSound;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.sound.VanillaSound;

import java.util.List;

public class SpinnerBuilder {

    private SpinnerBuilder() {

    }

    @NotNull
    public static NameStep rewardBuilder() {
        return newBuilder(SpinnerType.REWARD);
    }

    @NotNull
    public static NameStep animationBuilder() {
        return newBuilder(SpinnerType.ANIMATION);
    }

    @NotNull
    public static NameStep newBuilder(@NotNull SpinnerType mode) {
        return new Steps(mode);
    }

    public interface NameStep {

        @NotNull ModeStep name(@NotNull String name);
    }

    public interface ModeStep {

        @NotNull SpinnerStep mode(@NotNull SpinMode mode);
    }

    public interface SpinnerStep {

        SlotsStep spinnerId(@NotNull String spinnerId);
    }

    public interface SlotsStep {

        @NotNull DelayStep slots(int... slots);
    }

    public interface DelayStep {

        @NotNull SpinsStep delay(int spinDelay);
    }

    public interface SpinsStep {

        @NotNull ProviderStep steps(SpinStep... spins);
    }

    public interface ProviderStep {

        @NotNull BuildStep provider(@NotNull SpinnerProvider provider);
    }

    public interface BuildStep {

        @NotNull BuildStep sound(@NotNull Sound sound);

        @NotNull SpinnerHolder build();
    }

    private static class Steps implements NameStep, ModeStep, SpinnerStep, SlotsStep, DelayStep, SpinsStep, ProviderStep, BuildStep {

        private final SpinnerType type;

        private String          name;
        private SpinMode        mode;
        private String          spinnerId;
        private int[]           slots;
        private int             spinDelay;
        private List<SpinStep>  spinSteps;
        private NightSound      sound;
        private SpinnerProvider provider;

        private Steps(@NotNull SpinnerType type) {
            this.type = type;
            this.sound = null;
        }

        @NotNull
        public BuildStep sound(@NotNull Sound sound) {
            this.sound = VanillaSound.of(sound, 0.8f, 1f);
            return this;
        }

        @Override
        @NotNull
        public ModeStep name(@NotNull String name) {
            this.name = name;
            return this;
        }

        @Override
        @NotNull
        public SpinnerStep mode(@NotNull SpinMode mode) {
            this.mode = mode;
            return this;
        }

        @Override
        public SlotsStep spinnerId(@NotNull String spinnerId) {
            this.spinnerId = spinnerId;
            return this;
        }

        @Override
        @NotNull
        public SpinsStep delay(int spinDelay) {
            this.spinDelay = spinDelay;
            return this;
        }

        @Override
        @NotNull
        public DelayStep slots(int... slots) {
            this.slots = slots;
            return this;
        }

        @Override
        @NotNull
        public ProviderStep steps(SpinStep... steps) {
            this.spinSteps = Lists.newList(steps);
            return this;
        }

        @Override
        @NotNull
        public BuildStep provider(@NotNull SpinnerProvider provider) {
            this.provider = provider;
            return this;
        }

        @Override
        @NotNull
        public SpinnerHolder build() {
            SpinnerData data = new SpinnerData(this.spinnerId, this.mode, this.slots, this.spinDelay, this.spinSteps, this.sound);
            return new SpinnerHolder(this.name, this.type, data, this.provider);
        }
    }
}