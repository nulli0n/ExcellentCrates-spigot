package su.nightexpress.excellentcrates.reward.editor.lang;

import org.bukkit.Material;
import org.bukkit.Sound;

import su.nightexpress.excellentcrates.core.SharedPlaceholders;
import su.nightexpress.nightcore.locale.LangContainer;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.DialogElementLocale;
import su.nightexpress.nightcore.locale.entry.IconLocale;
import su.nightexpress.nightcore.locale.entry.MessageLocale;
import su.nightexpress.nightcore.locale.entry.TextLocale;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

public final class RewardEditorLang implements LangContainer {

    public static final MessageLocale CREATION_ID_GENERATION_FAILED = LangEntry
        .builder("rewards.editor.creation.id_generation.failed")
        .chatMessage(
            Sound.ENTITY_VILLAGER_NO,
            TagWrappers.GRAY.wrap("Can not generate unique reward ID for the given item context: " +
                TagWrappers.WHITE.wrap(SharedPlaceholders.ITEM) + ".")
        );

    public static final MessageLocale CREATION_INVALID_ID = LangEntry
        .builder("rewards.editor.creation.invalid_id")
        .chatMessage(
            Sound.ENTITY_VILLAGER_NO,
            TagWrappers.GRAY.wrap("Invalid reward ID provided: " +
                TagWrappers.WHITE.wrap(CommonPlaceholders.GENERIC_VALUE) + ".")
        );

    public static final MessageLocale CREATION_DUPLICATED_ID = LangEntry
        .builder("rewards.editor.creation.duplicated_id")
        .chatMessage(
            Sound.ENTITY_VILLAGER_NO,
            TagWrappers.GRAY.wrap("Reward with the ID " +
                TagWrappers.WHITE.wrap(CommonPlaceholders.GENERIC_VALUE) + " already exists.")
        );

    public static final MessageLocale DELETION_FAILURE = LangEntry
        .builder("rewards.editor.deletion.failure")
        .chatMessage(
            Sound.ENTITY_VILLAGER_NO,
            TagWrappers.GRAY.wrap("Failed to delete reward with ID " +
                TagWrappers.WHITE.wrap(SharedPlaceholders.REWARD_ID) + ".")
        );

    public static final TextLocale UI_INVENTORY_REWARDS_TITLE = LangEntry
        .builder("rewards.editor.ui.inventory.rewards.title")
        .text("Reward Editor • All Rewards");

    public static final IconLocale UI_INVENTORY_REWARDS_REWARD = LangEntry
        .iconBuilder("rewards.editor.ui.inventory.rewards.reward")
        .rawName(SharedPlaceholders.REWARD_NAME)
        .appendCurrent("ID", SharedPlaceholders.REWARD_ID)
        .appendCurrent("Rarity", SharedPlaceholders.REWARD_RARITY)
        .br()
        .appendClick("Click to edit")
        .build();

    public static final IconLocale UI_INVENTORY_REWARDS_BUTTON_CREATION = LangEntry
        .iconBuilder("rewards.editor.ui.inventory.rewards.button.creation")
        .accentColor(TagWrappers.GREEN)
        .name("Create Reward")
        .appendInfo("To configure and create a reward",
            "manually, " + TagWrappers.GREEN.wrap("drag and drop the desired"),
            "item directly onto this slot."
        )
        .build();

    public static final IconLocale UI_INVENTORY_REWARDS_BUTTON_QUICK_MODE = LangEntry
        .iconBuilder("rewards.editor.ui.inventory.rewards.button.quick_mode")
        .accentColor(TagWrappers.AQUA)
        .name("Quick Mode")
        .appendCurrent("Status", SharedPlaceholders.STATE)
        .br()
        .appendInfo("When enabled, allows you to rapidly",
            "create new rewards by " + TagWrappers.AQUA.wrap("double-clicking"),
            "items inside your inventory."
        )
        .br()
        .appendClick("Click to toggle")
        .build();

    public static final TextLocale UI_INVENTORY_OPTIONS_TITLE = LangEntry
        .builder("rewards.editor.ui.inventory.options.title")
        .text("Reward Editor • Options");

