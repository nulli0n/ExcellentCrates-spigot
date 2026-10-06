package su.nightexpress.excellentcrates.keys.display;

import java.util.concurrent.TimeUnit;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bootstrap.context.NamedBootstrapContext;
import su.nightexpress.engine.dispatcher.MessageDispatcher;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.settings.ReadOnlySettings;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.key.editor.KeyEditorExtension;
import su.nightexpress.excellentcrates.api.key.registry.KeyRegistry;
import su.nightexpress.excellentcrates.keys.config.settings.KeyCoreSettings;
import su.nightexpress.excellentcrates.keys.display.editor.KeyDisplayEditorBootstrapContext;
import su.nightexpress.excellentcrates.keys.display.lang.KeyDisplayLang;
import su.nightexpress.excellentcrates.keys.display.placeholder.KeyDisplayPlaceholder;

@NullMarked
public class KeyDisplayBootstrapContext extends NamedBootstrapContext {

    private static final Identifier ID   = new Identifier("keys.display");
    private static final String     NAME = "Display";

    public final KeyDisplayResolver resolver;

    private final KeyEditorExtension    editorExtension;
    private final KeyDisplayPlaceholder placeholder;

    public KeyDisplayBootstrapContext(CratesPlugin plugin,
                                      CoreUIService coreUI,
                                      MessageDispatcher dispatcher,
                                      KeyRegistry keyRegistry,
                                      ReadOnlySettings<KeyCoreSettings> settings) {
        super(ID, NAME);
        plugin.injectLang(KeyDisplayLang.class);

        int cacheTTL = settings.get().displayCacheTTL();
        this.resolver = new KeyDisplayResolver(cacheTTL, TimeUnit.MINUTES);

        KeyDisplayEditorBootstrapContext editorBootstrapContext = new KeyDisplayEditorBootstrapContext(
            plugin, coreUI, dispatcher, keyRegistry
        );

        this.editorExtension = editorBootstrapContext.extension;
        this.placeholder = new KeyDisplayPlaceholder(this.resolver);

        this.addComponent(editorBootstrapContext);
    }

    public KeyEditorExtension getEditorExtension() {
        return this.editorExtension;
    }

    public KeyDisplayPlaceholder getPlaceholder() {
        return this.placeholder;
    }
}
