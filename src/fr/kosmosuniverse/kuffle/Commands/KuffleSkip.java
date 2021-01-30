package fr.kosmosuniverse.kuffle.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.kosmosuniverse.kuffle.KuffleMain;
import fr.kosmosuniverse.kuffle.Core.GameTask;

public class KuffleSkip implements CommandExecutor {
	private KuffleMain km;

	public KuffleSkip(KuffleMain _km) {
		km = _km;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if (!(sender instanceof Player))
			return false;
		
		Player p = (Player) sender;
		
		if (!p.hasPermission("kskip")) {
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

		if (!km.config.getSkip()) {
			p.sendMessage("This command is disabled in config.");
			return false;
		}
		
		for (GameTask gt : km.games) {
			if (gt.getPlayer().equals(p)) {
				if ((gt.getAge() + 1) < km.config.getSkipAge()) {
					p.sendMessage("You can't skip block this age.");
					return false;
				}
				if (gt.getBlockCount() > 1) {
					gt.setBlockCount(gt.getBlockCount() - 1);
					gt.skip();
					return true;
				} else {
					p.sendMessage("You can't skip the first block of the age.");
					return false;
				}
			}
		}
		
		p.sendMessage("You are not in the game.");
		
		return false;
	}
}
