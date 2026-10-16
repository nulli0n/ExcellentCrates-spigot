package su.nightexpress.engine.component;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.userdata.UserDataManager;

@NullMarked
public interface EngineDependencies {

    NightCorePlugin plugin();

    CoreUIService uiService();

    UserDataManager userService();

    DatabaseClient databaseClient();
}
