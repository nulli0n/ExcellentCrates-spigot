package su.nightexpress.excellentcrates.dialog.cost;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.crate.cost.entry.impl.EcoCostEntry;
import su.nightexpress.excellentcrates.dialog.CrateDialog;
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;
import su.nightexpress.nightcore.bridge.dialog.wrap.input.single.WrappedSingleOptionEntry;
import su.nightexpress.nightcore.integration.currency.EconomyBridge;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.DialogElementLocale;
import su.nightexpress.nightcore.locale.entry.TextLocale;
import su.nightexpress.nightcore.ui.dialog.Dialogs;
import su.nightexpress.nightcore.ui.dialog.build.*;

import static su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers.*;

public class CurrencyCostOptionsDialog extends CrateDialog<EcoCostEntry> {

    private static final String INPUT_ID     = "id";
    private static final String INPUT_AMOUNT = "amount";

    private static final TextLocale TITLE = LangEntry.builder("Dialog.CostEntry.Currency.Title").text(title("Cost Entry", "Currency Options"));

    private static final DialogElementLocale BODY = LangEntry.builder("Dialog.CostEntry.Currency.Body").dialogElement(400,
        "Specify which currency and how much of it are required to open the crate.",
        "",
        "You can learn more about currency integration here: " + SOFT_YELLOW.wrap(OPEN_URL.with(Placeholders.URL_WIKI_CURRENCIES).wrap("[Click to Open]"))
    );

    private static final TextLocale LABEL_CURRENCY = LangEntry.builder("Dialog.CostEntry.Currency.Input.Currency").text(SOFT_YELLOW.wrap("Currency"));
    private static final TextLocale LABEL_AMOUNT   = LangEntry.builder("Dialog.CostEntry.Currency.Input.Amount").text("Amount");

    @Override
    @NotNull
    public WrappedDialog create(@NotNull Player player, @NotNull EcoCostEntry entry) {
        return Dialogs.create(builder -> {
            builder.base(DialogBases.builder(TITLE)
                .body(DialogBodies.plainMessage(BODY))
                .inputs(
                    DialogInputs.singleOption(INPUT_ID, LABEL_CURRENCY, EconomyBridge.getCurrencies().stream()
                        .map(currency -> new WrappedSingleOptionEntry(currency.getInternalId(), currency.getName(), entry.getCurrencyId().equalsIgnoreCase(currency.getInternalId())))
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

                String currencyId = nbtHolder.getText(INPUT_ID, entry.getCurrencyId());
                double amount = nbtHolder.getDouble(INPUT_AMOUNT, entry.getAmount());

                entry.setCurrencyId(currencyId);
                entry.setAmount(amount);

                user.callback();
            });
        });
    }
}
