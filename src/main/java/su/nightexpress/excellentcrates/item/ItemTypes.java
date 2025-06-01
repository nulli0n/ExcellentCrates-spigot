package su.nightexpress.excellentcrates.item;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesAPI;
import su.nightexpress.excellentcrates.api.item.ItemProvider;
import su.nightexpress.excellentcrates.api.item.ItemType;
import su.nightexpress.excellentcrates.config.Keys;
import su.nightexpress.excellentcrates.hooks.HookId;
import su.nightexpress.excellentcrates.item.provider.impl.CustomItemProvider;
import su.nightexpress.excellentcrates.item.provider.impl.DummyItemProvider;
import su.nightexpress.excellentcrates.item.provider.impl.VanillaItemProvider;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.util.PDCUtil;
import su.nightexpress.nightcore.util.Plugins;

public class ItemTypes {

    public static final DummyItemProvider DUMMY = new DummyItemProvider();

    public static boolean isDummy(@NotNull ItemStack itemStack) {
        return PDCUtil.getBoolean(itemStack, Keys.dummyItem).isPresent();
    }

    public static boolean isCustom(@NotNull ItemStack itemStack) {
        ItemProvider provider = fromItem(itemStack);
        return !provider.isDummy() && !(provider instanceof VanillaItemProvider);
    }

    @NotNull
    public static ItemProvider read(@NotNull FileConfig config, @NotNull String path) {
        ItemType type = config.getEnum(path + ".Type", ItemType.class, ItemType.VANILLA);

        if (type == ItemType.CUSTOM && !Plugins.hasEconomyBridge()) {
            CratesAPI.error("Could not load custom item due to missing " + HookId.ECONOMY_BRIDGE + " dependency. Caused by '" + config.getFile().getName() + "' -> '" + path + "'.");
            return DUMMY;
        }

        ItemProvider provider = switch (type) {
            case VANILLA -> VanillaItemProvider.read(config, path);
            case CUSTOM -> CustomItemProvider.read(config, path);
        };

        return provider == null ? DUMMY : provider;
    }

    @NotNull
    public static ItemProvider vanilla(@NotNull ItemStack itemStack) {
        return VanillaItemProvider.fromItem(itemStack);
    }

    @NotNull
    public static ItemProvider fromItem(@NotNull ItemStack itemStack) {
        ItemProvider provider = null;

        if (Plugins.hasEconomyBridge()) {
            provider = CustomItemProvider.fromItem(itemStack);
        }
        if (provider == null) {
            provider = vanilla(itemStack);
        }
        return provider;
    }
}
