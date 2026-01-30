package su.nightexpress.excellentcrates.crate.cost.entry.impl;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.crate.cost.entry.AbstractCostEntry;
import su.nightexpress.excellentcrates.crate.cost.type.impl.KeyCostType;
import su.nightexpress.excellentcrates.dialog.DialogKey;
import su.nightexpress.excellentcrates.dialog.DialogRegistry;
import su.nightexpress.excellentcrates.dialog.cost.KeyCostOptionsDialog;
import su.nightexpress.excellentcrates.key.CrateKey;
import su.nightexpress.excellentcrates.key.KeyManager;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.util.LowerCase;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.placeholder.Replacer;

import java.util.Optional;

public class KeyCostEntry extends AbstractCostEntry<KeyCostType> {

    private static final DialogKey<KeyCostEntry> DIALOG_KEY = new DialogKey<>("key_cost_options");

    private final KeyManager keyManager;
    private final DialogRegistry dialogs;

    private String keyId;
    private int amount;

    public KeyCostEntry(@NotNull KeyCostType type, @NotNull KeyManager keyManager, @NotNull DialogRegistry dialogs, @NotNull String keyId, int amount) {
        super(type);
        this.keyManager = keyManager;
        this.dialogs = dialogs;
        this.setKeyId(keyId);
        this.setAmount(amount);

        this.dialogs.register(DIALOG_KEY, new KeyCostOptionsDialog(keyManager));
    }

    @Override
    protected void writeAdditional(@NotNull FileConfig config, @NotNull String path) {
        config.set(path + ".Key", this.keyId);
        config.set(path + ".Amount", this.amount);
    }

    @Override
    public void openEditor(@NotNull Player player, @Nullable Runnable callback) {
        this.dialogs.show(player, DIALOG_KEY, this, callback);
    }

    @NotNull
    public Optional<CrateKey> key() {
        return Optional.ofNullable(this.keyManager.getKeyById(this.keyId));
    }

    public int countKeys(@NotNull Player player) {
        return this.key().map(key -> this.keyManager.getKeysAmount(player, key)).orElse(0);
    }

    @Override
    @NotNull
    public NightItem getEditorIcon() {
        Optional<CrateKey> keyOpt = this.key();

        return keyOpt.map(key -> NightItem.fromItemStack(key.getRawItem())).orElse(NightItem.fromType(Material.BARRIER))
            .localized(KeyCostType.LOCALE_EDIT_BUTTON)
            .replacement(replacer -> replacer
                .replace(Placeholders.GENERIC_ID, () -> keyOpt.map(key -> CoreLang.goodEntry(key.getId())).orElse(CoreLang.badEntry(this.keyId)))
                .replace(Placeholders.GENERIC_AMOUNT, () -> this.amount > 0 ? CoreLang.goodEntry(String.valueOf(this.amount)) : CoreLang.badEntry(String.valueOf(this.amount)))
                .replace(Placeholders.GENERIC_NAME, () -> keyOpt.map(CrateKey::getName).orElse(this.keyId))
            )
            .hideAllComponents();
    }

    @Override
    public boolean isValid() {
        return this.amount > 0 && this.key().isPresent();
    }

    @Override
    @NotNull
    public String format() {
        return Replacer.create()
            .replace(Placeholders.GENERIC_AMOUNT, () -> String.valueOf(this.amount))
            .replace(Placeholders.GENERIC_NAME,  () -> this.key().map(CrateKey::getName).orElse(this.keyId))
            .apply(KeyCostType.LOCALE_FORMAT.text());
    }

    @Override
    public int countPossibleOpenings(@NotNull Player player) {
        return this.countKeys(player) / this.amount;
    }

    @Override
    public boolean hasEnough(@NotNull Player player) {
        return this.countKeys(player) >= this.amount;
    }

    @Override
    public void take(@NotNull Player player) {
        this.key().ifPresent(key -> this.keyManager.takeKey(player, key, this.amount));
    }

    @Override
    public void refund(@NotNull Player player) {
        this.key().ifPresent(key -> this.keyManager.giveKey(player, key, this.amount));
    }

    @NotNull
    public String getKeyId() {
        return this.keyId;
    }

    public void setKeyId(@NotNull String keyId) {
        this.keyId = LowerCase.INTERNAL.apply(keyId);
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int amount) {
        this.amount = Math.max(1, amount);
    }

    @Override
    public String toString() {
        return "[" + "keyId='" + keyId + '\'' + ", amount=" + amount + ']';
    }
}
