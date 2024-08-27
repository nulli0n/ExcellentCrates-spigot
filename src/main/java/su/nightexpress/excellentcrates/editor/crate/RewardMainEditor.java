package su.nightexpress.excellentcrates.editor.crate;

import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.config.EditorLang;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.crate.impl.LimitType;
import su.nightexpress.excellentcrates.crate.impl.Rarity;
import su.nightexpress.excellentcrates.crate.impl.Reward;
import su.nightexpress.excellentcrates.crate.impl.RewardWinLimit;
import su.nightexpress.nightcore.menu.MenuOptions;
import su.nightexpress.nightcore.menu.MenuSize;
import su.nightexpress.nightcore.menu.MenuViewer;
import su.nightexpress.nightcore.menu.click.ClickResult;
import su.nightexpress.nightcore.menu.impl.AbstractMenu;
import su.nightexpress.nightcore.menu.impl.EditorMenu;
import su.nightexpress.nightcore.util.ItemReplacer;
import su.nightexpress.nightcore.util.ItemUtil;
import su.nightexpress.nightcore.util.Players;

import java.util.stream.Stream;

public class RewardMainEditor extends EditorMenu<CratesPlugin, Reward> implements CrateEditor {

    private static final String TEXTURE_COMMAND = "d174349f79311d104d7917d32bf7a0dcee423421ca9e8a131f2d402a3c538572";
    private static final String TEXTURE_ITEMS = "7a3c8c6d3aaa96363d4bef2578f1024781ea14e9d85a9dcfc0935847a6fb5c8d";
    private static final String TEXTURE_PERMS = "264d3ca1206e921c66a2cef74b854170541e4ee9abe8fa678cfaf964964a16a2";
    private static final String TEXTURE_WEIGHT = "e0a443e0eca7f5d30622dd937f1e5ea2cdf15d10c27a199c68a7ce09c39f6b69";

    public RewardMainEditor(@NotNull CratesPlugin plugin) {
        super(plugin, Lang.EDITOR_TITLE_REWARD_SETTINGS.getString(), MenuSize.CHEST_54);

        this.addReturn(49, (viewer, event, reward) -> {
            this.runNextTick(() -> plugin.getEditorManager().openRewards(viewer.getPlayer(), reward.getCrate()));
        });

        this.addItem(Material.ITEM_FRAME, EditorLang.REWARD_PREVIEW, 4, (viewer, event, reward) -> {
            if (event.isRightClick()) {
                Players.addItem(viewer.getPlayer(), reward.getPreview());
                return;
            }

            ItemStack cursor = event.getCursor();
            if (cursor != null && !cursor.getType().isAir()) {
                reward.setPreview(cursor);
                event.getView().setCursor(null);
                this.saveReward(viewer, reward, true);
            }
        }).getOptions().setDisplayModifier(((viewer, item) -> {
            Reward reward = this.getLink(viewer);
            item.setType(reward.getPreview().getType());
            item.setItemMeta(reward.getPreview().getItemMeta());
            ItemReplacer.create(item).readLocale(EditorLang.REWARD_PREVIEW).hideFlags().writeMeta();
        }));

        this.addItem(Material.FLOWER_BANNER_PATTERN, EditorLang.REWARD_SET_PLACEHOLDERS, 13, (viewer, event, reward) -> {
            reward.setPlaceholderApply(!reward.isPlaceholderApply());
            this.saveReward(viewer, reward, true);
        });

        this.addItem(Material.NAME_TAG, EditorLang.REWARD_DISPLAY_NAME, 19, (viewer, event, reward) -> {
            if (event.isShiftClick()) {
                if (event.isLeftClick()) {
                    reward.setName(ItemUtil.getItemName(reward.getPreview()));
                } else if (event.isRightClick()) {
                    ItemStack preview = reward.getPreview();
                    ItemUtil.editMeta(preview, meta -> meta.setDisplayName(reward.getNameTranslated()));
                    reward.setPreview(preview);
                }
                this.saveReward(viewer, reward, true);
                return;
            }

            this.handleInput(viewer, Lang.EDITOR_ENTER_DISPLAY_NAME, (dialog, input) -> {
                reward.setName(input.getText());
                this.saveReward(viewer, reward, false);
                return true;
            });
        });

        this.addItem(Material.ENDER_PEARL, EditorLang.REWARD_BROADCAST, 28, (viewer, event, reward) -> {
            reward.setBroadcast(!reward.isBroadcast());
            this.saveReward(viewer, reward, true);
        }).getOptions().addDisplayModifier((viewer, item) -> {
            if (this.getLink(viewer).isBroadcast()) item.setType(Material.ENDER_EYE);
        });

        this.addItem(ItemUtil.getSkinHead(TEXTURE_WEIGHT), EditorLang.REWARD_WEIGHT, 21, (viewer, event, reward) -> {
            if (event.isLeftClick()) {
                this.handleInput(viewer, Lang.EDITOR_ENTER_RARITY, (dialog, wrapper) -> {
                    Rarity rarity = this.plugin.getCrateManager().getRarity(wrapper.getTextRaw());
                    if (rarity == null) return true;

                    reward.setRarity(rarity);
                    this.saveReward(viewer, reward, false);
                    return true;
                }).setSuggestions(plugin.getCrateManager().getRarityMap().keySet(), true);
            }
            if (event.isRightClick()) {
                this.handleInput(viewer, Lang.EDITOR_ENTER_WEIGHT, (dialog, input) -> {
                    reward.setWeight(input.asDouble());
                    this.saveReward(viewer, reward, false);
                    return true;
                });
            }
        });

        this.addItem(Material.WATER_BUCKET, EditorLang.REWARD_PLAYER_WIN_LIMIT, 25, this.getWinLimitClick(LimitType.PLAYER))
                .getOptions().addDisplayModifier((viewer, itemStack) -> {
                    if (!this.getLink(viewer).getWinLimit(LimitType.PLAYER).isEnabled()) itemStack.setType(Material.BUCKET);
                });

        this.addItem(Material.LAVA_BUCKET, EditorLang.REWARD_GLOBAL_WIN_LIMIT, 34, this.getWinLimitClick(LimitType.GLOBAL))
                .getOptions().addDisplayModifier((viewer, itemStack) -> {
                    if (!this.getLink(viewer).getWinLimit(LimitType.GLOBAL).isEnabled()) itemStack.setType(Material.BUCKET);
                });

        this.addItem(ItemUtil.getSkinHead(TEXTURE_COMMAND), EditorLang.REWARD_COMMANDS, 32, (viewer, event, reward) -> {
            if (event.isRightClick()) {
                reward.getCommands().clear();
                this.saveReward(viewer, reward, true);
                return;
            }
            this.handleInput(viewer, Lang.EDITOR_ENTER_COMMAND, (dialog, input) -> {
                reward.getCommands().add(input.getText());
                this.saveReward(viewer, reward, false);
                return true;
            });
        });

        this.addItem(ItemUtil.getSkinHead(TEXTURE_ITEMS), EditorLang.REWARD_ITEMS, 23, (viewer, event, reward) -> {
            new ContentEditor(plugin, reward).open(viewer.getPlayer());
        });

        this.addItem(ItemUtil.getSkinHead(TEXTURE_PERMS), EditorLang.REWARD_IGNORED_PERMISSIONS, 30, (viewer, event, reward) -> {
            if (event.isRightClick()) {
                reward.getIgnoredForPermissions().clear();
                this.saveReward(viewer, reward, true);
                return;
            }
            this.handleInput(viewer, Lang.EDITOR_ENTER_PERMISSION, (dialog, input) -> {
                reward.getIgnoredForPermissions().add(input.getTextRaw());
                this.saveReward(viewer, reward, false);
                return true;
            });
        });

        this.getItems().forEach(menuItem -> menuItem.getOptions().addDisplayModifier((viewer, item) -> {
            ItemReplacer.replace(item, Placeholders.forRewardAll(this.getLink(viewer)).replacer());
        }));
    }

