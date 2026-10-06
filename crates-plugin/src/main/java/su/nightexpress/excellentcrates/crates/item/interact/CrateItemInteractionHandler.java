package su.nightexpress.excellentcrates.crates.item.interact;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.settings.ReadOnlySettings;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.dispatcher.CrateFeedbackHandler;
import su.nightexpress.excellentcrates.api.crate.dispatcher.CrateMessageDispatcher;
import su.nightexpress.excellentcrates.api.crate.interact.context.CrateInteractContext;
import su.nightexpress.excellentcrates.api.crate.interact.context.ItemCrateInteractContext;
import su.nightexpress.excellentcrates.api.crate.item.interact.ItemInteractionResult;
import su.nightexpress.excellentcrates.api.crate.item.interact.ItemInteractionType;
import su.nightexpress.excellentcrates.api.crate.registry.CrateResolver;
import su.nightexpress.excellentcrates.crates.interact.CrateInteractionService;
import su.nightexpress.excellentcrates.crates.item.CrateItemService;
import su.nightexpress.excellentcrates.crates.item.settings.ItemSettings;

@NullMarked
public class CrateItemInteractionHandler implements CrateFeedbackHandler {

    private final ReadOnlySettings<ItemSettings> settings;
    private final CrateResolver                  crateResolver;
    private final CrateItemService               itemService;
    private final CrateInteractionService        interactionService;
    private final CrateMessageDispatcher         dispatcher;

    public CrateItemInteractionHandler(ReadOnlySettings<ItemSettings> settings,
                                       CrateResolver crateResolver,
                                       CrateItemService itemService,
                                       CrateInteractionService interactionService,
                                       CrateMessageDispatcher dispatcher) {
        this.settings = settings;
        this.crateResolver = crateResolver;
        this.itemService = itemService;
        this.interactionService = interactionService;
        this.dispatcher = dispatcher;
    }

    @Override
    public CrateMessageDispatcher getDispatcher() {
        return this.dispatcher;
    }

    public ActionResult interactWithCrateItem(Player player, ItemStack itemStack, ItemInteractionType type) {
        Identifier crateId = this.itemService.getCrateId(itemStack);
        if (crateId == null) {
            return ActionResult.fail(ItemInteractionResult.IGNORE);
        }

        Crate crate = this.crateResolver.resolveCrate(crateId);
        if (crate == null) {
            return ActionResult.fail(ItemInteractionResult.IGNORE);
        }

        ItemStack copyStack = new ItemStack(itemStack);
        CrateInteractContext context = new ItemCrateInteractContext(player, crate, copyStack);

        Identifier actionId = this.settings.get().clickActions().get(type);
        if (actionId == null) {
            return ActionResult.fail(ItemInteractionResult.IGNORE);
        }

        this.handleFeedbackBase(player, crate, this.interactionService.performAction(context, actionId));

        return ActionResult.ok(ItemInteractionResult.SUCCESS);
    }
}
