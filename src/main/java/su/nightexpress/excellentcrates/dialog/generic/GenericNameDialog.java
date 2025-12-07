package su.nightexpress.excellentcrates.dialog.generic;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.dialog.CrateDialog;
import su.nightexpress.excellentcrates.util.ItemHelper;
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;
import su.nightexpress.nightcore.bridge.dialog.wrap.input.WrappedDialogInput;
import su.nightexpress.nightcore.bridge.item.AdaptedItem;
import su.nightexpress.nightcore.bridge.item.ItemAdapter;
import su.nightexpress.nightcore.locale.entry.TextLocale;
import su.nightexpress.nightcore.ui.dialog.Dialogs;
import su.nightexpress.nightcore.ui.dialog.build.*;
import su.nightexpress.nightcore.util.ItemUtil;

import java.util.ArrayList;
import java.util.List;

public abstract class GenericNameDialog<T> extends CrateDialog<T> {

    private static final String JSON_NAME         = "name";
    private static final String JSON_REPLACE_NAME = "replace_name";

    @NotNull
    protected abstract TextLocale title();

    @NotNull
    protected abstract AdaptedItem getItem(@NotNull T source);

    protected abstract void setItem(@NotNull T source, @NotNull AdaptedItem item);

    @NotNull
    protected abstract String getName(@NotNull T source);

    protected abstract void setName(@NotNull T source, @NotNull String name);

    protected abstract void save(@NotNull T source);

    @Override
    @NotNull
    public WrappedDialog create(@NotNull Player player, @NotNull T source) {
        List<WrappedDialogInput> inputs = new ArrayList<>();

        AdaptedItem crateItem = this.getItem(source);
        ItemAdapter<?> itemAdapter = crateItem.getAdapter();

        inputs.add(DialogInputs.text(JSON_NAME, Lang.DIALOG_GENERIC_NAME_INPUT_NAME).initial(this.getName(source)).maxLength(100).build());

        // It makes no sense to replace custom item's name.
        if (itemAdapter.isVanilla() && crateItem.isValid()) {
            inputs.add(DialogInputs.bool(JSON_REPLACE_NAME, Lang.DIALOG_GENERIC_NAME_INPUT_REPLACE_NAME).build());
        }

        return Dialogs.create(builder -> {
            builder.base(DialogBases.builder(this.title())
                .body(DialogBodies.plainMessage(Lang.DIALOG_GENERIC_NAME_BODY))
                .inputs(inputs)
                .build()
            );

            builder.type(DialogTypes.multiAction(DialogButtons.ok())
                .exitAction(DialogButtons.back())
                .build()
            );

            builder.handleResponse(DialogActions.OK, (viewer, identifier, nbtHolder) -> {
                if (nbtHolder == null) return;

                String name = nbtHolder.getText(JSON_NAME, this.getName(source));
                boolean replace = nbtHolder.getBoolean(JSON_REPLACE_NAME, false);

                if (replace) {
                    ItemStack itemStack = ItemHelper.toItemStack(crateItem);
                    ItemUtil.editMeta(itemStack, meta -> ItemUtil.setCustomName(meta, name));
                    this.setItem(source, ItemHelper.vanilla(itemStack));
                }

                this.setName(source, name);
                this.save(source);
                viewer.callback();
            });
        });
    }
}
