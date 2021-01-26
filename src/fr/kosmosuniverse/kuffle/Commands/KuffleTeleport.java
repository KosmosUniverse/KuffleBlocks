package fr.kosmosuniverse.kuffle.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.kosmosuniverse.kuffle.KuffleMain;
import fr.kosmosuniverse.kuffle.Core.GameTask;

public class KuffleTeleport implements CommandExecutor {
	private KuffleMain km;

	public KuffleTeleport(KuffleMain _km) {
		km = _km;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if (!(sender instanceof Player))
			return false;
		
		Player player = (Player) sender;
		
		if (!player.hasPermission("kteleport")) {
			player.sendMessage("You are not allowed to do this command.");
			return false;
		}
		
		if (km.games.size() == 0 || !km.games.get(0).getEnable()) {
			player.sendMessage("The game has not launched yet.");
			return false;
		}
		
		for (GameTask gt : km.games) {
			if (gt.getPlayer() == player) {
				if (gt.getExit()) {
					player.openInventory(km.playersHeads);
				} else {
					player.sendMessage("You can not use this command until yout finished the game !");
				}
			}
		}
		
		return true;
	}
}