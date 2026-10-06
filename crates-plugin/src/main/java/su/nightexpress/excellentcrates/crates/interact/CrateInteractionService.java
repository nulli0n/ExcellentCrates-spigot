package su.nightexpress.excellentcrates.crates.interact;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.id.IdentifiableRegistry;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.interact.action.InteractAction;
import su.nightexpress.excellentcrates.api.crate.interact.context.CrateInteractContext;
import su.nightexpress.excellentcrates.crates.interact.cooldown.InteractCooldown;
import su.nightexpress.excellentcrates.crates.interact.cooldown.InteractCooldownService;
import su.nightexpress.excellentcrates.crates.interact.lang.InteractionLang;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.time.TimeFormatType;
import su.nightexpress.nightcore.util.time.TimeFormats;

@NullMarked
public class CrateInteractionService {

    private final IdentifiableRegistry<InteractAction> actions;
    private final InteractCooldownService              cooldownService;

    public CrateInteractionService(IdentifiableRegistry<InteractAction> actions,
                                   InteractCooldownService cooldownService) {
        this.actions = actions;
        this.cooldownService = cooldownService;
    }

    public ActionResult checkCooldown(Player player, Crate crate) {
        InteractCooldown cooldown = this.cooldownService.getCooldown(player, crate);
        if (cooldown != null && !cooldown.hasExpired()) {
            return ActionResult.fail(InteractionLang.INTERACT_COOLDOWN_ACTIVE, ctx -> ctx
                .with(CommonPlaceholders.GENERIC_TIME, () -> {
                    return TimeFormats.formatDuration(cooldown.expiryTimestamp(), TimeFormatType.LITERAL);
                })
            );
        }

        return ActionResult.ok();
    }

    /**
     * Performs the specified action on the given crate interaction context.
     * 
     * @param context  The context of the crate interaction.
     * @param actionId The identifier of the action to perform.
     * @return Whether the action was triggered ({@link InteractAction#perform}).
     */
    public ActionResult performAction(CrateInteractContext context, Identifier actionId) {
        Player player = context.player();
        Crate crate = context.crate();

        ActionResult checkCooldown = this.checkCooldown(player, crate);
        if (!checkCooldown.success()) {
            return checkCooldown;
        }

        InteractAction action = this.actions.get(actionId);
        if (action == null) {
            return ActionResult.fail(InteractionLang.INTERACT_ACTION_NOT_FOUND, ctx -> ctx
                .with(CommonPlaceholders.GENERIC_NAME, actionId::value)
            );
        }

        action.perform(context);

        this.cooldownService.setCooldown(player, crate);

        return ActionResult.ok();
    }
}
