package su.nightexpress.excellentcrates.reward.preview;

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
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.data.model.RewardPreview;
import su.nightexpress.nightcore.bridge.item.AdaptedItem;
import su.nightexpress.nightcore.util.ItemUtil;
import su.nightexpress.nightcore.util.LangUtil;

public class RewardPreviewResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(RewardPreviewResolver.class);

    private final Cache<Identifier, NameAndLore> cache;

    public RewardPreviewResolver(long expireDuration, TimeUnit unit) {
        this.cache = CacheBuilder.newBuilder()
            .expireAfterWrite(expireDuration, unit)
            .build();
    }

    public void invalidateCache(Identifier id) {
        this.cache.invalidate(id);
    }

    /**
     * Возвращает отображаемую информацию. Запрашивает данные из кэша,
     * а при их отсутствии (или истечении времени) — вычисляет лениво.
     */
    public NameAndLore getDisplayInfo(Reward reward) {
        RewardPreview preview = reward.getPreview();

        if (!preview.isUseIconData()) {
            return new NameAndLore(preview.getName(), preview.getLore());
        }

        try {
            // Метод get() атомарно вычисляет значение, если его нет в кэше
            return cache.get(reward.id(), () -> computeFromIcon(reward));
        }
        catch (ExecutionException e) {
            LOGGER.error("Failed to resolve preview for reward {}", reward.idString(), e);
            // Fallback на данные из конфига при ошибке генерации
            return new NameAndLore(preview.getName(), preview.getLore());
        }
    }

    private NameAndLore computeFromIcon(Reward reward) {
        RewardPreview preview = reward.getPreview();
        AdaptedItem icon = preview.getIcon();
        ItemStack itemStack = icon.getItemStack();

        if (itemStack == null) {
            LOGGER.warn("Failed to update preview for reward {}: item data is invalid.", reward.idString());
            return new NameAndLore(preview.getName(), preview.getLore());
        }

        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            return new NameAndLore(preview.getName(), preview.getLore());
        }

        String name = ItemUtil.getNameSerialized(meta);
        if (name == null || name.isEmpty()) {
            name = LangUtil.getSerializedName(itemStack.getType());
        }

        List<String> lore = ItemUtil.getLoreSerialized(meta);

        return new NameAndLore(name, lore);
    }
}