package su.nightexpress.excellentcrates.crates.hologram.editor;

import java.util.List;
import java.util.function.BiFunction;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.component.CrateComponentKeys;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorHook;
import su.nightexpress.excellentcrates.api.crate.hologram.component.HologramComponent;
import su.nightexpress.excellentcrates.api.crate.hologram.component.HologramOffset;
import su.nightexpress.excellentcrates.api.crate.placeholder.CratePlaceholders;
import su.nightexpress.excellentcrates.core.SharedPlaceholders;
import su.nightexpress.excellentcrates.crates.hologram.HologramDisplayService;
import su.nightexpress.excellentcrates.crates.hologram.lang.HologramsLang;
import su.nightexpress.nightcore.core.config.CoreLang;

@NullMarked
public class HologramEditorService {

    private final HologramDisplayService displayService;
    private final CratePlaceholders      cratePlaceholders;

    public HologramEditorService(HologramDisplayService displayService,
                                 CratePlaceholders cratePlaceholders) {
        this.displayService = displayService;
        this.cratePlaceholders = cratePlaceholders;
    }

    private ActionResult modifyComponent(CrateEditorHook hook,
                                         BiFunction<Crate, HologramComponent, ActionResult> modifier) {
        return hook.modify(crate -> {
            HologramComponent component = crate.getComponentOrNull(CrateComponentKeys.HOLOGRAM);
            if (component == null) {
                return ActionResult.fail(HologramsLang.EDITOR_NO_COMPONENT, ctx -> ctx
                    .apply(this.cratePlaceholders.basePlaceholders(crate))
                );
            }

            ActionResult result = modifier.apply(crate, component);
            if (result.success()) {
                this.displayService.remake(crate);
            }

            return result;
        });
    }

    public ActionResult setHologramEnabled(CrateEditorHook hook, boolean enabled) {
        return modifyComponent(hook, (crate, component) -> {
            component.setEnabled(enabled);
            return ActionResult.ok(HologramsLang.EDITOR_HOLOGRAM_TOGGLE, ctx -> ctx
                .apply(this.cratePlaceholders.basePlaceholders(crate))
                .with(SharedPlaceholders.STATE, () -> CoreLang.STATE_ENABLED_DISALBED.get(enabled))
            );
        });
    }

    public ActionResult setHologramText(CrateEditorHook hook, List<String> lines) {
        return modifyComponent(hook, (crate, component) -> {
            component.setText(lines);
            return ActionResult.ok(HologramsLang.EDITOR_HOLOGRAM_TEXT, ctx -> ctx
                .apply(this.cratePlaceholders.basePlaceholders(crate))
            );
        });
    }

    public ActionResult setHologramOffset(CrateEditorHook hook, HologramOffset offset) {
        return modifyComponent(hook, (crate, component) -> {
            component.setOffset(offset);
            return ActionResult.ok(HologramsLang.EDITOR_HOLOGRAM_OFFSET, ctx -> ctx
                .apply(this.cratePlaceholders.basePlaceholders(crate))
            );
        });
    }
}
