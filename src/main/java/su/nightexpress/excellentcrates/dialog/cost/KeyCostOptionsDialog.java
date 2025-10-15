package su.nightexpress.excellentcrates.dialog.cost;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.crate.cost.entry.impl.KeyCostEntry;
import su.nightexpress.excellentcrates.dialog.CrateDialog;
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;
import su.nightexpress.nightcore.bridge.dialog.wrap.input.single.WrappedSingleOptionEntry;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.DialogElementLocale;
import su.nightexpress.nightcore.locale.entry.TextLocale;
import su.nightexpress.nightcore.ui.dialog.Dialogs;
import su.nightexpress.nightcore.ui.dialog.build.*;

import static su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers.*;

public class KeyCostOptionsDialog extends CrateDialog<KeyCostEntry> {

    private static final String INPUT_ID     = "id";
    private static final String INPUT_AMOUNT = "amount";

    private static final TextLocale TITLE = LangEntry.builder("Dialog.CostEntry.Key.Title").text(title("Cost Entry", "Key Options"));

    private static final DialogElementLocale BODY = LangEntry.builder("Dialog.CostEntry.Key.Body").dialogElement(400,
        "Specify which key and how many of it are required to open the crate.",
        "",
        "You can create and edit keys in the " + SOFT_YELLOW.wrap("ExcellentCrates Editor") + "."
    );

    private static final TextLocale LABEL_KEY    = LangEntry.builder("Dialog.CostEntry.Key.Input.Key").text(SOFT_YELLOW.wrap("Key"));
    private static final TextLocale LABEL_AMOUNT = LangEntry.builder("Dialog.CostEntry.Key.Input.Amount").text("Amount");

    private final CratesPlugin plugin;

    public KeyCostOptionsDialog(@NotNull CratesPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    @NotNull
    public WrappedDialog create(@NotNull Player player, @NotNull KeyCostEntry entry) {
        return Dialogs.create(builder -> {
            builder.base(DialogBases.builder(TITLE)
                .body(DialogBodies.plainMessage(BODY))
                .inputs(
                    DialogInputs.singleOption(INPUT_ID, LABEL_KEY, plugin.getKeyManager().getKeys().stream()
                        .map(key -> new WrappedSingleOptionEntry(key.getId(), key.getName(), entry.getKeyId().equalsIgnoreCase(key.getId())))
                        .toList()
                    ).build(),
                    DialogInputs.text(INPUT_AMOUNT, LABEL_AMOUNT).maxLength(10).initial(String.valueOf(entry.getAmount())).build()
                )
                .build()
            );

            builder.type(DialogTypes.multiAction(DialogButtons.ok())
                .exitAction(DialogButtons.back())
                .build()
            );

            builder.handleResponse(DialogActions.OK, (user, identifier, nbtHolder) -> {
                if (nbtHolder == null) return;

                String keyId = nbtHolder.getText(INPUT_ID, entry.getKeyId());
                int amount = nbtHolder.getInt(INPUT_AMOUNT, entry.getAmount());

                entry.setKeyId(keyId);
                entry.setAmount(amount);

                user.callback();
            });
        });
    }
}
