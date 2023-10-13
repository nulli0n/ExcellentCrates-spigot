package su.nightexpress.excellentcrates.crate.editor;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.editor.InputHandler;
import su.nexmedia.engine.api.lang.LangKey;
import su.nexmedia.engine.api.manager.EventListener;
import su.nexmedia.engine.api.menu.impl.EditorMenu;
import su.nexmedia.engine.api.menu.impl.MenuViewer;
import su.nexmedia.engine.editor.EditorManager;
import su.nexmedia.engine.utils.CollectionsUtil;
import su.nexmedia.engine.utils.ItemUtil;
import su.nexmedia.engine.utils.StringUtil;
import su.nexmedia.engine.utils.values.UniParticle;
import su.nightexpress.excellentcrates.ExcellentCratesPlugin;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.editor.EditorLocales;

public class CratePlacementEditor extends EditorMenu<ExcellentCratesPlugin, Crate> implements EventListener {

    private boolean isReadyForBlock = false;

    public CratePlacementEditor(@NotNull ExcellentCratesPlugin plugin, @NotNull Crate crate) {
        super(crate.plugin(), crate, Config.EDITOR_TITLE_CRATE.get(), 36);

        this.addReturn(31).setClick((viewer, event) -> {
            crate.getEditor().openNextTick(viewer, 1);
        });

        this.addItem(Material.ARMOR_STAND, EditorLocales.CRATE_HOLOGRAM, 10).setClick((viewer, event) -> {
            if (event.isLeftClick()) {
                crate.setHologramEnabled(!crate.isHologramEnabled());
                crate.updateHologram();
                this.save(viewer);
            }
            else if (event.isRightClick()) {
                this.handleInput(viewer, Lang.EDITOR_CRATE_ENTER_HOLOGRAM_TEMPLATE, wrapper -> {
                    crate.setHologramTemplate(wrapper.getTextRaw());
                    crate.updateHologram();
                    crate.save();
                    return true;
                });
                EditorManager.suggestValues(viewer.getPlayer(), Config.CRATE_HOLOGRAM_TEMPLATES.get().keySet(), true);
            }
        });

        this.addItem(Material.CHEST, EditorLocales.CRATE_LOCATIONS, 12).setClick((viewer, event) -> {
            if (event.isLeftClick()) {
                this.isReadyForBlock = true;
                this.handleInput(viewer, Lang.EDITOR_CRATE_ENTER_BLOCK_LOCATION, wrapper -> false);
            }
            else {
                crate.getBlockLocations().clear();
                crate.updateHologram();
                this.save(viewer);
            }
        });

        this.addItem(Material.SLIME_BLOCK, EditorLocales.CRATE_PUSHBACK, 14).setClick((viewer, event) -> {
            crate.setPushbackEnabled(!crate.isPushbackEnabled());
            this.save(viewer);
        });

        this.addItem(Material.BLAZE_POWDER, EditorLocales.CRATE_EFFECTS, 16).setClick((viewer, event) -> {
            if (event.getClick() == ClickType.DROP) {
                crate.setEffectModel(CollectionsUtil.next(crate.getEffectModel()));
                this.save(viewer);
            }
            else {
                if (event.isRightClick()) {
                    LangKey key;
                    Class<?> clazz = crate.getEffectParticle().getParticle().getDataType();
                    if (clazz == BlockData.class || clazz == ItemStack.class) {
                        key = Lang.EDITOR_CRATE_ENTER_PARTICLE_MATERIAL;
                    }
                    else if (clazz == Particle.DustOptions.class) {
                        key = Lang.EDITOR_CRATE_ENTER_PARTICLE_REDSTONE;
                    }
                    else return;

                    this.handleInput(viewer, key, wrapper -> {
                        crate.setEffectParticle(crate.getEffectParticle().parseData(wrapper.getTextRaw()));
                        crate.save();
                        return true;
                    });
                }
                else if (event.isLeftClick()) {
                    this.handleInput(viewer, Lang.EDITOR_CRATE_ENTER_PARTICLE_NAME, wrapper -> {
                        Particle particle = StringUtil.getEnum(wrapper.getTextRaw(), Particle.class).orElse(Particle.REDSTONE);
                        crate.setEffectParticle(UniParticle.of(particle).parseData(""));
                        crate.save();
                        return true;
                    });
                    EditorManager.suggestValues(viewer.getPlayer(), CollectionsUtil.getEnumsList(Particle.class), true);
                }
            }
        });

        this.getItems().forEach(menuItem -> menuItem.getOptions().addDisplayModifier(((viewer, item) -> {
                ItemUtil.replace(item, Placeholders.forCrateAll(crate).replacer());
            }))
        );

        this.registerListeners();
    }

    @Override
    public void registerListeners() {
        this.plugin.getPluginManager().registerEvents(this, this.plugin);
    }

    @Override
    public void clear() {
        super.clear();
        this.unregisterListeners();
    }

    private void save(@NotNull MenuViewer viewer) {
        this.object.save();
        this.openNextTick(viewer, viewer.getPage());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onCrateBlockClick(PlayerInteractEvent event) {
        if (!this.isReadyForBlock) return;

        Player player = event.getPlayer();

        InputHandler editor = EditorManager.getInputHandler(player);
        if (editor == null) return;

        Block block = event.getClickedBlock();
        if (block == null) return;

        event.setUseInteractedBlock(Event.Result.DENY);
        event.setUseItemInHand(Event.Result.DENY);

        if (plugin.getCrateManager().getCrateByBlock(block) != null) return;

        Crate crate = this.object;
        crate.getBlockLocations().add(block.getLocation());
        crate.updateHologram();
        crate.save();
        EditorManager.endEdit(player);
        this.isReadyForBlock = false;
    }
}
