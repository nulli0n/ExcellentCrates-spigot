package su.nightexpress.excellentcrates.crate.editor;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nexmedia.engine.api.editor.EditorHandler;
import su.nexmedia.engine.api.manager.IListener;
import su.nexmedia.engine.api.menu.impl.EditorMenu;
import su.nexmedia.engine.api.menu.impl.MenuViewer;
import su.nexmedia.engine.api.particle.SimpleParticle;
import su.nexmedia.engine.editor.EditorManager;
import su.nexmedia.engine.utils.*;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.api.OpenCostType;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.editor.EditorLocales;

import java.util.ArrayList;
import java.util.List;

public class CrateMainEditor extends EditorMenu<ExcellentCrates, Crate> implements IListener {

    private CrateRewardListEditor editorRewards;
    private boolean               isReadyForBlock = false;

    public CrateMainEditor(@NotNull Crate crate) {
        super(crate.plugin(), crate, Config.EDITOR_TITLE_CRATE.get(), 45);

        this.addReturn(40).setClick((viewer, event) -> {
            this.plugin.runTask(task -> this.plugin.getEditor().getCratesEditor().open(viewer.getPlayer(), 1));
        });

        this.addItem(Material.NAME_TAG, EditorLocales.CRATE_NAME, 2).setClick((viewer, event) -> {
            this.startEdit(viewer.getPlayer(), plugin.getMessage(Lang.EDITOR_CRATE_ENTER_DISPLAY_NAME), chat -> {
                crate.setName(chat.getMessage());
                crate.save();
                return true;
            });
        });

        this.addItem(Material.ITEM_FRAME, EditorLocales.CRATE_ITEM, 4).setClick((viewer, event) -> {
            if (event.isRightClick()) {
                PlayerUtil.addItem(viewer.getPlayer(), crate.getRawItem());
                return;
            }

            ItemStack cursor = event.getCursor();
            if (cursor == null || cursor.getType().isAir()) return;

            crate.setItem(cursor);
            event.getView().setCursor(null);
            this.save(viewer);
        }).getOptions().setDisplayModifier(((viewer, item) -> {
            item.setType(crate.getItem().getType());
            item.setItemMeta(crate.getItem().getItemMeta());
            ItemUtil.mapMeta(item, meta -> {
                meta.setDisplayName(EditorLocales.CRATE_ITEM.getLocalizedName());
                meta.setLore(EditorLocales.CRATE_ITEM.getLocalizedLore());
            });
        }));

        this.addItem(Material.REDSTONE_TORCH, EditorLocales.CRATE_PERMISSION, 6).setClick((viewer, event) -> {
            crate.setPermissionRequired(!crate.isPermissionRequired());
            this.save(viewer);
        });

        this.addItem(Material.TRIPWIRE_HOOK, EditorLocales.CRATE_KEYS, 10).setClick((viewer, event) -> {
            if (event.isLeftClick()) {
                this.startEdit(viewer.getPlayer(), plugin.getMessage(Lang.EDITOR_CRATE_ENTER_KEY_ID), chat -> {
                    crate.getKeyIds().add(Colorizer.strip(chat.getMessage()));
                    crate.save();
                    return true;
                });
                EditorManager.suggestValues(viewer.getPlayer(), plugin.getKeyManager().getKeyIds(), true);
            }
            else if (event.isRightClick()) {
                crate.getKeyIds().clear();
                this.save(viewer);
            }
        });

        this.addItem(Material.PAINTING, EditorLocales.CRATE_CONFIG, 11).setClick((viewer, event) -> {
            if (event.isShiftClick()) {
                if (event.isRightClick()) {
                    crate.setPreviewConfig(null);
                    this.save(viewer);
                    return;
                }
                List<String> previews = FileUtil.getFiles(plugin.getDataFolder() + Config.DIR_PREVIEWS, true)
                    .stream().map(f -> f.getName().replace(".yml", "")).toList();

                this.startEdit(viewer.getPlayer(), plugin.getMessage(Lang.EDITOR_CRATE_ENTER_PREVIEW_CONFIG), chat -> {
                    crate.setPreviewConfig(Colorizer.strip(chat.getMessage()));
                    crate.createPreview();
                    crate.save();
                    return true;
                });
                EditorManager.suggestValues(viewer.getPlayer(), previews, true);
            }
            else {
                if (event.isRightClick()) {
                    crate.setOpeningConfig(null);
                    this.save(viewer);
                    return;
                }
                this.startEdit(viewer.getPlayer(), plugin.getMessage(Lang.EDITOR_CRATE_ENTER_ANIMATION_CONFIG), chat -> {
                    crate.setOpeningConfig(Colorizer.strip(chat.getMessage()));
                    crate.save();
                    return true;
                });
                EditorManager.suggestValues(viewer.getPlayer(), plugin.getCrateManager().getOpeningsMap().keySet(), true);
            }
        });

        this.addItem(Material.EMERALD, EditorLocales.CRATE_REWARDS, 13).setClick((viewer, event) -> {
            this.plugin.runTask(task -> this.getEditorRewards().open(viewer.getPlayer(), 1));
        });

        this.addItem(Material.CLOCK, EditorLocales.CRATE_OPEN_COOLDOWN, 15).setClick((viewer, event) -> {
            if (event.getClick() == ClickType.DROP) {
                crate.setOpenCooldown(-1);
                this.save(viewer);
                return;
            }
            if (event.isRightClick()) {
                crate.setOpenCooldown(0);
                this.save(viewer);
                return;
            }
            this.startEdit(viewer.getPlayer(), plugin.getMessage(Lang.EDITOR_CRATE_ENTER_COOLDOWN), chat -> {
                int cooldown = StringUtil.getInteger(Colorizer.strip(chat.getMessage()), 0);
                crate.setOpenCooldown(cooldown);
                crate.save();
                return true;
            });
        });

        this.addItem(Material.GOLD_NUGGET, EditorLocales.CRATE_OPEN_COST, 16).setClick((viewer, event) -> {
            if (event.getClick() == ClickType.DROP) {
                crate.setOpenCost(OpenCostType.MONEY, 0D);
                crate.setOpenCost(OpenCostType.EXP, 0);
                this.save(viewer);
                return;
            }
            if (event.isLeftClick()) {
                this.startEdit(viewer.getPlayer(), plugin.getMessage(Lang.EDITOR_CRATE_ENTER_OPEN_COST_MONEY), chat -> {
                    double costMoney = StringUtil.getDouble(Colorizer.strip(chat.getMessage()), 0);
                    crate.setOpenCost(OpenCostType.MONEY, costMoney);
                    crate.save();
                    return true;
                });
            }
            else if (event.isRightClick()) {
                this.startEdit(viewer.getPlayer(), plugin.getMessage(Lang.EDITOR_CRATE_ENTER_OPEN_COST_EXP), chat -> {
                    double costExp = StringUtil.getDouble(Colorizer.strip(chat.getMessage()), 0D);
                    crate.setOpenCost(OpenCostType.EXP, (int) costExp);
                    crate.save();
                    return true;
                });
            }
        });

        this.addItem(Material.CHEST, EditorLocales.CRATE_BLOCK_LOCATIONS, 22).setClick((viewer, event) -> {
            if (event.isLeftClick()) {
                this.isReadyForBlock = true;
                this.startEdit(viewer.getPlayer(), plugin.getMessage(Lang.EDITOR_CRATE_ENTER_BLOCK_LOCATION), chat -> false);
            }
            else {
                crate.getBlockLocations().clear();
                crate.updateHologram();
                this.save(viewer);
            }
        });

        this.addItem(Material.SLIME_BLOCK, EditorLocales.CRATE_BLOCK_PUSHBACK, 21).setClick((viewer, event) -> {
            crate.setBlockPushbackEnabled(!crate.isBlockPushbackEnabled());
            this.save(viewer);
        });

        this.addItem(Material.ARMOR_STAND, EditorLocales.CRATE_BLOCK_HOLOGRAM, 31).setClick((viewer, event) -> {
            if (event.isShiftClick()) {
                if (event.isLeftClick()) {
                    crate.setBlockHologramEnabled(!crate.isBlockHologramEnabled());
                    crate.updateHologram();
                    this.save(viewer);
                }
                else if (event.isRightClick()) {
                    this.startEdit(viewer.getPlayer(), plugin.getMessage(Lang.EDITOR_CRATE_ENTER_BLOCK_HOLOGRAM_OFFSET), chat -> {
                        double offset = StringUtil.getDouble(Colorizer.strip(chat.getMessage()), 0D);
                        crate.setBlockHologramOffsetY(offset);
                        crate.updateHologram();
                        crate.save();
                        return true;
                    });
                }
            }
            else {
                if (event.isLeftClick()) {
                    this.startEdit(viewer.getPlayer(), plugin.getMessage(Lang.EDITOR_CRATE_ENTER_BLOCK_HOLOGRAM_TEXT), chat -> {
                        List<String> list = crate.getBlockHologramText();
                        list.add(chat.getMessage());
                        crate.setBlockHologramText(list);
                        crate.updateHologram();
                        crate.save();
                        return true;
                    });
                }
                else if (event.isRightClick()) {
                    crate.setBlockHologramText(new ArrayList<>());
                    crate.updateHologram();
                    this.save(viewer);
                }
            }
        });

        this.addItem(Material.BLAZE_POWDER, EditorLocales.CRATE_BLOCK_EFFECT, 23).setClick((viewer, event) -> {
            if (event.getClick() == ClickType.DROP) {
                crate.setBlockEffectModel(CollectionsUtil.next(crate.getBlockEffectModel()));
                this.save(viewer);
            }
            else {
                if (event.isRightClick()) {
                    this.startEdit(viewer.getPlayer(), plugin.getMessage(Lang.EDITOR_CRATE_ENTER_PARTICLE_DATA), chat -> {
                        String data = chat.getMessage();
                        crate.setBlockEffectParticle(crate.getBlockEffectParticle().parseData(data));
                        crate.save();
                        return true;
                    });
                }
                else if (event.isLeftClick()) {
                    this.startEdit(viewer.getPlayer(), plugin.getMessage(Lang.EDITOR_CRATE_ENTER_PARTICLE_NAME), chat -> {
                        Particle particle = StringUtil.getEnum(chat.getMessage(), Particle.class).orElse(Particle.REDSTONE);
                        crate.setBlockEffectParticle(SimpleParticle.of(particle).parseData(""));
                        crate.save();
                        return true;
                    });
                    EditorManager.suggestValues(viewer.getPlayer(), CollectionsUtil.getEnumsList(Particle.class), true);
                }
            }
        });

        this.getItems().forEach(menuItem -> {
            if (menuItem.getOptions().getDisplayModifier() == null) {
                menuItem.getOptions().setDisplayModifier(((viewer, item) -> {
                    ItemUtil.replace(item, crate.replacePlaceholders());
                }));
            }
        });

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
        if (this.editorRewards != null) {
            this.editorRewards.clear();
            this.editorRewards = null;
        }
    }

