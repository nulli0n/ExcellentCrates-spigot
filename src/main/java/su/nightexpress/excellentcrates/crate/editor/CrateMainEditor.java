package su.nightexpress.excellentcrates.crate.editor;

import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nexmedia.engine.api.menu.impl.EditorMenu;
import su.nexmedia.engine.api.menu.impl.MenuViewer;
import su.nexmedia.engine.editor.EditorManager;
import su.nexmedia.engine.utils.FileUtil;
import su.nexmedia.engine.utils.ItemUtil;
import su.nexmedia.engine.utils.PlayerUtil;
import su.nexmedia.engine.utils.StringUtil;
import su.nightexpress.excellentcrates.ExcellentCratesPlugin;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.api.currency.Currency;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.editor.EditorLocales;

import java.util.List;

public class CrateMainEditor extends EditorMenu<ExcellentCratesPlugin, Crate> {

    private static final String TEXTURE_REWARDS = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjYzMDI5Y2M4MTY3ODk3ZTY1MzVhM2M1NzM0YmJhYmFmZjE4OGQwOTA1ZjlkOTM1M2FmYWM2MmEwNmRhZGY4NiJ9fX0=";
    private static final String TEXTURE_PLACEMENT = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTgxZTEyNGEyNzY1YzRiMzIwZDc1NGYwNGUxODA3YWQ3YjNjMjZmZjk1Mzc2ZDBiNDI2M2M0ZTFhZTg0ZTc1OCJ9fX0=";
    private static final String TEXTURE_MILESTONES = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODE2MjNkNTIzOGRhYjdkZWNkMzIwMjY1Y2FlMWRjNmNhOTFiN2ZhOTVmMzQ2NzNhYWY0YjNhZDVjNmJhMTZlMSJ9fX0=";

    private CratePlacementEditor  placementEditor;
    private RewardListEditor      rewardsEditor;
    private CrateMilestonesEditor milestonesEditor;

