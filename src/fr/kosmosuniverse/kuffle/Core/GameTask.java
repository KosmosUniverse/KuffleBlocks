package fr.kosmosuniverse.kuffle.Core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Score;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import fr.kosmosuniverse.kuffle.KuffleMain;
import fr.kosmosuniverse.kuffle.utils.Utils;

public class GameTask {
	private ArrayList<String> alreadyGot;
	
	private KuffleMain km;
	
	private Location spawnLoc;
	private Location deathLoc;
	
	private Inventory deathInv = null;
	
	private String[] ageNames = {"Archaic", "Classic", "Mineric", "Netheric", "Heroic", "Mythic"};

	private String currentBlock = null;
	private String blockDisplay = null;
	private String configLang;
	
	private int age = 0;
	private int time;
	private int blockCount = 1;
	private int gameRank;
	
	private long previousShuffle = -1;
	private long interval = -1;
	private long deathTime = 0;
	
	private boolean enable = false;
	private boolean exit = false;
	private boolean found = false;
	
	private double calc = 0;
	
	private BukkitTask runnable;
	private Player player;
	private BossBar ageDisplay;
	private Score blockScore;
	
	public GameTask(KuffleMain _km, Player _p) {
		km = _km;
		player = _p;
		
		configLang = km.config.getLang();
	}
	
