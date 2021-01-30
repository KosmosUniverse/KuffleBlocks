package fr.kosmosuniverse.kuffle.Commands;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import fr.kosmosuniverse.kuffle.KuffleMain;
import fr.kosmosuniverse.kuffle.Core.ActionBar;
import fr.kosmosuniverse.kuffle.Core.GameTask;
import fr.kosmosuniverse.kuffle.utils.Utils;

public class KuffleAdminLoad implements CommandExecutor {
	private KuffleMain km;
	private File dataFolder;
	
	public KuffleAdminLoad(KuffleMain _km, File _dataFolder) {
		km = _km;
		dataFolder = _dataFolder;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if (!(sender instanceof Player))
			return false;
		
		Player player = (Player) sender;
		
		if (!player.hasPermission("kadminload")) {
			player.sendMessage("You are not allowed to do this command.");
			return false;
		}
		
		if (km.games.size() != 0) {
			if (km.games.get(0).getEnable()) {
				player.sendMessage("A game is already launched.");
			} else {
				player.sendMessage("There already areplayers in list.");
			}
			return false;
		}
		
		List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
		File tmp = null;
		
		for (Player p : players) {
			if (dataFolder.getPath().contains("\\")) {
				tmp = new File(dataFolder.getPath() + "\\" + p.getDisplayName());
			} else {
				tmp = new File(dataFolder.getPath() + "/" + p.getDisplayName());
			}
			
			if (tmp.exists()) {
				km.games.add(new GameTask(km, p));
			}
		}
		
		FileReader reader = null;
		JSONParser parser = new JSONParser();
		JSONObject mainObject = new JSONObject();
		
		if (km.config.getSaturation()) {
			for (GameTask gt : km.games) {
				gt.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 999999, 10, false, false, false));
			}
		}
		
		for (GameTask gt : km.games) {
			gt.startRunnable();
		}
		
		for (GameTask gt : km.games) {
			try {
				if (dataFolder.getPath().contains("\\")) {
					reader = new FileReader(dataFolder.getPath() + "\\" + gt.getPlayer().getDisplayName());
				} else {
					reader = new FileReader(dataFolder.getPath() + "/" + gt.getPlayer().getDisplayName());
				}
				
				try {
					mainObject = (JSONObject) parser.parse(reader);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
				gt.loadGame(Integer.parseInt(((Long) mainObject.get("age")).toString()), (String) mainObject.get("current"), (Long) mainObject.get("interval") - 7000, Integer.parseInt(((Long) mainObject.get("time")).toString()), Integer.parseInt(((Long) mainObject.get("blockCount")).toString()), (JSONArray) mainObject.get("alreadyGot"));
				
				reader.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
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
		}, 20);
		Bukkit.getScheduler().scheduleSyncDelayedTask(km, new Runnable() {
			@Override
			public void run() {
				for (GameTask gt : km.games) {
					ActionBar.sendRawTitle("{\"text\":\"4\",\"bold\":true,\"color\":\"gold\"}", gt.getPlayer());
				}
			}
		}, 40);
		Bukkit.getScheduler().scheduleSyncDelayedTask(km, new Runnable() {
			@Override
			public void run() {
				for (GameTask gt : km.games) {
					ActionBar.sendRawTitle("{\"text\":\"3\",\"bold\":true,\"color\":\"yellow\"}", gt.getPlayer());
				}
			}
		}, 60);
		Bukkit.getScheduler().scheduleSyncDelayedTask(km, new Runnable() {
			@Override
			public void run() {
				for (GameTask gt : km.games) {
					ActionBar.sendRawTitle("{\"text\":\"2\",\"bold\":true,\"color\":\"green\"}", gt.getPlayer());
				}
			}
		}, 80);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(km, new Runnable() {
			@Override
			public void run() {
				for (GameTask gt : km.games) {
					ActionBar.sendRawTitle("{\"text\":\"1\",\"bold\":true,\"color\":\"blue\"}", gt.getPlayer());
				}
			}
		}, 100);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(km, new Runnable() {
			@Override
			public void run() {
				for (GameTask gt : km.games) {
					ActionBar.sendRawTitle("{\"text\":\"GO!\",\"bold\":true,\"color\":\"dark_purple\"}", gt.getPlayer());
					gt.enable();
					ActionBar.sendRawTitle("{\"text\":\"GO!\",\"bold\":true,\"color\":\"dark_purple\"}", gt.getPlayer());
				}
				
				km.paused = false;
			}
		}, 120);
		
		return true;
	}
}
