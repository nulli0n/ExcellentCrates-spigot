package su.nightexpress.excellentcrates.keys.cost.editor;

import java.util.function.Function;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.crate.component.CrateComponentKeys;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorHook;
import su.nightexpress.excellentcrates.api.key.crate.KeyRequirementEntry;
import su.nightexpress.excellentcrates.api.key.crate.KeyRequirementComponent;
import su.nightexpress.excellentcrates.keys.cost.component.model.StandardKeyRequirementEntry;
import su.nightexpress.excellentcrates.keys.cost.lang.KeyCostLang;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

@NullMarked
public class KeyCostEditorService {

    private ActionResult modifyComponent(CrateEditorHook context,
                                         Function<KeyRequirementComponent, ActionResult> modifier) {
        return context.modify(crate -> {
            KeyRequirementComponent component = crate.getComponentOrNull(CrateComponentKeys.KEY_REQUIREMENT);
            if (component == null) {
                return ActionResult.fail(KeyCostLang.ERROR_NO_KEY_COST_COMPONENT);
            }

            return modifier.apply(component);
        });
    }

    public ActionResult addCostEntry(CrateEditorHook context, Identifier keyId) {
        return modifyComponent(context, component -> {
            KeyRequirementEntry entry = component.getKeyEntry(keyId);
            if (entry != null) {
                return ActionResult.fail(KeyCostLang.ERROR_KEY_ENTRY_ALREADY_EXISTS, ctx -> ctx
                    .with(CommonPlaceholders.GENERIC_VALUE, keyId::value)
                );
            }

            component.addKeyEntry(keyId, new StandardKeyRequirementEntry(1));

            return ActionResult.ok();
        });
    }

    public ActionResult removeCostEntry(CrateEditorHook context, Identifier keyId) {
        return modifyComponent(context, component -> {
            KeyRequirementEntry entry = component.getKeyEntry(keyId);
            if (entry == null) {
                return ActionResult.fail(KeyCostLang.ERROR_NO_KEY_ENTRY, ctx -> ctx
                    .with(CommonPlaceholders.GENERIC_VALUE, keyId::value)
                );
            }

            component.removeKeyEntry(keyId);

            return ActionResult.ok();
        });
    }

    public ActionResult setCostEnabled(CrateEditorHook context, boolean enabled) {
        return modifyComponent(context, component -> {
            component.setEnabled(enabled);

            return ActionResult.ok();
        });
    }

    public ActionResult setCostEntryAmount(CrateEditorHook context, Identifier keyId, int amount) {
        return modifyComponent(context, component -> {
            KeyRequirementEntry entry = component.getKeyEntry(keyId);
            if (entry == null) {
                return ActionResult.fail(KeyCostLang.ERROR_NO_KEY_ENTRY, ctx -> ctx
                    .with(CommonPlaceholders.GENERIC_VALUE, keyId::value)
                );
            }

            entry.setAmount(amount);

            return ActionResult.ok();
        });
    }
}
