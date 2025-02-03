package su.nightexpress.excellentcrates.opening.world.impl;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.Reward;
import su.nightexpress.excellentcrates.crate.impl.CrateSource;
import su.nightexpress.excellentcrates.key.CrateKey;
import su.nightexpress.excellentcrates.opening.OpeningUtils;
import su.nightexpress.excellentcrates.opening.world.WorldOpening;
import su.nightexpress.excellentcrates.util.CrateUtils;
import su.nightexpress.nightcore.util.LocationUtil;
import su.nightexpress.nightcore.util.bukkit.NightSound;

public class SimpleRollOpening extends WorldOpening {

    //private static final int  STEPS_AMOUNT = 15;
    //private static final long STEPS_TICK   = 3L;
    //private static final long MAX_TICKS    = STEPS_AMOUNT * STEPS_TICK + 40L;

    private final int stepsAmount;
    private final long stepsTick;
    private final long maxTicks;

    private int    step;
    private Item   rewardDisplay;
    private Reward reward;
    private Location    displayLocation;

    public SimpleRollOpening(@NotNull CratesPlugin plugin,
                             @NotNull Player player,
                             @NotNull CrateSource source,
                             @Nullable CrateKey key,
                             int stepsAmount,
                             long stepsTick,
                             long completePause) {
        super(plugin, player, source, key);
        this.stepsAmount = stepsAmount;
        this.stepsTick = stepsTick;
        this.maxTicks = (this.stepsAmount * this.stepsTick) + completePause;
    }

    private void onFirstTick() {
        Block block = this.source.getBlock();
        double yAdd = this.crate.getHologramYOffset();

        Location center;
        if (block == null) {
            Location playerLoc = this.player.getEyeLocation().clone();
            Vector direction = playerLoc.getDirection();

            for (int i = 0; i < 5; i++) {
                playerLoc.add(direction);
            }

            center = playerLoc;
        }
        else {
            yAdd += block.getBoundingBox().getHeight() / 2D;
            center = block.getLocation();
        }

        this.displayLocation = LocationUtil.setCenter3D(center).add(0, yAdd, 0);

        this.hideHologram();
    }

    @Override
    public void instaRoll() {
        this.step = this.stepsAmount - 1;
        this.roll();
        // Display roll visuals only when instal roll was called in the middle of the opening process to "finish" the visual part.
        if (this.tickCount > 0) {
            this.displayRoll();
        }
        this.tickCount = this.maxTicks;
        this.stop();
    }

    @Override
    protected void onStart() {

    }

    @Override
    protected void onTick() {
        if (this.tickCount == 0) {
            this.onFirstTick();
        }

        if (this.tickCount % this.stepsTick == 0 && this.step < this.stepsAmount) {
            this.roll();
            this.displayRoll();
        }
    }

    @Override
    protected void onComplete() {
        this.reward.give(this.player);

        CrateUtils.callRewardObtainEvent(this.player, this.reward);
    }

    @Override
    protected void onStop() {
        if (this.rewardDisplay != null) {
            this.rewardDisplay.remove();
            this.rewardDisplay = null;
        }

        this.showHologram();

        super.onStop();
    }

    @Override
    public boolean isCompleted() {
        return this.tickCount >= this.maxTicks;
    }

    private void roll() {
        this.reward = this.crate.rollReward(this.player);
        this.step++;
        if (this.step == this.stepsAmount) {
            this.setRefundable(false);
        }
    }

    private void displayRoll() {
        if (this.rewardDisplay == null) {
            this.rewardDisplay = player.getWorld().spawn(this.displayLocation, Item.class, item -> item.setVelocity(new Vector()));
            this.rewardDisplay.setPersistent(false);
            this.rewardDisplay.setCustomNameVisible(true);
            this.rewardDisplay.setGravity(false);
            this.rewardDisplay.setPickupDelay(Integer.MAX_VALUE);
            this.rewardDisplay.setUnlimitedLifetime(true);
            this.rewardDisplay.setInvulnerable(true);
            //this.rewardDisplay.setBillboard(Display.Billboard.CENTER);
            //this.rewardDisplay.setTransformation(new Transformation(new Vector3f(), new AxisAngle4f(), new Vector3f(0.35f, 0.35f, 0.35f), new AxisAngle4f()));
        }
        if (this.rewardDisplay != null) {
            ItemStack itemStack = this.reward.getPreviewItem();
            this.rewardDisplay.setItemStack(itemStack);
            this.rewardDisplay.setCustomName(this.reward.getNameTranslated());
        }

        NightSound.of(Sound.UI_BUTTON_CLICK, 0.5f).play(this.displayLocation);
        NightSound.of(Sound.BLOCK_NOTE_BLOCK_BELL, 0.5f).play(this.displayLocation);

        if (this.step == this.stepsAmount) {
            NightSound.of(Sound.ENTITY_GENERIC_EXPLODE, 0.7f).play(this.displayLocation);
            OpeningUtils.createFirework(this.displayLocation);
        }
    }
}
