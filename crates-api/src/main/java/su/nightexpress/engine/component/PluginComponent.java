package su.nightexpress.engine.component;

import org.jspecify.annotations.NullMarked;

@NullMarked
public interface PluginComponent {

    void start();

    void reload();

    void shutdown();

    boolean isRunning();
}