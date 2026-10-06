package su.nightexpress.engine.component;

import org.jspecify.annotations.NullMarked;

@NullMarked
public interface StartupComponent extends PluginComponent {

    @Override
    default void reload() {
        // Do nothing on reload
    }

    @Override
    default void shutdown() {
        // Do nothing on shutdown
    }

    @Override
    default boolean isRunning() {
        // It's a fire-and-forget task, so we can just consider it 
        // "running" (completed) after start is called.
        return true;
    }
}
