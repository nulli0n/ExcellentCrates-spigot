package su.nightexpress.excellentcrates.dialog.crate;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Vibration;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.dialog.CrateDialog;
import su.nightexpress.excellentcrates.util.CrateUtils;
import su.nightexpress.nightcore.bridge.common.NightNbtHolder;
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;
import su.nightexpress.nightcore.bridge.dialog.wrap.base.WrappedDialogAfterAction;
import su.nightexpress.nightcore.bridge.dialog.wrap.button.WrappedActionButton;
import su.nightexpress.nightcore.bridge.dialog.wrap.input.WrappedDialogInput;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.DialogElementLocale;
import su.nightexpress.nightcore.locale.entry.TextLocale;
import su.nightexpress.nightcore.ui.dialog.Dialogs;
import su.nightexpress.nightcore.ui.dialog.build.*;
import su.nightexpress.nightcore.util.BukkitThing;
import su.nightexpress.nightcore.util.Version;
import su.nightexpress.nightcore.util.bridge.RegistryType;
import su.nightexpress.nightcore.util.wrapper.UniParticle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import static su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers.*;

public class CrateParticleDialog extends CrateDialog<Crate> {

    private static final TextLocale TITLE_MAIN = LangEntry.builder("Dialog.Crate.Particle.Title").text(title("Crate", "Particle Type"));
    private static final TextLocale TITLE_DATA = LangEntry.builder("Dialog.Crate.ParticleData.Title").text(title("Crate", "Particle Data"));

    private static final DialogElementLocale BODY_MAIN = LangEntry.builder("Dialog.Crate.Particle.Body").dialogElement(400,
        "Select the desired " + SOFT_YELLOW.wrap("particle type") + ".",
        "",
        "Particles marked with an asterisk " + SOFT_RED.wrap("(*)") + " have additional options that must be filled in."
    );

    private static final DialogElementLocale BODY_DATA_COLOR = LangEntry.builder("Dialog.Crate.ParticleData.Body.Color").dialogElement(400,
        "This particle type requires a " + SOFT_YELLOW.wrap("color") + " specified in RGB format."
    );

    private static final DialogElementLocale BODY_DATA_ITEM = LangEntry.builder("Dialog.Crate.ParticleData.Body.Item").dialogElement(400,
        "This particle type requires an " + SOFT_YELLOW.wrap("item name") + " specified."
    );

    private static final DialogElementLocale BODY_DATA_BLOCK = LangEntry.builder("Dialog.Crate.ParticleData.Body.Block").dialogElement(400,
        "This particle type requires a " + SOFT_YELLOW.wrap("block name") + " specified."
    );

    private static final DialogElementLocale BODY_DATA_GENERIC = LangEntry.builder("Dialog.Crate.ParticleData.Body.Generic").dialogElement(400,
        "This particle type requires " + SOFT_YELLOW.wrap("additional options") + " specified."
    );

    private static final TextLocale INPUT_DATA_RED   = LangEntry.builder("Dialog.Crate.ParticleData.Input.Red").text(SOFT_RED.wrap("Red"));
    private static final TextLocale INPUT_DATA_GREEN = LangEntry.builder("Dialog.Crate.ParticleData.Input.Green").text(SOFT_GREEN.wrap("Green"));
    private static final TextLocale INPUT_DATA_BLUE  = LangEntry.builder("Dialog.Crate.ParticleData.Input.Blue").text(SOFT_BLUE.wrap("Blue"));
    private static final TextLocale INPUT_DATA_ALPHA = LangEntry.builder("Dialog.Crate.ParticleData.Input.Alpha").text("Alpha");
    private static final TextLocale INPUT_DATA_SIZE  = LangEntry.builder("Dialog.Crate.ParticleData.Input.Size").text("Size");
    private static final TextLocale INPUT_DATA_POWER = LangEntry.builder("Dialog.Crate.ParticleData.Input.Power").text("Power");
    private static final TextLocale INPUT_DATA_DELAY = LangEntry.builder("Dialog.Crate.ParticleData.Input.Delay").text("Delay");
    private static final TextLocale INPUT_DATA_BLOCK = LangEntry.builder("Dialog.Crate.ParticleData.Input.BlockName").text("Block Name");
    private static final TextLocale INPUT_DATA_ITEM  = LangEntry.builder("Dialog.Crate.ParticleData.Input.ItemName").text("Item Name");

