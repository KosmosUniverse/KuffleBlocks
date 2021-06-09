package fr.kosmosuniverse.kuffleblocks.Core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Score;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import fr.kosmosuniverse.kuffleblocks.KuffleMain;
import fr.kosmosuniverse.kuffleblocks.utils.Utils;
import net.md_5.bungee.api.ChatColor;

public class Game {
	private KuffleMain km;
	private ArrayList<String> alreadyGot;
	private HashMap<String, Long> times;
	
	private boolean finished;
	private boolean lose;
	private boolean dead;
	
	private int time;
	private int blockCount = 1;
	private int age = 0;
	private int gameRank = -1;
	private int sameIdx = 0;
	
	private int deathCount = 0;
	private int skipCount = 0;
	private int sbttCount = 0;
	
	private long timeShuffle = -1;
	private long interval = -1;
	private long timeBase;
	
	private String currentblock;
	private String blockDisplay;
	private String configLang;
	private String teamName;
	
	private Location spawnLoc;
	private Location deathLoc;
	
	private Player player;
	private Inventory deathInv = null;
	private Score blockScore;
	private BossBar ageDisplay;
	
	public Game(KuffleMain _km, Player _player) {
		km = _km;
		player = _player;
		finished = false;
		lose = false;
		dead = false;
	}
	
	public void setup() {
		time = km.config.getStartTime();
		timeBase = System.currentTimeMillis();
		times = new HashMap<String, Long>();
		alreadyGot = new ArrayList<String>();
		configLang = km.config.getLang();
		ageDisplay = Bukkit.createBossBar(Utils.getLangString(km, player.getName(), "START"), BarColor.PURPLE, BarStyle.SOLID);
		ageDisplay.addPlayer(player);
		deathLoc = null;
		updateBar();
	}
	
	public void stop() {
		for (PotionEffect pe : player.getActivePotionEffects()) {
			player.removePotionEffect(pe.getType());
		}
		
		resetBar();
		alreadyGot.clear();
	}
	
