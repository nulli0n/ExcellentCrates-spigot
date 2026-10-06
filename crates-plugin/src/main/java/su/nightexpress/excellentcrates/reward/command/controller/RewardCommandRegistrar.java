package su.nightexpress.excellentcrates.reward.command.controller;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.component.PluginComponent;
import su.nightexpress.engine.registry.TinyRegistry;
import su.nightexpress.engine.settings.ReadOnlySettings;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.reward.command.RewardCommand;
import su.nightexpress.excellentcrates.reward.config.settings.RewardCoreSettings;
import su.nightexpress.excellentcrates.reward.lang.RewardsLang;
import su.nightexpress.excellentcrates.reward.permission.RewardPerms;
import su.nightexpress.nightcore.commands.command.NightCommand;

@NullMarked
public class RewardCommandRegistrar implements PluginComponent {

    private final CratesPlugin                         plugin;
    private final TinyRegistry<RewardCommand>          registry;
    private final ReadOnlySettings<RewardCoreSettings> settings;

    @Nullable
    private NightCommand command;

    public RewardCommandRegistrar(CratesPlugin plugin,
                                  TinyRegistry<RewardCommand> registry,
                                  ReadOnlySettings<RewardCoreSettings> settings) {
        this.plugin = plugin;
        this.registry = registry;
        this.settings = settings;
    }

    @Override
    public void reload() {

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
        this.command = NightCommand.hub(plugin, this.settings.get().commandAliases(), builder -> {
            builder.description(RewardsLang.COMMAND_ROOT_DESCRIPTION);
            builder.permission(RewardPerms.COMMAND_ROOT);

            this.registry.getEntries().forEach(extension -> {
                builder.branch(extension.createCommand());
            });
        });
        this.command.register();

        this.registry.clear();
        this.registry.lock();
    }
}
