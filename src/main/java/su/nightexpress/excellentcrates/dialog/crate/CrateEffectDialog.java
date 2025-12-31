package su.nightexpress.excellentcrates.dialog.crate;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.crate.CrateDialogs;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.dialog.Dialog;
import su.nightexpress.excellentcrates.dialog.DialogRegistry;
import su.nightexpress.excellentcrates.registry.CratesRegistries;
import su.nightexpress.nightcore.bridge.common.NightNbtHolder; // Import Added
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;
import su.nightexpress.nightcore.bridge.dialog.wrap.base.WrappedDialogAfterAction;
import su.nightexpress.nightcore.bridge.dialog.wrap.button.WrappedActionButton; // Import Added
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.ButtonLocale;
import su.nightexpress.nightcore.locale.entry.DialogElementLocale;
import su.nightexpress.nightcore.locale.entry.TextLocale;
import su.nightexpress.nightcore.ui.dialog.Dialogs;
import su.nightexpress.nightcore.ui.dialog.build.*;

import java.util.ArrayList;
import java.util.List;

import static su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers.*;

public class CrateEffectDialog extends Dialog<Crate> {

    private static final TextLocale TITLE = LangEntry.builder("Dialog.Crate.Effect.Title").text(title("Crate", "Block Effect"));

    private static final DialogElementLocale BODY = LangEntry.builder("Dialog.Crate.Effect.Body").dialogElement(400,
            "Here you can choose the " + SOFT_YELLOW.wrap("effect model") + " and " + SOFT_YELLOW.wrap("particle type") + " for the crate.",
            "These effects are shown around the blocks linked to the crate.",
            "",
            "Current Effect: %1$s" // Modified to show status
    );

    private static final ButtonLocale BUTTON_PARTICLE = LangEntry.builder("Dialog.Crate.Effect.Button.Particle").button(SOFT_YELLOW.wrap("Particle: ") + "%s");

    private static final String ACTION_PARTICLE = "particle";
    private static final String JSON_ENABLED = "enabled";
    private static final String JSON_MODEL   = "model";

    private final DialogRegistry dialogs;

    public CrateEffectDialog(@NotNull DialogRegistry dialogs) {
        this.dialogs = dialogs;
    }

    @Override
    @NotNull
    public WrappedDialog create(@NotNull Player player, @NotNull Crate crate) {
        List<WrappedActionButton> buttons = new ArrayList<>();

        // --- BUTTON 1: Edit Particle Type ---
        // We keep this at the top so users can easily change the particle (Smoke, Flame, etc)
        String particleName = Lang.PARTICLE.getLocalized(crate.getEffectParticle().getParticle());
        buttons.add(DialogButtons.action(BUTTON_PARTICLE.replace(str -> str.formatted(particleName)))
                .action(DialogActions.customClick(ACTION_PARTICLE))
                .build());

        // --- BUTTON 2: Disable Effect ---
        // Clicking this disables the effect entirely
        NightNbtHolder disableNbt = NightNbtHolder.builder()
                .put(JSON_ENABLED, false)
                .put(JSON_MODEL, "")
                .build();

        buttons.add(DialogButtons.action(SOFT_RED.wrap("Disable Effect"))
                .action(DialogActions.customClick(DialogActions.OK, disableNbt))
                .build());

        // --- BUTTONS 3+: Effect Models (Helix, Fountain, etc) ---
        CratesRegistries.getEffects().forEach(effect -> {
            boolean isSelected = crate.isEffectEnabled() && crate.getEffect() == effect;
            String label = isSelected ? SOFT_GREEN.wrap(effect.getName() + " (Selected)") : SOFT_YELLOW.wrap(effect.getName());

            // Prepare data: Enable the effect + set the model ID
            NightNbtHolder nbt = NightNbtHolder.builder()
                    .put(JSON_ENABLED, true)
                    .put(JSON_MODEL, effect.getId())
                    .build();

            buttons.add(DialogButtons.action(label)
                    .action(DialogActions.customClick(DialogActions.OK, nbt))
                    .build());
        });

        return Dialogs.create(builder -> {
            // Update Body text to show what is currently selected
            String status = crate.isEffectEnabled() && crate.getEffect() != null
                    ? SOFT_GREEN.wrap(crate.getEffect().getName())
                    : SOFT_RED.wrap("Disabled");

            builder.base(DialogBases.builder(TITLE)
                    .body(DialogBodies.plainMessage(BODY))
                    .afterAction(WrappedDialogAfterAction.NONE)
                    .build() // No inputs needed anymore
            );

            builder.type(DialogTypes.multiAction(buttons)
                    .exitAction(DialogButtons.back())
                    .columns(3) // Grid layout
                    .build());

            // Handler: Open Particle Menu
            builder.handleResponse(ACTION_PARTICLE, (viewer, identifier, nbtHolder) -> {
                this.dialogs.show(player, CrateDialogs.CRATE_PARTICLE, crate, () -> this.show(player, crate, viewer.getCallback()));
            });

            // Handler: Go Back
            builder.handleResponse(DialogActions.BACK, (viewer, identifier, nbtHolder) -> {
                viewer.callback(); // Uses callback to return to previous menu nicely
            });

            // Handler: Select Effect (or Disable)
            builder.handleResponse(DialogActions.OK, (viewer, identifier, nbtHolder) -> {
                if (nbtHolder == null) return;

                boolean enabled = nbtHolder.getBoolean(JSON_ENABLED, false);
                String modelId = nbtHolder.getText(JSON_MODEL).orElse(null);

                crate.setEffectEnabled(enabled);

                // Only update the model type if one was actually clicked (not if we just disabled it)
                if (enabled && modelId != null) {
                    crate.setEffectType(modelId);
                }

                crate.markDirty();

                // Refresh the menu to show the new selection (Green text)
                // If you prefer the menu to close after selection, change this to: viewer.callback();
                this.show(player, crate, viewer.getCallback());
            });
        });
    }
}