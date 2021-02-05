package fr.kosmosuniverse.kuffle.Core;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import fr.kosmosuniverse.kuffle.KuffleMain;

public class Config {
	private boolean saturation;
	private boolean spread;
	private boolean rewards;
	private boolean skip;
	private boolean crafts;
	private boolean seeBlockCnt;
	private int spreadMin;
	private int spreadMax;
	private int blockPerAge;
	private int skipAge;
	private int maxAges;
	private int startTime;
	private int addedTime;
	private int level;
	private String lang;
	
	private ArrayList<String> ret = new ArrayList<String>();
	
	public HashMap<String, String> booleanElems = new HashMap<String, String>();
	public HashMap<String, String> intElems = new HashMap<String, String>();
	public HashMap<String, String> stringElems = new HashMap<String, String>();
	
	public HashMap<String, ArrayList<String>> booleanRet = new HashMap<String, ArrayList<String>>();
	public HashMap<String, ArrayList<String>> intRet = new HashMap<String, ArrayList<String>>();
	public HashMap<String, ArrayList<String>> stringRet = new HashMap<String, ArrayList<String>>();
	
	public String booleanErrorMsg = "Invalid value ! True or False awaited.";
	public String stringErrorMsg = "Invalid value !";
	public String intErrorMsg = "Invalid value !";
	
	public Config(KuffleMain km) {
		ret.add("TRUE");
		ret.add("FALSE");
		
		booleanElems.put("SATURATION", "setSaturation");
		booleanElems.put("SPREADPLAYERS", "setSpreadplayers");
		booleanElems.put("REWARDS", "setRewards");
		booleanElems.put("SKIP", "setSkip");
		booleanElems.put("CUSTOM_CRAFTS", "setCrafts");
		booleanElems.put("SEE_BLOCK_CNT", "setBlockCnt");
		
		booleanRet.put("SATURATION", ret);
		booleanRet.put("SPREADPLAYERS", ret);
		booleanRet.put("REWARDS", ret);
		booleanRet.put("SKIP", ret);
		booleanRet.put("CUSTOM_CRAFTS", ret);
		booleanRet.put("SEE_BLOCK_CNT", ret);
		
		intElems.put("SPREAD_MIN_DISTANCE", "setSpreadMin");
		intElems.put("SPREAD_MAX_DISTANCE", "setSpreadMax");
		intElems.put("BLOCK_PER_AGE", "setBlockAge");
		intElems.put("FIRST_AGE_SKIP", "setFirstSkip");
		intElems.put("NB_AGE", "setMaxAge");
		intElems.put("START_DURATION", "setStartTime");
		intElems.put("ADDED_DURATION", "setAddedTime");
		intElems.put("LEVEL", "setLevel");
		
		ret = new ArrayList<String>();
		
		for (int i = 1; i < 11; i++) {
			ret.add("" + i);
		}
		
		intRet.put("BLOCK_PER_AGE", ret);
		
		ret = new ArrayList<String>();
		
		ret.add("50");

		for (int i = 1; i < 11; i++) {
			ret.add("" + (i * 100));
		}
		
		intRet.put("SPREAD_MIN_DISTANCE", ret);
		
		ret = new ArrayList<String>();
		
		ret.add("100");

		for (int i = 1; i < 11; i++) {
			ret.add("" + (i * 150));
		}
		
		intRet.put("SPREAD_MAX_DISTANCE", ret);
		intRet.put("FIRST_AGE_SKIP", null);
		
		ret = new ArrayList<String>();
		
		for (int i = 1; i < 7; i++) {
			ret.add("" + i);
		}
		
		intRet.put("NB_AGE", ret);
		
		ret = new ArrayList<String>();
		
		for (int i = 1; i < 7; i++) {
			ret.add("" + i);
		}
		
		intRet.put("START_DURATION", ret);
		
		ret = new ArrayList<String>();
		
		for (int i = 1; i < 6; i++) {
			ret.add("" + i);
		}
		
		intRet.put("ADDED_DURATION", ret);
		
		ret = new ArrayList<String>();
		
		for (Level l : Level.values()) {
			ret.add(l.toString());
		}
		
		intRet.put("LEVEL", ret);
		
		stringElems.put("LANG", "setLang");
		
		stringRet.put("LANG", km.langs);
	}
	
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
		skip = configFile.getBoolean("game_settings.skip.enable");
		crafts = configFile.getBoolean("game_settings.custom_crafts");
		seeBlockCnt = configFile.getBoolean("game_settings.see_block_count");
		
