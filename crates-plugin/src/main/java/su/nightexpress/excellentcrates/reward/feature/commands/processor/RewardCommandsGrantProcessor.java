package su.nightexpress.excellentcrates.reward.feature.commands.processor;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.commands.RewardCommandPool;
import su.nightexpress.excellentcrates.api.reward.commands.RewardCommandsComponent;
import su.nightexpress.excellentcrates.api.reward.commands.RewardCommandExecutionMode;
import su.nightexpress.excellentcrates.api.reward.component.RewardComponentKeys;
import su.nightexpress.excellentcrates.api.reward.grant.RewardGrantProcessor;
import su.nightexpress.excellentcrates.util.WeightedRandomSelector;
import su.nightexpress.nightcore.util.Randomizer;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext;

@NullMarked
public class RewardCommandsGrantProcessor implements RewardGrantProcessor {

    @Override
    public void execute(Player player, Crate crate, Reward reward, PlaceholderContext placeholders) {
        RewardCommandsComponent commandContent = reward.getComponentOrNull(RewardComponentKeys.COMMANDS);
        if (commandContent == null || !commandContent.isEnabled() || commandContent.isEmpty()) {
            return;
        }

        RewardCommandExecutionMode giveMode = commandContent.getGiveMode();
        int iterations = commandContent.getIterations();

        for (int i = 0; i < iterations; i++) {
            List<RewardCommandPool> selectedBundles = this.selectBundles(commandContent, giveMode);
            for (RewardCommandPool bundle : selectedBundles) {
                this.executeBundle(bundle, placeholders);
            }
        }

    }

    private List<RewardCommandPool> selectBundles(RewardCommandsComponent commandContent,
                                                  RewardCommandExecutionMode giveMode) {
        return switch (giveMode) {
            case NORMAL -> commandContent.getBundles();
            case WEIGHTED -> {
                WeightedRandomSelector selector = new WeightedRandomSelector(Randomizer.getSource());
                yield List.of(selector.selectItem(commandContent.getBundles(), RewardCommandPool::getWeight));
            }
            case CHANCE -> {
                yield commandContent.getBundles().stream()
                    .filter(bundle -> Randomizer.checkChance(bundle.getWeight()))
                    .toList();
            }
        };
    }

    private void executeBundle(RewardCommandPool bundle, PlaceholderContext placeholders) {
        bundle.getCommands().forEach(command -> {
            String processedCommand = placeholders.apply(command);
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), processedCommand);
        });
    }
}
