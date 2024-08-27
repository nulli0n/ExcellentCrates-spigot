package su.nightexpress.excellentcrates.editor.crate;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.config.EditorLang;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.nightcore.language.LangAssets;
import su.nightexpress.nightcore.language.entry.LangItem;
import su.nightexpress.nightcore.menu.MenuOptions;
import su.nightexpress.nightcore.menu.MenuSize;
import su.nightexpress.nightcore.menu.MenuViewer;
import su.nightexpress.nightcore.menu.click.ClickResult;
import su.nightexpress.nightcore.menu.impl.EditorMenu;
import su.nightexpress.nightcore.util.ItemReplacer;
import su.nightexpress.nightcore.util.wrapper.UniParticle;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public class CrateParticleEditor extends EditorMenu<CratesPlugin, Crate> implements CrateEditor {

    public CrateParticleEditor(@NotNull CratesPlugin plugin) {
        super(plugin, Lang.EDITOR_TITLE_CRATE_EFFECT.getString(), MenuSize.CHEST_36);

        this.addReturn(31, (viewer, event, crate) -> {
            this.runNextTick(() -> this.plugin.getEditorManager().openPlacement(viewer.getPlayer(), crate));
        });


        /*
            Dust options related.
         */

        this.addColorButton(Material.RED_DYE, EditorLang.CRATE_PARTICLE_DATA_RED, 10, (value, dustOptions) -> {
            return Color.fromRGB(value, dustOptions.getColor().getGreen(), dustOptions.getColor().getBlue());
        }, dustOptions -> dustOptions.getColor().getRed());

        this.addColorButton(Material.LIME_DYE, EditorLang.CRATE_PARTICLE_DATA_GREEN, 12, (value, dustOptions) -> {
            return Color.fromRGB(dustOptions.getColor().getRed(), value, dustOptions.getColor().getBlue());
        }, dustOptions -> dustOptions.getColor().getGreen());

        this.addColorButton(Material.BLUE_DYE, EditorLang.CRATE_PARTICLE_DATA_BLUE, 14, (value, dustOptions) -> {
            return Color.fromRGB(dustOptions.getColor().getRed(), dustOptions.getColor().getGreen(), value);
        }, dustOptions -> dustOptions.getColor().getBlue());

        this.addItem(Material.SLIME_BALL, EditorLang.CRATE_PARTICLE_DATA_SIZE, 16, (viewer, event, crate) -> {
                    UniParticle oldParticle = crate.getEffectParticle();
                    Particle.DustOptions oldOptions = this.getDustOptions(oldParticle);

                    this.handleInput(viewer, Lang.EDITOR_ENTER_VALUE, (dialog, input) -> {
                        float value = (float) input.asDouble();
                        UniParticle particle = new UniParticle(oldParticle.getParticle(), new Particle.DustOptions(oldOptions.getColor(), value));
                        crate.setEffectParticle(particle);
                        this.saveSettings(viewer, crate, false);
                        return true;
                    });

                }).getOptions()
                .addVisibilityPolicy(viewer -> this.isDusty(this.getLink(viewer).getEffectParticle()))
                .addDisplayModifier((viewer, itemStack) -> {
                    UniParticle particle = this.getLink(viewer).getEffectParticle();
                    Particle.DustOptions dustOptions = this.getDustOptions(particle);

                    ItemReplacer.replace(itemStack, str -> str.replace(Placeholders.GENERIC_VALUE, String.valueOf(dustOptions.getSize())));
                });

        /*
            Material related.
         */

        this.addItem(Material.STONE, EditorLang.CRATE_PARTICLE_DATA_MATERIAL, 13, (viewer, event, crate) -> {
                    UniParticle particle = crate.getEffectParticle();

                    ItemStack cursor = event.getCursor();
                    if (cursor == null || cursor.getType().isAir()) return;

                    if (particle.getParticle().getDataType() == BlockData.class) {
                        if (!cursor.getType().isBlock()) return;

                        crate.setEffectParticle(UniParticle.of(particle.getParticle(), cursor.getType().createBlockData()));
                    } else {
                        if (!cursor.getType().isItem()) return;

                        crate.setEffectParticle(UniParticle.of(particle.getParticle(), cursor));
                    }
                    this.saveSettings(viewer, crate, false);
                    event.getView().setCursor(null);
                    this.flush(viewer);
                }).getOptions()
                .addVisibilityPolicy(viewer -> this.isMaterial(this.getLink(viewer).getEffectParticle()))
                .addDisplayModifier((viewer, itemStack) -> {
                    UniParticle particle = this.getLink(viewer).getEffectParticle();
                    Material material = this.getMaterial(particle);

                    itemStack.setType(material);
                    ItemReplacer.replace(itemStack, str -> str.replace(Placeholders.GENERIC_VALUE, LangAssets.get(material)));
                });


        /*
            Numeric related.
         */

        this.addItem(Material.REPEATER, EditorLang.CRATE_PARTICLE_DATA_NUMBER, 13, (viewer, event, crate) -> {
            UniParticle particle = crate.getEffectParticle();

            this.handleInput(viewer, Lang.EDITOR_ENTER_VALUE, (dialog, input) -> {
                float value = (float) input.asDouble();
                UniParticle particle2;

                if (this.isInteger(particle)) {
                    particle2 = new UniParticle(particle.getParticle(), (int) value);
                } else {
                    particle2 = new UniParticle(particle.getParticle(), value);
                }
                crate.setEffectParticle(particle2);
                this.saveSettings(viewer, crate, false);
                return true;
            });

        }).getOptions().addVisibilityPolicy(viewer -> {
            Crate crate = this.getLink(viewer);
            return this.isInteger(crate.getEffectParticle()) || this.isFloat(crate.getEffectParticle());
        }).addDisplayModifier((viewer, itemStack) -> {
            UniParticle particle = this.getLink(viewer).getEffectParticle();
            float value = particle.getData() instanceof Integer i ? i.floatValue() : particle.getData() instanceof Float f ? f : 0;

            ItemReplacer.replace(itemStack, str -> str.replace(Placeholders.GENERIC_VALUE, String.valueOf(value)));
        });
    }

    private void addColorButton(@NotNull Material material,
                                @NotNull LangItem langItem,
                                int slot,
                                @NotNull BiFunction<Integer, Particle.DustOptions, Color> colorCreator,
                                @NotNull Function<Particle.DustOptions, Integer> getColor) {
        this.addItem(material, langItem, slot, (viewer, event, crate) -> {
                    UniParticle oldParticle = crate.getEffectParticle();
                    Particle.DustOptions oldOptions = this.getDustOptions(oldParticle);

                    this.handleInput(viewer, Lang.EDITOR_ENTER_VALUE, (dialog, input) -> {
                        int value = input.asInt();
                        Color color = colorCreator.apply(value, oldOptions);
                        UniParticle particle = new UniParticle(oldParticle.getParticle(), new Particle.DustOptions(color, oldOptions.getSize()));
                        crate.setEffectParticle(particle);
                        this.saveSettings(viewer, crate, false);
                        return true;
                    });

                }).getOptions()
                .addVisibilityPolicy(viewer -> this.isDusty(this.getLink(viewer).getEffectParticle()))
                .addDisplayModifier((viewer, itemStack) -> {
                    UniParticle particle = this.getLink(viewer).getEffectParticle();
                    Particle.DustOptions dustOptions = this.getDustOptions(particle);

                    ItemReplacer.replace(itemStack, str -> str.replace(Placeholders.GENERIC_VALUE, String.valueOf(getColor.apply(dustOptions))));
                });
    }

    @Override
    protected void onPrepare(@NotNull MenuViewer viewer, @NotNull MenuOptions options) {
        options.editTitle(this.getLink(viewer).replacePlaceholders());
    }

    @Override
    protected void onReady(@NotNull MenuViewer viewer, @NotNull Inventory inventory) {

    }

    @Override
    public void onClick(@NotNull MenuViewer viewer, @NotNull ClickResult result, @NotNull InventoryClickEvent event) {
        super.onClick(viewer, result, event);

        Crate crate = this.getLink(viewer);
        if (result.isInventory() && this.isMaterial(crate.getEffectParticle())) {
            event.setCancelled(false);
        }
    }

    private boolean isDusty(@NotNull UniParticle wrapped) {
        return this.checkClass(wrapped, Particle.DustOptions.class::isAssignableFrom);
    }

    private boolean isMaterial(@NotNull UniParticle wrapped) {
        return this.checkClass(wrapped, dataType -> dataType == BlockData.class || dataType == ItemStack.class);
    }

    private boolean isInteger(@NotNull UniParticle wrapped) {
        return this.checkClass(wrapped, dataType -> dataType == Integer.class);
    }

    private boolean isFloat(@NotNull UniParticle wrapped) {
        return this.checkClass(wrapped, dataType -> dataType == Float.class);
    }

    private boolean checkClass(@NotNull UniParticle wrapped, @NotNull Predicate<Class<?>> predicate) {
        Particle particle = wrapped.getParticle();
        if (particle == null) return false;

        return predicate.test(particle.getDataType());
    }

    @NotNull
    private Particle.DustOptions getDustOptions(@NotNull UniParticle particle) {
        Particle.DustOptions dustOptions;
        if (particle.getData() instanceof Particle.DustOptions dust) {
            dustOptions = dust;
        } else dustOptions = new Particle.DustOptions(Color.WHITE, 1F);

        return dustOptions;
    }

    @NotNull
    private Material getMaterial(@NotNull UniParticle particle) {
        Object data = particle.getData();
        if (data instanceof BlockData blockData) return blockData.getMaterial();
        if (data instanceof ItemStack itemStack) return itemStack.getType();

        return Material.STONE;
    }
}
