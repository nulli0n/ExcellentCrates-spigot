package su.nightexpress.excellentcrates.dialog;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.dialog.cost.*;
import su.nightexpress.excellentcrates.dialog.crate.*;
import su.nightexpress.excellentcrates.dialog.key.KeyCreationDialog;
import su.nightexpress.excellentcrates.dialog.key.KeyItemDialog;
import su.nightexpress.excellentcrates.dialog.key.KeyNameDialog;
import su.nightexpress.excellentcrates.dialog.reward.*;
import su.nightexpress.nightcore.manager.AbstractManager;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class CrateDialogs extends AbstractManager<CratesPlugin> {

    private static final Set<Holder<?>> HOLDERS = new HashSet<>();

    public static final Holder<CrateCreationDialog>       CRATE_CREATION        = new Holder<>();
    public static final Holder<CrateNameDialog>           CRATE_NAME            = new Holder<>();
    public static final Holder<CrateDescriptionDialog>    CRATE_DESCRIPTION     = new Holder<>();
    public static final Holder<CrateItemDialog>           CRATE_ITEM            = new Holder<>();
    public static final Holder<CratePreviewDialog>        CRATE_PREVIEW         = new Holder<>();
    public static final Holder<CrateOpeningDialog>        CRATE_OPENING         = new Holder<>();
    public static final Holder<CrateCooldownDialog>       CRATE_COOLDOWN        = new Holder<>();
    public static final Holder<CrateEffectDialog>         CRATE_EFFECT          = new Holder<>();
    public static final Holder<CrateParticleDialog>       CRATE_PARTICLE        = new Holder<>();
    public static final Holder<CrateHologramDialog>       CRATE_HOLOGRAM        = new Holder<>();
    public static final Holder<CostCreationDialog>        COST_CREATION         = new Holder<>();
    public static final Holder<CostNameDialog>            COST_NAME             = new Holder<>();
    public static final Holder<CostEntryCreationDialog>   COST_ENTRY_CREATION   = new Holder<>();
    public static final Holder<KeyCostOptionsDialog>      KEY_COST_OPTIONS      = new Holder<>();
    public static final Holder<CurrencyCostOptionsDialog> CURRENCY_COST_OPTIONS = new Holder<>();
    public static final Holder<RewardCreationDialog>      REWARD_CREATION       = new Holder<>();
    public static final Holder<RewardSortingDialog>       REWARD_SORTING        = new Holder<>();
    public static final Holder<RewardPreviewDialog>       REWARD_PREVIEW        = new Holder<>();
    public static final Holder<RewardItemDialog>          REWARD_ITEM           = new Holder<>();
    public static final Holder<RewardCommandsDialog>      REWARD_COMMANDS       = new Holder<>();
    public static final Holder<RewardNameDialog>          REWARD_NAME           = new Holder<>();
    public static final Holder<RewardDescriptionDialog>   REWARD_DESCRIPTION    = new Holder<>();
    public static final Holder<RewardWeightDialog>        REWARD_WEIGHT         = new Holder<>();
    public static final Holder<RewardPermissionsDialog>   REWARD_PERMISSIONS    = new Holder<>();
    public static final Holder<RewardLimitsDialog>        REWARD_LIMITS         = new Holder<>();
    public static final Holder<KeyCreationDialog>         KEY_CREATION          = new Holder<>();
    public static final Holder<KeyNameDialog>             KEY_NAME              = new Holder<>();
    public static final Holder<KeyItemDialog>             KEY_ITEM              = new Holder<>();

    public CrateDialogs(@NotNull CratesPlugin plugin) {
        super(plugin);
    }

    @Override
    protected void onLoad() {
        this.register(CRATE_CREATION, CrateCreationDialog::new);
        this.register(CRATE_NAME, CrateNameDialog::new);
        this.register(CRATE_DESCRIPTION, CrateDescriptionDialog::new);
        this.register(CRATE_ITEM, CrateItemDialog::new);
        this.register(CRATE_PREVIEW, () -> new CratePreviewDialog(this.plugin));
        this.register(CRATE_OPENING, () -> new CrateOpeningDialog(this.plugin));
        this.register(CRATE_COOLDOWN, CrateCooldownDialog::new);
        this.register(CRATE_EFFECT, CrateEffectDialog::new);
        this.register(CRATE_PARTICLE, CrateParticleDialog::new);
        this.register(CRATE_HOLOGRAM, CrateHologramDialog::new);
        this.register(COST_CREATION, CostCreationDialog::new);
        this.register(COST_NAME, CostNameDialog::new);
        this.register(COST_ENTRY_CREATION, CostEntryCreationDialog::new);
        this.register(KEY_COST_OPTIONS, () -> new KeyCostOptionsDialog(this.plugin));
        this.register(CURRENCY_COST_OPTIONS, CurrencyCostOptionsDialog::new);
        this.register(REWARD_CREATION, () -> new RewardCreationDialog(this.plugin));
        this.register(REWARD_SORTING, RewardSortingDialog::new);
        this.register(REWARD_PREVIEW, RewardPreviewDialog::new);
        this.register(REWARD_ITEM, RewardItemDialog::new);
        this.register(REWARD_COMMANDS, RewardCommandsDialog::new);
        this.register(REWARD_NAME, RewardNameDialog::new);
        this.register(REWARD_DESCRIPTION, RewardDescriptionDialog::new);
        this.register(REWARD_WEIGHT, () -> new RewardWeightDialog(this.plugin));
        this.register(REWARD_PERMISSIONS, RewardPermissionsDialog::new);
        this.register(REWARD_LIMITS, RewardLimitsDialog::new);
        this.register(KEY_CREATION, KeyCreationDialog::new);
        this.register(KEY_NAME, KeyNameDialog::new);
        this.register(KEY_ITEM, KeyItemDialog::new);
    }

    @Override
    protected void onShutdown() {
        HOLDERS.forEach(Holder::clear);
        HOLDERS.clear();
    }

    public <T extends CrateDialog<?>> void register(@NotNull Holder<T> holder, @NotNull Supplier<T> supplier) {
        T dialog = supplier.get();
        this.plugin.injectLang(dialog);
        holder.set(dialog);
        HOLDERS.add(holder);
    }

    public static class Holder<T> {

        private T value;

        public void set(@NotNull T value) {
            if (this.value != null) throw new IllegalStateException("Holder value already set: " + value);

            this.value = value;
        }

        @NotNull
        public T get() {
            if (this.value == null) throw new IllegalStateException("Holder value is not set");

            return this.value;
        }

        public void clear() {
            this.value = null;
        }

        public void ifPresent(@NotNull Consumer<T> consumer) {
            if (this.value != null) {
                consumer.accept(this.value);
            }
        }

        public boolean isPresent() {
            return this.value != null;
        }

        public boolean isEmpty() {
            return this.value == null;
        }
    }
}
