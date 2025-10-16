package su.nightexpress.excellentcrates.dialog.generic;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.dialog.CrateDialog;
import su.nightexpress.excellentcrates.util.ItemHelper;
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;
import su.nightexpress.nightcore.bridge.dialog.wrap.input.WrappedDialogInput;
import su.nightexpress.nightcore.bridge.dialog.wrap.input.text.WrappedMultilineOptions;
import su.nightexpress.nightcore.bridge.item.AdaptedItem;
import su.nightexpress.nightcore.bridge.item.ItemAdapter;
import su.nightexpress.nightcore.locale.entry.TextLocale;
import su.nightexpress.nightcore.ui.dialog.Dialogs;
import su.nightexpress.nightcore.ui.dialog.build.*;
import su.nightexpress.nightcore.util.ItemUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class GenericDescriptionDialog<T> extends CrateDialog<T> {

    protected static final String JSON_DESCRIPTION  = "description";
    protected static final String JSON_REPLACE_LORE = "replace_lore";

    @NotNull
    protected abstract TextLocale title();

    @NotNull
    protected abstract AdaptedItem getItem(@NotNull T source);

    protected abstract void setItem(@NotNull T source, @NotNull AdaptedItem item);

    @NotNull
    protected abstract List<String> getDescription(@NotNull T source);

    protected abstract void setDescription(@NotNull T source, @NotNull List<String> description);

    protected abstract void save(@NotNull T source);

    @Override
    @NotNull
    public WrappedDialog create(@NotNull Player player, @NotNull T source) {
        List<WrappedDialogInput> inputs = new ArrayList<>();

        AdaptedItem crateItem = this.getItem(source);
        ItemAdapter<?> itemAdapter = crateItem.getAdapter();

        inputs.add(DialogInputs.text(JSON_DESCRIPTION, Lang.DIALOG_GENERIC_DESCRIPTION_INPUT_DESC)
            .initial(String.join("\n", this.getDescription(source)))
            .maxLength(500)
            .width(300)
            .multiline(new WrappedMultilineOptions(10, 150))
            .build()
        );

        // It makes no sense to replace custom item's name.
        if (itemAdapter.isVanilla() && crateItem.isValid()) {
            inputs.add(DialogInputs.bool(JSON_REPLACE_LORE, Lang.DIALOG_GENERIC_DESCRIPTION_INPUT_REPLACE_LORE).build());
        }

        return Dialogs.create(builder -> {
            builder.base(DialogBases.builder(this.title())
                .body(DialogBodies.plainMessage(Lang.DIALOG_GENERIC_DESCRIPTION_BODY))
                .inputs(inputs)
                .build()
            );

            builder.type(DialogTypes.multiAction(DialogButtons.ok())
                .exitAction(DialogButtons.back())
                .build()
            );

            builder.handleResponse(DialogActions.OK, (user, identifier, nbtHolder) -> {
                if (nbtHolder == null) return;

                String raw = nbtHolder.getText(JSON_DESCRIPTION).orElse(null);
                if (raw == null) return;

                List<String> description = Arrays.asList(raw.split("\n"));
                boolean replace = nbtHolder.getBoolean(JSON_REPLACE_LORE, false);

                if (replace) {
                    ItemStack itemStack = ItemHelper.toItemStack(crateItem);
                    ItemUtil.editMeta(itemStack, meta -> ItemUtil.setLore(meta, description));
                    this.setItem(source, ItemHelper.vanilla(itemStack));
                }

                this.setDescription(source, description);
                this.save(source);
                user.callback();
            });
        });
    }
}
