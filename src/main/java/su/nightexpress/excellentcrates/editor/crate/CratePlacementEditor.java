package su.nightexpress.excellentcrates.editor.crate;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.config.EditorLang;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.util.CrateUtils;
import su.nightexpress.nightcore.menu.MenuOptions;
import su.nightexpress.nightcore.menu.MenuSize;
import su.nightexpress.nightcore.menu.MenuViewer;
import su.nightexpress.nightcore.menu.impl.EditorMenu;
import su.nightexpress.nightcore.util.ItemReplacer;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.StringUtil;
import su.nightexpress.nightcore.util.wrapper.UniParticle;

public class CratePlacementEditor extends EditorMenu<CratesPlugin, Crate> implements CrateEditor {

    public CratePlacementEditor(@NotNull CratesPlugin plugin) {
        super(plugin, Lang.EDITOR_TITLE_CRATE_PLACEMENT.getString(), MenuSize.CHEST_36);

        this.addReturn(31, (viewer, event, crate) -> {
            this.runNextTick(() -> this.plugin.getEditorManager().openCrate(viewer.getPlayer(), crate));
        });

        this.addItem(Material.ARMOR_STAND, EditorLang.CRATE_HOLOGRAM, 10, (viewer, event, crate) -> {
            if (event.isShiftClick() && event.isLeftClick()) {
                this.handleInput(viewer, Lang.EDITOR_ENTER_VALUE, (dialog, input) -> {
                    crate.setHologramYOffset(input.asAnyDouble(0D));
                    crate.updateHologram();
                    this.saveSettings(viewer, crate, false);
                    return true;
                });
            } else if (event.isLeftClick()) {
                crate.removeHologram();
                crate.setHologramEnabled(!crate.isHologramEnabled());
                crate.createHologram();
                this.saveSettings(viewer, crate, true);
            } else if (event.isRightClick()) {
                this.handleInput(viewer, Lang.EDITOR_ENTER_HOLOGRAM_TEMPLATE, (dialog, input) -> {
                    crate.setHologramTemplate(input.getTextRaw());
                    crate.updateHologram();
                    this.saveSettings(viewer, crate, false);
                    return true;
                }).setSuggestions(Config.CRATE_HOLOGRAM_TEMPLATES.get().keySet(), true);
            }
        });


        this.addItem(Material.CHEST, EditorLang.CRATE_LOCATIONS, 12, (viewer, event, crate) -> {
            if (event.isLeftClick()) {
                CrateUtils.setAssignBlockCrate(viewer.getPlayer(), crate);
                this.handleInput(viewer, Lang.EDITOR_ENTER_BLOCK_LOCATION, (dialog, input) -> false);
            } else {
                crate.clearBlockPositions();
                crate.updateHologram();
                this.saveSettings(viewer, crate, false);
            }
        });

        this.addItem(Material.SLIME_BLOCK, EditorLang.CRATE_PUSHBACK, 14, (viewer, event, crate) -> {
            crate.setPushbackEnabled(!crate.isPushbackEnabled());
            this.saveSettings(viewer, crate, true);
        });

        this.addItem(Material.BLAZE_POWDER, EditorLang.CRATE_EFFECTS, 16, (viewer, event, crate) -> {
            if (event.getClick() == ClickType.DROP) {
                crate.setEffectModel(Lists.next(crate.getEffectModel()));
                this.saveSettings(viewer, crate, true);
            } else {
                if (event.isRightClick()) {
                    if (CrateUtils.isSupportedParticleData(crate.getEffectParticle())) {
                        this.runNextTick(() -> plugin.getEditorManager().openParticle(viewer.getPlayer(), crate));
                    }
                } else if (event.isLeftClick()) {
                    this.handleInput(viewer, Lang.EDITOR_ENTER_PARTICLE_NAME, (dialog, wrapper) -> {
                        Particle particle = StringUtil.getEnum(wrapper.getTextRaw(), Particle.class).orElse(null);
                        if (particle == null) return true;

                        crate.setEffectParticle(UniParticle.of(particle));
                        this.saveSettings(viewer, crate, false);
                        return true;
                    }).setSuggestions(Lists.getEnums(Particle.class), true);
                }
            }
        });

        this.getItems().forEach(menuItem -> menuItem.getOptions().addDisplayModifier((viewer, item) -> {
            ItemReplacer.replace(item, this.getLink(viewer).getAllPlaceholders().replacer());
        }));
    }

    @Override
    protected void onPrepare(@NotNull MenuViewer viewer, @NotNull MenuOptions options) {

    }

    @Override
    protected void onReady(@NotNull MenuViewer viewer, @NotNull Inventory inventory) {

    }
}
