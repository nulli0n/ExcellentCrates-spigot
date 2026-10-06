package su.nightexpress.excellentcrates.keys.command.controller;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.component.PluginComponent;
import su.nightexpress.engine.registry.TinyRegistry;
import su.nightexpress.engine.settings.ReadOnlySettings;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.key.command.KeyCommand;
import su.nightexpress.excellentcrates.keys.config.settings.KeyCoreSettings;
import su.nightexpress.excellentcrates.keys.lang.KeyLang;
import su.nightexpress.excellentcrates.keys.permission.KeyPerms;
import su.nightexpress.nightcore.commands.command.NightCommand;

@NullMarked
public class KeyCommandRegistrar implements PluginComponent {

    private final CratesPlugin                      plugin;
    private final TinyRegistry<KeyCommand>          commands;
    private final ReadOnlySettings<KeyCoreSettings> settings;

    @Nullable
    private NightCommand command;

    public KeyCommandRegistrar(CratesPlugin plugin,
                               TinyRegistry<KeyCommand> commands,
                               ReadOnlySettings<KeyCoreSettings> settings) {
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
    public boolean isRunning() {
        return true;
    }

    @Override
    public void start() {
        this.command = NightCommand.hub(plugin, this.settings.get().commandAliases(), builder -> {
            builder.description(KeyLang.COMMAND_ROOT_DESCRIPTION);
            builder.permission(KeyPerms.COMMAND_ROOT);

            for (KeyCommand extension : this.commands.getEntries()) {
                builder.branch(extension.createCommand());
            }
        });
        this.command.register();
    }
}
