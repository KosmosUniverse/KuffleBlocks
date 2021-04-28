package fr.kosmosuniverse.kuffleblocks.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.kosmosuniverse.kuffleblocks.KuffleMain;
import fr.kosmosuniverse.kuffleblocks.Core.GameTask;

public class KufflePlayers implements CommandExecutor {

	private KuffleMain km;

	public KufflePlayers(KuffleMain _km) {
		km = _km;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if (!(sender instanceof Player))
			return false;
		
		Player player = (Player) sender;
		
		km.logs.logMsg(player, "achieved command <kplayers>");
		
		if (!player.hasPermission("kplayers")) {
			km.logs.writeMsg(player, "You are not allowed to do this command.");
			
			return false;
		}
		
		if (km.games.size() == 0 || !km.games.get(0).getEnable()) {
			km.logs.writeMsg(player, "Game has not launched yet.");
			
			return false;
		}
		
		for (GameTask gt : km.games) {
			if (gt.getPlayer() == player) {
				player.openInventory(km.playersHeads);
			}
		}
		
		return true;
	}
}
