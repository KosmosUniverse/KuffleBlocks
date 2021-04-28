package fr.kosmosuniverse.kuffleblocks.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.kosmosuniverse.kuffleblocks.KuffleMain;
import fr.kosmosuniverse.kuffleblocks.Core.GameTask;

public class KuffleValidate implements CommandExecutor {
	private KuffleMain km;

	public KuffleValidate(KuffleMain _km) {
		km = _km;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if (!(sender instanceof Player))
			return false;
		
		Player player = (Player) sender;
		
		km.logs.logMsg(player, "achieved command <kvalidate>");
		
		if (!player.hasPermission("kvalidate")) {
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
		
		if (args.length != 1) {
			km.logs.writeMsg(player, "This command takes 1 argument: the name of the player to validate his block.");
			return false;
		}

		for (GameTask gt : km.games) {
			if (gt.getPlayer().getName().contains(args[0])) {
				String tmp = gt.getCurrentBlock();
				
				gt.validate();
				km.logs.writeMsg(player, "Block [" + tmp + "] was validated for player <" + gt.getPlayer().getDisplayName() + ">.");
				
				return true;
			}
		}
		
		km.logs.writeMsg(player, "Can't find player to validate his block.");
		
		return false;
	}

}
