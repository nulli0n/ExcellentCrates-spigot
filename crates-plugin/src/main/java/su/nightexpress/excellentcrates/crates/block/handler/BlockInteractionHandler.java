package su.nightexpress.excellentcrates.crates.block.handler;

import java.util.Comparator;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.action.FeedbackHandler;
import su.nightexpress.engine.dispatcher.MessageDispatcher;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.settings.ReadOnlySettings;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.block.BlockRegistry;
import su.nightexpress.excellentcrates.api.crate.block.handler.HandlerResult;
import su.nightexpress.excellentcrates.api.crate.block.interact.BlockInteractionType;
import su.nightexpress.excellentcrates.api.crate.block.position.CratePositionRegistry;
import su.nightexpress.excellentcrates.api.crate.block.provider.BlockProvider;
import su.nightexpress.excellentcrates.api.crate.interact.context.CrateInteractContext;
import su.nightexpress.excellentcrates.api.crate.interact.context.LocatedCrateInteractContext;
import su.nightexpress.excellentcrates.crates.block.settings.BlockSettings;
import su.nightexpress.excellentcrates.crates.interact.CrateInteractionService;

@NullMarked
public class BlockInteractionHandler implements FeedbackHandler {

    private final ReadOnlySettings<BlockSettings> settings;

    private final BlockRegistry           resolveService;
    private final CratePositionRegistry   positionResolver;
    private final CrateInteractionService interactionService;
    private final MessageDispatcher       dispatcher;

    public BlockInteractionHandler(ReadOnlySettings<BlockSettings> settings,
                                   BlockRegistry resolveService,
                                   CratePositionRegistry positionResolver,
                                   CrateInteractionService interactionService,
                                   MessageDispatcher dispatcher) {
        this.settings = settings;
        this.resolveService = resolveService;
        this.positionResolver = positionResolver;
        this.interactionService = interactionService;
        this.dispatcher = dispatcher;
    }

    @Override
    public MessageDispatcher getDispatcher() {
        return this.dispatcher;
    }

    public ActionResult handleInteraction(Identifier sourceId, BlockInteractionType type, Player player,
                                          Location location) {
        Crate crate = this.positionResolver.getCrateAt(location);
        if (crate == null) {
            return ActionResult.fail(HandlerResult.IGNORE);
        }

        // Резолвим провайдера источника по его идентификатору
        BlockProvider sourceProvider = this.resolveService.getProvider(sourceId);
        if (sourceProvider == null) {
            return ActionResult.fail(HandlerResult.IGNORE);
        }

        // Проверяем, есть ли более "сильный" провайдер для данной локации/блока
        BlockProvider handlerCandidate = this.resolveService.getProviders()
            .stream()
            .filter(provider -> provider.canHandle(location))
            .max(Comparator.comparingInt(provider -> provider.getPriority()))
            .orElse(null);

        // Если более сильный кандидат найден и не совпадает с источником, игнорируем взаимодействие
        if (handlerCandidate != null && !handlerCandidate.id().equals(sourceId)) {
            return ActionResult.fail(HandlerResult.IGNORE);
        }

        Identifier actionId = this.settings.get().clickActions().get(type);
        if (actionId == null) {
            return ActionResult.fail(HandlerResult.IGNORE);
        }

        CrateInteractContext context = new LocatedCrateInteractContext(player, crate, location);

        this.handleFeedback(player, this.interactionService.performAction(context, actionId));
        return ActionResult.ok(HandlerResult.SUCCESS);
    }
}
