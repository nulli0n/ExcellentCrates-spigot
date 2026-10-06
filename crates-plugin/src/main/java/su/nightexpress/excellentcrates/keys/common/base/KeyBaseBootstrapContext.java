package su.nightexpress.excellentcrates.keys.common.base;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bootstrap.context.NamedBootstrapContext;
import su.nightexpress.engine.dispatcher.MessageDispatcher;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.key.editor.KeyEditorExtension;
import su.nightexpress.excellentcrates.api.key.registry.KeyRegistry;
import su.nightexpress.excellentcrates.keys.common.base.editor.KeyBaseEditorContext;
import su.nightexpress.excellentcrates.keys.common.base.lang.KeyBaseLang;

@NullMarked
public class KeyBaseBootstrapContext extends NamedBootstrapContext {

    private static final Identifier ID   = new Identifier("keys.base");
    private static final String     NAME = "Base";

    private final KeyEditorExtension editorExtension;

    public KeyBaseBootstrapContext(CratesPlugin plugin,
                                   MessageDispatcher dispatcher,
                                   CoreUIService coreUI,
                                   KeyRegistry keyRegistry) {
        super(ID, NAME);

        plugin.injectLang(KeyBaseLang.class);

        KeyBaseEditorContext editorContext = new KeyBaseEditorContext(plugin, dispatcher, coreUI, keyRegistry);
        this.editorExtension = editorContext.editorExtension;

        this.addComponent(editorContext);
    }

    public KeyEditorExtension getEditorExtension() {
        return this.editorExtension;
    }
}
