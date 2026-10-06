package su.nightexpress.excellentcrates.crates.command.controller;

import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.component.PluginComponent;
import su.nightexpress.engine.registry.TinyRegistry;
import su.nightexpress.engine.settings.ReadOnlySettings;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.command.CrateCommand;
import su.nightexpress.excellentcrates.core.crate.permission.CratePerms;
import su.nightexpress.excellentcrates.crates.config.settings.CrateCoreSettings;
import su.nightexpress.excellentcrates.crates.lang.CratesLang;
import su.nightexpress.nightcore.commands.command.NightCommand;

public class CrateCommandRegistrarController implements PluginComponent {

    private final CratesPlugin                        plugin;
    private final TinyRegistry<CrateCommand>          commands;
    private final ReadOnlySettings<CrateCoreSettings> settings;

    @Nullable
    private NightCommand command;

    public CrateCommandRegistrarController(CratesPlugin plugin,
                                           TinyRegistry<CrateCommand> commands,
                                           ReadOnlySettings<CrateCoreSettings> settings) {
        this.plugin = plugin;
        this.commands = commands;
        this.settings = settings;
    }

    @Override
    public void reload() {
        this.shutdown();
        this.start();
    }

    @Override
    public void shutdown() {
        if (this.command != null) {
            this.command.unregister();
        }
    }

    @Override
    public void start() {
        this.loadCommand();
    }

    @Override
    public boolean isRunning() {
        return true;
    }

    public void loadCommand() {
        this.command = NightCommand.hub(this.plugin, this.settings.get().commandAliases(), builder -> {
            builder.description(CratesLang.COMMAND_ROOT_DESCRIPTION);
            builder.permission(CratePerms.COMMAND_ROOT);

            this.commands.getEntries().forEach(extension -> {
                builder.branch(extension.createCommand());
            });
        });
        this.command.register();

        this.commands.lock();
    }
}
