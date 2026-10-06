package su.nightexpress.excellentcrates.keys.placeholder;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.id.IdentifierParser;
import su.nightexpress.engine.placeholder.PlaceholderAPIResolver;
import su.nightexpress.engine.placeholder.PlaceholderAPIResult;
import su.nightexpress.excellentcrates.api.key.CrateKey;
import su.nightexpress.excellentcrates.api.key.placeholder.KeyPlaceholders;
import su.nightexpress.excellentcrates.api.key.registry.KeyResolver;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext;

@NullMarked
public class KeyPlaceholderAPIResolver implements PlaceholderAPIResolver {

    private static final String PREFIX = "key_";

    private final KeyResolver     resolver;
    private final KeyPlaceholders placeholders;

    public KeyPlaceholderAPIResolver(KeyResolver resolver, KeyPlaceholders placeholders) {
        this.resolver = resolver;
        this.placeholders = placeholders;
    }

    @Override
    public @Nullable PlaceholderAPIResult handleRequest(@Nullable Player player, String identifier) {
        if (!identifier.startsWith(PREFIX)) {
            return null;
        }

        // Separate the key ID from the rest of the identifier
        int lastUnderscore = identifier.lastIndexOf('_');

        // If the last underscore is at or before the prefix, the format is invalid
        if (lastUnderscore <= PREFIX.length() - 1) {
            return null;
        }

        String keyId = identifier.substring(lastUnderscore + 1);
        Identifier id = IdentifierParser.parse(keyId).orElse(null);
        CrateKey key = id == null ? null : this.resolver.resolveKey(id);
        if (key == null) {
            return new PlaceholderAPIResult(null);
        }

        String cleanPlaceholder = "%" + identifier.substring(0, lastUnderscore) + "%";

        PlaceholderContext context = PlaceholderContext.builder()
            .apply(this.placeholders.allPlaceholders(key, player))
            .build();

        String result = context.apply(cleanPlaceholder);

        return new PlaceholderAPIResult(result);
    }

}
