package su.nightexpress.excellentcrates.keys.display;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bootstrap.context.NamedBootstrapContext;
import su.nightexpress.engine.dispatcher.MessageDispatcher;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.key.editor.KeyEditorExtension;
import su.nightexpress.excellentcrates.api.key.registry.KeyRegistry;
import su.nightexpress.excellentcrates.keys.display.editor.KeyDisplayEditorBootstrapContext;
import su.nightexpress.excellentcrates.keys.display.lang.KeyDisplayLang;

@NullMarked
public class KeyDisplayBootstrapContext extends NamedBootstrapContext {

    private static final Identifier ID   = new Identifier("keys.display");
    private static final String     NAME = "Display";

    private final KeyEditorExtension editorExtension;

    public KeyDisplayBootstrapContext(CratesPlugin plugin,
                                      CoreUIService coreUI,
                                      MessageDispatcher dispatcher,
                                      KeyRegistry keyRegistry) {
        super(ID, NAME);
        plugin.injectLang(KeyDisplayLang.class);

        KeyDisplayEditorBootstrapContext editorBootstrapContext = new KeyDisplayEditorBootstrapContext(
            plugin, coreUI, dispatcher, keyRegistry
        );

        this.editorExtension = editorBootstrapContext.extension;

        this.addComponent(editorBootstrapContext);
    }

    public KeyEditorExtension getEditorExtension() {
        return this.editorExtension;
    }
}
