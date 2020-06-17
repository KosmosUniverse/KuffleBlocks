package fr.kosmosuniverse.kuffle.Core;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import fr.kosmosuniverse.kuffle.KuffleMain;
import net.md_5.bungee.api.ChatColor;

public class Scores {
	private KuffleMain km;
	private Scoreboard scoreboard;
	private Objective age;
	private Objective blocks;
	private Score archaic;
	private Score classic;
	private Score mineric;
	private Score netheric;
	private Score heroic;
	private Score mythic;
	
	public Scores(KuffleMain _km) {
		km = _km;
		scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		age = scoreboard.registerNewObjective("ages", "dummy", ChatColor.LIGHT_PURPLE + "Ages");
		blocks = scoreboard.registerNewObjective("blocks", "dummy", "Blocks");
		
		archaic = age.getScore(ChatColor.RED + "Archaic Age");
		classic = age.getScore(ChatColor.GOLD + "Classic Age");
		mineric = age.getScore(ChatColor.YELLOW + "Mineric Age");
		netheric = age.getScore(ChatColor.GREEN + "Netheric Age");
		heroic = age.getScore(ChatColor.DARK_GREEN + "Heroic Age");
		mythic = age.getScore(ChatColor.DARK_BLUE + "Mythic Age");
		
		archaic.setScore(1);
		classic.setScore(1);
		mineric.setScore(1);
		netheric.setScore(1);
		heroic.setScore(1);
		mythic.setScore(1);
	}
	
	public void setupPlayerScores(DisplaySlot slot) {
		blocks.setDisplaySlot(slot);
		age.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		for (GameTask gt : km.games) {
			gt.setBlockScore(blocks.getScore(gt.getPlayer().getDisplayName()));
			gt.getBlockScore().setScore(1);
			gt.getPlayer().setScoreboard(scoreboard);
			gt.getPlayer().setPlayerListName(ChatColor.RED + gt.getPlayer().getName());
		}
	}
	
	public void clear() {
		scoreboard.clearSlot(age.getDisplaySlot());
		scoreboard.clearSlot(blocks.getDisplaySlot());
		
		for (GameTask gt : km.games) {
			gt.getPlayer().setPlayerListName(ChatColor.WHITE + gt.getPlayer().getName());
		}
	}
	
	public void reset() {
		for (GameTask gt : km.games) {
			gt.getBlockScore().setScore(1);;
			gt.getPlayer().setPlayerListName(ChatColor.RED + gt.getPlayer().getName());
		}
	}
}
