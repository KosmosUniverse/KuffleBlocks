package fr.kosmosuniverse.kuffle.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.kosmosuniverse.kuffle.KuffleMain;
import fr.kosmosuniverse.kuffle.Core.GameTask;

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
		
		if (!player.hasPermission("klist")) {
			player.sendMessage("You are not allowed to do this command.");
			return false;
		}
		
		if (args.length == 0) {
			if (km.games.size() == 0) {
				player.sendMessage("No players in the list.");
			} else {
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < km.games.size(); i++) {
					if (i == 0) {
						sb.append(km.games.get(i).getPlayer().getName());
					} else {
						sb.append(", ").append(km.games.get(i).getPlayer().getName());	
					}
				}
				player.sendMessage("The player list contain: " + sb.toString());
			}
			return true;
		}
		
		List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
		
		for (String arg : args) {
			if (arg.contains("@a")) {
				for (Player p : players) {
					km.games.add(new GameTask(km, p));
				}
				return true;
			}
		}
		
		Player retComp;
		
		for (String arg  : args) {
			if ((retComp = searchPlayerByName(players, arg)) != null) {
				km.games.add(new GameTask(km, retComp));
			}
		}
		
		return true;
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
