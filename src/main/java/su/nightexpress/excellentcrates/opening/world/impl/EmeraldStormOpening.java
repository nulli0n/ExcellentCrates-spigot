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

public class EmeraldStormOpening extends WorldOpening {

    private final int maxTicks;
    private final double radius;
    private final Reward finalReward;

    private Item rewardDisplay;
    private Location centerLocation;

    // Animation variables
    private double angle = 0;
    private boolean isRevealed = false;

    // Define the specific "Emerald" color for particles
    private final Particle.DustOptions emeraldDust;

    public EmeraldStormOpening(@NotNull CratesPlugin plugin,
                               @NotNull Player player,
                               @NotNull CrateSource source,
                               @Nullable Cost cost,
                               int maxTicks,
                               double radius) {
        super(plugin, player, source, cost);
        this.maxTicks = maxTicks;
        this.radius = radius;
        this.finalReward = source.getCrate().rollReward(player);

        // R:0 G:255 B:100 = Bright Emerald Green, Size 1.5
        this.emeraldDust = new Particle.DustOptions(Color.fromRGB(0, 255, 100), 1.5f);
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

        // Initial Display Item
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
            EntityUtil.setCustomName(item, "§2§lChecking Crate...");
        });

        // Deep energy sound start
        VanillaSound.of(Sound.BLOCK_BEACON_ACTIVATE, 1.0f, 0.5f).play(this.centerLocation);
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

        // --- 1. REWARD CYCLING (Suspense) ---
        int revealTick = (int) (this.maxTicks * 0.85);

        if (this.tickCount < revealTick) {
            // Rapid cycle
            if (this.tickCount % 2 == 0) {
                Reward temp = this.crate.rollReward(this.player);
                this.rewardDisplay.setItemStack(temp.getPreviewItem());

                // Glitchy text effect
                String[] titles = {"§a§k|||§2§l LOADING §a§k|||", "§2§k...§a§l SEARCHING §2§k..."};
                EntityUtil.setCustomName(this.rewardDisplay, titles[Math.toIntExact((this.tickCount / 2) % titles.length)]);

                // Click sound
                this.player.playSound(this.centerLocation, Sound.UI_BUTTON_CLICK, 0.1f, 2.0f);
            }

            // Hover Float effect
            double hover = Math.sin(this.tickCount * 0.2) * 0.2;
            this.rewardDisplay.teleport(this.centerLocation.clone().add(0, hover, 0));

        } else if (!isRevealed) {
            // REVEAL
            this.isRevealed = true;
            this.rewardDisplay.setItemStack(this.finalReward.getPreviewItem());
            EntityUtil.setCustomName(this.rewardDisplay, this.finalReward.getName());

            // Lock-in sound
            VanillaSound.of(Sound.BLOCK_CONDUIT_ACTIVATE, 1.5f, 1.5f).play(this.centerLocation);
            this.centerLocation.getWorld().spawnParticle(Particle.FLASH, this.centerLocation, 1);
        }

        // --- 2. THE EMERALD STORM PARTICLES ---

        // A. The Vortex (Replaces Happy Villager)
        // Creates a rising double spiral of high-quality green energy
        double rise = (this.tickCount % 40) / 20.0; // 0 to 2 height loop

        for (int i = 0; i < 2; i++) {
            // Two arms offset by PI
            double currentAng = angle + (i * Math.PI);

            double x = Math.cos(currentAng) * (radius * 0.8);
            double z = Math.sin(currentAng) * (radius * 0.8);

            // Spawn Colored Dust (Emerald)
            Location vortexLoc = this.centerLocation.clone().add(x, -0.5 + rise, z);
            this.centerLocation.getWorld().spawnParticle(Particle.DUST, vortexLoc, 1, 0, 0, 0, 0, emeraldDust);

            // Add a trail of Totem particles slightly behind
            if (this.tickCount % 2 == 0) {
                this.centerLocation.getWorld().spawnParticle(Particle.TOTEM_OF_UNDYING, vortexLoc, 0, 0, 0, 0);
            }
        }

        // B. The Core Pulse (Center of item)
        if (this.tickCount % 10 == 0) {
            this.centerLocation.getWorld().spawnParticle(Particle.CRIT, this.centerLocation.clone().add(0, 0.5, 0), 5, 0.2, 0.2, 0.2, 0.1);
        }

        // C. Audio Atmosphere (Pitch rises over time)
        if (this.tickCount % 5 == 0) {
            float progress = (float) this.tickCount / this.maxTicks;
            // Uses Beacon Ambient for a sci-fi/energy hum
            this.player.playSound(this.centerLocation, Sound.BLOCK_BEACON_AMBIENT, 0.5f, 0.5f + (progress * 1.5f));
        }

        // Rotate speed increases as it gets closer to finishing
        double speedMult = 1.0 + ((double)this.tickCount / this.maxTicks);
        angle += (0.2 * speedMult);
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

            // --- GRAND FINALE ---
            // 1. Massive Emerald Burst
            // Expanding ring of green dust
            for (double a = 0; a < Math.PI * 2; a += 0.2) {
                double x = Math.cos(a) * 2;
                double z = Math.sin(a) * 2;
                finalLoc.getWorld().spawnParticle(Particle.DUST, finalLoc.clone().add(x, 0, z), 1, 0, 0, 0, 0, emeraldDust);
            }

            // 2. Vertical Column of Totem Energy
            finalLoc.getWorld().spawnParticle(Particle.TOTEM_OF_UNDYING, finalLoc, 60, 0.5, 1.5, 0.5, 0.5);

            // 3. Sounds
            VanillaSound.of(Sound.ITEM_TOTEM_USE, 1f, 1f).play(finalLoc);
            VanillaSound.of(Sound.ENTITY_GENERIC_EXPLODE, 0.5f, 1f).play(finalLoc);

            OpeningUtils.createFirework(finalLoc);

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