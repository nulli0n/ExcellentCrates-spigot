package su.nightexpress.excellentcrates.dialog.reward;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.api.crate.Reward;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.crate.limit.CooldownMode;
import su.nightexpress.excellentcrates.crate.limit.LimitValues;
import su.nightexpress.excellentcrates.dialog.CrateDialog;
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;
import su.nightexpress.nightcore.bridge.dialog.wrap.input.single.WrappedSingleOptionEntry;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.DialogElementLocale;
import su.nightexpress.nightcore.locale.entry.TextLocale;
import su.nightexpress.nightcore.ui.dialog.Dialogs;
import su.nightexpress.nightcore.ui.dialog.build.*;
import su.nightexpress.nightcore.util.Enums;

import java.util.ArrayList;
import java.util.List;

import static su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers.*;

public class RewardLimitsDialog extends CrateDialog<Reward> {

    private static final TextLocale TITLE = LangEntry.builder("Dialog.Reward.Limits.Title").text(title("Reward", "Limits"));

    private static final DialogElementLocale BODY = LangEntry.builder("Dialog.Reward.Limits.Body").dialogElement(400,
        "Here you can set how often and how many times this reward can be won.",
        "",
        SOFT_ORANGE.wrap("Global") + " settings apply to all players at once, while the " + SOFT_BLUE.wrap("Individual") + " ones apply individually.",
        "",
        SPRITE_NO_ATLAS.apply("item/clock_00") + " Use " + SOFT_YELLOW.wrap("Daily") + " cooldown mode to reset the cooldown at " + SOFT_YELLOW.wrap("midnight") + " after the specified number of days.",
        SPRITE_NO_ATLAS.apply("item/clock_00") + " Use " + SOFT_YELLOW.wrap("Custom") + " cooldown mode to set an exact cooldown time in " + SOFT_YELLOW.wrap("seconds") + ".",
        "",
        SPRITE_NO_ATLAS.apply("item/barrier") + " To disable cooldown, set it's value to " + SOFT_RED.wrap("0") + ".",
        SPRITE_NO_ATLAS.apply("item/barrier") + " To remove the rolls limit, set it's value to " + SOFT_RED.wrap("-1") + "."
    );

    private static final TextLocale INPUT_ENABLED         = LangEntry.builder("Dialog.Reward.Limits.Input.Enabled").text("Enabled");
    private static final TextLocale INPUT_COOLDOWN_MODE   = LangEntry.builder("Dialog.Reward.Limits.Input.CooldownMode").text(SOFT_YELLOW.wrap("Cooldown Mode"));
    private static final TextLocale INPUT_GLOBAL_AMOUNT   = LangEntry.builder("Dialog.Reward.Limits.Input.GlobalAmount").text(SOFT_ORANGE.wrap("(Global)") + " Max. Rolls");
    private static final TextLocale INPUT_GLOBAL_COOLDOWN = LangEntry.builder("Dialog.Reward.Limits.Input.GlobalCooldown").text(SOFT_ORANGE.wrap("(Global)") + " Cooldown");
    private static final TextLocale INPUT_PLAYER_AMOUNT   = LangEntry.builder("Dialog.Reward.Limits.Input.PlayerAmount").text(SOFT_BLUE.wrap("(Individual)") + " Max. Rolls");
    private static final TextLocale INPUT_PLAYER_COOLDOWN = LangEntry.builder("Dialog.Reward.Limits.Input.PlayerCooldown").text(SOFT_BLUE.wrap("(Individual)") + " Cooldown");

    private static final String JSON_ENABLED       = "enabled";
    private static final String JSON_COOLDOWN_MODE = "cooldown_mode";
    private static final String JSON_GLOBAL_AMOUNT = "g_amount";
    private static final String JSON_GLOBAL_COOLDOWN = "g_cooldown";
    private static final String JSON_PLAYER_AMOUNT   = "p_amount";
    private static final String JSON_PLAYER_COOLDOWN = "p_cooldown";

    @Override
    @NotNull
    public WrappedDialog create(@NotNull Player player, @NotNull Reward reward) {
        LimitValues limits = reward.getLimits();
        List<WrappedSingleOptionEntry> types = new ArrayList<>();

        for (CooldownMode type : CooldownMode.values()) {
            types.add(new WrappedSingleOptionEntry(type.id(), Lang.COOLDOWN_MODE.getLocalized(type), limits.getCooldownType() == type));
        }

        return Dialogs.create(builder -> {
            builder.base(DialogBases.builder(TITLE)
                .body(DialogBodies.plainMessage(BODY))
                .inputs(
                    DialogInputs.bool(JSON_ENABLED, INPUT_ENABLED).initial(limits.isEnabled()).build(),
                    DialogInputs.singleOption(JSON_COOLDOWN_MODE, INPUT_COOLDOWN_MODE, types).build(),
                    DialogInputs.text(JSON_GLOBAL_AMOUNT, INPUT_GLOBAL_AMOUNT).initial(String.valueOf(limits.getGlobalAmount())).maxLength(7).build(),
                    DialogInputs.text(JSON_GLOBAL_COOLDOWN, INPUT_GLOBAL_COOLDOWN).initial(String.valueOf(limits.getGlobalCooldown())).maxLength(7).build(),
                    DialogInputs.text(JSON_PLAYER_AMOUNT, INPUT_PLAYER_AMOUNT).initial(String.valueOf(limits.getPlayerAmount())).maxLength(7).build(),
                    DialogInputs.text(JSON_PLAYER_COOLDOWN, INPUT_PLAYER_COOLDOWN).initial(String.valueOf(limits.getPlayerCooldown())).maxLength(7).build()
                )
                .build()
            );

            builder.type(DialogTypes.multiAction(DialogButtons.ok()).exitAction(DialogButtons.back()).build());

            builder.handleResponse(DialogActions.OK, (viewer, identifier, nbtHolder) -> {
                if (nbtHolder == null) return;

                boolean enabled = nbtHolder.getBoolean(JSON_ENABLED, limits.isEnabled());
                CooldownMode cooldownType = nbtHolder.getText(JSON_COOLDOWN_MODE).map(raw -> Enums.get(raw, CooldownMode.class)).orElse(limits.getCooldownType());
                int globalAmount = nbtHolder.getInt(JSON_GLOBAL_AMOUNT, limits.getGlobalAmount());
                long globalCooldown = nbtHolder.getInt(JSON_GLOBAL_COOLDOWN, (int) limits.getGlobalCooldown());
                int playerAmount = nbtHolder.getInt(JSON_PLAYER_AMOUNT, limits.getPlayerAmount());
                long playerCooldown = nbtHolder.getInt(JSON_PLAYER_COOLDOWN, (int) limits.getPlayerCooldown());

                limits.setEnabled(enabled);
                limits.setCooldownType(cooldownType);
                limits.setGlobalAmount(globalAmount);
                limits.setGlobalCooldown(globalCooldown);
                limits.setPlayerAmount(playerAmount);
                limits.setPlayerCooldown(playerCooldown);
                reward.getCrate().markDirty();
                viewer.callback();
            });
        });
    }
}
