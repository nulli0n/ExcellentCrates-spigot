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
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.Perms;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.crate.impl.Crate;

import java.util.Arrays;
import java.util.List;

public class DropCommand extends AbstractCommand<ExcellentCrates> {

    public DropCommand(@NotNull ExcellentCrates plugin) {
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
        if (arg == 2) {
            return CollectionsUtil.worldNames();
        }
        if (arg == 3) {
            return Arrays.asList("<x>", NumberUtil.format(player.getLocation().getX()));
        }
        if (arg == 4) {
            return Arrays.asList("<y>", NumberUtil.format(player.getLocation().getY()));
        }
        if (arg == 5) {
            return Arrays.asList("<z>", NumberUtil.format(player.getLocation().getZ()));
        }
        return super.getTab(player, arg, args);
    }

    @Override
    protected void onExecute(@NotNull CommandSender sender, @NotNull CommandResult result) {
        if (result.length() != 6) {
            this.printUsage(sender);
            return;
        }

        Crate crate = plugin.getCrateManager().getCrateById(result.getArg(1));
        if (crate == null) {
            plugin.getMessage(Lang.CRATE_ERROR_INVALID).send(sender);
            return;
        }

        World world = plugin.getServer().getWorld(result.getArg(2));
        if (world == null) {
            plugin.getMessage(Lang.ERROR_WORLD_INVALID).send(sender);
            return;
        }

        double x = result.getDouble(3, 0);
        double y = result.getDouble(4, 0);
        double z = result.getDouble(5, 0);
        Location location = new Location(world, x, y, z);

        if (!plugin.getCrateManager().spawnCrate(crate, location)) return;

        plugin.getMessage(Lang.COMMAND_DROP_DONE)
            .replace(crate.replacePlaceholders())
            .replace(Placeholders.LOCATION.replacer(location))
            .send(sender);
    }
}
