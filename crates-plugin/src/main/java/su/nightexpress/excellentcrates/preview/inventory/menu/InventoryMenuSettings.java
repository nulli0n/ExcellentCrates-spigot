package su.nightexpress.excellentcrates.preview.inventory.menu;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.IntStream;

import org.bukkit.Material;
import org.bukkit.inventory.MenuType;

import su.nightexpress.engine.text.layout.LayoutComponent;
import su.nightexpress.engine.text.layout.LayoutComponentGroup;
import su.nightexpress.engine.text.layout.TextLayout;
import su.nightexpress.engine.text.layout.TextLayoutConstants;
import su.nightexpress.excellentcrates.core.SharedPlaceholders;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

public class InventoryMenuSettings {

    private static final int[] DEFAULT_SLOTS = {
        10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34
    };

    public static final TextLayout DEFAULT_REWARD_LORE_LAYOUT = TextLayout.builder()
        .withTextTemplate(Lists.newList(
            "%rarity%",
            TagWrappers.DARK_GRAY.wrap("»") + " " + TagWrappers.GRAY.wrap("Drop Chance: ") +
                TagWrappers.WHITE.wrap(SharedPlaceholders.REWARD_ROLL_CHANCE + "%"),
            "%description%",
            "%cooldowns%",
            "%limit%"
        ))
        .withComponent("rarity", new LayoutComponent(
            SharedPlaceholders.REWARD_HAS_RARITY_MARKER,
            TagWrappers.DARK_GRAY.wrap("»") + " " + TagWrappers.GRAY.wrap("Rarity: ") +
                TagWrappers.WHITE.wrap(SharedPlaceholders.REWARD_RARITY),
            ""
        ))
        .withComponent("description", new LayoutComponent(
            SharedPlaceholders.REWARD_DESCRIPTION,
            TagWrappers.BR + TextLayoutConstants.VALUE,
            ""
        ))
        .withComponent("active_cooldown", new LayoutComponent(
            SharedPlaceholders.REWARD_HAS_ACTIVE_COOLDOWN_MARKER,
            TagWrappers.WHITE.wrap(TagWrappers.SPRITE_ITEMS.apply("item/clock_12")) + " " +
                TagWrappers.RED.wrap("Active cooldown:") + " " +
                TagWrappers.WHITE.wrap(SharedPlaceholders.REWARD_ACTIVE_COOLDOWN),
            ""
        ))
        .withComponent("expected_cooldown", new LayoutComponent(
            SharedPlaceholders.REWARD_HAS_EXPECTED_COOLDOWN_MARKER,
            TagWrappers.WHITE.wrap(TagWrappers.SPRITE_ITEMS.apply("item/clock_12")) + " " +
                TagWrappers.GRAY.wrap("Next cooldown:") + " " +
                TagWrappers.WHITE.wrap(SharedPlaceholders.REWARD_EXPECTED_COOLDOWN),
            ""
        ))
        .withGroup("cooldowns", new LayoutComponentGroup(
            Lists.newList("active_cooldown", "expected_cooldown"),
            TagWrappers.BR,
            TagWrappers.BR + TextLayoutConstants.VALUE
        ))
        .withComponent("limit", new LayoutComponent(
            SharedPlaceholders.REWARD_HAS_LIMIT_MARKER,
            TagWrappers.BR +
                TagWrappers.WHITE.wrap(TagWrappers.SPRITE_ITEM.apply(Material.CHEST_MINECART)) + " " +
                TagWrappers.GRAY.wrap("Remaining: " +
                    TagWrappers.WHITE.wrap(SharedPlaceholders.REWARD_LIMIT_REMAINING) + "/" +
                    TagWrappers.WHITE.wrap(SharedPlaceholders.REWARD_LIMIT_CAPACITY)
                ),
            ""
        ))
        .build();

    private final String     name;
    private final boolean    hideUnavailable;
    private final String     rewardName;
    private final TextLayout rewardLore;
    private final int[]      rewardSlots;

    private final String                                    inventoryTitle;
    private final MenuType                                  inventoryType;
    private final Map<String, InventoryButton>              inventoryItems;
    private final Map<InventoryButtonType, InventoryButton> inventoryButtons;

