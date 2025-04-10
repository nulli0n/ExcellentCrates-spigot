package su.nightexpress.excellentcrates.editor.crate;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MenuType;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.api.crate.Reward;
import su.nightexpress.excellentcrates.api.item.ItemProvider;
import su.nightexpress.excellentcrates.config.EditorLang;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.crate.impl.Rarity;
import su.nightexpress.excellentcrates.crate.reward.impl.CommandReward;
import su.nightexpress.excellentcrates.crate.reward.impl.ItemReward;
import su.nightexpress.excellentcrates.item.ItemTypes;
import su.nightexpress.nightcore.ui.UIUtils;
import su.nightexpress.nightcore.ui.dialog.Dialog;
import su.nightexpress.nightcore.ui.menu.MenuViewer;
import su.nightexpress.nightcore.ui.menu.click.ClickResult;
import su.nightexpress.nightcore.ui.menu.confirmation.Confirmation;
import su.nightexpress.nightcore.ui.menu.item.ItemOptions;
import su.nightexpress.nightcore.ui.menu.item.MenuItem;
import su.nightexpress.nightcore.ui.menu.type.LinkedMenu;
import su.nightexpress.nightcore.ui.menu.type.NormalMenu;
import su.nightexpress.nightcore.util.ItemUtil;
import su.nightexpress.nightcore.util.Players;
import su.nightexpress.nightcore.util.bukkit.NightItem;

import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.stream.Stream;

@SuppressWarnings("UnstableApiUsage")
public class RewardOptionsMenu extends LinkedMenu<CratesPlugin, Reward> {

    private static final String TEXTURE_COMMAND      = "c2af9b072d19455809dc9d09d9da8bb32f63ad16b015ac772acd9a9f22c77098";
    private static final String TEXTURE_ITEMS        = "86bd920b402815ad89018df82977be9f7ea19e799ecf016f7f0da4ab47ca23c5";
    private static final String TEXTURE_PERMS_GREEN  = "fae119a2382eda864b244fa8c53ac3e544163103ee66795f0cd6c64f7abb8cf1";
    private static final String TEXTURE_PERMS_RED    = "b45e85edda1a81d224adb713b13b7038d5cc6becd98a716b8a3dec7e3a0f9817";
    private static final String TEXTURE_RARITY       = "c7db2aeca61b7616888b91fbe215501c70fc72ee8165aa971c0312381d41a795";
    private static final String TEXTURE_WEIGHT       = "e0a443e0eca7f5d30622dd937f1e5ea2cdf15d10c27a199c68a7ce09c39f6b69";
    private static final String TEXTURE_BROADCAST    = "1694928bd38f42dca585be02aeff1b293ee22f7a3b1444845ba456ef745b26b1";
    private static final String TEXTURE_PLACEHOLDERS = "c7e2aa79fc62fa4f5a8919f3dd0f12ab35e2d30f8e234bfea896c4ef31eee3db";
    private static final String TEXTURE_LIMIT_ON     = "a4efb34417d95faa94f25769a21676a022d263346c8553eb5525658b34269";
    private static final String TEXTURE_LIMIT_OFF    = "915f7c313bca9c2f958e68ab14ab393867d67503affff8f20cb13fbe917fd31";