    public static final IconLocale UI_INVENTORY_OPTIONS_BUTTON_PREVIEW = LangEntry
        .iconBuilder("rewards.editor.ui.inventory.options.button.preview")
        .name("Preview Settings")
        .appendInfo("Configure visual reward properties,",
            "including " + TagWrappers.WHITE.wrap("name") + ", " +
                TagWrappers.WHITE.wrap("lore") + ", and " +
                TagWrappers.WHITE.wrap("icon") + "."
        )
        .br()
        .appendClick("Click to navigate")
        .build();

    public static final TextLocale UI_INVENTORY_PREVIEW_TITLE = LangEntry
        .builder("rewards.editor.ui.inventory.preview.title")
        .text("Reward Editor • Preview");

    public static final IconLocale UI_INVENTORY_PREVIEW_BUTTON_NAME = LangEntry
        .iconBuilder("rewards.editor.ui.inventory.preview.button.name")
        .name("Display Name")
        .appendCurrent("Current", CommonPlaceholders.GENERIC_VALUE)
        .br()
        .appendInfo("Configure the visible display name", "shown for this reward in menus", "and messages.")
        .br()
        .appendClick("Click to change")
        .build();

    public static final IconLocale UI_INVENTORY_PREVIEW_BUTTON_LORE = LangEntry
        .iconBuilder("rewards.editor.ui.inventory.preview.button.lore")
        .name("Lore")
        .rawLore(CommonPlaceholders.GENERIC_VALUE, CommonPlaceholders.EMPTY_IF_ABOVE)
        .appendInfo("Configure descriptive lore lines",
            "displayed beneath the reward name."
        )
        .br()
        .appendClick("Click to change")
        .build();

    public static final IconLocale UI_INVENTORY_PREVIEW_BUTTON_ICON = LangEntry
        .iconBuilder("rewards.editor.ui.inventory.preview.button.icon")
        .name("Icon")
        .appendInfo("Sets the primary display item icon.",
            "Can differ completely from the actual",
            "items granted upon winning.",
            "",
            TagWrappers.GOLD.wrap("Double-click") + " any item in your inventory",
            "to set it as the preview icon."
        )
        .build();

    public static final IconLocale UI_INVENTORY_PREVIEW_BUTTON_INHERIT_ICON_META = LangEntry
        .iconBuilder("rewards.editor.ui.inventory.preview.button.inherit_icon_meta")
        .name("Inherit Icon Meta")
        .appendCurrent("State", SharedPlaceholders.STATE)
        .br()
        .appendInfo("When enabled, inherits the " + TagWrappers.WHITE.wrap("display name"),
            "and " + TagWrappers.WHITE.wrap("lore") + " directly from the assigned",
            "icon rather than manual text settings."
        )
        .br()
        .appendClick("Click to toggle")
        .build();

    public static final IconLocale UI_INVENTORY_OPTIONS_BUTTON_DELETE = LangEntry
        .iconBuilder("rewards.editor.ui.inventory.options.button.delete")
        .accentColor(TagWrappers.RED)
        .name("Delete Reward")
        .appendInfo("Permanently deletes the reward.")
        .br()
        .appendClick("Click to delete")
        .build();

    public static final TextLocale UI_DIALOG_CREATION_TITLE = LangEntry
        .builder("rewards.editor.ui.dialog.creation.title")
        .text(TagWrappers.GREEN.and(TagWrappers.UNDERLINED).wrap("Reward Creation"));

    public static final DialogElementLocale UI_DIALOG_CREATION_BODY = LangEntry
        .builder("rewards.editor.ui.dialog.creation.body")
        .dialogElement(
            "You are configuring a " + TagWrappers.GREEN.wrap("new reward") + ".",
            "Ensure the displayed icon matches your intended item.",
            "",
            TagWrappers.GRAY.wrap(
                "Adjust the " + TagWrappers.GOLD.wrap("Reward ID") + " if needed - it " +
                    TagWrappers.RED.wrap("cannot be changed") + " once the reward is created."
            ),
            "",
            TagWrappers.GRAY.wrap("If this item is the primary reward, check " +
                TagWrappers.YELLOW.wrap("Add to Given Items") + " to save time and avoid adding it manually later."
            )
        );

