package su.nightexpress.excellentcrates.api.crate;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nexmedia.engine.api.manager.ConfigHolder;
import su.nexmedia.engine.api.manager.ICleanable;
import su.nexmedia.engine.api.manager.IEditable;
import su.nexmedia.engine.api.manager.IPlaceholder;
import su.nexmedia.engine.api.menu.IMenu;
import su.nexmedia.engine.utils.ArrayUtil;
import su.nexmedia.engine.utils.LocationUtil;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.api.OpenCostType;
import su.nightexpress.excellentcrates.crate.effect.CrateEffectSettings;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public interface ICrate extends ConfigHolder, ICleanable, IEditable, IPlaceholder {

    String PLACEHOLDER_ID                         = "%crate_id%";
    String PLACEHOLDER_NAME                       = "%crate_name%";
    String PLACEHOLDER_ANIMATION_CONFIG           = "%crate_animation_config%";
    String PLACEHOLDER_PREVIEW_CONFIG             = "%crate_preview_config%";
    String PLACEHOLDER_PERMISSION                 = "%crate_permission%";
    String PLACEHOLDER_PERMISSION_REQUIRED        = "%crate_permission_required%";
    String PLACEHOLDER_ATTACHED_CITIZENS          = "%crate_attached_citizens%";
    String PLACEHOLDER_OPENING_COOLDOWN           = "%crate_opening_cooldown%";
    String PLACEHOLDER_OPENING_COST_MONEY         = "%crate_opening_cost_money%";
    String PLACEHOLDER_OPENING_COST_EXP           = "%crate_opening_cost_exp%";
    String PLACEHOLDER_KEY_IDS                    = "%crate_key_ids%";
    String PLACEHOLDER_ITEM_NAME                  = "%crate_item_name%";
    String PLACEHOLDER_ITEM_LORE                  = "%crate_item_lore%";
    String PLACEHOLDER_BLOCK_PUSHBACK_ENABLED     = "%crate_block_pushback_enabled%";
    String PLACEHOLDER_BLOCK_HOLOGRAM_ENABLED     = "%crate_block_hologram_enabled%";
    String PLACEHOLDER_BLOCK_HOLOGRAM_OFFSET_Y    = "%crate_block_hologram_offset_y%";
    String PLACEHOLDER_BLOCK_HOLOGRAM_TEXT        = "%crate_block_hologram_text%";
    String PLACEHOLDER_BLOCK_LOCATIONS            = "%crate_block_locations%";
    String PLACEHOLDER_BLOCK_EFFECT_MODEL         = "%crate_block_effect_model%";
    String PLACEHOLDER_BLOCK_EFFECT_PARTICLE_NAME = "%crate_block_effect_particle_name%";
    String PLACEHOLDER_BLOCK_EFFECT_PARTICLE_DATA = "%crate_block_effect_particle_data%";

    @Override
    @NotNull UnaryOperator<String> replacePlaceholders();

    @NotNull ExcellentCrates plugin();

    @NotNull String getId();

    @NotNull String getName();

    void setName(@NotNull String name);

    @Nullable String getAnimationConfig();

    void setAnimationConfig(@Nullable String animationConfig);

    @Nullable String getPreviewConfig();

    void setPreviewConfig(@Nullable String previewConfig);

    boolean isPermissionRequired();

    void setPermissionRequired(boolean permissionRequired);

    boolean hasPermission(@NotNull Player player);

    int[] getAttachedCitizens();

    void setAttachedCitizens(int[] attachedCitizens);

    default boolean isAttachedNPC(int id) {
        return ArrayUtil.contains(this.getAttachedCitizens(), id);
    }

    int getOpenCooldown();

    void setOpenCooldown(int openCooldown);

    double getOpenCost(@NotNull OpenCostType openCostType);

    void setOpenCost(@NotNull OpenCostType openCost, double amount);

    @NotNull Set<String> getKeyIds();

    void setKeyIds(@NotNull Set<String> keyIds);

    @NotNull ItemStack getItem();

    void setItem(@NotNull ItemStack item);

    boolean isBlockPushbackEnabled();

    void setBlockPushbackEnabled(boolean blockPushbackEnabled);

    @NotNull Set<Location> getBlockLocations();

    void setBlockLocations(@NotNull Set<Location> blockLocations);

    default void addBlockLocation(@NotNull Location location) {
        this.getBlockLocations().add(location);
    }

    default void removeBlockLocation(@NotNull Location location) {
        this.getBlockLocations().remove(location);
    }

    boolean isBlockHologramEnabled();

    void setBlockHologramEnabled(boolean blockHologramEnabled);

    double getBlockHologramOffsetY();

    void setBlockHologramOffsetY(double blockHologramOffsetY);

    @NotNull List<String> getBlockHologramText();

    void setBlockHologramText(@NotNull List<String> blockHologramText);

    @NotNull
    default Location getBlockHologramLocation(@NotNull Location loc) {
        double offset = this.getBlockHologramOffsetY();//1 + (0.25 * this.getBlockHologramText().size());
        return LocationUtil.getCenter(loc.clone()).add(0D, offset, 0D);
    }

    void createHologram();

    void removeHologram();

    void updateHologram();

    @NotNull CrateEffectSettings getBlockEffect();

    @NotNull LinkedHashMap<String, ICrateReward> getRewardsMap();

    void setRewardsMap(@NotNull LinkedHashMap<String, ICrateReward> rewardsMap);

    @NotNull
    default Collection<ICrateReward> getRewards() {
        return this.getRewardsMap().values();
    }

    default void setRewards(@NotNull List<ICrateReward> rewards) {
        this.setRewardsMap(rewards.stream().collect(
            Collectors.toMap(ICrateReward::getId, Function.identity(), (has, add) -> add, LinkedHashMap::new)));
    }

    @NotNull Collection<ICrateReward> getRewards(@NotNull Player player);

    @Nullable
    default ICrateReward getReward(@NotNull String id) {
        return this.getRewardsMap().get(id.toLowerCase());
    }

    default void addReward(@NotNull ICrateReward crateReward) {
        this.getRewardsMap().put(crateReward.getId(), crateReward);
    }

    default void removeReward(@NotNull ICrateReward crateReward) {
        this.removeReward(crateReward.getId());
    }

    default void removeReward(@NotNull String id) {
        this.getRewardsMap().remove(id);
    }

    @NotNull ICrateReward rollReward();

    @Nullable ICrateReward rollReward(@NotNull Player player);

    @Nullable IMenu getPreview();

    void createPreview();

    void openPreview(@NotNull Player player);
}
