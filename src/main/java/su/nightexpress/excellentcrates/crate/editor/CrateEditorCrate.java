package su.nightexpress.excellentcrates.crate.editor;

import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.menu.AbstractMenu;
import su.nexmedia.engine.api.menu.IMenuClick;
import su.nexmedia.engine.api.menu.IMenuItem;
import su.nexmedia.engine.api.menu.MenuItemType;
import su.nexmedia.engine.hooks.Hooks;
import su.nexmedia.engine.utils.*;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.api.crate.ICrate;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.crate.effect.CrateEffectSettings;
import su.nightexpress.excellentcrates.editor.CrateEditorHandler;
import su.nightexpress.excellentcrates.editor.CrateEditorType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class CrateEditorCrate extends AbstractMenu<ExcellentCrates> {

    private final ICrate crate;

    private CrateEditorRewards editorRewards;

    public CrateEditorCrate(@NotNull ExcellentCrates plugin, @NotNull ICrate crate) {
        super(plugin, CrateEditorHandler.CRATE_MAIN, "");
        this.crate = crate;

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
                                plugin.getEditorHandlerNew().startEdit(player, crate, CrateEditorType.CRATE_CHANGE_BLOCK_HOLOGRAM_OFFSET_Y);
                                EditorUtils.tipCustom(player, plugin.lang().Editor_Crate_Enter_BlockHologramOffset.getLocalized());
                                player.closeInventory();
                                return;
                            }
                        }
                        else {
                            if (e.isLeftClick()) {
                                plugin.getEditorHandlerNew().startEdit(player, crate, CrateEditorType.CRATE_CHANGE_BLOCK_HOLOGRAM_TEXT);
                                EditorUtils.tipCustom(player, plugin.lang().Editor_Crate_Enter_BlockHologramText.getLocalized());
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
                                plugin.getEditorHandlerNew().startEdit(player, crate, type2);
                                EditorUtils.tipCustom(player, plugin.lang().Editor_Crate_Enter_BlockLocation.getLocalized());
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
                                plugin.getEditorHandlerNew().startEdit(player, crate, CrateEditorType.CRATE_CHANGE_BLOCK_EFFECT_PARTICLE_DATA);
                                EditorUtils.tipCustom(player, plugin.lang().Editor_Crate_Enter_Particle_Data.getLocalized());
                            }
                            else if (e.isLeftClick()) {
                                plugin.getEditorHandlerNew().startEdit(player, crate, CrateEditorType.CRATE_CHANGE_BLOCK_EFFECT_PARTICLE_NAME);
                                EditorUtils.tipCustom(player, plugin.lang().Editor_Crate_Enter_Particle_Name.getLocalized());

                                List<String> items = Arrays.stream(Particle.values()).map(Particle::name).toList();
                                EditorUtils.sendClickableTips(player, items);
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
                        plugin.getEditorHandlerNew().startEdit(player, crate, type2);
                        EditorUtils.tipCustom(player, plugin.lang().Editor_Crate_Enter_Cooldown.getLocalized());
                        player.closeInventory();
                        return;
                    }
                    case CRATE_CHANGE_KEYS -> {
                        if (e.isLeftClick()) {
                            plugin.getEditorHandlerNew().startEdit(player, crate, type2);
                            EditorUtils.tipCustom(player, plugin.lang().Editor_Crate_Enter_KeyId.getLocalized());
                            EditorUtils.sendClickableTips(player, plugin.getKeyManager().getKeyIds());
                            player.closeInventory();
                            return;
                        }

                        if (e.isRightClick()) {
                            crate.setKeyIds(new HashSet<>());
                        }
                    }
                    case CRATE_CHANGE_NAME -> {
                        plugin.getEditorHandlerNew().startEdit(player, crate, type2);
                        EditorUtils.tipCustom(player, plugin.lang().Editor_Crate_Enter_DisplayName.getLocalized());
                        player.closeInventory();
                        return;
                    }
                    case CRATE_CHANGE_CITIZENS -> {
                        if (!Hooks.hasPlugin(Hooks.CITIZENS)) return;

                        if (e.isLeftClick()) {
                            plugin.getEditorHandlerNew().startEdit(player, crate, type2);
                            EditorUtils.tipCustom(player, plugin.lang().Editor_Crate_Enter_Citizens.getLocalized());
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
                            plugin.getEditorHandlerNew().startEdit(player, crate, CrateEditorType.CRATE_CHANGE_CONFIG_TEMPLATE);
                            EditorUtils.tipCustom(player, plugin.lang().Editor_Crate_Enter_AnimationConfig.getLocalized());
                            EditorUtils.sendClickableTips(player, plugin.getAnimationManager().getAnimationIds());
                        }
                        else if (e.isRightClick()) {
                            if (e.isShiftClick()) {
                                crate.setPreviewConfig(null);
                                break;
                            }
                            List<String> previews = FileUtil.getFiles(plugin.getDataFolder() + Config.DIR_PREVIEWS, true)
                                .stream().map(f -> f.getName().replace(".yml", "")).toList();

                            plugin.getEditorHandlerNew().startEdit(player, crate, CrateEditorType.CRATE_CHANGE_CONFIG_PREVIEW);
                            EditorUtils.tipCustom(player, plugin.lang().Editor_Crate_Enter_PreviewConfig.getLocalized());
                            EditorUtils.sendClickableTips(player, previews);
                        }
                        player.closeInventory();
                        return;
                    }
                    case CRATE_CHANGE_OPEN_COST -> {
                        if (e.isLeftClick()) {
                            plugin.getEditorHandlerNew().startEdit(player, crate, CrateEditorType.CRATE_CHANGE_OPEN_COST_MONEY);
                            EditorUtils.tipCustom(player, plugin.lang().Editor_Crate_Enter_OpenCost_Money.getLocalized());
                        }
                        else if (e.isRightClick()) {
                            plugin.getEditorHandlerNew().startEdit(player, crate, CrateEditorType.CRATE_CHANGE_OPEN_COST_EXP);
                            EditorUtils.tipCustom(player, plugin.lang().Editor_Crate_Enter_OpenCost_Exp.getLocalized());
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
    public boolean cancelClick(@NotNull SlotType slotType, int slot) {
        return slotType != SlotType.EMPTY_PLAYER && slotType != SlotType.PLAYER;
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
}
