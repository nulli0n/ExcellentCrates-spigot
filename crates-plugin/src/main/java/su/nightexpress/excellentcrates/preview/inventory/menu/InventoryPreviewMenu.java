package su.nightexpress.excellentcrates.preview.inventory.menu;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.placeholder.CratePlaceholders;
import su.nightexpress.excellentcrates.api.crate.registry.CrateResolver;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.RewardsAPI;
import su.nightexpress.excellentcrates.api.reward.crate.CrateRewardsComponent;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.ui.inventory.action.MenuItemAction;
import su.nightexpress.nightcore.ui.inventory.action.MenuItemActions;
import su.nightexpress.nightcore.ui.inventory.condition.ItemStateCondition;
import su.nightexpress.nightcore.ui.inventory.condition.ItemStateConditions;
import su.nightexpress.nightcore.ui.inventory.item.ItemPopulator;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.ui.inventory.menu.AbstractObjectMenu;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;
import su.nightexpress.nightcore.util.LowerCase;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext;

@NullMarked
public class InventoryPreviewMenu extends AbstractObjectMenu<InventoryMenuContext> {

    private final CrateResolver         crateResolver;
    private final CratePlaceholders     cratePlaceholders;
    private final RewardsAPI            rewardsAPI;
    private final InventoryMenuSettings settings;

    private final ItemPopulator<Identifier> rewardPopulator;

    public InventoryPreviewMenu(CratesPlugin plugin,
                                CratePlaceholders cratePlaceholders,
                                CrateResolver crateResolver,
                                RewardsAPI rewardsAPI,
                                InventoryMenuSettings settings) {
        super(plugin, settings.getInventoryType(), settings.getInventoryTitle(), InventoryMenuContext.class);
        this.cratePlaceholders = cratePlaceholders;
        this.crateResolver = crateResolver;
        this.rewardsAPI = rewardsAPI;
        this.settings = settings;

        this.rewardPopulator = ItemPopulator.builder(Identifier.class)
            .slots(this.settings.getRewardSlots())
            .itemProvider((context, rewardId) -> {
                Player player = context.getPlayer();
                InventoryMenuContext menuContext = this.getObject(context);

                Crate crate = this.crateResolver.resolveCrate(menuContext.crateId());
                if (crate == null) return null; // Crate is not found, skip.

                Reward reward = this.rewardsAPI.getReward(rewardId);
                if (reward == null) return null; // Reward is not found, skip.

                NightItem icon = this.rewardsAPI.getView().createPreviewIcon(reward);

                PlaceholderContext placeholders = PlaceholderContext.builder()
                    .apply(this.cratePlaceholders.basePlaceholders(crate))
                    .apply(this.rewardsAPI.getPlaceholders().allPlaceholders(crate, reward, player))
                    .build();

                String name = placeholders.apply(this.settings.getRewardName());
                List<String> lore = this.settings.getRewardLore().render(placeholders);

                return icon
                    .setDisplayName(name)
                    .setLore(lore);
            })
            .actionProvider(reward -> context -> {
                // No action is required
            })
            .build();
    }

    @Override
    protected String getRawTitle(ViewerContext context) {
        InventoryMenuContext menuContext = this.getObject(context);

        Crate crate = this.crateResolver.resolveCrate(menuContext.crateId());
        if (crate == null) return super.getRawTitle(context);

        PlaceholderContext placeholders = PlaceholderContext.builder()
            .apply(this.cratePlaceholders.basePlaceholders(crate))
            .build();

        return placeholders.apply(super.getRawTitle(context));
    }

    @Override
    public void defineDefaultLayout() {
        this.settings.getInventoryItems().forEach((id, item) -> {
            this.addDefaultButton(id, MenuItem.custom()
                .defaultState(item.getIcon())
                .slots(item.getSlots())
                .build()
            );
        });

        this.settings.getInventoryButtons().forEach(this::addDefaultButton);
    }

    private void addDefaultButton(InventoryButtonType type, InventoryButton button) {
        MenuItemAction action = context -> {
        };
        ItemStateCondition condition = context -> true;

        switch (type) {
            case RETURN -> {
                action = this::backToCrateMenu;
            }
            case CLOSE -> {
                action = this::closeMenu;
            }
            case NEXT_PAGE -> {
                action = MenuItemActions.NEXT_PAGE;
                condition = ItemStateConditions.NEXT_PAGE;
            }
            case PREVIOUS_PAGE -> {
                action = MenuItemActions.PREVIOUS_PAGE;
                condition = ItemStateConditions.PREVIOUS_PAGE;
            }
            case NEXT_PAGE_INACTIVE -> {
                condition = context -> !context.getViewer().canNavigateForward();
            }
            case PREVIOUS_PAGE_INACTIVE -> {
                condition = context -> !context.getViewer().canNavigateBackward();
            }
            default -> {
            }
        }

        this.addDefaultButton(LowerCase.internal(type.name()), MenuItem.custom()
            .defaultState(button.getIcon(), action, condition)
            .slots(button.getSlots())
            .build()
        );
    }

    @Override
    protected void onClick(ViewerContext context, InventoryClickEvent event) {

    }

    @Override
    protected void onClose(ViewerContext context, InventoryCloseEvent event) {

    }

    @Override
    protected void onDrag(ViewerContext context, InventoryDragEvent event) {

    }

    @Override
    protected void onLoad(FileConfig config) {

    }

    @Override
    public void registerActions() {

    }

    @Override
    public void registerConditions() {

    }

    @Override
    public void onPrepare(ViewerContext context, InventoryView view, Inventory inventory, List<MenuItem> items) {
        InventoryMenuContext menuContext = this.getObject(context);
        Crate crate = this.crateResolver.resolveCrate(menuContext.crateId());
        if (crate == null) return; // Crate is not found, skip.

        List<Identifier> rewardIds = new ArrayList<>();

        CrateRewardsComponent rewards = this.rewardsAPI.getRewardsComponent(crate);
        if (rewards == null) return; // No rewards component, skip.

        Player player = context.getPlayer();

        rewards.getRewards().forEach(crateReward -> {
            if (crateReward.getWeight() <= 0) return; // Skip rewards with zero weight.

            Identifier rewardId = crateReward.getRewardId();
            Reward reward = this.rewardsAPI.getReward(rewardId);
            if (reward == null) return; // Reward is not found, skip.

            if (this.settings.isHideUnavailable()) {
                boolean isAvailable = this.rewardsAPI.getQuota().testQuotas(player, crate, reward).success();
                if (!isAvailable) return; // Skip unavailable rewards.
            }

            rewardIds.add(rewardId);
        });

        rewardIds.sort(Comparator.comparing(Identifier::value));

        this.rewardPopulator.populateTo(context, rewardIds, items);
    }

    @Override
    public void onReady(ViewerContext context, InventoryView view, Inventory inventory) {

    }

    @Override
    public void onRender(ViewerContext context, InventoryView view, Inventory inventory) {

    }

    private void backToCrateMenu(ActionContext context) {
        Player player = context.getPlayer();
        InventoryMenuContext menuContext = this.getObject(context);

        menuContext.moveBackward(player);
    }

    private void closeMenu(ActionContext context) {
        context.getPlayer().closeInventory();
    }
}