package su.nightexpress.engine.component;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.dispatcher.CrateMessageDispatcher;
import su.nightexpress.excellentcrates.api.crate.placeholder.CratePlaceholders;
import su.nightexpress.excellentcrates.api.crate.registry.CrateRegistry;
import su.nightexpress.nightcore.userdata.UserDataManager;

@NullMarked
public interface CoreDependencies {

    CratesPlugin plugin();

    CoreUIService uiService();

    UserDataManager userService();

    DatabaseClient databaseClient();

    // Core Domain

    CrateRegistry crateRegistry();

    CratePlaceholders cratePlaceholders();

    CrateMessageDispatcher dispatcher();
}
