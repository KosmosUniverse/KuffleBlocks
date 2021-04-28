package fr.kosmosuniverse.kuffleblocks.TabCmd;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import fr.kosmosuniverse.kuffleblocks.KuffleMain;
import fr.kosmosuniverse.kuffleblocks.Core.GameTask;

public class KuffleAdminSkipTab implements TabCompleter {
	private KuffleMain km;
	
	public KuffleAdminSkipTab(KuffleMain _km) {
		km = _km;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender,  Command cmd, String msg, String[] args) {
		if (!(sender instanceof Player))
			return null;
		
		if (cmd.getName().equalsIgnoreCase("kadminskip")) {
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