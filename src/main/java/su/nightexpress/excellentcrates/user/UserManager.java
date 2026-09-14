package su.nightexpress.excellentcrates.user;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.data.DataHandler;
import su.nightexpress.nightcore.db.AbstractUserManager;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class UserManager extends AbstractUserManager<CratesPlugin, CrateUser> {

    private final Map<String, CompletableFuture<Void>> taskMap;

    public UserManager(@NotNull CratesPlugin plugin, @NotNull DataHandler dataHandler) {
        super(plugin, dataHandler);
        this.taskMap = new ConcurrentHashMap<>();
    }

    @Override
    public void loadOnline() {
        this.plugin.runTaskAsync(super::loadOnline);
    }

    @Override
    @NotNull
    public CrateUser create(@NotNull UUID uuid, @NotNull String name) {
        return new CrateUser(uuid, name);
    }

    @Override
    public void manageUser(@NotNull String name, @NotNull Consumer<CrateUser> consumer) {
        CrateUser user = this.getLoaded(name);
        if (user != null) {
            consumer.accept(user);
            return;
        }

        this.schedule(name.toLowerCase(), () -> consumer.accept(this.getOrFetch(name)));
    }

    @Override
    public void manageUser(@NotNull UUID playerId, @NotNull Consumer<CrateUser> consumer) {
        CrateUser user = this.getLoaded(playerId);
        if (user != null) {
            consumer.accept(user);
            return;
        }

        this.schedule(playerId.toString(), () -> consumer.accept(this.getOrFetch(playerId)));
    }

    private void schedule(@NotNull String key, @NotNull Runnable task) {
        this.taskMap.compute(key, (id, previous) -> {
            CompletableFuture<Void> next = previous == null
                ? CompletableFuture.runAsync(task)
                : previous.handle((result, error) -> {
                    task.run();
                    return null;
                });

            next.whenComplete((result, error) -> this.taskMap.remove(id, next));
            return next;
        });
    }
}
