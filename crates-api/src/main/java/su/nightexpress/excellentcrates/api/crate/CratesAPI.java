package su.nightexpress.excellentcrates.api.crate;

import java.util.Optional;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.service.PluginAPI;
import su.nightexpress.excellentcrates.api.crate.block.BlockAPI;
import su.nightexpress.excellentcrates.api.crate.command.CrateCommandsAPI;
import su.nightexpress.excellentcrates.api.crate.cooldown.CrateCooldownsAPI;
import su.nightexpress.excellentcrates.api.crate.data.CrateDataAPI;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorAPI;
import su.nightexpress.excellentcrates.api.crate.hologram.HologramsAPI;
import su.nightexpress.excellentcrates.api.crate.interact.InteractionAPI;
import su.nightexpress.excellentcrates.api.crate.item.CrateItemAPI;
import su.nightexpress.excellentcrates.api.crate.pipeline.PipelineAPI;
import su.nightexpress.excellentcrates.api.crate.placeholder.CratePlaceholders;
import su.nightexpress.excellentcrates.api.crate.registry.CrateRegistry;

@NullMarked
public interface CratesAPI extends PluginAPI {

    CrateRegistry registry();

    CratePlaceholders getPlaceholders();

    CrateCommandsAPI commands();

    CrateDataAPI data();

    CrateEditorAPI editor();

    CrateItemAPI items();

    PipelineAPI pipeline();

    InteractionAPI interaction();

    Optional<BlockAPI> blocks();

    Optional<HologramsAPI> holograms();

    Optional<CrateCooldownsAPI> cooldowns();
}
