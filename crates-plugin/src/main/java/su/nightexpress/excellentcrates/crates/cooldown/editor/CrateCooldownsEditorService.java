package su.nightexpress.excellentcrates.crates.cooldown.editor;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.excellentcrates.api.common.cooldown.CooldownOptions;
import su.nightexpress.excellentcrates.api.common.cooldown.CooldownSnapshot;
import su.nightexpress.excellentcrates.api.common.cooldown.CooldownType;
import su.nightexpress.excellentcrates.api.crate.component.CrateComponentKeys;
import su.nightexpress.excellentcrates.api.crate.cooldown.component.CrateCooldownComponent;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorHook;
import su.nightexpress.excellentcrates.api.crate.placeholder.CratePlaceholders;
import su.nightexpress.excellentcrates.crates.cooldown.lang.CrateCooldownsLang;

@NullMarked
public class CrateCooldownsEditorService {

    private final CratePlaceholders cratePlaceholders;

    public CrateCooldownsEditorService(CratePlaceholders cratePlaceholders) {
        this.cratePlaceholders = cratePlaceholders;
    }

    public ActionResult editCooldown(CrateEditorHook hook, CooldownType type, CooldownSnapshot newCooldown) {
        return hook.modify(crate -> {
            CrateCooldownComponent cooldowns = crate.getComponentOrNull(CrateComponentKeys.COOLDOWN);
            if (cooldowns == null) {
                return ActionResult.fail(CrateCooldownsLang.EDITOR_NO_COMPONENT, ctx -> ctx
                    .apply(this.cratePlaceholders.basePlaceholders(crate))
                );
            }

            CooldownOptions cooldown = switch (type) {
                case GLOBAL -> cooldowns.getGlobalCooldown();
                case INDIVIDUAL -> cooldowns.getIndividualCooldown();
            };

            cooldown.setEnabled(newCooldown.enabled());
            cooldown.setDuration(newCooldown.duration());
            cooldown.setMode(newCooldown.mode());

            return ActionResult.ok();
        });
    }
}
