package fr.kosmosuniverse.kuffleblocks.Core;

import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class AgeManager {
	public static ArrayList<Age> getAges(String ageContent) {
		ArrayList<Age> finalList = new ArrayList<Age>();

		if (ageContent == null) {
			return null;
		}
		
		JSONObject jsonObj = new JSONObject();
		JSONParser parser = new JSONParser();
		
		try {
			jsonObj = (JSONObject) parser.parse(ageContent);
			
			for (Object key : jsonObj.keySet()) {
				JSONObject ageObj = (JSONObject) jsonObj.get(key);
				
				finalList.add(new Age((String) key,
						(Integer) Integer.parseInt(ageObj.get("Number").toString()),
						(String) ageObj.get("TextColor"),
						((String) ageObj.get("BoxColor") + "_SHULKER_BOX")));
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		
		return finalList;
	}
	
	public static boolean ageExists(ArrayList<Age> ages, String ageName) {
		for (Age age : ages) {
			if (age.name.equals(ageName)) {
				return true;
			}
		}
		
		return false;
	}
	
	public static Age getAgeByNumber(ArrayList<Age> ages, int ageNumber) {
		for (Age age : ages) {
			if (age.number == ageNumber) {
				return age;
			}
		}
		
		return getDefaultAge(ages);
	}
	
	public static Age getDefaultAge(ArrayList<Age> ages) {
		for (Age age : ages) {
			if (age.number == -1) {
				return age;
			}
		}
		
		return null;
	}
	
	public static int getAgeMaxNumber(ArrayList<Age> ages) {
		int max = 0;
		
		for (Age age : ages) {
			max = max < age.number ? age.number : max;
		}
		
		return max;
	}
}
