package su.nightexpress.excellentcrates.crates.item;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bootstrap.context.NamedBootstrapContext;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.command.CrateCommand;
import su.nightexpress.excellentcrates.api.crate.dispatcher.CrateMessageDispatcher;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorExtension;
import su.nightexpress.excellentcrates.api.crate.item.CrateItemAPI;
import su.nightexpress.excellentcrates.api.crate.registry.CrateRegistry;
import su.nightexpress.excellentcrates.core.settings.SettingsController;
import su.nightexpress.excellentcrates.core.settings.SettingsProvider;
import su.nightexpress.excellentcrates.crates.interact.CrateInteractionService;
import su.nightexpress.excellentcrates.crates.item.api.StandardCrateItemAPI;
import su.nightexpress.excellentcrates.crates.item.command.CrateItemDropCommand;
import su.nightexpress.excellentcrates.crates.item.command.CrateItemGetCommand;
import su.nightexpress.excellentcrates.crates.item.command.CrateItemGiveCommand;
import su.nightexpress.excellentcrates.crates.item.editor.CrateItemEditorBootstrapContext;
import su.nightexpress.excellentcrates.crates.item.factory.CrateItemFactory;
import su.nightexpress.excellentcrates.crates.item.interact.CrateItemInteractionHandler;
import su.nightexpress.excellentcrates.crates.item.interact.controller.CrateItemInteractionController;
import su.nightexpress.excellentcrates.crates.item.lang.CrateItemLang;
import su.nightexpress.excellentcrates.crates.item.permission.CrateItemPerms;
import su.nightexpress.excellentcrates.crates.item.pipeline.CrateItemTakeStage;
import su.nightexpress.excellentcrates.crates.item.pipeline.CrateItemValidationStage;
import su.nightexpress.excellentcrates.crates.item.settings.ItemSettings;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;

@NullMarked
public class CrateItemBootstrapContext extends NamedBootstrapContext {

    private static final Identifier ID   = new Identifier("crates.item");
    private static final String     NAME = "Item";

    private static final String SETTINGS_FILE_NAME = "crates.item.yml";
    private static final String ITEM_KEY_CRATE_ID  = "crate_id";

    public final CrateItemFactory itemFactory;
    public final CrateItemService itemService;
    public final CrateItemAPI     api;

    private final CrateEditorExtension     editorExtension;
    private final CrateItemValidationStage validationStage;
    private final CrateItemTakeStage       takeStage;
    private final List<CrateCommand>       commands;

    public CrateItemBootstrapContext(CratesPlugin plugin,
                                     CoreUIService coreUI,
                                     CrateMessageDispatcher dispatcher,
                                     CrateRegistry crates,
                                     CrateInteractionService interactionService) {
        super(ID, NAME);
        this.commands = new ArrayList<>();

        plugin.injectLang(CrateItemLang.class);
        plugin.registerPermissions(CrateItemPerms.ROOT);

        Path settingsPath = plugin.configPath().resolve(SETTINGS_FILE_NAME);
        SettingsProvider<ItemSettings> settings = new SettingsProvider<>(ItemSettings.defaults());

        AdaptedKey itemKey = plugin.keyDomain().make(ITEM_KEY_CRATE_ID);

        this.itemFactory = new CrateItemFactory();
        this.itemService = new CrateItemService(this.itemFactory, itemKey);
        this.api = new StandardCrateItemAPI(this.itemService);

        CrateItemEditorBootstrapContext editorContext = new CrateItemEditorBootstrapContext(
            plugin, coreUI, dispatcher, crates, itemFactory
        );

        CrateItemInteractionHandler handler = new CrateItemInteractionHandler(
            settings, crates, itemService, interactionService, dispatcher
        );

        this.addComponent(new SettingsController<>(settingsPath, ItemSettings::loadFrom, settings));
        this.addComponent(editorContext);
        this.addComponent(new CrateItemInteractionController(plugin, itemService, handler));

        this.editorExtension = editorContext.extension;

        this.validationStage = new CrateItemValidationStage(itemService, dispatcher);
        this.takeStage = new CrateItemTakeStage(itemService, dispatcher);

        this.commands.add(new CrateItemGetCommand(itemService, dispatcher));
        this.commands.add(new CrateItemGiveCommand(itemService, dispatcher));
        this.commands.add(new CrateItemDropCommand(itemService, dispatcher));
    }

    public CrateEditorExtension getEditorExtension() {
        return this.editorExtension;
    }

    public CrateItemValidationStage getValidationStage() {
        return this.validationStage;
    }

    public CrateItemTakeStage getTakeStage() {
        return this.takeStage;
    }

    public List<CrateCommand> getCommands() {
        return Collections.unmodifiableList(this.commands);
    }
}
