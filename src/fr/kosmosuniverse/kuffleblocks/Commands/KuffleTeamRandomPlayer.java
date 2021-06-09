package fr.kosmosuniverse.kuffleblocks.Commands;

import java.security.SecureRandom;
import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.kosmosuniverse.kuffleblocks.KuffleMain;
import fr.kosmosuniverse.kuffleblocks.Core.Team;
import fr.kosmosuniverse.kuffleblocks.utils.Utils;

public class KuffleTeamRandomPlayer implements CommandExecutor {
	private KuffleMain km;

	public KuffleTeamRandomPlayer(KuffleMain _km) {
		km = _km;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if (!(sender instanceof Player))
			return false;
		
		Player player = (Player) sender;
		
		km.logs.logMsg(player, Utils.getLangString(km, player.getName(), "CMD_PERF").replace("<#>", "<kb-team-random-player>"));
		
		if (!player.hasPermission("kb-team-random-player")) {
			km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "NOT_ALLOWED"));
			return false;
		}
		
		if (km.games.size() > 0 && km.gameStarted) {
			km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "GAME_ALREADY_LAUNCHED"));
			return true;
		}
		
		if (args.length != 0) {
			return false;
		}
		
		if (km.games.size() == 0) {
			km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "LIST_EMPTY"));
			return true;
		}
		
		if (calcMAxPlayers() < Utils.getPlayerList(km.games).size()) {
			km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "TEAM_TOO_MANY_PLAYERS"));
			return true;
		}
		
		if (!checkEmptyTeams()) {
			km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "TEAM_ALREADY_PLAYERS"));
			return true;
		}
		
		int cnt = 0;
		ArrayList<Player> players = Utils.getPlayerList(km.games);
		SecureRandom r = new SecureRandom();
		
		while (players.size() > 0) {
			int idx = r.nextInt(players.size());
			
			km.teams.affectPlayer(km.teams.getTeams().get(cnt).name, players.get(idx));
			
			players.remove(idx);
			
			cnt++;
			
			if (cnt >= km.teams.getTeams().size()) {
				cnt = 0;
			}
		}
		
		km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "RANDOM").replace("%i", "" + Utils.getPlayerNames(km.games).size()).replace("%j", "" + km.teams.getTeams().size()));

		return true;
	}
	
	public int calcMAxPlayers() {
		return (km.config.getTeamSize() * km.teams.getTeams().size());
	}

	public boolean checkEmptyTeams() {
		for (Team item : km.teams.getTeams()) {
			if (item.players.size() != 0) {
				return false;
			}
		}
		
		return true;
	}
}
