package su.nightexpress.excellentcrates.preview;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.BaseModuleBootstrap;
import su.nightexpress.engine.component.ComponentBundle;
import su.nightexpress.engine.component.CoreDependencies;
import su.nightexpress.engine.service.ServiceRegistry;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CoreServices;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.dispatcher.CrateMessageDispatcher;
import su.nightexpress.excellentcrates.api.crate.placeholder.CratePlaceholders;
import su.nightexpress.excellentcrates.api.crate.registry.CrateRegistry;
import su.nightexpress.excellentcrates.api.preview.PreviewRegistry;
import su.nightexpress.excellentcrates.preview.crate.command.CratePreviewCommand;
import su.nightexpress.excellentcrates.preview.crate.component.PreviewComponentBootstrapContext;
import su.nightexpress.excellentcrates.preview.crate.editor.PreviewComponentEditorBootstrapContext;
import su.nightexpress.excellentcrates.preview.crate.interact.PreviewInteractAction;
import su.nightexpress.excellentcrates.preview.data.PreviewDataBootstrapContext;
import su.nightexpress.excellentcrates.preview.inventory.InventoryPreviewBootstrapContext;
import su.nightexpress.excellentcrates.preview.lang.PreviewLang;
import su.nightexpress.excellentcrates.preview.permission.PreviewPerms;
import su.nightexpress.excellentcrates.preview.view.PreviewViewService;

@NullMarked
public final class PreviewModuleBootstrap extends BaseModuleBootstrap {

    @Override
    public void onRegister(ServiceRegistry services, CoreDependencies dependencies) {
        CratesPlugin plugin = dependencies.plugin();
        CoreUIService coreUI = dependencies.uiService();
        CrateMessageDispatcher dispatcher = dependencies.dispatcher();

        CrateRegistry crates = dependencies.crateRegistry();
        CratePlaceholders cratePlaceholders = dependencies.cratePlaceholders();
        PreviewRegistry previewRegistry = new DefaultPreviewRegistry();

        plugin.injectLang(PreviewLang.class);
        plugin.registerPermissions(PreviewPerms.ROOT);

        // =====================
        // Initialize Preview Component
        // =====================

        PreviewComponentBootstrapContext componentContext = new PreviewComponentBootstrapContext();
        PreviewComponentEditorBootstrapContext componentEditorContext = new PreviewComponentEditorBootstrapContext(
            plugin, coreUI, dispatcher, crates, previewRegistry
        );
        this.registerComponent(componentEditorContext);

        // =====================
        // Initialize preview data context
        // =====================

        PreviewDataBootstrapContext dataContext = new PreviewDataBootstrapContext(plugin, previewRegistry);
        this.registerComponent(dataContext);

        // =====================
        // Initialize View Service
        // =====================

        PreviewViewService viewService = new PreviewViewService(previewRegistry);

        this.bridge.requireAvailable(CoreServices.CRATES, cratesApi -> {
            cratesApi.data().registerExtension(componentContext.getDataExtension());
            cratesApi.editor().registerExtension(componentEditorContext.getEditorExtension());
            cratesApi.commands().registerCommand(new CratePreviewCommand(viewService, dispatcher));
            cratesApi.interaction().registerAction(new PreviewInteractAction(viewService, dispatcher));
        });

        this.bridge.onAvailable(CoreServices.REWARDS, rewardsApi -> {
            InventoryPreviewBootstrapContext inventoryPreviewContext = new InventoryPreviewBootstrapContext(
                plugin, crates, cratePlaceholders, rewardsApi
            );
            previewRegistry.registerProvider(inventoryPreviewContext.getProvider());
        });

        services.register(CoreServices.PREVIEW, new DefaultPreviewAPI(previewRegistry, viewService));
    }

    @Override
    protected ComponentBundle buildComponent(ServiceRegistry services, CoreDependencies dependencies) {
        return new PreviewModule();
    }
}
