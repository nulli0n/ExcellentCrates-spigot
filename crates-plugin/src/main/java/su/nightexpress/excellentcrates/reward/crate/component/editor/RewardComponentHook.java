package su.nightexpress.excellentcrates.reward.crate.component.editor;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorHook;

@NullMarked
public final class RewardComponentHook {

    private final CrateEditorHook              hook;
    private final RewardComponentEditorService editorService;

    public RewardComponentHook(CrateEditorHook hook, RewardComponentEditorService editorService) {
        this.hook = hook;
        this.editorService = editorService;
    }

    public ActionResult addReward(Crate crate, Identifier rewardId) {
        return this.editorService.addReward(this.hook, rewardId);
    }

    public ActionResult removeReward(Crate crate, Identifier rewardId) {
        return this.editorService.removeReward(this.hook, rewardId);
    }
}
