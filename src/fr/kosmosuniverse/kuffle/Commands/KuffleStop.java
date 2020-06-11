package fr.kosmosuniverse.kuffle.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.kosmosuniverse.kuffle.KuffleMain;
import fr.kosmosuniverse.kuffle.Core.GameTask;

public class KuffleStop implements CommandExecutor {
	private KuffleMain km;

	public KuffleStop(KuffleMain _km) {
		km = _km;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if (!(sender instanceof Player))
			return false;
		
		Player p = (Player) sender;
		
		if (!p.hasPermission("kstop")) {
			p.sendMessage("You are not allowed to do this command.");
			return false;
		}
		
		if (km.games.size() == 0) {
			p.sendMessage("No game launched, you can launch a game with kstart command.");
			return false;
		} else if (!km.games.get(0).getEnable()) {
			p.sendMessage("No game launched, you can launch a game with kstart command.");
			return false;
		}
		
		for (GameTask gt : km.games) {
			gt.disable();
		}
		
		for (GameTask gt : km.games) {
			gt.exit();
		}
		
		for (GameTask gt : km.games) {
			gt.kill();
		}
		
		km.games.clear();
		return true;
	}

}
