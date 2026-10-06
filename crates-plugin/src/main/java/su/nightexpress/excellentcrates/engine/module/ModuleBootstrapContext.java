package su.nightexpress.excellentcrates.engine.module;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.ModuleBootstrap;
import su.nightexpress.excellentcrates.animation.AnimationModuleBootstrap;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.cost.CostModuleBootstrap;
import su.nightexpress.excellentcrates.crates.CratesModuleBootstrap;
import su.nightexpress.excellentcrates.effect.EffectsModuleBootstrap;
import su.nightexpress.excellentcrates.keys.KeysModuleBootstrap;
import su.nightexpress.excellentcrates.preview.PreviewModuleBootstrap;
import su.nightexpress.excellentcrates.rarity.RarityModuleBootstrap;
import su.nightexpress.excellentcrates.reward.RewardsModuleBootstrap;

@NullMarked
public class ModuleBootstrapContext {

    private static final String SETTINGS_FILE_NAME = "engine.modules.yml";

    private final List<ModuleBootstrap> bootstraps;

    public ModuleBootstrapContext(CratesPlugin plugin) {
        Path settingsPath = plugin.configPath().resolve(SETTINGS_FILE_NAME);
        ModuleSettings settings = ModuleSettings.loadFrom(settingsPath);

        this.bootstraps = new ArrayList<>();

        this.bootstraps.add(new CratesModuleBootstrap());

        if (settings.rarityEnabled()) {
            this.bootstraps.add(new RarityModuleBootstrap());
        }
        if (settings.animationsEnabled()) {
            this.bootstraps.add(new AnimationModuleBootstrap());
        }
        if (settings.costEnabled()) {
            this.bootstraps.add(new CostModuleBootstrap());
        }
        if (settings.effectsEnabled()) {
            this.bootstraps.add(new EffectsModuleBootstrap());
        }
        if (settings.keysEnabled()) {
            this.bootstraps.add(new KeysModuleBootstrap());
        }
        if (settings.previewEnabled()) {
            this.bootstraps.add(new PreviewModuleBootstrap());
        }
        if (settings.rewardsEnabled()) {
            this.bootstraps.add(new RewardsModuleBootstrap());
        }
    }

    public List<ModuleBootstrap> getBootstraps() {
        return this.bootstraps;
    }
}