    public static final TextLocale UI_DIALOG_CREATION_INPUT_ID = LangEntry
        .builder("rewards.editor.ui.dialog.creation.input.id")
        .text(TagWrappers.SPRITE_ITEM.apply(Material.NAME_TAG) + " Reward ID");

    /* public static final TextLocale UI_DIALOG_CREATION_INPUT_SET_PREVIEW = LangEntry
        .builder("rewards.editor.ui.dialog.creation.input.set_preview")
        .text(TagWrappers.SPRITE_ITEM.apply(Material.OAK_SIGN) + " Set as Preview"); */

    public static final TextLocale UI_DIALOG_CREATION_INPUT_ADD_TO_GIVEN_ITEMS = LangEntry
        .builder("rewards.editor.ui.dialog.creation.input.add_given_items")
        .text(TagWrappers.SPRITE_ITEM.apply(Material.CHEST_MINECART) + " Add to Given Items");

    public static final TextLocale UI_DIALOG_DELETION_TITLE = LangEntry
        .builder("rewards.editor.ui.dialog.deletion.title")
        .text(TagWrappers.RED.and(TagWrappers.UNDERLINED).wrap("Reward Deletion"));

    public static final DialogElementLocale UI_DIALOG_DELETION_BODY = LangEntry
        .builder("rewards.editor.ui.dialog.deletion.body")
        .dialogElement(
            "Are you sure you want to delete reward " + TagWrappers.RED.wrap(SharedPlaceholders.REWARD_ID) + " ?",
            "",
            TagWrappers.GRAY.wrap("This action is " + TagWrappers.RED.wrap("irreversible") + "."),
            TagWrappers.GRAY.wrap("Think twice before proceeding.")
        );

    public static final TextLocale UI_DIALOG_PREVIEW_NAME_TITLE = LangEntry
        .builder("rewards.editor.ui.dialog.preview_name.title")
        .text(TagWrappers.GOLD.and(TagWrappers.UNDERLINED).wrap("Reward Display Name"));

    public static final DialogElementLocale UI_DIALOG_PREVIEW_NAME_BODY = LangEntry
        .builder("rewards.editor.ui.dialog.preview_name.body")
        .dialogElement(
            "Set the desired " + TagWrappers.GOLD.wrap("display name") + "."
        );

    public static final TextLocale UI_DIALOG_PREVIEW_NAME_INPUT_NAME = LangEntry
        .builder("rewards.editor.ui.dialog.preview_name.input.name")
        .text(TagWrappers.SPRITE_ITEM.apply(Material.NAME_TAG) + " Display Name");

    public static final TextLocale UI_DIALOG_PREVIEW_LORE_TITLE = LangEntry
        .builder("rewards.editor.ui.dialog.preview_lore.title")
        .text(TagWrappers.GOLD.and(TagWrappers.UNDERLINED).wrap("Reward Lore / Description"));


    public static final DialogElementLocale UI_DIALOG_PREVIEW_LORE_BODY = LangEntry
        .builder("rewards.editor.ui.dialog.preview_lore.body")
        .dialogElement(
            "Set the desired " + TagWrappers.GOLD.wrap("lore / description") + "."
        );

    public static final TextLocale UI_DIALOG_PREVIEW_LORE_INPUT_LORE = LangEntry
        .builder("rewards.editor.ui.dialog.preview_lore.input.lore")
        .text(TagWrappers.SPRITE_ITEM.apply(Material.WRITABLE_BOOK) + " Lore / Description");

    public static final TextLocale UI_DIALOG_ITEM_ADD_TITLE = LangEntry
        .builder("rewards.editor.ui.dialog.item_add.title")
        .text(TagWrappers.GOLD.and(TagWrappers.UNDERLINED).wrap("Add Reward Item"));

    public static final TextLocale UI_DIALOG_ITEM_ADD_BODY = LangEntry
        .builder("rewards.editor.ui.dialog.item_add.body")
        .text("Select the desired " + TagWrappers.GOLD.wrap("item save method") + ".");

    private RewardEditorLang() {
    }
}
