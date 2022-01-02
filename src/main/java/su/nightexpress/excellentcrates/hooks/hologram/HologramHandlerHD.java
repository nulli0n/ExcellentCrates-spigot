package su.nightexpress.excellentcrates.hooks.hologram;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.hook.AbstractHook;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.api.crate.ICrate;
import su.nightexpress.excellentcrates.api.crate.ICrateReward;
import su.nightexpress.excellentcrates.api.hook.HologramHandler;

import java.util.*;

public class HologramHandlerHD extends AbstractHook<ExcellentCrates> implements HologramHandler {

	private Map<String, Set<Hologram>> holoCrates;
	private Map<Player, Hologram> holoRewards;
	
	public HologramHandlerHD(@NotNull ExcellentCrates plugin, @NotNull String pluginName) {
		super(plugin, pluginName);
	}

	@Override
	public boolean setup() {
		this.holoCrates = new HashMap<>();
		this.holoRewards = new WeakHashMap<>();
		return true;
	}

	@Override
	public void shutdown() {
		if (this.holoCrates != null) {
			this.holoCrates.values().forEach(set -> set.forEach(Hologram::delete));
			this.holoCrates.clear();
			this.holoCrates = null;
		}
		if (this.holoRewards != null) {
			this.holoRewards.values().forEach(Hologram::delete);
			this.holoRewards.clear();
			this.holoRewards = null;
		}
	}

	@Override
	public void create(@NotNull ICrate crate) {
		String id = crate.getId();

		crate.getBlockLocations().forEach(loc -> {
			Hologram hologram = HologramsAPI.createHologram(plugin, crate.getBlockHologramLocation(loc));
			for (String line : crate.getBlockHologramText()) {
				hologram.appendTextLine(line);
			}
			this.holoCrates.computeIfAbsent(id, set -> new HashSet<>()).add(hologram);
		});
	}
	
	@Override
	public void remove(@NotNull ICrate crate) {
		Set<Hologram> set = this.holoCrates.remove(crate.getId());
		if (set == null) return;
		
		set.forEach(Hologram::delete);
	}

	@Override
	public void createReward(@NotNull Player player, @NotNull ICrateReward reward, @NotNull Location location) {
		this.removeReward(player);

		ICrate crate = reward.getCrate();
		Hologram hologram = HologramsAPI.createHologram(plugin, location);
		hologram.appendTextLine(reward.getName());
		hologram.appendItemLine(reward.getPreview());
		hologram.getVisibilityManager().setVisibleByDefault(false);
		hologram.getVisibilityManager().showTo(player);

		this.holoRewards.put(player, hologram);
	}

	@Override
	public void removeReward(@NotNull Player player) {
		Hologram hologram = this.holoRewards.remove(player);
		if (hologram != null) hologram.delete();
	}
}
