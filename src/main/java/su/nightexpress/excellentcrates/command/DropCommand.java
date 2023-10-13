package su.nightexpress.excellentcrates.command;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.command.AbstractCommand;
import su.nexmedia.engine.api.command.CommandResult;
import su.nexmedia.engine.utils.CollectionsUtil;
import su.nexmedia.engine.utils.NumberUtil;
import su.nightexpress.excellentcrates.ExcellentCratesPlugin;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.config.Perms;
import su.nightexpress.excellentcrates.crate.impl.Crate;

import java.util.Arrays;
import java.util.List;

public class DropCommand extends AbstractCommand<ExcellentCratesPlugin> {

    public DropCommand(@NotNull ExcellentCratesPlugin plugin) {
        super(plugin, new String[]{"drop"}, Perms.COMMAND_DROP);
        this.setDescription(plugin.getMessage(Lang.COMMAND_DROP_DESC));
        this.setUsage(plugin.getMessage(Lang.COMMAND_DROP_USAGE));
    }

    @Override
    @NotNull
    public List<String> getTab(@NotNull Player player, int arg, @NotNull String[] args) {
        if (arg == 1) {
            return plugin.getCrateManager().getCrateIds(false);
        }
        if (arg >= 2 && arg <= 4) {
            Location location = player.getLocation();

            if (arg == 2) {
                return Arrays.asList(
                    String.valueOf(location.getBlockX()),
                    location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ()
                );
            }
            else if (arg == 3) {
                return Arrays.asList(
                    String.valueOf(location.getBlockY()),
                    location.getBlockY() + " " + location.getBlockZ()
                );
            }
            else {
                return Arrays.asList(String.valueOf(location.getBlockZ()));
            }
        }
        if (arg == 5) {
            return CollectionsUtil.worldNames();
        }
        return super.getTab(player, arg, args);
    }

    @Override
    protected void onExecute(@NotNull CommandSender sender, @NotNull CommandResult result) {
        if (result.length() < 5) {
            this.printUsage(sender);
            return;
        }

        Crate crate = plugin.getCrateManager().getCrateById(result.getArg(1));
        if (crate == null) {
            plugin.getMessage(Lang.CRATE_ERROR_INVALID).send(sender);
            return;
        }

        double x = result.getDouble(2, 0);
        double y = result.getDouble(3, 0);
        double z = result.getDouble(4, 0);

        World world;
        if (result.length() < 6) {
            if (!(sender instanceof Player player)) {
                this.printUsage(sender);
                return;
            }
            else world = player.getWorld();
        }
        else {
            world = plugin.getServer().getWorld(result.getArg(5));
            if (world == null) {
                this.plugin.getMessage(Lang.ERROR_WORLD_INVALID).send(sender);
                return;
            }
        }
        Location location = new Location(world, x, y, z);

        if (!plugin.getCrateManager().spawnCrate(crate, location)) return;

        plugin.getMessage(Lang.COMMAND_DROP_DONE)
            .replace(crate.replacePlaceholders())
            .replace(Placeholders.forLocation(location))
            .send(sender);
    }
}
