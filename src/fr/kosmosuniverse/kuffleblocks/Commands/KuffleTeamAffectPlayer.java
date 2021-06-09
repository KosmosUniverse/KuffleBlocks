package fr.kosmosuniverse.kuffleblocks.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.kosmosuniverse.kuffleblocks.KuffleMain;
import fr.kosmosuniverse.kuffleblocks.utils.Utils;

public class KuffleTeamAffectPlayer implements CommandExecutor {
	private KuffleMain km;

	public KuffleTeamAffectPlayer(KuffleMain _km) {
		km = _km;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if (!(sender instanceof Player))
			return false;
		
		Player player = (Player) sender;
		
		km.logs.logMsg(player, Utils.getLangString(km, player.getName(), "CMD_PERF").replace("<#>", "<kb-team-affect-player>"));
		
		if (!player.hasPermission("kb-team-affect-player")) {
			km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "NOT_ALLOWED"));
			return false;
		}
		
		if (km.games.size() > 0 && km.gameStarted) {
			km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "GAME_LAUNCHED"));
			return true;
		}
		
		if (args.length != 2) {
			return false;
		}
		
		if (!km.teams.hasTeam(args[0])) {
			km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "TEAM_NOT_EXISTS").replace("<#>", "<" + args[0] + ">"));
			return true;
		}
		
		if (km.teams.getTeam(args[0]).players.size() == km.config.getTeamSize()) {
			km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "TEAM_FULL"));
			return true;
		}
		
		if (!Utils.getPlayerNames(km.games).contains(args[1])) {
			km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "PLAYER_NOT_IN_GAME"));
			return true;
		}
		
		for (String playerName : km.games.keySet()) {
			if (km.games.get(playerName).getPlayer().getName().equals(args[1])) {
				km.teams.affectPlayer(args[0], km.games.get(playerName).getPlayer());
				
				km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "TEAM_ADD_PLAYER").replace("<#>", "<" + args[1] + ">").replace("<##>", "<" + args[0] + ">"));
				
				return true;
			}
		}
		
		return true;
	}

}
