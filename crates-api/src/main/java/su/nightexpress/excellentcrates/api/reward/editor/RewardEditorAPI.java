package su.nightexpress.excellentcrates.api.reward.editor;

import org.jspecify.annotations.NullMarked;

@NullMarked
public interface RewardEditorAPI {

    void registerExtension(RewardEditorExtension extension);
}
