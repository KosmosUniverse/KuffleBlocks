package fr.kosmosuniverse.kuffleblocks.Listeners;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.kosmosuniverse.kuffleblocks.KuffleMain;
import fr.kosmosuniverse.kuffleblocks.Core.Game;
import fr.kosmosuniverse.kuffleblocks.Crafts.ACrafts;
import fr.kosmosuniverse.kuffleblocks.utils.Utils;

public class PlayerEvents implements Listener {
	private KuffleMain km;
	private File dataFolder;
	private ArrayList<Material> exceptions;
	
	public PlayerEvents(KuffleMain _km, File _dataFolder) {
		km = _km;
		dataFolder = _dataFolder;

		exceptions = new ArrayList<Material>();
		
		for (Material m : Material.values()) {
			if (m.name().contains("SHULKER_BOX")) {
				exceptions.add(m);
			}
		}
		
		exceptions.add(Material.CRAFTING_TABLE);
		exceptions.add(Material.FURNACE);
		exceptions.add(Material.STONECUTTER);
	}
	
	@EventHandler
	public void onPlayerConnectEvent(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		Game tmpGame;

		for (ACrafts item : km.crafts.getRecipeList()) {
			player.discoverRecipe(new NamespacedKey(km, item.getName()));
		}
	
		if (!km.gameStarted) {
			return;
		}
		
		if (Utils.fileExists(dataFolder.getPath(), player.getName() + ".kb")) {
			Utils.loadGame(km, player);
		} else {
			return;
		}
		
		tmpGame = km.games.get(player.getName());

		Inventory newInv = Bukkit.createInventory(null, 54, "§8Players");
		
		for (ItemStack item : km.playersHeads) {
			if (item != null) {
				newInv.addItem(item);
			}
		}
		
		newInv.addItem(Utils.getHead(tmpGame.getPlayer()));
		
		km.playersHeads = newInv;
		km.scores.setupPlayerScores(tmpGame);
		tmpGame.load();
		km.updatePlayersHead(player.getName(), tmpGame.getBlockDisplay());
		
		for (String playerName : km.games.keySet()) {
			km.games.get(playerName).getPlayer().sendMessage("[KuffleBlocks] : <" + player.getName() + "> game is reloaded !");
		}
		
		km.logs.logBroadcastMsg("[KuffleBlocks] : <" + player.getName() + "> game is reloaded !");
		
		return;
	}
	
