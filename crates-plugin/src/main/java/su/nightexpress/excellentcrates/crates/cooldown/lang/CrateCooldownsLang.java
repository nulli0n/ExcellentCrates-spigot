package su.nightexpress.excellentcrates.crates.cooldown.lang;

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
public final class CrateCooldownsLang implements LangContainer {

    public static final MessageLocale QUOTA_COOLDOWN = LangEntry
        .builder("crates.cooldown.quota.cooldown")
        .chatMessage(Sound.ENTITY_VILLAGER_NO,
            "The " + TagWrappers.WHITE.wrap(SharedPlaceholders.CRATE_NAME) + " is on cooldown: " +
                TagWrappers.RED.wrap(CommonPlaceholders.GENERIC_TIME)
        );

    public static final MessageLocale EDITOR_NO_COMPONENT = LangEntry
        .builder("crates.cooldowns.editor.no_component")
        .chatMessage("Crate " + TagWrappers.WHITE.wrap(SharedPlaceholders.CRATE_NAME) + " has no cooldown component.");

    public static final IconLocale UI_EXTENSION_BUTTON = LangEntry.iconBuilder(
        "crates.cooldowns.ui.extension.button.cooldowns")
        .accentColor(TagWrappers.GRADIENT.with("#ffe259", "#ffa751"))
        .name("Cooldowns")
        .appendInfo(
            "Configure " + TagWrappers.GOLD.wrap("global") + " and " +
                TagWrappers.GOLD.wrap("personal") + " cooldowns.",
            "Requiring players to wait before",
            "they can open the crate again."
        )
        .br()
        .appendClick("Click to navigate")
        .build();

    public static final TextLocale EDITOR_UI_INVENTORY_COOLDOWNS_TITLE = LangEntry
        .builder("crates.cooldowns.editor.ui.inventory.cooldowns.title")
        .text("Crate Editor • Cooldowns");

    public static final IconLocale EDITOR_UI_INVENTORY_COOLDOWNS_GLOBAL_BUTTON = LangEntry
        .iconBuilder("crates.cooldowns.editor.ui.inventory.cooldowns.global.button")
        .accentColor(TagWrappers.AQUA)
        .name("Global Cooldown")
        .appendCurrent("State", SharedPlaceholders.STATE)
        .br()
        .appendInfo("Global cooldown applies to", "all players.")
        .br()
        .appendClick("Click to edit")
        .build();

    public static final IconLocale EDITOR_UI_INVENTORY_COOLDOWNS_PLAYER_BUTTON = LangEntry
        .iconBuilder("crates.cooldowns.editor.ui.inventory.cooldowns.player.button")
        .accentColor(TagWrappers.ORANGE)
        .name("Player Cooldown")
        .appendCurrent("State", SharedPlaceholders.STATE)
        .br()
        .appendInfo("Per-player cooldown applies to", "individual players.")
        .br()
        .appendClick("Click to edit")
        .build();

    public static final TextLocale EDITOR_UI_DIALOG_COOLDOWN_SETTINGS_TITLE = LangEntry
        .builder("crates.cooldowns.editor.ui.dialog.cooldown.settings.title")
        .text("Cooldown Settings");

    public static final TextLocale EDITOR_UI_DIALOG_COOLDOWN_SETTINGS_BODY_MAIN = LangEntry
        .builder("crates.cooldowns.editor.ui.dialog.cooldown.settings.body.main")
        .text(
            "Cooldown settings."
        );

    public static final TextLocale EDITOR_UI_DIALOG_COOLDOWN_SETTINGS_BODY_GLOBAL = LangEntry
        .builder("crates.cooldowns.editor.ui.dialog.cooldown.settings.body.global")
        .text(
            TagWrappers.AQUA.and(TagWrappers.BOLD).wrap("Global Cooldown"),
            "Applies to " + TagWrappers.AQUA.wrap("all players") + ".",
            "If a player opens the crate, all players must wait for the cooldown to expire before they can open the crate again."
        );

    public static final TextLocale EDITOR_UI_DIALOG_COOLDOWN_SETTINGS_BODY_PLAYER = LangEntry
        .builder("crates.cooldowns.editor.ui.dialog.cooldown.settings.body.player")
        .text(
            TagWrappers.ORANGE.and(TagWrappers.BOLD).wrap("Player Cooldown"),
            "Applies to " + TagWrappers.ORANGE.wrap("individual players") + ".",
            "If a player opens the crate, they must wait for the cooldown to expire before they can open the crate again.",
            TagWrappers.GRAY.wrap("Other players are unaffected.")
        );

    private CrateCooldownsLang() {
    }
}
