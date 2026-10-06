package su.nightexpress.excellentcrates.reward.items.lang;

import org.bukkit.Sound;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.core.SharedPlaceholders;
import su.nightexpress.nightcore.locale.LangContainer;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.IconLocale;
import su.nightexpress.nightcore.locale.entry.MessageLocale;
import su.nightexpress.nightcore.locale.entry.TextLocale;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public final class RewardItemsLang implements LangContainer {

    public static final MessageLocale ERROR_NO_ITEMS_COMPONENT = LangEntry
        .builder("rewards.items.error.no_items_component")
        .chatMessage("Reward " + TagWrappers.WHITE.wrap(SharedPlaceholders.REWARD_NAME) + " has no items component.");

    public static final MessageLocale ITEMS_REMOVE_INVALID_INDEX = LangEntry
        .builder("rewards.editor.items.remove.invalid_index")
        .chatMessage(
            Sound.ENTITY_VILLAGER_NO,
            TagWrappers.GRAY.wrap("Invalid item index provided: " +
                TagWrappers.WHITE.wrap(CommonPlaceholders.GENERIC_VALUE) + ".")
        );

    public static final IconLocale UI_INVENTORY_OPTIONS_BUTTON_ITEM_CONTENT = LangEntry
        .iconBuilder("rewards.editor.ui.inventory.options.button.item_content")
        .name("Given Items")
        .appendCurrent("Items", SharedPlaceholders.AMOUNT)
        .br()
        .appendInfo("Configure physical items deposited",
            "directly into the player's inventory",
            "upon winning this reward."
        )
        .br()
        .appendClick("Click to navigate")
        .build();

    public static final TextLocale UI_INVENTORY_ITEMS_TITLE = LangEntry
        .builder("rewards.editor.ui.inventory.items.title")
        .text("Reward Options • Given Items");

    public static final IconLocale UI_INVENTORY_ITEMS_ADD_AVAILABLE = LangEntry
        .iconBuilder("rewards.editor.ui.inventory.items.button.add.available")
        .accentColor(TagWrappers.GREEN)
        .name("Add Item")
        .appendCurrent("Items Added", SharedPlaceholders.CURRENT + "/" + SharedPlaceholders.MAX)
        .br()
        .appendInfo(
            TagWrappers.GREEN.wrap("Double-click") + " any item in",
            "your inventory to add it",
            "to the reward.",
            "",
            "Press " + TagWrappers.RED.wrap("[" + TagWrappers.KEY.apply("key.drop") + "]") + " on the item",
            "to delete it from the reward."
        )
        .build();

    public static final IconLocale UI_INVENTORY_ITEMS_ADD_UNAVAILABLE = LangEntry
        .iconBuilder("rewards.editor.ui.inventory.items.button.add.unavailable")
        .accentColor(TagWrappers.RED)
        .name("Add Item")
        .appendCurrent("Items Added", SharedPlaceholders.CURRENT + "/" + SharedPlaceholders.MAX)
        .br()
        .appendInfo("There are maximum items added.")
        .br()
        .appendInfo(
            "Press " + TagWrappers.RED.wrap("[" + TagWrappers.KEY.apply("key.drop") + "]") + " on the item",
            "to delete it from the reward."
        )
        .build();

    private RewardItemsLang() {
    }
}
