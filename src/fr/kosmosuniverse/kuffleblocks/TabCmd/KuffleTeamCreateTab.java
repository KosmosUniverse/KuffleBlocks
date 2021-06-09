package fr.kosmosuniverse.kuffleblocks.TabCmd;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import fr.kosmosuniverse.kuffleblocks.KuffleMain;

public class KuffleTeamCreateTab implements TabCompleter {
	private KuffleMain km;
	
	public KuffleTeamCreateTab(KuffleMain _km) {
		km = _km;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String msg, String[] args) {
		if (!(sender instanceof Player)) {
			return null;
		}
		
		if (cmd.getName().equalsIgnoreCase("kb-team-create")) {
			if (args.length == 2) {
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
