package su.nightexpress.excellentcrates.dialog.generic;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.dialog.CrateDialog;
import su.nightexpress.excellentcrates.util.ItemHelper;
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;
import su.nightexpress.nightcore.bridge.dialog.wrap.input.WrappedDialogInput;
import su.nightexpress.nightcore.bridge.item.AdaptedItem;
import su.nightexpress.nightcore.locale.entry.TextLocale;
import su.nightexpress.nightcore.ui.dialog.Dialogs;
import su.nightexpress.nightcore.ui.dialog.build.*;
import su.nightexpress.nightcore.util.ItemUtil;

import java.util.ArrayList;
import java.util.List;

public abstract class GenericItemDialog<T> extends CrateDialog<GenericItemDialog.Data<T>> {

    private static final String JSON_USE_NBT             = "use_nbt";
    private static final String JSON_REPLACE_NAME        = "replace_name";
    private static final String JSON_REPLACE_DESCRIPTION = "replace_description";

    public record Data<T>(@NotNull T source, @NotNull ItemStack itemStack){}

    public void show(@NotNull Player player, @NotNull T source, @NotNull ItemStack itemStack, @Nullable Runnable callback) {
        this.show(player, new Data<>(source, itemStack), callback);
    }

    @NotNull
    protected abstract TextLocale title();

    protected abstract void setName(@NotNull T source, @NotNull String name);

    protected abstract void setDescription(@NotNull T source, @NotNull List<String> description);

    protected abstract void setItem(@NotNull T source, @NotNull AdaptedItem item);

    @Override
    @NotNull
    public WrappedDialog create(@NotNull Player player, @NotNull Data<T> data) {
        T source = data.source;
        ItemStack itemStack = data.itemStack;
        ItemMeta meta = itemStack.getItemMeta();
        AdaptedItem adaptedItem = ItemHelper.adapt(itemStack);
        boolean isCustom = !adaptedItem.getAdapter().isVanilla();

        List<WrappedDialogInput> inputs = new ArrayList<>();

        if (meta != null) {
            if (meta.hasDisplayName() || meta.hasItemName()) {
                inputs.add(DialogInputs.bool(JSON_REPLACE_NAME, Lang.DIALOG_GENERIC_ITEM_INPUT_REP_NAME).build());
            }
            if (meta.hasLore()) {
                inputs.add(DialogInputs.bool(JSON_REPLACE_DESCRIPTION, Lang.DIALOG_GENERIC_ITEM_INPUT_REP_DESC).build());
            }
        }

        if (isCustom) {
            inputs.add(DialogInputs.bool(JSON_USE_NBT, Lang.DIALOG_GENERIC_ITEM_INPUT_NBT).initial(false).build());
        }

        return Dialogs.create(builder -> {

            builder.base(DialogBases.builder(this.title())
                .body(
                    // Re-adapt custom item for more accurate preview (useful for mixed (e.g. Nexo + ExecutableItems) items only)
                    DialogBodies.item(isCustom ? ItemHelper.toItemStack(adaptedItem) : itemStack).build(),
                    DialogBodies.plainMessage(isCustom ? Lang.DIALOG_GENERIC_ITEM_BODY_CUSTOM : Lang.DIALOG_GENERIC_ITEM_BODY_NORMAL)
                )
                .inputs(inputs)
                .build()
            );

            builder.type(DialogTypes.multiAction(DialogButtons.ok()).exitAction(DialogButtons.back()).build());

            builder.handleResponse(DialogActions.OK, (viewer, identifier, nbtHolder) -> {
                if (nbtHolder == null) return;

                boolean replaceName = nbtHolder.getBoolean(JSON_REPLACE_NAME).orElse(false);
                boolean replaceDesc = nbtHolder.getBoolean(JSON_REPLACE_DESCRIPTION).orElse(false);
                boolean allowCustoms = !nbtHolder.getBoolean(JSON_USE_NBT, false);

                AdaptedItem adapt = ItemHelper.adapt(itemStack, allowCustoms);
                if (replaceName && meta != null) {
                    this.setName(source, String.valueOf(ItemUtil.getNameSerialized(meta)));
                }
                if (replaceDesc && meta != null) {
                    this.setDescription(source, ItemUtil.getLoreSerialized(meta));
                }
                this.setItem(source, adapt);
                viewer.callback();
            });
        });
    }
}
