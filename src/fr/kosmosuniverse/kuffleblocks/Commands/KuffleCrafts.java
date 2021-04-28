package fr.kosmosuniverse.kuffleblocks.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.kosmosuniverse.kuffleblocks.KuffleMain;

public class KuffleCrafts implements CommandExecutor {
	private KuffleMain km;

	public KuffleCrafts(KuffleMain _km) {
		km = _km;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if (!(sender instanceof Player))
			return false;
		
		Player player = (Player) sender;
		
		km.logs.logMsg(player, "achieved command <kcrafts>");
		
		if (!player.hasPermission("kcrafts")) {
			km.logs.writeMsg(player, "You are not allowed to do this command.");
			return false;
		}
		
		player.openInventory(km.crafts.getAllCraftsInventory());
		
		return true;
	}
}
