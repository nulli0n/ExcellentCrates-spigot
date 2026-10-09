package su.nightexpress.excellentcrates.keys.cost.evaluator;

import java.math.BigDecimal;
import java.util.List;

import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.settings.ReadOnlySettings;
import su.nightexpress.excellentcrates.api.cost.type.CostDisplayInfo;
import su.nightexpress.excellentcrates.api.cost.type.CostDisplayProvider;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.key.CrateKey;
import su.nightexpress.excellentcrates.api.key.data.model.KeyDisplay;
import su.nightexpress.excellentcrates.api.key.registry.KeyResolver;
import su.nightexpress.excellentcrates.keys.cost.lang.KeyCostLang;
import su.nightexpress.excellentcrates.keys.cost.settings.KeyCostSettings;
import su.nightexpress.excellentcrates.keys.item.KeyItemFactory;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext;

@NullMarked
public class KeyCostDisplayProvider implements CostDisplayProvider<KeyCostOption> {

    private final ReadOnlySettings<KeyCostSettings> settings;
    private final KeyResolver                       keyResolver;
    private final KeyItemFactory                    itemFactory;

    public KeyCostDisplayProvider(ReadOnlySettings<KeyCostSettings> settings,
                                  KeyResolver keyResolver,
                                  KeyItemFactory itemFactory) {
        this.settings = settings;
        this.keyResolver = keyResolver;
        this.itemFactory = itemFactory;
    }


    @Override
    public String formatBalance(BigDecimal balance, KeyCostOption option) {
        Identifier keyId = option.getKeyId();
        CrateKey key = this.keyResolver.resolveKey(keyId);
        if (key == null) {
            return String.valueOf(balance.intValue());
        }

        KeyDisplay display = key.getDisplay();

        PlaceholderContext placeholders = PlaceholderContext.builder()
            .with(CommonPlaceholders.GENERIC_AMOUNT, () -> NumberUtil.format(balance.intValue()))
            .with(CommonPlaceholders.GENERIC_NAME, display::getName)
            .build();

        return placeholders.apply(KeyCostLang.DISPLAY_BALANCE_FORMAT.text());
    }


    @Override
    public CostDisplayInfo getCategoryDisplay(Crate crate) {
        NightItem icon = this.settings.get().categoryIcon();
        String name = this.settings.get().categoryName();
        List<String> description = this.settings.get().categoryDescription();

        return new CostDisplayInfo(icon, name, description);
    }

    @Override
    public CostDisplayInfo getOptionDisplay(Crate crate, KeyCostOption option) {
        Identifier keyId = option.getKeyId();
        CrateKey key = this.keyResolver.resolveKey(keyId);
        if (key == null) {
            return new CostDisplayInfo(NightItem.fromType(Material.BARRIER), keyId.value(), List.of());
        }

        NightItem icon = this.itemFactory.createDisplayIcon(key);
        KeyDisplay display = key.getDisplay();

        return new CostDisplayInfo(icon, display.getName(), display.getLore());
    }
}