    InventoryMenuSettings(Builder builder) {
        this.name = builder.name;
        this.hideUnavailable = builder.hideUnavailable;
        this.rewardName = builder.rewardName;
        this.rewardLore = builder.rewardLore;
        this.rewardSlots = builder.rewardSlots;

        this.inventoryTitle = builder.inventoryTitle;
        this.inventoryType = builder.inventoryType;
        this.inventoryItems = Map.copyOf(builder.inventoryItems);
        this.inventoryButtons = Map.copyOf(builder.inventoryButtons);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static InventoryMenuSettings defaults() {
        Map<String, InventoryButton> inventoryItems = new HashMap<>();
        Map<InventoryButtonType, InventoryButton> inventoryButtons = new HashMap<>();

        inventoryItems.put("background_pane_gray", new InventoryButton(
            NightItem.fromType(Material.GRAY_STAINED_GLASS_PANE).hideAllComponents().setHideTooltip(true),
            IntStream.range(0, 45).toArray()
        ));

        inventoryItems.put("background_pane_black", new InventoryButton(
            NightItem.fromType(Material.BLACK_STAINED_GLASS_PANE).hideAllComponents().setHideTooltip(true),
            IntStream.range(45, 54).toArray()
        ));

        inventoryButtons.put(InventoryButtonType.NEXT_PAGE, new InventoryButton(
            NightItem.fromType(Material.ARROW)
                .hideAllComponents()
                .setDisplayName(TagWrappers.AQUA.wrap("Next Page →")),
            53
        ));

        inventoryButtons.put(InventoryButtonType.PREVIOUS_PAGE, new InventoryButton(
            NightItem.fromType(Material.ARROW)
                .hideAllComponents()
                .setDisplayName(TagWrappers.AQUA.wrap("← Previous Page")),
            45
        ));

        return builder()
            .inventoryItems(inventoryItems)
            .inventoryButtons(inventoryButtons)
            .build();
    }

    public String getName() {
        return this.name;
    }

    public boolean isHideUnavailable() {
        return hideUnavailable;
    }

    public String getRewardName() {
        return rewardName;
    }

    public TextLayout getRewardLore() {
        return this.rewardLore;
    }

    public int[] getRewardSlots() {
        return Arrays.copyOf(this.rewardSlots, this.rewardSlots.length);
    }

    public String getInventoryTitle() {
        return inventoryTitle;
    }

    public MenuType getInventoryType() {
        return inventoryType;
    }

    public Map<String, InventoryButton> getInventoryItems() {
        return this.inventoryItems;
    }

    public Map<InventoryButtonType, InventoryButton> getInventoryButtons() {
        return this.inventoryButtons;
    }

    public static class Builder {

        private String     name;
        private boolean    hideUnavailable;
        private String     rewardName;
        private TextLayout rewardLore;
        private int[]      rewardSlots;

        private String                                    inventoryTitle;
        private MenuType                                  inventoryType;
        private Map<String, InventoryButton>              inventoryItems;
        private Map<InventoryButtonType, InventoryButton> inventoryButtons;

        Builder() {
            this.name = "Inventory (Default)";
            this.hideUnavailable = false;
            this.rewardName = SharedPlaceholders.REWARD_NAME;
            this.rewardLore = DEFAULT_REWARD_LORE_LAYOUT;
            this.rewardSlots = DEFAULT_SLOTS;

            this.inventoryTitle = SharedPlaceholders.CRATE_NAME;
            this.inventoryType = MenuType.GENERIC_9X6;
            this.inventoryItems = Map.of();
            this.inventoryButtons = Map.of();
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder hideUnavailable(boolean hideUnavailable) {
            this.hideUnavailable = hideUnavailable;
            return this;
        }

        public Builder rewardName(String rewardName) {
            this.rewardName = rewardName;
            return this;
        }

        public Builder rewardLore(TextLayout rewardLore) {
            this.rewardLore = rewardLore;
            return this;
        }

        public Builder rewardSlots(int... rewardSlots) {
            this.rewardSlots = Arrays.copyOf(rewardSlots, rewardSlots.length);
            return this;
        }

        public Builder inventoryTitle(String inventoryTitle) {
            this.inventoryTitle = inventoryTitle;
            return this;
        }

        public Builder inventoryType(MenuType inventoryType) {
            this.inventoryType = inventoryType;
            return this;
        }

        public Builder inventoryItems(Map<String, InventoryButton> inventoryItems) {
            this.inventoryItems = new LinkedHashMap<>(inventoryItems);
            return this;
        }

        public Builder inventoryButtons(Map<InventoryButtonType, InventoryButton> inventoryButtons) {
            this.inventoryButtons = new LinkedHashMap<>(inventoryButtons);
            return this;
        }

        public InventoryMenuSettings build() {
            return new InventoryMenuSettings(this);
        }
    }
}
