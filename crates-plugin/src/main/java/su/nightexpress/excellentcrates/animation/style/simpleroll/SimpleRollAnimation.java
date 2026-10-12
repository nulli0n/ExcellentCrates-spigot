package su.nightexpress.excellentcrates.animation.style.simpleroll;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentcrates.animation.visibility.AnimationAudience;
import su.nightexpress.excellentcrates.api.animation.AnimationContext;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.core.animation.BaseAnimationInstance;
import su.nightexpress.nightcore.util.EntityUtil;
import su.nightexpress.nightcore.util.LocationUtil;
import su.nightexpress.nightcore.util.Randomizer;

public class SimpleRollAnimation extends BaseAnimationInstance {

    private final SimpleRollSettings settings;
    private final AnimationAudience  audience;
    private final Location           rewardLocation;

    private @Nullable Item rewardDisplay;

    private long tickCount;
    private int  rollsLeft;
    private int  finishTicks;

    public SimpleRollAnimation(Plugin plugin, SimpleRollSettings settings, AnimationContext context, Runnable onComplete) {
        super(context, onComplete);
        this.settings = settings;
        this.rollsLeft = settings.getRollAmount();
        this.finishTicks = settings.getFinishDelay();

        this.rewardLocation = this.adjustLocation(context.location());
        this.audience = new AnimationAudience(plugin, this.player, settings.getVisibility());
    }

    @Override
    public void onStart() {
        this.rewardDisplay = this.createDisplay();
    }

    @Override
    public void onStop() {
        this.audience.cleanup();
        this.rewardDisplay = null;
    }

    @Override
    public boolean onTick() {
        if (this.rollsLeft <= 0 && this.finishTicks <= 0) {
            return false;
        }

        if (this.rollsLeft > 0) {
            if (this.tickCount % this.settings.getRollInterval() == 0) {
                this.rollsLeft--;
                this.displayReward();
            }
        }
        else {
            if (this.finishTicks > 0) {
                this.finishTicks--;
            }
        }

        this.tickCount++;
        return true;
    }

    private Location adjustLocation(Location originLocation) {
        Block block = originLocation.getBlock();

        double offset = 0.35;
        double height = block.getBoundingBox().getHeight() + offset;

        return LocationUtil.setCenter2D(block.getLocation()).add(0, height, 0);
    }

    private Item createDisplay() {
        return this.audience.spawn(this.rewardLocation, Item.class, spawnedItem -> {
            spawnedItem.setVelocity(new Vector());
            spawnedItem.setCustomNameVisible(true);
            spawnedItem.setGravity(false);
            spawnedItem.setPickupDelay(Integer.MAX_VALUE);
            spawnedItem.setUnlimitedLifetime(true);
            spawnedItem.setInvulnerable(true);
        });
    }

    private void displayReward() {
        if (this.rewardDisplay == null) return;

        boolean lastRoll = this.rollsLeft <= 0;

        Reward reward = lastRoll ? this.reward.getRolled() : Randomizer.pick(this.availableRewards);

        ItemStack itemStack = this.rewardPreview.createPreviewItem(reward);
        String itemName = this.rewardPreview.getDisplayInfo(reward).name();

        this.rewardDisplay.setItemStack(itemStack);
        EntityUtil.setCustomName(this.rewardDisplay, itemName);

        this.audience.playSound(this.rewardLocation, Sound.UI_BUTTON_CLICK, 0.5f);

        if (lastRoll) {
            this.audience.playSound(this.rewardLocation, Sound.ENTITY_GENERIC_EXPLODE, 0.7f);
            this.audience.celebrate(this.rewardLocation);
        }
        else {
            this.audience.playSound(this.rewardLocation, Sound.BLOCK_NOTE_BLOCK_BELL, 0.5f);
        }
    }
}
