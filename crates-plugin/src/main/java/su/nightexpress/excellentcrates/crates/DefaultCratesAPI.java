package su.nightexpress.excellentcrates.crates;

import java.util.Objects;
import java.util.Optional;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentcrates.api.crate.CratesAPI;
import su.nightexpress.excellentcrates.api.crate.block.BlockAPI;
import su.nightexpress.excellentcrates.api.crate.command.CrateCommandsAPI;
import su.nightexpress.excellentcrates.api.crate.cooldown.CrateCooldownsAPI;
import su.nightexpress.excellentcrates.api.crate.data.CrateDataAPI;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorAPI;
import su.nightexpress.excellentcrates.api.crate.hologram.HologramsAPI;
import su.nightexpress.excellentcrates.api.crate.interact.InteractionAPI;
import su.nightexpress.excellentcrates.api.crate.item.CrateItemAPI;
import su.nightexpress.excellentcrates.api.crate.pipeline.PipelineAPI;
import su.nightexpress.excellentcrates.api.crate.registry.CrateRegistry;

@NullMarked
public class DefaultCratesAPI implements CratesAPI {

    private final CrateRegistry    registry;
    private final CrateCommandsAPI commands;
    private final CrateDataAPI     data;
    private final CrateEditorAPI   editor;
    private final CrateItemAPI     items;
    private final PipelineAPI      pipeline;
    private final InteractionAPI   interaction;

    private final @Nullable BlockAPI          blocks;
    private final @Nullable HologramsAPI      holograms;
    private final @Nullable CrateCooldownsAPI cooldowns;

    DefaultCratesAPI(Builder builder) {
        this.registry = Objects.requireNonNull(builder.registry, "registry cannot be null");
        this.commands = Objects.requireNonNull(builder.commands, "commands cannot be null");
        this.data = Objects.requireNonNull(builder.data, "data cannot be null");
        this.editor = Objects.requireNonNull(builder.editor, "editor cannot be null");
        this.pipeline = Objects.requireNonNull(builder.pipeline, "pipeline cannot be null");
        this.interaction = Objects.requireNonNull(builder.interaction, "interaction cannot be null");
        this.items = Objects.requireNonNull(builder.items, "items cannot be null");

        this.blocks = builder.blocks;
        this.holograms = builder.holograms;
        this.cooldowns = builder.cooldowns;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public CrateRegistry registry() {
        return this.registry;
    }

    @Override
    public CrateCommandsAPI commands() {
        return this.commands;
    }

    @Override
    public CrateDataAPI data() {
        return this.data;
    }

    @Override
    public CrateEditorAPI editor() {
        return this.editor;
    }

    @Override
    public CrateItemAPI items() {
        return this.items;
    }

    @Override
    public PipelineAPI pipeline() {
        return this.pipeline;
    }

    @Override
    public InteractionAPI interaction() {
        return this.interaction;
    }

    @Override
    public Optional<BlockAPI> blocks() {
        return Optional.ofNullable(this.blocks);
    }

    @Override
    public Optional<HologramsAPI> holograms() {
        return Optional.ofNullable(this.holograms);
    }

    @Override
    public Optional<CrateCooldownsAPI> cooldowns() {
        return Optional.ofNullable(this.cooldowns);
    }

    public static class Builder {

        private @Nullable CrateRegistry    registry;
        private @Nullable CrateCommandsAPI commands;
        private @Nullable CrateDataAPI     data;
        private @Nullable CrateEditorAPI   editor;
        private @Nullable CrateItemAPI     items;
        private @Nullable PipelineAPI      pipeline;
        private @Nullable InteractionAPI   interaction;

        private @Nullable BlockAPI          blocks;
        private @Nullable HologramsAPI      holograms;
        private @Nullable CrateCooldownsAPI cooldowns;

        public Builder registry(@Nullable CrateRegistry registry) {
            this.registry = registry;
            return this;
        }

        public Builder commands(@Nullable CrateCommandsAPI commands) {
            this.commands = commands;
            return this;
        }

        public Builder data(@Nullable CrateDataAPI data) {
            this.data = data;
            return this;
        }

        public Builder editor(@Nullable CrateEditorAPI editor) {
            this.editor = editor;
            return this;
        }

        public Builder items(@Nullable CrateItemAPI items) {
            this.items = items;
            return this;
        }

        public Builder pipeline(@Nullable PipelineAPI pipeline) {
            this.pipeline = pipeline;
            return this;
        }

        public Builder interaction(@Nullable InteractionAPI interaction) {
            this.interaction = interaction;
            return this;
        }

        public Builder blocks(@Nullable BlockAPI blocksAPI) {
            this.blocks = blocksAPI;
            return this;
        }

        public Builder holograms(@Nullable HologramsAPI hologramsAPI) {
            this.holograms = hologramsAPI;
            return this;
        }

        public Builder cooldowns(@Nullable CrateCooldownsAPI cooldownsAPI) {
            this.cooldowns = cooldownsAPI;
            return this;
        }

        public DefaultCratesAPI build() {
            return new DefaultCratesAPI(this);
        }
    }
}
