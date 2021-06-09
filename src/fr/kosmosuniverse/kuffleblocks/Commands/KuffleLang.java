package fr.kosmosuniverse.kuffleblocks.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.kosmosuniverse.kuffleblocks.KuffleMain;
import fr.kosmosuniverse.kuffleblocks.utils.Utils;

public class KuffleLang implements CommandExecutor {
	private KuffleMain km;
	
	public KuffleLang(KuffleMain _km) {
		km = _km;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if (!(sender instanceof Player))
			return false;
		
		if (args.length > 1) {
			return false; 
		}
		
		Player player = (Player) sender;
		
		km.logs.logMsg(player, Utils.getLangString(km, player.getName(), "CMD_PERF").replace("<#>", "<kb-lang>"));
		
		if (!player.hasPermission("kb-lang")) {
			km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "NOT_ALLOWED"));
			return false;
		}
		
		if (km.gameStarted) {
			for (String playerName : km.games.keySet()) {
				if (km.games.get(playerName).getPlayer().equals(player)) {
					if (args.length == 0) {
						km.logs.writeMsg(player, km.games.get(playerName).getLang());
						
						return true;
					} else if (args.length == 1) {
						String lang = args[0].toLowerCase();
						
						if (km.langs.contains(lang)) {
							km.games.get(playerName).setLang(lang);
							
							km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "LANG_SET").replace("[#]", " [" + lang + "]"));
						} else {
							km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "REQ_LANG_NOT_AVAIL"));
						}
						
						return true;
					}
				}
			}
		} else {
			km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "NOT_PLAYING"));
			return false;
		}

		km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "GAME_NOT_LAUNCHED"));
		return true;
	}

}
