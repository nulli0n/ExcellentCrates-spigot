package su.nightexpress.excellentcrates.crate.impl;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.economybridge.EconomyBridge;
import su.nightexpress.economybridge.api.Currency;
import su.nightexpress.economybridge.currency.CurrencyId;

public class Cost {

    private final String currencyId;

    private double amount;

    public Cost(@NotNull String currencyId, double amount) {
        this.currencyId = CurrencyId.reroute(currencyId);
        this.setAmount(amount);
    }

    public boolean isValid() {
        return this.isVailidCurrency() && this.isValidAmount();
    }

    public boolean isVailidCurrency() {
        return EconomyBridge.hasCurrency(this.currencyId);
    }

    public boolean isValidAmount() {
        return this.amount > 0D;
    }

    @NotNull
    public String format() {
        Currency currency = EconomyBridge.getCurrency(this.currencyId);
        if (currency != null) return currency.format(this.amount);

        return this.currencyId + " " + this.amount;
    }

    public boolean deposit(@NotNull Player player) {
        return EconomyBridge.deposit(player, this.currencyId, this.amount);
    }

    public boolean withdraw(@NotNull Player player) {
        return EconomyBridge.withdraw(player, this.currencyId, this.amount);
    }

    public boolean hasEnough(@NotNull Player player) {
        return EconomyBridge.hasEnough(player, this.currencyId, this.amount);
    }

    @NotNull
    public String getCurrencyId() {
        return this.currencyId;
    }

    public double getAmount() {
        return this.amount;
    }

    public void setAmount(double amount) {
        this.amount = Math.max(0, amount);
    }
}
