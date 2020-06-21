package fr.kosmosuniverse.kuffle.Listeners;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import fr.kosmosuniverse.kuffle.KuffleMain;
import fr.kosmosuniverse.kuffle.Core.GameTask;

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
		
		km.listTab.reset();
		km.skipTab.reset();
		km.validateTab.reset();
		
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
					gt.loadGame(Integer.parseInt(((Long) mainObject.get("age")).toString()), (String) mainObject.get("current"), (Long) mainObject.get("interval"), Integer.parseInt(((Long) mainObject.get("time")).toString()), Integer.parseInt(((Long) mainObject.get("blockCount")).toString()), (JSONArray) mainObject.get("alreadyGot"));
					if (enable) {
						gt.enable();
					} else {
						gt.disable();
					}
					
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
		
		km.listTab.reset();
		km.skipTab.reset();
		km.validateTab.reset();
		
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
	
	
	
	private GameTask playerIsInGame(String name) {
		for (GameTask gt : km.games) {
			if (gt.getPlayer().getName().equals(name)) {
				return gt;
			}
		}
		
		return null;
	}
}
