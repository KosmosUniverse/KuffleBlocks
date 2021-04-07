package fr.kosmosuniverse.kuffle.Commands;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import fr.kosmosuniverse.kuffle.KuffleMain;
import fr.kosmosuniverse.kuffle.Core.GameTask;

public class KuffleAgeBlocks implements CommandExecutor  {
	private KuffleMain km;

	public KuffleAgeBlocks(KuffleMain _km) {
		km = _km;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cnd, String msg, String[] args) {
		if (!(sender instanceof Player))
			return false;
		
		Player player = (Player) sender;
		
		km.logs.logMsg(player, "achieved command <kageblocks>");
		
		if (!player.hasPermission("kageblocks")) {
			km.logs.writeMsg(player, "You are not allowed to do this command.");
			return false;
		}
		
		if (km.games.size() != 0) {
			if (km.games.get(0).getEnable()) {
				String age;
				
				if ((age = getPlayerAge(player.getName())) != null) {
					ArrayList<Inventory> ageBlocks = km.blocksInvs.get(age);
					
					player.openInventory(ageBlocks.get(0));
				} else {
					km.logs.writeMsg(player, "You are not playing in this game.");
				}
			}
		} else {
			km.logs.writeMsg(player, "The game has not launched yet.");			
		}

		return true;
	}
	
	private String getPlayerAge(String player) {
		for (GameTask gt : km.games) {
			if (gt.getPlayer().getDisplayName().equals(player)) {
				return (gt.getAgeName());
			}
		}
		
		return null;
	}
}
