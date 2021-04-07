package fr.kosmosuniverse.kuffle.Commands;

import org.bukkit.Location;
import org.bukkit.Material;
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
		
		km.logs.logMsg(player, "achieved command <kback>");
		
		if (km.games.size() != 0) {
			if (km.games.get(0).getEnable()) {
				for (GameTask gt : km.games) {
					if (gt.getPlayer().getDisplayName().equals(player.getDisplayName())) {
						if (gt.getDeathLoc() != null) {
							if (!compareLoc(player.getLocation().add(0, -1, 0), gt.getSpawnLoc())) {
								km.logs.writeMsg(player, "You have to stand on your spawn point to make this command work.");
							} else if (System.currentTimeMillis() - gt.getDeathTime() > (gt.getMinTime() * 1000)) {
								Location loc = gt.getDeathLoc();
								
								if (loc.getWorld().getName().contains("the_end") && loc.getY() < 0) {
									int tmp = loc.getWorld().getHighestBlockYAt(loc);
									if (tmp == -1) {
										loc.setY(59);
										
										for (double cntX = -2; cntX <= 2; cntX++) {
											for (double cntZ = -2; cntZ <= 2; cntZ++) {
												Location platform = loc.clone();
												
												platform.add(cntX, 0, cntZ);
												
												platform.getBlock().setType(Material.COBBLESTONE);
											}
										}
										
										loc.setY(61);
									} else {
										loc.setY(loc.getWorld().getHighestBlockYAt(loc) + 1);
									}
								}
								
								player.teleport(loc);
								gt.restorePlayerInv();
								gt.setDeathLoc(null);
								km.logs.writeMsg(player, "Here you are ! You can only reuse this command once you have died again.");	
							} else {
								km.logs.writeMsg(player, "You have to wait " + gt.getMinTime() + " seconds after death to tp back");	
							}
							return true;
						} else {
							km.logs.writeMsg(player, "You need to die to use this command.");
							return false;
						}
					}
				}
				
				km.logs.writeMsg(player, "You are not playing in this game.");
				return false;
			}
		}
		
		km.logs.writeMsg(player, "Game is not launched yet.");
		return false;
	}
	
	private boolean compareLoc(Location player, Location spawn) {
		return player.getBlockX() == spawn.getBlockX() && player.getBlockY() == spawn.getBlockY() && player.getBlockZ() == spawn.getBlockZ();
	}
}