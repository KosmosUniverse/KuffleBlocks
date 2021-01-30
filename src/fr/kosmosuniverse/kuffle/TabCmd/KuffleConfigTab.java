package fr.kosmosuniverse.kuffle.TabCmd;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import fr.kosmosuniverse.kuffle.KuffleMain;

public class KuffleConfigTab implements TabCompleter {
	private KuffleMain km;
	private ArrayList<String> all = new ArrayList<String>();
	
	public KuffleConfigTab(KuffleMain _km) {
		km = _km;
		
		all.addAll(km.config.booleanElems.keySet());
		all.addAll(km.config.intElems.keySet());
		all.addAll(km.config.stringElems.keySet());
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String msg, String[] args) {
		if (!(sender instanceof Player)) {
			return null;
		}
		
		if (cmd.getName().equalsIgnoreCase("kconfig")) {
			if (args.length == 0) {
				return all;
			} else if (args.length % 2 == 1) {
				ArrayList<String> ret = new ArrayList<String>();
				ret.addAll(all);
				
				for (String arg : args) {
					if (ret.contains(arg)) {
						ret.remove(arg);
					}
				}
				
				return ret;
			} else {
				if (km.config.booleanElems.keySet().contains(args[args.length - 2])) {
					return km.config.booleanRet.get(args[args.length - 2]);
				} else if (km.config.intElems.keySet().contains(args[args.length - 2])) {
					return km.config.intRet.get(args[args.length - 2]);
				} else if (km.config.stringElems.keySet().contains(args[args.length - 2])) {
					return km.config.stringRet.get(args[args.length - 2]);
				} else {
					return new ArrayList<String>();	
				}
			}
		}
		
		return null;
	}

}
