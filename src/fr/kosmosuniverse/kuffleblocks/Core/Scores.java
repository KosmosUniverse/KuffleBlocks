package fr.kosmosuniverse.kuffleblocks.Core;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import fr.kosmosuniverse.kuffleblocks.KuffleMain;
import net.md_5.bungee.api.ChatColor;

public class Scores {
	private KuffleMain km;
	private Scoreboard scoreboard;
	private Objective age = null;
	private Objective items;
	private ArrayList<Score> S_ages = new ArrayList<Score>();
	
	public Scores(KuffleMain _km) {
		km = _km;
		scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		items = scoreboard.registerNewObjective("blocks", "dummy", "Blocks");
	}
	
	public void setupPlayerScores() {
		items.setDisplaySlot(DisplaySlot.PLAYER_LIST);
		
		if (age != null) {
			age.unregister();
		}
		
		age = scoreboard.registerNewObjective("ages", "dummy", ChatColor.LIGHT_PURPLE + "Ages");
		
		int ageCnt = 0;
		
		for (; ageCnt < km.config.getMaxAges(); ageCnt++) {
			S_ages.add(age.getScore(AgeManager.getAgeByNumber(km.ages, ageCnt).color + AgeManager.getAgeByNumber(km.ages, ageCnt).name.replace("_", " ")));
		}
				
		ageCnt = 1;
		
		for (Score ageScore : S_ages) {
			ageScore.setScore(ageCnt);
			ageCnt++;
		}
		
		age.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		for (String playerName : km.games.keySet()) {
			km.games.get(playerName).setBlockScore(items.getScore(playerName));
			km.games.get(playerName).getBlockScore().setScore(1);
			km.games.get(playerName).getPlayer().setScoreboard(scoreboard);
			km.games.get(playerName).updatePlayerListName();
		}
	}
	
	public void setupPlayerScores(Game _game) {
		items.setDisplaySlot(DisplaySlot.PLAYER_LIST);
		age.setDisplaySlot(DisplaySlot.SIDEBAR);

		_game.setBlockScore(items.getScore(_game.getPlayer().getName()));
		_game.getBlockScore().setScore(1);
		_game.getPlayer().setScoreboard(scoreboard);
	}
	
	public void clear() {
		scoreboard.clearSlot(age.getDisplaySlot());
		
		if (items.getDisplaySlot() != null) {
			scoreboard.clearSlot(items.getDisplaySlot());
		}

		age.unregister();
		age = null;
		S_ages.clear();
		
		for (String playerName : km.games.keySet()) {
			km.games.get(playerName).getPlayer().setPlayerListName(ChatColor.WHITE + km.games.get(playerName).getPlayer().getName());
		}
	}
	
	public void reset() {
		for (String playerName : km.games.keySet()) {
			km.games.get(playerName).getBlockScore().setScore(1);
			km.games.get(playerName).getPlayer().setPlayerListName(ChatColor.RED + km.games.get(playerName).getPlayer().getName());
		}
	}
}
