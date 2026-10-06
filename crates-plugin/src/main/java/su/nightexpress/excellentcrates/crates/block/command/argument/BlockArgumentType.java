package su.nightexpress.excellentcrates.crates.block.command.argument;

import java.util.List;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.block.BlockRegistry;
import su.nightexpress.excellentcrates.api.crate.block.CrateBlock;
import su.nightexpress.excellentcrates.crates.block.lang.BlocksLang;
import su.nightexpress.nightcore.bridge.BukkitKeys;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;
import su.nightexpress.nightcore.commands.SuggestionsProvider;
import su.nightexpress.nightcore.commands.argument.ArgumentReader;
import su.nightexpress.nightcore.commands.argument.ArgumentType;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.context.CommandContextBuilder;
import su.nightexpress.nightcore.commands.exceptions.CommandSyntaxException;

@NullMarked
public class BlockArgumentType implements ArgumentType<CrateBlock>, SuggestionsProvider {

    private final BlockRegistry registry;

    public BlockArgumentType(BlockRegistry registry) {
        this.registry = registry;
    }

    @Override
    public CrateBlock parse(CommandContextBuilder contextBuilder, String string) throws CommandSyntaxException {
        AdaptedKey key = BukkitKeys.parse(string)
            .orElseThrow(() -> CommandSyntaxException.custom(BlocksLang.COMMAND_SYNTAX_INVALID_BLOCK_KEY));

        CrateBlock block = this.registry.getBlock(key);
        if (block == null) {
            throw CommandSyntaxException.custom(BlocksLang.COMMAND_SYNTAX_INVALID_BLOCK_ARGUMENT);
        }

        return block;
    }

    @Override
    public List<String> suggest(ArgumentReader reader, CommandContext context) {
        return this.registry.getBlockKeys().stream()
            .map(AdaptedKey::toString)
            .toList();
    }
}
