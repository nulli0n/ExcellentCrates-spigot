package su.nightexpress.excellentcrates.command;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.command.AbstractCommand;
import su.nexmedia.engine.core.config.CoreConfig;
import su.nexmedia.engine.utils.LocationUtil;
import su.nexmedia.engine.utils.NumberUtil;
import su.nexmedia.engine.utils.StringUtil;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.Perms;
import su.nightexpress.excellentcrates.api.crate.ICrate;

import java.util.Arrays;
import java.util.List;

public class DropCommand extends AbstractCommand<ExcellentCrates> {

    public DropCommand(@NotNull ExcellentCrates plugin) {
        super(plugin, new String[]{"drop"}, Perms.COMMAND_DROP);
    }

    @Override
    @NotNull
    public String getUsage() {
        return plugin.lang().Command_Drop_Usage.getLocalized();
    }

    @Override
    @NotNull
    public String getDescription() {
        return plugin.lang().Command_Drop_Desc.getLocalized();
    }

    @Override
    public boolean isPlayerOnly() {
        return false;
    }

    @Override
    @NotNull
    public List<String> getTab(@NotNull Player player, int arg, @NotNull String[] args) {
        if (arg == 1) {
            return plugin.getCrateManager().getCrateIds(false);
        }
        if (arg == 2) {
            return LocationUtil.getWorldNames();
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
    protected void onExecute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length != 6) {
            this.printUsage(sender);
            return;
        }

        ICrate crate = plugin.getCrateManager().getCrateById(args[1]);
        if (crate == null) {
            plugin.lang().Crate_Error_Invalid.replace(ICrate.PLACEHOLDER_ID, args[1]).send(sender);
            return;
        }

        World world = plugin.getServer().getWorld(args[2]);
        if (world == null) {
            plugin.lang().Error_World_Invalid.replace("%world%", args[2]).send(sender);
            return;
        }

        double x = StringUtil.getDouble(args[3], 0, true);
        double y = StringUtil.getDouble(args[4], 0, true);
        double z = StringUtil.getDouble(args[5], 0, true);
        Location location = new Location(world, x, y, z);

        if (!plugin.getCrateManager().spawnCrate(crate, location)) return;

        plugin.lang().Command_Drop_Done
            .replace(ICrate.PLACEHOLDER_ID, crate.getId())
            .replace(ICrate.PLACEHOLDER_NAME, crate.getName())
            .replace("%x%", NumberUtil.format(x))
            .replace("%y%", NumberUtil.format(y))
            .replace("%z%", NumberUtil.format(z))
            .replace("%world%", CoreConfig.getWorldName(world.getName()))
            .send(sender);
    }
}
