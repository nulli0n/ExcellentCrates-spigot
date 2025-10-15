package su.nightexpress.excellentcrates.config;

import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;

public class Keys {

    public static NamespacedKey crateId;
    public static NamespacedKey keyId;
    public static NamespacedKey linkToolCrateId;

    public static void load(@NotNull CratesPlugin plugin) {
        crateId = new NamespacedKey(plugin, "crate.id");
        keyId = new NamespacedKey(plugin, "crate_key.id");
        linkToolCrateId = new NamespacedKey(plugin, "linktool.crate_id");
    }

    public static void clear() {
        crateId = null;
        keyId = null;
        linkToolCrateId = null;
    }
}
