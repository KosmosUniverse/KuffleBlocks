package fr.kosmosuniverse.kuffleblocks.Commands;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.kosmosuniverse.kuffleblocks.KuffleMain;
import fr.kosmosuniverse.kuffleblocks.utils.Utils;

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
		
		km.logs.logMsg(player, "achieved command <kteam-color>");
		
		if (!player.hasPermission("kteam-color")) {
			km.logs.writeMsg(player, "You are not allowed to do this command.");
			return false;
		}
		
		if (km.games.size() > 0 && km.games.get(0).getEnable()) {
			km.logs.writeMsg(player, "Game is already launched, you cannot modify teams during the game.");
			return true;
		}
		
		if (args.length < 1 || args.length > 2) {
			return false;
		}
		
		if (!km.teams.hasTeam(args[0])) {
			km.logs.writeMsg(player, "Team <" + args[0] + "> does not exist, please choose another name.");
		} else {
			if (km.teams.getTeam(args[0]).hasPlayer(args[1])) {
				km.logs.writeMsg(player, "This player is already in this team.");
				return true;
			}
			
			ChatColor tmp;
			
			if ((tmp = Utils.findChatColor(args[1])) == null) {
				km.logs.writeMsg(player, "Color <" + args[1] + "> does not exist, please choose another name.");
			} else {
				ArrayList<String> colorUsed = km.teams.getTeamColors();
				
				if (!colorUsed.contains(tmp.name())) {
					String tmpColor = km.teams.getTeam(args[0]).color.name();
					
					km.teams.changeTeamColor(args[0], tmp);	
					
					km.logs.writeMsg(player, "Color [" + tmpColor + "] was changed to [" + tmp.name() + "] for team <" + args[0] + ">.");
				} else {
					km.logs.writeMsg(player, "Color [" + tmp.name() + "] is already used, please choose another one.");
				}
			}
		}
		
		return true;
	}

}
