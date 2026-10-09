package su.nightexpress.excellentcrates.reward.id;

import java.util.Optional;
import java.util.UUID;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.id.IdentifierParser;
import su.nightexpress.excellentcrates.api.reward.registry.RewardId;
import su.nightexpress.excellentcrates.api.reward.registry.RewardRegistry;
import su.nightexpress.excellentcrates.core.SharedConstants;
import su.nightexpress.excellentcrates.util.ItemHelper;
import su.nightexpress.nightcore.bridge.item.AdaptedDataItem;
import su.nightexpress.nightcore.bridge.item.AdaptedItem;
import su.nightexpress.nightcore.integration.item.data.ItemIdData;
import su.nightexpress.nightcore.util.BukkitThing;
import su.nightexpress.nightcore.util.ItemUtil;
import su.nightexpress.nightcore.util.text.night.NightMessage;

@NullMarked
public class RewardIdService {

    private final RewardRegistry registry;

    public RewardIdService(RewardRegistry registry) {
        this.registry = registry;
    }

    public Optional<Identifier> createUniqueRewardId(Identifier crateId, ItemStack itemStack) {
        Optional<Identifier> result = this.createRewardId(itemStack);
        if (result.isEmpty()) return result;

        Identifier id = this.trimLength(result.get());
        RewardId rewardId = new RewardId(crateId, id);

        if (this.registry.containsKey(rewardId)) {
            return this.makeUnique(id);
        }

        return result;
    }

    private Identifier trimLength(Identifier id) {
        String value = id.value();
        int length = value.length();
        int threshold = SharedConstants.MAX_REWARD_ID_LENGTH;
        if (length > threshold) {
            return new Identifier(value.substring(0, threshold));
        }

        return id;
    }

    private Optional<Identifier> makeUnique(Identifier id) {
        String value = id.value();
        String uniqueSuffix = "_" + UUID.randomUUID().toString().substring(0, 8);
        int suffixLength = uniqueSuffix.length();

        String croppedValue = value.substring(0, Math.max(0, value.length() - suffixLength));

        return IdentifierParser.parse(croppedValue + uniqueSuffix);
    }

    public Optional<Identifier> createRewardId(ItemStack itemStack) {
        if (ItemHelper.isCustom(itemStack) && !ItemHelper.isMixed(itemStack)) {
            AdaptedItem adaptedItem = ItemHelper.adapt(itemStack);
            if (adaptedItem instanceof AdaptedDataItem<?> dataItem && dataItem.getData() instanceof ItemIdData idData) {
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
