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

public class GalaxyOpening extends WorldOpening {

    private final int maxTicks;
    private final double maxRadius;
    private final Reward finalReward;

    private Item rewardDisplay;
    private Location centerLocation;

    // Animation variables
    private double currentRotation = 0;
    private boolean isRevealed = false;

    // Cosmic Colors
    private final Particle.DustOptions dustBlue;
    private final Particle.DustOptions dustPurple;
    private final Particle.DustOptions dustCyan;

    public GalaxyOpening(@NotNull CratesPlugin plugin,
                         @NotNull Player player,
                         @NotNull CrateSource source,
                         @Nullable Cost cost,
                         int maxTicks,
                         double maxRadius) {
        super(plugin, player, source, cost);
        this.maxTicks = maxTicks;
        this.maxRadius = maxRadius;
        this.finalReward = source.getCrate().rollReward(player);

        // Define galaxy colors
        this.dustBlue = new Particle.DustOptions(Color.fromRGB(0, 50, 255), 1.2f);
        this.dustPurple = new Particle.DustOptions(Color.fromRGB(150, 0, 255), 1.2f);
        this.dustCyan = new Particle.DustOptions(Color.fromRGB(0, 255, 255), 0.8f);
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

        // Spawn central "Star" (The item)
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
            EntityUtil.setCustomName(item, "§b§k... §3GALAXY §b§k...");
        });

        // Initial deep space sound
        VanillaSound.of(Sound.BLOCK_BEACON_ACTIVATE, 0.5f, 0.5f).play(this.centerLocation);
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

        // Calculate progress
        int revealTick = (int) (this.maxTicks * 0.90); // Implosion starts at 90%

        // --- 1. REWARD CYCLING ---
        if (this.tickCount < revealTick) {
            if (this.tickCount % 4 == 0) {
                Reward temp = this.crate.rollReward(this.player);
                this.rewardDisplay.setItemStack(temp.getPreviewItem());
                EntityUtil.setCustomName(this.rewardDisplay, "§d" + temp.getName());
                // Soft click
                this.player.playSound(this.centerLocation, Sound.UI_BUTTON_CLICK, 0.1f, 1.5f);
            }

            // Slow rotation of the item itself
            this.rewardDisplay.setRotation((float) (this.tickCount * 5), 0);
        }

        // --- 2. GALAXY PARTICLES ---

        if (this.tickCount < revealTick) {
            // Draw 3 spiral arms
            int arms = 3;
            double particlesPerArm = 15;

            // Speed increases over time
            double speed = 0.1 + ((double) this.tickCount / this.maxTicks) * 0.4;
            this.currentRotation += speed;

            for (int arm = 0; arm < arms; arm++) {
                double armOffset = (Math.PI * 2 / arms) * arm;

                for (double i = 0; i < particlesPerArm; i++) {
                    // Distance from center (0 to maxRadius)
                    double r = (i / particlesPerArm) * maxRadius;

                    // Spiral logic: Angle increases with radius
                    double angle = this.currentRotation + armOffset + (r * 0.8);

                    double x = Math.cos(angle) * r;
                    double z = Math.sin(angle) * r;

                    Location particleLoc = this.centerLocation.clone().add(x, 0, z);

                    // Pick color based on arm
                    Particle.DustOptions color = (arm == 0) ? dustBlue : (arm == 1) ? dustPurple : dustCyan;

                    this.centerLocation.getWorld().spawnParticle(Particle.DUST, particleLoc, 1, 0, 0, 0, 0, color);

                    // Add some "Stars" (End Rod) near the tips
                    if (i > particlesPerArm - 3) {
                        this.centerLocation.getWorld().spawnParticle(Particle.END_ROD, particleLoc, 0, 0, 0, 0);
                    }
                }
            }

            // Core glow
            this.centerLocation.getWorld().spawnParticle(Particle.DRAGON_BREATH, this.centerLocation, 1, 0.1, 0.1, 0.1, 0.01);

            // Hum Sound (Rising pitch)
            if (this.tickCount % 5 == 0) {
                float pitch = 0.5f + ((float)this.tickCount / revealTick) * 1.5f;
                VanillaSound.of(Sound.BLOCK_PORTAL_TRIGGER, 0.2f, pitch).play(this.centerLocation);
            }

        } else {
            // --- 3. IMPLOSION PHASE (90% - 100%) ---
            if (!isRevealed) {
                // Lock in the item immediately when implosion starts
                isRevealed = true;
                this.rewardDisplay.setItemStack(this.finalReward.getPreviewItem());
                EntityUtil.setCustomName(this.rewardDisplay, this.finalReward.getName());

                // Vacuum sound
                VanillaSound.of(Sound.ENTITY_ZOMBIE_VILLAGER_CURE, 1.5f, 0.5f).play(this.centerLocation);
            }

            // Particles sucking IN
            double reverseProgress = (double)(this.maxTicks - this.tickCount) / (this.maxTicks - revealTick); // 1.0 to 0.0
            double r = reverseProgress * maxRadius;

            for (int i = 0; i < 20; i++) {
                double angle = Math.random() * Math.PI * 2;
                double x = Math.cos(angle) * r;
                double z = Math.sin(angle) * r;
                // Purple dust rushing in
                this.centerLocation.getWorld().spawnParticle(Particle.DUST, this.centerLocation.clone().add(x, 0, z), 1, 0, 0, 0, 0, dustPurple);
            }
        }
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

            // --- 4. SUPERNOVA (Explosion) ---

            // Blast wave
            finalLoc.getWorld().spawnParticle(Particle.EXPLOSION_EMITTER, finalLoc, 1);
            finalLoc.getWorld().spawnParticle(Particle.FLASH, finalLoc, 5);

            // Expanding ring of nebula colors
            for (int i = 0; i < 100; i++) {
                double angle = (Math.PI * 2 * i) / 100;
                double x = Math.cos(angle) * 2.5;
                double z = Math.sin(angle) * 2.5;

                // Mix of colors
                Particle.DustOptions color = (i % 2 == 0) ? dustBlue : dustCyan;
                finalLoc.getWorld().spawnParticle(Particle.DUST, finalLoc.clone().add(x, 0, z), 1, 0, 0, 0, 0.5, color);
            }

            VanillaSound.of(Sound.ENTITY_GENERIC_EXPLODE, 1f, 0.8f).play(finalLoc);
            VanillaSound.of(Sound.ITEM_TRIDENT_THUNDER, 1f, 1.2f).play(finalLoc);

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