package su.nightexpress.excellentcrates.reward.feature.commands.editor.ui.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.MenuType;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.commands.RewardCommandExecutionMode;
import su.nightexpress.excellentcrates.api.reward.commands.RewardCommandPool;
import su.nightexpress.excellentcrates.api.reward.commands.RewardCommandsComponent;
import su.nightexpress.excellentcrates.api.reward.component.RewardComponentKeys;
import su.nightexpress.excellentcrates.api.reward.editor.RewardEditorHook;
import su.nightexpress.excellentcrates.api.reward.registry.RewardId;
import su.nightexpress.excellentcrates.api.reward.registry.RewardResolver;
import su.nightexpress.excellentcrates.core.SharedPlaceholders;
import su.nightexpress.excellentcrates.reward.feature.commands.component.StandrdRewardCommandPool;
import su.nightexpress.excellentcrates.reward.feature.commands.editor.ui.RewardCommandsEditorUIController;
import su.nightexpress.excellentcrates.reward.feature.commands.editor.ui.context.CommandBundleContext;
import su.nightexpress.excellentcrates.reward.feature.commands.editor.ui.menu.context.RewardCommandsMenuContext;
import su.nightexpress.excellentcrates.reward.feature.commands.lang.RewardCommandsLang;
import su.nightexpress.excellentcrates.util.UIUtils;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.ui.inventory.item.ItemState;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.ui.inventory.menu.AbstractObjectMenu;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

@NullMarked
public class RewardCommandsMenu extends AbstractObjectMenu<RewardCommandsMenuContext> {

    private static final int[] ITEM_SLOTS = IntStream.range(9, 36).toArray();
    private static final int   ITEM_LIMIT = ITEM_SLOTS.length;

    private final RewardResolver                   resolver;
    private final RewardCommandsEditorUIController controller;

    public RewardCommandsMenu(CratesPlugin plugin,
                              RewardResolver resolver,
                              RewardCommandsEditorUIController controller) {
        super(plugin, MenuType.GENERIC_9X5, RewardCommandsLang.UI_INVENTORY_COMMANDS_TITLE
            .text(), RewardCommandsMenuContext.class);
        this.resolver = resolver;
        this.controller = controller;
    }

    @Override
    public void defineDefaultLayout() {
        this.addBackgroundItem(Material.BLACK_STAINED_GLASS_PANE, IntStream.range(0, 9).toArray());
        this.addBackgroundItem(Material.GRAY_STAINED_GLASS_PANE, IntStream.range(9, 36).toArray());
        this.addBackgroundItem(Material.BLACK_STAINED_GLASS_PANE, IntStream.range(36, 45).toArray());

        this.addBackButton(this::handleBack, 36);
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
        Player player = context.getPlayer();
        RewardCommandsMenuContext menuContext = this.getObject(context);
        RewardId rewardId = menuContext.rewardId();

        Reward reward = this.resolver.resolveReward(rewardId);
        if (reward == null) return;

        RewardCommandsComponent content = reward.getComponentOrNull(RewardComponentKeys.COMMANDS);
        if (content == null) return;

        items.addAll(this.createMenuButtons(content));
        items.addAll(this.createBundleItems(player, content, menuContext));
    }

