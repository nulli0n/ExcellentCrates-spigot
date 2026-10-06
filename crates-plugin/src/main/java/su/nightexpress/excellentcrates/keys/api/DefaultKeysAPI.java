package su.nightexpress.excellentcrates.keys.api;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.key.KeysAPI;
import su.nightexpress.excellentcrates.api.key.balance.KeyBalanceAPI;
import su.nightexpress.excellentcrates.api.key.item.KeyItemAPI;

@NullMarked
public class DefaultKeysAPI implements KeysAPI {

    private final KeyBalanceAPI balanceApi;
    private final KeyItemAPI    itemApi;

    public DefaultKeysAPI(KeyBalanceAPI balance, KeyItemAPI itemApi) {
        this.balanceApi = balance;
        this.itemApi = itemApi;
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
