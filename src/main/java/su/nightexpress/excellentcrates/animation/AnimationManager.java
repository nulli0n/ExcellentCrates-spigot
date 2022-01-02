package su.nightexpress.excellentcrates.animation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nexmedia.engine.api.config.JYML;
import su.nexmedia.engine.api.manager.AbstractManager;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.api.AnimationType;
import su.nightexpress.excellentcrates.api.crate.ICrateAnimation;
import su.nightexpress.excellentcrates.config.Config;

import java.util.*;

public class AnimationManager extends AbstractManager<ExcellentCrates> {

    private Map<String, ICrateAnimation> animations;

    public AnimationManager(@NotNull ExcellentCrates plugin) {
        super(plugin);
    }

    @Override
    protected void onLoad() {
        this.plugin.getConfigManager().extract(Config.DIR_ANIMATIONS);

        this.animations = new HashMap<>();
        for (JYML cfg : JYML.loadAll(plugin.getDataFolder() + Config.DIR_ANIMATIONS, true)) {
            AnimationType animationType = cfg.getEnum("Type", AnimationType.class);
            if (animationType == null) {
                continue;
            }
            try {
                ICrateAnimation crateAnimation = this.getCrateAnimation(animationType, cfg);
                this.getAnimationsMap().put(crateAnimation.getId(), crateAnimation);
            }
            catch (Exception e) {
                this.plugin.error("Could not load animation config: '" + cfg.getFile().getName() + "' !");
                e.printStackTrace();
            }
        }

        this.plugin.info("Loaded " + this.getAnimationsMap().size() + " animation configs.");
    }

    @Override
    protected void onShutdown() {
        if (this.animations != null) {
            this.animations.values().forEach(ICrateAnimation::clear);
            this.animations.clear();
        }
    }

    @NotNull
    private ICrateAnimation getCrateAnimation(@NotNull AnimationType animationType, @NotNull JYML cfg) {
        return switch (animationType) {
            case SPIN -> new CrateSpinAnimation(this.plugin, cfg);
            case CLICK_OPEN -> new CrateHiddenAnimation(this.plugin, cfg);
        };
    }

    @NotNull
    public Map<String, ICrateAnimation> getAnimationsMap() {
        return this.animations;
    }

    public boolean isAnimation(@NotNull String id) {
        return this.getAnimationById(id) != null;
    }

    @Nullable
    public ICrateAnimation getAnimationById(@NotNull String id) {
        return this.getAnimationsMap().get(id.toLowerCase());
    }

    @NotNull
    public Collection<ICrateAnimation> getAnimations() {
        return this.getAnimationsMap().values();
    }

    @NotNull
    public List<String> getAnimationIds() {
        return new ArrayList<>(this.getAnimationsMap().keySet());
    }
}
