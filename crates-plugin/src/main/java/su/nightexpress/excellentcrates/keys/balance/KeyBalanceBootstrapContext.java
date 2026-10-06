package su.nightexpress.excellentcrates.keys.balance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bootstrap.context.NamedBootstrapContext;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.key.balance.KeyBalanceAPI;
import su.nightexpress.excellentcrates.api.key.command.KeyCommand;
import su.nightexpress.excellentcrates.api.key.dispatcher.KeyMessageDispatcher;
import su.nightexpress.excellentcrates.api.key.registry.KeyRegistry;
import su.nightexpress.excellentcrates.keys.balance.api.DefaultKeyBalanceAPI;
import su.nightexpress.excellentcrates.keys.balance.command.KeyBalanceCommand;
import su.nightexpress.excellentcrates.keys.balance.command.KeyGiveAllCommand;
import su.nightexpress.excellentcrates.keys.balance.command.KeyGiveCommand;
import su.nightexpress.excellentcrates.keys.balance.command.KeyRedeemCommand;
import su.nightexpress.excellentcrates.keys.balance.command.KeyRemoveCommand;
import su.nightexpress.excellentcrates.keys.balance.controller.KeyAutoRedeemController;
import su.nightexpress.excellentcrates.keys.balance.placeholder.KeyBalancePlaceholder;
import su.nightexpress.excellentcrates.keys.item.KeyItemService;
import su.nightexpress.excellentcrates.keys.storage.db.KeyStorageCachedDataService;
import su.nightexpress.nightcore.userdata.UserDataManager;

@NullMarked
public class KeyBalanceBootstrapContext extends NamedBootstrapContext {

    private static final Identifier ID   = new Identifier("keys.balance");
    private static final String     NAME = "Balance";

    public final KeyBalanceService balanceService;
    public final KeyBalanceAPI     api;

    private final KeyBalancePlaceholder placeholder;
    private final List<KeyCommand>      commands;

    public KeyBalanceBootstrapContext(CratesPlugin plugin,
                                      KeyMessageDispatcher dispatcher,
                                      UserDataManager userDataService,
                                      KeyRegistry keyRegistry,
                                      KeyStorageCachedDataService storageService,
                                      KeyItemService itemService) {
        super(ID, NAME);
        this.commands = new ArrayList<>();

        this.balanceService = new KeyBalanceService(storageService, itemService);

        this.placeholder = new KeyBalancePlaceholder(this.balanceService);

        this.commands.add(new KeyGiveCommand(this.balanceService, userDataService, dispatcher));
        this.commands.add(new KeyRemoveCommand(this.balanceService, userDataService, dispatcher));
        this.commands.add(new KeyGiveAllCommand(this.balanceService, dispatcher));
        this.commands.add(new KeyBalanceCommand(keyRegistry, balanceService, userDataService, dispatcher));
        this.commands.add(new KeyRedeemCommand(keyRegistry, balanceService, dispatcher));

        this.api = new DefaultKeyBalanceAPI(this.balanceService);

        this.addComponent(new KeyAutoRedeemController(plugin, keyRegistry, balanceService, dispatcher));
    }

    public KeyBalancePlaceholder getPlaceholder() {
        return this.placeholder;
    }

    public List<KeyCommand> getCommands() {
        return Collections.unmodifiableList(this.commands);
    }
}
