package su.nightexpress.excellentcrates.dialog.crate;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.dialog.CrateDialog;
import su.nightexpress.excellentcrates.dialog.CrateDialogs;
import su.nightexpress.excellentcrates.registry.CratesRegistries;
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;
import su.nightexpress.nightcore.bridge.dialog.wrap.base.WrappedDialogAfterAction;
import su.nightexpress.nightcore.bridge.dialog.wrap.input.single.WrappedSingleOptionEntry;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.ButtonLocale;
import su.nightexpress.nightcore.locale.entry.DialogElementLocale;
import su.nightexpress.nightcore.locale.entry.TextLocale;
import su.nightexpress.nightcore.ui.dialog.Dialogs;
import su.nightexpress.nightcore.ui.dialog.build.*;

import java.util.ArrayList;
import java.util.List;

import static su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers.SOFT_YELLOW;

public class CrateEffectDialog extends CrateDialog<Crate> {

    private static final TextLocale TITLE = LangEntry.builder("Dialog.Crate.Effect.Title").text(title("Crate", "Block Effect"));

    private static final DialogElementLocale BODY = LangEntry.builder("Dialog.Crate.Effect.Body").dialogElement(400,
        "Here you can choose the " + SOFT_YELLOW.wrap("effect model") + " and " + SOFT_YELLOW.wrap("particle type") + " for the crate.",
        "These effects are shown around the blocks linked to the crate."
    );

    private static final TextLocale INPUT_MODEL = LangEntry.builder("Dialog.Crate.Effect.Input.Model").text(SOFT_YELLOW.wrap("Effect Model"));

    private static final ButtonLocale BUTTON_PARTICLE = LangEntry.builder("Dialog.Crate.Effect.Button.Particle").button(SOFT_YELLOW.wrap("Particle: ") + "%s");

    private static final String ACTION_PARTICLE = "particle";
    private static final String JSON_MODEL      = "model";

    @Override
    @NotNull
    public WrappedDialog create(@NotNull Player player, @NotNull Crate crate) {
        List<WrappedSingleOptionEntry> entries = new ArrayList<>();

        CratesRegistries.getEffects().forEach(effect -> {
            entries.add(new WrappedSingleOptionEntry(effect.getId(), effect.getName(), crate.getEffect() == effect));
        });

        return Dialogs.create(builder -> {
            builder.base(DialogBases.builder(TITLE)
                .body(DialogBodies.plainMessage(BODY))
                .inputs(DialogInputs.singleOption(JSON_MODEL, INPUT_MODEL, entries).build())
                .afterAction(WrappedDialogAfterAction.NONE)
                .build()
            );

            builder.type(DialogTypes.multiAction(
                DialogButtons.action(BUTTON_PARTICLE.replace(str -> str.formatted(Lang.PARTICLE.getLocalized(crate.getEffectParticle().getParticle()))))
                    .action(DialogActions.customClick(ACTION_PARTICLE)).build(),
                DialogButtons.ok()
            ).exitAction(DialogButtons.back()).columns(1).build());

            builder.handleResponse(ACTION_PARTICLE, (viewer, identifier, nbtHolder) -> {
                CrateDialogs.CRATE_PARTICLE.ifPresent(dialog -> dialog.show(player, crate, () -> this.show(player, crate, viewer.getCallback())));
            });

            builder.handleResponse(DialogActions.BACK, (viewer, identifier, nbtHolder) -> {
                viewer.closeFully();
            });

            builder.handleResponse(DialogActions.OK, (viewer, identifier, nbtHolder) -> {
                if (nbtHolder == null) return;

                crate.setEffectType(nbtHolder.getText(JSON_MODEL, crate.getEffectType()));
                crate.markDirty();
                viewer.closeFully();
            });
        });
    }
}
