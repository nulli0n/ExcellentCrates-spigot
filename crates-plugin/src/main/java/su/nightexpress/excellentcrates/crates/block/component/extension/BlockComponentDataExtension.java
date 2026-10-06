package su.nightexpress.excellentcrates.crates.block.component.extension;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.block.crate.BlockComponent;
import su.nightexpress.excellentcrates.api.crate.block.position.CratePositionRegistry;
import su.nightexpress.excellentcrates.api.crate.component.CrateComponentKeys;
import su.nightexpress.excellentcrates.api.crate.data.extension.CrateDataExtension;
import su.nightexpress.excellentcrates.api.crate.data.model.ICrateBuilder;
import su.nightexpress.excellentcrates.crates.block.component.DefaultBlockComponent;
import su.nightexpress.nightcore.config.FileConfig;

@NullMarked
public class BlockComponentDataExtension implements CrateDataExtension {

    private final CratePositionRegistry positionRegistry;

    public BlockComponentDataExtension(CratePositionRegistry positionRegistry) {
        this.positionRegistry = positionRegistry;
    }

    @Override
    public void onBuild(ICrateBuilder builder, Identifier crateId) {
        builder.component(CrateComponentKeys.BLOCK, DefaultBlockComponent.empty());
    }

    @Override
    public void onRead(FileConfig config, ICrateBuilder builder, Identifier crateId) {
        DefaultBlockComponent component = config.getOrSet("blocks",
            DefaultBlockComponent.class,
            DefaultBlockComponent.empty()
        );

        builder.component(CrateComponentKeys.BLOCK, component);
    }

    @Override
    public void onWrite(FileConfig config, Crate crate) {
        BlockComponent component = crate.getComponentOrNull(CrateComponentKeys.BLOCK);
        config.set("blocks", component);
    }

    @Override
    public void onCreate(Crate crate) {

    }

    @Override
    public void onDelete(Crate crate) {

    }

    @Override
    public void onLoad(Crate crate) {

    }

    @Override
    public void onUnload(Crate crate) {
        this.positionRegistry.unregisterPositions(crate);
    }
}
