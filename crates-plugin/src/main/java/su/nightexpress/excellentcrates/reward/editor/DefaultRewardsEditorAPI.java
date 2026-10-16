package su.nightexpress.excellentcrates.reward.editor;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.IdentifiableRegistry;
import su.nightexpress.engine.registry.TinyRegistry;
import su.nightexpress.excellentcrates.api.reward.editor.RewardEditorAPI;
import su.nightexpress.excellentcrates.api.reward.editor.RewardEditorExtension;
import su.nightexpress.excellentcrates.api.reward.editor.RewardSorter;

@NullMarked
public class DefaultRewardsEditorAPI implements RewardEditorAPI {

    private final TinyRegistry<RewardEditorExtension> extensions;
    private final IdentifiableRegistry<RewardSorter>  sorters;

    public DefaultRewardsEditorAPI(TinyRegistry<RewardEditorExtension> extensions,
                                   IdentifiableRegistry<RewardSorter> sorters) {
        this.extensions = extensions;
        this.sorters = sorters;
    }

    @Override
    public void registerExtension(RewardEditorExtension extension) {
        this.extensions.register(extension);
    }

    @Override
    public void registerSorter(RewardSorter sorter) {
        this.sorters.register(sorter);
    }
}
