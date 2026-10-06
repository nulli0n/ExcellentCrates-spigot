package su.nightexpress.excellentcrates.keys.item.editor.ui.context;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record KeyItemSetupContext(ItemStack itemStack,
                                  boolean useItemRef,
                                  boolean setDisplayName,
                                  boolean setDisplayLore) {

}