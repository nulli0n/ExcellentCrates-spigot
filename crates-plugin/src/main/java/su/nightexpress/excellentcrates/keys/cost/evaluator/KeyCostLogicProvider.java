package su.nightexpress.excellentcrates.keys.cost.evaluator;

import java.math.BigDecimal;
import java.util.List;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.id.IdentifierParser;
import su.nightexpress.excellentcrates.api.cost.type.CostLogicProvider;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.component.CrateComponentKeys;
import su.nightexpress.excellentcrates.api.key.CrateKey;
import su.nightexpress.excellentcrates.api.key.crate.KeyRequirementEntry;
import su.nightexpress.excellentcrates.api.key.registry.KeyResolver;
import su.nightexpress.excellentcrates.api.key.crate.KeyRequirementComponent;
import su.nightexpress.excellentcrates.keys.balance.KeyBalanceService;

@NullMarked
public class KeyCostLogicProvider implements CostLogicProvider<KeyCostOption> {

    private final KeyResolver       keyResolver;
    private final KeyBalanceService balanceService;

    public KeyCostLogicProvider(KeyResolver keyResolver, KeyBalanceService balanceService) {
        this.keyResolver = keyResolver;
        this.balanceService = balanceService;
    }

    private @Nullable KeyRequirementEntry getKeyEntry(Crate crate, Identifier keyId) {
        KeyRequirementComponent component = crate.getComponentOrNull(CrateComponentKeys.KEY_REQUIREMENT);
        if (component == null || !component.isEnabled()) return null;

        return component.getKeyEntry(keyId);
    }

    @Override
    public BigDecimal getBalance(Player player, Crate crate, KeyCostOption option) {
        CrateKey key = this.keyResolver.resolveKey(option.getKeyId());
        if (key == null) return BigDecimal.ZERO;

        return BigDecimal.valueOf(this.balanceService.countKeys(player, key));
    }

    @Override
    public BigDecimal getCost(Crate crate, KeyCostOption option, int amount) {
        CrateKey key = this.keyResolver.resolveKey(option.getKeyId());
        if (key == null) return BigDecimal.ZERO;

        KeyRequirementEntry entry = this.getKeyEntry(crate, option.getKeyId());
        if (entry == null) return BigDecimal.ZERO;

        return BigDecimal.valueOf(entry.getAmount() * amount);
    }

    @Override
    public int getMaxAffordableOpens(Player player, Crate crate, KeyCostOption option) {
        KeyRequirementEntry entry = this.getKeyEntry(crate, option.getKeyId());
        if (entry == null) return 0;

        CrateKey key = this.keyResolver.resolveKey(option.getKeyId());
        if (key == null) return 0;

        return this.balanceService.countKeys(player, key) / entry.getAmount();
    }

    @Override
    public boolean canAfford(Player player, Crate crate, KeyCostOption option, int amount) {
        if (this.getCost(crate, option, amount) == BigDecimal.ZERO) return true;

        CrateKey key = this.keyResolver.resolveKey(option.getKeyId());
        if (key == null) return false;

        KeyRequirementEntry entry = this.getKeyEntry(crate, option.getKeyId());
        return entry != null && this.balanceService.hasKey(player, key, entry.getAmount() * amount);
    }

    @Override
    public @Nullable KeyCostOption getOptionById(Crate crate, String identifier) {
        Identifier keyId = IdentifierParser.parse(identifier).orElse(null);
        if (keyId == null) return null;

        return new KeyCostOption(keyId);
    }

    @Override
    public List<KeyCostOption> getOptions(Crate crate) {
        KeyRequirementComponent component = crate.getComponentOrNull(CrateComponentKeys.KEY_REQUIREMENT);
        if (component == null || !component.isEnabled()) return List.of();

        return component.getKeyEntryMap().entrySet()
            .stream()
            .filter(entry -> this.isValidEntry(entry.getKey(), entry.getValue()))
            .map(entry -> new KeyCostOption(entry.getKey()))
            .toList();
    }

    private boolean isValidEntry(Identifier keyId, KeyRequirementEntry entry) {
        return this.keyResolver.resolveKey(keyId) != null && entry.getAmount() > 0;
    }

    @Override
    public boolean hasCost(Crate crate) {
        KeyRequirementComponent component = crate.getComponentOrNull(CrateComponentKeys.KEY_REQUIREMENT);
        if (component == null || !component.isEnabled() || !component.hasKeyEntries()) return false;

        return component.getKeyEntryMap().entrySet().stream().anyMatch(entry -> {
            KeyRequirementEntry keyEntry = entry.getValue();
            Identifier keyId = entry.getKey();
            return this.isValidEntry(keyId, keyEntry);
        });
    }

    @Override
    public void refund(Player player, Crate crate, KeyCostOption option, int amount) {
        Identifier keyId = option.getKeyId();
        KeyRequirementEntry keyEntry = this.getKeyEntry(crate, keyId);
        if (keyEntry == null) return;

        CrateKey key = this.keyResolver.resolveKey(keyId);
        if (key == null) return;

        this.balanceService.addKey(player, key, keyEntry.getAmount() * amount);
    }

    @Override
    public void take(Player player, Crate crate, KeyCostOption option, int amount) {
        Identifier keyId = option.getKeyId();
        KeyRequirementEntry keyEntry = this.getKeyEntry(crate, keyId);
        if (keyEntry == null) return;

        CrateKey key = this.keyResolver.resolveKey(keyId);
        if (key == null) return;

        this.balanceService.removeKey(player, key, keyEntry.getAmount() * amount);
    }
}
