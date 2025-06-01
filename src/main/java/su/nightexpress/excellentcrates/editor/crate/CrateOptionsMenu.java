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
import su.nightexpress.excellentcrates.Placeholders;
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

public class CrateOptionsMenu extends LinkedMenu<CratesPlugin, Crate> {

    private static final String TEXTURE_KEYS       = "311790e8005c7f972c469b7b875eab218e0713afe5f2edfd468659910ed622e3";
    private static final String TEXTURE_REWARDS    = "663029cc8167897e6535a3c5734bbabaff188d0905f9d9353afac62a06dadf86";
    private static final String TEXTURE_PLACEMENT  = "181e124a2765c4b320d754f04e1807ad7b3c26ff95376d0b4263c4e1ae84e758";
    private static final String TEXTURE_MILESTONES = "d194a22345d9cdde75168299ad61873bc105e3ae73cd6c9ac02a285291ad0f1b";
    private static final String SKULL_STACK        = "e2e7ac70bf77ba3dd33f4cb78d88ac149ac6036cef2eac8e7a6fd3676fbaf1aa";

    public CrateOptionsMenu(@NotNull CratesPlugin plugin) {
        super(plugin, MenuType.GENERIC_9X6, Lang.EDITOR_TITLE_CRATE_SETTINGS.getString());


        this.addItem(MenuItem.buildReturn(this, 49, (viewer, event) -> {
            this.runNextTick(() -> this.plugin.getEditorManager().openCrateList(viewer.getPlayer()));
        }));


        this.addItem(ItemUtil.getCustomHead(Placeholders.SKULL_DELETE), EditorLang.CRATE_EDIT_DELETE, 8, (viewer, event, crate) -> {
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


        this.addItem(Material.NAME_TAG, EditorLang.CRATE_EDIT_NAME, 2, (viewer, event, crate) -> {
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


        this.addItem(Material.ITEM_FRAME, EditorLang.CRATE_EDIT_ITEM, 4, (viewer, event, crate) -> {
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
                crate.setName(ItemUtil.getNameSerialized(clean));
                this.saveAndFlush(viewer, crate);
            }
            else {
                this.runNextTick(() -> plugin.getEditorManager().openItemTypeMenu(viewer.getPlayer(), clean, provider -> {
                    crate.setItemProvider(provider);
                    crate.setName(ItemUtil.getNameSerialized(clean));
                    crate.saveSettings();
                    this.runNextTick(() -> this.open(viewer.getPlayer(), crate));
                }));
            }

            event.getView().setCursor(null);

        });

        this.addItem(NightItem.asCustomHead(SKULL_STACK), Lang.EDITOR_BUTTON_CRATE_ITEM_STACKABLE, 0, (viewer, event, crate) -> {
            crate.setItemStackable(!crate.isItemStackable());
            this.saveAndFlush(viewer, crate);
        });


        this.addItem(Material.PAINTING, EditorLang.CRATE_EDIT_PREVIEW, 6, (viewer, event, crate) -> {
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


        this.addItem(NightItem.asCustomHead(TEXTURE_KEYS), EditorLang.CRATE_KEY_REQUIREMENT, 19, (viewer, event, crate) -> {
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


        this.addItem(Material.REDSTONE, EditorLang.CRATE_EDIT_PERMISSION_REQUIREMENT, 21, (viewer, event, crate) -> {
            crate.setPermissionRequired(!crate.isPermissionRequired());
            this.saveAndFlush(viewer, crate);
        }, ItemOptions.builder().setDisplayModifier((viewer, item) -> {
            if (!this.getLink(viewer).isPermissionRequired()) item.setMaterial(Material.GUNPOWDER);
        }).build());


        this.addItem(Material.CLOCK, EditorLang.CRATE_EDIT_OPEN_COOLDOWN, 23, (viewer, event, crate) -> {
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


        this.addItem(Material.GOLD_INGOT, EditorLang.CRATE_EDIT_OPEN_COST, 25, (viewer, event, crate) -> {
            this.runNextTick(() -> plugin.getEditorManager().openCostsMenu(viewer.getPlayer(), crate));
        }, ItemOptions.builder().setVisibilityPolicy(viewer -> Plugins.hasEconomyBridge()).build());


        this.addItem(NightItem.asCustomHead(TEXTURE_PLACEMENT), EditorLang.CRATE_EDIT_PLACEMENT, 38, (viewer, event, crate) -> {
            this.runNextTick(() -> this.plugin.getEditorManager().openPlacementMenu(viewer.getPlayer(), crate));
        });


        this.addItem(NightItem.asCustomHead(TEXTURE_REWARDS), EditorLang.CRATE_EDIT_REWARDS, 40, (viewer, event, crate) -> {
            this.runNextTick(() -> this.plugin.getEditorManager().openRewardList(viewer.getPlayer(), crate));
        });


        this.addItem(NightItem.asCustomHead(TEXTURE_MILESTONES), EditorLang.CRATE_EDIT_MILESTONES, 42, (viewer, event, crate) -> {
            if (event.isRightClick()) {
                crate.setMilestonesRepeatable(!crate.isMilestonesRepeatable());
                crate.saveMilestones();
                this.runNextTick(() -> this.flush(viewer));
                return;
            }

            this.runNextTick(() -> this.plugin.getEditorManager().openMilestones(viewer.getPlayer(), crate));
        }, ItemOptions.builder().setVisibilityPolicy(viewer -> Config.isMilestonesEnabled()).build());
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
