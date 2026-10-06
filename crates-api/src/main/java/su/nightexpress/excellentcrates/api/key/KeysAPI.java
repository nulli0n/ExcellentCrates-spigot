package su.nightexpress.excellentcrates.api.key;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.service.PluginAPI;
import su.nightexpress.excellentcrates.api.key.balance.KeyBalanceAPI;
import su.nightexpress.excellentcrates.api.key.item.KeyItemAPI;

@NullMarked
public interface KeysAPI extends PluginAPI {

    KeyBalanceAPI getBalanceAPI();

    KeyItemAPI getItemAPI();
}
