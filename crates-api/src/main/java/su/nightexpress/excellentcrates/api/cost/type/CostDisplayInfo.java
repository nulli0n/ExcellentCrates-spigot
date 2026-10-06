package su.nightexpress.excellentcrates.api.cost.type;

import java.util.List;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.util.bukkit.NightItem;

@NullMarked
public record CostDisplayInfo(NightItem icon, String name, List<String> lore) {

}
