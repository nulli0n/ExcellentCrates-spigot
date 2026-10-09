package su.nightexpress.excellentcrates.crates.block.handler;

import java.util.Comparator;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.block.BlockLinker;
import su.nightexpress.excellentcrates.api.crate.block.BlockRegistry;
import su.nightexpress.excellentcrates.api.crate.block.handler.HandlerResult;
import su.nightexpress.excellentcrates.api.crate.block.provider.BlockProvider;
import su.nightexpress.excellentcrates.api.crate.dispatcher.CrateMessageDispatcher;
import su.nightexpress.excellentcrates.crates.block.item.BlockItemService;
import su.nightexpress.excellentcrates.crates.block.permission.BlocksPerms;
import su.nightexpress.excellentcrates.crates.block.position.DefaultCratePositionRegistry;
import su.nightexpress.nightcore.core.config.CoreLang;

@NullMarked
public class BlockPlacementHandler {

    private final BlockRegistry                blockRegistry;
    private final BlockItemService             itemService;
    private final BlockLinker                  linker;
    private final DefaultCratePositionRegistry positions;
    private final CrateMessageDispatcher       dispatcher;

    public BlockPlacementHandler(BlockRegistry blockRegistry,
                                 BlockItemService itemService,
                                 BlockLinker linker,
                                 DefaultCratePositionRegistry positions,
                                 CrateMessageDispatcher dispatcher) {
        this.blockRegistry = blockRegistry;
        this.itemService = itemService;
        this.linker = linker;
        this.positions = positions;
        this.dispatcher = dispatcher;
    }

    public ActionResult handlePlacement(Identifier source, Player player, ItemStack itemInHand, Location location) {
        // Резолвим провайдера источника по его идентификатору
        BlockProvider sourceProvider = this.blockRegistry.getProvider(source);
        if (sourceProvider == null) {
            return ActionResult.fail(HandlerResult.IGNORE);
        }

        // Проверяем, есть ли более "сильный" провайдер для данного предмета в руке
        BlockProvider handlerCandidate = this.blockRegistry.getProviders()
            .stream()
            .filter(provider -> provider.isBlock(itemInHand))
            .max(Comparator.comparingInt(provider -> provider.getPriority()))
            .orElse(null);

        // Если найден более сильный кандидат и он не совпадает с источником, игнорируем размещение
        if (handlerCandidate != null && !handlerCandidate.id().equals(source)) {
            return ActionResult.fail(HandlerResult.IGNORE);
        }

        Crate crate = this.itemService.getCrate(itemInHand).orElse(null);
        if (crate == null) {
            return ActionResult.fail(HandlerResult.IGNORE);
        }

        if (!player.hasPermission(BlocksPerms.BLOCK_PLACE)) {
            this.dispatcher.send(player, CoreLang.ERROR_NO_PERMISSION);
            return ActionResult.fail(HandlerResult.DENY);
        }

        this.dispatcher.handleFeedbackBase(player, crate, this.linker.linkCrateBlock(crate, location));
        return ActionResult.ok(HandlerResult.SUCCESS);
    }

    public ActionResult handleRemoval(Identifier source, Player player, Location location) {
        Crate crate = this.positions.getCrateAt(location);
        if (crate == null) {
            return ActionResult.fail(HandlerResult.IGNORE);
        }

        if (!player.isSneaking()) {
            if (!player.hasPermission(BlocksPerms.BLOCK_REMOVE)) {
                this.dispatcher.send(player, CoreLang.ERROR_NO_PERMISSION);
            }
            return ActionResult.fail(HandlerResult.DENY);
        }

        this.dispatcher.handleFeedbackBase(player, crate, this.linker.unlinkCrateBlock(crate, location));
        return ActionResult.ok(HandlerResult.SUCCESS);
    }
}