	public void startRunnable() {
		ageDisplay = Bukkit.createBossBar(ageNames[age] + " Age: 1", BarColor.PURPLE, BarStyle.SOLID) ;
		ageDisplay.addPlayer(player);
		calc = 1 / km.config.getBlockPerAge();
		ageDisplay.setProgress(calc);
		exit = false;
		alreadyGot = new ArrayList<String>();
		time = km.config.getStartTime();
		
		runnable = new BukkitRunnable() {
			@Override
			public void run() {
				if (exit) {
					if (age != km.config.getMaxAges()) {
						ageDisplay.setColor(BarColor.RED);
						ageDisplay.setTitle("Game Done ! Rank : " + getOppositeGameRank());
					} else {
						ageDisplay.setColor(getRandomColor());	
					}
					
					return;
				}
				
				if (enable) {
					time = km.config.getStartTime() + (km.config.getAddedTime() * age);
					calc = ((double) blockCount) / km.config.getBlockPerAge();
					calc = calc > 1.0 ? 1.0 : calc;
					ageDisplay.setProgress(calc);
					
					if (age == km.config.getMaxAges()) {
						ageDisplay.setTitle("Game Done ! Rank : " + gameRank);
					} else {
						ageDisplay.setTitle(ageNames[age] + " Age: " + blockCount);
					}
					
					blockScore.setScore(blockCount);
					
					if (currentBlock == null || age == km.config.getMaxAges()) {
						previousShuffle = System.currentTimeMillis();
						currentBlock = ChooseBlockInList.newBlock(alreadyGot, km.allBlocks.get(ageNames[age] + "_Age"));
						blockDisplay = LangManager.findBlockDisplay(km.allLang, currentBlock, configLang);
						alreadyGot.add(currentBlock);
						km.updatePlayersHead(player.getDisplayName(), blockDisplay);
					}
					
					if (System.currentTimeMillis() - previousShuffle > (time * 60000)) {
						player.sendMessage("§4You didn't find your block. Let's give you another one.§r");
						currentBlock = null;
					} else {
						if (found || player.getLocation().add(0, -1, 0).getBlock().getType() == Material.matchMaterial(currentBlock) || player.getLocation().getBlock().getType() == Material.matchMaterial(currentBlock)) {
							player.playSound(player.getLocation(), Sound.BLOCK_BELL_USE, 1f, 1f);
							currentBlock = null;
							blockCount++;
							blockScore.setScore(blockCount);
							calc = ((double) blockCount) / km.config.getBlockPerAge();;
							calc = calc > 1.0 ? 1.0 : calc;
							ageDisplay.setProgress(calc);
							ageDisplay.setTitle(ageNames[age] + " Age: " + blockCount);
							found = false;
						}
					}
					
					if (blockCount >= (km.config.getBlockPerAge() + 1)) {
						player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST, 1f, 1f);
						blockCount = 1;
						blockScore.setScore(blockCount);
						alreadyGot.clear();
						
						if (km.config.getRewards()) {
							if (age > 0) {
								RewardManager.managePreviousEffects(km.allRewards.get(ageNames[age - 1] + "_Age"), km.effects, player, ageNames[age - 1]);
							}
							
							RewardManager.givePlayerReward(km.allRewards.get(ageNames[age] + "_Age"), km.effects, player, ageNames[age]);
						}

						age++;
						player.setPlayerListName(Utils.getColor(age) + player.getName());
						calc = 1 / km.config.getBlockPerAge();
						ageDisplay.setProgress(calc);
						
						if (age == km.config.getMaxAges()) {
							gameRank = getGameRank();
							ageDisplay.setTitle("Game Done ! Rank : " + gameRank);
						} else {
							ageDisplay.setTitle(ageNames[age] + " Age: 1");
							Bukkit.broadcastMessage("§1" + player.getName() + " has moved to the §6§l" + ageNames[age] + " Age§1.");
						}
					}
					
					if (age == km.config.getMaxAges()) {
						player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_TWINKLE_FAR, 1f, 1f);
						Bukkit.broadcastMessage("§1" + player.getName() + " §6§lcomplete this game !§r");
						exit = true;
						previousShuffle = -1;
						ageDisplay.setProgress(1.0);
					}
					
					if (previousShuffle != -1) {
						long count = time * 60000;
						count -= (System.currentTimeMillis() - previousShuffle);
						count /= 1000;
						String dispCurBlock;
						
						if (currentBlock == null)
							dispCurBlock = "Something New...";
						else
							dispCurBlock = blockDisplay;
						
						String color = null;
						
						if (count < 30) {
							color = "\u00a7c";
						} else if (count < 60) {
							color = "\u00a7e";
						} else {
							color = "\u00a7a";
						}
						
						ActionBar.sendMessage(color + "Time Left: " + count + " seconds to find: " + dispCurBlock + ".", player);
					}
				}
			}
		}.runTaskTimer(km, 0, 20);
	}

	public boolean getEnable() {
		return enable;
	}
	
	public boolean getExit() {
		return exit;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public Location getDeathLoc() {
		return deathLoc;
	}
	
	public Long getDeathTime() {
		return deathTime;
	}
	
	public Location getSpawnLoc() {
		return spawnLoc;
	}
	
	public int getAge() {
		return age;
	}
	
	public String getAgeName() {
		return ageNames[age] + "_Age";
	}
	
	public String[] getAgeNames() {
		return ageNames;
	}
	
	public int getBlockCount() {
		return blockCount;
	}
	
	public Score getBlockScore() {
		return blockScore;
	}
	
	private int getGameRank() {
		km.playerRank.put(player.getDisplayName(), true);
		
		int count = 0;
		
		for (String player : km.playerRank.keySet()) {
			if (km.playerRank.get(player))
				count++;
		}
		
		return count;
	}
	
	private int getOppositeGameRank() {
		km.playerRank.put(player.getDisplayName(), true);
		
		int count = 0;
		
		for (String player : km.playerRank.keySet()) {
			if (km.playerRank.get(player))
				count++;
		}
		
		return km.games.size() - count + 1;
	}
	
	public String getLang() {
		return configLang;
	}
	
	public void setExit(boolean _exit) {
		exit = _exit;
	}
	
	public void setDeathLoc(Location _deathLoc) {
		deathLoc = _deathLoc;
		deathTime = System.currentTimeMillis();
	}
	
	public void setSpawnLoc(Location _spawnLoc) {
		spawnLoc = _spawnLoc;
	}
	
	public void setLang(String _configLang) {
		if (_configLang.equals(configLang)) {
			return ;
		}
		
		configLang = _configLang;
		
		if (currentBlock != null) {
			blockDisplay = LangManager.findBlockDisplay(km.allLang, currentBlock, configLang);
		}
	}
	
	public void setBlockScore(Score score) {
		blockScore = score;
	}
	
	public void setBlockCount(int _blockCount) {
		blockCount = _blockCount;
	}
	
	public void reloadEffects() {
		if (km.config.getRewards()) {
			if (km.config.getSaturation()) {
				player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 999999, 10, false, false, false));
			}
			
			int tmp = age - 1;
			
			if (tmp < 0) 
				return;

			RewardManager.givePlayerRewardEffect(km.allRewards.get(ageNames[tmp] + "_Age"), km.effects, player, ageNames[tmp]);
		}
	}
	
	
	public void savePlayerInv() {
		deathInv = Bukkit.createInventory(null, 54);
		
		for (ItemStack item : player.getInventory().getContents()) {
			if (item != null) {
				deathInv.addItem(item);
			}
		}
	}
	
	public void restorePlayerInv() {
		if (System.currentTimeMillis() - deathTime > (Utils.maxSecondsWithLevel(km.config.getLevel()) * 1000)) {
			player.sendMessage("You waited too much to return to your death spot, your stuff is now unreachable.");
			deathInv.clear();
			deathInv = null;
			return;
		}
		
		for (ItemStack item : deathInv.getContents()) {
			if (item != null) {
				HashMap<Integer, ItemStack> ret = player.getInventory().addItem(item);
				if (!ret.isEmpty()) {
					for (Integer cnt : ret.keySet()) {
						player.getWorld().dropItem(player.getLocation(), ret.get(cnt));
					}
				}
			}
		}
		
		deathInv.clear();
		deathInv = null;
	}
	
	public void enable() {
		if (age == 6) {
			exit = true;
			enable = true;
			return;
		}
		
		if (interval != -1) {
			previousShuffle = System.currentTimeMillis() - interval;
		}
		enable = true;
	}
	
	public void disable() {
		interval = System.currentTimeMillis() - previousShuffle;
		enable = false;
	}
	
	public void kill() {
		if (runnable != null && !runnable.isCancelled()) {
			runnable.cancel();
		}
	}
	
	public void exit() {
		exit = true;
		if (alreadyGot != null)
			alreadyGot.clear();
		age = 0;
		currentBlock = null;
		previousShuffle = -1;
		interval = -1;
		enable = false;
		blockCount = 1;
		time = km.config.getStartTime();
		if (ageDisplay != null && ageDisplay.getPlayers().size() != 0) {
			ageDisplay.removeAll();
			ageDisplay = null;
		}
	}
	
	public void skip() {
		enable = false;
		Bukkit.getScheduler().scheduleSyncDelayedTask(km, new Runnable() {
			@Override
			public void run() {
				currentBlock = null;
				enable = true;
			}
		}, 30);
	}
	
	public void validate() {
		found = true;
	}
	
	@SuppressWarnings("unchecked")
	public String saveGame() {
		enable = false;
		
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
		
		global.put("age", age);
		global.put("maxAge", km.config.getMaxAges());
		global.put("current", currentBlock);
		global.put("interval", interval);
		global.put("time", time);
		global.put("blockCount", blockCount);
		global.put("spawn", jsonSpawn);
		global.put("death", jsonDeath);
		
		JSONArray got = new JSONArray();
		
		for (String block : alreadyGot) {
			got.add(block);
		}
		
		global.put("alreadyGot", got);

		return (global.toString());
	}
	
	public void loadGame(int _age, int maxAge, String _current, long _interval, int _time, int _blockCount, JSONArray _alreadyGot, JSONObject spawn, JSONObject death) {
		age = _age;
		currentBlock = _current;
		previousShuffle = System.currentTimeMillis() - _interval;
		interval = -1;
		time = km.config.getStartTime() + (km.config.getAddedTime() * age);
		
		if (km.config.getSeeBlockCnt()) {
			km.scores.setupPlayerScores(DisplaySlot.PLAYER_LIST, player);
		} else {
			km.scores.setupPlayerScores(DisplaySlot.BELOW_NAME, player);
		}

		spawnLoc = new Location(Bukkit.getWorld((String) spawn.get("World")), (double) spawn.get("X"), (double) spawn.get("Y"), (double) spawn.get("Z"));
		if (death == null) {
			deathLoc = null;
		} else {
			deathLoc = new Location(Bukkit.getWorld((String) death.get("World")), (double) death.get("X"), (double) death.get("Y"), (double) death.get("Z"));	
		}
		
		blockDisplay = LangManager.findBlockDisplay(km.allLang, currentBlock, configLang);
		km.updatePlayersHead(player.getDisplayName(), blockDisplay);
		
		blockCount = _blockCount;
		blockScore.setScore(blockCount);
		player.setPlayerListName(Utils.getColor(age) + player.getName());
		
		for (int i = 0; i < _alreadyGot.size(); i++) {
			alreadyGot.add((String) _alreadyGot.get(i));			
		}
		
		if (age == km.config.getMaxAges()) {
			ageDisplay.setTitle("Game Done ! Rank : " + getGameRank());
			ageDisplay.setProgress(1.0);
			km.playerRank.put(player.getDisplayName(), true);
		}
	}
	
	private BarColor getRandomColor() {
		Random r = new Random();
		
		return (BarColor.values()[r.nextInt(BarColor.values().length)]);
	}
}