    private static final String LABEL_NORMAL = "%1$s %2$s";
    private static final String LABEL_ASTERISK = LABEL_NORMAL + SOFT_RED.wrap(" (*)");

    private static final String PARTICLES_ATLAS = "particles";

    private static final float DEF_SIZE = 1F;
    private static final float DEF_POWER = 1F;
    private static final float DEF_DELAY = 0F;

    private static final float MIN_SIZE = 0.1F;
    private static final float MAX_SIZE = 10F;
    private static final float SIZE_STEP = 0.1F;

    private static final float MIN_POWER = 0.1F;
    private static final float MAX_POWER = 10F;
    private static final float POWER_STEP = 0.1F;

    private static final float MIN_DELAY = 0F;
    private static final float MAX_DELAY = 1F;
    private static final float DELAY_STEP = 0.05F;

    private static final int    COLOR_MAX       = 255;
    private static final int    COLOR_MIN       = 0;

    private static final String JSON_ID    = "id";
    private static final String JSON_RED   = "red";
    private static final String JSON_GREEN = "green";
    private static final String JSON_BLUE  = "blue";
    private static final String JSON_ALPHA = "alpha";
    private static final String JSON_EXTRA = "extra";

    @Override
    @NotNull
    public WrappedDialog create(@NotNull Player player, @NotNull Crate crate) {
        List<WrappedActionButton> buttons = new ArrayList<>();

        BukkitThing.getAll(RegistryType.PARTICLE_TYPE).stream().filter(CrateUtils::isSupportedParticle).sorted(Comparator.comparing(BukkitThing::getValue)).forEach(particle -> {
            boolean hasData = isConfigurable(particle);
            String localized = Lang.PARTICLE.getLocalized(particle);
            String spriteName = getSprite(particle);
            String sprite = spriteName == null ? null : SPRITE.apply(PARTICLES_ATLAS, spriteName);
            String label = (hasData ? LABEL_ASTERISK : LABEL_NORMAL).formatted(sprite == null ? "" : sprite, localized);

            NightNbtHolder nbt = NightNbtHolder.builder().put(JSON_ID, BukkitThing.getAsString(particle)).build();
            buttons.add(DialogButtons.action(label).action(DialogActions.customClick(DialogActions.OK, nbt)).build());
        });

        return Dialogs.create(builder -> {
            builder.base(DialogBases.builder(TITLE_MAIN)
                .body(DialogBodies.plainMessage(BODY_MAIN))
                .afterAction(WrappedDialogAfterAction.NONE) // None for smoother Particle Data dialog switch.
                .build()
            );

            builder.type(DialogTypes.multiAction(buttons).exitAction(DialogButtons.back()).columns(3).build());

            builder.handleResponse(DialogActions.BACK, (viewer, identifier, nbtHolder) -> {
                viewer.callback();
            });

            builder.handleResponse(DialogActions.OK, (viewer, identifier, nbtHolder) -> {
                if (nbtHolder == null) return;

                String name = nbtHolder.getText(JSON_ID).orElse(null);
                if (name == null) return;

                Particle particle = BukkitThing.getParticle(name);
                if (particle == null) return;

                if (isConfigurable(particle)) {
                    this.openParticleData(player, crate, particle, viewer.getCallback());
                    return;
                }

                crate.setEffectParticle(UniParticle.of(particle));
                crate.markDirty();
                viewer.callback(); // Close due to NONE action.
            });
        });
    }

