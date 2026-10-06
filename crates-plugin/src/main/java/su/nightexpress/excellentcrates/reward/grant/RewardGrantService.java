package su.nightexpress.excellentcrates.reward.grant;

import java.util.Comparator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.registry.TinyRegistry;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.placeholder.CratePlaceholders;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.dispatcher.RewardMessageDispatcher;
import su.nightexpress.excellentcrates.api.reward.event.RewardGrantEvent;
import su.nightexpress.excellentcrates.api.reward.event.RewardGrantedEvent;
import su.nightexpress.excellentcrates.api.reward.grant.RegisteredGrantProcessor;
import su.nightexpress.excellentcrates.api.reward.grant.RewardGrantProcessor;
import su.nightexpress.excellentcrates.api.reward.placeholder.RewardPlaceholders;
import su.nightexpress.excellentcrates.reward.lang.RewardsLang;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext;

@NullMarked
public class RewardGrantService {

    private final TinyRegistry<RegisteredGrantProcessor> processors;
    private final CratePlaceholders                      cratePlaceholders;
    private final RewardPlaceholders                     rewardPlaceholders;
    private final RewardMessageDispatcher                dispatcher;

    public RewardGrantService(TinyRegistry<RegisteredGrantProcessor> processors,
                              CratePlaceholders cratePlaceholders,
                              RewardPlaceholders rewardPlaceholders,
                              RewardMessageDispatcher dispatcher) {
        this.processors = processors;
        this.cratePlaceholders = cratePlaceholders;
        this.rewardPlaceholders = rewardPlaceholders;
        this.dispatcher = dispatcher;
    }

    // Not cached into a field because the processors can be registered at any time, even after this service is constructed.
    private List<RewardGrantProcessor> compilePipeline() {
        return this.processors.getEntries().stream()
            .sorted(Comparator.comparingInt(RegisteredGrantProcessor::priority))
            .map(RegisteredGrantProcessor::processor)
            .toList();
    }

    public void registerProcessor(int priority, RewardGrantProcessor processor) {
        this.processors.register(new RegisteredGrantProcessor(priority, processor));
    }

    public void giveReward(Player player, Crate crate, Reward reward) {
        RewardGrantEvent grantEvent = new RewardGrantEvent(player, crate, reward);
        Bukkit.getPluginManager().callEvent(grantEvent);
        if (grantEvent.isCancelled()) {
            return;
        }

        PlaceholderContext placeholders = PlaceholderContext.builder()
            .apply(this.cratePlaceholders.allPlaceholders(crate, player))
            .apply(this.rewardPlaceholders.allPlaceholders(crate, reward, player))
            .andThen(CommonPlaceholders.PLAYER.replacer(player))
            .andThen(CommonPlaceholders.forPlaceholderAPI(player))
            .build();

        this.compilePipeline().forEach(processor -> {
            processor.execute(player, crate, reward, placeholders);
        });

        this.dispatcher.sendAll(player, crate, reward, RewardsLang.GRANT_NOTIFY);

        RewardGrantedEvent grantedEvent = new RewardGrantedEvent(player, crate, reward);
        Bukkit.getPluginManager().callEvent(grantedEvent);
    }
}
