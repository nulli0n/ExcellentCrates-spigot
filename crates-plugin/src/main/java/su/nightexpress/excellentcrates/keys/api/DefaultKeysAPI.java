package su.nightexpress.excellentcrates.keys.api;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.key.KeysAPI;
import su.nightexpress.excellentcrates.api.key.balance.KeyBalanceAPI;
import su.nightexpress.excellentcrates.api.key.item.KeyItemAPI;
import su.nightexpress.excellentcrates.api.key.registry.KeyRegistry;

@NullMarked
public class DefaultKeysAPI implements KeysAPI {

    private final KeyRegistry   registry;
    private final KeyBalanceAPI balanceApi;
    private final KeyItemAPI    itemApi;

    public DefaultKeysAPI(KeyRegistry registry, KeyBalanceAPI balance, KeyItemAPI itemApi) {
        this.registry = registry;
        this.balanceApi = balance;
        this.itemApi = itemApi;
    }

    @Override
    public KeyRegistry getRegistry() {
        return this.registry;
    }

    @Override
    public KeyBalanceAPI getBalanceAPI() {
        return this.balanceApi;
    }

    @Override
    public KeyItemAPI getItemAPI() {
        return this.itemApi;
    }
}
