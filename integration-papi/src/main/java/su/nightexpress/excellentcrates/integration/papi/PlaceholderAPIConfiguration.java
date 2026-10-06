package su.nightexpress.excellentcrates.integration.papi;

import java.util.List;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.NamedComponentBundle;
import su.nightexpress.engine.component.PluginComponent;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.placeholder.PlaceholderAPIResolver;
import su.nightexpress.excellentcrates.api.CratesPlugin;

@NullMarked
public final class PlaceholderAPIConfiguration {

    private static final Identifier ID   = new Identifier("placeholderapi");
    private static final String     NAME = "PlaceholderAPI Integration";

    public static PluginComponent configure(CratesPlugin plugin, List<PlaceholderAPIResolver> resolvers) {
        NamedComponentBundle bundle = new NamedComponentBundle(ID, NAME);

        PlaceholderAPIExpansion expansion = new PlaceholderAPIExpansion(plugin, resolvers);

        bundle.addComponent(new PlaceholderAPIController(expansion));

        return bundle;
    }

    private PlaceholderAPIConfiguration() {
    }
}