    @Nullable
    private static String getSprite(@NotNull Particle particle) {
        String value = BukkitThing.getValue(particle);

        if (Version.withCopperAge()) {
            if (value.equalsIgnoreCase("copper_fire_flame")) return value;
        }

        return switch (particle) {
            case ANGRY_VILLAGER -> "angry";
            case LARGE_SMOKE -> "big_smoke_5";
            case BUBBLE_POP -> "bubble_pop_1";
            case CHERRY_LEAVES -> "cherry_0";
            case CRIT -> "critical_hit";
            case DAMAGE_INDICATOR -> "damage";
            case EFFECT -> "effect_5";
            case EXPLOSION -> "explosion_10";
            case HAPPY_VILLAGER -> "glint";
            case GUST -> "gust_8";
            case HEART, INFESTED, ENCHANTED_HIT, FIREFLY, FLASH, GLOW, BUBBLE, LAVA,
                 NAUTILUS, NOTE, RAID_OMEN, SHRIEK, SOUL_FIRE_FLAME, TRIAL_OMEN, VAULT_CONNECTION -> value;
            case TINTED_LEAVES -> "leaf_0";
            case PALE_OAK_LEAVES -> "pale_oak_0";
            case SCULK_CHARGE -> "sculk_charge_3";
            case SCULK_CHARGE_POP -> "sculk_charge_pop_0";
            case SCULK_SOUL -> "sculk_soul_5";
            case SMALL_GUST -> "small_gust_4";
            case SONIC_BOOM -> "sonic_boom_10";
            case SOUL -> "soul_1";
            case ELECTRIC_SPARK -> "spark_5";
            case SPLASH -> "splash_0";
            case SWEEP_ATTACK -> "sweep_3";
            case TRIAL_SPAWNER_DETECTION -> "trial_spawner_detection_0";
            case TRIAL_SPAWNER_DETECTION_OMINOUS -> "trial_spawner_detection_ominous_0";
            default -> null;
        };
    }