    @NotNull
    private EditorHandler<Reward> getWinLimitClick(@NotNull LimitType limitType) {
        return (viewer, event, reward) -> {
            RewardWinLimit winLimit = reward.getWinLimit(limitType);

            if (event.getClick() == ClickType.SWAP_OFFHAND && limitType == LimitType.GLOBAL) {
                reward.resetGlobalWinData();
                return;
            }

            if (event.getClick() == ClickType.DROP) {
                winLimit.setEnabled(!winLimit.isEnabled());
                if (winLimit.isEnabled() && limitType == LimitType.GLOBAL) {
                    reward.loadGlobalWinData();
                }
                this.saveReward(viewer, reward, true);
                return;
            }

            if (event.isShiftClick()) {
                if (event.isLeftClick()) {
                    this.handleInput(viewer, Lang.EDITOR_ENTER_AMOUNT, (dialog, input) -> {
                        winLimit.setCooldownStep(input.asInt(1));
                        this.saveReward(viewer, reward, false);
                        return true;
                    });
                } else if (event.isRightClick()) {
                    winLimit.setMidnightCooldown();
                    this.saveReward(viewer, reward, true);
                }
                return;
            }

            if (event.isLeftClick()) {
                this.handleInput(viewer, Lang.EDITOR_ENTER_AMOUNT, (dialog, input) -> {
                    winLimit.setAmount(input.asAnyInt(-1));
                    this.saveReward(viewer, reward, false);
                    return true;
                });
            } else if (event.isRightClick()) {
                this.handleInput(viewer, Lang.EDITOR_ENTER_SECONDS, (dialog, input) -> {
                    winLimit.setCooldown(input.asAnyInt(0));
                    this.saveReward(viewer, reward, false);
                    return true;
                });
            }
        };
    }

    @Override
    protected void onPrepare(@NotNull MenuViewer viewer, @NotNull MenuOptions options) {

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

    private static class ContentEditor extends AbstractMenu<CratesPlugin> {

        private final Reward reward;

        public ContentEditor(@NotNull CratesPlugin plugin, @NotNull Reward reward) {
            super(plugin, reward.getName(), MenuSize.CHEST_27);
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
        protected void onPrepare(@NotNull MenuViewer viewer, @NotNull MenuOptions options) {

        }

        @Override
        public void onReady(@NotNull MenuViewer viewer, @NotNull Inventory inventory) {
            inventory.setContents(this.reward.getItems().stream().map(ItemStack::new).toList().toArray(new ItemStack[0]));
        }

        @Override
        public void onClose(@NotNull MenuViewer viewer, @NotNull InventoryCloseEvent event) {
            Inventory inventory = event.getInventory();
            this.reward.setItems(Stream.of(inventory.getContents()).toList());
            this.reward.getCrate().saveReward(this.reward);
            this.runNextTick(() -> this.plugin.getEditorManager().openReward(viewer.getPlayer(), this.reward));
            super.onClose(viewer, event);
        }
    }
}
