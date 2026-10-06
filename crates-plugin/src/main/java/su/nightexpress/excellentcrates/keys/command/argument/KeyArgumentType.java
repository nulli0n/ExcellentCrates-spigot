package su.nightexpress.excellentcrates.keys.command.argument;

import java.util.Comparator;
import java.util.List;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.id.IdentifierParser;
import su.nightexpress.excellentcrates.api.key.CrateKey;
import su.nightexpress.excellentcrates.api.key.registry.KeyResolver;
import su.nightexpress.excellentcrates.core.lang.Lang;
import su.nightexpress.excellentcrates.keys.lang.KeyLang;
import su.nightexpress.nightcore.commands.SuggestionsProvider;
import su.nightexpress.nightcore.commands.argument.ArgumentReader;
import su.nightexpress.nightcore.commands.argument.ArgumentType;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.context.CommandContextBuilder;
import su.nightexpress.nightcore.commands.exceptions.CommandSyntaxException;

@NullMarked
public class KeyArgumentType implements ArgumentType<CrateKey>, SuggestionsProvider {

    private final KeyResolver resolver;

    public KeyArgumentType(KeyResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public CrateKey parse(CommandContextBuilder contextBuilder, String string) throws CommandSyntaxException {
        Identifier id = IdentifierParser.parse(string)
            .orElseThrow(() -> new CommandSyntaxException(Lang.COMMAND_SYNTAX_INVALID_ID, string));

        CrateKey key = this.resolver.resolveKey(id);
        if (key == null) {
            throw new CommandSyntaxException(KeyLang.COMMAND_SYNTAX_INVALID_KEY, string);
        }

        return key;
    }

    @Override
    public List<String> suggest(ArgumentReader reader, CommandContext context) {
        return this.resolver.keyValues().stream()
            .map(CrateKey::id)
            .map(Identifier::toString)
            .sorted(Comparator.comparing(String::toString))
            .toList();
    }
}
