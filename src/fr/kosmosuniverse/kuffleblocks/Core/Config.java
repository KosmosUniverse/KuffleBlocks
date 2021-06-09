package fr.kosmosuniverse.kuffleblocks.Core;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.configuration.file.FileConfiguration;
import org.json.simple.JSONObject;

import fr.kosmosuniverse.kuffleblocks.KuffleMain;
import fr.kosmosuniverse.kuffleblocks.utils.Utils;

public class Config {
	private KuffleMain km;
	private boolean saturation;
	private boolean spread;
	private boolean rewards;
	private boolean skip;
	private boolean crafts;
	private boolean team;
	private boolean same;
	private boolean duoMode;
	private boolean sbttMode;
	private boolean gameEnd;
	private boolean endOne;
	private boolean passive;
	private int sbttAmount;
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

	public Config(KuffleMain _km) {
		km = _km;

		ret.add("TRUE");
		ret.add("FALSE");

		booleanElems.put("SATURATION", "setSaturation");
		booleanElems.put("SPREADPLAYERS", "setSpreadplayers");
		booleanElems.put("REWARDS", "setRewards");
		booleanElems.put("SKIP", "setSkip");
		booleanElems.put("CUSTOM_CRAFTS", "setCrafts");
		booleanElems.put("TEAM", "setTeam");
		booleanElems.put("SAME_MODE", "setSame");
		booleanElems.put("DOUBLE_MODE", "setDoubleMode");
		booleanElems.put("SBTT_MODE", "setSbttMode");
		booleanElems.put("AUTO_DETECT_END", "setGameEnd");
		booleanElems.put("END_WHEN_ONE", "setEndOne");
		booleanElems.put("PASSIVE", "setPassive");

		booleanRet.put("SATURATION", ret);
		booleanRet.put("SPREADPLAYERS", ret);
		booleanRet.put("REWARDS", ret);
		booleanRet.put("SKIP", ret);
		booleanRet.put("CUSTOM_CRAFTS", ret);
		booleanRet.put("TEAM", ret);
		booleanRet.put("SAME_MODE", ret);
		booleanRet.put("DOUBLE_MODE", ret);
		booleanRet.put("SBTT_MODE", ret);
		booleanRet.put("AUTO_DETECT_END", ret);
		booleanRet.put("END_WHEN_ONE", ret);
		booleanRet.put("PASSIVE", ret);

		intElems.put("SPREAD_MIN_DISTANCE", "setSpreadDistance");
		intElems.put("SPREAD_MIN_RADIUS", "setSpreadRadius");
		intElems.put("BLOCK_PER_AGE", "setBlockAge");
		intElems.put("FIRST_AGE_SKIP", "setFirstSkip");
		intElems.put("NB_AGE", "setMaxAge");
		intElems.put("START_DURATION", "setStartTime");
		intElems.put("ADDED_DURATION", "setAddedTime");
		intElems.put("TEAMSIZE", "setTeamSize");
		intElems.put("SBTT_AMOUNT", "setSbttAmount");
		
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

		for (int i = 1; i <= AgeManager.getAgeMaxNumber(km.ages) + 1; i++) {
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

		for (int i = 1; i < 10; i++) {
			ret.add("" + i);
		}
		
		intRet.put("SBTT_AMOUNT", ret);
		
		ret = new ArrayList<String>();

		for (Level l : km.levels) {
			ret.add(l.name);
		}

		stringElems.put("LANG", "setLang");
		stringElems.put("LEVEL", "setLevel");

		stringRet.put("LANG", km.langs);
		stringRet.put("LEVEL", ret);
	}

	public void setupConfig(KuffleMain km, FileConfiguration configFile) {
		if (!configFile.contains("game_settings.lang")
				|| !km.langs.contains(configFile.getString("game_settings.lang"))) {
			System.out.println("Config Lang parameter is not correct, use of default value.");
			configFile.set("game_settings.lang", "en");
		} else {
			lang = configFile.getString("game_settings.lang");
		}
		
		if (!configFile.contains("game_settings.block_per_age")
				|| configFile.getInt("game_settings.block_per_age") < 1) {
			System.out.println(Utils.getLangString(km, null, "CONFIG_DEFAULT").replace("<#>", "blocks per age"));
			configFile.set("game_settings.block_per_age", 5);
		}

		if (!configFile.contains("game_settings.spreadplayers.minimum_distance")
				|| configFile.getInt("game_settings.spreadplayers.minimum_distance") < 1) {
			System.out.println(Utils.getLangString(km, null, "CONFIG_DEFAULT").replace("<#>", "spreadplayers minimum distance"));
			configFile.set("game_settings.spreadplayers.minimum_distance", 500);
		}

		if (!configFile.contains("game_settings.spreadplayers.minimum_radius")
				|| configFile.getInt("game_settings.spreadplayers.minimum_radius") < configFile
						.getInt("game_settings.spreadplayers.minimum_distance")) {
			System.out.println(Utils.getLangString(km, null, "CONFIG_DEFAULT").replace("<#>", "spreadplayers minimum radius"));
			configFile.set("game_settings.spreadplayers.minimum_radius", 1000);
		}

		if (!configFile.contains("game_settings.start_time") || configFile.getInt("game_settings.start_time") < 1) {
			System.out.println(Utils.getLangString(km, null, "CONFIG_DEFAULT").replace("<#>", "start time"));
			configFile.set("game_settings.start_time", 4);
		}

		if (!configFile.contains("game_settings.time_added") || configFile.getInt("game_settings.time_added") < 1) {
			System.out.println(Utils.getLangString(km, null, "CONFIG_DEFAULT").replace("<#>", "time added"));
			configFile.set("game_settings.time_added", 2);
		}

		if (!configFile.contains("game_settings.max_age") || configFile.getInt("game_settings.max_age") < 1 || configFile.getInt("game_settings.max_age") > (AgeManager.getAgeMaxNumber(km.ages) + 1)) {
			System.out.println(Utils.getLangString(km, null, "CONFIG_DEFAULT").replace("<#>", "max ages"));
			configFile.set("game_settings.max_age", AgeManager.getAgeMaxNumber(km.ages) + 1);
		}

		if (!configFile.contains("game_settings.level") || configFile.getInt("game_settings.level") < 0
				|| configFile.getInt("game_settings.level") > 3) {
			System.out.println(Utils.getLangString(km, null, "CONFIG_DEFAULT").replace("<#>", "level"));
			configFile.set("game_settings.level", 1);
		}

		if (!configFile.contains("game_settings.skip.enable")) {
			System.out.println(Utils.getLangString(km, null, "CONFIG_DEFAULT").replace("<#>", "enabling skip"));
			configFile.set("game_settings.skip.enable", true);
		}

		if (!configFile.contains("game_settings.skip.age") || configFile.getInt("game_settings.skip.age") < 1) {
			System.out.println(Utils.getLangString(km, null, "CONFIG_DEFAULT").replace("<#>", "skip age"));
			configFile.set("game_settings.skip.age", 2);
		}

		if (!configFile.contains("game_settings.custom_crafts")) {
			System.out.println(Utils.getLangString(km, null, "CONFIG_DEFAULT").replace("<#>", "enabling custom crafts"));
			configFile.set("game_settings.custom_crafts", true);
		}

		if (!configFile.contains("game_settings.team.enable")) {
			System.out.println(Utils.getLangString(km, null, "CONFIG_DEFAULT").replace("<#>", "enabling team"));
			configFile.set("game_settings.team.enable", false);
		}

		if (!configFile.contains("game_settings.team.size") || configFile.getInt("game_settings.team.size") < 2
				|| configFile.getInt("game_settings.team.size") > 10) {
			System.out.println(Utils.getLangString(km, null, "CONFIG_DEFAULT").replace("<#>", "max team size"));
			configFile.set("game_settings.team.size", 2);
		}
		
		if (!configFile.contains("game_settings.same_mode")) {
			System.out.println(Utils.getLangString(km, null, "CONFIG_DEFAULT").replace("<#>", "enabling same mode"));
			configFile.set("game_settings.same_mode", false);
		}
		
		if (!configFile.contains("game_settings.auto_detect_game_end.enable")) {
			System.out.println(Utils.getLangString(km, null, "CONFIG_DEFAULT").replace("<#>", "enabling auto detect game end"));
			configFile.set("game_settings.auto_detect_game_end.enable", false);
		}
		
		gameEnd = configFile.getBoolean("game_settings.auto_detect_game_end.enable");
		
		if (!configFile.contains("game_settings.auto_detect_game_end.end_when_one") ||
				(!gameEnd && configFile.getBoolean("game_settings.auto_detect_game_end.end_when_one"))) {
			System.out.println(Utils.getLangString(km, null, "CONFIG_DEFAULT").replace("<#>", "game end when one"));
			configFile.set("game_settings.auto_detect_game_end.end_when_one", false);
		}
		
		if (!configFile.contains("game_settings.sbtt_mode.enable")) {
			System.out.println(Utils.getLangString(km, null, "CONFIG_DEFAULT").replace("<#>", "SBTT mode"));
			configFile.set("game_settings.sbtt_mode.enable", false);
		}
		
		if (!configFile.contains("game_settings.passive")) {
			System.out.println(Utils.getLangString(km, null, "CONFIG_DEFAULT").replace("<#>", "passive mode"));
			configFile.set("game_settings.passive", true);
		}
		
		if (!configFile.contains("game_settings.sbtt_mode.amount") ||
				configFile.getInt("game_settings.sbtt_mode.amount") < 1 ||
				configFile.getInt("game_settings.sbtt_mode.amount") > 9) {
			System.out.println(Utils.getLangString(km, null, "CONFIG_DEFAULT").replace("<#>", "SBTT item amount"));
			configFile.set("game_settings.sbtt_mode.amount", 4);
		}

		saturation = configFile.getBoolean("game_settings.saturation");
		spread = configFile.getBoolean("game_settings.spreadplayers.enable");
		rewards = configFile.getBoolean("game_settings.rewards");
		skip = configFile.getBoolean("game_settings.skip.enable");
		crafts = configFile.getBoolean("game_settings.custom_crafts");
		team = configFile.getBoolean("game_settings.team.enable");
		same = configFile.getBoolean("game_settings.same_mode");
		endOne = configFile.getBoolean("game_settings.auto_detect_game_end.end_when_one");
		sbttMode = configFile.getBoolean("game_settings.sbtt_mode.enable");
		passive = configFile.getBoolean("game_settings.passive");
		
		spreadMin = configFile.getInt("game_settings.spreadplayers.minimum_distance");
		spreadMax = configFile.getInt("game_settings.spreadplayers.minimum_radius");
		blockPerAge = configFile.getInt("game_settings.block_per_age");
		skipAge = configFile.getInt("game_settings.skip.age");
		maxAges = configFile.getInt("game_settings.max_age");
		startTime = configFile.getInt("game_settings.start_time");
		addedTime = configFile.getInt("game_settings.time_added");
		level = configFile.getInt("game_settings.level");
		teamSize = configFile.getInt("game_settings.team.size");
		sbttAmount = configFile.getInt("game_settings.sbtt_mode.amount");

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
		sb.append("Nb block per age: ").append(blockPerAge).append("\n");
		sb.append("First Age for Skipping: ").append(skipAge).append("\n");
		sb.append("Max age: ").append(maxAges).append("\n");
		sb.append("Start duration: ").append(startTime).append("\n");
		sb.append("Added duration: ").append(addedTime).append("\n");
		sb.append("Lang: ").append(lang).append("\n");
		sb.append("Level: ").append(LevelManager.getLevelByNumber(km.levels, level).name).append("\n");
		sb.append("Detect Game End: ").append(gameEnd).append("\n");
		sb.append("End game at one: ").append(endOne).append("\n");
		sb.append("Passive: ").append(passive).append("\n");
		sb.append("Team: ").append(team).append("\n");
		sb.append("Team Size: ").append(teamSize).append("\n");
		sb.append("Same mode: ").append(same).append("\n");
		sb.append("Double mode: ").append(duoMode).append("\n");
		sb.append("SBTT mode: ").append(sbttMode).append("\n");
		sb.append("SBTT amount: ").append(sbttAmount).append("\n");

		return sb.toString();
	}
	
	public void clear() {
		ret.clear();
		
		booleanElems.clear();
		intElems.clear();
		stringElems.clear();
		
		for (String config : booleanRet.keySet()) {
			booleanRet.get(config).clear();
		}
		
		for (String config : intRet.keySet()) {
			intRet.get(config).clear();
		}
		
		for (String config : stringRet.keySet()) {
			stringRet.get(config).clear();
		}
		
		booleanRet.clear();
		intRet.clear();
		stringRet.clear();
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject saveConfig() {
		JSONObject configObj = new JSONObject();
		
		configObj.put("saturation", saturation);
		configObj.put("spread", spread);
		configObj.put("rewards", rewards);
		configObj.put("skip", skip);
		configObj.put("crafts", crafts);
		configObj.put("team", team);
		configObj.put("same", same);
		configObj.put("duoMode", duoMode);
		configObj.put("sbttMode", sbttMode);
		configObj.put("gameEnd", gameEnd);
		configObj.put("endOne", endOne);
		configObj.put("sbttAmount", sbttAmount);
		configObj.put("teamSize", teamSize);
		configObj.put("spreadMin", spreadMin);
		configObj.put("spreadMax", spreadMax);
		configObj.put("blockPerAge", blockPerAge);
		configObj.put("skipAge", skipAge);
		configObj.put("maxAges", maxAges);
		configObj.put("startTime", startTime);
		configObj.put("addedTime", addedTime);
		configObj.put("level", level);
		configObj.put("lang", lang);
		
		return configObj;
	}
	
	public void loadConfig(JSONObject configObj) {
		saturation = (boolean) configObj.get("saturation");
		spread = (boolean) configObj.get("spread");
		rewards = (boolean) configObj.get("rewards");
		skip = (boolean) configObj.get("skip");
		crafts = (boolean) configObj.get("crafts");
		team = (boolean) configObj.get("team");
		same = (boolean) configObj.get("same");
		duoMode = (boolean) configObj.get("duoMode");
		sbttMode = (boolean) configObj.get("sbttMode");
		gameEnd = (boolean) configObj.get("gameEnd");
		endOne = (boolean) configObj.get("endOne");
		
		sbttAmount = Integer.parseInt(configObj.get("sbttAmount").toString());
		teamSize = Integer.parseInt(configObj.get("teamSize").toString());
		spreadMin = Integer.parseInt(configObj.get("spreadMin").toString());
		spreadMax = Integer.parseInt(configObj.get("spreadMax").toString());
		blockPerAge = Integer.parseInt(configObj.get("blockPerAge").toString());
		skipAge = Integer.parseInt(configObj.get("skipAge").toString());
		maxAges = Integer.parseInt(configObj.get("maxAges").toString());
		startTime = Integer.parseInt(configObj.get("startTime").toString());
		addedTime = Integer.parseInt(configObj.get("addedTime").toString());
		level = Integer.parseInt(configObj.get("level").toString());
		
		lang = configObj.get("lang").toString();
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

	public boolean getTeam() {
		return team;
	}
	
	public boolean getSame() {
		return same;
	}
	
	public boolean getDouble() {
		return duoMode;
	}
	
	public boolean getSBTT() {
		return sbttMode;
	}
	
	public boolean getGameEnd() {
		return gameEnd;
	}
	
	public boolean getEndOne() {
		return endOne;
	}
	
	public boolean getPassive() {
		return passive;
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
	
	public int getSBTTAmount() {
		return sbttAmount;
	}

	public Level getLevel() {
		return LevelManager.getLevelByNumber(km.levels, level);
	}

	public String getLang() {
		return lang;
	}

	public boolean setSaturation(boolean _saturation) {
		saturation = _saturation;
		return true;
	}

	public boolean setSpreadplayers(boolean _spread) {
		spread = _spread;
		return true;
	}

	public boolean setRewards(boolean _rewards) {
		rewards = _rewards;
		return true;
	}

	public boolean setSkip(boolean _skip) {
		skip = _skip;
		return true;
	}

	public boolean setCrafts(boolean _crafts) {
		crafts = _crafts;
		return true;
	}

	public boolean setTeam(boolean _team) {
		if (km.gameStarted) {
			return false;
		}

		team = _team;
		
		return true;
	}
	
	public boolean setSame(boolean _same) {
		if (km.gameStarted) {
			return false;
		}
		
		same = _same;
		
		return true;
	}
	
	public boolean setDoubleMode(boolean _duoMode) {
		duoMode = _duoMode;
		
		return true;
	}
	
	public boolean setSbttMode(boolean _sbttMode) {
		sbttMode = _sbttMode;
		
		return true;
	}
	
	public boolean setGameEnd(boolean _gameEnd) {
		gameEnd = _gameEnd;
		
		if (!gameEnd) {
			endOne = false;
		}
		
		return true;
	}
	
	public boolean setEndOne(boolean _endOne) {
		if (!gameEnd) {
			return false;
		}
		
		endOne = _endOne;
		
		return true;
	}
	
	public boolean setPassive(boolean _passive) {
		passive = _passive;
		
		return true;
	}

 	public boolean setTeamSize(int _teamSize) {
 		if (km.gameStarted) {
			return false;
		}
 		
		if (km.teams.getTeams().size() > 0 && km.teams.getMaxTeamSize() > _teamSize) {
			return false;
		}

		teamSize = _teamSize;
		return true;
	}

	public boolean setSpreadDistance(int _spreadMin) {
		spreadMin = _spreadMin;
		return true;
	}

	public boolean setSpreadRadius(int _spreadMax) {
		spreadMax = _spreadMax;
		return true;
	}

	public boolean setBlockAge(int _blockPerAge) {
		blockPerAge = _blockPerAge;
		return true;
	}

	public boolean setFirstSkip(int _skipAge) {
		if (_skipAge > maxAges) {
			return false;
		}
		
		skipAge = _skipAge;
		return true;
	}

	public boolean setMaxAge(int _maxAges) {
		if (_maxAges > (AgeManager.getAgeMaxNumber(km.ages) + 1)) {
			return false;
		}
		
		maxAges = _maxAges;

		ret = new ArrayList<String>();

		for (int i = 1; i < maxAges + 1; i++) {
			ret.add("" + i);
		}

		intRet.put("NB_AGE", ret);
		return true;
	}

	public boolean setStartTime(int _startTime) {
		startTime = _startTime;
		return true;
	}

	public boolean setAddedTime(int _addedTime) {
		addedTime = _addedTime;
		return true;
	}
	
	public boolean setSbttAmount(int _sbttAmount) {
		sbttAmount = _sbttAmount;
		
		return true;
	}

	public boolean setLevel(String _level) {
		Level l;
		
		if ((l = LevelManager.getLevelByName(km.levels, _level)) == null) {
			return false;
		}
		
		level = l.number;
		
		return true;
	}

	public boolean setLang(String _lang) {
		if (km.langs.contains(_lang)) {
			lang = _lang;
			return true;
		}
		
		return false;
	}
}
