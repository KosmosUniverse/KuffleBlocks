package fr.kosmosuniverse.kuffle.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.kosmosuniverse.kuffle.KuffleMain;
import fr.kosmosuniverse.kuffle.MultiBlock.AMultiblock;

public class KuffleAdminSpawn implements CommandExecutor {
	private KuffleMain km;
	
	public KuffleAdminSpawn(KuffleMain _km) {
		km = _km;
	}
	
	@Override
	public boolean onCommand (CommandSender sender, Command cmd, String msg, String[] args) {
		if (!(sender instanceof Player))
			return false;
		
		Player p = (Player) sender;
		
		if (!p.hasPermission("kadminspawn")) {
			p.sendMessage("You are not allowed to do this command.");
			return false;
		}
		
		if (args.length != 1) {
			return false;
		}
		
		AMultiblock multi;
		
		if ((multi = km.multiBlock.findMultiBlockByName(args[0])) != null) {
			multi.getMultiblock().spawnMultiBlock(p);
		}
		
		return true;
	}
}