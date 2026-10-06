package su.nightexpress.excellentcrates.cost.lang;

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
public final class CostLang implements LangContainer {

    public static final MessageLocale SELECTION_NOT_FOUND = LangEntry
        .builder("cost.selection.not_found")
        .chatMessage("The selected option is invalid.");

    public static final MessageLocale SELECTION_NOT_ENOUGH_FUNDS = LangEntry
        .builder("cost.selection.not_enough_funds")
        .chatMessage("You do not have " + TagWrappers.RED.wrap(SharedPlaceholders.COST) + "!");

    public static final MessageLocale PIPELINE_WITHDRAWAL_NOTIFY = LangEntry
        .builder("cost.pipeline.withdrawal.notify")
        .chatMessage("You spent " + TagWrappers.WHITE.wrap(SharedPlaceholders.COST) + " to open " +
            TagWrappers.WHITE.wrap(SharedPlaceholders.CRATE_NAME) + "."
        );

    public static final MessageLocale SELECTION_NOTHING_AFFORDABLE = LangEntry
        .builder("cost.selection.nothing_affordable")
        .chatMessage("You cannot afford to open " + TagWrappers.WHITE.wrap(SharedPlaceholders.CRATE_NAME) + ".");

    public static final TextLocale UI_INVENTORY_CATEGORIES_TITLE = LangEntry
        .builder("cost.ui.inventory.categories.title")
        .text("Select a cost category");

    public static final IconLocale UI_INVENTORY_CATEGORIES_BUTTON_CATEGORY = LangEntry
        .iconBuilder("cost.ui.inventory.categories.button.category")
        .rawName(CommonPlaceholders.GENERIC_NAME)
        .rawLore(CommonPlaceholders.GENERIC_DESCRIPTION)
        .br()
        .appendClick("Click to select")
        .build();

    public static final TextLocale UI_INVENTORY_OPTIONS_TITLE = LangEntry
        .builder("cost.ui.inventory.options.title")
        .text("Select a cost option");

    public static final IconLocale UI_INVENTORY_OPTIONS_BUTTON_OPTION = LangEntry
        .iconBuilder("cost.ui.inventory.options.button.option")
        .rawName(CommonPlaceholders.GENERIC_NAME)
        .rawLore(CommonPlaceholders.GENERIC_DESCRIPTION)
        .br()
        .appendInfo(TagWrappers.WHITE.wrap("»") + " You have " + TagWrappers.WHITE.wrap(SharedPlaceholders.BALANCE))
        .appendInfo(TagWrappers.WHITE.wrap("»") + " You can open " +
            TagWrappers.GREEN.wrap(SharedPlaceholders.AVAILABLE) + " crates."
        )
        .br()
        .appendClick("Click to select")
        .build();

    private CostLang() {
    }
}
