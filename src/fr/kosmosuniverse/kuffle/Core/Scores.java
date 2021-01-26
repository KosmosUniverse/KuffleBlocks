package fr.kosmosuniverse.kuffle.Core;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import fr.kosmosuniverse.kuffle.KuffleMain;
import fr.kosmosuniverse.kuffle.utils.Utils;
import net.md_5.bungee.api.ChatColor;

public class Scores {
	private KuffleMain km;
	private Scoreboard scoreboard;
	private Objective age;
	private Objective blocks;
	private String[] ageNames = {"Archaic", "Classic", "Mineric", "Netheric", "Heroic", "Mythic"};
	private ArrayList<Score> S_ages = new ArrayList<Score>();
	
	public Scores(KuffleMain _km) {
		km = _km;
		scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		age = scoreboard.registerNewObjective("ages", "dummy", ChatColor.LIGHT_PURPLE + "Ages");
		blocks = scoreboard.registerNewObjective("blocks", "dummy", "Blocks");
		
		int ageCnt = 0;
		
		for (String ageStr : ageNames) {
			S_ages.add(age.getScore(Utils.getColor(ageCnt) + ageStr + " Age"));
			ageCnt++;
		}
		
		ageCnt = 1;
		
		for (Score ageScore : S_ages) {
			ageScore.setScore(ageCnt);
			ageCnt++;
		}
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
	
	public void setupPlayerScores(DisplaySlot slot, Player player) {
		blocks.setDisplaySlot(slot);
		age.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		for (GameTask gt : km.games) {
			if (gt.getPlayer().getName().equals(player.getName())) {
				gt.setBlockScore(blocks.getScore(gt.getPlayer().getDisplayName()));
				gt.getBlockScore().setScore(1);
				gt.getPlayer().setScoreboard(scoreboard);
				return ;
			}
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
