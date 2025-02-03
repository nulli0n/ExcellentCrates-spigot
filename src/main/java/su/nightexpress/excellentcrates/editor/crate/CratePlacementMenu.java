package su.nightexpress.excellentcrates.editor.crate;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Registry;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.MenuType;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.config.EditorLang;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.crate.effect.CrateEffect;
import su.nightexpress.excellentcrates.crate.effect.EffectId;
import su.nightexpress.excellentcrates.crate.effect.EffectRegistry;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.util.CrateUtils;
import su.nightexpress.nightcore.ui.dialog.Dialog;
import su.nightexpress.nightcore.ui.menu.MenuViewer;
import su.nightexpress.nightcore.ui.menu.item.ItemOptions;
import su.nightexpress.nightcore.ui.menu.item.MenuItem;
import su.nightexpress.nightcore.ui.menu.type.LinkedMenu;
import su.nightexpress.nightcore.util.BukkitThing;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.wrapper.UniParticle;

@SuppressWarnings("UnstableApiUsage")
public class CratePlacementMenu extends LinkedMenu<CratesPlugin, Crate> {

    public CratePlacementMenu(@NotNull CratesPlugin plugin) {
        super(plugin, MenuType.GENERIC_9X6, Lang.EDITOR_TITLE_CRATE_PLACEMENT.getString());

        this.addItem(MenuItem.buildReturn(this, 49, (viewer, event) -> {
            this.runNextTick(() -> this.plugin.getEditorManager().openOptionsMenu(viewer.getPlayer(), this.getLink(viewer)));
        }));

        if (plugin.hasHolograms()) {
            this.addItem(Material.LIME_DYE, EditorLang.CRATE_EDIT_HOLOGRAM_TOGGLE, 10, (viewer, event, crate) -> {
                crate.removeHologram();
                crate.setHologramEnabled(!crate.isHologramEnabled());
                crate.createHologram();
                this.saveAndFlush(viewer, crate);
            }, ItemOptions.builder().setDisplayModifier((viewer, item) -> {
                if (!this.getLink(viewer).isHologramEnabled()) item.setMaterial(Material.GRAY_DYE);
            }).build());

            this.addItem(Material.ARMOR_STAND, EditorLang.CRATE_EDIT_HOLOGRAM_TEMPLATE, 11, (viewer, event, crate) -> {
                this.handleInput(Dialog.builder(viewer, Lang.EDITOR_ENTER_HOLOGRAM_TEMPLATE, input -> {
                    crate.setHologramTemplateId(input.getTextRaw());
                    crate.recreateHologram();
                    crate.saveSettings();
                    return true;
                }).setSuggestions(Config.getHologramTemplateIds(), true));
            });

            this.addItem(Material.LADDER, EditorLang.CRATE_EDIT_HOLOGRAM_OFFSET, 12, (viewer, event, crate) -> {
                this.handleInput(Dialog.builder(viewer, Lang.EDITOR_ENTER_VALUE, input -> {
                    crate.setHologramYOffset(input.asDouble(0D));
                    crate.recreateHologram();
                    crate.saveSettings();
                    return true;
                }));
            });
        }



        this.addItem(Material.FIREWORK_ROCKET, EditorLang.CRATE_EDIT_EFFECT_MODEL, 16, (viewer, event, crate) -> {
            if (event.isRightClick()) {
                crate.setEffectType(EffectId.NONE);
                this.saveAndFlush(viewer, crate);
                return;
            }

            this.handleInput(Dialog.builder(viewer, Lang.EDITOR_ENTER_MODEL_NAME, input -> {
                String effectId = input.getTextRaw();

                CrateEffect effect = EffectRegistry.getEffectById(effectId);
                if (effect == null) return true;

                crate.setEffectType(effectId);
                crate.saveSettings();
                return true;
            }).setSuggestions(EffectRegistry.getEffectNames(), true));
        });

        this.addItem(Material.REDSTONE, EditorLang.CRATE_EDIT_EFFECT_PARTICLE, 15, (viewer, event, crate) -> {
            this.handleInput(Dialog.builder(viewer, Lang.EDITOR_ENTER_PARTICLE_NAME, input -> {
                Particle particle = BukkitThing.getParticle(input.getTextRaw());
                if (particle == null) return true;

                crate.setEffectParticle(UniParticle.of(particle));
                crate.saveSettings();
                return true;
            }).setSuggestions(BukkitThing.getNames(Registry.PARTICLE_TYPE), true));
        }, ItemOptions.builder()
            .setVisibilityPolicy(viewer -> this.getLink(viewer).isEffectEnabled())
            .build());

        this.addItem(Material.GLOWSTONE_DUST, EditorLang.CRATE_EDIT_EFFECT_PARTICLE_DATA, 14, (viewer, event, crate) -> {
            this.runNextTick(() -> plugin.getEditorManager().openParticleMenu(viewer.getPlayer(), crate));
        }, ItemOptions.builder()
            .setVisibilityPolicy(viewer -> this.getLink(viewer).isEffectEnabled() && CrateUtils.isSupportedParticleData(this.getLink(viewer).getEffectParticle()))
            .build()
        );



        this.addItem(Material.CHEST, EditorLang.CRATE_EDIT_BLOCKS, 29, (viewer, event, crate) -> {
            if (event.isLeftClick()) {
                CrateUtils.setAssignBlockCrate(viewer.getPlayer(), crate);
                this.handleInput(Dialog.builder(viewer, Lang.EDITOR_ENTER_BLOCK_LOCATION, input -> false));
            }
            else if (event.isRightClick()) {
                crate.removeHologram();
                crate.clearBlockPositions();
                this.saveAndFlush(viewer, crate);
            }
        });

        this.addItem(Material.SLIME_BLOCK, EditorLang.CRATE_EDIT_PUSHBACK, 33, (viewer, event, crate) -> {
            crate.setPushbackEnabled(!crate.isPushbackEnabled());
            this.saveAndFlush(viewer, crate);
        });
    }

    private void saveAndFlush(@NotNull MenuViewer viewer, @NotNull Crate crate) {
        crate.saveSettings();
        this.runNextTick(() -> this.flush(viewer));
    }

    @Override
    protected void onItemPrepare(@NotNull MenuViewer viewer, @NotNull MenuItem menuItem, @NotNull NightItem item) {
        super.onItemPrepare(viewer, menuItem, item);

        item.replacement(replacer -> replacer.replace(this.getLink(viewer).replaceAllPlaceholders()));
    }

    @Override
    protected void onPrepare(@NotNull MenuViewer viewer, @NotNull InventoryView view) {

    }

    @Override
    protected void onReady(@NotNull MenuViewer viewer, @NotNull Inventory inventory) {

    }
}
