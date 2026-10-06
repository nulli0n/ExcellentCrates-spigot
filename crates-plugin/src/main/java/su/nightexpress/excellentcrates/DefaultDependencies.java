package su.nightexpress.excellentcrates;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.CoreDependencies;
import su.nightexpress.engine.component.DatabaseClient;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.dispatcher.CrateMessageDispatcher;
import su.nightexpress.excellentcrates.api.crate.placeholder.CratePlaceholders;
import su.nightexpress.excellentcrates.api.crate.registry.CrateRegistry;
import su.nightexpress.nightcore.userdata.UserDataManager;

@NullMarked
public record DefaultDependencies(CratesPlugin plugin,
                                  CoreUIService uiService,
                                  UserDataManager userService,
                                  DatabaseClient databaseClient,
                                  CrateRegistry crateRegistry,
                                  CratePlaceholders cratePlaceholders,
                                  CrateMessageDispatcher dispatcher) implements CoreDependencies {

}
