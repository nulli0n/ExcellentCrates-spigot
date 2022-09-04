package su.nightexpress.excellentcrates.crate.editor;

import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.editor.EditorInput;
import su.nexmedia.engine.api.menu.AbstractMenu;
import su.nexmedia.engine.api.menu.IMenuClick;
import su.nexmedia.engine.api.menu.IMenuItem;
import su.nexmedia.engine.api.menu.MenuItemType;
import su.nexmedia.engine.editor.EditorManager;
import su.nexmedia.engine.hooks.Hooks;
import su.nexmedia.engine.utils.*;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.api.OpenCostType;
import su.nightexpress.excellentcrates.api.crate.ICrate;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.crate.effect.CrateEffectSettings;
import su.nightexpress.excellentcrates.editor.CrateEditorHandler;
import su.nightexpress.excellentcrates.editor.CrateEditorType;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CrateEditorCrate extends AbstractMenu<ExcellentCrates> {

    private final ICrate crate;

    private CrateEditorRewards editorRewards;

    public CrateEditorCrate(@NotNull ExcellentCrates plugin, @NotNull ICrate crate) {
        super(plugin, CrateEditorHandler.CRATE_MAIN, "");
        this.crate = crate;

        EditorInput<ICrate, CrateEditorType> input = (player, crate2, type, e) -> {
            String msg = StringUtil.color(e.getMessage());

            switch (type) {
                case CRATE_CHANGE_BLOCK_HOLOGRAM_TEXT -> {
                    List<String> list = crate2.getBlockHologramText();
                    list.add(msg);
                    crate2.setBlockHologramText(list);
                }
                case CRATE_CHANGE_BLOCK_HOLOGRAM_OFFSET_Y -> {
                    double offset = StringUtil.getDouble(StringUtil.colorOff(msg), 0D);
                    crate2.setBlockHologramOffsetY(offset);
                }
                case CRATE_CHANGE_COOLDOWN -> {
                    int cooldown = StringUtil.getInteger(StringUtil.colorOff(msg), 0);
                    crate2.setOpenCooldown(cooldown);
                }
                case CRATE_CHANGE_CITIZENS -> {
                    int npcId = StringUtil.getInteger(StringUtil.colorOff(msg), -1);
                    if (npcId < 0) {
                        EditorManager.error(player, EditorManager.ERROR_NUM_INVALID);
                        return false;
                    }

                    Set<Integer> has = IntStream.of(crate2.getAttachedCitizens()).boxed().collect(Collectors.toSet());
                    has.add(npcId);
                    crate2.setAttachedCitizens(has.stream().mapToInt(i -> i).toArray());
                }
                case CRATE_CHANGE_CONFIG_TEMPLATE -> crate2.setAnimationConfig(EditorManager.fineId(msg));
                case CRATE_CHANGE_CONFIG_PREVIEW -> crate2.setPreviewConfig(EditorManager.fineId(msg));
                case CRATE_CHANGE_NAME -> crate2.setName(msg);
                case CRATE_CHANGE_KEYS -> crate2.getKeyIds().add(EditorManager.fineId(msg));
                case CRATE_CHANGE_OPEN_COST_MONEY -> {
                    double costMoney = StringUtil.getDouble(StringUtil.colorOff(msg), -1);
                    if (costMoney < 0) {
                        EditorManager.error(player, EditorManager.ERROR_NUM_INVALID);
                        return false;
                    }
                    crate2.setOpenCost(OpenCostType.MONEY, costMoney);
                }
                case CRATE_CHANGE_OPEN_COST_EXP -> {
                    double costExp = StringUtil.getDouble(StringUtil.colorOff(msg), -1);
                    if (costExp < 0) {
                        EditorManager.error(player, EditorManager.ERROR_NUM_INVALID);
                        return false;
                    }
                    crate2.setOpenCost(OpenCostType.EXP, (int) costExp);
                }
                case CRATE_CHANGE_BLOCK_EFFECT_PARTICLE_NAME -> crate2.getBlockEffect().setParticleName(StringUtil.colorOff(msg));
                case CRATE_CHANGE_BLOCK_EFFECT_PARTICLE_DATA -> crate2.getBlockEffect().setParticleData(StringUtil.colorOff(msg));
                default -> { }
            }

            crate2.save();
            return true;
        };

        IMenuClick clickHandler = (player, type, e) -> {
            if (type instanceof MenuItemType type2) {
                if (type2 == MenuItemType.RETURN) {
                    this.plugin.getEditor().getCratesEditor().open(player, 1);
                }
            }
            else if (type instanceof CrateEditorType type2) {
                ClickType click = e.getClick();

                switch (type2) {
                    case CRATE_DELETE -> {
                        if (!e.isShiftClick()) return;
                        if (!plugin.getCrateManager().delete(crate)) return;
                        this.plugin.getEditor().getCratesEditor().open(player, 1);
                        return;
                    }
                    case CRATE_CHANGE_PERMISSION -> crate.setPermissionRequired(!crate.isPermissionRequired());
                    case CRATE_CHANGE_BLOCK_HOLOGRAM -> {
                        if (e.isShiftClick()) {
                            if (e.isLeftClick()) {
                                crate.setBlockHologramEnabled(!crate.isBlockHologramEnabled());
                            }
                            else if (e.isRightClick()) {
                                EditorManager.startEdit(player, crate, CrateEditorType.CRATE_CHANGE_BLOCK_HOLOGRAM_OFFSET_Y, input);
                                EditorManager.tip(player, plugin.getMessage(Lang.EDITOR_CRATE_ENTER_BLOCK_HOLOGRAM_OFFSET).getLocalized());
                                player.closeInventory();
                                return;
                            }
                        }
                        else {
                            if (e.isLeftClick()) {
                                EditorManager.startEdit(player, crate, CrateEditorType.CRATE_CHANGE_BLOCK_HOLOGRAM_TEXT, input);
                                EditorManager.tip(player, plugin.getMessage(Lang.EDITOR_CRATE_ENTER_BLOCK_HOLOGRAM_TEXT).getLocalized());
                                player.closeInventory();
                                return;
                            }
                            else if (e.isRightClick()) {
                                crate.setBlockHologramText(new ArrayList<>());
                            }
                        }
                    }
                    case CRATE_CHANGE_ITEM -> {
                        if (e.isRightClick()) {
                            PlayerUtil.addItem(player, crate.getItem());
                            return;
                        }

                        ItemStack cursor = e.getCursor();
                        if (cursor == null || cursor.getType().isAir()) return;

                        crate.setItem(cursor);
                        e.getView().setCursor(null);
                    }
                    case CRATE_CHANGE_BLOCK_LOCATION -> {
                        if (e.isShiftClick()) {
                            if (e.isLeftClick()) {
                                crate.setBlockPushbackEnabled(!crate.isBlockPushbackEnabled());
                            }
                        }
                        else {
                            if (e.isLeftClick()) {
                                EditorManager.startEdit(player, crate, type2, input);
                                EditorManager.tip(player, plugin.getMessage(Lang.EDITOR_CRATE_ENTER_BLOCK_LOCATION).getLocalized());
                                player.closeInventory();
                                return;
                            }
                            else {
                                crate.getBlockLocations().clear();
                            }
                        }
                    }
                    case CRATE_CHANGE_BLOCK_EFFECT -> {
                        if (!e.isShiftClick()) {
                            if (e.isLeftClick()) {
                                CrateEffectSettings effect = crate.getBlockEffect();
                                effect.setModel(CollectionsUtil.switchEnum(effect.getModel()));
                            }
                        }
                        else {
                            if (e.isRightClick()) {
                                EditorManager.startEdit(player, crate, CrateEditorType.CRATE_CHANGE_BLOCK_EFFECT_PARTICLE_DATA, input);
                                EditorManager.tip(player, plugin.getMessage(Lang.EDITOR_CRATE_ENTER_PARTICLE_DATA).getLocalized());
                            }
                            else if (e.isLeftClick()) {
                                EditorManager.startEdit(player, crate, CrateEditorType.CRATE_CHANGE_BLOCK_EFFECT_PARTICLE_NAME, input);
                                EditorManager.tip(player, plugin.getMessage(Lang.EDITOR_CRATE_ENTER_PARTICLE_NAME).getLocalized());

                                List<String> items = Arrays.stream(Particle.values()).map(Particle::name).toList();
                                EditorManager.suggestValues(player, items, true);
                            }
                            player.closeInventory();
                            return;
                        }
                    }
                    case CRATE_CHANGE_COOLDOWN -> {
                        if (e.isRightClick()) {
                            crate.setOpenCooldown(-1);
                            break;
                        }
                        EditorManager.startEdit(player, crate, type2, input);
                        EditorManager.tip(player, plugin.getMessage(Lang.EDITOR_CRATE_ENTER_COOLDOWN).getLocalized());
                        player.closeInventory();
                        return;
                    }
                    case CRATE_CHANGE_KEYS -> {
                        if (e.isLeftClick()) {
                            EditorManager.startEdit(player, crate, type2, input);
                            EditorManager.tip(player, plugin.getMessage(Lang.EDITOR_CRATE_ENTER_KEY_ID).getLocalized());
                            EditorManager.suggestValues(player, plugin.getKeyManager().getKeyIds(), true);
                            player.closeInventory();
                            return;
                        }

                        if (e.isRightClick()) {
                            crate.setKeyIds(new HashSet<>());
                        }
                    }
                    case CRATE_CHANGE_NAME -> {
                        EditorManager.startEdit(player, crate, type2, input);
                        EditorManager.tip(player, plugin.getMessage(Lang.EDITOR_CRATE_ENTER_DISPLAY_NAME).getLocalized());
                        player.closeInventory();
                        return;
                    }
                    case CRATE_CHANGE_CITIZENS -> {
                        if (!Hooks.hasPlugin(Hooks.CITIZENS)) return;

                        if (e.isLeftClick()) {
                            EditorManager.startEdit(player, crate, type2, input);
                            EditorManager.tip(player, plugin.getMessage(Lang.EDITOR_CRATE_ENTER_CITIZENS).getLocalized());
                            player.closeInventory();
                            return;
                        }
                        else if (e.isRightClick()) {
                            crate.setAttachedCitizens(new int[0]);
                        }
                    }
                    case CRATE_CHANGE_CONFIG -> {
                        if (e.isLeftClick()) {
                            if (e.isShiftClick()) {
                                crate.setAnimationConfig(null);
                                break;
                            }
                            EditorManager.startEdit(player, crate, CrateEditorType.CRATE_CHANGE_CONFIG_TEMPLATE, input);
                            EditorManager.tip(player, plugin.getMessage(Lang.EDITOR_CRATE_ENTER_ANIMATION_CONFIG).getLocalized());
                            EditorManager.suggestValues(player, plugin.getAnimationManager().getAnimationIds(), true);
                        }
                        else if (e.isRightClick()) {
                            if (e.isShiftClick()) {
                                crate.setPreviewConfig(null);
                                break;
                            }
                            List<String> previews = FileUtil.getFiles(plugin.getDataFolder() + Config.DIR_PREVIEWS, true)
                                .stream().map(f -> f.getName().replace(".yml", "")).toList();

                            EditorManager.startEdit(player, crate, CrateEditorType.CRATE_CHANGE_CONFIG_PREVIEW, input);
                            EditorManager.tip(player, plugin.getMessage(Lang.EDITOR_CRATE_ENTER_PREVIEW_CONFIG).getLocalized());
                            EditorManager.suggestValues(player, previews, true);
                        }
                        player.closeInventory();
                        return;
                    }
                    case CRATE_CHANGE_OPEN_COST -> {
                        if (e.isLeftClick()) {
                            EditorManager.startEdit(player, crate, CrateEditorType.CRATE_CHANGE_OPEN_COST_MONEY, input);
                            EditorManager.tip(player, plugin.getMessage(Lang.EDITOR_CRATE_ENTER_OPEN_COST_MONEY).getLocalized());
                        }
                        else if (e.isRightClick()) {
                            EditorManager.startEdit(player, crate, CrateEditorType.CRATE_CHANGE_OPEN_COST_EXP, input);
                            EditorManager.tip(player, plugin.getMessage(Lang.EDITOR_CRATE_ENTER_OPEN_COST_EXP).getLocalized());
                        }
                        player.closeInventory();
                        return;
                    }
                    case CRATE_OPEN_REWARDS -> {
                        this.getEditorRewards().open(player, 1);
                        return;
                    }
                    default -> {
                        return;
                    }
                }
                crate.save();
                this.open(player, 1);
            }
        };

        for (String sId : cfg.getSection("Content")) {
            IMenuItem menuItem = cfg.getMenuItem("Content." + sId, MenuItemType.class);

            if (menuItem.getType() != null) {
                menuItem.setClick(clickHandler);
            }
            this.addItem(menuItem);
        }

        for (String sId : cfg.getSection("Editor")) {
            IMenuItem menuItem = cfg.getMenuItem("Editor." + sId, CrateEditorType.class);

            if (menuItem.getType() != null) {
                menuItem.setClick(clickHandler);
            }
            this.addItem(menuItem);
        }
    }

    @NotNull
    public CrateEditorRewards getEditorRewards() {
        if (this.editorRewards == null) {
            this.editorRewards = new CrateEditorRewards(this.plugin, this.crate);
        }
        return this.editorRewards;
    }

    @Override
    public void onPrepare(@NotNull Player player, @NotNull Inventory inventory) {

    }

    @Override
    public void onReady(@NotNull Player player, @NotNull Inventory inventory) {

    }

    @Override
    public void onItemPrepare(@NotNull Player player, @NotNull IMenuItem menuItem, @NotNull ItemStack item) {
        super.onItemPrepare(player, menuItem, item);
        ItemUtil.replace(item, this.crate.replacePlaceholders());
    }

    @Override
    public boolean cancelClick(@NotNull InventoryClickEvent e, @NotNull SlotType slotType) {
        return slotType != SlotType.EMPTY_PLAYER && slotType != SlotType.PLAYER;
    }
}
