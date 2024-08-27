package su.nightexpress.excellentcrates.currency;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.api.currency.Currency;
import su.nightexpress.excellentcrates.api.currency.CurrencyHandler;
import su.nightexpress.excellentcrates.currency.handler.PlayerLevelsHandler;
import su.nightexpress.excellentcrates.currency.handler.PlayerXPHandler;
import su.nightexpress.excellentcrates.currency.handler.VaultEconomyHandler;
import su.nightexpress.excellentcrates.currency.impl.CoinsEngineCurrency;
import su.nightexpress.excellentcrates.currency.impl.ConfigCurrency;
import su.nightexpress.excellentcrates.hooks.HookId;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.integration.VaultHook;
import su.nightexpress.nightcore.manager.SimpleManager;
import su.nightexpress.nightcore.util.Plugins;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class CurrencyManager extends SimpleManager<CratesPlugin> {

    private final Map<String, Currency> currencyMap;

    public CurrencyManager(@NotNull CratesPlugin plugin) {
        super(plugin);
        this.currencyMap = new HashMap<>();
    }

    @Override
    protected void onLoad() {
        this.loadCurrency(PlayerXPHandler.ID, PlayerXPHandler::new);
        this.loadCurrency(PlayerLevelsHandler.ID, PlayerLevelsHandler::new);

        if (Plugins.hasVault() && VaultHook.hasEconomy()) {
            this.loadCurrency(VaultEconomyHandler.ID, VaultEconomyHandler::new);
        }
        if (Plugins.isInstalled(HookId.COINS_ENGINE)) {
            CoinsEngineCurrency.getCurrencies().forEach(this::registerCurrency);
        }
    }

    @Override
    protected void onShutdown() {
        this.currencyMap.clear();
    }

    public void loadCurrency(@NotNull String id, @NotNull Supplier<CurrencyHandler> supplier) {
        FileConfig config = this.plugin.getConfig();

        ConfigCurrency currency = ConfigCurrency.read(config, "Currency." + id, id, supplier.get());
        if (!currency.isEnabled()) return;

        this.registerCurrency(currency);
    }

    public void registerCurrency(@NotNull Currency currency) {
        this.currencyMap.put(currency.getId(), currency);
        this.plugin.info("Registered currency: " + currency.getId());
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
