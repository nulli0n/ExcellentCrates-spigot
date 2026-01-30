package su.nightexpress.excellentcrates.dialog;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.nightcore.bridge.registry.NightRegistry;

import java.util.function.Supplier;

public class DialogRegistry {

    private final CratesPlugin                           plugin;
    private final NightRegistry<DialogKey<?>, Dialog<?>> backendRegistry;

    public DialogRegistry(@NotNull CratesPlugin plugin) {
        this.plugin = plugin;
        this.backendRegistry = new NightRegistry<>();
    }

    public void clear() {
        this.backendRegistry.clear();
    }

    public <T> void register(@NotNull DialogKey<T> key, @NotNull Supplier<Dialog<T>> supplier) {
        this.register(key, supplier.get());
    }

    public <T> void register(@NotNull DialogKey<T> key, @NotNull Dialog<T> dialog) {
        this.backendRegistry.register(key, dialog);
        this.plugin.injectLang(dialog);
    }

    @SuppressWarnings("unchecked")
    public <T> boolean show(@NotNull Player player, @NotNull DialogKey<T> key, @NotNull T data, @Nullable Runnable callback) {
        Dialog<T> dialog = (Dialog<T>) this.backendRegistry.byKey(key);

        if (dialog == null) {
            this.plugin.warn("Dialog '%s' not found or disabled.".formatted(key.id()));
            return false;
        }

        dialog.show(player, data, callback);
        return true;
    }
}
