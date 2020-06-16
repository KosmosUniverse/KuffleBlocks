package fr.kosmosuniverse.kuffle.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.kosmosuniverse.kuffle.KuffleMain;

public class KuffleMultiBlocks implements CommandExecutor {
	private KuffleMain km;

	public KuffleMultiBlocks(KuffleMain _km) {
		km = _km;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if (!(sender instanceof Player))
			return false;
		
		Player p = (Player) sender;
		
		if (!p.hasPermission("kmultiblocks")) {
			p.sendMessage("You are not allowed to do this command.");
			return false;
		}
		
		p.openInventory(km.multiBlock.getAllMultiBlocksInventory());
		
		return true;
	}
}
