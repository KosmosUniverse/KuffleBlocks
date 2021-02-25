package fr.kosmosuniverse.kuffle.Commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import fr.kosmosuniverse.kuffle.KuffleMain;
import fr.kosmosuniverse.kuffle.Core.GameTask;
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
				for (GameTask gt : km.games) {
					if (gt.getPlayer().getDisplayName().equals(player.getDisplayName())) {
						if (gt.getDeathLoc() != null) {
							if (!compareLoc(player.getLocation().add(0, -1, 0), gt.getSpawnLoc())) {
								player.sendMessage("You have to stand on your spawn point to make this command work.");
							} else if (System.currentTimeMillis() - gt.getDeathTime() > (gt.getMinTime() * 1000)) {
								player.teleport(gt.getDeathLoc());
								gt.restorePlayerInv();
								gt.setDeathLoc(null);
								player.sendMessage("Here you are ! You can only reuse this command once you have died again.");	
							} else {
								player.sendMessage("You have to wait " + gt.getMinTime() + " seconds after death to tp back");	
							}
							return true;
						} else {
							player.sendMessage("You need to die to use this command.");
							return false;
						}
					}
				}
				
				player.sendMessage("You are not playing in this game.");
				return false;
			}
		}
		
		player.sendMessage("The game has not launched yet.");
		return false;
	}
	
	private boolean compareLoc(Location player, Location spawn) {
		return player.getBlockX() == spawn.getBlockX() && player.getBlockY() == spawn.getBlockY() && player.getBlockZ() == spawn.getBlockZ();
	}
}