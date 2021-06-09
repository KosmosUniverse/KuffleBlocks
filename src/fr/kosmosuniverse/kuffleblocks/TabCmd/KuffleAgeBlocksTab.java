package fr.kosmosuniverse.kuffleblocks.TabCmd;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import fr.kosmosuniverse.kuffleblocks.KuffleMain;
import fr.kosmosuniverse.kuffleblocks.Core.AgeManager;

public class KuffleAgeBlocksTab implements TabCompleter  {
	private KuffleMain km;
	private ArrayList<String> ages = new ArrayList<String>();

	public KuffleAgeBlocksTab(KuffleMain _km) {
		km = _km;
		
		int max = AgeManager.getAgeMaxNumber(km.ages);
		
		for (int cnt = 0; cnt <= max; cnt++) {
			String age = AgeManager.getAgeByNumber(km.ages, cnt).name;

			ages.add(age);
		}
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String msg, String[] args) {
		if (!(sender instanceof Player)) {
			return null;
		}
		
		if (cmd.getName().equalsIgnoreCase("kb-ageitems")) {
			if (args.length == 1) {
				return ages;
			}
		}
		
		return new ArrayList<String>();
	}
}