    private List<MenuItem> createMenuButtons(RewardCommandsComponent content) {
        List<MenuItem> items = new ArrayList<>();

        int countBundles = content.countBundles();
        boolean limitReached = countBundles >= ITEM_LIMIT;

        items.add(MenuItem.custom()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.ANVIL)
                    .hideAllComponents()
                    .localized(RewardCommandsLang.UI_INVENTORY_COMMANDS_BUTTON_ADD_AVAILABLE)
                    .replace(ctx -> ctx
                        .with(SharedPlaceholders.CURRENT, () -> String.valueOf(countBundles))
                        .with(SharedPlaceholders.MAX, () -> String.valueOf(ITEM_LIMIT))
                    )
                )
                .action(this::handleAdd)
                .condition(ctx -> !limitReached)
                .build()
            )
            .state("limit_reached", ItemState.builder()
                .icon(NightItem.fromType(Material.GRAY_DYE)
                    .hideAllComponents()
                    .localized(RewardCommandsLang.UI_INVENTORY_COMMANDS_BUTTON_ADD_UNAVAILABLE)
                    .replace(ctx -> ctx
                        .with(SharedPlaceholders.CURRENT, () -> String.valueOf(countBundles))
                        .with(SharedPlaceholders.MAX, () -> String.valueOf(ITEM_LIMIT))
                    )
                )
                .action(this::handleAdd)
                .condition(ctx -> limitReached)
                .build()
            )
            .slots(40)
            .build()
        );

        items.add(MenuItem.custom()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.REPEATER)
                    .hideAllComponents()
                    .localized(RewardCommandsLang.UI_INVENTORY_COMMANDS_BUTTON_ITERATIONS)
                    .replace(ctx -> ctx
                        .with(CommonPlaceholders.GENERIC_VALUE, () -> {
                            return String.valueOf(content.getIterations());
                        })
                    )
                )
                .action(this::handleIterations)
                .build()
            )
            .slots(2)
            .build()
        );

        items.add(MenuItem.custom()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.LIME_DYE)
                    .hideAllComponents()
                    .localized(RewardCommandsLang.UI_INVENTORY_COMMANDS_BUTTON_STATE)
                    .replace(ctx -> ctx
                        .with(CommonPlaceholders.GENERIC_VALUE, () -> {
                            return CoreLang.STATE_ENABLED_DISALBED.get(content.isEnabled());
                        })
                    )
                )
                .action(this::handleState)
                .condition(ctx -> content.isEnabled())
                .build()
            )
            .state("disabled", ItemState.builder()
                .icon(NightItem.fromType(Material.GRAY_DYE)
                    .hideAllComponents()
                    .localized(RewardCommandsLang.UI_INVENTORY_COMMANDS_BUTTON_STATE)
                    .replace(ctx -> ctx
                        .with(CommonPlaceholders.GENERIC_VALUE, () -> {
                            return CoreLang.STATE_ENABLED_DISALBED.get(content.isEnabled());
                        })
                    )
                )
                .action(this::handleState)
                .condition(ctx -> !content.isEnabled())
                .build()
            )
            .slots(4)
            .build()
        );

        items.add(MenuItem.custom()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.GLOWSTONE)
                    .hideAllComponents()
                    .localized(RewardCommandsLang.UI_INVENTORY_COMMANDS_BUTTON_GIVE_MODE)
                    .replace(ctx -> ctx
                        .with(CommonPlaceholders.GENERIC_VALUE, () -> {
                            return RewardCommandsLang.GIVE_MODE.getLocalized(content.getGiveMode());
                        })
                    )
                )
                .action(this::handleGiveMode)
                .build()
            )
            .slots(6)
            .build()
        );

        return items;
    }

    private List<MenuItem> createBundleItems(Player player, RewardCommandsComponent content,
                                             RewardCommandsMenuContext menuContext) {
        List<MenuItem> items = new ArrayList<>();
        List<RewardCommandPool> bundles = content.getBundles();

        RewardId rewardId = menuContext.rewardId();
        boolean supportsWeight = content.isWeightEffective();
        RewardEditorHook hook = menuContext.hook();
        Runnable refreshUI = () -> this.refresh(player);

        IntStream.range(0, ITEM_SLOTS.length).forEach(index -> {
            RewardCommandPool bundle = index < bundles.size() ? bundles.get(index) : null;
            if (bundle == null) return;

            UUID bundleId = bundle.getId();
            double weight = bundle.getWeight();
            List<String> commands = bundle.getCommands();
            CommandBundleContext bundleContext = new CommandBundleContext(bundleId, supportsWeight, weight, commands);

            NightItem icon = NightItem.fromType(Material.COMMAND_BLOCK_MINECART)
                .hideAllComponents()
                .localized(RewardCommandsLang.UI_INVENTORY_COMMANDS_BUNDLE)
                .replace(ctx -> ctx
                    .with(CommonPlaceholders.GENERIC_VALUE, () -> String.valueOf(index + 1))
                    .with(SharedPlaceholders.WEIGHT, () -> NumberUtil.format(weight))
                    .with(SharedPlaceholders.VALUES, () -> UIUtils.formatCommandList(commands))
                );

            int slot = ITEM_SLOTS[index];

            items.add(MenuItem.custom()
                .defaultState(ItemState.builder()
                    .icon(icon)
                    .action(ctx -> {
                        if (ctx.getEvent().getClick() == ClickType.DROP) {
                            this.controller.onCommandsMenuBundleDeleteClick(player, bundleContext, hook, refreshUI);
                            return;
                        }
                        this.controller.onCommandsMenuBundleClick(player, rewardId, bundleContext, hook, refreshUI);
                    })
                    .build()
                )
                .slots(slot)
                .build()
            );
        });

        return items;
    }

    @Override
    public void onReady(ViewerContext context, InventoryView view, Inventory inventory) {

    }

    @Override
    public void onRender(ViewerContext context, InventoryView view, Inventory inventory) {

    }

    private void handleBack(ActionContext context) {
        RewardCommandsMenuContext menuContext = this.getObject(context);

        menuContext.moveBackward(context.getPlayer());
    }

    private void handleAdd(ActionContext context) {
        Player player = context.getPlayer();
        RewardCommandsMenuContext menuContext = this.getObject(context);
        RewardEditorHook hook = menuContext.hook();
        RewardId rewardId = menuContext.rewardId();

        Reward reward = this.resolver.resolveReward(rewardId);
        if (reward == null) return;

        RewardCommandsComponent commandContent = reward.getComponentOrNull(RewardComponentKeys.COMMANDS);
        if (commandContent == null) return;

        boolean supportsWeight = commandContent.isWeightEffective();
        Runnable refreshUI = () -> this.refresh(player);

        RewardCommandPool newBundle = StandrdRewardCommandPool.createDefault();

        if (this.controller.onCommandsMenuAddClick(player, rewardId, newBundle, hook)) {
            CommandBundleContext bundleContext = CommandBundleContext.from(newBundle, supportsWeight);
            this.controller.onCommandsMenuBundleClick(player, rewardId, bundleContext, hook, refreshUI);
        }
    }

    private void handleState(ActionContext context) {
        Player player = context.getPlayer();
        RewardCommandsMenuContext menuContext = this.getObject(context);
        RewardId rewardId = menuContext.rewardId();

        Reward reward = this.resolver.resolveReward(rewardId);
        if (reward == null) return;

        RewardCommandsComponent commandContent = reward.getComponentOrNull(RewardComponentKeys.COMMANDS);
        if (commandContent == null) return;

        boolean newState = !commandContent.isEnabled();

        if (this.controller.onCommandsMenuStateClick(player, menuContext.hook(), newState)) {
            this.refresh(player);
        }
    }

    private void handleIterations(ActionContext context) {
        Player player = context.getPlayer();
        RewardCommandsMenuContext menuContext = this.getObject(context);
        RewardEditorHook hook = menuContext.hook();
        RewardId rewardId = menuContext.rewardId();

        Reward reward = this.resolver.resolveReward(rewardId);
        if (reward == null) return;

        RewardCommandsComponent commandContent = reward.getComponentOrNull(RewardComponentKeys.COMMANDS);
        if (commandContent == null) return;

        int currentIterations = commandContent.getIterations();

        Runnable refreshUI = () -> this.refresh(player);

        this.controller.onCommandsMenuIterationsClick(player, rewardId, currentIterations, hook, refreshUI);
    }

    private void handleGiveMode(ActionContext context) {
        Player player = context.getPlayer();
        RewardCommandsMenuContext menuContext = this.getObject(context);
        RewardEditorHook hook = menuContext.hook();
        RewardId rewardId = menuContext.rewardId();

        Reward reward = this.resolver.resolveReward(rewardId);
        if (reward == null) return;

        RewardCommandsComponent commandContent = reward.getComponentOrNull(RewardComponentKeys.COMMANDS);
        if (commandContent == null) return;

        RewardCommandExecutionMode currentMode = commandContent.getGiveMode();

        Runnable refreshUI = () -> this.refresh(player);

        this.controller.onCommandsMenuGiveModeClick(player, rewardId, currentMode, hook, refreshUI);
    }
}
