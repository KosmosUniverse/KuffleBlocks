package fr.kosmosuniverse.kuffle.Core;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.configuration.file.FileConfiguration;

import fr.kosmosuniverse.kuffle.KuffleMain;

public class Config {
	private boolean saturation;
	private boolean spread;
	private boolean rewards;
	private boolean skip;
	private boolean crafts;
	private boolean seeBlockCnt;
	private boolean team;
	private int teamSize;
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
		booleanElems.put("TEAM", "setTeam");
		
		booleanRet.put("SATURATION", ret);
		booleanRet.put("SPREADPLAYERS", ret);
		booleanRet.put("REWARDS", ret);
		booleanRet.put("SKIP", ret);
		booleanRet.put("CUSTOM_CRAFTS", ret);
		booleanRet.put("SEE_BLOCK_CNT", ret);
		booleanRet.put("TEAM", ret);
		
		intElems.put("SPREAD_MIN_DISTANCE", "setSpreadDistance");
		intElems.put("SPREAD_MIN_RADIUS", "setSpreadRadius");
		intElems.put("BLOCK_PER_AGE", "setBlockAge");
		intElems.put("FIRST_AGE_SKIP", "setFirstSkip");
		intElems.put("NB_AGE", "setMaxAge");
		intElems.put("START_DURATION", "setStartTime");
		intElems.put("ADDED_DURATION", "setAddedTime");
		intElems.put("TEAMSIZE", "setTeamSize");
		
		ret = new ArrayList<String>();
		
		for (int i = 1; i < 11; i++) {
			ret.add("" + i);
		}
		
		intRet.put("BLOCK_PER_AGE", ret);
		
		ret = new ArrayList<String>();

		for (int i = 2; i < 11; i++) {
			ret.add("" + i);
		}
		
		intRet.put("TEAMSIZE", ret);
		
		ret = new ArrayList<String>();
		
		ret.add("100");

		for (int i = 1; i < 11; i++) {
			ret.add("" + (i + 100));
		}
		
		intRet.put("SPREAD_MIN_DISTANCE", ret);
		
		ret = new ArrayList<String>();
		
		ret.add("500");

		for (int i = 1; i < 11; i++) {
			ret.add("" + (i + 100));
		}
		
		intRet.put("SPREAD_MIN_RADIUS", ret);
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
		
		stringElems.put("LANG", "setLang");
		stringElems.put("LEVEL", "setLevel");
		
