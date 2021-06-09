package fr.kosmosuniverse.kuffleblocks.Core;

import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class LevelManager {
	public static ArrayList<Level> getLevels(String content) {
		ArrayList<Level> finalList = new ArrayList<Level>();

		if (content == null) {
			return null;
		}
		
		JSONObject jsonObj = new JSONObject();
		JSONParser parser = new JSONParser();
		
		try {
			jsonObj = (JSONObject) parser.parse(content);
			
			for (Object key : jsonObj.keySet()) {
				JSONObject levelObj = (JSONObject) jsonObj.get(key);
				
				finalList.add(new Level((String) key,
						(Integer) Integer.parseInt(levelObj.get("Number").toString()),
						(Integer) Integer.parseInt(levelObj.get("Seconds").toString()),
						Boolean.parseBoolean(levelObj.get("Lose").toString())));
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		
		return finalList;
	}
	
	public static Level getLevelByNumber(ArrayList<Level> levels, int levelNumber) {
		for (Level level : levels) {
			if (level.number == levelNumber) {
				return level;
			}
		}
		
		return null;
	}
	
	public static Level getLevelByName(ArrayList<Level> levels, String levelName) {
		for (Level level : levels) {
			if (level.name.equalsIgnoreCase(levelName)) {
				return level;
			}
		}
		
		return null;
	}
	
	public static int getLevelMaxNumber(ArrayList<Level> levels) {
		int max = 0;
		
		for (Level level : levels) {
			max = max < level.number ? level.number : max;
		}
		
		return max;
	}
}
