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
		
		Player player= (Player) sender;
		
		km.logs.logMsg(player, "achieved command <kadminspawn>");
		
		if (!player.hasPermission("kadminspawn")) {
			km.logs.writeMsg(player, "You are not allowed to do this command.");
			return false;
		}
		
		if (args.length != 1) {
			return false;
		}
		
		AMultiblock multi;
		
		if ((multi = km.multiBlock.findMultiBlockByName(args[0])) != null) {
			multi.getMultiblock().spawnMultiBlock(player);
			km.logs.writeMsg(player, "MultiBlock [" + multi.getName() + "] was spawn.");
		}
		
		return true;
	}
}
