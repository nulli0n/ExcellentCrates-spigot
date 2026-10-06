package su.nightexpress.excellentcrates.crates.item.editor.ui.menu.context;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorHook;

@NullMarked
public record CrateItemSetupContext(CrateEditorHook hook,
                                    ItemStack itemStack,
                                    boolean useItemRef,
                                    boolean setDisplayName,
                                    boolean setDisplayLore) {

}
