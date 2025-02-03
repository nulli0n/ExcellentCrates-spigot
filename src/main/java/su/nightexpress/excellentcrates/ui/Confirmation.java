package su.nightexpress.excellentcrates.ui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.ui.menu.MenuViewer;
import su.nightexpress.nightcore.ui.menu.item.ItemClick;

public interface Confirmation {

    void onAccept(@NotNull MenuViewer viewer, @NotNull InventoryClickEvent event);

    void onDecline(@NotNull MenuViewer viewer, @NotNull InventoryClickEvent event);

    @NotNull
    static Confirmation create(@NotNull ItemClick accept, @NotNull ItemClick decline) {
        return new Confirmation() {

            @Override
            public void onAccept(@NotNull MenuViewer viewer, @NotNull InventoryClickEvent event) {
                accept.onClick(viewer, event);
            }

            @Override
            public void onDecline(@NotNull MenuViewer viewer, @NotNull InventoryClickEvent event) {
                decline.onClick(viewer, event);
            }
        };
    }
}
