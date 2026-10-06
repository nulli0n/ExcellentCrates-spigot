package su.nightexpress.excellentcrates.cost.ui;

import java.nio.file.Path;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bootstrap.context.NamedBootstrapContext;
import su.nightexpress.engine.id.IdentifiableRegistry;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.cost.CostSelectionHandler;
import su.nightexpress.excellentcrates.api.cost.type.CostType;
import su.nightexpress.excellentcrates.api.crate.dispatcher.CrateMessageDispatcher;
import su.nightexpress.excellentcrates.api.crate.registry.CrateResolver;
import su.nightexpress.excellentcrates.cost.core.CostService;
import su.nightexpress.excellentcrates.cost.ui.controller.CostUIMenuRegistrar;

@NullMarked
public final class CostUIBootstrapContext extends NamedBootstrapContext {

    private static final Identifier ID   = new Identifier("cost.ui");
    private static final String     NAME = "UI";

    public final CostSelectionHandler selectionHandler;

    public CostUIBootstrapContext(CratesPlugin plugin,
                                  CoreUIService coreUI,
                                  CrateMessageDispatcher dispatcher,
                                  CrateResolver resolver,
                                  IdentifiableRegistry<CostType<?>> costTypes,
                                  CostService costService) {
        super(ID, NAME);

        Path menuDir = plugin.menuPath();

        CostUIService uiService = new CostUIService(coreUI);
        CostUIController uiController = new CostUIController(costTypes, costService, uiService, dispatcher);

        this.selectionHandler = uiController;

        this.addComponent(new CostUIMenuRegistrar(menuDir, plugin, coreUI, resolver, costTypes));
    }
}