	@SuppressWarnings("unchecked")
	public String save() {
		JSONObject jsonSpawn = new JSONObject();
		
		jsonSpawn.put("World", spawnLoc.getWorld().getName());
		jsonSpawn.put("X", spawnLoc.getX());
		jsonSpawn.put("Y", spawnLoc.getY());
		jsonSpawn.put("Z", spawnLoc.getZ());
		
		JSONObject jsonDeath = new JSONObject();
		if (deathLoc == null) {
			jsonDeath = null;
		} else {
			jsonDeath.put("World", deathLoc.getWorld().getName());
			jsonDeath.put("X", deathLoc.getX());
			jsonDeath.put("Y", deathLoc.getY());
			jsonDeath.put("Z", deathLoc.getZ());
		}
		
		JSONObject global = new JSONObject();
		
		if (deathInv != null) {
			try {
				saveInventory();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		global.put("age", age);
		global.put("maxAge", km.config.getMaxAges());
		global.put("current", currentblock);
		global.put("interval", System.currentTimeMillis() - timeShuffle);
		global.put("time", time);
		global.put("isDead", dead);
		global.put("blockCount", blockCount);
		global.put("spawn", jsonSpawn);
		global.put("death", jsonDeath);
		global.put("teamName", teamName);
		global.put("sameIdx", sameIdx);
		global.put("deathCount", deathCount);
		global.put("skipCount", skipCount);
		global.put("finished", finished);
		global.put("lose", lose);
		
		JSONArray got = new JSONArray();
		
		for (String block : alreadyGot) {
			got.add(block);
		}
		
		global.put("alreadyGot", got);
		
		JSONObject saveTimes = new JSONObject();
		
		for (String time : times.keySet()) {
			saveTimes.put(time, times.get(time));
		}
		
		saveTimes.put("interval", System.currentTimeMillis() - timeBase);
		
		global.put("times", saveTimes);

		return (global.toString());
	}
	
	public void saveInventory() throws IOException {
        File f = new File(km.getDataFolder().getPath(), player.getName() + ".yml");
        FileConfiguration c = YamlConfiguration.loadConfiguration(f);
        c.set("inventory.content", deathInv.getContents());
        c.save(f);
    }
	
	@SuppressWarnings("unchecked")
	public void loadInventory() throws IOException {
        File f = new File(km.getDataFolder().getPath(), player.getName() + ".yml");
        FileConfiguration c = YamlConfiguration.loadConfiguration(f);
        
        deathInv = Bukkit.createInventory(null, 54);
        
        ItemStack[] content = ((List<ItemStack>) c.get("inventory.content")).toArray(new ItemStack[0]);
        deathInv.setContents(content);
    }
	
	public void load() {
		if (finished) {
			gameRank = km.playerRank.get(player.getName());
		}
		
		updateBar();
		reloadEffects();
		updatePlayerListName();
		blockScore.setScore(blockCount);
	}
	
	public void pause() {
		interval = System.currentTimeMillis() - timeShuffle;
	}
	
	public void resume() {
		timeShuffle = System.currentTimeMillis() - interval;
		interval = -1;
	}
	
	private void updateBar() {
		if (lose) {
			ageDisplay.setProgress(0.0);
			ageDisplay.setTitle(Utils.getLangString(km, player.getName(), "GAME_DONE").replace("%i", "" + gameRank));
			
			return ;
		} 
		
		if (finished) {
			ageDisplay.setProgress(1.0);
			ageDisplay.setTitle(Utils.getLangString(km, player.getName(), "GAME_DONE").replace("%i", "" + gameRank));
			
			return ;
		}
		
		double calc = ((double) blockCount) / km.config.getBlockPerAge();
		calc = calc > 1.0 ? 1.0 : calc;
		ageDisplay.setProgress(calc);
		ageDisplay.setTitle(AgeManager.getAgeByNumber(km.ages, age).name.replace("_", " ") + ": " + blockCount);
	}
	
	public void resetBar() {
		if (ageDisplay != null && ageDisplay.getPlayers().size() != 0) {
			ageDisplay.removeAll();
			ageDisplay = null;
		}
	}
	
	public void foundSBTT() {
		sbttCount++;
		
		found();
	}
	
	public void found() {
		currentblock = null;
		blockCount++;
		player.playSound(player.getLocation(), Sound.BLOCK_BELL_USE, 1f, 1f);
		blockScore.setScore(blockCount);
		updateBar();
	}
	
	public void nextAge() {
		if (km.config.getRewards()) {
			if (age > 0) {
				RewardManager.managePreviousEffects(km.allRewards.get(AgeManager.getAgeByNumber(km.ages, age - 1).name), player, AgeManager.getAgeByNumber(km.ages, age - 1).name);
			}
			
			RewardManager.givePlayerReward(km.allRewards.get(AgeManager.getAgeByNumber(km.ages, age).name), player, km.ages,  AgeManager.getAgeByNumber(km.ages, age).number);
		}
		
		times.put(AgeManager.getAgeByNumber(km.ages, age).name, System.currentTimeMillis() - timeBase);
		
		timeBase = System.currentTimeMillis();
		alreadyGot.clear();
		currentblock = null;
		blockCount = 1;
		sameIdx = 0;
		age++;
		time = time + km.config.getAddedTime();
		player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST, 1f, 1f);
		updatePlayerListName();
		blockScore.setScore(blockCount);
		updateBar();
		
		Age tmpAge = AgeManager.getAgeByNumber(km.ages, age);
		
		for (String playerName : km.games.keySet()) {
			km.games.get(playerName).getPlayer().sendMessage(Utils.getLangString(km, playerName, "AGE_MOVED").replace("<#>", ChatColor.BLUE + "<§6§l" + player.getName() + ChatColor.BLUE + ">").replace("<##>", "<" + tmpAge.color + tmpAge.name.replace("_Age", "") + ChatColor.BLUE + ">"));
			//km.games.get(playerName).getPlayer().sendMessage("§6§l" + player.getName() + ChatColor.BLUE + " has moved to the " + tmpAge.color + tmpAge.name.replace("_", " ") + "§1.");
		}
	}
	
	public void finish(int _gameRank) {
		finished = true;
		
		if (km.config.getTeam()) {
			int tmpRank;

			if ((tmpRank = checkTeamMateRank()) != -1) {
				_gameRank = tmpRank;
			}
		}
		
		gameRank = _gameRank;
		ageDisplay.setTitle(Utils.getLangString(km, player.getName(), "GAME_DONE").replace("%i", "" + gameRank));
		
		if (lose) {
			ageDisplay.setProgress(0.0f);	
		} else {
			ageDisplay.setProgress(1.0f);	
		}
		
		km.playerRank.put(player.getName(), gameRank);
		km.updatePlayersHead(player.getName(), null);
		
		for (PotionEffect pe : player.getActivePotionEffects()) {
			player.removePotionEffect(pe.getType());
		}
		
		if (lose) {
			for (int cnt = age; cnt < km.config.getMaxAges(); cnt++) {
				times.put(AgeManager.getAgeByNumber(km.ages, cnt).name, (long) -1);
			}
		} else {
			times.put(AgeManager.getAgeByNumber(km.ages, age).name, System.currentTimeMillis() - timeBase);
		}
		
		age = -1;
		
		updatePlayerListName();
		
		player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + player.getName());
		player.sendMessage(ChatColor.BLUE + Utils.getLangString(km, player.getName(), "DEATH_COUNT").replace("%i", "" + ChatColor.RESET + deathCount));
		player.sendMessage(ChatColor.BLUE + Utils.getLangString(km, player.getName(), "SKIP_COUNT").replace("%i", "" + ChatColor.RESET + skipCount));
		player.sendMessage(ChatColor.BLUE + Utils.getLangString(km, player.getName(), "TEMPLATE_COUNT").replace("%i", "" + ChatColor.RESET + sbttCount));
		player.sendMessage(ChatColor.BLUE + Utils.getLangString(km, player.getName(), "TIME_TAB"));
		
		for (int i = 0; i < km.config.getMaxAges(); i++) {
			Age age = AgeManager.getAgeByNumber(km.ages, i);
			
			String tmp;
			String tmpLog;
			
			if (times.get(age.name) == -1) {
				tmp = Utils.getLangString(km, player.getName(), "FINISH_ABANDON").replace("%s", age.color + age.name.replace("_Age", "") + ChatColor.RESET);
				tmpLog = Utils.getLangString(km, player.getName(), "FINISH_ABANDON").replace("%s", age.name.replace("_Age", ""));
			} else {
				tmp = Utils.getLangString(km, player.getName(), "FINISH_TIME").replace("%s", age.color + age.name.replace("_Age", "") + ChatColor.BLUE).replace("%t", ChatColor.RESET + Utils.getTimeFromSec(times.get(age.name) / 1000));
				tmpLog = Utils.getLangString(km, player.getName(), "FINISH_TIME").replace("%s", age.name.replace("_Age", "")).replace("%t", Utils.getTimeFromSec(times.get(age.name) / 1000));
			}
			
			player.sendMessage(tmp);
			km.logs.logMsg(player, tmpLog);
		}
	}
	
