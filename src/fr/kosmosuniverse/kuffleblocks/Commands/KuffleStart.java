package fr.kosmosuniverse.kuffleblocks.Commands;

import java.util.Collections;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.kosmosuniverse.kuffleblocks.KuffleMain;
import fr.kosmosuniverse.kuffleblocks.Core.ActionBar;
import fr.kosmosuniverse.kuffleblocks.Core.GameLoop;
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
		
		km.logs.logMsg(player, Utils.getLangString(km, player.getName(), "CMD_PERF").replace("<#>", "<kb-start>"));
		
		if (!player.hasPermission("kb-start")) {
			km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "NOT_ALLOWED"));
			return false;
		}
		
		if (km.games.size() == 0) {
			km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "NO_PLAYERS"));
			
			return false;
		}
		
		if (km.gameStarted) {
			km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "GAME_LAUNCHED"));
			return false;
		}
		
		if (km.config.getSaturation()) {
			for (String p : km.games.keySet()) {
				km.games.get(p).getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 999999, 10, false, false, false));
			}
		}
		
		int spread = 0;
		
		if (km.config.getTeam() && !checkTeams()) {
			km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "PLAYER_NOT_IN_TEAM"));
			return true;
		}
		
		for (String key : km.allBlocks.keySet()) {
			Collections.shuffle(km.allBlocks.get(key));
		}
		
		for (String playerName : km.games.keySet()) {
			km.games.get(playerName).getPlayer().sendMessage(Utils.getLangString(km, player.getName(), "GAME_STARTED"));
		}
		
		km.logs.logBroadcastMsg(Utils.getLangString(km, player.getName(), "GAME_STARTED"));
		
		if (km.config.getSpread()) {
			if (km.config.getTeam()) {
				SpreadPlayer.spreadPlayers(km, player, (double) km.config.getSpreadDistance(), km.config.getSpreadRadius(), km.teams.getTeams(), Utils.getPlayerList(km.games));	
			} else {
				SpreadPlayer.spreadPlayers(km, player, (double) km.config.getSpreadDistance(), km.config.getSpreadRadius(), null, Utils.getPlayerList(km.games));
			}
			
			for (String playerName : km.games.keySet()) {
				if (km.config.getTeam()) {
					km.games.get(playerName).setTeamName(km.teams.findTeamByPlayer(playerName));
				}
				
				km.games.get(playerName).getPlayer().setBedSpawnLocation(km.games.get(playerName).getPlayer().getLocation(), true);
				km.games.get(playerName).setSpawnLoc(km.games.get(playerName).getPlayer().getLocation());
				km.games.get(playerName).getSpawnLoc().add(0, -1, 0).getBlock().setType(Material.BEDROCK);
			}
			
			spread = 20;
		} else {
			Location spawn = null;

			for (String playerName : km.games.keySet()) {
				if (km.config.getTeam()) {
					km.games.get(playerName).setTeamName(km.teams.findTeamByPlayer(playerName));
				}
				
				if (spawn == null) {
					spawn = km.games.get(playerName).getPlayer().getLocation().getWorld().getSpawnLocation();
				}
				
				km.games.get(playerName).setSpawnLoc(spawn);
			}
			
			spawn.add(0, -1, 0).getBlock().setType(Material.BEDROCK);
		}
		
		int invCnt = 0;
		
		km.playersHeads = Bukkit.createInventory(null, 54, "§8Players");
		
		for (String playerName : km.games.keySet()) {
			km.playersHeads.setItem(invCnt, Utils.getHead(km.games.get(playerName).getPlayer()));

			if (km.config.getTeam() && !km.playerRank.containsKey(km.games.get(playerName).getTeamName())) {
				km.playerRank.put(km.games.get(playerName).getTeamName(), 0);
			} else {
				km.playerRank.put(playerName, 0);
			}
			
			invCnt++;
		}
		
		km.paused = true;
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(km, new Runnable() {
			@Override
			public void run() {
				for (String playerName : km.games.keySet()) {
					ActionBar.sendRawTitle("{\"text\":\"5\",\"bold\":true,\"color\":\"red\"}", km.games.get(playerName).getPlayer());
				}
				
				if (km.config.getSBTT()) {
					Utils.setupTemplates(km);
				}
			}
		}, 20 + spread);
		Bukkit.getScheduler().scheduleSyncDelayedTask(km, new Runnable() {
			@Override
			public void run() {
				for (String playerName : km.games.keySet()) {
					ActionBar.sendRawTitle("{\"text\":\"4\",\"bold\":true,\"color\":\"gold\"}", km.games.get(playerName).getPlayer());
				}
			}
		}, 40 + spread);
		Bukkit.getScheduler().scheduleSyncDelayedTask(km, new Runnable() {
			@Override
			public void run() {
				for (String playerName : km.games.keySet()) {
					ActionBar.sendRawTitle("{\"text\":\"3\",\"bold\":true,\"color\":\"yellow\"}", km.games.get(playerName).getPlayer());
				}
			}
		}, 60 + spread);
		Bukkit.getScheduler().scheduleSyncDelayedTask(km, new Runnable() {
			@Override
			public void run() {
				for (String playerName : km.games.keySet()) {
					ActionBar.sendRawTitle("{\"text\":\"2\",\"bold\":true,\"color\":\"green\"}", km.games.get(playerName).getPlayer());
				}
			}
		}, 80 + spread);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(km, new Runnable() {
			@Override
			public void run() {
				for (String playerName : km.games.keySet()) {
					ActionBar.sendRawTitle("{\"text\":\"1\",\"bold\":true,\"color\":\"blue\"}", km.games.get(playerName).getPlayer());
					km.games.get(playerName).setup();
				}
				
				km.scores.setupPlayerScores();
			}
		}, 100 + spread);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(km, new Runnable() {
			@Override
			public void run() {
				ItemStack box = getStartBox();
				
				for (String playerName : km.games.keySet()) {
					ActionBar.sendRawTitle("{\"text\":\"GO!\",\"bold\":true,\"color\":\"dark_purple\"}", km.games.get(playerName).getPlayer());
					km.games.get(playerName).getPlayer().getInventory().addItem(box);
				}

				km.loop = new GameLoop(km);
				km.loop.startRunnable();
				km.gameStarted = true;
				km.paused = false;
			}
		}, 120 + spread);
		
		return true;
	}
	
	private ItemStack getStartBox() {
		ItemStack item = new ItemStack(Material.WHITE_SHULKER_BOX);
		ItemMeta itM = item.getItemMeta();
		
		itM.setDisplayName("Start Box");
		item.setItemMeta(itM);
		
		return item;
	}

	public boolean checkTeams() {
		for (String playerName : km.games.keySet()) {
			if (!km.teams.isInTeam(playerName)) {
				return false;
			}
		}
		
		return true;
	}
}