		stringRet.put("LANG", km.langs);
		stringRet.put("LEVEL", ret);
	}
	
	public void setupConfig(KuffleMain km, FileConfiguration configFile) {
		if (!configFile.contains("game_settings.block_per_age") || configFile.getInt("game_settings.block_per_age") < 1) {
			System.out.println("Config for block per age is not correct, use of default value.");
			configFile.set("game_settings.block_per_age", 5);
		}
		
		if (!configFile.contains("game_settings.spreadplayers.minimum_distance") || configFile.getInt("game_settings.spreadplayers.minimum_distance") < 1) {
			System.out.println("Config for spreadplayers minimum distance is not correct, use of default value.");
			configFile.set("game_settings.spreadplayers.minimum_distance", 500);
		}
		
		if (!configFile.contains("game_settings.spreadplayers.minimum_radius") || configFile.getInt("game_settings.spreadplayers.minimum_radius") < configFile.getInt("game_settings.spreadplayers.minimum_distance")) {
			System.out.println("Config for spreadplayers maximum area is not correct, use of default value.");
			configFile.set("game_settings.spreadplayers.minimum_radius", 1000);
		}
		
		if (!configFile.contains("game_settings.start_time") || configFile.getInt("game_settings.start_time") < 1) {
			System.out.println("Config for start time is not correct, use of default value.");
			configFile.set("game_settings.start_time", 4);
		}
		
		if (!configFile.contains("game_settings.time_added") || configFile.getInt("game_settings.time_added") < 1) {
			System.out.println("Config for time added is not correct, use of default value.");
			configFile.set("game_settings.time_added", 2);
		}
		
		if (!configFile.contains("game_settings.max_age") || configFile.getInt("game_settings.max_age") < 1) {
			System.out.println("Config for max age is not correct, use of default value.");
			configFile.set("game_settings.max_age", 6);
		}
		
		if (!configFile.contains("game_settings.lang") || !km.langs.contains(configFile.getString("game_settings.lang"))) {
			System.out.println("Config for lang is not correct, use of default value.");
			configFile.set("game_settings.lang", "en");
		}
		
		if (!configFile.contains("game_settings.level") || configFile.getInt("game_settings.level") < 0 || configFile.getInt("game_settings.level") > 3) {
			System.out.println("Config for level is not correct, use of default value.");
			configFile.set("game_settings.level", 1);
		}
		
		if (!configFile.contains("game_settings.skip.enable")) {
			System.out.println("Config for enabling skip is not correct, use of default value.");
			configFile.set("game_settings.skip.enable", true);
		}
		
		if (!configFile.contains("game_settings.skip.age") || configFile.getInt("game_settings.skip.age") < 1) {
			System.out.println("Config for min skip age is not correct, use of default value.");
			configFile.set("game_settings.skip.age", 2);
		}
		
		if (!configFile.contains("game_settings.custom_crafts")) {
			System.out.println("Config for enabling custom crafts is not correct, use of default value.");
			configFile.set("game_settings.custom_crafts", true);
		}
		
		if (!configFile.contains("game_settings.see_block_count")) {
			System.out.println("Config for enabling block count display is not correct, use of default value.");
			configFile.set("game_settings.see_block_count", true);
		}
		
		if (!configFile.contains("game_settings.team.enable")) {
			System.out.println("Config for enabling team is not correct, use of default value.");
			configFile.set("game_settings.team.enable", false);
		}
		
		if (!configFile.contains("game_settings.team.size") || configFile.getInt("game_settings.team.size") < 2 || configFile.getInt("game_settings.team.size") > 10) {
			System.out.println("Config for max team size is not correct, use of default value.");
			configFile.set("game_settings.team.size", 2);
		}
		
		saturation = configFile.getBoolean("game_settings.saturation");
		spread = configFile.getBoolean("game_settings.spreadplayers.enable");
		rewards = configFile.getBoolean("game_settings.rewards");
		skip = configFile.getBoolean("game_settings.skip.enable");
		crafts = configFile.getBoolean("game_settings.custom_crafts");
		seeBlockCnt = configFile.getBoolean("game_settings.see_block_count");
		team = configFile.getBoolean("game_settings.team.enable");
		
		spreadMin = configFile.getInt("game_settings.spreadplayers.minimum_distance");
		spreadMax = configFile.getInt("game_settings.spreadplayers.minimum_radius");
		blockPerAge = configFile.getInt("game_settings.block_per_age");
		skipAge = configFile.getInt("game_settings.skip.age");
		maxAges = configFile.getInt("game_settings.max_age");
		startTime = configFile.getInt("game_settings.start_time");
		addedTime = configFile.getInt("game_settings.time_added");
		teamSize = configFile.getInt("game_settings.team.size");
		
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
		sb.append("Spreadplayer min radius: ").append(spreadMax).append("\n");
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
		sb.append("Level: ").append(Level.values()[level]).append("\n");
		sb.append("Team: ").append(team).append("\n");
		sb.append("Team Size: ").append(teamSize).append("\n");
		
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
	
	public boolean getTeam() {
		return team;
	}
	
	public int getTeamSize() {
		return teamSize;
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
	
	public int getSpreadDistance() {
		return spreadMin;
	}
	
	public int getSpreadRadius() {
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
	
	public void setTeam(boolean _team) {
		team = _team;
	}
	
	public void setTeamSize(int _teamSize) {
		teamSize = _teamSize;
	}
	
	public void setSpreadDistance(int _spreadMin) {
		spreadMin = _spreadMin;
	}
	
	public void setSpreadRadius(int _spreadMax) {
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
			if (_level.equals(Level.values()[cnt].toString().toUpperCase())) {
				level = cnt;
				return;
			}
		}
	}
	
	public void setLang(String _lang) {
		lang = _lang;
	}
}
