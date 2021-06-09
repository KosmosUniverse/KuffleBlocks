package fr.kosmosuniverse.kuffleblocks.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.kosmosuniverse.kuffleblocks.KuffleMain;
import fr.kosmosuniverse.kuffleblocks.Core.Game;
import fr.kosmosuniverse.kuffleblocks.utils.Utils;

public class KuffleList implements CommandExecutor {
	private KuffleMain km;

	public KuffleList(KuffleMain _km) {
		km = _km;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if (!(sender instanceof Player))
			return false;
		
		Player player = (Player) sender;
		
		km.logs.logMsg(player, Utils.getLangString(km, player.getName(), "CMD_PERF").replace("<#>", "<kb-list>"));
		
		if (!player.hasPermission("kb-list")) {
			km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "NOT_ALLOWED"));
			
			return false;
		}
		
		if (args.length == 0) {
			if (km.games.size() == 0) {
				km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "NO_PLAYERS"));
			} else {
				StringBuilder sb = new StringBuilder();
				int i = 0;
				
				for (String playerName : km.games.keySet()) {
					if (i == 0) {
						sb.append(playerName);
					} else {
						sb.append(", ").append(playerName);	
					}
					
					i++;
				}
				
				km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "PLAYER_LIST") + " " + sb.toString());
			}
			
			return true;
		} else if (args.length == 1) {
			if (args[0].equals("reset")) {
				if (km.games.size() == 0) {
					km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "NO_PLAYERS"));
					
					return false;
				}
				
				km.games.clear();
				km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "LIST_RESET"));
				
				return true;
			}
		} else if (args.length == 2) {
			List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());

			if (args[0].equals("add")) {
				if (args[1].equals("@a")) {
					int cnt = 0;
					
					for (Player p : players) {
						if (!km.games.containsKey(p.getName())) {
							km.games.put(p.getName(), new Game(km, p));
							cnt++;
						}
					}
					
					km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "ADDED_LIST").replace("%i", "" + cnt));
					
					return true;
				} else {
					Player retComp;
					
					if ((retComp = searchPlayerByName(players, args[1])) != null) {
						if (!km.games.containsKey(retComp.getName())) {
							km.games.put(retComp.getName(), new Game(km, retComp));
							km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "ADDED_ONE_LIST"));
							
							return true;
						} else {
							km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "PLAYER_ALREADY_LIST"));
							
							return false;
						}
					} else {
						return false;
					}
				}
			} else if (args[0].equals("remove")) {
				if (km.games.size() == 0) {
					km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "NO_PLAYERS"));
					
					return false;
				}
				
				if (km.games.containsKey(args[1])) {
					km.games.remove(args[1]);
					km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "REMOVED_LIST"));
					
					return true;
				}
			}
		}
		
		return false;
	}
	
	private Player searchPlayerByName(List<Player> players, String name) {
		for (Player player : players) {
			if (player.getName().contains(name)) {
				return player;
			}
		}
		
		return null;
	}

}
