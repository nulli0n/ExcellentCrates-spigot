package su.nightexpress.excellentcrates.reward.editor.ui.dialog;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.IdentifiableRegistry;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.id.IdentifierParser;
import su.nightexpress.excellentcrates.api.reward.editor.RewardSorter;
import su.nightexpress.excellentcrates.reward.editor.lang.RewardEditorLang;
import su.nightexpress.excellentcrates.reward.editor.ui.RewardEditorUIController;
import su.nightexpress.excellentcrates.reward.editor.ui.dialog.context.RewardSortDialogContext;
import su.nightexpress.nightcore.bridge.common.NightNbtHolder;
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;
import su.nightexpress.nightcore.bridge.dialog.wrap.action.WrappedDialogAction;
import su.nightexpress.nightcore.bridge.dialog.wrap.button.WrappedActionButton;
import su.nightexpress.nightcore.bridge.dialog.wrap.input.single.WrappedSingleOptionEntry;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.ui.dialog.Dialogs;
import su.nightexpress.nightcore.ui.dialog.build.DialogActions;
import su.nightexpress.nightcore.ui.dialog.build.DialogBases;
import su.nightexpress.nightcore.ui.dialog.build.DialogBodies;
import su.nightexpress.nightcore.ui.dialog.build.DialogButtons;
import su.nightexpress.nightcore.ui.dialog.build.DialogInputs;
import su.nightexpress.nightcore.ui.dialog.build.DialogTypes;
import su.nightexpress.nightcore.ui.dialog.wrap.Dialog;

@NullMarked
public class RewardSortDialog extends Dialog<RewardSortDialogContext> {

    private static final String ACTION_SORT_BUTTON = "sort_button";
    private static final String KEY_SORTER         = "sorter";
    private static final String KEY_INVERT         = "invert";

    private final IdentifiableRegistry<RewardSorter> sorters;
    private final RewardEditorUIController           controller;

    public RewardSortDialog(IdentifiableRegistry<RewardSorter> sorters, RewardEditorUIController controller) {
        super();
        this.sorters = sorters;
        this.controller = controller;
    }

    @Override
    public WrappedDialog create(Player player, RewardSortDialogContext context) {
        List<WrappedActionButton> sortButtons = new ArrayList<>();
        List<WrappedSingleOptionEntry> boolEntries = new ArrayList<>();

        this.sorters.values().forEach(sorter -> {
            String key = sorter.idString();
            String name = sorter.getName().resolve();

            NightNbtHolder nbt = NightNbtHolder.builder().put(KEY_SORTER, key).build();
            WrappedDialogAction action = DialogActions.customClick(ACTION_SORT_BUTTON, nbt);

            WrappedActionButton sortButton = DialogButtons.action(name)
                .action(action)
                .build();

            sortButtons.add(sortButton);
        });

        for (boolean value : new boolean[]{true, false}) {
            String key = String.valueOf(value);
            String display = CoreLang.STATE_ENABLED_DISALBED.get(value);
            boolean initial = !value;

            WrappedSingleOptionEntry entry = new WrappedSingleOptionEntry(key, display, initial);
            boolEntries.add(entry);
        }

        return Dialogs.create(builder -> {
            builder.base(DialogBases.builder(RewardEditorLang.UI_DIALOG_SORT_TITLE)
                .body(DialogBodies.plain(RewardEditorLang.UI_DIALOG_SORT_BODY).build())
                .inputs(
                    DialogInputs
                        .singleOption(KEY_INVERT, RewardEditorLang.UI_DIALOG_SORT_INPUT_INVERT, boolEntries)
                        .build()
                )
                .build()
            );

            builder.type(DialogTypes.multiAction(sortButtons).exitAction(DialogButtons.cancel()).build());

            builder.handleResponse(ACTION_SORT_BUTTON, (viewer, identifier, nbtHolder) -> {
                if (nbtHolder == null) return;

                String sorterKey = nbtHolder.getText(KEY_SORTER).orElse(null);
                if (sorterKey == null) return;

                Identifier sorterId = IdentifierParser.parse(sorterKey).orElse(null);
                if (sorterId == null) return;

                boolean invert = nbtHolder.getBoolean(KEY_INVERT, false);

                Player clicker = viewer.getPlayer();
                if (this.controller.onSortDialogSelect(clicker, context, sorterId, invert)) {
                    viewer.callback();
                }
            });
        });
    }
}
