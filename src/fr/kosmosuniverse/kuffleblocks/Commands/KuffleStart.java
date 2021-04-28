package fr.kosmosuniverse.kuffleblocks.Commands;

import java.util.Collections;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;

import fr.kosmosuniverse.kuffleblocks.KuffleMain;
import fr.kosmosuniverse.kuffleblocks.Core.ActionBar;
import fr.kosmosuniverse.kuffleblocks.Core.GameTask;
import fr.kosmosuniverse.kuffleblocks.Core.SpreadPlayer;
import fr.kosmosuniverse.kuffleblocks.utils.Utils;

public class KuffleStart implements CommandExecutor {
	private KuffleMain km;

	public KuffleStart(KuffleMain _km) {
		km = _km;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if (!(sender instanceof Player))
			return false;
		
		Player player = (Player) sender;
		
		km.logs.logMsg(player, "achieved command <kstart>");
		
		if (!player.hasPermission("kstart")) {
			km.logs.writeMsg(player, "You are not allowed to do this command.");
			return false;
		}
		
		if (km.games.size() == 0) {
			km.logs.writeMsg(player, "You need to first add people with klist command.");
			return false;
		}
		
		if (km.games.get(0).getEnable()) {
			km.logs.writeMsg(player, "The game is already running.");
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
		
		if (km.config.getTeam() && !checkTeams()) {
			km.logs.writeMsg(player, "Team are enabled and not all players are in a Team.");
			return true;
		}
		
		if (km.config.getSame()) {
			for (String key : km.allBlocks.keySet()) {
				Collections.shuffle(km.allBlocks.get(key));
			}
		}
		
		km.logs.logBroadcastMsg("Game Started, please wait for the countdown.");
		
		if (km.config.getSpread()) {
			if (km.config.getTeam()) {
				SpreadPlayer.spreadPlayers(player, (double) km.config.getSpreadDistance(), km.config.getSpreadRadius(), km.teams.getTeams(), Utils.getPlayerList(km.games));	
			} else {
				SpreadPlayer.spreadPlayers(player, (double) km.config.getSpreadDistance(), km.config.getSpreadRadius(), null, Utils.getPlayerList(km.games));
			}
			
			for (GameTask gt : km.games) {
				if (km.config.getTeam()) {
					gt.setTeamName(km.teams.findTeamByPlayer(gt.getPlayer().getDisplayName()));
				}
				
				gt.getPlayer().setBedSpawnLocation(gt.getPlayer().getLocation(), true);
				gt.setSpawnLoc(gt.getPlayer().getLocation());
				gt.getSpawnLoc().add(0, -1, 0).getBlock().setType(Material.BEDROCK);
			}
			
			spread = 20;
		} else {
			Location spawn = km.games.get(0).getPlayer().getLocation().getWorld().getSpawnLocation();
			
			spawn.add(0, -1, 0).getBlock().setType(Material.BEDROCK);
			
			for (GameTask gt : km.games) {
				if (km.config.getTeam()) {
					gt.setTeamName(km.teams.findTeamByPlayer(gt.getPlayer().getDisplayName()));
				}
				
				gt.setSpawnLoc(spawn);
			}
		}
		
		int invCnt = 0;
		
		km.playersHeads = Bukkit.createInventory(null, 54, "�8Players");
		
		for (GameTask gt : km.games) {
			km.playerRank.put(gt.getPlayer().getDisplayName(), false);
			km.playersHeads.setItem(invCnt, Utils.getHead(gt.getPlayer()));
			gt.setDeathLoc(null);
			
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

	public boolean checkTeams() {
		for (GameTask gt : km.games) {
			if (!km.teams.isInTeam(gt.getPlayer().getDisplayName())) {
				return false;
			}
		}
		
		return true;
	}
}