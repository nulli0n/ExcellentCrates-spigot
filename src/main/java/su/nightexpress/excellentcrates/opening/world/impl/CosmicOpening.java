package su.nightexpress.excellentcrates.opening.world.impl;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.Reward;
import su.nightexpress.excellentcrates.crate.cost.Cost;
import su.nightexpress.excellentcrates.crate.impl.CrateSource;
import su.nightexpress.excellentcrates.opening.OpeningUtils;
import su.nightexpress.excellentcrates.opening.world.WorldOpening;
import su.nightexpress.excellentcrates.util.pos.WorldPos;
import su.nightexpress.nightcore.util.EntityUtil;
import su.nightexpress.nightcore.util.LocationUtil;
import su.nightexpress.nightcore.util.sound.VanillaSound;

public class CosmicOpening extends WorldOpening {

    private final int maxTicks;
    private final double radius;
    private final double riseSpeed;
    private final Reward finalReward;
    private Item rewardDisplay;
    private Location centerLocation;
    private double currentHeight = 0;
    private double currentAngle = 0;
    private boolean isRevealed = false;

    public CosmicOpening(@NotNull CratesPlugin plugin,
                         @NotNull Player player,
                         @NotNull CrateSource source,
                         @Nullable Cost cost,
                         int maxTicks,
                         double radius,
                         double riseSpeed) {
        super(plugin, player, source, cost);
        this.maxTicks = maxTicks;
        this.radius = radius;
        this.riseSpeed = riseSpeed;

        this.finalReward = source.getCrate().rollReward(player);
    }

    @Override
    public long getInterval() {
        return 1L;
    }

    private void onFirstTick() {
        Block block = this.source.getBlock();

        if (block == null) {
            Location playerLoc = this.player.getEyeLocation().clone();
            Vector direction = playerLoc.getDirection();
            for (int i = 0; i < 3; i++) {
                playerLoc.add(direction);
            }
            this.centerLocation = LocationUtil.setCenter3D(playerLoc);
        } else {
            double offset = Math.max(0, this.crate.getHologramYOffset());
            double height = block.getBoundingBox().getHeight() + offset;

            this.centerLocation = LocationUtil.setCenter2D(block.getLocation()).add(0, height, 0);

            WorldPos blockPos = WorldPos.from(block);
            this.hideHologram(blockPos);
        }

        Reward randomReward = this.crate.rollReward(this.player);

        // VLIEGEND ITEM
        this.rewardDisplay = this.player.getWorld().spawn(this.centerLocation, Item.class, item -> {
            item.setVelocity(new Vector(0, 0, 0));
            item.setPersistent(false);
            item.setCustomNameVisible(true);
            item.setGravity(false);
            item.setPickupDelay(Integer.MAX_VALUE);
            item.setUnlimitedLifetime(true);
            item.setInvulnerable(true);
            item.setItemStack(randomReward.getPreviewItem());
            EntityUtil.setCustomName(item, "???");
        });

        this.setRefundable(false);
    }

    @Override
    public void instaRoll() {
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

        if (this.centerLocation == null || this.rewardDisplay == null) return;

        this.currentHeight += this.riseSpeed;
        Location itemLoc = this.centerLocation.clone().add(0, currentHeight, 0);
        this.rewardDisplay.teleport(itemLoc);
        int revealTick = (int) (this.maxTicks * 0.90);

        if (this.tickCount < revealTick) {
            if (this.tickCount % 3 == 0) {
                Reward randomTemp = this.crate.rollReward(this.player);
                this.rewardDisplay.setItemStack(randomTemp.getPreviewItem());
                EntityUtil.setCustomName(this.rewardDisplay, "§k" + randomTemp.getName());

                VanillaSound.of(Sound.UI_BUTTON_CLICK, 0.2f, 2.0f).play(itemLoc);
            }
        } else if (!isRevealed) {
            this.isRevealed = true;
            this.rewardDisplay.setItemStack(this.finalReward.getPreviewItem());
            EntityUtil.setCustomName(this.rewardDisplay, this.finalReward.getName());

            VanillaSound.of(Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 2f).play(itemLoc);
            itemLoc.getWorld().spawnParticle(Particle.FLASH, itemLoc, 1);
        }

        // 1 Paarse cirkel
        double x1 = radius * Math.cos(currentAngle);
        double z1 = radius * Math.sin(currentAngle);
        itemLoc.getWorld().spawnParticle(Particle.WITCH, itemLoc.clone().add(x1, 0, z1), 1, 0, 0, 0, 0);

        // 2 Witte Cirkel
        double x2 = radius * Math.cos(currentAngle + Math.PI);
        double z2 = radius * Math.sin(currentAngle + Math.PI);
        itemLoc.getWorld().spawnParticle(Particle.END_ROD, itemLoc.clone().add(x2, 0, z2), 1, 0, 0, 0, 0.01);

        if (this.tickCount % 4 == 0) {
            itemLoc.getWorld().spawnParticle(Particle.DRAGON_BREATH, itemLoc, 1, 0.1, 0.1, 0.1, 0.02);
        }

        if (this.tickCount % 5 == 0) {
            float pitch = 0.5f + ((float) this.tickCount / this.maxTicks) * 1.5f;
            VanillaSound.of(Sound.BLOCK_NOTE_BLOCK_CHIME, 0.4f, pitch).play(itemLoc);
        }

        this.currentAngle += 0.2;
    }

    @Override
    protected void onComplete() {
    }

    @Override
    public boolean isCompleted() {
        return this.tickCount >= this.maxTicks;
    }

    @Override
    protected void onStop() {
        // REWARD
        this.addReward(this.finalReward);

        // BOOM
        if (this.rewardDisplay != null) {
            Location finalLoc = this.rewardDisplay.getLocation();

            VanillaSound.of(Sound.ENTITY_GENERIC_EXPLODE, 1f, 1f).play(finalLoc);
            VanillaSound.of(Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 1f).play(finalLoc);

            OpeningUtils.createFirework(finalLoc);
            finalLoc.getWorld().spawnParticle(Particle.EXPLOSION_EMITTER, finalLoc, 1);

            this.rewardDisplay.remove();
            this.rewardDisplay = null;
        }

        //Hologram
        Block block = this.source.getBlock();
        if (block != null) {
            WorldPos blockPos = WorldPos.from(block);
            this.showHologram(blockPos);
        }

        super.onStop();
    }
}