    private void openParticleData(@NotNull Player player, @NotNull Crate crate, @NotNull Particle particle, @Nullable Runnable originCallback) {
        Class<?> type = particle.getDataType();
        Function<NightNbtHolder, Object> dataParser;

        List<WrappedDialogInput> inputs = new ArrayList<>();
        DialogElementLocale bodyLocale;

        if (type == Color.class) {
            bodyLocale = BODY_DATA_COLOR;
            inputs.addAll(this.getColorButtons());
            dataParser = CrateParticleDialog::parseColor;
        }
        else if (type == Particle.DustOptions.class) {
            bodyLocale = BODY_DATA_COLOR;
            inputs.add(DialogInputs.numberRange(JSON_EXTRA, INPUT_DATA_SIZE, MIN_SIZE, MAX_SIZE).initial(DEF_SIZE).step(SIZE_STEP).build());
            inputs.addAll(this.getColorButtons());
            dataParser = nbtHolder -> {
                Color color = parseColor(nbtHolder);
                float size = nbtHolder.getFloat(JSON_EXTRA, DEF_SIZE);
                return new Particle.DustOptions(color, size);
            };
        }
        else if (type.getSimpleName().equalsIgnoreCase("Spell")) {
            bodyLocale = BODY_DATA_COLOR;
            inputs.add(DialogInputs.numberRange(JSON_EXTRA, INPUT_DATA_POWER, MIN_POWER, MAX_POWER).initial(DEF_POWER).step(POWER_STEP).build());
            inputs.addAll(this.getColorButtons());
            dataParser = nbtHolder -> {
                Color color = parseColor(nbtHolder);
                float power = nbtHolder.getFloat(JSON_EXTRA, DEF_POWER);
                return new Particle.Spell(color, power);
            };
        }
        else if (type == ItemStack.class) {
            bodyLocale = BODY_DATA_ITEM;
            inputs.add(DialogInputs.text(JSON_EXTRA, INPUT_DATA_ITEM).build());
            dataParser = nbtHolder -> {
                String name = nbtHolder.getText(JSON_EXTRA).orElse(null);
                Material material = name == null ? null : BukkitThing.getMaterial(name);
                return material == null ? null : new ItemStack(material);
            };
        }
        else if (type == BlockData.class) {
            bodyLocale = BODY_DATA_BLOCK;
            inputs.add(DialogInputs.text(JSON_EXTRA, INPUT_DATA_BLOCK).build());
            dataParser = nbtHolder -> {
                String name = nbtHolder.getText(JSON_EXTRA).orElse(null);
                Material material = name == null ? null : BukkitThing.getMaterial(name);
                return material == null ? null : material.createBlockData();
            };
        }
        else {
            bodyLocale = BODY_DATA_GENERIC;
            if (particle == Particle.DRAGON_BREATH) {
                inputs.add(DialogInputs.numberRange(JSON_EXTRA, INPUT_DATA_DELAY, MIN_DELAY, MAX_DELAY).initial(DEF_DELAY).step(DELAY_STEP).build());
                dataParser = nbtHolder -> nbtHolder.getFloat(JSON_EXTRA).orElse(DEF_DELAY);
            }
            else dataParser = nbtHolder -> null;
        }

        Dialogs.createAndShow(player, builder -> {
            builder.base(DialogBases.builder(TITLE_DATA)
                .body(DialogBodies.plainMessage(bodyLocale))
                .inputs(inputs)
                .afterAction(WrappedDialogAfterAction.NONE)
                .build()
            );

            builder.type(DialogTypes.multiAction(DialogButtons.ok()).exitAction(DialogButtons.back()).build());

            builder.handleResponse(DialogActions.BACK, (viewer, identifier, nbtHolder) -> {
                viewer.callback();
            });

            builder.handleResponse(DialogActions.OK, (viewer, identifier, nbtHolder) -> {
                if (nbtHolder == null) return;

                Object data = dataParser.apply(nbtHolder);
                if (data != null) {
                    crate.setEffectParticle(UniParticle.of(particle, data));
                    crate.markDirty();
                }
                viewer.callback();
            });
        }, originCallback);
    }

    @NotNull
    private static Color parseColor(@NotNull NightNbtHolder nbtHolder) {
        int red = nbtHolder.getInt(JSON_RED, COLOR_MAX);
        int green = nbtHolder.getInt(JSON_GREEN, COLOR_MAX);
        int blue = nbtHolder.getInt(JSON_BLUE, COLOR_MAX);
        int alpha = nbtHolder.getInt(JSON_ALPHA, COLOR_MAX);

        return Color.fromARGB(alpha, red, green, blue);
    }

    @NotNull
    private List<WrappedDialogInput> getColorButtons() {
        List<WrappedDialogInput> inputs = new ArrayList<>();

        inputs.add(DialogInputs.numberRange(JSON_RED, INPUT_DATA_RED, COLOR_MIN, COLOR_MAX).initial((float) COLOR_MAX).step(1F).build());
        inputs.add(DialogInputs.numberRange(JSON_GREEN, INPUT_DATA_GREEN, COLOR_MIN, COLOR_MAX).initial((float) COLOR_MAX).step(1F).build());
        inputs.add(DialogInputs.numberRange(JSON_BLUE, INPUT_DATA_BLUE, COLOR_MIN, COLOR_MAX).initial((float) COLOR_MAX).step(1F).build());
        inputs.add(DialogInputs.numberRange(JSON_ALPHA, INPUT_DATA_ALPHA, COLOR_MIN, COLOR_MAX).initial((float) COLOR_MAX).step(1F).build());

        return inputs;
    }

    private static boolean isConfigurable(@NotNull Particle particle) {
        if (particle == Particle.DRAGON_BREATH) return true; // Float data

        Class<?> type = particle.getDataType();
        return type != Void.class && type != Integer.class && type != Float.class && type != Vibration.class && type != Particle.Trail.class && type != Particle.DustTransition.class;
    }
}
