package fr.kosmosuniverse.kuffle.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.kosmosuniverse.kuffle.KuffleMain;

public class KuffleConfig implements CommandExecutor {
	private KuffleMain km;
	
	public KuffleConfig(KuffleMain _km) {
		km = _km;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if (!(sender instanceof Player))
			return false;
		
		if (args.length % 2 == 1) {
			return false;
		}
		
		Player player = (Player) sender;
		
		if (!player.hasPermission("kconfig")) {
			player.sendMessage("You are not allowed to do this command.");
			return false;
		}
		
		if (args.length == 0) {
			km.config.displayConfig();
		} else {
			for (int i = 0; i < args.length; i++) {
				String before = "";
				
				if (i % 2 == 0) {
					if (before != "") {
						before = "";
					}
					
					before = args[i];
				} else {
					if (before == "SATURATION") {
						km.config.saturation = Boolean.getBoolean(args[i]);
					} else if (before == "SPREADPLAYERS") {
						
					} else if (before == "SPREAD_MIN_DISTANCE") {
						
					} else if (before == "SPREAD_MAX_DISTANCE") {
						
					} else if (before == "REWARDS") {
						
					} else if (before == "SKIP") {
						
					} else if (before == "CUSTOM_CRAFTS") {
						
					} else if (before == "SEE_BLOCK_CNT") {
						
					} else if (before == "BLOCK_PER_AGE") {
						
					} else if (before == "FIRST_AGE_SKIP") {
						
					} else if (before == "NB_AGE") {
						
					} else if (before == "START_DURATION") {
						
					} else if (before == "ADDED_DURATION") {
						
					} else if (before == "LANG") {
						
					} else if (before == "LEVEL") {
						
					}
				}
			}
		}
		
		return true;
	}
}
