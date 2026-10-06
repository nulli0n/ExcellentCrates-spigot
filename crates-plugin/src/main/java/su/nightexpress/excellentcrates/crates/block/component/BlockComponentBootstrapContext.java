package su.nightexpress.excellentcrates.crates.block.component;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bootstrap.context.NamedBootstrapContext;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.block.BlockRegistry;
import su.nightexpress.excellentcrates.api.crate.block.position.CratePositionRegistry;
import su.nightexpress.excellentcrates.api.crate.dispatcher.CrateMessageDispatcher;
import su.nightexpress.excellentcrates.api.crate.placeholder.CratePlaceholders;
import su.nightexpress.excellentcrates.api.crate.registry.CrateResolver;
import su.nightexpress.excellentcrates.crates.block.component.codec.BlockComponentCodec;
import su.nightexpress.excellentcrates.crates.block.component.editor.BlockEditorService;
import su.nightexpress.excellentcrates.crates.block.component.editor.ui.BlockEditorUIController;
import su.nightexpress.excellentcrates.crates.block.component.editor.ui.BlockEditorUIService;
import su.nightexpress.excellentcrates.crates.block.component.editor.ui.controller.BlockEditorUIDialogRegistrar;
import su.nightexpress.excellentcrates.crates.block.component.editor.ui.controller.BlockEditorUIMenuRegistrar;
import su.nightexpress.excellentcrates.crates.block.component.editor.ui.extension.BlockComponentEditorExtension;
import su.nightexpress.excellentcrates.crates.block.component.extension.BlockComponentDataExtension;
import su.nightexpress.excellentcrates.crates.block.item.BlockItemService;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;

@NullMarked
public class BlockComponentBootstrapContext extends NamedBootstrapContext {

    private static final Identifier ID   = new Identifier("crates.blocks.component");
    private static final String     NAME = "Data Component";

    private final BlockComponentDataExtension   dataExtension;
    private final BlockComponentEditorExtension editorExtension;

    public BlockComponentBootstrapContext(CratesPlugin plugin,
                                          CoreUIService coreUI,
                                          CrateMessageDispatcher dispatcher,
                                          CrateResolver crateResolver,
                                          CratePlaceholders cratePlaceholders,
                                          CratePositionRegistry positionRegistry,
                                          BlockRegistry blockRegistry,
                                          BlockItemService itemService) {
        super(ID, NAME);
        ConfigCodecs.register(DefaultBlockComponent.class, BlockComponentCodec.INSTANCE);

        BlockEditorService editorService = new BlockEditorService(positionRegistry, cratePlaceholders);
        BlockEditorUIService uiService = new BlockEditorUIService(coreUI);
        BlockEditorUIController controller = new BlockEditorUIController(
            itemService, editorService, uiService, dispatcher
        );

        this.addComponent(
            new BlockEditorUIMenuRegistrar(plugin, coreUI, crateResolver, blockRegistry, itemService, controller)
        );

        this.addComponent(new BlockEditorUIDialogRegistrar(coreUI, controller));

        this.dataExtension = new BlockComponentDataExtension(positionRegistry);
        this.editorExtension = new BlockComponentEditorExtension(controller);
    }

    public BlockComponentDataExtension getDataExtension() {
        return this.dataExtension;
    }

    public BlockComponentEditorExtension getEditorExtension() {
        return this.editorExtension;
    }
}
