package fr.kosmosuniverse.kuffle.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.kosmosuniverse.kuffle.KuffleMain;
import fr.kosmosuniverse.kuffle.Core.GameTask;

public class KuffleStart implements CommandExecutor {
	private KuffleMain km;

	public KuffleStart(KuffleMain _km) {
		km = _km;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if (!(sender instanceof Player))
			return false;
		
		Player p = (Player) sender;
		
		if (!p.hasPermission("kstart")) {
			p.sendMessage("You are not allowed to do this command.");
			return false;
		}
		
		if (km.games.size() == 0) {
			p.sendMessage("You need to first add people with klist command.");
			return false;
		}
		
		if (km.games.get(0).getEnable()) {
			p.sendMessage("The game is already running.");
			return false;
		}
		
		if (km.getConfig().getBoolean("game_settings.saturation")) {
			for (GameTask gt : km.games) {
				gt.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 999999, 10, false, false, false));
			}
		}
		
		int spread = 0;
		
		if (km.getConfig().getBoolean("game_settings.spreadplayers.enable")) {
/*			StringBuilder sb = new StringBuilder();
			
			for (GameTask gt : km.games) {
				sb.append(" ").append(gt.getPlayer().getName());
			}*/
			
			Bukkit.dispatchCommand(sender, "spreadplayers " + p.getLocation().getBlockX() + " " + p.getLocation().getBlockZ() + " " + (km.getConfig().getInt("game_settings.spreadplayers.minimum_distance") * km.games.size()) + " " + (km.getConfig().getInt("game_settings.spreadplayers.maximum_area") * km.games.size()) + " false @a");
			
			for (GameTask gt : km.games) {
				if (gt.getPlayer().isOp()) {
					gt.getPlayer().performCommand("spawnpoint");
				} else {
					Bukkit.dispatchCommand(sender, "op " + gt.getPlayer().getName());
					gt.getPlayer().performCommand("spawnpoint");
					Bukkit.dispatchCommand(sender, "deop " + gt.getPlayer().getName());
				}
			}
			spread = 20;
		}
		
		km.paused = true;
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(km, new Runnable() {
			@Override
			public void run() {
				Bukkit.dispatchCommand(sender, "title @a title {\"text\":\"5\",\"bold\":true,\"color\":\"red\"}");
			}
		}, 20 + spread);
		Bukkit.getScheduler().scheduleSyncDelayedTask(km, new Runnable() {
			@Override
			public void run() {
				Bukkit.dispatchCommand(sender, "title @a title {\"text\":\"4\",\"bold\":true,\"color\":\"gold\"}");
			}
		}, 40 + spread);
		Bukkit.getScheduler().scheduleSyncDelayedTask(km, new Runnable() {
			@Override
			public void run() {
				Bukkit.dispatchCommand(sender, "title @a title {\"text\":\"3\",\"bold\":true,\"color\":\"yellow\"}");
			}
		}, 60 + spread);
		Bukkit.getScheduler().scheduleSyncDelayedTask(km, new Runnable() {
			@Override
			public void run() {
				Bukkit.dispatchCommand(sender, "title @a title {\"text\":\"2\",\"bold\":true,\"color\":\"green\"}");
			}
		}, 80 + spread);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(km, new Runnable() {
			@Override
			public void run() {
				Bukkit.dispatchCommand(sender, "title @a title {\"text\":\"1\",\"bold\":true,\"color\":\"blue\"}");
			}
		}, 100 + spread);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(km, new Runnable() {
			@Override
			public void run() {
				Bukkit.dispatchCommand(sender, "title @a title {\"text\":\"GO!\"}");
				for (GameTask gt : km.games) {
					gt.startRunnable();
				}
				
				for (GameTask gt : km.games) {
					gt.enable();
				}
				
				km.paused = false;
			}
		}, 120 + spread);
		
		return true;
	}

}
