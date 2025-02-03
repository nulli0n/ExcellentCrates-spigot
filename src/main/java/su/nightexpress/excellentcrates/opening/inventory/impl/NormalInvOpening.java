package su.nightexpress.excellentcrates.opening.inventory.impl;

import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.crate.impl.CrateSource;
import su.nightexpress.excellentcrates.key.CrateKey;
import su.nightexpress.excellentcrates.opening.inventory.InvOpeningProvider;
import su.nightexpress.excellentcrates.opening.inventory.InventoryOpening;

public class NormalInvOpening extends InventoryOpening {

    public NormalInvOpening(@NotNull CratesPlugin plugin,
                            @NotNull InvOpeningProvider config,
                            @NotNull InventoryView view,
                            @NotNull Player player,
                            @NotNull CrateSource source,
                            @Nullable CrateKey key) {
        super(plugin, config, view, player, source, key);
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.launch();
    }

    @Override
    protected void onInstaRoll() {

    }
}
