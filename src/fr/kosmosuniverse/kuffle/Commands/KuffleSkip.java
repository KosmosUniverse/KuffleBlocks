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
		
		Player player = (Player) sender;
		
		km.logs.logMsg(player, "achieved command <kskip>");
		
		if (!player.hasPermission("kskip")) {
			km.logs.writeMsg(player, "You are not allowed to do this command.");
			
			return false;
		}
		
		if (km.games.size() == 0) {
			km.logs.writeMsg(player, "No game launched, you can launch a game with kstart command.");
			
			return false;
		} else if (!km.games.get(0).getEnable()) {
			km.logs.writeMsg(player, "No game launched, you can launch a game with kstart command.");
			
			return false;
		}

		if (!km.config.getSkip()) {
			km.logs.writeMsg(player, "This command is disabled in config.");
			
			return false;
		}
		
		for (GameTask gt : km.games) {
			if (gt.getPlayer().equals(player)) {
				if ((gt.getAge() + 1) < km.config.getSkipAge()) {
					km.logs.writeMsg(player, "You can't skip block this age.");
					
					return false;
				}
				if (gt.getBlockCount() > 1) {
					gt.setBlockCount(gt.getBlockCount() - 1);
					
					String tmp = gt.getCurrentBlock();
					
					gt.skip();
					km.logs.writeMsg(player, "Block [" + tmp + "] was skipped.");
					
					return true;
				} else {
					km.logs.writeMsg(player, "You can't skip the first block of the age.");
					
					return false;
				}
			}
		}
		
		km.logs.writeMsg(player, "You are not in the game.");
		
		return false;
	}
}
