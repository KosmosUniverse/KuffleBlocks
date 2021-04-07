package fr.kosmosuniverse.kuffle.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.kosmosuniverse.kuffle.KuffleMain;
import fr.kosmosuniverse.kuffle.Core.GameTask;

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
		
		km.logs.logMsg(player, "achieved command <klang>");
		
		if (km.games.size() != 0) {
			if (km.games.get(0).getEnable()) {
				for (GameTask gt : km.games) {
					if (gt.getPlayer().equals(player)) {
						if (args.length == 0) {
							km.logs.writeMsg(player, gt.getLang());
							
							return true;
						} else if (args.length == 1) {
							String lang = args[0].toLowerCase();
							
							if (km.langs.contains(lang)) {
								gt.setLang(lang);
								
								km.logs.writeMsg(player, "Lang set to [" + lang + "]");
							} else {
								km.logs.writeMsg(player, "Requested lang is not available.");
							}
							
							return true;
						}
					}
				}
			} else {
				km.logs.writeMsg(player, "You are not playing in this game.");
				return false;
			}
		}

		km.logs.writeMsg(player, "Game has not launched yet.");
		return true;
	}

}
