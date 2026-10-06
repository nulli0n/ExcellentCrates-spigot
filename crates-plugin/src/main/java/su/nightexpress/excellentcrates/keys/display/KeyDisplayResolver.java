package su.nightexpress.excellentcrates.keys.display;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.common.preview.NameAndLore;
import su.nightexpress.excellentcrates.api.key.CrateKey;
import su.nightexpress.excellentcrates.api.key.data.model.KeyDisplay;
import su.nightexpress.nightcore.bridge.item.AdaptedItem;
import su.nightexpress.nightcore.util.ItemUtil;
import su.nightexpress.nightcore.util.LangUtil;

@Deprecated
public class KeyDisplayResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(KeyDisplayResolver.class);

    private final Cache<Identifier, NameAndLore> cache;

    public KeyDisplayResolver(long expireDuration, TimeUnit unit) {
        this.cache = CacheBuilder.newBuilder()
            .expireAfterWrite(expireDuration, unit)
            .build();
    }

    public void invalidateCache(Identifier id) {
        this.cache.invalidate(id);
    }

    public NameAndLore getDisplayInfo(CrateKey key) {
        KeyDisplay display = key.getDisplay();

        if (key.getItem().isInheritDisplaySettings()) {
            return new NameAndLore(display.getName(), display.getLore());
        }

        try {
            return cache.get(key.id(), () -> computeFromIcon(key));
        }
        catch (ExecutionException e) {
            LOGGER.error("Failed to resolve display for key {}", key.idString(), e);
            return new NameAndLore(display.getName(), display.getLore());
        }
    }

    private NameAndLore computeFromIcon(CrateKey key) {
        KeyDisplay display = key.getDisplay();
        AdaptedItem item = key.getItem().getItem();
        ItemStack itemStack = item.getItemStack();

        if (itemStack == null) {
            LOGGER.warn("Failed to update preview for key {}: item data is invalid.", key.idString());
            return new NameAndLore(display.getName(), display.getLore());
        }

        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            return new NameAndLore(display.getName(), display.getLore());
        }

        String name = ItemUtil.getNameSerialized(meta);
        if (name == null || name.isEmpty()) {
            name = LangUtil.getSerializedName(itemStack.getType());
        }

        List<String> lore = ItemUtil.getLoreSerialized(meta);

        return new NameAndLore(name, lore);
    }
}