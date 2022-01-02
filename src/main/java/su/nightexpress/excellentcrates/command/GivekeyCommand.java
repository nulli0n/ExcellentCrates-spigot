package su.nightexpress.excellentcrates.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.command.AbstractCommand;
import su.nexmedia.engine.utils.Constants;
import su.nexmedia.engine.utils.PlayerUtil;
import su.nexmedia.engine.utils.StringUtil;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.Perms;
import su.nightexpress.excellentcrates.api.crate.ICrateKey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GivekeyCommand extends AbstractCommand<ExcellentCrates> {

	public GivekeyCommand(@NotNull ExcellentCrates plugin) {
		super(plugin, new String[] {"givekey"}, Perms.COMMAND_GIVEKEY);
	}

	@Override
	@NotNull
	public String getUsage() {
		return plugin.lang().Command_GiveKey_Usage.getMsg();
	}

	@Override
	@NotNull
	public String getDescription() {
		return plugin.lang().Command_GiveKey_Desc.getMsg();
	}
	
	@Override
	public boolean isPlayerOnly() {
		return false;
	}

	@Override
	@NotNull
	public List<String> getTab(@NotNull Player player, int arg, @NotNull String[] args) {
		if (arg == 1) {
			List<String> list = new ArrayList<>(PlayerUtil.getPlayerNames());
			list.add(0, Constants.MASK_ANY);
			return list;
		}
		if (arg == 2) {
			return plugin.getKeyManager().getKeyIds();
		}
		if (arg == 3) {
			return Arrays.asList("1", "5", "10");
		}
		return super.getTab(player, arg, args);
	}

	@Override
	public void onExecute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
		if (args.length < 3) {
			this.printUsage(sender);
			return;
		}
		
		ICrateKey crateKey = plugin.getKeyManager().getKeyById(args[2]);
		if (crateKey == null) {
			plugin.lang().Crate_Key_Error_Invalid.send(sender);
			return;
		}
		
		int amount = args.length >= 4 ? StringUtil.getInteger(args[3], 1, false) : 1;
		
		String pName = args[1];
		if (pName.equalsIgnoreCase(Constants.MASK_ANY)) {
			for (Player player : plugin.getServer().getOnlinePlayers()) {
				plugin.getKeyManager().giveKey(player, crateKey, amount);

				plugin.lang().Command_GiveKey_Notify
					.replace("%amount%", amount)
					.replace(ICrateKey.PLACEHOLDER_NAME, crateKey.getName())
					.send(player);
			}
			pName = plugin.lang().Other_All_Online.getMsg();
		}
		else {
			if (!plugin.getKeyManager().giveKey(pName, crateKey, amount)) {
				this.errorPlayer(sender);
				return;
			}
		}
		
		plugin.lang().Command_GiveKey_Done
			.replace("%player%", pName)
			.replace("%amount%", amount)
			.replace(ICrateKey.PLACEHOLDER_NAME, crateKey.getName())
			.replace(ICrateKey.PLACEHOLDER_ID, crateKey.getId())
			.send(sender);
	}
}
