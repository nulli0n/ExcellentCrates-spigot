package su.nightexpress.excellentcrates.crates.cooldown.component.extension;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.component.CrateComponentKeys;
import su.nightexpress.excellentcrates.api.crate.data.extension.CrateDataExtension;
import su.nightexpress.excellentcrates.api.crate.data.model.ICrateBuilder;
import su.nightexpress.excellentcrates.crates.cooldown.component.StandardCrateCooldownComponent;
import su.nightexpress.excellentcrates.crates.cooldown.db.CrateCooldownCachedDataService;
import su.nightexpress.nightcore.config.FileConfig;

@NullMarked
public class CrateCooldownsLifecycleExtension implements CrateDataExtension {

    private final CrateCooldownCachedDataService cachedDataService;

    public CrateCooldownsLifecycleExtension(CrateCooldownCachedDataService cachedDataService) {
        this.cachedDataService = cachedDataService;
    }

    @Override
    public void onBuild(ICrateBuilder builder, Identifier crateId) {
        builder.component(CrateComponentKeys.COOLDOWN, StandardCrateCooldownComponent.createDefault());
    }

    @Override
    public void onCreate(Crate crate) {

    }

    @Override
    public void onDelete(Crate crate) {
        this.cachedDataService.markAllRemovedByKey(crate.id());
    }

    @Override
    public void onLoad(Crate crate) {

    }

    @Override
    public void onRead(FileConfig config, ICrateBuilder builder, Identifier crateId) {
        StandardCrateCooldownComponent cooldowns = config.getOrSet("cooldowns", StandardCrateCooldownComponent.class,
            StandardCrateCooldownComponent.createDefault());

        builder.component(CrateComponentKeys.COOLDOWN, cooldowns);
    }

    @Override
    public void onUnload(Crate crate) {

    }

    @Override
    public void onWrite(FileConfig config, Crate crate) {
        config.set("cooldowns", crate.getComponentOrNull(CrateComponentKeys.COOLDOWN));
    }
}
