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
		
		km.logs.logMsg(player, Utils.getLangString(km, player.getName(), "CMD_PERF").replace("<#>", "<kb-team-color>"));
		
		if (!player.hasPermission("kb-team-color")) {
			km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "NOT_ALLOWED"));
			return false;
		}
		
		if (km.games.size() > 0 && km.gameStarted) {
			km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "GAME_ALREADY_LAUNCHED"));
			return true;
		}
		
		if (args.length < 1 || args.length > 2) {
			return false;
		}
		
		if (!km.teams.hasTeam(args[0])) {
			km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "TEAM_NOT_EXISTS").replace("<#>", "<" + args[0] + ">"));
		} else {
			if (km.teams.getTeam(args[0]).hasPlayer(args[1])) {
				km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "TEAM_PLAYER"));
				return true;
			}
			
			ChatColor tmp;
			
			if ((tmp = Utils.findChatColor(args[1])) == null) {
				km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "COLOR_NOT_EXISTS").replace("[#]", "[" + args[1] + "]"));
			} else {
				ArrayList<String> colorUsed = km.teams.getTeamColors();
				
				if (!colorUsed.contains(tmp.name())) {
					String tmpColor = km.teams.getTeam(args[0]).color.name();
					
					km.teams.changeTeamColor(args[0], tmp);	
					
					km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "COLOR_CHANGED").replace("[#]", "[" + tmpColor + "]").replace("[##]", "[" + tmp.name() + "]").replace("<#>",	"<" + args[0] + ">"));
				} else {
					km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "COLOR_ALREADY_USED").replace("[#]", "[" + tmp.name() + "]"));
				}
			}
		}
		
		return true;
	}

}
