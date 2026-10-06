package su.nightexpress.excellentcrates.crates.placeholder;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.id.IdentifierParser;
import su.nightexpress.engine.placeholder.PlaceholderAPIResolver;
import su.nightexpress.engine.placeholder.PlaceholderAPIResult;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.placeholder.CratePlaceholders;
import su.nightexpress.excellentcrates.api.crate.registry.CrateResolver;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext;

@NullMarked
public class CratePlaceholderAPIResolver implements PlaceholderAPIResolver {

    private static final String PREFIX = "crate_";

    private final CrateResolver     crateResolver;
    private final CratePlaceholders placeholders;

    public CratePlaceholderAPIResolver(CrateResolver crateResolver, CratePlaceholders placeholders) {
        this.crateResolver = crateResolver;
        this.placeholders = placeholders;
    }

    @Override
    public @Nullable PlaceholderAPIResult handleRequest(@Nullable Player player, String identifier) {
        if (!identifier.startsWith(PREFIX)) {
            return null;
        }

        // Separate the crate ID from the rest of the identifier
        int lastUnderscore = identifier.lastIndexOf('_');

        // If the last underscore is at or before the prefix, the format is invalid
        if (lastUnderscore <= PREFIX.length() - 1) {
            return null;
        }

        String crateId = identifier.substring(lastUnderscore + 1);
        Identifier id = IdentifierParser.parse(crateId).orElse(null);
        Crate crate = id == null ? null : this.crateResolver.resolveCrate(id);
        if (crate == null) {
            return new PlaceholderAPIResult(null);
        }

        String cleanPlaceholder = "%" + identifier.substring(0, lastUnderscore) + "%";

        PlaceholderContext context = PlaceholderContext.builder()
            .apply(this.placeholders.allPlaceholders(crate, player))
            .build();

        String result = context.apply(cleanPlaceholder);

        return new PlaceholderAPIResult(result);
    }
}
