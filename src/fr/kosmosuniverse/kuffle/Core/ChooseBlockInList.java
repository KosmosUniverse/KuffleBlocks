package fr.kosmosuniverse.kuffle.Core;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
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
	
	public static HashMap<String, ArrayList<Inventory>> getBlocksInvs(HashMap<String, ArrayList<String>> allblocks) {
		HashMap<String, ArrayList<Inventory>> invs = new HashMap<String, ArrayList<Inventory>>();
		for (String age : allblocks.keySet()) {
			invs.put(age, getAgeInvs(age, allblocks.get(age)));
		}
		
		return invs;
	}

	public static ArrayList<Inventory> getAgeInvs(String age, ArrayList<String> ageBlocks) {
		ArrayList<Inventory> invs = new ArrayList<Inventory>();
		Inventory inv;
		int invCnt = 0;
		int nbInv = 1;
		
		ItemStack limePane = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
		ItemStack redPane = new ItemStack(Material.RED_STAINED_GLASS_PANE);
		ItemStack bluePane = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
		ItemMeta itM =  limePane.getItemMeta();
		
		itM = limePane.getItemMeta();
		itM.setDisplayName(" ");
		limePane.setItemMeta(itM);
		itM = redPane.getItemMeta();
		itM.setDisplayName("<- Previous");
		redPane.setItemMeta(itM);
		itM = bluePane.getItemMeta();
		itM.setDisplayName("Next ->");
		bluePane.setItemMeta(itM);
		
		if (ageBlocks.size() > 45) {
			inv = Bukkit.createInventory(null, 54, "§8" + age + " blocks Tab 1");
		} else {
			inv = Bukkit.createInventory(null, 54, "§8" + age + " blocks");
		}
		
		for (; invCnt < 9; invCnt++) {
			if (invCnt == 8) {
				inv.setItem(invCnt, bluePane);
			} else {
				inv.setItem(invCnt, limePane);
			}
		}
		
		for (String item : ageBlocks) {
			try {
				inv.setItem(invCnt, getMaterial(item));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
			
			if (invCnt == 53) {
				invCnt = 0;
				invs.add(inv);
				nbInv++;
				inv = Bukkit.createInventory(null, 54, "§8" + age + " blocks Tab " + nbInv);
				
				for (; invCnt < 9; invCnt++) {
					if (invCnt == 0) {
						inv.setItem(invCnt, redPane);
					} else if (invCnt == 8) {
						inv.setItem(invCnt, bluePane);
					} else {
						inv.setItem(invCnt, limePane);
					}
				}
			} else {
				invCnt++;
			}
		}
		
		inv.setItem(8, limePane);
		
		invs.add(inv);
		
		return invs;
	}
	
	private static ItemStack getMaterial(String item) {
		for (Material mat : Material.values()) {
			if (mat.getKey().toString().split(":")[1].equals(item) && mat.isItem()) {
				return new ItemStack(mat);
			}
		}
		
		ItemStack retItem;
		
		for (Material mat : Material.values()) {
			if (mat.getKey().toString().split(":")[1].contains(item) && mat.isItem()) {
				retItem = new ItemStack(mat);

				ItemMeta itM = retItem.getItemMeta();
				
				itM.setDisplayName(item);
				retItem.setItemMeta(itM);
				
				return retItem;
			}
		}
		
		for (Material mat : Material.values()) {
			if (item.contains(mat.getKey().toString().split(":")[1]) && mat.isItem()) {
				retItem = new ItemStack(mat);

				ItemMeta itM = retItem.getItemMeta();
				
				itM.setDisplayName(item);
				retItem.setItemMeta(itM);
				
				return retItem;
			}
		}
		
		return null;
	}
}
