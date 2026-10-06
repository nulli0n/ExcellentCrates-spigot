package su.nightexpress.excellentcrates.effect.instance.impl;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.bukkit.particle.ParticleEffect;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.block.position.CratePosition;
import su.nightexpress.excellentcrates.api.crate.component.CrateComponentKeys;
import su.nightexpress.excellentcrates.api.effect.EffectBaseSettings;
import su.nightexpress.excellentcrates.api.effect.EffectInstance;
import su.nightexpress.excellentcrates.api.effect.EffectProfile;
import su.nightexpress.excellentcrates.api.effect.EffectRegistry;
import su.nightexpress.excellentcrates.api.effect.crate.EffectComponent;
import su.nightexpress.nightcore.util.LocationUtil;

@NullMarked
public class DefaultEffectInstance implements EffectInstance {

    private final Crate         crate;
    private final CratePosition position;

    private @Nullable Location cachedLocation;

    private int currentStep;
    private int pauseTicksLeft;

    public DefaultEffectInstance(Crate crate, CratePosition position) {
        this.crate = crate;
        this.position = position;
        this.currentStep = 0;
        this.pauseTicksLeft = 0;
    }

    @Override
    public void tick(EffectRegistry registry) {
        EffectComponent component = this.crate.getComponentOrNull(CrateComponentKeys.EFFECT);
        if (component == null || !component.isEnabled()) return;

        // Dynamic lookup allows hot-reloading configurations mid-animation
        EffectProfile<?> profile = registry.getProfile(component.getProfileKey());
        if (profile == null) return;

        EffectBaseSettings settings = profile.baseSettings();

        if (this.pauseTicksLeft > 0) {
            this.pauseTicksLeft--;
            return;
        }

        if (this.currentStep % settings.tickInterval() != 0) {
            this.currentStep++;
            return;
        }

        Location location = this.cachedLocation;
        if (location == null) {
            World world = Bukkit.getWorld(this.position.worldKey().bukkit());
            if (world == null) return;

            location = LocationUtil.setCenter2D(this.position.position().toLocation(world));
            this.cachedLocation = location;
        }

        ParticleEffect<?> effect = settings.effect();

        profile.play(location.clone(), effect, this.currentStep);
        this.currentStep++;

        if (this.currentStep >= settings.maxFrames()) {
            this.currentStep = 0;
            this.pauseTicksLeft = settings.pauseTicks();
        }
    }
}
