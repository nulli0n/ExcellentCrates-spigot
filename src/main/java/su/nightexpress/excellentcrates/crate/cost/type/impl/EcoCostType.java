package su.nightexpress.excellentcrates.crate.cost.type.impl;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.api.cost.CostEntry;
import su.nightexpress.excellentcrates.crate.cost.CostTypeId;
import su.nightexpress.excellentcrates.crate.cost.entry.impl.EcoCostEntry;
import su.nightexpress.excellentcrates.crate.cost.type.AbstractCostType;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.integration.currency.CurrencyId;
import su.nightexpress.nightcore.integration.currency.EconomyBridge;
import su.nightexpress.nightcore.locale.LangContainer;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.IconLocale;
import su.nightexpress.nightcore.locale.entry.TextLocale;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

import static su.nightexpress.excellentcrates.Placeholders.*;
import static su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers.*;
import static su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers.BOLD;
import static su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers.GRAY;

public class EcoCostType extends AbstractCostType implements LangContainer {

    public static final TextLocale LOCALE_NAME = LangEntry.builder("Costs.Currency.Name").text(GREEN.wrap("[$]") + " " + WHITE.wrap("Currency"));

    public static final IconLocale LOCALE_EDIT_BUTTON = LangEntry.iconBuilder("Costs.Currency.EditButton")
        .rawName(YELLOW.and(BOLD).wrap("Currency Cost") + GRAY.wrap(" - ") + WHITE.wrap(GENERIC_NAME))
        .rawLore(ITALIC.and(DARK_GRAY).wrap("Press " + SOFT_RED.wrap(TagWrappers.KEY.apply("key.drop")) + " key to delete.")).br()
        .appendCurrent("Currency ID", GENERIC_ID)
        .appendCurrent("Amount", GENERIC_AMOUNT).br()
        .appendClick("Click to edit")
        .build();

    public EcoCostType(@NotNull CratesPlugin plugin) {
        super(CostTypeId.CURRENCY);
    }

    @Override
    public boolean isAvailable() {
        return EconomyBridge.hasCurrency();
    }

    @Override
    @NotNull
    public String getName() {
        return LOCALE_NAME.text();
    }

    @Override
    @NotNull
    public CostEntry load(@NotNull FileConfig config, @NotNull String path) {
        String currencyId = ConfigValue.create(path + ".Currency", CurrencyId.VAULT).read(config);
        double amount = ConfigValue.create(path + ".Amount", 0D).read(config);

        return new EcoCostEntry(this, currencyId, amount);
    }

    @Override
    @NotNull
    public EcoCostEntry createEmpty() {
        return new EcoCostEntry(this, CurrencyId.VAULT, 0);
    }
}
