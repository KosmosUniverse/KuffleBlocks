package fr.kosmosuniverse.kuffle.TabCmd;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import fr.kosmosuniverse.kuffle.KuffleMain;
import fr.kosmosuniverse.kuffle.Core.GameTask;

public class KuffleListTab implements TabCompleter {
	private KuffleMain km;
	private ArrayList<String> list = new ArrayList<String>();
	
	public KuffleListTab(KuffleMain _km) {
		km = _km;
		
		list.add("add");
		list.add("remove");
		list.add("reset");
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender,  Command cmd, String msg, String[] args) {
		if (cmd.getName().equalsIgnoreCase("klist")) {
			if (!(sender instanceof Player))
				return null;
			if (args.length == 1) {
				return list;	
			} else if (args.length == 2) {
				ArrayList<String> nextList = new ArrayList<String>();
				
				if (args[0].equals("add")) {
					nextList.add("@a");
					
					for (Player player : Bukkit.getOnlinePlayers()) {
						nextList.add(player.getName());
					}
					
					return nextList;
				} else if (args[0].equals("remove")) {
					for (GameTask gt : km.games) {
						nextList.add(gt.getPlayer().getName());
					}
					return nextList;
				}
			}
		}
		
		return new ArrayList<String>();
	}
}
