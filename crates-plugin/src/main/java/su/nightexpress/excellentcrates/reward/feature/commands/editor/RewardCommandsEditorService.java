package su.nightexpress.excellentcrates.reward.feature.commands.editor;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.commands.RewardCommandPool;
import su.nightexpress.excellentcrates.api.reward.commands.RewardCommandsComponent;
import su.nightexpress.excellentcrates.api.reward.commands.RewardCommandExecutionMode;
import su.nightexpress.excellentcrates.api.reward.component.RewardComponentKeys;
import su.nightexpress.excellentcrates.api.reward.editor.RewardEditorHook;
import su.nightexpress.excellentcrates.reward.feature.commands.editor.ui.context.CommandBundleContext;
import su.nightexpress.excellentcrates.reward.lang.RewardsLang;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

@NullMarked
public class RewardCommandsEditorService {

    private void editCommandContent(Reward reward, Consumer<RewardCommandsComponent> action) {
        RewardCommandsComponent content = reward.getComponentStrict(RewardComponentKeys.COMMANDS);
        action.accept(content);
    }

    public ActionResult setCommandsState(RewardEditorHook hook, boolean state) {
        return hook.modify(reward -> {
            this.editCommandContent(reward, content -> content.setEnabled(state));
            return ActionResult.ok();
        });
    }

    public ActionResult setCommandsGiveMode(RewardEditorHook hook, RewardCommandExecutionMode giveMode) {
        return hook.modify(reward -> {
            this.editCommandContent(reward, content -> content.setGiveMode(giveMode));
            return ActionResult.ok();
        });
    }

    public ActionResult setCommandsIterations(RewardEditorHook hook, int amount) {
        return hook.modify(reward -> {
            this.editCommandContent(reward, content -> content.setIterations(amount));
            return ActionResult.ok();
        });
    }

    public ActionResult addCommandBundle(RewardEditorHook hook, RewardCommandPool bundle) {
        return hook.modify(reward -> {
            this.editCommandContent(reward, content -> content.addBundle(bundle));
            return ActionResult.ok();
        });
    }

    public ActionResult removeCommandBundle(RewardEditorHook hook, UUID bundleId) {
        return hook.modify(reward -> {
            this.editCommandContent(reward, content -> content.removeBundle(bundleId));
            return ActionResult.ok();
        });
    }

    public ActionResult updateCommandBundle(RewardEditorHook hook, CommandBundleContext bundleContext) {
        return hook.modify(reward -> {
            UUID bundleId = bundleContext.id();
            double weight = bundleContext.weight();
            List<String> commands = bundleContext.commands();

            RewardCommandPool bundle = reward.getComponentStrict(RewardComponentKeys.COMMANDS).getBundle(bundleId);
            if (bundle == null) {
                return ActionResult.fail(RewardsLang.GENERIC_REWARD_COMMAND_BUNDLE_NOT_FOUND, ctx -> ctx
                    .with(CommonPlaceholders.GENERIC_VALUE, bundleId::toString)
                );
            }

            if (bundleContext.supportsWeight()) {
                bundle.setWeight(weight);
            }
            bundle.setCommands(commands);

            return ActionResult.ok();
        });
    }
}
