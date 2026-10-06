package su.nightexpress.excellentcrates.crates.block.component.editor;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.crate.block.crate.BlockComponent;
import su.nightexpress.excellentcrates.api.crate.block.position.CratePositionRegistry;
import su.nightexpress.excellentcrates.api.crate.component.CrateComponentKeys;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorHook;
import su.nightexpress.excellentcrates.api.crate.placeholder.CratePlaceholders;
import su.nightexpress.excellentcrates.crates.block.lang.BlocksLang;

@NullMarked
public class BlockEditorService {

    private final CratePositionRegistry positions;
    private final CratePlaceholders     cratePlaceholders;

    public BlockEditorService(CratePositionRegistry positions, CratePlaceholders cratePlaceholders) {
        this.positions = positions;
        this.cratePlaceholders = cratePlaceholders;
    }

    public ActionResult clearPositions(CrateEditorHook hook, Identifier crateId) {
        return hook.modify(crate -> {
            BlockComponent component = crate.getComponentOrNull(CrateComponentKeys.BLOCK);
            if (component == null) {
                return ActionResult.fail(BlocksLang.ERROR_NO_BLOCKS_COMPONENT, ctx -> ctx
                    .apply(this.cratePlaceholders.basePlaceholders(crate))
                );
            }

            this.positions.unregisterPositions(crate);

            component.clearBlockPositions();

            return ActionResult.ok(BlocksLang.EDITOR_POSITION_UNLINK_SUCCESS, ctx -> ctx
                .apply(this.cratePlaceholders.basePlaceholders(crate))
            );
        });
    }
}
