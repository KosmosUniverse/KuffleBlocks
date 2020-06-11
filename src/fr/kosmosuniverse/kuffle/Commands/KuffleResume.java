package fr.kosmosuniverse.kuffle.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import fr.kosmosuniverse.kuffle.KuffleMain;
import fr.kosmosuniverse.kuffle.Core.GameTask;

public class KuffleResume implements CommandExecutor {
	private KuffleMain km;
	
	public KuffleResume(KuffleMain _km) {
		km = _km;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if (!(sender instanceof Player))
			return false;
		
		Player p = (Player) sender;
		
		if (!p.hasPermission("kresume")) {
			p.sendMessage("You are not allowed to do this command.");
			return false;
		}
		
		if (km.games.size() == 0) {
			p.sendMessage("You need to first add people with klist command, launch a game with kstart command.");
			return false;
		}
		
		if (km.games.get(0).getEnable()) {
			p.sendMessage("Your game is already running, you can pause it with kpause command.");
			return false;
		}
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(km, new Runnable() {
			@Override
			public void run() {
				Bukkit.dispatchCommand(sender, "title @a title {\"text\":\"3\",\"bold\":true,\"color\":\"red\"}");
			}
		}, 20);
		Bukkit.getScheduler().scheduleSyncDelayedTask(km, new Runnable() {
			@Override
			public void run() {
				Bukkit.dispatchCommand(sender, "title @a title {\"text\":\"2\",\"bold\":true,\"color\":\"yellow\"}");
			}
		}, 40);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(km, new Runnable() {
			@Override
			public void run() {
				Bukkit.dispatchCommand(sender, "title @a title {\"text\":\"1\",\"bold\":true,\"color\":\"green\"}");
			}
		}, 60);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(km, new Runnable() {
			@Override
			public void run() {
				Bukkit.dispatchCommand(sender, "title @a title {\"text\":\"Game Resumed!\",\"bold\":true,\"color\":\"dark_purple\"}");
				km.paused = false;
				
				for (GameTask gt : km.games) {
					gt.enable();
					gt.getPlayer().removePotionEffect(PotionEffectType.INVISIBILITY);
				}
			}
		}, 80);

		return true;
	}

}