		spreadMin = configFile.getInt("game_settings.spreadplayers.minimum_distance");
		spreadMax = configFile.getInt("game_settings.spreadplayers.maximum_area");
		blockPerAge = configFile.getInt("game_settings.block_per_age");
		skipAge = configFile.getInt("game_settings.skip.age");
		maxAges = configFile.getInt("game_settings.max_age");
		startTime = configFile.getInt("game_settings.start_time");
		addedTime = configFile.getInt("game_settings.time_added");
		
		for (int cnt = 0; cnt < Level.values().length; cnt++) {
			if (Level.valueOf(configFile.getString("game_settings.level")) == Level.values()[cnt]) {
				level = cnt;
			}
		}
		
		lang = configFile.getString("game_settings.lang");
		
		ret = new ArrayList<String>();
		
		for (int i = 1; i < maxAges + 1; i++) {
			ret.add("" + i);
		}
		
		intRet.put("FIRST_AGE_SKIP", ret);
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
	
	public boolean getSaturation() {
		return saturation;
	}

	public boolean getSpread() {
		return spread;
	}
	
	public boolean getRewards() {
		return rewards;
	}
	
	public boolean getSkip() {
		return skip;
	}
	
	public boolean getCrafts() {
		return crafts;
	}

	public boolean getSeeBlockCnt() {
		return seeBlockCnt;
	}
	
	public int getBlockPerAge() {
		return blockPerAge;
	}

	public int getSkipAge() {
		return skipAge;
	}

	public int getMaxAges() {
		return maxAges;
	}
	
	public int getStartTime() {
		return startTime;
	}
	
	public int getAddedTime() {
		return addedTime;
	}
	
	public int getSpreadMin() {
		return spreadMin;
	}
	
	public int getSpreadMax() {
		return spreadMax;
	}
	
	public Level getLevel() {
		return Level.values()[level];
	}
	
	public String getLang() {
		return lang;
	}
	
	public void setSaturation(boolean _saturation) {
		saturation = _saturation;
	}
	
	public void setSpreadplayers(boolean _spread) {
		spread = _spread;
	}
	
	public void setRewards(boolean _rewards) {
		rewards = _rewards;
	}
	
	public void setSkip(boolean _skip) {
		skip = _skip;
	}
	public void setCrafts(boolean _crafts) {
		crafts = _crafts;
	}
	
	public void setBlockCnt(boolean _seeBlockCnt) {
		seeBlockCnt = _seeBlockCnt;
	}
	
	public void setSpreadMin(int _spreadMin) {
		spreadMin = _spreadMin;
	}
	
	public void setSpreadMax(int _spreadMax) {
		spreadMax = _spreadMax;
	}
	
	public void setBlockAge(int _blockPerAge) {
		blockPerAge = _blockPerAge;
	}
	
	public void setFirstSkip(int _skipAge) {
		skipAge = _skipAge;
	}
	
	public void setMaxAge(int _maxAges) {
		maxAges = _maxAges;
		
		ret = new ArrayList<String>();
		
		for (int i = 1; i < maxAges + 1; i++) {
			ret.add("" + i);
		}
		
		intRet.put("FIRST_AGE_SKIP", ret);
	}
	
	public void setStartTime(int _startTime) {
		startTime = _startTime;
	}
	
	public void setAddedTime(int _addedTime) {
		addedTime = _addedTime;
	}
	
	public void setLevel(String _level) {
		for (int cnt = 0; cnt < Level.values().length; cnt++) {
			if (Level.valueOf(_level) == Level.values()[cnt]) {
				level = cnt;
			}
		}
	}
	
	public void setLang(String _lang) {
		lang = _lang;
	}
}
