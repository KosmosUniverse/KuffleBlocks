package fr.kosmosuniverse.kuffle.Commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import fr.kosmosuniverse.kuffle.KuffleMain;

public class KuffleBack implements CommandExecutor {
	private KuffleMain km;
	
	public KuffleBack(KuffleMain _km) {
		km = _km;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if (!(sender instanceof Player))
			return false;
		
		Player player = (Player) sender;
		
		if (km.games.size() != 0) {
			if (km.games.get(0).getEnable()) {
				if (km.backCmd.containsKey(player.getDisplayName())) {
					Location loc;
					
					if ((loc = km.backCmd.get(player.getDisplayName())) != null) {
						player.teleport(loc);
						km.backCmd.put(player.getDisplayName(), null);
						player.sendMessage("Here you are ! You can only reuse this command once you have died again.");
						return true;
					} else {
						player.sendMessage("You need to die to use this command.");
						return false;
					}
				} else {
					player.sendMessage("You are not playing in this game.");
					return false;
				}
			}
		}
		
		player.sendMessage("The game has not launched yet.");
		return false;
	}
}
