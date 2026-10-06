package su.nightexpress.excellentcrates.preview.lang;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.core.SharedPlaceholders;
import su.nightexpress.nightcore.locale.LangContainer;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.MessageLocale;
import su.nightexpress.nightcore.locale.entry.TextLocale;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public final class PreviewLang implements LangContainer {

    public static final TextLocale COMMAND_PREVIEW_DESCRIPTION = LangEntry
        .builder("preview.command.preview.description")
        .text("Preview a crate.");

    public static final MessageLocale COMMAND_PREVIEW_SELF = LangEntry
        .builder("preview.command.preview.self")
        .chatMessage("You have opened " + TagWrappers.WHITE.wrap(SharedPlaceholders.CRATE_NAME) + " preview.");

    public static final MessageLocale COMMAND_PREVIEW_OTHERS = LangEntry
        .builder("preview.command.preview.others")
        .chatMessage("You have showed " + TagWrappers.WHITE.wrap(SharedPlaceholders.CRATE_NAME) + " preview for " +
            TagWrappers.WHITE.wrap(CommonPlaceholders.PLAYER_NAME) + "."
        );

    public static final MessageLocale ERROR_NO_PREVIEW_COMPONENT = LangEntry
        .builder("preview.error.no_preview_component")
        .chatMessage("Crate " + TagWrappers.WHITE.wrap(SharedPlaceholders.CRATE_NAME) +
            " has no preview configured."
        );

    public static final MessageLocale ERROR_PREVIEW_NOT_FOUND = LangEntry
        .builder("preview.error.preview_not_found")
        .chatMessage("Preview " + TagWrappers.WHITE.wrap(CommonPlaceholders.GENERIC_VALUE) +
            " was not found."
        );

    public static final MessageLocale ERROR_PREVIEW_INVENTORY_NOT_OPENED = LangEntry
        .builder("preview.error.preview_inventory_not_opened")
        .chatMessage("Preview inventory could not be opened.");

    private PreviewLang() {
    }
}
