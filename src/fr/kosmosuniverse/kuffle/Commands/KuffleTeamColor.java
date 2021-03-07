package fr.kosmosuniverse.kuffle.Commands;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.kosmosuniverse.kuffle.KuffleMain;
import fr.kosmosuniverse.kuffle.utils.Utils;

public class KuffleTeamColor implements CommandExecutor {
	private KuffleMain km;

	public KuffleTeamColor(KuffleMain _km) {
		km = _km;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if (!(sender instanceof Player))
			return false;
		
		Player player = (Player) sender;
		
		if (!player.hasPermission("kteam-color")) {
			player.sendMessage("You are not allowed to do this command.");
			return false;
		}
		
		if (args.length < 1 || args.length > 2) {
			return false;
		}
		
		if (!km.teams.hasTeam(args[0])) {
			sender.sendMessage("Team <" + args[0] + "> does not exist, please choose another name.");
		} else {
			if (km.teams.getTeam(args[0]).hasPlayer(args[1])) {
				sender.sendMessage("This player is already in this team.");
				return true;
			}
			
			ChatColor tmp;
			
			if ((tmp = Utils.findChatColor(args[1])) == null) {
				sender.sendMessage("Color <" + args[1] + "> does not exist, please choose another name.");
			} else {
				ArrayList<String> colorUsed = km.teams.getTeamColors();
				
				if (!colorUsed.contains(tmp.name())) {
					String tmpColor = km.teams.getTeam(args[0]).color.name();
					
					km.teams.changeTeamColor(args[0], tmp);	
					
					sender.sendMessage("Color [" + tmpColor + "] was changed to [" + tmp + "] for team <" + args[0] + ">.");
				} else {
					sender.sendMessage("Color [" + tmp.name() + "] is already used, please choose another one.");
				}
			}
		}
		
		return true;
	}

}
