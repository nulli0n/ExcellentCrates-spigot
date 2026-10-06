package su.nightexpress.excellentcrates.crates.block.component.editor.ui.menu;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MenuType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.block.BlockRegistry;
import su.nightexpress.excellentcrates.api.crate.block.CrateBlock;
import su.nightexpress.excellentcrates.api.crate.block.provider.BlockProvider;
import su.nightexpress.excellentcrates.api.crate.registry.CrateResolver;
import su.nightexpress.excellentcrates.crates.block.component.editor.ui.BlockEditorUIController;
import su.nightexpress.excellentcrates.crates.block.component.editor.ui.menu.context.BlockCatalogMenuContext;
import su.nightexpress.excellentcrates.crates.block.item.BlockItemService;
import su.nightexpress.excellentcrates.crates.block.lang.BlocksLang;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.ui.inventory.item.ItemPopulator;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.ui.inventory.menu.AbstractObjectMenu;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;
import su.nightexpress.nightcore.util.bukkit.NightItem;

@NullMarked
public class BlockCatalogMenu extends AbstractObjectMenu<BlockCatalogMenuContext> {

    private final CrateResolver           crateResolver;
    private final BlockRegistry           blockRegistry;
    private final BlockItemService        itemService;
    private final BlockEditorUIController controller;

    private final ItemPopulator<AdaptedKey> blockPopulator;

    public BlockCatalogMenu(CratesPlugin plugin,
                            CrateResolver crateResolver,
                            BlockRegistry blockRegistry,
                            BlockItemService itemService,
                            BlockEditorUIController controller) {
        super(plugin, MenuType.GENERIC_9X5, BlocksLang.EDITOR_UI_INVENTORY_CATALOG_TITLE
            .text(), BlockCatalogMenuContext.class);
        this.crateResolver = crateResolver;
        this.blockRegistry = blockRegistry;
        this.itemService = itemService;
        this.controller = controller;

        this.blockPopulator = ItemPopulator.builder(AdaptedKey.class)
            .slots(IntStream.range(0, 36).toArray())
            .itemProvider((context, block) -> this.createBlockItem(context, block))
            .actionProvider(block -> context -> this.handleBlockClick(context, block))
            .build();
    }

    @Override
    public void defineDefaultLayout() {
        this.addBackgroundItem(Material.GRAY_STAINED_GLASS_PANE, IntStream.range(0, 36).toArray());
        this.addBackgroundItem(Material.BLACK_STAINED_GLASS_PANE, IntStream.range(36, 45).toArray());

        this.addBackButton(this::handleBack, 36);
        this.addNextPageButton(41);
        this.addPreviousPageButton(39);
    }

    @Override
    protected void onClick(ViewerContext context, InventoryClickEvent event) {

    }

    @Override
    protected void onClose(ViewerContext context, InventoryCloseEvent event) {

    }

    @Override
    protected void onDrag(ViewerContext context, InventoryDragEvent event) {

    }

    @Override
    protected void onLoad(FileConfig config) {

    }

    @Override
    public void registerActions() {

    }

    @Override
    public void registerConditions() {

    }

    @Override
    public void onPrepare(ViewerContext context, InventoryView view, Inventory inventory, List<MenuItem> items) {
        List<AdaptedKey> blocks = new ArrayList<>();

        this.blockRegistry.getProviders()
            .stream()
            .sorted(Comparator.comparingInt((BlockProvider<?> p) -> p.getPriority()).reversed())
            .forEach(provider -> {
                provider.fetchBlocks().stream()
                    .map(CrateBlock::getKey)
                    .sorted(Comparator.comparing(AdaptedKey::asString))
                    .forEach(blocks::add);
            });

        this.blockPopulator.populateTo(context, blocks, items);
    }

    @Override
    public void onReady(ViewerContext context, InventoryView view, Inventory inventory) {

    }

    @Override
    public void onRender(ViewerContext context, InventoryView view, Inventory inventory) {

    }

    private void handleBack(ActionContext context) {
        Player player = context.getPlayer();
        BlockCatalogMenuContext menuContext = this.getObject(context);

        menuContext.moveBackward(player);
    }

    private @Nullable NightItem createBlockItem(ViewerContext context, AdaptedKey blockKey) {
        BlockCatalogMenuContext menuContext = this.getObject(context);
        Crate crate = this.crateResolver.resolveCrate(menuContext.crateId());
        if (crate == null) {
            return null;
        }

        CrateBlock block = this.blockRegistry.getBlock(blockKey);
        if (block == null) {
            return null;
        }

        ItemStack itemStack = this.itemService.getBlockItem(crate, block.key()).orElse(null);
        return itemStack != null ? NightItem.fromItemStack(itemStack) : null;
    }

    private void handleBlockClick(ActionContext context, AdaptedKey blockKey) {
        Player player = context.getPlayer();
        BlockCatalogMenuContext menuContext = this.getObject(context);

        Crate crate = this.crateResolver.resolveCrate(menuContext.crateId());
        if (crate == null) return;

        CrateBlock block = this.blockRegistry.getBlock(blockKey);
        if (block == null) return;

        this.controller.onCatalogMenuGetBlockClick(player, crate, block);
    }
}
