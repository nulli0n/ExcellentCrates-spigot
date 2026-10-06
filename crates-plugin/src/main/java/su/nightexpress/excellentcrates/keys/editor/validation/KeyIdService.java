package su.nightexpress.excellentcrates.keys.editor.validation;

import java.util.Optional;
import java.util.UUID;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.id.IdentifierParser;
import su.nightexpress.excellentcrates.api.key.registry.KeyRegistry;
import su.nightexpress.excellentcrates.util.ItemHelper;
import su.nightexpress.nightcore.bridge.item.AdaptedItem;
import su.nightexpress.nightcore.integration.item.data.ItemIdData;
import su.nightexpress.nightcore.integration.item.impl.AdaptedItemStack;
import su.nightexpress.nightcore.util.BukkitThing;
import su.nightexpress.nightcore.util.ItemUtil;
import su.nightexpress.nightcore.util.text.night.NightMessage;

@NullMarked
public class KeyIdService {

    private final KeyRegistry registry;

    public KeyIdService(KeyRegistry registry) {
        this.registry = registry;
    }

    public Optional<Identifier> createUniqueKeyId(ItemStack itemStack) {
        Optional<Identifier> result = this.createKeyId(itemStack);
        if (result.isEmpty()) return result;

        Identifier id = result.get();
        if (this.registry.contains(id)) {
            return IdentifierParser.parse(id.value() + "_" + UUID.randomUUID().toString().substring(0, 8));
        }

        return result;
    }

    public Optional<Identifier> createKeyId(ItemStack itemStack) {
        if (ItemHelper.isCustom(itemStack) && !ItemHelper.isMixedItem(itemStack)) {
            AdaptedItem adaptedItem = ItemHelper.adapt(itemStack);
            if (adaptedItem instanceof AdaptedItemStack stack && stack.getData() instanceof ItemIdData idData) {
                return IdentifierParser.parseSanitized(idData.getItemId());
            }
        }

        ItemMeta meta = itemStack.getItemMeta();
        String metaName = meta == null ? null : ItemUtil.getNameSerialized(meta);
        String cleanName = metaName == null ? null : NightMessage.stripTags(metaName);

        if (cleanName == null || cleanName.isBlank()) {
            cleanName = BukkitThing.getValue(itemStack.getType());
        }

        return IdentifierParser.parseSanitized(cleanName);
    }
}
