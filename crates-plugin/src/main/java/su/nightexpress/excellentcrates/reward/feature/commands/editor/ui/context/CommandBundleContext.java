package su.nightexpress.excellentcrates.reward.feature.commands.editor.ui.context;

import java.util.List;
import java.util.UUID;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.reward.commands.RewardCommandPool;

@NullMarked
public record CommandBundleContext(UUID id,
                                   boolean supportsWeight,
                                   double weight,
                                   List<String> commands) {

    public static CommandBundleContext from(RewardCommandPool bundle, boolean supportsWeight) {
        return new CommandBundleContext(bundle.getId(), supportsWeight, bundle.getWeight(), bundle.getCommands());
    }
}
