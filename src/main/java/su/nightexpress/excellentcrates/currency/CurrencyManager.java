package su.nightexpress.excellentcrates.currency;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nexmedia.engine.api.manager.AbstractManager;
import su.nexmedia.engine.integration.VaultHook;
import su.nexmedia.engine.utils.EngineUtils;
import su.nightexpress.excellentcrates.ExcellentCratesPlugin;
import su.nightexpress.excellentcrates.api.currency.Currency;
import su.nightexpress.excellentcrates.api.currency.CurrencyHandler;
import su.nightexpress.excellentcrates.currency.handler.VaultEconomyHandler;
import su.nightexpress.excellentcrates.currency.handler.XPCurrencyHandler;
import su.nightexpress.excellentcrates.currency.impl.CoinsEngineCurrency;
import su.nightexpress.excellentcrates.currency.impl.ConfigCurrency;
import su.nightexpress.excellentcrates.hooks.HookId;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class CurrencyManager extends AbstractManager<ExcellentCratesPlugin> {

    public static final String XP    = "xp";
    public static final String MONEY = "money";
    public static final String COINS = "coins";

    private final Map<String, Currency> currencyMap;

    public CurrencyManager(@NotNull ExcellentCratesPlugin plugin) {
        super(plugin);
        this.currencyMap = new HashMap<>();
    }

    @Override
    protected void onLoad() {
        this.registerCurrency(XP, XPCurrencyHandler::new);

        if (EngineUtils.hasVault() && VaultHook.hasEconomy()) {
            this.registerCurrency(MONEY, VaultEconomyHandler::new);
        }
        if (EngineUtils.hasPlugin(HookId.COINS_ENGINE)) {
            CoinsEngineCurrency.getCurrencies().forEach(this::registerCurrency);
        }
    }

    @Override
    protected void onShutdown() {
        this.currencyMap.clear();
    }

    @Nullable
    public Currency registerCurrency(@NotNull String id, @NotNull Supplier<CurrencyHandler> supplier) {
        ConfigCurrency currency = new ConfigCurrency(this.plugin, id, supplier.get());
        if (!currency.load()) return null;

        return this.registerCurrency(currency);
    }

    @NotNull
    public Currency registerCurrency(@NotNull Currency currency) {
        this.currencyMap.put(currency.getId(), currency);
        this.plugin.info("Registered currency: " + currency.getId());
        return currency;
    }

    public boolean hasCurrency() {
        return !this.currencyMap.isEmpty();
    }

    @NotNull
    public Collection<Currency> getCurrencies() {
        return currencyMap.values();
    }

    @NotNull
    public Set<String> getCurrencyIds() {
        return this.currencyMap.keySet();
    }

    @Nullable
    public Currency getCurrency(@NotNull String id) {
        return this.currencyMap.get(id.toLowerCase());
    }
}
