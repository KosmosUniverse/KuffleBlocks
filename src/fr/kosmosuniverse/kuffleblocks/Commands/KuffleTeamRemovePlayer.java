package fr.kosmosuniverse.kuffleblocks.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.kosmosuniverse.kuffleblocks.KuffleMain;
import fr.kosmosuniverse.kuffleblocks.utils.Utils;

public class KuffleTeamRemovePlayer implements CommandExecutor {
	private KuffleMain km;

	public KuffleTeamRemovePlayer(KuffleMain _km) {
		km = _km;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if (!(sender instanceof Player))
			return false;
		
		Player player = (Player) sender;
		
		km.logs.logMsg(player, Utils.getLangString(km, player.getName(), "CMD_PERF").replace("<#>", "<kb-team-remove-player>"));
		
		if (!player.hasPermission("kb-team-remove-player")) {
			km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "NOT_ALLOWED"));
			return false;
		}
		
		if (km.games.size() > 0 && km.gameStarted) {
			km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "GAME_ALREADY_LAUNCHED"));
			return true;
		}
				
		if (args.length != 2) {
			return false;
		}
		
		if (!km.teams.hasTeam(args[0])) {
			km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "TEAM_NOT_EXISTS").replace("<#>", "<" + args[0] + ">"));
			return true;
		}
		
		if (!km.teams.getTeam(args[0]).hasPlayer(args[1])) {
			km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "TEAM_NO_PLAYER"));
			return true;
		}
		
		for (String playerName : km.games.keySet()) {
			if (km.games.get(playerName).getPlayer().getDisplayName().equals(args[1])) {
				km.teams.removePlayer(args[0], km.games.get(playerName).getPlayer());
				km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "TEAM_REMOVED").replace("<#>", "<" + args[1] + ">").replace("<##>", "<" + args[0] + ">"));
				
				return true;
			}
		}
		
		return true;
	}

}
