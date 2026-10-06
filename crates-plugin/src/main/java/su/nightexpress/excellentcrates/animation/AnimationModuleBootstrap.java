package su.nightexpress.excellentcrates.animation;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.BaseModuleBootstrap;
import su.nightexpress.engine.component.ComponentBundle;
import su.nightexpress.engine.component.CoreDependencies;
import su.nightexpress.engine.service.ServiceRegistry;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.animation.component.AnimationComponentBootstrapContext;
import su.nightexpress.excellentcrates.animation.component.editor.AnimationComponentEditorBootstrapContext;
import su.nightexpress.excellentcrates.animation.data.AnimationDataBootstrapContext;
import su.nightexpress.excellentcrates.animation.lang.AnimationsLang;
import su.nightexpress.excellentcrates.animation.pipeline.AnimationProfilePipelineStage;
import su.nightexpress.excellentcrates.animation.pipeline.AnimationPipelineProcessor;
import su.nightexpress.excellentcrates.animation.session.AnimationSessionController;
import su.nightexpress.excellentcrates.animation.session.AnimationSessionManager;
import su.nightexpress.excellentcrates.animation.session.AnimationSessionService;
import su.nightexpress.excellentcrates.animation.style.AnimationTypeBootstrapContext;
import su.nightexpress.excellentcrates.api.CoreServices;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.animation.AnimationRegistry;
import su.nightexpress.excellentcrates.api.crate.dispatcher.CrateMessageDispatcher;
import su.nightexpress.excellentcrates.api.crate.pipeline.PipelinePhase;
import su.nightexpress.excellentcrates.api.crate.pipeline.PipelineStageOrder;
import su.nightexpress.excellentcrates.api.crate.registry.CrateRegistry;

@NullMarked
public class AnimationModuleBootstrap extends BaseModuleBootstrap {

    @Override
    public void onRegister(ServiceRegistry services, CoreDependencies dependencies) {
        CratesPlugin plugin = dependencies.plugin();
        CoreUIService coreUI = dependencies.uiService();
        CrateMessageDispatcher dispatcher = dependencies.dispatcher();

        CrateRegistry crates = dependencies.crateRegistry();
        AnimationRegistry animations = new DefaultAnimationRegistry();

        plugin.injectLang(AnimationsLang.class);

        // -------------------------------
        // Data & IO Bootstrap Context
        // -------------------------------

        AnimationDataBootstrapContext dataContext = new AnimationDataBootstrapContext(plugin, animations);
        this.registerComponent(dataContext);

        // -------------------------------
        // Animation Variant Bootstrap Context
        // -------------------------------

        AnimationTypeBootstrapContext variantContext = new AnimationTypeBootstrapContext(animations);
        this.registerComponent(variantContext);

        // -------------------------------
        // Animation Component Bootstrap Context
        // -------------------------------

        AnimationComponentBootstrapContext componentContext = new AnimationComponentBootstrapContext();
        AnimationComponentEditorBootstrapContext editorContext = new AnimationComponentEditorBootstrapContext(
            plugin, coreUI, dispatcher, crates, animations
        );

        this.registerComponent(editorContext);

        // -------------------------------
        // Animation Session Management
        // -------------------------------

        AnimationSessionManager sessionManager = new AnimationSessionManager();
        AnimationSessionService sessionService = new AnimationSessionService(sessionManager);
        AnimationSessionController sessionController = new AnimationSessionController(plugin, sessionManager);
        this.registerComponent(sessionController);

        // -------------------------------
        // Animation Play Service Registration
        // -------------------------------

        this.bridge.requireAvailable(CoreServices.CRATES, cratesApi -> {
            cratesApi.data().registerExtension(componentContext.getDataExtension());
            cratesApi.editor().registerExtension(editorContext.getEditorExtension());

            this.bridge.onAvailable(CoreServices.REWARDS, rewardsApi -> {
                AnimationPlayService playService = new AnimationPlayService(
                    animations, sessionService, rewardsApi.getView()
                );

                cratesApi.pipeline().registerStage(
                    PipelinePhase.PRE_ROLL,
                    PipelineStageOrder.PRE_ROLL_ANIMATION,
                    new AnimationProfilePipelineStage(animations, sessionService, dispatcher)
                );
                cratesApi.pipeline().registerProcessor(
                    new AnimationPipelineProcessor(animations, playService, dispatcher)
                );
            });
        });
    }

    @Override
    protected ComponentBundle buildComponent(ServiceRegistry services, CoreDependencies dependencies) {
        return new AnimationModule();
    }
}
