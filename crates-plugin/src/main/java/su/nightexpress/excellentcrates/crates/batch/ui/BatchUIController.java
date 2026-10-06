package su.nightexpress.excellentcrates.crates.batch.ui;

import java.util.concurrent.atomic.AtomicBoolean;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ProcessCallback;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.batch.BatchSelectionHandler;
import su.nightexpress.excellentcrates.api.crate.dispatcher.CrateMessageDispatcher;
import su.nightexpress.excellentcrates.crates.batch.ui.menu.context.BatchAmountSelectionMenuContext;

@NullMarked
public class BatchUIController implements BatchSelectionHandler {

    private static final int SINGLE_OPTION_THRESHOLD = 1;

    private final BatchUIService         uiService;
    private final CrateMessageDispatcher dispatcher;

    public BatchUIController(BatchUIService uiService, CrateMessageDispatcher dispatcher) {
        this.uiService = uiService;
        this.dispatcher = dispatcher;
    }

    @Override
    public void startSelection(Player player, Crate crate, int maxAllowed, ProcessCallback<Integer> callback) {
        if (maxAllowed <= SINGLE_OPTION_THRESHOLD) {
            callback.proceed(maxAllowed);
            return;
        }

        if (!this.openSelectionMenu(player, crate, maxAllowed, callback)) {
            callback.cancel();
        }
    }

    private boolean openSelectionMenu(Player player, Crate crate, int maxAllowed, ProcessCallback<Integer> callback) {
        Identifier crateId = crate.id();
        AtomicBoolean isChosen = new AtomicBoolean(false);

        return this.dispatcher.handleFeedbackBase(player, crate, this.uiService.openAmountSelectionMenu(player,
            new BatchAmountSelectionMenuContext(
                crateId, maxAllowed, isChosen, callback::cancel, selectedAmount -> {
                    if (selectedAmount > maxAllowed) {
                        return;
                    }

                    isChosen.set(true);
                    player.closeInventory();

                    callback.proceed(selectedAmount);
                }))
        );
    }
}
