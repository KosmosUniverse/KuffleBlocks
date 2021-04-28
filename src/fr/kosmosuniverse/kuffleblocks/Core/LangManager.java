package fr.kosmosuniverse.kuffleblocks.Core;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class LangManager {
	public static HashMap<String, HashMap<String, String>> getAllBlocksLang(String JSONContent, File dataFolder) {
		HashMap<String, HashMap<String, String>> finalMap = new HashMap<String, HashMap<String, String>>();
		JSONObject langages = new JSONObject();
		JSONParser jsonParser = new JSONParser();
		FileWriter writer = null;
		
		try {
			if (dataFolder.getPath().contains("\\")) {
				writer = new FileWriter(dataFolder.getPath() + "\\logs.txt", true);
			} else {
				writer = new FileWriter(dataFolder.getPath() + "/logs.txt", true);
			}
			
			langages = (JSONObject) jsonParser.parse(JSONContent);
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		
		try {
			for (Iterator<?> itBlock = langages.keySet().iterator(); itBlock.hasNext();) {
				String keyBlock = (String) itBlock.next();
				JSONObject block = (JSONObject) langages.get(keyBlock);
				
				
				writer.append(keyBlock);
				
				
				HashMap<String, String> blockLangs = new HashMap<String, String>();
				
				for (Iterator<?> itLang = block.keySet().iterator(); itLang.hasNext();) {
					String keyLang = (String) itLang.next();
					String value = (String) block.get(keyLang);
					
					writer.append("\t" + keyLang + " : " + value + "\n");
					blockLangs.put(keyLang, value);
				}
				
				finalMap.put(keyBlock, blockLangs);
			}
			
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return finalMap;
	}
	
	public static String findBlockDisplay(HashMap<String, HashMap<String, String>> allLangs, String block, String lang) {
		String res = null;
		
		if (allLangs.containsKey(block)) {
			HashMap<String, String> langs = allLangs.get(block);
			
			if (langs.containsKey(lang) && langs.get(lang) != "") {
				return langs.get(lang);
			} else {
				return block;
			}
		}
			
		return res;
	}
	
	public static ArrayList<String> findAllLangs(HashMap<String, HashMap<String, String>> allBlocksLangs) {
		ArrayList<String> allLangs = new ArrayList<String>();
		
		HashMap<String, String> first = allBlocksLangs.get(allBlocksLangs.keySet().toArray()[0]);
		
		for (String key : first.keySet()) {
			allLangs.add(key);
		}
		
		return allLangs;
	}
}
