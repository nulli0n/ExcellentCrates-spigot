package su.nightexpress.engine.component;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.util.bukkit.NightTask;

@NullMarked
public abstract class BaseController extends BasePluginComponent implements Listener {

    protected final NightCorePlugin plugin;
    protected final List<NightTask> tasks;

    public BaseController(NightCorePlugin plugin) {
        super();
        this.plugin = plugin;
        this.tasks = new ArrayList<>();
    }

    protected abstract void onControllerStart();

    protected abstract void onControllerShutdown();

    protected abstract void onControllerReload();

    @Override
    protected final void onStart() {
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);

        this.onControllerStart();
    }

    @Override
    protected final void onShutdown() {
        // Automatically kill all tasks started by this controller
        this.stopTasks();

        // Automatically unregister all Bukkit events inside this class
        HandlerList.unregisterAll(this);

        // Fire an optional cleanup method for subclasses
        this.onControllerShutdown();
    }

    @Override
    protected final void onReload() {
        this.onControllerReload();
    }

    public void stopTasks() {
        this.tasks.forEach(NightTask::stop);
        this.tasks.clear();
    }

    protected void addSecondsTask(Runnable runnable, int interval) {
        this.addTask(NightTask.create(plugin, runnable, interval));
    }

    protected void addTickTask(Runnable runnable, long interval) {
        this.addTask(NightTask.create(plugin, runnable, interval));
    }

    protected void addAsyncSecondsTask(Runnable runnable, int interval) {
        this.addTask(NightTask.createAsync(plugin, runnable, interval));
    }

    protected void addAsyncTickTask(Runnable runnable, long interval) {
        this.addTask(NightTask.createAsync(plugin, runnable, interval));
    }

    protected void addTask(NightTask task) {
        if (task.isValid()) {
            this.tasks.add(task);
        }
    }
}