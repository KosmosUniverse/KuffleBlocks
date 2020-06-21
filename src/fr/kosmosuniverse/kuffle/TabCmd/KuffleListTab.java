package fr.kosmosuniverse.kuffle.TabCmd;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class KuffleListTab implements TabCompleter {
	private ArrayList<String> list = new ArrayList<String>();
	
	public KuffleListTab() {
		list.add("@a");
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			list.add(player.getName());
		}
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender,  Command cmd, String msg, String[] args) {
		if (cmd.getName().equalsIgnoreCase("klist")) {
			if (!(sender instanceof Player))
				return null;
			return list;
		}
		
		return null;
	}
	
	public void reset() {
		list.add("@a");
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			list.add(player.getName());
		}
	}
}
