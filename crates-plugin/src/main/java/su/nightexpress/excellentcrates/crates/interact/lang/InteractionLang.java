package su.nightexpress.excellentcrates.crates.interact.lang;

import org.bukkit.Sound;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.locale.LangContainer;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.MessageLocale;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public final class InteractionLang implements LangContainer {

    public static final MessageLocale INTERACT_ACTION_NOT_FOUND = LangEntry
        .builder("crates.interaction.action.not_found")
        .chatMessage(Sound.ENTITY_VILLAGER_NO,
            "Could not find an interact action with the id: " +
                TagWrappers.RED.wrap(CommonPlaceholders.GENERIC_NAME) + "."
        );

    public static final MessageLocale INTERACT_COOLDOWN_ACTIVE = LangEntry
        .builder("crates.interaction.cooldown.active")
        .chatMessage(Sound.ENTITY_VILLAGER_NO,
            "You must wait " + TagWrappers.RED.wrap(CommonPlaceholders.GENERIC_TIME) +
                " before interacting with this crate again."
        );

    private InteractionLang() {
    }
}