    @NotNull
    public CrateRewardListEditor getEditorRewards() {
        if (this.editorRewards == null) {
            this.editorRewards = new CrateRewardListEditor(this.object);
        }
        return this.editorRewards;
    }

    private void save(@NotNull MenuViewer viewer) {
        this.object.save();
        this.plugin.runTask(task -> this.open(viewer.getPlayer(), viewer.getPage()));
    }

    @Override
    public void onClick(@NotNull MenuViewer viewer, @Nullable ItemStack item, @NotNull SlotType slotType, int slot, @NotNull InventoryClickEvent event) {
        super.onClick(viewer, item, slotType, slot, event);
        if (slotType == SlotType.PLAYER || slotType == SlotType.PLAYER_EMPTY) {
            event.setCancelled(false);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onCrateBlockClick(PlayerInteractEvent e) {
        if (!this.isReadyForBlock) return;

        Player player = e.getPlayer();

        EditorHandler editor = EditorManager.getEditorHandler(player);
        if (editor == null) return;

        Block block = e.getClickedBlock();
        if (block == null) return;

        e.setUseInteractedBlock(Event.Result.DENY);
        e.setUseItemInHand(Event.Result.DENY);

        if (plugin.getCrateManager().getCrateByBlock(block) != null) return;

        Crate crate = this.object;
        crate.getBlockLocations().add(block.getLocation());
        crate.updateHologram();
        crate.save();
        EditorManager.endEdit(player);
        this.isReadyForBlock = false;
    }
}
