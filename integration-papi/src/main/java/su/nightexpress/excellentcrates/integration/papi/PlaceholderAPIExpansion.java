package su.nightexpress.excellentcrates.integration.papi;

import java.util.List;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import su.nightexpress.engine.placeholder.PlaceholderAPIResolver;
import su.nightexpress.engine.placeholder.PlaceholderAPIResult;
import su.nightexpress.excellentcrates.api.CratesPlugin;

public class PlaceholderAPIExpansion extends PlaceholderExpansion {

    //private final CratesPlugin                 plugin;
    private final List<PlaceholderAPIResolver> resolvers;

    public PlaceholderAPIExpansion(CratesPlugin plugin, List<PlaceholderAPIResolver> resolvers) {
        super();
        //this.plugin = plugin;
        this.resolvers = resolvers;
    }

    @Override
    public @NotNull String getAuthor() {
        return "NightExpress";
    }

    @Override
    public @NotNull String getIdentifier() {
        return "excellentcrates";
    }

    @Override
    public @NotNull String getVersion() {
        return "7.0.0";
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        for (PlaceholderAPIResolver resolver : this.resolvers) {
            PlaceholderAPIResult result = resolver.handleRequest(player, params);
            if (result != null) {
                return result.payload();
            }
        }
        return null;
    }
}
