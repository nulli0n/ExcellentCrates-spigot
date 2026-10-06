package su.nightexpress.excellentcrates.crates.command.argument;

import java.util.List;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.id.IdentifierParser;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.registry.CrateResolver;
import su.nightexpress.excellentcrates.core.lang.Lang;
import su.nightexpress.excellentcrates.crates.lang.CratesLang;
import su.nightexpress.nightcore.commands.SuggestionsProvider;
import su.nightexpress.nightcore.commands.argument.ArgumentReader;
import su.nightexpress.nightcore.commands.argument.ArgumentType;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.context.CommandContextBuilder;
import su.nightexpress.nightcore.commands.exceptions.CommandSyntaxException;

@NullMarked
public class CrateArgumentType implements ArgumentType<Crate>, SuggestionsProvider {

    private final CrateResolver resolver;

    public CrateArgumentType(CrateResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public Crate parse(CommandContextBuilder contextBuilder, String string) throws CommandSyntaxException {
        Identifier id = IdentifierParser.parseSanitized(string)
            .orElseThrow(() -> new CommandSyntaxException(Lang.COMMAND_SYNTAX_INVALID_ID, string));

        Crate crate = this.resolver.resolveCrate(id);
        if (crate == null) {
            throw new CommandSyntaxException(CratesLang.COMMAND_SYNTAX_INVALID_CRATE_ARGUMENT, string);
        }

        return crate;
    }

    @Override
    public List<String> suggest(ArgumentReader reader, CommandContext context) {
        return this.resolver.crates().stream().map(Crate::idString).toList();
    }
}
