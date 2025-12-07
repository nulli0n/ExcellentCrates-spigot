package su.nightexpress.excellentcrates.dialog.reward;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.api.crate.Reward;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.crate.reward.impl.CommandReward;
import su.nightexpress.excellentcrates.dialog.CrateDialog;
import su.nightexpress.excellentcrates.util.ItemHelper;
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;
import su.nightexpress.nightcore.bridge.dialog.wrap.input.WrappedDialogInput;
import su.nightexpress.nightcore.bridge.item.AdaptedItem;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.DialogElementLocale;
import su.nightexpress.nightcore.locale.entry.TextLocale;
import su.nightexpress.nightcore.ui.dialog.Dialogs;
import su.nightexpress.nightcore.ui.dialog.build.*;
import su.nightexpress.nightcore.util.ItemUtil;

import java.util.ArrayList;
import java.util.List;

import static su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers.*;

public class RewardPreviewDialog extends CrateDialog<RewardPreviewDialog.Data> {

    private static final TextLocale TITLE          = LangEntry.builder("Dialog.Reward.Preview.Title").text(title("Reward", "Preview"));
    private static final TextLocale INPUT_NBT      = LangEntry.builder("Dialog.Reward.Preview.Input.NBT").text(SOFT_RED.wrap("Save as NBT"));
    private static final TextLocale INPUT_REP_NAME = LangEntry.builder("Dialog.Reward.Preview.Input.ReplaceName").text("Replace Reward Name");
    private static final TextLocale INPUT_REP_DESC = LangEntry.builder("Dialog.Reward.Preview.Input.ReplaceDesc").text("Replace Reward Description");

    private static final DialogElementLocale BODY_NORMAL = LangEntry.builder("Dialog.Reward.Preview.Body.Normal").dialogElement(400,
        "Please confirm reward preview replacement.",
        GRAY.wrap("Check the additional fields if needed.")
    );

    private static final DialogElementLocale BODY_CUSTOM = LangEntry.builder("Dialog.Reward.Preview.Body.Custom").dialogElement(400,
        "Please confirm reward preview replacement.",
        GRAY.wrap("Check the additional fields if needed."),
        "",
        SOFT_RED.and(BOLD).wrap("IMPORTANT NOTE:"),
        "If the item above doesn't match the one you used, enable the " + SOFT_RED.wrap("Save as NBT") + " option.",
        GRAY.wrap("This ensures the exact item data is saved correctly.")
    );

    private static final String JSON_USE_NBT             = "use_nbt";
    private static final String JSON_REPLACE_NAME        = "replace_name";
    private static final String JSON_REPLACE_DESCRIPTION = "replace_description";

    public record Data(@NotNull Reward reward, @NotNull ItemStack itemStack){}

    public void show(@NotNull Player player, @NotNull Reward reward, @NotNull ItemStack itemStack, @Nullable Runnable callback) {
        this.show(player, new Data(reward, itemStack), callback);
    }

    @Override
    @NotNull
    public WrappedDialog create(@NotNull Player player, @NotNull Data data) {
        Reward reward = data.reward;
        Crate crate = reward.getCrate();
        ItemStack itemStack = data.itemStack;
        ItemMeta meta = itemStack.getItemMeta();
        AdaptedItem adaptedItem = ItemHelper.adapt(itemStack);
        boolean isCustom = !adaptedItem.getAdapter().isVanilla();

        List<WrappedDialogInput> inputs = new ArrayList<>();

        if (reward instanceof CommandReward && meta != null) {
            if (meta.hasDisplayName() || meta.hasItemName()) {
                inputs.add(DialogInputs.bool(JSON_REPLACE_NAME, INPUT_REP_NAME).build());
            }
            if (meta.hasLore()) {
                inputs.add(DialogInputs.bool(JSON_REPLACE_DESCRIPTION, INPUT_REP_DESC).build());
            }
        }

        if (isCustom) {
            inputs.add(DialogInputs.bool(JSON_USE_NBT, INPUT_NBT).initial(false).build());
        }

        return Dialogs.create(builder -> {

            builder.base(DialogBases.builder(TITLE)
                .body(
                    // Re-adapt custom item for more accurate preview (useful for mixed (e.g. Nexo + ExecutableItems) items only)
                    DialogBodies.item(isCustom ? ItemHelper.toItemStack(adaptedItem) : itemStack).build(),
                    DialogBodies.plainMessage(isCustom ? BODY_CUSTOM : BODY_NORMAL)
                )
                .inputs(inputs)
                .build()
            );

            builder.type(DialogTypes.multiAction(DialogButtons.ok()).exitAction(DialogButtons.back()).build());

            builder.handleResponse(DialogActions.OK, (viewer, identifier, nbtHolder) -> {
                if (nbtHolder == null) return;

                boolean allowCustoms = !nbtHolder.getBoolean(JSON_USE_NBT, false);

                AdaptedItem adapt = ItemHelper.adapt(itemStack, allowCustoms);

                if (reward instanceof CommandReward commandReward) {
                    boolean replaceName = nbtHolder.getBoolean(JSON_REPLACE_NAME).orElse(false);
                    boolean replaceDesc = nbtHolder.getBoolean(JSON_REPLACE_DESCRIPTION).orElse(false);
                    if (replaceName && meta != null) {
                        commandReward.setName(String.valueOf(ItemUtil.getNameSerialized(meta)));
                    }
                    if (replaceDesc && meta != null) {
                        commandReward.setDescription(ItemUtil.getLoreSerialized(meta));
                    }
                }
                reward.setPreview(adapt);
                crate.markDirty();
                viewer.callback();
            });
        });
    }
}
