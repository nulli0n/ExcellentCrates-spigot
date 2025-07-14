package su.nightexpress.excellentcrates.editor.crate;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MenuType;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.config.EditorLang;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.item.ItemTypes;
import su.nightexpress.excellentcrates.key.CrateKey;
import su.nightexpress.excellentcrates.util.CrateUtils;
import su.nightexpress.nightcore.ui.UIUtils;
import su.nightexpress.nightcore.ui.dialog.Dialog;
import su.nightexpress.nightcore.ui.menu.MenuViewer;
import su.nightexpress.nightcore.ui.menu.click.ClickResult;
import su.nightexpress.nightcore.ui.menu.confirmation.Confirmation;
import su.nightexpress.nightcore.ui.menu.item.ItemOptions;
import su.nightexpress.nightcore.ui.menu.item.MenuItem;
import su.nightexpress.nightcore.ui.menu.type.LinkedMenu;
import su.nightexpress.nightcore.util.ItemUtil;
import su.nightexpress.nightcore.util.Players;
import su.nightexpress.nightcore.util.Plugins;
import su.nightexpress.nightcore.util.bukkit.NightItem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.IntStream;

public class CrateOptionsMenu extends LinkedMenu<CratesPlugin, Crate> {

    public CrateOptionsMenu(@NotNull CratesPlugin plugin) {
        super(plugin, MenuType.GENERIC_9X6, Lang.EDITOR_TITLE_CRATE_SETTINGS.getString());


        this.addItem(MenuItem.buildReturn(this, 49, (viewer, event) -> {
            this.runNextTick(() -> this.plugin.getEditorManager().openCrateList(viewer.getPlayer()));
        }));


        this.addItem(Material.BARRIER, EditorLang.CRATE_EDIT_DELETE, 53, (viewer, event, crate) -> {
            Player player = viewer.getPlayer();

            UIUtils.openConfirmation(player, Confirmation.builder()
                .onAccept((viewer1, event1) -> {
                    plugin.getCrateManager().delete(crate);
                    plugin.runTask(task -> plugin.getEditorManager().openCrateList(player));
                })
                .onReturn((viewer1, event1) -> {
                    plugin.runTask(task -> plugin.getEditorManager().openOptionsMenu(player, crate));
                })
                .build());
        });


        this.addItem(Material.NAME_TAG, EditorLang.CRATE_EDIT_NAME, 10, (viewer, event, crate) -> {
            this.handleInput(Dialog.builder(viewer, Lang.EDITOR_ENTER_DISPLAY_NAME, input -> {
                crate.setName(input.getText());
                crate.saveSettings();
                return true;
            }));
        });


        this.addItem(Material.WRITABLE_BOOK, EditorLang.CRATE_EDIT_DESCRIPTION, 11, (viewer, event, crate) -> {
            if (event.isRightClick()) {
                crate.setDescription(new ArrayList<>());
                this.saveAndFlush(viewer, crate);
                return;
            }

            this.handleInput(Dialog.builder(viewer, Lang.EDITOR_ENTER_TEXT, input -> {
                crate.getDescription().add(input.getText());
                crate.saveSettings();
                return true;
            }));
        });


        this.addItem(Material.ITEM_FRAME, EditorLang.CRATE_EDIT_ITEM, 12, (viewer, event, crate) -> {
            ItemStack cursor = event.getCursor();
            if (cursor == null || cursor.getType().isAir()) {
                ItemStack itemStack = event.isLeftClick() ? crate.getItem() : crate.getRawItem();
                Players.addItem(viewer.getPlayer(), itemStack);
                return;
            }

            // Remove crate tags to avoid infinite recursion in ItemProvider.
            ItemStack clean = CrateUtils.removeCrateTags(new ItemStack(cursor));

            if (!ItemTypes.isCustom(clean)) {
                crate.setItemProvider(ItemTypes.vanilla(clean));
                this.inheritNameAndLore(crate, clean);
                this.saveAndFlush(viewer, crate);
            }
            else {
                this.runNextTick(() -> plugin.getEditorManager().openItemTypeMenu(viewer.getPlayer(), clean, provider -> {
                    crate.setItemProvider(provider);
                    this.inheritNameAndLore(crate, clean);
                    crate.saveSettings();
                    this.runNextTick(() -> this.open(viewer.getPlayer(), crate));
                }));
            }

            event.getView().setCursor(null);
        });

        this.addItem(Material.CHEST_MINECART, Lang.EDITOR_BUTTON_CRATE_ITEM_STACKABLE, 16, (viewer, event, crate) -> {
            crate.setItemStackable(!crate.isItemStackable());
            this.saveAndFlush(viewer, crate);
        });


        this.addItem(Material.PAINTING, EditorLang.CRATE_EDIT_PREVIEW, 14, (viewer, event, crate) -> {
            if (event.isRightClick()) {
                crate.setPreviewEnabled(!crate.isPreviewEnabled());
                this.saveAndFlush(viewer, crate);
                return;
            }

            this.handleInput(Dialog.builder(viewer, Lang.EDITOR_ENTER_PREVIEW_ID, input -> {
                crate.setPreviewId(input.getTextRaw());
                crate.saveSettings();
                return true;
            }).setSuggestions(plugin.getCrateManager().getPreviewNames(), true));
        });


        this.addItem(Material.GLOW_ITEM_FRAME, EditorLang.CRATE_EDIT_ANIMATION, 15, (viewer, event, crate) -> {
            if (event.isRightClick()) {
                crate.setAnimationEnabled(!crate.isAnimationEnabled());
                this.saveAndFlush(viewer, crate);
                return;
            }

            this.handleInput(Dialog.builder(viewer, Lang.EDITOR_ENTER_ANIMATION_ID, input -> {
                crate.setAnimationId(input.getTextRaw());
                crate.saveSettings();
                return true;
            }).setSuggestions(plugin.getOpeningManager().getProviderIds(), true));
        });




        this.addItem(Material.TRIAL_KEY, EditorLang.CRATE_KEY_REQUIREMENT, 28, (viewer, event, crate) -> {
            if (event.isLeftClick()) {
                this.handleInput(Dialog.builder(viewer, Lang.EDITOR_ENTER_KEY_ID, input -> {
                    CrateKey key = this.plugin.getKeyManager().getKeyById(input.getTextRaw());
                    if (key != null) {
                        crate.addKeyId(key.getId());
                        crate.saveSettings();
                    }
                    return true;
                }).setSuggestions(plugin.getKeyManager().getKeyIds(), true));
            }
            else if (event.isRightClick()) {
                crate.setKeyIds(new HashSet<>());
                this.saveAndFlush(viewer, crate);
            }
            else if (event.getClick() == ClickType.DROP) {
                crate.setKeyRequired(!crate.isKeyRequired());
                this.saveAndFlush(viewer, crate);
            }
        });


        this.addItem(Material.REDSTONE, EditorLang.CRATE_EDIT_PERMISSION_REQUIREMENT, 29, (viewer, event, crate) -> {
            crate.setPermissionRequired(!crate.isPermissionRequired());
            this.saveAndFlush(viewer, crate);
        }, ItemOptions.builder().setDisplayModifier((viewer, item) -> {
            if (!this.getLink(viewer).isPermissionRequired()) item.setMaterial(Material.GUNPOWDER);
        }).build());


        this.addItem(Material.CLOCK, EditorLang.CRATE_EDIT_OPEN_COOLDOWN, 30, (viewer, event, crate) -> {
            if (event.getClick() == ClickType.DROP) {
                crate.setOpenCooldown(-1);
                this.saveAndFlush(viewer, crate);
                return;
            }
            if (event.isRightClick()) {
                crate.setOpenCooldown(0);
                this.saveAndFlush(viewer, crate);
                return;
            }
            this.handleInput(Dialog.builder(viewer, Lang.EDITOR_ENTER_SECONDS, input -> {
                crate.setOpenCooldown(input.asInt(0));
                crate.saveSettings();
                return true;
            }));
        });


        this.addItem(Material.GOLD_INGOT, EditorLang.CRATE_EDIT_OPEN_COST, 31, (viewer, event, crate) -> {
            this.runNextTick(() -> plugin.getEditorManager().openCostsMenu(viewer.getPlayer(), crate));
        }, ItemOptions.builder().setVisibilityPolicy(viewer -> Plugins.hasEconomyBridge()).build());


        this.addItem(Material.BEACON, EditorLang.CRATE_EDIT_PLACEMENT, 32, (viewer, event, crate) -> {
            this.runNextTick(() -> this.plugin.getEditorManager().openPlacementMenu(viewer.getPlayer(), crate));
        });


        this.addItem(Material.VAULT, EditorLang.CRATE_EDIT_REWARDS, 33, (viewer, event, crate) -> {
            this.runNextTick(() -> this.plugin.getEditorManager().openRewardList(viewer.getPlayer(), crate));
        });


        this.addItem(Material.CAMPFIRE, EditorLang.CRATE_EDIT_MILESTONES, 34, (viewer, event, crate) -> {
            if (event.isRightClick()) {
                crate.setMilestonesRepeatable(!crate.isMilestonesRepeatable());
                crate.saveMilestones();
                this.runNextTick(() -> this.flush(viewer));
                return;
            }

            this.runNextTick(() -> this.plugin.getEditorManager().openMilestones(viewer.getPlayer(), crate));
        }, ItemOptions.builder().setVisibilityPolicy(viewer -> Config.isMilestonesEnabled()).build());

        this.addItem(NightItem.fromType(Material.BLACK_STAINED_GLASS_PANE).setHideTooltip(true).toMenuItem().setPriority(-1).setSlots(IntStream.range(45, 54).toArray()));
        this.addItem(NightItem.fromType(Material.GLASS_PANE).setHideTooltip(true).toMenuItem().setPriority(-1).setSlots(IntStream.range(19, 26).toArray()));
    }

    private void inheritNameAndLore(@NotNull Crate crate, @NotNull ItemStack from) {
        if (Config.EDITOR_CRATE_INHERITANCE_ITEM_NAME.get()) {
            crate.setName(ItemUtil.getNameSerialized(from));
        }
        if (Config.EDITOR_CRATE_INHERITANCE_ITEM_LORE.get()) {
            crate.setDescription(ItemUtil.getLoreSerialized(from));
        }
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

    @Override
    public void onClick(@NotNull MenuViewer viewer, @NotNull ClickResult result, @NotNull InventoryClickEvent event) {
        super.onClick(viewer, result, event);
        if (result.isInventory() && !event.isShiftClick()) {
            event.setCancelled(false);
        }
    }
}
