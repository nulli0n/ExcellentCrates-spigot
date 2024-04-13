package su.nightexpress.excellentcrates.currency.impl;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.coinsengine.api.CoinsEngineAPI;
import su.nightexpress.excellentcrates.api.currency.Currency;
import su.nightexpress.excellentcrates.api.currency.CurrencyHandler;
import su.nightexpress.nightcore.util.placeholder.PlaceholderMap;

import java.util.HashSet;
import java.util.Set;

public class CoinsEngineCurrency implements Currency, CurrencyHandler {

    private final su.nightexpress.coinsengine.api.currency.Currency currency;

    public CoinsEngineCurrency(@NotNull su.nightexpress.coinsengine.api.currency.Currency currency) {
        this.currency = currency;
    }

    @NotNull
    public static Set<CoinsEngineCurrency> getCurrencies() {
        Set<CoinsEngineCurrency> currencies = new HashSet<>();
        CoinsEngineAPI.getCurrencyManager().getCurrencies().forEach(cura -> {
            if (!cura.isVaultEconomy()) {
                currencies.add(new CoinsEngineCurrency(cura));
            }
        });
        return currencies;
    }

    @Override
    @NotNull
    public String formatValue(double price) {
        return this.currency.formatValue(price);
    }

    @Override
    @NotNull
    public CurrencyHandler getHandler() {
        return this;
    }

    @Override
    @NotNull
    public String getDefaultName() {
        return this.getName();
    }

    @Override
    @NotNull
    public String getDefaultFormat() {
        return this.currency.getFormat();
    }

    @Override
    @NotNull
    public String getId() {
        return "coinsengine_" + this.currency.getId();
    }

    @Override
    @NotNull
    public String getName() {
        return this.currency.getName();
    }

    @Override
    @NotNull
    public String getFormat() {
        return this.currency.getFormat();
    }

    @Override
    @NotNull
    public PlaceholderMap getPlaceholders() {
        return this.currency.getPlaceholders();
    }

    @Override
    public double getBalance(@NotNull Player player) {
        return CoinsEngineAPI.getBalance(player, this.currency);
    }

    @Override
    public void give(@NotNull Player player, double amount) {
        CoinsEngineAPI.addBalance(player, this.currency, amount);
    }

    @Override
    public void take(@NotNull Player player, double amount) {
        CoinsEngineAPI.removeBalance(player, this.currency, amount);
    }

    @Override
    public void set(@NotNull Player player, double amount) {
        CoinsEngineAPI.setBalance(player, this.currency, amount);
    }
}
