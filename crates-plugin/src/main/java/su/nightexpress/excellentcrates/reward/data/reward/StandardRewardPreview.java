package su.nightexpress.excellentcrates.reward.data.reward;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.reward.data.model.RewardPreview;
import su.nightexpress.nightcore.bridge.item.AdaptedItem;
import su.nightexpress.nightcore.integration.item.impl.AdaptedVanillaStack;

@NullMarked
public class StandardRewardPreview implements RewardPreview {

    private String       name;
    private List<String> lore;
    private AdaptedItem  icon;
    private boolean      useIconData;

    public StandardRewardPreview(String name, List<String> lore, AdaptedItem icon, boolean useIconData) {
        this.name = name;
        this.lore = List.copyOf(lore);
        this.icon = icon;
        this.useIconData = useIconData;
    }

    public static StandardRewardPreview createDefault() {
        return new StandardRewardPreview("Reward", List.of(), AdaptedVanillaStack.of(new ItemStack(
            Material.ITEM_FRAME)),
            false);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public List<String> getLore() {
        return lore;
    }

    @Override
    public void setLore(List<String> lore) {
        this.lore = List.copyOf(lore);
    }

    @Override
    public AdaptedItem getIcon() {
        return icon;
    }

    @Override
    public void setIcon(AdaptedItem icon) {
        this.icon = icon;
    }

    @Override
    public boolean isUseIconData() {
        return useIconData;
    }

    @Override
    public void setUseIconData(boolean autoResolveFromIcon) {
        this.useIconData = autoResolveFromIcon;
    }
}
