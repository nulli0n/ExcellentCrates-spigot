package su.nightexpress.excellentcrates.crate.cost.entry.impl;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.crate.cost.entry.AbstractCostEntry;
import su.nightexpress.excellentcrates.crate.cost.type.impl.EcoCostType;
import su.nightexpress.excellentcrates.dialog.DialogKey;
import su.nightexpress.excellentcrates.dialog.DialogRegistry;
import su.nightexpress.excellentcrates.dialog.cost.CurrencyCostOptionsDialog;
import su.nightexpress.nightcore.bridge.currency.Currency;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.integration.currency.EconomyBridge;
import su.nightexpress.nightcore.util.bukkit.NightItem;

import java.util.Optional;

public class EcoCostEntry extends AbstractCostEntry<EcoCostType> {

    private static final DialogKey<EcoCostEntry> DIALOG_KEY = new DialogKey<>("eco_cost_options");

    private final DialogRegistry dialogs;

    private String currencyId;
    private double amount;

    public EcoCostEntry(@NotNull EcoCostType type, @NotNull DialogRegistry dialogs, @NotNull String currencyId, double amount) {
        super(type);
        this.dialogs = dialogs;

        this.setCurrencyId(currencyId);
        this.setAmount(amount);

        this.dialogs.register(DIALOG_KEY, new CurrencyCostOptionsDialog());
    }

    @Override
    protected void writeAdditional(@NotNull FileConfig config, @NotNull String path) {
        config.set(path + ".Currency", this.currencyId);
        config.set(path + ".Amount", this.amount);
    }

    @Override
    public void openEditor(@NotNull Player player, @Nullable Runnable callback) {
        this.dialogs.show(player, DIALOG_KEY, this, callback);
    }

    @NotNull
    public Optional<Currency> currency() {
        return EconomyBridge.currency(this.currencyId);
    }

    @Override
    @NotNull
    public NightItem getEditorIcon() {
        Optional<Currency> ecoOpt = this.currency();

        return ecoOpt.map(currency -> NightItem.fromItemStack(currency.getIcon())).orElse(NightItem.fromType(Material.BARRIER))
            .localized(EcoCostType.LOCALE_EDIT_BUTTON)
            .replacement(replacer -> replacer
                .replace(Placeholders.GENERIC_ID, () -> ecoOpt.map(currency -> CoreLang.goodEntry(currency.getInternalId())).orElse(CoreLang.badEntry(this.currencyId)))
                .replace(Placeholders.GENERIC_AMOUNT, () -> this.amount > 0 ? CoreLang.goodEntry(String.valueOf(this.amount)) : CoreLang.badEntry(String.valueOf(this.amount)))
                .replace(Placeholders.GENERIC_NAME, () -> ecoOpt.map(Currency::getName).orElse(this.currencyId))
            )
            .hideAllComponents();
    }

    @Override
    @NotNull
    public String format() {
        return this.currency().map(currency -> currency.format(this.amount)).orElse(this.amount + " " + this.currencyId);
    }

    @Override
    public boolean isValid() {
        return this.amount > 0 && this.currency().isPresent();
    }

    @Override
    public int countPossibleOpenings(@NotNull Player player) {
        return (int) Math.floor(EconomyBridge.getBalance(player, this.currencyId) / this.amount);
    }

    @Override
    public boolean hasEnough(@NotNull Player player) {
        return EconomyBridge.hasEnough(player, this.currencyId, this.amount);
    }

    @Override
    public void take(@NotNull Player player) {
        EconomyBridge.withdraw(player, this.currencyId, this.amount);
    }

    @Override
    public void refund(@NotNull Player player) {
        EconomyBridge.deposit(player, this.currencyId, this.amount);
    }

    @NotNull
    public String getCurrencyId() {
        return this.currencyId;
    }

    public void setCurrencyId(@NotNull String currencyId) {
        this.currencyId = currencyId;
    }

    public double getAmount() {
        return this.amount;
    }

    public void setAmount(double amount) {
        this.amount = Math.max(0, amount);
    }

    @Override
    public String toString() {
        return "[" + "currencyId='" + currencyId + '\'' + ", amount=" + amount + ']';
    }
}
