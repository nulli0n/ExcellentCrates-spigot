package su.nightexpress.excellentcrates.command.basic;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.config.Perms;
import su.nightexpress.excellentcrates.key.CrateKey;
import su.nightexpress.nightcore.command.CommandResult;
import su.nightexpress.nightcore.command.impl.AbstractCommand;
import su.nightexpress.nightcore.util.Lists;

import java.util.List;

public class DropKeyCommand extends AbstractCommand<CratesPlugin> {

    public DropKeyCommand(@NotNull CratesPlugin plugin) {
        super(plugin, new String[]{"dropkey"}, Perms.COMMAND_DROP_KEY);
        this.setDescription(Lang.COMMAND_DROP_KEY_DESC);
        this.setUsage(Lang.COMMAND_DROP_KEY_USAGE);
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
                return List.of(
                    String.valueOf(location.getBlockX()),
                    location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ()
                );
            }
            else if (arg == 3) {
                return List.of(
                    String.valueOf(location.getBlockY()),
                    location.getBlockY() + " " + location.getBlockZ()
                );
            }
            else {
                return List.of(String.valueOf(location.getBlockZ()));
            }
        }
        if (arg == 5) {
            return Lists.worldNames();
        }
        return super.getTab(player, arg, args);
    }

    @Override
    protected void onExecute(@NotNull CommandSender sender, @NotNull CommandResult result) {
        if (result.length() < 5) {
            this.errorUsage(sender);
            return;
        }

        CrateKey key = plugin.getKeyManager().getKeyById(result.getArg(1));
        if (key == null) {
            Lang.ERROR_INVALID_KEY.getMessage().send(sender);
            return;
        }

        double x = result.getDouble(2, 0);
        double y = result.getDouble(3, 0);
        double z = result.getDouble(4, 0);

        World world;
        if (result.length() < 6) {
            if (!(sender instanceof Player player)) {
                this.errorUsage(sender);
                return;
            }
            else world = player.getWorld();
        }
        else {
            world = plugin.getServer().getWorld(result.getArg(5));
            if (world == null) {
                Lang.ERROR_INVALID_WORLD.getMessage(plugin).send(sender);
                return;
            }
        }
        Location location = new Location(world, x, y, z);

        if (!plugin.getKeyManager().spawnKey(key, location)) return;

        Lang.COMMAND_DROP_KEY_DONE.getMessage()
            .replace(key.replacePlaceholders())
            .replace(Placeholders.forLocation(location))
            .send(sender);
    }
}
