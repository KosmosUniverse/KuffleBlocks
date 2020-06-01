package fr.kosmosuniverse.kuffle.Core;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import fr.kosmosuniverse.kuffle.KuffleMain;

public class GameTask {
	private BukkitTask runnable;
	private Player player;
	private ArrayList<String> alreadyGot;
	private int age = 0;
	private String[] ageNames = {"Archaic", "Classic", "Netheric", "Heroic", "Mythic"};
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
	
	public GameTask(KuffleMain _km, Player _p) {
		km = _km;
		player = _p;
	}
	
	public void startRunnable() {
		ageDisplay = Bukkit.createBossBar(ageNames[age] + " Age: 1", BarColor.PURPLE, BarStyle.SOLID) ;
		ageDisplay.addPlayer(player);
		maxBlock = km.getConfig().getDouble("game_settings.block_per_age");
		double tmp = 1 / maxBlock;
		ageDisplay.setProgress(tmp);
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
					if (currentBlock == null) {
						previousShuffle = System.currentTimeMillis();
						currentBlock = ChooseBlockInList.newBlock(alreadyGot, km.allBlocks.get(ageNames[age] + "_Age"));
						alreadyGot.add(currentBlock);
						double tmp = ((double) blockCount) / maxBlock;
						tmp = tmp > 1.0 ? 1.0 : tmp;
						ageDisplay.setProgress(tmp);
						ageDisplay.setTitle(ageNames[age] + " Age: " + blockCount);
					}
					
					if (System.currentTimeMillis() - previousShuffle > (time * 60000)) {
						player.sendMessage("§4You didn't find your block. Let's give you another one.§r");
						currentBlock = null;
					} else {
						if (found || player.getLocation().add(0, -1, 0).getBlock().getType() == Material.matchMaterial(currentBlock) || player.getLocation().getBlock().getType() == Material.matchMaterial(currentBlock)) {
							player.playSound(player.getLocation(), Sound.BLOCK_BELL_USE, 1f, 1f);
							currentBlock = null;
							blockCount++;
							double tmp = ((double) blockCount) / maxBlock;
							tmp = tmp > 1.0 ? 1.0 : tmp;
							ageDisplay.setProgress(tmp);
							ageDisplay.setTitle(ageNames[age] + " Age: " + blockCount);
							found = false;
						}
					}
					
					if (blockCount == (maxBlock + 1)) {
						player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST_FAR, 1f, 1f);
						blockCount = 1;
						alreadyGot.clear();
						time += 2;
						if (km.getConfig().getBoolean("game_settings.rewards"))
							RewardManager.givePlayerReward(km.allRewards.get(ageNames[age] + "_Age"), player, ageNames[age]);
						age++;
						double tmp = 1 / maxBlock;
						ageDisplay.setProgress(tmp);
						ageDisplay.setTitle(ageNames[age] + " Age: 1");
						Bukkit.broadcastMessage("§1" + player.getName() + " has moved to the §6§l" + ageNames[age] + " Age§1.");
					}
					
					if (age == 2) {
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
}
