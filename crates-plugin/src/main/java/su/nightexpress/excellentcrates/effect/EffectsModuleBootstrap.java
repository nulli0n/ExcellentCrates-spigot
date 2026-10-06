package su.nightexpress.excellentcrates.effect;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.BaseModuleBootstrap;
import su.nightexpress.engine.component.ComponentBundle;
import su.nightexpress.engine.component.CoreDependencies;
import su.nightexpress.engine.service.ServiceRegistry;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CoreServices;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.dispatcher.CrateMessageDispatcher;
import su.nightexpress.excellentcrates.api.crate.registry.CrateRegistry;
import su.nightexpress.excellentcrates.api.effect.EffectRegistry;
import su.nightexpress.excellentcrates.effect.crate.component.EffectComponentBootstrapContext;
import su.nightexpress.excellentcrates.effect.crate.editor.EffectComponentEditorBootstrapContext;
import su.nightexpress.excellentcrates.effect.data.EffectDataBootstrapContext;
import su.nightexpress.excellentcrates.effect.instance.EffectInstanceBootstrapContext;
import su.nightexpress.excellentcrates.effect.lang.EffectsLang;
import su.nightexpress.excellentcrates.effect.model.EffectModelBootstrapContext;
import su.nightexpress.excellentcrates.effect.registry.DefaultEffectRegistry;

@NullMarked
public class EffectsModuleBootstrap extends BaseModuleBootstrap {

    @Override
    public void onRegister(ServiceRegistry services, CoreDependencies dependencies) {
        CratesPlugin plugin = dependencies.plugin();
        CoreUIService coreUI = dependencies.uiService();
        CrateMessageDispatcher dispatcher = dependencies.dispatcher();

        CrateRegistry crateRegistry = dependencies.crateRegistry();
        EffectRegistry effectRegistry = new DefaultEffectRegistry();

        plugin.injectLang(EffectsLang.class);

        // Initialize effect data context

        EffectDataBootstrapContext dataContext = new EffectDataBootstrapContext(plugin, effectRegistry);
        this.registerComponent(dataContext);

        // Initialize effect component context

        EffectComponentBootstrapContext componentContext = new EffectComponentBootstrapContext();
        this.registerComponent(componentContext);

        // Initialize effect component editor context

        EffectComponentEditorBootstrapContext componentEditorContext = new EffectComponentEditorBootstrapContext(
            plugin, coreUI, dispatcher, crateRegistry, effectRegistry
        );
        this.registerComponent(componentEditorContext);

        // Initialize effect instance context

        EffectInstanceBootstrapContext instanceContext = new EffectInstanceBootstrapContext(
            plugin, crateRegistry, effectRegistry
        );
        this.registerComponent(instanceContext);

        // Initialize effect model context

        EffectModelBootstrapContext modelContext = new EffectModelBootstrapContext();
        modelContext.getModels().forEach(effectRegistry::registerModel);

        this.bridge.requireAvailable(CoreServices.CRATES, cratesApi -> {
            cratesApi.data().registerExtension(componentContext.getDataExtension());
            cratesApi.editor().registerExtension(componentEditorContext.getEditorExtension());
            cratesApi.blocks().ifPresent(blocksApi -> {
                blocksApi.addObserver(instanceContext.getPositionObserver());
            });
        });
    }

    @Override
    protected ComponentBundle buildComponent(ServiceRegistry services, CoreDependencies dependencies) {
        return new EffectsModule();
    }
}
