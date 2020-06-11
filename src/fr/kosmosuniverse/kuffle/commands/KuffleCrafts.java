package fr.kosmosuniverse.kuffle.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.kosmosuniverse.kuffle.KuffleMain;

public class KuffleCrafts implements CommandExecutor {
	private KuffleMain km;

	public KuffleCrafts(KuffleMain _km) {
		km = _km;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if (!(sender instanceof Player))
			return false;
		
		Player p = (Player) sender;
		
		if (!p.hasPermission("kcrafts")) {
			p.sendMessage("You are not allowed to do this command.");
			return false;
		}
		
		return true;
	}

}
