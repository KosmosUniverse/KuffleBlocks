package fr.kosmosuniverse.kuffleblocks.Commands;

import java.util.ArrayList;
import java.util.Random;

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
		
		km.logs.logMsg(player, "achieved command <kteam-random-player>");
		
		if (!player.hasPermission("kteam-random-player")) {
			km.logs.writeMsg(player, "You are not allowed to do this command.");
			return false;
		}
		
		if (km.games.size() > 0 && km.games.get(0).getEnable()) {
			km.logs.writeMsg(player, "Game is already launched, you cannot modify teams during the game.");
			return true;
		}
		
		if (args.length != 0) {
			return false;
		}
		
		if (calcMAxPlayers() < Utils.getPlayerList(km.games).size()) {
			km.logs.writeMsg(player, "There are too many players for that number of team, please create more teams or change team size with kconfig command.");
			return true;
		}
		
		if (!checkEmptyTeams()) {
			km.logs.writeMsg(player, "There already are players in some teams, please reset all teams using '/kteam-reset-players <Team>' command.");
			return true;
		}
		
		int cnt = 0;
		ArrayList<Player> players = Utils.getPlayerList(km.games);
		Random r = new Random();
		
		while (players.size() > 0) {
			int idx = r.nextInt(players.size());
			
			km.teams.affectPlayer(km.teams.getTeams().get(cnt).name, players.get(idx));
			
			players.remove(idx);
			
			cnt++;
			
			if (cnt >= km.teams.getTeams().size()) {
				cnt = 0;
			}
		}
		
		km.logs.writeMsg(player, "Randomly add " + Utils.getPlayerNames(km.games).size() + " in " + km.teams.getTeams().size() + " teams.");
		
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
