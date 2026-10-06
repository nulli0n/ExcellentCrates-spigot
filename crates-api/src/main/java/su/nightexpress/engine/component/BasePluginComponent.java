package su.nightexpress.engine.component;

import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class BasePluginComponent implements PluginComponent {

    private boolean isRunning;

    public BasePluginComponent() {
        this.isRunning = false;
    }

    @Override
    public final void start() {
        if (this.isRunning) return;

        this.onStart();
        this.isRunning = true;
    }

    @Override
    public final void shutdown() {
        if (!this.isRunning) return;

        this.onShutdown();
        this.isRunning = false;
    }

    @Override
    public final void reload() {
        this.onReload();
    }

    @Override
    public final boolean isRunning() {
        return this.isRunning;
    }

    protected abstract void onStart();

    protected abstract void onShutdown();

    protected abstract void onReload();
}