package fr.kosmosuniverse.kuffle.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.kosmosuniverse.kuffle.KuffleMain;
import fr.kosmosuniverse.kuffle.Core.GameTask;

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
		
		if (!player.hasPermission("kplayers")) {
			player.sendMessage("You are not allowed to do this command.");
			return false;
		}
		
		if (km.games.size() == 0 || !km.games.get(0).getEnable()) {
			player.sendMessage("The game has not launched yet.");
			
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
