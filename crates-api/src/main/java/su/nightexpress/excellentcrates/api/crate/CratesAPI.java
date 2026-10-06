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
import su.nightexpress.excellentcrates.api.crate.pipeline.PipelineAPI;

@NullMarked
public interface CratesAPI extends PluginAPI {

    CrateCommandsAPI commands();

    CrateDataAPI data();

    CrateEditorAPI editor();

    PipelineAPI pipeline();

    InteractionAPI interaction();

    Optional<BlockAPI> blocks();

    Optional<HologramsAPI> holograms();

    Optional<CrateCooldownsAPI> cooldowns();
}
