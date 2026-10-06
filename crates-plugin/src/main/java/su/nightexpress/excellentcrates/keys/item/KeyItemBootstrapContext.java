package su.nightexpress.excellentcrates.keys.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.NamespacedKey;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bootstrap.context.NamedBootstrapContext;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.key.command.KeyCommand;
import su.nightexpress.excellentcrates.api.key.dispatcher.KeyMessageDispatcher;
import su.nightexpress.excellentcrates.api.key.editor.KeyEditorExtension;
import su.nightexpress.excellentcrates.api.key.item.KeyItemAPI;
import su.nightexpress.excellentcrates.api.key.placeholder.KeyPlaceholders;
import su.nightexpress.excellentcrates.api.key.registry.KeyRegistry;
import su.nightexpress.excellentcrates.keys.item.api.DefaultKeyItemAPI;
import su.nightexpress.excellentcrates.keys.item.command.KeyItemDropCommand;
import su.nightexpress.excellentcrates.keys.item.controller.KeyItemInteractionController;
import su.nightexpress.excellentcrates.keys.item.editor.KeyItemEditorBootstrapContext;
import su.nightexpress.excellentcrates.keys.item.lang.KeyItemLang;

@NullMarked
public class KeyItemBootstrapContext extends NamedBootstrapContext {

    private static final Identifier ID   = new Identifier("keys.item");
    private static final String     NAME = "Item";

    public final KeyItemFactory itemFactory;
    public final KeyItemService itemService;
    public final KeyItemAPI     api;

    private final KeyEditorExtension editorExtension;
    private final List<KeyCommand>   commands;

    public KeyItemBootstrapContext(CratesPlugin plugin,
                                   CoreUIService coreUI,
                                   KeyMessageDispatcher dispatcher,
                                   KeyRegistry keyRegistry,
                                   KeyPlaceholders keyPlaceholders) {
        super(ID, NAME);
        this.commands = new ArrayList<>();

        plugin.injectLang(KeyItemLang.class);

        NamespacedKey itemKey = new NamespacedKey(plugin, "key.id");

        this.itemFactory = new KeyItemFactory(keyPlaceholders);
        this.itemService = new KeyItemService(this.itemFactory, itemKey);
        this.api = new DefaultKeyItemAPI(this.itemFactory, this.itemService);

        KeyItemEditorBootstrapContext editorContext = new KeyItemEditorBootstrapContext(
            plugin, coreUI, dispatcher, keyRegistry, this.itemFactory
        );

        this.addComponent(editorContext);
        this.addComponent(new KeyItemInteractionController(plugin, itemService));

        this.editorExtension = editorContext.extension;

        this.commands.add(new KeyItemDropCommand(itemService, dispatcher));
    }

    public KeyEditorExtension getEditorExtension() {
        return this.editorExtension;
    }

    public List<KeyCommand> getCommands() {
        return Collections.unmodifiableList(this.commands);
    }
}