    public RewardOptionsMenu(@NotNull CratesPlugin plugin) {
        super(plugin, MenuType.GENERIC_9X6, Lang.EDITOR_TITLE_REWARD_SETTINGS.getString());

        this.addItem(MenuItem.buildReturn(this, 49, (viewer, event) -> {
            this.runNextTick(() -> plugin.getEditorManager().openRewardList(viewer.getPlayer(), this.getLink(viewer).getCrate()));
        }));

        this.addItem(ItemUtil.getSkinHead(Placeholders.SKULL_DELETE), EditorLang.REWARD_EDIT_DELETE, 8, (viewer, event, reward) -> {
            Player player = viewer.getPlayer();
            Crate crate = reward.getCrate();

            UIUtils.openConfirmation(player, Confirmation.builder()
                .onAccept((viewer1, event1) -> {
                    crate.removeReward(reward);
                    crate.saveRewards();
                    plugin.runTask(task -> plugin.getEditorManager().openRewardList(player, crate));
                })
                .onReturn((viewer1, event1) -> {
                    plugin.runTask(task -> plugin.getEditorManager().openRewardOptions(player, reward));
                })
                .build());
        });

        // ---------------------
        // Universal Options
        // ---------------------

        this.addItem(Material.ITEM_FRAME, EditorLang.REWARD_EDIT_ICON, 4, (viewer, event, reward) -> {
            if (event.isRightClick()) {
                Players.addItem(viewer.getPlayer(), reward.getPreviewItem());
                return;
            }

            ItemStack cursor = event.getCursor();
            if (cursor == null || cursor.getType().isAir()) return;

            ItemProvider provider = ItemTypes.fromItem(cursor);
            if (!provider.canProduceItem()) return;

            reward.setPreview(provider);
            event.getView().setCursor(null);
            this.saveAndFlush(viewer, reward);

        }, ItemOptions.builder().setDisplayModifier((viewer, item) -> {
                item.inherit(NightItem.fromItemStack(this.getLink(viewer).getPreviewItem()))
                    .localized(EditorLang.REWARD_EDIT_ICON)
                    .setHideComponents(true);
            }).setVisibilityPolicy(viewer -> {
                Reward reward = this.getLink(viewer);
                if (reward instanceof CommandReward) return true;

                return reward instanceof ItemReward itemReward && itemReward.isCustomPreview();
            }).build()
        );

        this.addItem(ItemUtil.getSkinHead(TEXTURE_RARITY), EditorLang.REWARD_EDIT_RARITY, 10, (viewer, event, reward) -> {
            this.handleInput(Dialog.builder(viewer, Lang.EDITOR_ENTER_RARITY, wrapper -> {
                Rarity rarity = this.plugin.getCrateManager().getRarity(wrapper.getTextRaw());
                if (rarity == null) return true;

                reward.setRarity(rarity);
                reward.save();
                return true;
            }).setSuggestions(plugin.getCrateManager().getRarityIds(), true));
        });

        this.addItem(ItemUtil.getSkinHead(TEXTURE_WEIGHT), EditorLang.REWARD_EDIT_WEIGHT, 11, (viewer, event, reward) -> {
            if (event.isLeftClick()) {
                this.handleInput(Dialog.builder(viewer, Lang.EDITOR_ENTER_WEIGHT, input -> {
                    reward.setWeight(input.asDouble(-1D));
                    reward.save();
                    return true;
                }));
            }
            if (event.isRightClick()) {
                reward.setWeight(-1D);
                this.saveAndFlush(viewer, reward);
            }
        });


        this.addItem(ItemUtil.getSkinHead(TEXTURE_PERMS_RED), EditorLang.REWARD_EDIT_IGNORED_PERMISSIONS, 16, (viewer, event, reward) -> {
            if (event.isRightClick()) {
                reward.getIgnoredPermissions().clear();
                this.saveAndFlush(viewer, reward);
                return;
            }
            this.handleInput(Dialog.builder(viewer, Lang.EDITOR_ENTER_PERMISSION, input -> {
                reward.getIgnoredPermissions().add(input.getTextRaw());
                reward.save();
                return true;
            }));
        });

        this.addItem(ItemUtil.getSkinHead(TEXTURE_PERMS_GREEN), EditorLang.REWARD_EDIT_REQUIRED_PERMISSIONS, 15, (viewer, event, reward) -> {
            if (event.isRightClick()) {
                reward.getRequiredPermissions().clear();
                this.saveAndFlush(viewer, reward);
                return;
            }
            this.handleInput(Dialog.builder(viewer, Lang.EDITOR_ENTER_PERMISSION, input -> {
                reward.getRequiredPermissions().add(input.getTextRaw());
                reward.save();
                return true;
            }));
        });


        this.addItem(ItemUtil.getSkinHead(TEXTURE_BROADCAST), EditorLang.REWARD_EDIT_BROADCAST, 28, (viewer, event, reward) -> {
            reward.setBroadcast(!reward.isBroadcast());
            this.saveAndFlush(viewer, reward);
        });


        this.addItem(ItemUtil.getSkinHead(TEXTURE_PLACEHOLDERS), EditorLang.REWARD_EDIT_PLACEHOLDERS, 30, (viewer, event, reward) -> {
            reward.setPlaceholderApply(!reward.isPlaceholderApply());
            this.saveAndFlush(viewer, reward);
        });


        this.addItem(ItemUtil.getSkinHead(TEXTURE_LIMIT_OFF), EditorLang.REWARD_EDIT_PLAYER_LIMIT, 32, (viewer, event, reward) -> {
            this.runNextTick(() -> plugin.getEditorManager().openRewardLimits(viewer.getPlayer(), reward, reward.getPlayerLimits()));
        }, ItemOptions.builder().setDisplayModifier((viewer, item) -> {
            if (this.getLink(viewer).getPlayerLimits().isEnabled()) item.setSkinURL(TEXTURE_LIMIT_ON);
        }).build());

        this.addItem(ItemUtil.getSkinHead(TEXTURE_LIMIT_OFF), EditorLang.REWARD_EDIT_GLOBAL_LIMIT, 34, (viewer, event, reward) -> {
            this.runNextTick(() -> plugin.getEditorManager().openRewardLimits(viewer.getPlayer(), reward, reward.getGlobalLimits()));
        }, ItemOptions.builder().setDisplayModifier((viewer, item) -> {
            if (this.getLink(viewer).getGlobalLimits().isEnabled()) item.setSkinURL(TEXTURE_LIMIT_ON);
        }).build());


        // ---------------------
        // Command Options
        // ---------------------

        this.addItem(Material.NAME_TAG, EditorLang.REWARD_EDIT_NAME, 2, (viewer, event, reward) -> {
            if (!(reward instanceof CommandReward commandReward)) return;

            this.handleInput(Dialog.builder(viewer, Lang.EDITOR_ENTER_DISPLAY_NAME, input -> {
                commandReward.setName(input.getText());
                commandReward.save();
                return true;
            }));
        }, ItemOptions.builder().setVisibilityPolicy(viewer -> this.getLink(viewer) instanceof CommandReward).build());



        this.addItem(Material.WRITABLE_BOOK, EditorLang.REWARD_EDIT_DESCRIPTION, 6, (viewer, event, reward) -> {
            if (!(reward instanceof CommandReward commandReward)) return;

            if (event.isRightClick()) {
                commandReward.setDescription(new ArrayList<>());
                this.saveAndFlush(viewer, commandReward);
                return;
            }

            this.handleInput(Dialog.builder(viewer, Lang.EDITOR_ENTER_TEXT, input -> {
                commandReward.getDescription().add(input.getText());
                commandReward.save();
                return true;
            }));
        }, ItemOptions.builder().setVisibilityPolicy(viewer -> this.getLink(viewer) instanceof CommandReward).build());


        this.addItem(ItemUtil.getSkinHead(TEXTURE_COMMAND), EditorLang.REWARD_EDIT_COMMANDS, 13, (viewer, event, reward) -> {
            if (!(reward instanceof CommandReward commandReward)) return;

            if (event.isRightClick()) {
                commandReward.getCommands().clear();
                this.saveAndFlush(viewer, commandReward);
                return;
            }
            this.handleInput(Dialog.builder(viewer, Lang.EDITOR_ENTER_COMMAND, input -> {
                commandReward.getCommands().add(input.getText());
                commandReward.save();
                return true;
            }));
        }, ItemOptions.builder().setVisibilityPolicy(viewer -> this.getLink(viewer) instanceof CommandReward).build());


        // ---------------------
        // Item Options
        // ---------------------

        this.addItem(Material.GLOWSTONE_DUST, EditorLang.REWARD_EDIT_CUSTOM_ICON, 2, (viewer, event, reward) -> {
            if (!(reward instanceof ItemReward itemReward)) return;

            itemReward.setCustomPreview(!itemReward.isCustomPreview());
            this.saveAndFlush(viewer, reward);
        }, ItemOptions.builder().setVisibilityPolicy(viewer -> this.getLink(viewer) instanceof ItemReward).build());

        this.addItem(ItemUtil.getSkinHead(TEXTURE_ITEMS), EditorLang.REWARD_EDIT_ITEMS, 13, (viewer, event, reward) -> {
            if (!(reward instanceof ItemReward itemReward)) return;

            new ContentMenu(plugin, itemReward).open(viewer.getPlayer());
        }, ItemOptions.builder().setVisibilityPolicy(viewer -> this.getLink(viewer) instanceof ItemReward).build());
    }

