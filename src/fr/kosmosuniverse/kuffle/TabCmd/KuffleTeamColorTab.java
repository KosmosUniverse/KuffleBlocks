package fr.kosmosuniverse.kuffle.TabCmd;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import fr.kosmosuniverse.kuffle.KuffleMain;
import fr.kosmosuniverse.kuffle.Core.Team;

public class KuffleTeamColorTab implements TabCompleter {
	private KuffleMain km;
	
	public KuffleTeamColorTab(KuffleMain _km) {
		km = _km;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String msg, String[] args) {
		if (!(sender instanceof Player)) {
			return null;
		}
		
		if (cmd.getName().equalsIgnoreCase("kteam-color")) {
			if (args.length == 1) {
				ArrayList<Team> teams = km.teams.getTeams();
				ArrayList<String> ret = new ArrayList<String>();
				
				for (Team item : teams) {
					ret.add(item.name);
				}
				
				return ret;
			} else if (args.length == 2) {
				ArrayList<String> colorList = new ArrayList<String>();
				ArrayList<String> colorUsed = km.teams.getTeamColors();
				
				for (ChatColor item : ChatColor.values()) {
					if (!colorUsed.contains(item.name())) {
						colorList.add(item.name());	
					}
				}
				
				return colorList;
			}
		}

		return new ArrayList<String>();
	}
}
