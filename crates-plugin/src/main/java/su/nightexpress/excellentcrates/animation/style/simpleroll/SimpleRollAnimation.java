package su.nightexpress.excellentcrates.animation.style.simpleroll;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import su.nightexpress.excellentcrates.api.animation.AnimationContext;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.core.animation.BaseAnimationInstance;
import su.nightexpress.excellentcrates.core.util.FireworkUtil;
import su.nightexpress.nightcore.util.EntityUtil;
import su.nightexpress.nightcore.util.LocationUtil;
import su.nightexpress.nightcore.util.Randomizer;
import su.nightexpress.nightcore.util.sound.VanillaSound;

public class SimpleRollAnimation extends BaseAnimationInstance {

    private final SimpleRollSettings settings;
    private final Item               rewardDisplay;
    private final Location           rewardLocation;

    private long tickCount;
    private int  rollsLeft;
    private int  finishTicks;

    public SimpleRollAnimation(SimpleRollSettings settings, AnimationContext context, Runnable onComplete) {
        super(context, onComplete);
        this.settings = settings;
        this.rollsLeft = settings.getRollAmount();
        this.finishTicks = settings.getFinishDelay();

        this.rewardLocation = this.adjustLocation(context.location());
        this.rewardDisplay = this.createDisplay(this.player.getWorld());
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {
        if (this.rewardDisplay != null) {
            this.rewardDisplay.remove();
        }
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

    private Item createDisplay(World world) {
        Item item = world.spawn(this.rewardLocation, Item.class, spawnedItem -> {
            spawnedItem.setVelocity(new Vector());
        });

        item.setPersistent(false);
        item.setCustomNameVisible(true);
        item.setGravity(false);
        item.setPickupDelay(Integer.MAX_VALUE);
        item.setUnlimitedLifetime(true);
        item.setInvulnerable(true);

        return item;
    }

    private void displayReward() {
        boolean lastRoll = this.rollsLeft <= 0;

        Reward reward = lastRoll ? this.reward.getRolled() : Randomizer.pick(this.availableRewards);

        ItemStack itemStack = this.rewardPreview.createPreviewItem(reward);
        String itemName = this.rewardPreview.getDisplayInfo(reward).name();

        this.rewardDisplay.setItemStack(itemStack);
        EntityUtil.setCustomName(this.rewardDisplay, itemName);

        VanillaSound.of(Sound.UI_BUTTON_CLICK, 0.5f).play(this.rewardLocation);

        if (lastRoll) {
            VanillaSound.of(Sound.ENTITY_GENERIC_EXPLODE, 0.7f).play(this.rewardLocation);
            FireworkUtil.createRandom(this.rewardLocation);
        }
        else {
            VanillaSound.of(Sound.BLOCK_NOTE_BLOCK_BELL, 0.5f).play(this.rewardLocation);
        }
    }
}
