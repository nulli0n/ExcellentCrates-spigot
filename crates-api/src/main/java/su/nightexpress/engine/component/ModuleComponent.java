package su.nightexpress.engine.component;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;

@NullMarked
public abstract class ModuleComponent extends NamedComponentBundle {

    public ModuleComponent(Identifier id, String name) {
        super(id, name);
    }

    @Override
    public final void start() {
        if (this.isRunning()) return;

        // Execute hook before children components start
        this.onStart();

        super.start();
    }

    @Override
    public final void shutdown() {
        if (!this.isRunning()) return;

        // Shuts down children first
        super.shutdown();

        this.onShutdown();
    }

    @Override
    public final void reload() {
        // Hook executes first
        this.onReload();

        // Children reload next
        super.reload();
    }

    protected abstract void onStart();

    protected abstract void onShutdown();

    protected abstract void onReload();
}