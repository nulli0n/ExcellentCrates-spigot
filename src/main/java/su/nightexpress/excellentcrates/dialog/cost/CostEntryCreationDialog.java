package su.nightexpress.excellentcrates.dialog.cost;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.api.cost.CostEntry;
import su.nightexpress.excellentcrates.api.cost.CostType;
import su.nightexpress.excellentcrates.crate.cost.Cost;
import su.nightexpress.excellentcrates.dialog.CrateDialog;
import su.nightexpress.excellentcrates.registry.CratesRegistries;
import su.nightexpress.nightcore.bridge.common.NightNbtHolder;
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;
import su.nightexpress.nightcore.bridge.dialog.wrap.button.WrappedActionButton;
import su.nightexpress.nightcore.locale.entry.DialogElementLocale;
import su.nightexpress.nightcore.locale.entry.TextLocale;
import su.nightexpress.nightcore.ui.dialog.Dialogs;
import su.nightexpress.nightcore.ui.dialog.build.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers.*;

public class CostEntryCreationDialog extends CrateDialog<Cost> {

    private static final String ACTION_TYPE = "type";
    private static final String JSON_ID     = "id";

    private static final TextLocale TITLE = TextLocale.builder("Dialog.CostEntry.Creation.Title").text(title("Cost Entry", "Creation"));

    private static final DialogElementLocale BODY = DialogElementLocale.builder("Dialog.CostEntry.Creation.Body").dialogElement(400,
        "Select a cost type."
    );

    private static final DialogElementLocale BODY_EMPTY = DialogElementLocale.builder("Dialog.CostEntry.Creation.BodyEmpty").dialogElement(400,
        SOFT_RED.and(UNDERLINED).wrap("NO AVAILABLE COST TYPES"),
        "",
        "You may see this message for one of the following reasons:",
        "",
        SOFT_RED.wrap("→") + " The " + SOFT_RED.wrap("Keys") + " module is unavailable or has no keys created. You can create keys using the ExcellentCrates editor.",
        "",
        SOFT_RED.wrap("→") + " No supported " + SOFT_RED.wrap("economy") + " or " + SOFT_RED.wrap("currency") + " plugins are installed, or their integration is disabled in the main " + SOFT_RED.wrap("nightcore") + " configuration file."
    );

    @Override
    @NotNull
    public WrappedDialog create(@NotNull Player player, @NotNull Cost cost) {
        List<WrappedActionButton> buttons = new ArrayList<>();

        Set<CostType> costTypes = CratesRegistries.getAvailableCostTypes();
        costTypes.stream().sorted(Comparator.comparing(CostType::getId)).forEach(costType -> {
            NightNbtHolder nbtHolder = NightNbtHolder.builder().put(JSON_ID, costType.getId()).build();

            buttons.add(DialogButtons.action(costType.getName()).action(DialogActions.customClick(ACTION_TYPE, nbtHolder)).build());
        });

        return Dialogs.create(builder -> {
            builder.base(DialogBases.builder(TITLE)
                .body(DialogBodies.plainMessage(costTypes.isEmpty() ? BODY_EMPTY : BODY))
                .build()
            );

            if (!costTypes.isEmpty()) {
                builder.type(DialogTypes.multiAction(buttons).exitAction(DialogButtons.back()).build());
            }
            else {
                builder.type(DialogTypes.notice(DialogButtons.back()));
            }

            builder.handleResponse(ACTION_TYPE, (user, identifier, nbtHolder) -> {
                if (nbtHolder == null) return;

                String id = nbtHolder.getText(JSON_ID).orElse(null);
                if (id == null) return;

                CostType type = CratesRegistries.COST_TYPE.byKey(id);
                if (type == null) return;

                CostEntry entry = type.createEmpty();
                cost.addEntry(entry);
                user.callback();
                entry.openEditor(player, user.getCallback());
            });
        });
    }
}
