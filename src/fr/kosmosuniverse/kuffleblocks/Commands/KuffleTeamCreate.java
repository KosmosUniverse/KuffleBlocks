package fr.kosmosuniverse.kuffleblocks.Commands;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.kosmosuniverse.kuffleblocks.KuffleMain;
import fr.kosmosuniverse.kuffleblocks.utils.Utils;

public class KuffleTeamCreate implements CommandExecutor {
	private KuffleMain km;

	public KuffleTeamCreate(KuffleMain _km) {
		km = _km;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if (!(sender instanceof Player))
			return false;
		
		Player player = (Player) sender;
		
		km.logs.logMsg(player, Utils.getLangString(km, player.getName(), "CMD_PERF").replace("<#>", "<kb-team-create>"));
		
		if (!player.hasPermission("kb-team-create")) {
			km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "NOT_ALLOWED"));
			return false;
		}
		
		if (!km.config.getTeam()) {
			km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "TEAM_ENABLE"));
			return true;
		}
		
		if (km.games.size() > 0 && km.gameStarted) {
			km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "GAME_ALREADY_LAUNCHED"));
			return true;
		}
		
		if (args.length < 1 || args.length > 2) {
			return false;
		}
		
		if (km.teams.hasTeam(args[0])) {
			km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "TEAM_EXISTS").replace("<#>", "<" + args[0] + ">"));
		} else {
			if (args.length == 1) {
				km.teams.createTeam(args[0]);
				km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "TEAM_CREATED").replace("<#>", "<" + args[0] + ">"));
			} else if (args.length == 2) {
				ChatColor tmp;
				
				if ((tmp = Utils.findChatColor(args[1])) == null) {
					km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "COLOR_NOT_EXISTS").replace("[#]", "[" + args[1] + "]"));
				} else {
					ArrayList<String> colorUsed = km.teams.getTeamColors();
					
					if (!colorUsed.contains(tmp.name())) {
						km.teams.createTeam(args[0], tmp);
						
						km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "TEAM_CREATED").replace("<#>", "<" + args[0] + ">"));
					} else {
						km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "COLOR_ALREADY_USED").replace("[#]", "[" + tmp.name() + "]"));
					}
				}
			}
		}
		
		return true;
	}
}
