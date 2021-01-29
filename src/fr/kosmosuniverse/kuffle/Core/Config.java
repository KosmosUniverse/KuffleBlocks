package fr.kosmosuniverse.kuffle.Core;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import fr.kosmosuniverse.kuffle.KuffleMain;

public class Config {
	public boolean saturation;
	public boolean spread;
	public boolean rewards;
	public boolean skip;
	public boolean crafts;
	public boolean seeBlockCnt;
	public int spreadMin;
	public int spreadMax;
	public int blockPerAge;
	public int skipAge;
	public int maxAges;
	public int startTime;
	public int addedTime;
	public String lang;
	public Level level;
	
	private String booleanErrorMsg = "Invalid value ! True or False awaited.";
	
	public void setupConfig(KuffleMain km, FileConfiguration configFile) {
		if (!configFile.contains("game_settings.block_per_age") || configFile.getInt("game_settings.block_per_age") < 1) {
			Bukkit.broadcastMessage("Config for block per age is not correct, use of default value.");
			configFile.set("game_settings.block_per_age", 5);
		}
		
		if (!configFile.contains("game_settings.spreadplayers.minimum_distance") || configFile.getInt("game_settings.spreadplayers.minimum_distance") < 1) {
			Bukkit.broadcastMessage("Config for spreadplayers minimum distance is not correct, use of default value.");
			configFile.set("game_settings.spreadplayers.minimum_distance", 100);
		}
		
		if (!configFile.contains("game_settings.spreadplayers.maximum_distance") || configFile.getInt("game_settings.spreadplayers.maximum_area") < configFile.getInt("game_settings.spreadplayers.minimum_distance")) {
			Bukkit.broadcastMessage("Config for spreadplayers maximum area is not correct, use of default value.");
			configFile.set("game_settings.spreadplayers.maximum_area", 500);
		}
		
		if (!configFile.contains("game_settings.start_time") || configFile.getInt("game_settings.start_time") < 1) {
			Bukkit.broadcastMessage("Config for start time is not correct, use of default value.");
			configFile.set("game_settings.start_time", 4);
		}
		
		if (!configFile.contains("game_settings.time_added") || configFile.getInt("game_settings.time_added") < 1) {
			Bukkit.broadcastMessage("Config for time added is not correct, use of default value.");
			configFile.set("game_settings.time_added", 2);
		}
		
		if (!configFile.contains("game_settings.max_age") || configFile.getInt("game_settings.max_age") < 1) {
			Bukkit.broadcastMessage("Config for max age is not correct, use of default value.");
			configFile.set("game_settings.max_age", 6);
		}
		
		if (!configFile.contains("game_settings.lang") || !km.langs.contains(configFile.getString("game_settings.lang"))) {
			Bukkit.broadcastMessage("Config for lang is not correct, use of default value.");
			configFile.set("game_settings.lang", "en");
		}
		
		if (!configFile.contains("game_settings.level") || configFile.getInt("game_settings.lang") < 0 || configFile.getInt("game_settings.lang") > 3) {
			Bukkit.broadcastMessage("Config for level is not correct, use of default value.");
			configFile.set("game_settings.level", 1);
		}
		
		if (!configFile.contains("game_settings.skip.enable")) {
			Bukkit.broadcastMessage("Config for enabling skip is not correct, use of default value.");
			configFile.set("game_settings.skip.enable", true);
		}
		
		if (!configFile.contains("game_settings.skip.age") || configFile.getInt("game_settings.skip.age") < 1) {
			Bukkit.broadcastMessage("Config for min skip age is not correct, use of default value.");
			configFile.set("game_settings.skip.age", 2);
		}
		
		if (!configFile.contains("game_settings.custom_crafts")) {
			Bukkit.broadcastMessage("Config for enabling custom crafts is not correct, use of default value.");
			configFile.set("game_settings.custom_crafts", true);
		}
		
		if (!configFile.contains("game_settings.see_block_count")) {
			Bukkit.broadcastMessage("Config for enabling block count display is not correct, use of default value.");
			configFile.set("game_settings.see_block_count", true);
		}
		
		saturation = configFile.getBoolean("game_settings.saturation");
		spread = configFile.getBoolean("game_settings.spreadplayers.enable");
		rewards = configFile.getBoolean("game_settings.rewards");
		skip = configFile.getBoolean("game_settings.skip");
		crafts = configFile.getBoolean("game_settings.custom_crafts");
		seeBlockCnt = configFile.getBoolean("game_settings.see_block_count");
		
		spreadMin = configFile.getInt("game_settings.spreadplayers.minimum_distance");
		spreadMax = configFile.getInt("game_settings.spreadplayers.maximum_area");
		blockPerAge = configFile.getInt("game_settings.block_per_age");
		skipAge = configFile.getInt("game_settings.skip.age");
		maxAges = configFile.getInt("game_settings.max_age");
		startTime = configFile.getInt("game_settings.start_time");
		addedTime = configFile.getInt("game_settings.time_added");
		level = Level.values()[configFile.getInt("game_settings.max_age")];
		
		lang = configFile.getString("game_settings.lang");
	}
	
	public String displayConfig() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("Saturation: ").append(saturation).append("\n");
		sb.append("Spreadplayers: ").append(spread).append("\n");
		sb.append("Spreadplayer min distance: ").append(spreadMin).append("\n");
		sb.append("Spreadplayer min distance: ").append(spreadMax).append("\n");
		sb.append("Rewards: ").append(rewards).append("\n");
		sb.append("Skip: ").append(skip).append("\n");
		
		sb.append("Crafts: ").append(crafts).append("\n");
		sb.append("Can see block cnt: ").append(seeBlockCnt).append("\n");
		sb.append("Nb block per age: ").append(blockPerAge).append("\n");
		sb.append("First Age for Skipping: ").append(skipAge).append("\n");
		sb.append("Max age: ").append(maxAges).append("\n");
		sb.append("Start duration: ").append(startTime).append("\n");
		sb.append("Added duration: ").append(addedTime).append("\n");
		
		sb.append("Lang: ").append(lang).append("\n");
		sb.append("Level: ").append(level).append("\n");
		
		return sb.toString();
	}
	
	public String setSaturation(String _saturation) {
		if (!_saturation.toLowerCase().equals("true") && !_saturation.toLowerCase().equals("false")) {
			return (booleanErrorMsg);
		}
		
		saturation = Boolean.getBoolean(_saturation);
		
		return null;
	}
	
	public void setSpreadPlayers(String _spread) {
		
	}
	
	public void setRewards(String _rewards) {
		
	}
	
	public void setSkip(String _skip) {
		
	}
	public void setCrafts(String _crafts) {
		
	}
	
	public void setBlockCnt(String _blockCnt) {
		
	}
}
