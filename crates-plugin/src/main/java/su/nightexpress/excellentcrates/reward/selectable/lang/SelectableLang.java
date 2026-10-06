package su.nightexpress.excellentcrates.reward.selectable.lang;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.core.SharedPlaceholders;
import su.nightexpress.nightcore.locale.LangContainer;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.IconLocale;
import su.nightexpress.nightcore.locale.entry.MessageLocale;
import su.nightexpress.nightcore.locale.entry.TextLocale;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public final class SelectableLang implements LangContainer {

    public static final MessageLocale ERROR_NO_SELECTABLE_COMPONENT = LangEntry
        .builder("rewards.selectable.error.no_selectable_component")
        .chatMessage("Crate " + TagWrappers.WHITE.wrap(SharedPlaceholders.CRATE_NAME) +
            " has no selectable rewards component."
        );

    public static final IconLocale EDITOR_UI_EXTENSION_BUTTON = LangEntry
        .iconBuilder("rewards.selectable.editor.ui.extension.button")
        .accentColor(TagWrappers.GRADIENT.with("#209CFF", "#68E0CF"))
        .name("Selectable Rewards")
        .appendCurrent("State", SharedPlaceholders.STATE)
        .br()
        .appendInfo(
            "Replaces RNG rolls with manual player",
            "selection of their desired rewards.",
            "",
            TagWrappers.DARK_GRAY.wrap("Total picks are configured in " + TagWrappers.WHITE.wrap("Rewards") + ".")
        )
        .br()
        .appendClick("Click to navigate")
        .build();

    public static final TextLocale EDITOR_UI_INVENTORY_SETTINGS_TITLE = LangEntry
        .builder("rewards.selectable.editor.ui.inventory.settings.title")
        .text("Crate Editor • Selectable Rewards");

    public static final IconLocale EDITOR_UI_INVENTORY_SETTINGS_BUTTON_STATE = LangEntry
        .iconBuilder("rewards.selectable.editor.ui.inventory.settings.button.state")
        .name("State")
        .appendCurrent("Current", SharedPlaceholders.STATE)
        .br()
        .appendInfo("Toggles the selectable rewards feature.")
        .br()
        .appendClick("Click to toggle")
        .build();

    private SelectableLang() {
    }
}
