package fr.kosmosuniverse.kuffleblocks.TabCmd;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import fr.kosmosuniverse.kuffleblocks.KuffleMain;
import fr.kosmosuniverse.kuffleblocks.Core.Team;
import fr.kosmosuniverse.kuffleblocks.utils.Utils;

public class KuffleTeamAffectPlayerTab implements TabCompleter {
	private KuffleMain km;
	
	public KuffleTeamAffectPlayerTab(KuffleMain _km) {
		km = _km;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String msg, String[] args) {
		if (!(sender instanceof Player)) {
			return null;
		}
		
		if (cmd.getName().equalsIgnoreCase("kteam-affect-player")) {
			if (args.length == 1) {				
				ArrayList<Team> teams = km.teams.getTeams();
				ArrayList<String> ret = new ArrayList<String>();
				
				for (Team item : teams) {
					ret.add(item.name);
				}
				
				return ret;
			} else if (args.length == 2) {
				ArrayList<String> ret = new ArrayList<String>();
				
				for (String item : Utils.getPlayerNames(km.games)) {
					if (!km.teams.isInTeam(item)) {
						ret.add(item);
					}
				}
				
				return ret;
			}
		}

		return new ArrayList<String>();
	}
}
