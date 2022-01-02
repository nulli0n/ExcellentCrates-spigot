package su.nightexpress.excellentcrates;

import org.bukkit.NamespacedKey;

public class Keys {

    private static final ExcellentCrates PLUGIN = ExcellentCrates.getPlugin(ExcellentCrates.class);

    public static final NamespacedKey CRATE_ID     = new NamespacedKey(PLUGIN, "crate.id");
    public static final NamespacedKey CRATE_KEY_ID = new NamespacedKey(PLUGIN, "crate_key.id");

}