    public CrateMainEditor(@NotNull Crate crate) {
        super(crate.plugin(), crate, Config.EDITOR_TITLE_CRATE.get(), 54);

        this.addReturn(49).setClick((viewer, event) -> {
            this.plugin.getEditor().getCratesEditor().openNextTick(viewer, 1);
        });

        this.addItem(Material.NAME_TAG, EditorLocales.CRATE_NAME, 2).setClick((viewer, event) -> {
            this.handleInput(viewer, Lang.EDITOR_ENTER_DISPLAY_NAME, wrapper -> {
                crate.setName(wrapper.getText());
                crate.save();
                return true;
            });
        });

        this.addItem(Material.ITEM_FRAME, EditorLocales.CRATE_ITEM, 4).setClick((viewer, event) -> {
            ItemStack cursor = event.getCursor();
            if (cursor == null || cursor.getType().isAir()) {
                if (event.isLeftClick()) {
                    PlayerUtil.addItem(viewer.getPlayer(), crate.getItem());
                }
                if (event.isRightClick()) {
                    PlayerUtil.addItem(viewer.getPlayer(), crate.getRawItem());
                }
                return;
            }

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

        this.addItem(Material.GLOW_ITEM_FRAME, EditorLocales.CRATE_TEMPLATE, 6).setClick((viewer, event) -> {
            if (event.isShiftClick()) {
                if (event.isLeftClick()) {
                    crate.setPreviewConfig(null);
                }
                else if (event.isRightClick()) {
                    crate.setOpeningConfig(null);
                }

                this.save(viewer);
            }
            else {
                if (event.isLeftClick()) {
                    List<String> previews = FileUtil.getFiles(plugin.getDataFolder() + Config.DIR_PREVIEWS, true)
                        .stream().map(f -> f.getName().replace(".yml", "")).toList();

                    this.handleInput(viewer, Lang.EDITOR_CRATE_ENTER_PREVIEW_CONFIG, wrapper -> {
                        crate.setPreviewConfig(wrapper.getTextRaw());
                        crate.save();
                        return true;
                    });
                    EditorManager.suggestValues(viewer.getPlayer(), previews, true);
                }
                else if (event.isRightClick()) {
                    this.handleInput(viewer, Lang.EDITOR_CRATE_ENTER_ANIMATION_CONFIG, wrapper -> {
                        crate.setOpeningConfig(wrapper.getTextRaw());
                        crate.save();
                        return true;
                    });
                    EditorManager.suggestValues(viewer.getPlayer(), plugin.getCrateManager().getOpeningsMap().keySet(), true);
                }
            }
        });

        this.addItem(Material.BLAZE_ROD, EditorLocales.CRATE_KEYS, 19).setClick((viewer, event) -> {
            if (event.isLeftClick()) {
                this.handleInput(viewer, Lang.EDITOR_CRATE_ENTER_KEY_ID, wrapper -> {
                    crate.getKeyIds().add(wrapper.getTextRaw());
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

        this.addItem(Material.REDSTONE, EditorLocales.CRATE_PERMISSION, 21).setClick((viewer, event) -> {
            crate.setPermissionRequired(!crate.isPermissionRequired());
            this.save(viewer);
        }).getOptions().addDisplayModifier((viewer, item) -> {
            if (!crate.isPermissionRequired()) item.setType(Material.GUNPOWDER);
        });

        this.addItem(Material.CLOCK, EditorLocales.CRATE_OPEN_COOLDOWN, 23).setClick((viewer, event) -> {
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
            this.handleInput(viewer, Lang.EDITOR_CRATE_ENTER_COOLDOWN, wrapper -> {
                crate.setOpenCooldown(wrapper.asAnyInt(0));
                crate.save();
                return true;
            });
        });

        this.addItem(Material.GOLD_INGOT, EditorLocales.CRATE_OPEN_COST, 25).setClick((viewer, event) -> {
            if (event.getClick() == ClickType.DROP) {
                crate.getOpenCostMap().clear();
                this.save(viewer);
                return;
            }

            this.handleInput(viewer, Lang.EDITOR_CRATE_ENTER_OPEN_COST, wrapper -> {
                String[] split = wrapper.getTextRaw().split(" ");

                Currency currency = plugin.getCurrencyManager().getCurrency(split[0]);
                if (currency == null) return true;

                double amount = split.length >= 2 ? StringUtil.getDouble(split[1], 0D) : 0D;

                crate.setOpenCost(currency, amount);
                crate.save();
                return true;
            });
            EditorManager.suggestValues(viewer.getPlayer(), plugin.getCurrencyManager().getCurrencyIds(), false);
        });

        this.addItem(ItemUtil.createCustomHead(TEXTURE_PLACEMENT), EditorLocales.CRATE_PLACEMENT_INFO, 38).setClick((viewer, event) -> {
            this.getPlacementEditor().openNextTick(viewer, 1);
        });

        this.addItem(ItemUtil.createCustomHead(TEXTURE_REWARDS), EditorLocales.CRATE_REWARDS, 40).setClick((viewer, event) -> {
            this.getRewardsEditor().openNextTick(viewer.getPlayer(), 1);
        });

        this.addItem(ItemUtil.createCustomHead(TEXTURE_MILESTONES), EditorLocales.CRATE_MILESTONES, 42).setClick((viewer, event) -> {
            if (event.isRightClick()) {
                crate.setMilestonesRepeatable(!crate.isMilestonesRepeatable());
                this.save(viewer);
                return;
            }

            this.getMilestonesEditor().openNextTick(viewer, 1);
        });

        this.getItems().forEach(menuItem -> menuItem.getOptions().addDisplayModifier(((viewer, item) -> {
                ItemUtil.replace(item, Placeholders.forCrateAll(crate).replacer());
            }))
        );
    }

    @Override
    public void clear() {
        super.clear();
        if (this.rewardsEditor != null) this.rewardsEditor.clear();
        if (this.milestonesEditor != null) this.milestonesEditor.clear();
    }

    @NotNull
    public CratePlacementEditor getPlacementEditor() {
        if (this.placementEditor == null) {
            this.placementEditor = new CratePlacementEditor(this.plugin, this.object);
        }
        return this.placementEditor;
    }

    @NotNull
    public RewardListEditor getRewardsEditor() {
        if (this.rewardsEditor == null) {
            this.rewardsEditor = new RewardListEditor(this.object);
        }
        return this.rewardsEditor;
    }

    @NotNull
    public CrateMilestonesEditor getMilestonesEditor() {
        if (this.milestonesEditor == null) {
            this.milestonesEditor = new CrateMilestonesEditor(this.plugin, this.object);
        }
        return milestonesEditor;
    }

    private void save(@NotNull MenuViewer viewer) {
        this.object.save();
        this.openNextTick(viewer, viewer.getPage());
    }

    @Override
    public void onClick(@NotNull MenuViewer viewer, @Nullable ItemStack item, @NotNull SlotType slotType, int slot, @NotNull InventoryClickEvent event) {
        super.onClick(viewer, item, slotType, slot, event);
        if (slotType == SlotType.PLAYER || slotType == SlotType.PLAYER_EMPTY) {
            event.setCancelled(false);
        }
    }
}
