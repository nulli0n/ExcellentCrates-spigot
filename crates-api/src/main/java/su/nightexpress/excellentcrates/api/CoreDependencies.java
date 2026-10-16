package su.nightexpress.excellentcrates.api;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.EngineDependencies;
import su.nightexpress.excellentcrates.api.crate.dispatcher.CrateMessageDispatcher;
import su.nightexpress.excellentcrates.api.crate.placeholder.CratePlaceholders;
import su.nightexpress.excellentcrates.api.crate.registry.CrateRegistry;

@NullMarked
public interface CoreDependencies extends EngineDependencies {

    CratesPlugin plugin();

    CrateRegistry crateRegistry();

    CratePlaceholders cratePlaceholders();

    CrateMessageDispatcher dispatcher();
}
