package fr.kosmosuniverse.kuffleblocks.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.kosmosuniverse.kuffleblocks.KuffleMain;
import fr.kosmosuniverse.kuffleblocks.utils.Utils;

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
		
		km.logs.logMsg(player, Utils.getLangString(km, player.getName(), "CMD_PERF").replace("<#>", "<kb-skip>"));
		
		if (!km.gameStarted) {
			km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "GAME_NOT_LAUNCHED"));
			
			return false;
		}

		if (!km.config.getSkip()) {
			km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "CONFIG_DISABLED"));
			
			return false;
		}
		
		if (msg.equals("kb-skip")) {
			if (!player.hasPermission("kb-skip")) {
				km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "NOT_ALLOWED"));
			} else {
				if (args.length != 0) {
					return false;
				}
				
				for (String playerName : km.games.keySet()) {
					if (km.games.get(playerName).getPlayer().equals(player)) {
						km.games.get(playerName).skip(true);
						
						return true;
					}
				}
				
				km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "NOT_PLAYING"));
			}
			
			return false;
		} else if (msg.equals("kb-adminskip")) {
			if (!player.hasPermission("kb-adminskip")) {
				km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "NOT_ALLOWED"));
			} else {
				if (args.length != 1) {
					return false;
				}
				
				if (args.length == 1) {
					for (String playerName : km.games.keySet()) {
						if (playerName.equals(args[0])) {
							String tmp = km.games.get(playerName).getCurrentBlock();
							km.games.get(playerName).skip(false);
							km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "BLOCK_SKIPPED").replace("[#]", " [" + tmp + "] ").replace("<#>", " <" + playerName + ">"));
							
							return true;
						}
					}
					
					km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "PLAYER_NOT_FOUND"));
				}
				
				return false;
			}
		}
		
		return false;
	}
}