	@EventHandler
	public void onPlayerDeconnectEvent(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		FileWriter writer = null;
		Game tmpGame;
		
		if (!km.gameStarted || !km.games.containsKey(player.getName())) {
			return ;
		}
		
		tmpGame = km.games.remove(player.getName());

		try {
			if (dataFolder.getPath().contains("\\")) {
				writer = new FileWriter(dataFolder.getPath() + "\\" + player.getName() + ".kb");
			} else {
				writer = new FileWriter(dataFolder.getPath() + "/" + player.getName() + ".kb");
			}
			
			Inventory newInv = Bukkit.createInventory(null, 54, "§8Players");
			
			for (ItemStack item : km.playersHeads.getContents()) {
				if (item != null) {
					if (!item.getItemMeta().getDisplayName().equals(player.getName())) {
						newInv.addItem(item);
					}
				}
			}
			
			km.playersHeads = newInv;
			
			writer.write(tmpGame.save());
			writer.close();
			
			tmpGame.stop();
			
			for (String playerName : km.games.keySet()) {
				km.games.get(playerName).getPlayer().sendMessage("[KuffleBlocks] : <" + player.getName() + "> game is saved.");
			}
			km.logs.logBroadcastMsg("[KuffleBlocks] : <" + player.getName() + "> game is saved.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@EventHandler
	public void onPlayerDeathEvent(PlayerDeathEvent event) {
		if (!km.gameStarted) {
			return ;
		}
		
		Player player = event.getEntity();
		Location deathLoc = player.getLocation();
		event.setKeepInventory(true);
		
		if (event.getDrops().size() > 0) {	
			event.getDrops().clear();
		}
		
		km.logs.logMsg(player, "just died.");
		
		for (String playerName : km.games.keySet()) {
			if (playerName.equals(player.getName())) {
				if (km.config.getLevel().losable) {
					km.games.get(playerName).setLose(true);
				} else {
					km.games.get(playerName).setDeathLoc(deathLoc);
					km.games.get(playerName).savePlayerInv();
					km.games.get(playerName).setDead(true);
				}
				
				return ;
			}
		}
	}
	
	@EventHandler
	public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
		if (!km.gameStarted) {
			return ;
		}
		
		Player player = event.getPlayer();
		
		km.logs.logMsg(player, "just respawned.");
		
		for (String playerName : km.games.keySet()) {
			if (playerName.equals(player.getName())) {
				event.setRespawnLocation(km.games.get(playerName).getSpawnLoc());
				player.getInventory().clear();
			}
		}
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(km, new Runnable() {
			@Override
			public void run() {
				for (String playerName : km.games.keySet()) {
					if (playerName.equals(player.getName())) {
						if (km.config.getLevel().losable) {
							player.sendMessage(ChatColor.RED + "YOU LOSE !");
						} else {
							teleportAutoBack(km.games.get(playerName));
							km.games.get(playerName).getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999, 1));
							km.games.get(playerName).getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 999999, 10));
						}
						
						return;
					}
				}
			}
		}, 20);
	}
	
	public void teleportAutoBack(Game tmpGame) {
		tmpGame.getPlayer().sendMessage("You will be tp back to your death spot in " + km.config.getLevel().seconds + " seconds.");
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(km, new Runnable() {
			@Override
			public void run() {
				Location loc = tmpGame.getDeathLoc();
				
				if (loc.getWorld().getName().contains("the_end") && loc.getY() < 0) {
					int tmp = loc.getWorld().getHighestBlockYAt(loc);
					
					if (tmp == -1) {
						loc.setY(59);
						
						for (double cntX = -2; cntX <= 2; cntX++) {
							for (double cntZ = -2; cntZ <= 2; cntZ++) {
								Location platform = loc.clone();
								
								platform.add(cntX, 0, cntZ);
								
								platform.getBlock().setType(Material.COBBLESTONE);
							}
						}
						
						loc.setY(61);
					} else {
						loc.setY(loc.getWorld().getHighestBlockYAt(loc) + 1);
					}
				} else {
					Location wall;
					
					for (double x = -2; x <= 2; x++) {
						for (double y = -2; y <= 2; y++) {
							for (double z = -2; z <= 2; z++) {
								wall = loc.clone();
								wall.add(x, y, z);
								
								if (x == 0 && y == -1 && z == 0) {
									setSign(wall, tmpGame.getPlayer().getName());
								} else if (x <= 1 && x >= -1 && y <= 1 && y >= -1 && z <= 1 && z >= -1) {
									replaceExeption(wall, Material.AIR);
								} else {
									replaceExeption(wall, Material.DIRT);
								}
							}
						}
					}
				}
				
				tmpGame.getPlayer().teleport(loc);
				
				for (Entity e : tmpGame.getPlayer().getNearbyEntities(3.0, 3.0, 3.0)) {
					e.remove();
				}
				
				tmpGame.restorePlayerInv();

				for (PotionEffect p : tmpGame.getPlayer().getActivePotionEffects()) {
					tmpGame.getPlayer().removePotionEffect(p.getType());
				}
				
				tmpGame.reloadEffects();
			}
		}, (km.config.getLevel().seconds * 20));
	}
	
	private void replaceExeption(Location loc, Material m) {
		if (!exceptions.contains(loc.getBlock().getType())) {
			loc.getBlock().setType(m);
		}
	}
	
	private void setSign(Location loc, String playerName) {
		if (!exceptions.contains(loc.getBlock().getType())) {
			loc.getBlock().setType(Material.OAK_SIGN);
			
			Sign sign = (Sign) loc.getBlock().getState();
			
			sign.setLine(0, "[KuffleBlocks]");
			sign.setLine(1, Utils.getLangString(km, null, "HERE_DIES"));
			sign.setLine(2, playerName);
			sign.update(true);
		}
	}
	
	@EventHandler
	public void onPauseEvent(PlayerMoveEvent event) {
		if (km.paused) {
			event.setCancelled(true);
			return ;
		}
	}
}
