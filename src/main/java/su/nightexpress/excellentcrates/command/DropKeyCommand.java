package su.nightexpress.excellentcrates.command;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.command.AbstractCommand;
import su.nexmedia.engine.api.command.CommandResult;
import su.nexmedia.engine.utils.CollectionsUtil;
import su.nightexpress.excellentcrates.ExcellentCratesPlugin;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.config.Perms;
import su.nightexpress.excellentcrates.key.CrateKey;

import java.util.Arrays;
import java.util.List;

public class DropKeyCommand extends AbstractCommand<ExcellentCratesPlugin> {

    public DropKeyCommand(@NotNull ExcellentCratesPlugin plugin) {
        super(plugin, new String[]{"dropkey"}, Perms.COMMAND_DROP_KEY);
        this.setDescription(plugin.getMessage(Lang.COMMAND_DROP_KEY_DESC));
        this.setUsage(plugin.getMessage(Lang.COMMAND_DROP_KEY_USAGE));
    }

    @Override
    @NotNull
    public List<String> getTab(@NotNull Player player, int arg, @NotNull String[] args) {
        if (arg == 1) {
            return plugin.getKeyManager().getKeyIds();
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

        CrateKey key = plugin.getKeyManager().getKeyById(result.getArg(1));
        if (key == null) {
            plugin.getMessage(Lang.CRATE_KEY_ERROR_INVALID).send(sender);
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

        if (!plugin.getKeyManager().spawnKey(key, location)) return;

        plugin.getMessage(Lang.COMMAND_DROP_KEY_DONE)
            .replace(key.replacePlaceholders())
            .replace(Placeholders.forLocation(location))
            .send(sender);
    }
}
