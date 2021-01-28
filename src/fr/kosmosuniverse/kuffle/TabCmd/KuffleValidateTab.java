package fr.kosmosuniverse.kuffle.TabCmd;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import fr.kosmosuniverse.kuffle.KuffleMain;
import fr.kosmosuniverse.kuffle.Core.GameTask;

public class KuffleValidateTab implements TabCompleter {
	private KuffleMain km;
	
	public KuffleValidateTab(KuffleMain _km) {
		km = _km;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender,  Command cmd, String msg, String[] args) {
		if (!(sender instanceof Player))
			return null;
		
		if (cmd.getName().equalsIgnoreCase("kvalidate")) {
			if (args.length == 1) {
				ArrayList<String> list = new ArrayList<String>();
				
				for (GameTask gt : km.games) {
					list.add(gt.getPlayer().getName());
				}
				
				return list;
			}
		}
		
		return new ArrayList<String>();
	}
}
