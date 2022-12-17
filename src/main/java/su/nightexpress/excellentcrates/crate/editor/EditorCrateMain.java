package su.nightexpress.excellentcrates.crate.editor;

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
import su.nexmedia.engine.api.editor.EditorButtonType;
import su.nexmedia.engine.api.editor.EditorInput;
import su.nexmedia.engine.api.editor.EditorObject;
import su.nexmedia.engine.api.menu.IMenuClick;
import su.nexmedia.engine.api.menu.IMenuItem;
import su.nexmedia.engine.api.menu.MenuItemType;
import su.nexmedia.engine.editor.AbstractEditorMenu;
import su.nexmedia.engine.editor.EditorManager;
import su.nexmedia.engine.hooks.Hooks;
import su.nexmedia.engine.utils.*;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.api.OpenCostType;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.crate.Crate;
import su.nightexpress.excellentcrates.crate.effect.CrateEffectSettings;
import su.nightexpress.excellentcrates.editor.CrateEditorMenu;
import su.nightexpress.excellentcrates.editor.CrateEditorType;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EditorCrateMain extends AbstractEditorMenu<ExcellentCrates, Crate> {

    private final Crate crate;

    private EditorCrateRewardList editorRewards;

    public EditorCrateMain(@NotNull Crate crate) {
        super(crate.plugin(), crate, CrateEditorMenu.TITLE_CRATE, 45);
        this.crate = crate;

        EditorInput<Crate, CrateEditorType> input = (player, crate2, type, e) -> {
            String msg = StringUtil.color(e.getMessage());

            switch (type) {
                case CRATE_CHANGE_BLOCK_HOLOGRAM_TEXT -> {
                    List<String> list = crate2.getBlockHologramText();
                    list.add(msg);
                    crate2.setBlockHologramText(list);
                    crate2.updateHologram();
                }
                case CRATE_CHANGE_BLOCK_HOLOGRAM_OFFSET_Y -> {
                    double offset = StringUtil.getDouble(StringUtil.colorOff(msg), 0D);
                    crate2.setBlockHologramOffsetY(offset);
                    crate2.updateHologram();
                }
                case CRATE_CHANGE_COOLDOWN -> {
                    int cooldown = StringUtil.getInteger(StringUtil.colorOff(msg), 0);
                    crate2.setOpenCooldown(cooldown);
                }
                case CRATE_CHANGE_CITIZENS -> {
                    int npcId = StringUtil.getInteger(StringUtil.colorOff(msg), -1);
                    if (npcId < 0) {
                        EditorManager.error(player, plugin.getMessage(Lang.EDITOR_ERROR_NUMBER_GENERIC).getLocalized());
                        return false;
                    }

                    Set<Integer> has = IntStream.of(crate2.getAttachedCitizens()).boxed().collect(Collectors.toSet());
                    has.add(npcId);
                    crate2.setAttachedCitizens(has.stream().mapToInt(i -> i).toArray());
                }
                case CRATE_CHANGE_CONFIG_OPENING -> crate2.setOpeningConfig(EditorManager.fineId(msg));
                case CRATE_CHANGE_CONFIG_PREVIEW -> crate2.setPreviewConfig(EditorManager.fineId(msg));
                case CRATE_CHANGE_NAME -> crate2.setName(msg);
                case CRATE_CHANGE_KEYS -> crate2.getKeyIds().add(EditorManager.fineId(msg));
                case CRATE_CHANGE_OPEN_COST_MONEY -> {
                    double costMoney = StringUtil.getDouble(StringUtil.colorOff(msg), -1);
                    if (costMoney < 0) {
                        EditorManager.error(player, plugin.getMessage(Lang.EDITOR_ERROR_NUMBER_GENERIC).getLocalized());
                        return false;
                    }
                    crate2.setOpenCost(OpenCostType.MONEY, costMoney);
                }
                case CRATE_CHANGE_OPEN_COST_EXP -> {
                    double costExp = StringUtil.getDouble(StringUtil.colorOff(msg), -1);
                    if (costExp < 0) {
                        EditorManager.error(player, plugin.getMessage(Lang.EDITOR_ERROR_NUMBER_GENERIC).getLocalized());
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

        IMenuClick click = (player, type, e) -> {
            if (type instanceof MenuItemType type2) {
                if (type2 == MenuItemType.RETURN) {
                    this.plugin.getEditor().getCratesEditor().open(player, 1);
                }
            }
            else if (type instanceof CrateEditorType type2) {
                switch (type2) {
                    case CRATE_CHANGE_PERMISSION -> crate.setPermissionRequired(!crate.isPermissionRequired());
                    case CRATE_CHANGE_BLOCK_HOLOGRAM -> {
                        if (e.isShiftClick()) {
                            if (e.isLeftClick()) {
                                crate.setBlockHologramEnabled(!crate.isBlockHologramEnabled());
                                crate.updateHologram();
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
                                crate.updateHologram();
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
                        if (e.getClick() == ClickType.DROP) {
                            crate.setBlockPushbackEnabled(!crate.isBlockPushbackEnabled());
                            break;
                        }
                        if (e.isLeftClick()) {
                            EditorManager.startEdit(player, crate, type2, input);
                            EditorManager.tip(player, plugin.getMessage(Lang.EDITOR_CRATE_ENTER_BLOCK_LOCATION).getLocalized());
                            player.closeInventory();
                            return;
                        }
                        else {
                            crate.getBlockLocations().clear();
                            crate.updateHologram();
                        }
                    }
                    case CRATE_CHANGE_BLOCK_EFFECT -> {
                        if (e.getClick() == ClickType.DROP) {
                            CrateEffectSettings effect = crate.getBlockEffect();
                            effect.setModel(CollectionsUtil.switchEnum(effect.getModel()));
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
                        if (e.getClick() == ClickType.DROP) {
                            crate.setOpenCooldown(-1);
                            break;
                        }
                        if (e.isRightClick()) {
                            crate.setOpenCooldown(0);
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
                        if (e.isShiftClick()) {
                            if (e.isRightClick()) {
                                crate.setPreviewConfig(null);
                                break;
                            }
                            List<String> previews = FileUtil.getFiles(plugin.getDataFolder() + Config.DIR_PREVIEWS, true)
                                .stream().map(f -> f.getName().replace(".yml", "")).toList();

                            EditorManager.startEdit(player, crate, CrateEditorType.CRATE_CHANGE_CONFIG_PREVIEW, input);
                            EditorManager.tip(player, plugin.getMessage(Lang.EDITOR_CRATE_ENTER_PREVIEW_CONFIG).getLocalized());
                            EditorManager.suggestValues(player, previews, true);
                        }
                        else {
                            if (e.isRightClick()) {
                                crate.setOpeningConfig(null);
                                break;
                            }
                            EditorManager.startEdit(player, crate, CrateEditorType.CRATE_CHANGE_CONFIG_OPENING, input);
                            EditorManager.tip(player, plugin.getMessage(Lang.EDITOR_CRATE_ENTER_ANIMATION_CONFIG).getLocalized());
                            EditorManager.suggestValues(player, plugin.getCrateManager().getOpeningsMap().keySet(), true);
                        }
                        player.closeInventory();
                        return;
                    }
                    case CRATE_CHANGE_OPEN_COST -> {
                        if (e.isShiftClick()) {
                            if (e.isLeftClick()) {
                                crate.setOpenCost(OpenCostType.MONEY, 0D);
                            }
                            else {
                                crate.setOpenCost(OpenCostType.EXP, 0);
                            }
                            break;
                        }
                        else {
                            if (e.isLeftClick()) {
                                EditorManager.startEdit(player, crate, CrateEditorType.CRATE_CHANGE_OPEN_COST_MONEY, input);
                                EditorManager.tip(player, plugin.getMessage(Lang.EDITOR_CRATE_ENTER_OPEN_COST_MONEY).getLocalized());
                            }
                            else if (e.isRightClick()) {
                                EditorManager.startEdit(player, crate, CrateEditorType.CRATE_CHANGE_OPEN_COST_EXP, input);
                                EditorManager.tip(player, plugin.getMessage(Lang.EDITOR_CRATE_ENTER_OPEN_COST_EXP).getLocalized());
                            }
                        }
                        player.closeInventory();
                        return;
                    }
                    case CRATE_CHANGE_REWARDS -> {
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

        this.loadItems(click);
    }

    @NotNull
    public EditorCrateRewardList getEditorRewards() {
        if (this.editorRewards == null) {
            this.editorRewards = new EditorCrateRewardList(this.crate);
        }
        return this.editorRewards;
    }

    @Override
    public void setTypes(@NotNull Map<EditorButtonType, Integer> map) {
        map.put(MenuItemType.RETURN, 40);
        map.put(CrateEditorType.CRATE_CHANGE_NAME, 2);
        map.put(CrateEditorType.CRATE_CHANGE_ITEM, 4);
        map.put(CrateEditorType.CRATE_CHANGE_PERMISSION, 6);

        map.put(CrateEditorType.CRATE_CHANGE_CITIZENS, 8);

        map.put(CrateEditorType.CRATE_CHANGE_KEYS, 10);
        map.put(CrateEditorType.CRATE_CHANGE_CONFIG, 11);
        map.put(CrateEditorType.CRATE_CHANGE_REWARDS, 13);
        map.put(CrateEditorType.CRATE_CHANGE_COOLDOWN, 15);
        map.put(CrateEditorType.CRATE_CHANGE_OPEN_COST, 16);

        map.put(CrateEditorType.CRATE_CHANGE_BLOCK_LOCATION, 21);
        map.put(CrateEditorType.CRATE_CHANGE_BLOCK_HOLOGRAM, 22);
        map.put(CrateEditorType.CRATE_CHANGE_BLOCK_EFFECT, 23);
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

    @EventHandler(priority = EventPriority.NORMAL)
    public void onCrateBlockClick(PlayerInteractEvent e) {
        Player player = e.getPlayer();

        EditorObject<?, ?> editor = EditorManager.getEditorInput(player);
        if (editor == null) return;

        Block block = e.getClickedBlock();
        if (block == null) return;

        if (editor.getType() == CrateEditorType.CRATE_CHANGE_BLOCK_LOCATION) {
            e.setUseInteractedBlock(Event.Result.DENY);
            e.setUseItemInHand(Event.Result.DENY);

            if (plugin.getCrateManager().getCrateByBlock(block) != null) return;

            Crate crate = (Crate) editor.getObject();
            crate.getBlockLocations().add(block.getLocation());
            crate.updateHologram();
            crate.save();
            EditorManager.endEdit(player);
        }
    }
}
