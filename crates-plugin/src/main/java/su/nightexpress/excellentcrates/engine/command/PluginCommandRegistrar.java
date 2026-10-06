package su.nightexpress.excellentcrates.engine.command;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.component.BasePluginComponent;
import su.nightexpress.engine.component.ComponentTreeFormatter;
import su.nightexpress.engine.component.PluginComponent;
import su.nightexpress.engine.dispatcher.MessageDispatcher;
import su.nightexpress.engine.settings.ReadOnlySettings;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.core.lang.Lang;
import su.nightexpress.excellentcrates.core.permission.Perms;
import su.nightexpress.excellentcrates.engine.config.PluginSettings;
import su.nightexpress.nightcore.commands.Commands;
import su.nightexpress.nightcore.commands.command.NightCommand;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.util.Players;

@NullMarked
public class PluginCommandRegistrar extends BasePluginComponent {

    private final CratesPlugin                     plugin;
    private final PluginComponent                  core;
    private final ReadOnlySettings<PluginSettings> settings;
    private final MessageDispatcher                dispatcher;

    private @Nullable NightCommand rootCommand;

    public PluginCommandRegistrar(CratesPlugin plugin,
                                  PluginComponent core,
                                  ReadOnlySettings<PluginSettings> settings,
                                  MessageDispatcher dispatcher) {
        super();
        this.plugin = plugin;
        this.core = core;
        this.settings = settings;
        this.dispatcher = dispatcher;
    }

    @Override
    protected void onReload() {

    }

    @Override
    protected void onShutdown() {
        if (this.rootCommand != null) {
            this.rootCommand.unregister();
        }
    }

    @Override
    protected void onStart() {
        this.createRootCommand();
    }

    private void createRootCommand() {
        this.rootCommand = NightCommand.hub(this.plugin, this.settings.get().commandAliases(), builder -> {
            builder.description(Lang.PLUGIN_COMMAND_ROOT_DESCRIPTION);

            builder.branch(Commands.literal("status")
                .description(Lang.PLUGIN_COMMAND_STATUS_DESCRIPTION)
                .permission(Perms.COMMAND_STATUS)
                .executes((context, arguments) -> {
                    Players.sendMessage(context.getSender(), ComponentTreeFormatter.buildTree(this.core));
                    return true;
                })
            );

            builder.branch(Commands.literal("reload")
                .description(Lang.PLUGIN_COMMAND_RELOAD_DESCRIPTION)
                .permission(Perms.COMMAND_RELOAD)
                .executes((context, arguments) -> {
                    this.plugin.reload();
                    this.dispatcher.send(context.getSender(), CoreLang.PLUGIN_RELOADED);
                    return true;
                })
            );
        });
        this.rootCommand.register();
    }
}
