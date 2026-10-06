package su.nightexpress.excellentcrates.preview.inventory.codec;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.inventory.MenuType;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.text.layout.TextLayout;
import su.nightexpress.excellentcrates.core.SharedPlaceholders;
import su.nightexpress.excellentcrates.preview.inventory.menu.InventoryButton;
import su.nightexpress.excellentcrates.preview.inventory.menu.InventoryButtonType;
import su.nightexpress.excellentcrates.preview.inventory.menu.InventoryMenuSettings;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;
import su.nightexpress.nightcore.util.BukkitThing;
import su.nightexpress.nightcore.util.Enums;
import su.nightexpress.nightcore.util.LowerCase;

@NullMarked
public class InventoryMenuSettingsCodec implements ConfigCodec<InventoryMenuSettings> {

    public static final InventoryMenuSettingsCodec INSTANCE = new InventoryMenuSettingsCodec();

    @Override
    public InventoryMenuSettings read(FileConfig config, String path) throws CodecReadException {
        String name = config.getOrSet(path + ".name", ConfigCodecs.STRING, "Inventory");
        boolean hideUnavailable = config.getOrSet(path + ".reward.hide_unavailable", ConfigCodecs.BOOLEAN, true);
        String rewardName = config.getOrSet(path + ".reward.name", ConfigCodecs.STRING, SharedPlaceholders.REWARD_NAME);
        TextLayout rewardLore = config.getOrSet(path + ".reward.lore", TextLayout.class,
            InventoryMenuSettings.DEFAULT_REWARD_LORE_LAYOUT);
        int[] rewardSlots = config.getOrSet(path + ".reward.slots", ConfigCodecs.INT_ARRAY, new int[0]);

        String invTitle = config.getOrSet(path + ".inventory.title", ConfigCodecs.STRING, "Crate Preview");
        MenuType invType = config.getOrSet(path + ".inventory.type", ConfigCodecs.MENU_TYPE, MenuType.GENERIC_9X6);

        Map<String, InventoryButton> items = new LinkedHashMap<>();
        Map<InventoryButtonType, InventoryButton> buttons = new LinkedHashMap<>();

        config.getSection(path + ".inventory.items").forEach(buttonId -> {
            InventoryButton button = config.get(path + ".inventory.items." + buttonId, InventoryButton.class);
            if (button == null) return;

            items.put(LowerCase.internal(buttonId), button);
        });

        config.getSection(path + ".inventory.buttons").forEach(buttonId -> {
            InventoryButtonType type = Enums.get(buttonId, InventoryButtonType.class);
            if (type == null) return;

            InventoryButton button = config.get(path + ".inventory.buttons." + buttonId, InventoryButton.class);
            if (button == null) return;

            buttons.put(type, button);
        });

        return InventoryMenuSettings.builder()
            .name(name)
            .hideUnavailable(hideUnavailable)
            .rewardName(rewardName)
            .rewardLore(rewardLore)
            .rewardSlots(rewardSlots)
            .inventoryTitle(invTitle)
            .inventoryType(invType)
            .inventoryItems(items)
            .inventoryButtons(buttons)
            .build();
    }

    @Override
    public void write(FileConfig config, String path, InventoryMenuSettings value) {
        config.set(path + ".name", value.getName());

        config.set(path + ".reward.hide_unavailable", value.isHideUnavailable());
        config.set(path + ".reward.name", value.getRewardName());
        config.set(path + ".reward.lore", value.getRewardLore());
        config.setArray(path + ".reward.slots", value.getRewardSlots());

        config.set(path + ".inventory.title", value.getInventoryTitle());
        config.set(path + ".inventory.type", BukkitThing.getAsString(value.getInventoryType()));
        config.remove(path + ".inventory.items");
        config.remove(path + ".inventory.buttons");

        value.getInventoryItems().forEach((id, item) -> {
            config.set(path + ".inventory.items." + id, item);
        });

        value.getInventoryButtons().forEach((type, button) -> {
            config.set(path + ".inventory.buttons." + LowerCase.internal(type.name()), button);
        });
    }
}
