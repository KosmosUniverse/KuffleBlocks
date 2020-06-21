package fr.kosmosuniverse.kuffle.TabCmd;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import fr.kosmosuniverse.kuffle.KuffleMain;

public class KuffleAdminSpawnTab implements TabCompleter {
	private KuffleMain km;
	private ArrayList<String> list = new ArrayList<String>();
	
	public KuffleAdminSpawnTab(KuffleMain _km) {
		km = _km;
		
		for (String key : km.multiBlock.getMultiBlocks().keySet()) {
			list.add(km.multiBlock.getMultiBlocks().get(key).getName());
		}
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender,  Command cmd, String msg, String[] args) {
		if (cmd.getName().equalsIgnoreCase("kadminspawn")) {
			if (!(sender instanceof Player))
				return null;
			return list;
		}
		
		return null;
	}
}