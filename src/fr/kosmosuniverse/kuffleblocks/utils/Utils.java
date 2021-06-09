package fr.kosmosuniverse.kuffleblocks.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffectType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import fr.kosmosuniverse.kuffleblocks.Core.AgeManager;
import fr.kosmosuniverse.kuffleblocks.Core.BlockManager;
import fr.kosmosuniverse.kuffleblocks.Core.Game;
import fr.kosmosuniverse.kuffleblocks.Core.LangManager;
import fr.kosmosuniverse.kuffleblocks.Crafts.Template;
import fr.kosmosuniverse.kuffleblocks.KuffleMain;

public class Utils {
	public static String readFileContent(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();
 
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line + System.lineSeparator());
        }
 
        return sb.toString();
	}
	
	public static boolean fileExists(String path, String fileName) {
		File tmp = null;
		
		if (path.contains("\\")) {
			tmp = new File(path + "\\" + fileName);
		} else {
			tmp = new File(path + "/" + fileName);
		}
		
		return tmp.exists();
	}
	
	public static boolean fileExists(String fileName) {
		File tmp = null;
		
		tmp = new File(fileName);
		
		return tmp.exists();
	}
	
	public static boolean fileDelete(String path, String fileName) {
		File tmp = null;
		
		if (path.contains("\\")) {
			tmp = new File(path + "\\" + fileName);
		} else {
			tmp = new File(path + "/" + fileName);
		}
		
		return tmp.delete();
	}
	
	public static int playerLasts(KuffleMain km) {
		int notEnded = 0;
		
		for (String playerName : km.games.keySet()) {
			if (!km.games.get(playerName).getFinished()) {
				notEnded++;
			}
		}
		
		return notEnded;
	}
	
	public static void forceFinish(KuffleMain km, int gameRank) {
		for (String playerName : km.games.keySet()) {
			if (!km.games.get(playerName).getFinished()) {
				km.games.get(playerName).finish(gameRank);
			}
		}
	}
	
	public static void loadGame(KuffleMain _km, Player player) {
		FileReader reader = null;
		JSONParser parser = new JSONParser();
		Game tmpGame = new Game(_km, player);
		
		tmpGame.setup();
		
		try {
			if (_km.getDataFolder().getPath().contains("\\")) {
				reader = new FileReader(_km.getDataFolder().getPath() + "\\" + player.getName() + ".kb");
			} else {
				reader = new FileReader(_km.getDataFolder().getPath() + "/" + player.getName() + ".kb");
			}
			
			JSONObject mainObject = (JSONObject) parser.parse(reader);
			
			tmpGame.setAge(Integer.parseInt(((Long) mainObject.get("age")).toString()));
			tmpGame.setCurrentBlock((String) mainObject.get("current"));
			tmpGame.setTimeShuffle(System.currentTimeMillis() - (Long) mainObject.get("interval"));
			tmpGame.setTime(Integer.parseInt(((Long) mainObject.get("time")).toString()));
			tmpGame.setDead((boolean) mainObject.get("isDead"));
			tmpGame.setFinished((boolean) mainObject.get("finished"));
			tmpGame.setLose((boolean) mainObject.get("lose"));
			tmpGame.setBlockCount(Integer.parseInt(((Long) mainObject.get("blockCount")).toString()));
			tmpGame.setTeamName((String) mainObject.get("teamName"));
			tmpGame.setAlreadyGot((JSONArray) mainObject.get("alreadyGot"));
			tmpGame.setSpawnLoc((JSONObject) mainObject.get("spawn"));
			tmpGame.setDeathLoc((JSONObject) mainObject.get("death"));
			tmpGame.setSameIdx(Integer.parseInt(((Long) mainObject.get("sameIdx")).toString()));
			tmpGame.setTimes((JSONObject) mainObject.get("times"));
			tmpGame.setDeathCount(Integer.parseInt(mainObject.get("deathCount").toString()));
			tmpGame.setSkipCount(Integer.parseInt(mainObject.get("skipCount").toString()));
			
			if (fileExists(_km.getDataFolder().getPath(), player.getName() + ".yml")) {
				tmpGame.loadInventory();
				fileDelete(_km.getDataFolder().getPath(), player.getName() + ".yml");
			}

			if (tmpGame.getDead()) {
				_km.playerEvents.teleportAutoBack(tmpGame);
			}
			
			_km.games.put(player.getName(), tmpGame);
		} catch (ParseException | IOException e) {
			e.printStackTrace();
		}
		
		return ;
	}
	
	public static ItemStack getHead(Player player) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skull = (SkullMeta) item.getItemMeta();
        
        skull.setDisplayName(player.getName());
        skull.setOwningPlayer(player);
        item.setItemMeta(skull);
        
        return item;
    }
	
	public static String getVersion() {
		String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
		
		version = version.split("v")[1];
		version = version.split("_")[0] + "." + version.split("_")[1];
		
		return version;
	}
	
	public static ChatColor findChatColor(String color) {
		for (ChatColor item : ChatColor.values()) {
			if (item.name().equals(color)) {
				return item;
			}
		}
		
		return null;
	}
	
	public static ArrayList<Player> getPlayerList(HashMap<String, Game> games) {
		ArrayList<Player> players = new ArrayList<Player>();
		
		for (String playerName : games.keySet()) {
			players.add(games.get(playerName).getPlayer());
		}
		
		return players;
	}
	
	public static ArrayList<String> getPlayerNames(HashMap<String, Game> games) {
		ArrayList<String> players = new ArrayList<String>();
		
		for (String playerName : games.keySet()) {
			players.add(playerName);
		}
		
		return players;
	}
	
	public static World findNormalWorld() {
		for (World w : Bukkit.getWorlds()) {
			if (!w.getName().contains("nether") && !w.getName().contains("the_end")) {
				return w;
			}
		}
		
		return null;
	}
	
	public static boolean checkEffect(String effect) {
		for (PotionEffectType potion : PotionEffectType.values()) {
			if (potion.getName().equalsIgnoreCase(effect)) {
				return true;
			}
		}
			
		return false;
	}
	
	public static void setupTemplates(KuffleMain km) {
		ArrayList<Template> templates = new ArrayList<Template>();
		
		for (int i = 0; i < km.config.getMaxAges(); i++)  {
			String name = AgeManager.getAgeByNumber(km.ages, i).name;
			
			name = name.replace("_Age", "");
			templates.add(new Template(km, name, getMaterials(AgeManager.getAgeByNumber(km.ages, i).name, km)));
		}
		
		for (Template t : templates) {
			km.crafts.addCraft(t);
			km.addRecipe(t.getRecipe());
		}
	}
	
	public static void removeTemplates(KuffleMain km) {
		for (int i = 0; i < km.config.getMaxAges(); i++)  {
			String name = AgeManager.getAgeByNumber(km.ages, i).name;
			name = name.replace("_Age", "");
			name = name + "Template";
			
			km.removeRecipe(name);
			km.crafts.removeCraft(name);
		}
	}
	
	public static ArrayList<Material> getMaterials(String age, KuffleMain km) {
		ArrayList<Material> compose = new ArrayList<Material>();
		ArrayList<String> done = new ArrayList<String>();
		
		for (int cnt = 0; cnt < km.config.getSBTTAmount(); cnt++) {
			done.add(BlockManager.newBlock(done, km.allSbtts.get(age)));
		}
		
		for (String item : done) {
			compose.add(Material.matchMaterial(item));
		}
		
		done.clear();
		return compose;
	}
	
	public static void reloadTemplate(KuffleMain km, String name, String age) {
		km.crafts.removeCraft(name);
		km.removeRecipe(name);
		
		String tmp = age;
		
		tmp = tmp.replace("_Age", "");
		
		Template t = new Template(km, tmp, getMaterials(age, km));
		
		km.crafts.addCraft(t);
		km.addRecipe(t.getRecipe());
		
		for (String playerName : km.games.keySet()) {
			km.games.get(playerName).getPlayer().discoverRecipe(new NamespacedKey(km, t.getName()));
		}
	}
	
	public static String getTimeFromSec(long sec) {
		StringBuilder sb = new StringBuilder();
		
		if (sec != 0 && sec >= 3600) {
			sb.append(sec / 3600);
			sb.append("h");
			
			sec = sec % 3600;
		}
		
		if (sec != 0 && sec >= 60) {
			sb.append(sec / 60);
			sb.append("m");
			
			sec = sec % 60;
		}
	
		sb.append(sec);
		sb.append("s");
		
		return sb.toString();
	}
	
	public static boolean compareItems(ItemStack first, ItemStack second, boolean hasItemMeta, boolean hasDisplayName, boolean hasLore) {
		if (first.getType() != second.getType()) {
			return false;
		}
		
		if (first.hasItemMeta() != second.hasItemMeta()) {
			return false;
		}
		
		if (first.hasItemMeta() != hasItemMeta) {
			return false;
		}
		
		if (!hasItemMeta) {
			return true;
		}
		
		ItemMeta firstMeta = first.getItemMeta();
		ItemMeta secondMeta = second.getItemMeta();
		
		if (firstMeta.hasDisplayName() != secondMeta.hasDisplayName()) {
			return false;
		}
		
		if (firstMeta.hasDisplayName() != hasDisplayName) {
			return false;
		}
		
		if (!hasDisplayName) {
			return true;
		}
		
		if (!firstMeta.getDisplayName().equals(secondMeta.getDisplayName())) {
			return false;
		}
		
		if (firstMeta.hasLore() != secondMeta.hasLore()) {
			return false;
		}
		
		if (firstMeta.hasLore() != hasLore) {
			return false;
		}
		
		if (!hasLore) {
			return true;
		}
		
		ArrayList<String> firstLore = (ArrayList<String>) firstMeta.getLore();
		ArrayList<String> secondLore = (ArrayList<String>) secondMeta.getLore();
		
		if (firstLore.size() != secondLore.size()) {
			return false;
		}
		
		for (int i = 0; i < firstLore.size(); i++) {
			if (!firstLore.get(i).equals(secondLore.get(i))) {
				return false;
			}
		}
		
		return true;
	}
	
	public static String getLangString(KuffleMain km, String player, String tag) {
		if (km.gameStarted && player != null && km.games.containsKey(player)) {
			return (LangManager.findDisplay(km.allLangs, tag, km.games.get(player).getLang()));
		} else {
			return (LangManager.findDisplay(km.allLangs, tag, km.config.getLang()));
		}
	}
}
 