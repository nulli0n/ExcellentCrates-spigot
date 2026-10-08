package su.nightexpress.excellentcrates.api.key;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.service.PluginAPI;
import su.nightexpress.excellentcrates.api.key.balance.KeyBalanceAPI;
import su.nightexpress.excellentcrates.api.key.item.KeyItemAPI;
import su.nightexpress.excellentcrates.api.key.registry.KeyRegistry;

@NullMarked
public interface KeysAPI extends PluginAPI {

    KeyRegistry getRegistry();

    KeyBalanceAPI getBalanceAPI();

    KeyItemAPI getItemAPI();
}