    private void saveAndFlush(@NotNull MenuViewer viewer, @NotNull Reward reward) {
        reward.save();
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
        if (result.isInventory()) {
            event.setCancelled(false);
        }
    }

    private static class ContentMenu extends NormalMenu<CratesPlugin> {

        private final ItemReward reward;

        public ContentMenu(@NotNull CratesPlugin plugin, @NotNull ItemReward reward) {
            super(plugin, MenuType.GENERIC_9X3, reward.getName());
            this.reward = reward;
        }

        @Override
        public boolean isPersistent() {
            return false;
        }

        @Override
        public void onClick(@NotNull MenuViewer viewer, @NotNull ClickResult result, @NotNull InventoryClickEvent event) {
            super.onClick(viewer, result, event);
            event.setCancelled(false);
        }

        @Override
        protected void onPrepare(@NotNull MenuViewer viewer, @NotNull InventoryView view) {

        }

        @Override
        public void onReady(@NotNull MenuViewer viewer, @NotNull Inventory inventory) {
            inventory.setContents(this.reward.getItems().stream().map(ItemProvider::getItemStack).filter(Predicate.not(ItemTypes::isDummy)).toList().toArray(new ItemStack[0]));
        }

        @Override
        public void onClose(@NotNull MenuViewer viewer, @NotNull InventoryCloseEvent event) {
            Inventory inventory = event.getInventory();
            this.reward.setItems(Stream.of(inventory.getContents()).filter(stack -> stack != null && !stack.getType().isAir()).map(ItemTypes::fromItem).toList());
            this.reward.getCrate().saveReward(this.reward);
            this.runNextTick(() -> this.plugin.getEditorManager().openRewardOptions(viewer.getPlayer(), this.reward));
            super.onClose(viewer, event);
        }
    }
}
