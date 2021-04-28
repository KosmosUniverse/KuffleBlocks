package fr.kosmosuniverse.kuffleblocks.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import fr.kosmosuniverse.kuffleblocks.KuffleMain;
import fr.kosmosuniverse.kuffleblocks.Core.ActionBar;
import fr.kosmosuniverse.kuffleblocks.Core.GameTask;

public class KuffleResume implements CommandExecutor {
	private KuffleMain km;
	
	public KuffleResume(KuffleMain _km) {
		km = _km;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if (!(sender instanceof Player))
			return false;
		
		Player player = (Player) sender;
		
		km.logs.logMsg(player, "achieved command <kresume>");
		
		if (!player.hasPermission("kresume")) {
			km.logs.writeMsg(player, "You are not allowed to do this command.");
			return false;
		}
		
		if (km.games.size() == 0) {
			km.logs.writeMsg(player, "You need to first add people with klist command, launch a game with kstart command.");
			return false;
		}
		
		if (km.games.get(0).getEnable()) {
			km.logs.writeMsg(player, "Your game is already running, you can pause it with kpause command.");
			return false;
		}
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(km, new Runnable() {
			@Override
			public void run() {
				for (GameTask gt : km.games) {
					ActionBar.sendRawTitle("{\"text\":\"3\",\"bold\":true,\"color\":\"red\"}", gt.getPlayer());
				}
			}
		}, 20);
		Bukkit.getScheduler().scheduleSyncDelayedTask(km, new Runnable() {
			@Override
			public void run() {
				for (GameTask gt : km.games) {
					ActionBar.sendRawTitle("{\"text\":\"2\",\"bold\":true,\"color\":\"yellow\"}", gt.getPlayer());
				}
			}
		}, 40);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(km, new Runnable() {
			@Override
			public void run() {
				for (GameTask gt : km.games) {
					ActionBar.sendRawTitle("{\"text\":\"1\",\"bold\":true,\"color\":\"green\"}", gt.getPlayer());
				}
			}
		}, 60);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(km, new Runnable() {
			@Override
			public void run() {
				for (GameTask gt : km.games) {
					ActionBar.sendRawTitle("{\"text\":\"Game Resumed!\",\"bold\":true,\"color\":\"dark_purple\"}", gt.getPlayer());
				}

				km.paused = false;
				
				for (GameTask gt : km.games) {
					gt.enable();
					ActionBar.sendRawTitle("{\"text\":\"Game Resumed!\",\"bold\":true,\"color\":\"dark_purple\"}", gt.getPlayer());
					gt.getPlayer().removePotionEffect(PotionEffectType.INVISIBILITY);
				}
			}
		}, 80);

		return true;
	}

}
