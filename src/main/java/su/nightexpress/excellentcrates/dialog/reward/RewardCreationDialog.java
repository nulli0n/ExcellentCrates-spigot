package su.nightexpress.excellentcrates.dialog.reward;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.Reward;
import su.nightexpress.excellentcrates.api.crate.RewardType;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.crate.reward.RewardFactory;
import su.nightexpress.excellentcrates.dialog.CrateDialog;
import su.nightexpress.excellentcrates.util.ItemHelper;
import su.nightexpress.nightcore.bridge.common.NightNbtHolder;
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;
import su.nightexpress.nightcore.bridge.dialog.wrap.button.WrappedActionButton;
import su.nightexpress.nightcore.bridge.dialog.wrap.input.WrappedDialogInput;
import su.nightexpress.nightcore.bridge.item.AdaptedItem;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.ButtonLocale;
import su.nightexpress.nightcore.locale.entry.DialogElementLocale;
import su.nightexpress.nightcore.locale.entry.TextLocale;
import su.nightexpress.nightcore.ui.dialog.Dialogs;
import su.nightexpress.nightcore.ui.dialog.build.*;
import su.nightexpress.nightcore.util.Enums;
import su.nightexpress.nightcore.util.ItemUtil;

import java.util.ArrayList;
import java.util.List;

import static su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers.*;

public class RewardCreationDialog extends CrateDialog<RewardCreationDialog.Data> {

    private static final TextLocale TITLE        = LangEntry.builder("Dialog.Rewards.Creation.Title").text(title("Reward", "Creation"));
    private static final TextLocale INPUT_METHOD = LangEntry.builder("Dialog.Rewards.Creation.Input.Method").text("Save Item as NBT");

    private static final ButtonLocale BUTTON_ITEM_TYPE = LangEntry.builder("Dialog.Rewards.Creation.Button.ItemType")
        .button(
            SOFT_BLUE.wrap("Item Reward"),
            "The " + SOFT_BLUE.wrap("Item") + " reward type gives the player one or more items when won." + BR + BR + "By default, " + SOFT_YELLOW.wrap("%1$s") + " will used as the item, but you can change it at any time."
        );

    private static final ButtonLocale BUTTON_COMMAND_TYPE = LangEntry.builder("Dialog.Rewards.Creation.Button.CommandType")
        .button(
            SOFT_ORANGE.wrap("Command Reward"),
            "The " + SOFT_ORANGE.wrap("Command") + " reward type runs one or more commands when won." + BR + BR + "By default, " + SOFT_YELLOW.wrap("%1$s") + " will used as the preview, but you can change it at any time."
        );

    private static final DialogElementLocale BODY_NORMAL = LangEntry.builder("Dialog.Rewards.Creation.Body.Normal").dialogElement(400,
        "Please select a reward type to create.",
        GRAY.wrap("Hover over a button to see a short description of each type.")
    );

    private static final DialogElementLocale BODY_CUSTOM = LangEntry.builder("Dialog.Rewards.Creation.Body.Custom").dialogElement(400,
        "Please select a reward type to create.",
        GRAY.wrap("Hover over a button to see a short description of each type."),
        "",
        SOFT_RED.and(BOLD).wrap("IMPORTANT NOTE:"),
        "If the item above doesn't match the one you used, enable the " + SOFT_RED.wrap("Save Item as NBT") + " option.",
        GRAY.wrap("This ensures the exact item data is saved correctly.")
    );

    private static final String JSON_USE_NBT = "use_nbt";
    private static final String JSON_TYPE    = "type";

    public record Data(@NotNull Crate crate, @NotNull ItemStack itemStack){}

    private final CratesPlugin plugin;

    public RewardCreationDialog(@NotNull CratesPlugin plugin) {
        this.plugin = plugin;
    }

    public void show(@NotNull Player player, @NotNull Crate crate, @NotNull ItemStack itemStack, @Nullable Runnable callback) {
        this.show(player, new Data(crate, itemStack), callback);
    }

    @Override
    @NotNull
    public WrappedDialog create(@NotNull Player player, @NotNull Data data) {
        Crate crate = data.crate;
        ItemStack itemStack = data.itemStack;
        AdaptedItem adaptedItem = ItemHelper.adapt(itemStack);
        boolean isCustom = !adaptedItem.getAdapter().isVanilla();

        List<WrappedDialogInput> inputs = new ArrayList<>();
        List<WrappedActionButton> buttons = new ArrayList<>();

        for (RewardType type : RewardType.values()) {
            ButtonLocale locale = (switch (type) {
                case ITEM -> BUTTON_ITEM_TYPE;
                case COMMAND -> BUTTON_COMMAND_TYPE;
            }).replace(str -> str.formatted(ItemUtil.getNameSerialized(itemStack)));

            buttons.add(DialogButtons.action(locale).action(DialogActions.customClick(DialogActions.OK, NightNbtHolder.builder().put(JSON_TYPE, type.name()).build())).build());
        }

        if (isCustom) {
            inputs.add(DialogInputs.bool(JSON_USE_NBT, INPUT_METHOD).initial(false).build());
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

            builder.type(DialogTypes.multiAction(buttons).exitAction(DialogButtons.back()).build());

            builder.handleResponse(DialogActions.OK, (viewer, identifier, nbtHolder) -> {
                if (nbtHolder == null) return;

                RewardType type = Enums.parse(nbtHolder.getText(JSON_TYPE, RewardType.ITEM.name()), RewardType.class).orElse(RewardType.ITEM);
                boolean allowCustoms = !nbtHolder.getBoolean(JSON_USE_NBT, false);

                AdaptedItem adapt = ItemHelper.adapt(itemStack, allowCustoms);
                Reward reward = RewardFactory.wizardCreation(this.plugin, crate, itemStack, type, adapt);
                crate.addReward(reward);
                crate.markDirty();
                viewer.closeFully();
            });
        });
    }
}
