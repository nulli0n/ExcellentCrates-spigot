package su.nightexpress.excellentcrates.reward.command;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bootstrap.context.NamedBootstrapContext;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.registry.SimpleRegistry;
import su.nightexpress.engine.registry.TinyRegistry;
import su.nightexpress.engine.settings.ReadOnlySettings;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.reward.command.RewardCommand;
import su.nightexpress.excellentcrates.api.reward.command.RewardCommandsAPI;
import su.nightexpress.excellentcrates.reward.command.api.DefaultRewardCommandsAPI;
import su.nightexpress.excellentcrates.reward.command.controller.RewardCommandRegistrar;
import su.nightexpress.excellentcrates.reward.config.settings.RewardCoreSettings;

@NullMarked
public class RewardCommandBootstrapContext extends NamedBootstrapContext {

    private static final Identifier ID   = new Identifier("rewards.commands");
    private static final String     NAME = "Commands";

    public final TinyRegistry<RewardCommand> commands;
    public final RewardCommandsAPI           api;

    public RewardCommandBootstrapContext(CratesPlugin plugin, ReadOnlySettings<RewardCoreSettings> settings) {
        super(ID, NAME);
        this.commands = new SimpleRegistry<>();

        this.api = new DefaultRewardCommandsAPI(this.commands);

        this.addComponent(new RewardCommandRegistrar(plugin, this.commands, settings));
    }
}
