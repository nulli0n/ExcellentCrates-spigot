package su.nightexpress.excellentcrates.crate.cost.type.impl;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.crate.cost.CostTypeId;
import su.nightexpress.excellentcrates.crate.cost.entry.impl.KeyCostEntry;
import su.nightexpress.excellentcrates.crate.cost.type.AbstractCostType;
import su.nightexpress.excellentcrates.dialog.DialogRegistry;
import su.nightexpress.excellentcrates.key.KeyManager;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.locale.LangContainer;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.IconLocale;
import su.nightexpress.nightcore.locale.entry.TextLocale;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

import static su.nightexpress.excellentcrates.Placeholders.*;
import static su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers.*;

public class KeyCostType extends AbstractCostType implements LangContainer {

    private static final TextLocale LOCALE_NAME = LangEntry.builder("Costs.Key.Name").text(YELLOW.wrap("[\uD83D\uDD11]") + " " + WHITE.wrap("Key"));

    public static final TextLocale LOCALE_FORMAT = LangEntry.builder("Costs.Key.Format").text(GRAY.wrap("x" + GENERIC_AMOUNT + " " + GENERIC_NAME));

    public static final IconLocale LOCALE_EDIT_BUTTON = LangEntry.iconBuilder("Costs.Key.EditButton")
        .rawName(YELLOW.and(BOLD).wrap("Key Cost") + GRAY.wrap(" - ") + WHITE.wrap(GENERIC_NAME))
        .rawLore(ITALIC.and(DARK_GRAY).wrap("Press " + SOFT_RED.wrap(TagWrappers.KEY.apply("key.drop")) + " key to delete.")).br()
        .appendCurrent("Key ID", GENERIC_ID)
        .appendCurrent("Amount", GENERIC_AMOUNT).br()
        .appendClick("Click to edit", YELLOW)
        .build();

    private final KeyManager keyManager;
    private final DialogRegistry dialogs;

    public KeyCostType(@NotNull CratesPlugin plugin, @NotNull KeyManager keyManager, @NotNull DialogRegistry dialogs) {
        super(CostTypeId.KEY);
        this.keyManager = keyManager;
        this.dialogs = dialogs;

        plugin.injectLang(this);
    }

    @Override
    public boolean isAvailable() {
        return this.keyManager.hasKeys();
    }

    @Override
    @NotNull
    public String getName() {
        return LOCALE_NAME.text();
    }

    @Override
    @NotNull
    public KeyCostEntry load(@NotNull FileConfig config, @NotNull String path) {
        String keyId = config.getOrSet(path + ".Key", ConfigCodecs.STRING, "null");
        int amount = config.getOrSet(path + ".Amount", ConfigCodecs.INT, 1);

        return new KeyCostEntry(this, this.keyManager, this.dialogs, keyId, amount);
    }

    @Override
    @NotNull
    public KeyCostEntry createEmpty() {
        return new KeyCostEntry(this, this.keyManager, this.dialogs, "null", 0);
    }
}
