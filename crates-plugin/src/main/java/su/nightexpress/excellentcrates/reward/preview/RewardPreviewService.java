package su.nightexpress.excellentcrates.reward.preview;

import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.settings.ReadOnlySettings;
import su.nightexpress.excellentcrates.api.common.preview.NameAndLore;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.data.model.RewardPreview;
import su.nightexpress.excellentcrates.api.reward.placeholder.RewardPlaceholders;
import su.nightexpress.excellentcrates.api.reward.preview.RewardColorProvider;
import su.nightexpress.excellentcrates.reward.preview.settings.RewardPreviewSettings;
import su.nightexpress.excellentcrates.util.ItemHelper;
import su.nightexpress.nightcore.bridge.item.AdaptedItem;
import su.nightexpress.nightcore.util.ItemUtil;
import su.nightexpress.nightcore.util.bukkit.NightItem;

@NullMarked
public class RewardPreviewService {

    private final ReadOnlySettings<RewardPreviewSettings> settings;
    private final RewardPreviewResolver                   previewResolver;
    private final RewardPlaceholders                      placeholders;

    private RewardColorProvider colorProvider;

    public RewardPreviewService(ReadOnlySettings<RewardPreviewSettings> settings,
                                RewardPreviewResolver previewResolver,
                                RewardPlaceholders placeholders) {
        this.settings = settings;
        this.previewResolver = previewResolver;
        this.placeholders = placeholders;
        this.colorProvider = (reward, defaultColor) -> defaultColor;
    }

    public void setColorProvider(RewardColorProvider colorProvider) {
        this.colorProvider = colorProvider;
    }

    public NightItem createPreviewIconWithPlaceholders(Reward reward) {
        return this.createPreviewIcon(reward)
            .replace(ctx -> ctx.apply(this.placeholders.basePlaceholders(reward)));
    }

    public NightItem createPreviewIconWithAllPlaceholders(Crate crate, Reward reward) {
        return this.createPreviewIcon(reward)
            .replace(ctx -> ctx.apply(this.placeholders.allPlaceholders(crate, reward)));
    }

    public NightItem createPreviewIcon(Reward reward) {
        return NightItem.fromItemStack(this.createPreviewItem(reward));
    }

    public ItemStack createDisplayItem(Reward reward) {
        ItemStack itemStack = this.createPreviewItem(reward);

        if (!reward.getPreview().isUseIconData()) {
            NameAndLore resolvedInfo = this.previewResolver.getDisplayInfo(reward);
            ItemUtil.editMeta(itemStack, meta -> {
                ItemUtil.setCustomName(meta, resolvedInfo.name());
                ItemUtil.setLore(meta, resolvedInfo.lore());
            });
        }

        return itemStack;
    }

    public ItemStack createPreviewItem(Reward reward) {
        RewardPreview preview = reward.getPreview();
        AdaptedItem icon = preview.getIcon();
        ItemStack itemStack = icon.getItemStack();
        return itemStack != null ? itemStack : ItemHelper.createPlaceholder();
    }

    public Color getColor(Reward reward) {
        return this.colorProvider.getColor(reward, this.settings.get().defaultRewardColor());
    }
}
