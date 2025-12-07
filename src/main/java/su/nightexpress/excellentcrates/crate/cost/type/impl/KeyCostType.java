package su.nightexpress.excellentcrates.crate.cost.type.impl;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.api.cost.CostEntry;
import su.nightexpress.excellentcrates.crate.cost.CostTypeId;
import su.nightexpress.excellentcrates.crate.cost.entry.impl.KeyCostEntry;
import su.nightexpress.excellentcrates.crate.cost.type.AbstractCostType;
import su.nightexpress.excellentcrates.key.KeyManager;
import su.nightexpress.nightcore.config.ConfigValue;
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

    public KeyCostType(@NotNull CratesPlugin plugin, @NotNull KeyManager keyManager) {
        super(CostTypeId.KEY);
        plugin.injectLang(this);
        this.keyManager = keyManager;
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
        String keyId = ConfigValue.create(path + ".Key", "null").read(config);
        int amount = ConfigValue.create(path + ".Amount", 1).read(config);

        return new KeyCostEntry(this, this.keyManager, keyId, amount);
    }

    @Override
    @NotNull
    public KeyCostEntry createEmpty() {
        return new KeyCostEntry(this, this.keyManager, "null", 0);
    }
}
