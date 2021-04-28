package fr.kosmosuniverse.kuffleblocks.Listeners;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import fr.kosmosuniverse.kuffleblocks.KuffleMain;
import fr.kosmosuniverse.kuffleblocks.Core.GameTask;
import fr.kosmosuniverse.kuffleblocks.Core.Level;
import fr.kosmosuniverse.kuffleblocks.utils.Utils;

public class PlayerEventListener implements Listener {
	private KuffleMain km;
	private File dataFolder;
	
	public PlayerEventListener(KuffleMain _km, File _dataFolder) {
		km = _km;
		dataFolder = _dataFolder;
	}
	
	@EventHandler
	public void onPlayerConnectEvent(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		JSONParser parser = new JSONParser();
		boolean enable;

		if (km.games.size() == 0) {
			return;
		} else {
			enable = km.games.get(0).getEnable();
		}
		
		File tmp = null;
		
		if (dataFolder.getPath().contains("\\")) {
			tmp = new File(dataFolder.getPath() + "\\" + player.getDisplayName());
		} else {
			tmp = new File(dataFolder.getPath() + "/" + player.getDisplayName());
		}
		
		if (tmp.exists()) {
			km.games.add(new GameTask(km, player));
		}
		
		FileReader reader = null;
		
		try {
			if (dataFolder.getPath().contains("\\")) {
				reader = new FileReader(dataFolder.getPath() + "\\" + player.getDisplayName());
			} else {
				reader = new FileReader(dataFolder.getPath() + "/" + player.getDisplayName());
			}
			
			JSONObject mainObject = (JSONObject) parser.parse(reader);
			
			for (GameTask gt : km.games) {
				if (gt.getPlayer().getDisplayName().equals(player.getDisplayName())) {
					gt.startRunnable();
					gt.loadGame(Integer.parseInt(((Long) mainObject.get("age")).toString()), Integer.parseInt(mainObject.get("maxAge").toString()), (String) mainObject.get("current"), (Long) mainObject.get("interval"), Integer.parseInt(((Long) mainObject.get("time")).toString()), Integer.parseInt(((Long) mainObject.get("blockCount")).toString()), Integer.parseInt(mainObject.get("sameIdx").toString()), (String) mainObject.get("teamName"), (JSONArray) mainObject.get("alreadyGot"), (JSONObject) mainObject.get("spawn"), (JSONObject) mainObject.get("death"));
					if (enable) {
						gt.enable();
					} else {
						gt.disable();
					}

					Inventory newInv = Bukkit.createInventory(null, 54, "§8Players");
					
					for (ItemStack item : km.playersHeads) {
						if (item != null) {
							newInv.addItem(item);
						}
					}
					
					newInv.addItem(Utils.getHead(gt.getPlayer()));
					
					km.playersHeads = newInv;
					
					return;
				}
			}
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}
	
	@EventHandler
	public void onPlayerDeconnectEvent(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		GameTask gt;
		
		if (km.games.size() == 0) {
			return ;	
		}
		
		if ((gt = playerIsInGame(player.getDisplayName())) != null) {	
			FileWriter writer = null;
			try {
				if (dataFolder.getPath().contains("\\")) {
					writer = new FileWriter(dataFolder.getPath() + "\\" + player.getDisplayName());
				} else {
					writer = new FileWriter(dataFolder.getPath() + "/" + player.getDisplayName());
				}
				
				Inventory newInv = Bukkit.createInventory(null, 54, "§8Players");
				
				for (ItemStack item : km.playersHeads.getContents()) {
					if (item != null) {
						if (!item.getItemMeta().getDisplayName().equals(player.getDisplayName())) {
							newInv.addItem(item);
						}
					}
				}
				
				km.playersHeads = newInv;
				
				gt.disable();
				writer.write(gt.saveGame());
				writer.close();
				gt.exit();
				gt.kill();
				
				km.games.remove(gt);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@EventHandler
	public void onPlayerDeathEvent(PlayerDeathEvent event) {
		Player player = event.getEntity();
		Location deathLoc = player.getLocation();
		event.setKeepInventory(true);
		
		if (event.getDrops().size() > 0) {	
			event.getDrops().clear();
		}
		
		for (GameTask gt : km.games) {
			if (gt.getPlayer().getName().equals(player.getDisplayName())) {
				if (km.config.getLevel() == Level.ULTRA) {
					gt.setExit(true);
				} else {
					gt.setDeathLoc(deathLoc);
					gt.savePlayerInv();
				}
				return;
			}
		}
	}
	
	@EventHandler
	public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		
		for (GameTask gt : km.games) {
			if (gt.getPlayer().getName().equals(player.getDisplayName())) {
				event.setRespawnLocation(gt.getSpawnLoc());
				player.getInventory().clear();
			}
		}
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(km, new Runnable() {
			@Override
			public void run() {
				for (GameTask gt : km.games) {
					if (gt.getPlayer().getName().equals(player.getDisplayName())) {
						if (km.config.getLevel() != Level.ULTRA) {
							gt.reloadEffects();
							gt.setDeathTime(System.currentTimeMillis(), Utils.minSecondsWithLevel(km.config.getLevel()), Utils.maxSecondsWithLevel(km.config.getLevel()));
							player.sendMessage("You can tp back to your death spot in " + gt.getMinTime() + " seconds. In " + gt.getMaxTime() + " seconds your stuff will be destroyed");
						}
						return;
					}
				}
			}
		}, 20);
	}
	
	@EventHandler
	public void onFireWorkThrow(PlayerInteractEvent event) {
		ItemStack item;
		Action action = event.getAction();
		Player player = event.getPlayer();

		if (km.games.size() == 0 || !km.games.get(0).getEnable() || playerIsInGame(player.getDisplayName()) == null) {
			return ;
		}
		
		if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) {
			return ;
		}
		
		if (event.getItem() != null && event.getItem().getType() == Material.FIREWORK_ROCKET) {
			item = event.getItem();
			
			if (item.getAmount() == 1) {
				item.setAmount(64);
				player.getInventory().setItemInMainHand(item);
			}
		} else if (player.getInventory().getItemInOffHand() != null && player.getInventory().getItemInOffHand().getType() == Material.FIREWORK_ROCKET) {
			item = player.getInventory().getItemInOffHand();
			
			if (item.getAmount() == 1) {
				item.setAmount(64);
				player.getInventory().setItemInOffHand(item);
			}
		} else {
			return;
		}
	}
	
	private GameTask playerIsInGame(String name) {
		for (GameTask gt : km.games) {
			if (gt.getPlayer().getName().equals(name)) {
				return gt;
			}
		}
		
		return null;
	}
}
