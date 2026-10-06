package su.nightexpress.excellentcrates.crates.interact.cooldown;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.settings.ReadOnlySettings;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.crates.interact.settings.InteractSettings;

@NullMarked
public class InteractCooldownService {

    private final ReadOnlySettings<InteractSettings> settings;
    private final InteractCooldownTracker            cooldownTracker;

    public InteractCooldownService(ReadOnlySettings<InteractSettings> settings,
                                   InteractCooldownTracker cooldownTracker) {
        this.settings = settings;
        this.cooldownTracker = cooldownTracker;
    }

    private boolean isEnabled() {
        return this.settings.get().cooldownEnabled();
    }

    public void setCooldown(Player player, Crate crate) {
        if (!this.isEnabled()) return;

        int durationMills = this.settings.get().cooldownDuration() * 1000;
        this.cooldownTracker.setCooldown(player.getUniqueId(), crate.id(), durationMills);
    }

    public @Nullable InteractCooldown getCooldown(Player player, Crate crate) {
        if (!this.isEnabled()) return null;

        return this.cooldownTracker.getCooldown(player.getUniqueId(), crate.id());
    }

    public void clearCooldown(Player player, Crate crate) {
        this.cooldownTracker.clearCooldown(player.getUniqueId(), crate.id());
    }

    public void clearAllCooldowns(Player player) {
        this.cooldownTracker.clearAllCooldowns(player.getUniqueId());
    }
}
