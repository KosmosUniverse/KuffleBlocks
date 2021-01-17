package fr.kosmosuniverse.kuffle.Core;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Material;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ChooseBlockInList {
	public static HashMap<String, ArrayList<String>> getAllBlocks(String blocksContent, File dataFolder) {
		HashMap<String, ArrayList<String>> finalMap = new HashMap<String, ArrayList<String>>();
		
		finalMap.put("Archaic_Age", getAgeBlocks("Archaic_Age", blocksContent, dataFolder));
		finalMap.put("Classic_Age", getAgeBlocks("Classic_Age", blocksContent, dataFolder));
		finalMap.put("Mineric_Age", getAgeBlocks("Mineric_Age", blocksContent, dataFolder));
		finalMap.put("Netheric_Age", getAgeBlocks("Netheric_Age", blocksContent, dataFolder));
		finalMap.put("Heroic_Age", getAgeBlocks("Heroic_Age", blocksContent, dataFolder));
		finalMap.put("Mythic_Age", getAgeBlocks("Mythic_Age", blocksContent, dataFolder));
		
		return finalMap;
	}
	
	public static synchronized ArrayList<String> getAgeBlocks(String age, String blocksContent, File dataFolder) {
		ArrayList<String> finalList = new ArrayList<String>();
		JSONObject blocks = new JSONObject();
		JSONParser jsonParser = new JSONParser();
		FileWriter writer = null;
		
		try {
			if (dataFolder.getPath().contains("\\")) {
				writer = new FileWriter(dataFolder.getPath() + "\\logs.txt");
			} else {
				writer = new FileWriter(dataFolder.getPath() + "/logs.txt");
			}
			
			blocks = (JSONObject) jsonParser.parse(blocksContent);
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		
		JSONObject ageObject = new JSONObject();
		JSONArray agePart = new JSONArray();
		
		ageObject = (JSONObject) blocks.get(age);
					
		for (Object k : ageObject.keySet()) {
			agePart = (JSONArray) ageObject.get(k);
			for (int j = 0; j < agePart.size(); j++) {
				finalList.add((String) agePart.get(j));
				if (Material.matchMaterial((String) agePart.get(j)) == null) {
					try {
						writer.append((String) agePart.get(j));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		return finalList;
	}

	public static synchronized String newBlock(ArrayList<String> done, ArrayList<String> allAgeBlocks) {	
		ArrayList<String> finalList = new ArrayList<String>();
		
		for (String s : allAgeBlocks) {
			if (!done.contains(s)) {
				finalList.add(s);
			}
		}
		
		Random r = new Random();
		
		return finalList.get(r.nextInt(finalList.size()));
	}
}
