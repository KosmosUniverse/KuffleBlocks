package fr.kosmosuniverse.kuffle.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;

import fr.kosmosuniverse.kuffle.KuffleMain;
import fr.kosmosuniverse.kuffle.Core.ActionBar;
import fr.kosmosuniverse.kuffle.Core.GameTask;
import fr.kosmosuniverse.kuffle.utils.Utils;

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
		
		if (km.config.getSaturation()) {
			for (GameTask gt : km.games) {
				gt.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 999999, 10, false, false, false));
			}
		}
		
		for (GameTask gt : km.games) {
			km.playerRank.put(gt.getPlayer().getDisplayName(), false);
		}
		
		int spread = 0;
		
		if (km.config.getSpread()) {
			Bukkit.dispatchCommand(sender, "spreadplayers " + p.getLocation().getBlockX() + " " + p.getLocation().getBlockZ() + " " + (km.config.getSpreadMin() * km.games.size()) + " " + (km.config.getSpreadMax() * km.games.size()) + " false @a");
			
			for (GameTask gt : km.games) {
				if (gt.getPlayer().isOp()) {
					gt.getPlayer().performCommand("spawnpoint");
				} else {
					gt.getPlayer().setBedSpawnLocation(gt.getPlayer().getLocation(), true);
				}
			}
			spread = 20;
		}
		
		int invCnt = 0;
		
		km.playersHeads = Bukkit.createInventory(null, 54, "§8Teleport");
		
		for (GameTask gt : km.games) {
			km.playerRank.put(gt.getPlayer().getDisplayName(), false);
			km.playersHeads.setItem(invCnt, Utils.getHead(gt.getPlayer()));
			
			invCnt++;
		}
		
		km.paused = true;
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(km, new Runnable() {
			@Override
			public void run() {
				for (GameTask gt : km.games) {
					ActionBar.sendRawTitle("{\"text\":\"5\",\"bold\":true,\"color\":\"red\"}", gt.getPlayer());
				}
			}
		}, 20 + spread);
		Bukkit.getScheduler().scheduleSyncDelayedTask(km, new Runnable() {
			@Override
			public void run() {
				for (GameTask gt : km.games) {
					ActionBar.sendRawTitle("{\"text\":\"4\",\"bold\":true,\"color\":\"gold\"}", gt.getPlayer());
				}
			}
		}, 40 + spread);
		Bukkit.getScheduler().scheduleSyncDelayedTask(km, new Runnable() {
			@Override
			public void run() {
				for (GameTask gt : km.games) {
					ActionBar.sendRawTitle("{\"text\":\"3\",\"bold\":true,\"color\":\"yellow\"}", gt.getPlayer());
				}
			}
		}, 60 + spread);
		Bukkit.getScheduler().scheduleSyncDelayedTask(km, new Runnable() {
			@Override
			public void run() {
				for (GameTask gt : km.games) {
					ActionBar.sendRawTitle("{\"text\":\"2\",\"bold\":true,\"color\":\"green\"}", gt.getPlayer());
				}
			}
		}, 80 + spread);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(km, new Runnable() {
			@Override
			public void run() {
				for (GameTask gt : km.games) {
					ActionBar.sendRawTitle("{\"text\":\"1\",\"bold\":true,\"color\":\"blue\"}", gt.getPlayer());
				}
				
				if (km.config.getSeeBlockCnt()) {
					km.scores.setupPlayerScores(DisplaySlot.PLAYER_LIST);
				} else {
					km.scores.setupPlayerScores(DisplaySlot.BELOW_NAME);
				}
			}
		}, 100 + spread);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(km, new Runnable() {
			@Override
			public void run() {
				for (GameTask gt : km.games) {
					gt.startRunnable();
				}
				
				for (GameTask gt : km.games) {
					gt.enable();
					ActionBar.sendRawTitle("{\"text\":\"GO!\",\"bold\":true,\"color\":\"dark_purple\"}", gt.getPlayer());
				}
				
				km.paused = false;
			}
		}, 120 + spread);
		
		return true;
	}

}
