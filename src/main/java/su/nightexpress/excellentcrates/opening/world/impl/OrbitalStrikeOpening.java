package su.nightexpress.excellentcrates.opening.world.impl;

import org.bukkit.Color;
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

public class OrbitalStrikeOpening extends WorldOpening {

    private final int maxTicks;
    private final double beamHeight;
    private final Reward finalReward;

    private Item rewardDisplay;
    private Location centerLocation;

    // Laser Colors
    private final Particle.DustOptions laserRed;
    private final Particle.DustOptions laserCore;

    private boolean hasStruck = false;

    public OrbitalStrikeOpening(@NotNull CratesPlugin plugin,
                                @NotNull Player player,
                                @NotNull CrateSource source,
                                @Nullable Cost cost,
                                int maxTicks,
                                double beamHeight) {
        super(plugin, player, source, cost);
        this.maxTicks = maxTicks;
        this.beamHeight = beamHeight;
        this.finalReward = source.getCrate().rollReward(player);

        // Bright Red for outer beam, White/Orange for core heat
        this.laserRed = new Particle.DustOptions(Color.fromRGB(255, 0, 0), 1.0f);
        this.laserCore = new Particle.DustOptions(Color.fromRGB(255, 150, 0), 2.0f);
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

            this.centerLocation = LocationUtil.setCenter2D(block.getLocation()).add(0, height + 0.5, 0);

            WorldPos blockPos = WorldPos.from(block);
            this.hideHologram(blockPos);
        }

        // Initialize Display Item (Invisible at first, appears during beam)
        Reward randomReward = this.crate.rollReward(this.player);
        this.rewardDisplay = this.player.getWorld().spawn(this.centerLocation, Item.class, item -> {
            item.setVelocity(new Vector(0, 0, 0));
            item.setPersistent(false);
            item.setCustomNameVisible(true);
            item.setGravity(false);
            item.setPickupDelay(Integer.MAX_VALUE);
            item.setUnlimitedLifetime(true);
            item.setInvulnerable(true);
            item.setItemStack(randomReward.getPreviewItem());
            EntityUtil.setCustomName(item, "§c§lTARGET LOCKED");
        });

        this.setRefundable(false);
    }

    @Override
    public void instaRoll() {
        this.tickCount = this.maxTicks;
        this.stop();
    }

    @Override
    protected void onStart() { }

    @Override
    protected void onTick() {
        if (this.tickCount == 0) this.onFirstTick();
        if (this.centerLocation == null || this.rewardDisplay == null) return;

        // Define phase timings
        int targetDuration = (int) (this.maxTicks * 0.35); // First 35% is targeting

        // --- PHASE 1: TARGETING (Lock on) ---
        if (this.tickCount < targetDuration) {
            // "Target Lock" Sound
            if (this.tickCount % 5 == 0) {
                this.player.playSound(this.centerLocation, Sound.BLOCK_NOTE_BLOCK_BIT, 1f, 2f);
            }

            // Expanding/Contracting Red Rings
            double radius = 3.0 - (3.0 * ((double)this.tickCount / targetDuration)); // Shrinks to 0
            for (int i = 0; i < 360; i += 20) {
                double rad = Math.toRadians(i);
                double x = Math.cos(rad) * radius;
                double z = Math.sin(rad) * radius;

                // Spawn laser dots
                this.centerLocation.getWorld().spawnParticle(Particle.DUST, this.centerLocation.clone().add(x, 0.2, z), 1, 0, 0, 0, 0, laserRed);
            }

            // Rotating item
            this.rewardDisplay.setRotation(this.tickCount * 10, 0);
            return;
        }

        // --- PHASE 2: THE STRIKE ---
        if (!hasStruck) {
            hasStruck = true;
            // Massive initial thunder sound
            VanillaSound.of(Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 2.0f, 1.0f).play(this.centerLocation);
            VanillaSound.of(Sound.ENTITY_GENERIC_EXPLODE, 0.5f, 0.5f).play(this.centerLocation);
        }

        // 1. The Beam Visuals
        // Draw a line from sky to crate
        for (double y = 0; y < beamHeight; y += 0.5) {
            Location beamLoc = this.centerLocation.clone().add(0, y, 0);

            // Core beam (Thick orange)
            beamLoc.getWorld().spawnParticle(Particle.DUST, beamLoc, 2, 0.1, 0.1, 0.1, 0, laserCore);

            // Outer beam (Red scatter)
            beamLoc.getWorld().spawnParticle(Particle.DUST, beamLoc, 2, 0.3, 0.3, 0.3, 0, laserRed);

            // Occasional lightning sparks
            if (Math.random() > 0.95) {
                beamLoc.getWorld().spawnParticle(Particle.FIREWORK, beamLoc, 0, 0, 0, 0);
            }
        }

        // 2. Ground Impact Effects
        this.centerLocation.getWorld().spawnParticle(Particle.LAVA, this.centerLocation, 5, 0.5, 0.1, 0.5);
        this.centerLocation.getWorld().spawnParticle(Particle.LARGE_SMOKE, this.centerLocation, 3, 0.2, 0.1, 0.2, 0.1);

        // 3. Item Cycling (Inside the beam)
        if (this.tickCount % 2 == 0) {
            Reward temp = this.crate.rollReward(this.player);
            this.rewardDisplay.setItemStack(temp.getPreviewItem());
            EntityUtil.setCustomName(this.rewardDisplay, "§c§k||| §6WARNING §c§k|||");

            // Beam hum sound
            this.player.playSound(this.centerLocation, Sound.BLOCK_BEACON_AMBIENT, 0.2f, 0.5f);
        }

        // Spin item aggressively
        this.rewardDisplay.setRotation(this.tickCount * 30, 0);
    }

    @Override
    protected void onComplete() { }

    @Override
    public boolean isCompleted() {
        return this.tickCount >= this.maxTicks;
    }

    @Override
    protected void onStop() {
        this.addReward(this.finalReward);

        if (this.rewardDisplay != null) {
            Location finalLoc = this.rewardDisplay.getLocation();

            // --- PHASE 3: IMPACT & REVEAL ---

            // 1. The Explosion
            finalLoc.getWorld().spawnParticle(Particle.EXPLOSION_EMITTER, finalLoc, 1);
            finalLoc.getWorld().spawnParticle(Particle.FLASH, finalLoc, 2);

            // Shockwave ring
            for (double r = 0; r < 3; r += 0.5) {
                for (int i = 0; i < 360; i += 15) {
                    double rad = Math.toRadians(i);
                    double x = Math.cos(rad) * r;
                    double z = Math.sin(rad) * r;
                    finalLoc.getWorld().spawnParticle(Particle.LARGE_SMOKE, finalLoc.clone().add(x, 0, z), 1, 0, 0, 0, 0.1);
                }
            }

            // 2. Sound Final
            VanillaSound.of(Sound.ENTITY_GENERIC_EXPLODE, 1f, 1f).play(finalLoc);
            VanillaSound.of(Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 1f).play(finalLoc);

            // 3. Set Final Item (User sees this for a split second before pickup, or if inv is full)
            this.rewardDisplay.setItemStack(this.finalReward.getPreviewItem());
            EntityUtil.setCustomName(this.rewardDisplay, this.finalReward.getName());

            // Cleanup item
            this.rewardDisplay.remove();
            this.rewardDisplay = null;
        }

        Block block = this.source.getBlock();
        if (block != null) {
            WorldPos blockPos = WorldPos.from(block);
            this.showHologram(blockPos);
        }

        super.onStop();
    }
}