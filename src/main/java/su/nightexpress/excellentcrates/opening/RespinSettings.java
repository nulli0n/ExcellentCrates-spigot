package su.nightexpress.excellentcrates.opening;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.nightcore.util.bukkit.NightItem;

import java.util.ArrayList;
import java.util.List;

public class RespinSettings {

    public static String getTitle(boolean isReroll) {
        return color(isReroll ? Config.RESPIN_TITLE_REROLL.get() : Config.RESPIN_TITLE_REBUY.get());
    }

    public static ItemStack getItem(String type, String costPlaceholder) {
        NightItem nightItem;

        // Map the type string to the ConfigValue field
        switch (type) {
            case "Filler":
                nightItem = Config.RESPIN_ITEM_FILLER.get();
                break;
            case "Keep":
                nightItem = Config.RESPIN_ITEM_KEEP.get();
                break;
            case "Close":
                nightItem = Config.RESPIN_ITEM_CLOSE.get();
                break;
            case "Reroll":
                nightItem = Config.RESPIN_ITEM_ACTION_REROLL.get();
                break;
            case "Rebuy":
                nightItem = Config.RESPIN_ITEM_ACTION_REBUY.get();
                break;
            default:
                return new ItemStack(Material.STONE);
        }

        // Convert NightItem to standard Bukkit ItemStack
        ItemStack item = nightItem.getItemStack(); // or .getItem() depending on API
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            // Replace placeholder in Lore
            if (meta.hasLore() && costPlaceholder != null) {
                List<String> lore = meta.getLore();
                List<String> newLore = new ArrayList<>();
                for (String line : lore) {
                    newLore.add(line.replace("%cost%", costPlaceholder));
                }
                meta.setLore(newLore);
            }
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static int getSlot(String type, int def) {
        if (type.equals("Close")) return Config.RESPIN_SLOT_CLOSE.get();
        if (type.equals("Action")) return Config.RESPIN_SLOT_ACTION.get();
        return def;
    }

    private static String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}