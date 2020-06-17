package fr.kosmosuniverse.kuffle.Core;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Score;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import fr.kosmosuniverse.kuffle.KuffleMain;

public class GameTask {
	private BukkitTask runnable;
	private Player player;
	private ArrayList<String> alreadyGot;
	private int age = 0;
	private String[] ageNames = {"Archaic", "Classic", "Mineric", "Netheric", "Heroic", "Mythic"};
	private String currentBlock = null;
	private long previousShuffle = -1;
	private int time;
	private long interval = -1;
	private boolean enable = false;
	private boolean exit = false;
	private int blockCount = 1;
	private KuffleMain km;
	private BossBar ageDisplay;
	private double maxBlock;
	private boolean found = false;
	private double calc = 0;
	private Score blockScore;
	
	public GameTask(KuffleMain _km, Player _p) {
		km = _km;
		player = _p;
	}
	
	public void startRunnable() {
		ageDisplay = Bukkit.createBossBar(ageNames[age] + " Age: 1", BarColor.PURPLE, BarStyle.SOLID) ;
		ageDisplay.addPlayer(player);
		maxBlock = km.getConfig().getDouble("game_settings.block_per_age");
		calc = 1 / maxBlock;
		ageDisplay.setProgress(calc);
		exit = false;
		alreadyGot = new ArrayList<String>();
		time = km.getConfig().getInt("game_settings.start_time");
		
		runnable = new BukkitRunnable() {
			@Override
			public void run() {
				if (exit) {
					return;
				}
				if (enable) {
					calc = ((double) blockCount) / maxBlock;
					calc = calc > 1.0 ? 1.0 : calc;
					ageDisplay.setProgress(calc);
					ageDisplay.setTitle(ageNames[age] + " Age: " + blockCount);
					
					if (currentBlock == null) {
						previousShuffle = System.currentTimeMillis();
						currentBlock = ChooseBlockInList.newBlock(alreadyGot, km.allBlocks.get(ageNames[age] + "_Age"));
						alreadyGot.add(currentBlock);
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
							calc = ((double) blockCount) / maxBlock;
							calc = calc > 1.0 ? 1.0 : calc;
							ageDisplay.setProgress(calc);
							ageDisplay.setTitle(ageNames[age] + " Age: " + blockCount);
							found = false;
						}
					}
					
					if (blockCount == (maxBlock + 1)) {
						player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST, 1f, 1f);
						blockCount = 1;
						blockScore.setScore(blockCount);
						alreadyGot.clear();
						time += 2;
						
						if (km.getConfig().getBoolean("game_settings.rewards")) {
							if (age > 0) {
								RewardManager.managePreviousEffects(km.allRewards.get(ageNames[age - 1] + "_Age"), km.effects, player, ageNames[age - 1]);
							}
							
							RewardManager.givePlayerReward(km.allRewards.get(ageNames[age] + "_Age"), km.effects, player, ageNames[age]);
						}

						age++;
						player.setPlayerListName(getColor() + player.getName());
						calc = 1 / maxBlock;
						ageDisplay.setProgress(calc);
						ageDisplay.setTitle(ageNames[age] + " Age: 1");
						Bukkit.broadcastMessage("§1" + player.getName() + " has moved to the §6§l" + ageNames[age] + " Age§1.");
					}
					
					if (age == 5) {
						player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_TWINKLE_FAR, 1f, 1f);
						player.sendMessage("§6§lYou complete this game !§r");
						exit = true;
					}
					
					if (previousShuffle != -1) {
						long count = time * 60000;
						count -= (System.currentTimeMillis() - previousShuffle);
						count /= 1000;
						String dispCurBlock;
						
						if (currentBlock == null)
							dispCurBlock = "Something New...";
						else if (currentBlock.contains("_"))
							dispCurBlock = currentBlock.replace("_", " ");
						else
							dispCurBlock = currentBlock;
						
						dispCurBlock = dispCurBlock.substring(0, 1).toUpperCase() + dispCurBlock.substring(1);
						String color = null;
						
						if (count < 30) {
							color = "\u00a7c";
						} else if (count < 60) {
							color = "\u00a7e";
						} else {
							color = "\u00a7a";
						}
						
						ActionBar.sendMessage(color + "Time Left: " + count + " seconds to find: " + dispCurBlock + ".", player);
					} else {
						ActionBar.sendMessage("\\u00a73XX:XX", player);
					}
				}
			}
		}.runTaskTimer(km, 0, 20);
	}

	public boolean getEnable() {
		return enable;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public int getAge() {
		return age;
	}
	
	public int getBlockCount() {
		return blockCount;
	}
	
	public Score getBlockScore() {
		return blockScore;
	}
	
	public ChatColor getColor() {
		switch (age) {
		case 0:
			return (ChatColor.RED);
		case 1:
			return (ChatColor.GOLD);
		case 2:
			return (ChatColor.YELLOW);
		case 3:
			return (ChatColor.GREEN);
		case 4:
			return (ChatColor.DARK_GREEN);
		case 5:
			return (ChatColor.DARK_BLUE);
		default:
			return (ChatColor.DARK_PURPLE);
		}
	}
	
	public void setBlockScore(Score score) {
		blockScore = score;
	}
	
	public void setBlockCount(int _blockCount) {
		blockCount = _blockCount;
	}
	
	public void enable() {
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
		time = km.getConfig().getInt("game_settings.start_time");
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
		
		JSONObject global = new JSONObject();
		
		global.put("age", age);
		global.put("current", currentBlock);
		global.put("interval", interval);
		global.put("time", time);
		global.put("blockCount", blockCount);
		
		JSONArray got = new JSONArray();
		
		for (String block : alreadyGot) {
			got.add(block);
		}
		
		global.put("alreadyGot", got);

		return (global.toString());
	}
	
	public void loadGame(int _age, String _current, long _interval, int _time, int _blockCount, JSONArray _alreadyGot) {
		age = _age;
		currentBlock = _current;
		previousShuffle = System.currentTimeMillis() - _interval;
		interval = -1;
		time = _time;
		blockCount = _blockCount;
		blockScore.setScore(blockCount);

		for (int i = 0; i < _alreadyGot.size(); i++) {
			alreadyGot.add((String) _alreadyGot.get(i));			
		}
	}
}