	private int checkTeamMateRank() {
		int tmp = -1;
		
		for (String playerName : km.games.keySet()) {
			if (km.games.get(playerName).getTeamName().equals(teamName) &&
					km.games.get(playerName).getRank() != -1) {
				tmp = km.games.get(playerName).getRank();
			}
		}
		
		return tmp;
	}
	
	public void randomBarColor() {
		ageDisplay.setColor(getRandomColor());
	}
	
	public boolean skip(boolean malus) {
		skipCount++;
		
		if (malus) {
			if ((age + 1) < km.config.getSkipAge()) {
				km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "CANT_SKIP_AGE"));
				
				return false;
			}
			
			if (blockCount == 1) {
				km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "CANT_SKIP_FIRST"));
				
				return false;
			}
			
			blockCount--;
			
			if (currentblock.contains("/")) {
				km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "BLOCKS_SKIP").replace("[#]", "[" + currentblock.split("/")[0] + "]").replace("[##]", "[" + currentblock.split("/")[1] + "]"));
			} else {
				km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "BLOCK_SKIP").replace("[#]", "[" + currentblock + "]"));	
			}
			
			blockScore.setScore(blockCount);
			updateBar();
			currentblock = null;
		} else {
			blockScore.setScore(blockCount);
			updateBar();
			currentblock = null;
		}
		
		return true;
	}
	
	public void reloadEffects() {
		if (km.config.getRewards()) {
			if (km.config.getSaturation()) {
				player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 999999, 10, false, false, false));
			}
			
			int tmp = age - 1;
			
			if (tmp < 0) 
				return;

			RewardManager.givePlayerRewardEffect(km.allRewards.get(AgeManager.getAgeByNumber(km.ages, tmp).name), player, AgeManager.getAgeByNumber(km.ages, tmp).name);
		}
	}
	
	public void savePlayerInv() {
		deathInv = Bukkit.createInventory(null, 54);
		
		for (ItemStack block : player.getInventory().getContents()) {
			if (block != null) {
				deathInv.addItem(block);
			}
		}
	}
	
	public void restorePlayerInv() {
		for (ItemStack block : deathInv.getContents()) {
			if (block != null) {
				HashMap<Integer, ItemStack> ret = player.getInventory().addItem(block);
				if (!ret.isEmpty()) {
					for (Integer cnt : ret.keySet()) {
						player.getWorld().dropItem(player.getLocation(), ret.get(cnt));
					}
				}
			}
		}
		
		deathInv.clear();
		deathInv = null;
		deathLoc = null;
		dead = false;
	}
	
	public void updatePlayerListName() {
		if (km.config.getTeam()) {
			player.setPlayerListName("[" + km.teams.getTeam(teamName).color + teamName + ChatColor.RESET + "] - " + AgeManager.getAgeByNumber(km.ages, age).color + player.getName());
		} else {
			player.setPlayerListName(AgeManager.getAgeByNumber(km.ages, age).color + player.getName());	
		}
	}
	
	public ArrayList<String> getAlreadyGot() {
		return alreadyGot;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public boolean getFinished() {
		return finished;
	}
	
	public boolean getLose() {
		return lose;
	}
	
	public boolean getDead() {
		return dead;
	}
	
	public int getTime() {
		return time;
	}
	
	public int getBlockCount() {
		return blockCount;
	}
	
	public int getAge() {
		return age;
	}
	
	public int getRank() {
		return gameRank;
	}
	
	public int getSameIdx() {
		return sameIdx;
	}
	
	public int getDeathCount() {
		return deathCount;
	}
	
	public int getSkipCount() {
		return skipCount;
	}
	
	public int getSbttCount() {
		return sbttCount;
	}
	
	public long getTimeShuffle() {
		return timeShuffle;
	}
	
	public long getAgeTime(String age) {
		return times.get(age);
	}
	
	public String getCurrentBlock() {
		return currentblock;
	}
	
	public String getBlockDisplay() {
		return blockDisplay;
	}
	
	public String getTeamName() {
		return teamName;
	}
	
	public String getLang() {
		return configLang;
	}
	
	public Score getBlockScore() {
		return blockScore;
	}

	public Location getSpawnLoc() {
		return spawnLoc;
	}
	
	public Location getDeathLoc() {
		return deathLoc;
	}

	public void setAlreadyGot(JSONArray _alreadyGot) {
		for (int i = 0; i < _alreadyGot.size(); i++) {
			alreadyGot.add((String) _alreadyGot.get(i));			
		}
	}
	
	public void setFinished(boolean _finished) {
		finished = _finished;
	}
	
	public void setLose(boolean _lose) {
		lose = _lose;
	}
	
	public void setDead(boolean _dead) {
		dead = _dead;
		
		if (dead) {
			deathCount++;
		}
	}
	
	public void setTime(int _time) {
		time = _time;
	}
	
	public void setBlockCount(int _blockCount) {
		blockCount = _blockCount;
		
		if (blockScore != null) {
			blockScore.setScore(blockCount);			
		}

		updateBar();
	}
	
	public void setAge(int _age) {
		if (age == _age) {
			return;
		}
		
		age = _age;
		alreadyGot.clear();
	}
	
	public void setSameIdx(int _sameIdx) {
		sameIdx = _sameIdx;
	}
	
	public void setDeathCount(int _deathCount) {
		deathCount = _deathCount;
	}
	
	public void setSkipCount(int _skipCount) {
		skipCount = _skipCount;
	}
	
	public void setTimeShuffle(long _timeShuffle) {
		timeShuffle = _timeShuffle;
	}

	public void setCurrentBlock(String _currentblock) {
		currentblock = _currentblock;
		
		if (currentblock == null) {
			return ;
		}

		if (km.config.getDouble()) {
			timeShuffle = System.currentTimeMillis();
			
			blockDisplay = LangManager.findDisplay(km.allBlocksLangs, currentblock.split("/")[0], configLang) + "/" + LangManager.findDisplay(km.allBlocksLangs, currentblock.split("/")[1], configLang);
			km.updatePlayersHead(player.getName(), blockDisplay);
		} else {
			if (!alreadyGot.contains(currentblock)) {
				alreadyGot.add(currentblock);
			}
			
			timeShuffle = System.currentTimeMillis();
			blockDisplay = LangManager.findDisplay(km.allBlocksLangs, currentblock, configLang);
			km.updatePlayersHead(player.getName(), blockDisplay);
		}
	}
	
	public void setTeamName(String _teamName) {
		teamName = _teamName;
	}
	
	public void setLang(String _configLang) {
		if (_configLang.equals(configLang)) {
			return ;
		}
		
		configLang = _configLang;
		
		if (currentblock != null) {
			blockDisplay = LangManager.findDisplay(km.allBlocksLangs, currentblock, configLang);
		}
	}
	
	public void setBlockScore(Score score) {
		blockScore = score;
	}
	
	public void setDeathInv(Inventory _deathInv) {
		deathInv = _deathInv;
	}

	public void setSpawnLoc(Location _spawnLoc) {
		spawnLoc = _spawnLoc;
	}
	
	public void setSpawnLoc(JSONObject _spawnloc) {
		spawnLoc = new Location(Bukkit.getWorld((String) _spawnloc.get("World")),
				(double) _spawnloc.get("X"),
				(double) _spawnloc.get("Y"),
				(double) _spawnloc.get("Z"));
	}
	
	public void setDeathLoc(Location _deathLoc) {
		deathLoc = _deathLoc;
	}
	
	public void setDeathLoc(JSONObject _deathLoc) {
		if (_deathLoc == null) {
			deathLoc = null;
		} else {
			deathLoc = new Location(Bukkit.getWorld((String) _deathLoc.get("World")),
					(double) _deathLoc.get("X"),
					(double) _deathLoc.get("Y"),
					(double) _deathLoc.get("Z"));	
		}
	}
	
	public void setTimes(JSONObject _times) {
		timeBase = System.currentTimeMillis() - (Long) _times.get("interval");
		
		for (int i = 0; i < km.config.getMaxAges(); i++) {
			Age age = AgeManager.getAgeByNumber(km.ages, i);
			
			if (_times.containsKey(age.name)) {
				times.put((String) age.name, (Long) _times.get(age.name));
			}
		}
	}
	
	public void addToAlreadyGot(String block) {
		alreadyGot.add(block);
	}
	
	public void removeFromList(String[] array) {
		alreadyGot.remove(currentblock.equals(array[0]) ? array[1] : array[0]);
	}
	
	public void resetList() {
		alreadyGot.clear();
	}
	
	private BarColor getRandomColor() {
		Random r = new Random();
		
		return (BarColor.values()[r.nextInt(BarColor.values().length)]);
	}
}
