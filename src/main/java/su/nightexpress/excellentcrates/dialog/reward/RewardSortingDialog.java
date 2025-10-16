package su.nightexpress.excellentcrates.dialog.reward;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.api.crate.Reward;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.dialog.CrateDialog;
import su.nightexpress.nightcore.bridge.common.NightNbtHolder;
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;
import su.nightexpress.nightcore.bridge.dialog.wrap.button.WrappedActionButton;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.ButtonLocale;
import su.nightexpress.nightcore.locale.entry.DialogElementLocale;
import su.nightexpress.nightcore.locale.entry.EnumLocale;
import su.nightexpress.nightcore.locale.entry.TextLocale;
import su.nightexpress.nightcore.ui.dialog.Dialogs;
import su.nightexpress.nightcore.ui.dialog.build.*;
import su.nightexpress.nightcore.util.BukkitThing;
import su.nightexpress.nightcore.util.Enums;
import su.nightexpress.nightcore.util.LowerCase;
import su.nightexpress.nightcore.util.text.night.NightMessage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers.*;

public class RewardSortingDialog extends CrateDialog<Crate> {

    private static final EnumLocale<SortMode> SORT_MODE_LOCALE = LangEntry.builder("Dialog.Rewards.Sorting.Mode").enumeration(SortMode.class);

    private static final TextLocale TITLE = LangEntry.builder("Dialog.Rewards.Sorting.Title").text(title("Rewards", "Sorting"));

    private static final DialogElementLocale BODY = LangEntry.builder("Dialog.Rewards.Sorting.Body").dialogElement(400,
        "Please select a sorting mode."
    );

    public static final TextLocale INPUT_REVERSED = LangEntry.builder("Dialog.Rewards.Sorting.Input.Reversed").text("Reversed");

    private static final ButtonLocale BUTTON_MODE = LangEntry.builder("Dialog.Rewards.Sorting.Button.Mode")
        .button(SOFT_YELLOW.wrap("→") + " Mode: " + SOFT_YELLOW.wrap("%s"));

    private static final String JSON_REVERSED = "reversed";
    private static final String JSON_MODE     = "mode";

    @Override
    @NotNull
    public WrappedDialog create(@NotNull Player player, @NotNull Crate crate) {
        List<WrappedActionButton> buttons = new ArrayList<>();

        for (SortMode mode : SortMode.values()) {
            buttons.add(
                DialogButtons.action(BUTTON_MODE.replace(str -> str.formatted(SORT_MODE_LOCALE.getLocalized(mode))))
                    .action(DialogActions.customClick(DialogActions.OK, NightNbtHolder.builder().put(JSON_MODE, LowerCase.INTERNAL.apply(mode.name())).build()))
                    .build()
            );
        }

        return Dialogs.create(builder -> {
            builder.base(DialogBases.builder(TITLE)
                .inputs(DialogInputs.bool(JSON_REVERSED, INPUT_REVERSED).initial(false).build())
                .body(DialogBodies.plainMessage(BODY))
                .build()
            );

            builder.type(DialogTypes.multiAction(buttons).exitAction(DialogButtons.back()).build());

            builder.handleResponse(DialogActions.OK, (viewer, identifier, nbtHolder) -> {
                if (nbtHolder == null) return;

                String raw = nbtHolder.getText(JSON_MODE).orElse(null);
                if (raw == null) return;

                SortMode mode = Enums.get(raw, SortMode.class);
                if (mode == null) return;

                boolean reversed = nbtHolder.getBoolean(JSON_REVERSED, false);
                Comparator<Reward> comparator = mode.getComparator(reversed);

                crate.setRewards(crate.getRewards().stream().sorted(comparator).toList());
                crate.markDirty();
                viewer.closeFully();
            });
        });
    }

    private enum SortMode {

        WEIGHT(Comparator.comparingDouble(Reward::getWeight)),
        RARITY(Comparator.comparingDouble((Reward reward) -> reward.getRarity().getWeight())),
        CHANCE(Comparator.comparingDouble(Reward::getRollChance)),
        NAME(Comparator.comparing(reward -> NightMessage.stripTags(reward.getName()))),
        ITEM(Comparator.comparing(reward -> BukkitThing.getValue(reward.getPreviewItem().getType())));

        private final Comparator<Reward> comparator;

        SortMode(@NotNull Comparator<Reward> comparator) {
            this.comparator = comparator;
        }

        @NotNull
        public Comparator<Reward> getComparator(boolean reversed) {
            return reversed ? this.comparator.reversed() : this.comparator;
        }
    }
}
