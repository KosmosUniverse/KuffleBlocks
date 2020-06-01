package fr.kosmosuniverse.kuffle.Core;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class RewardManager {
	public static synchronized HashMap<String, HashMap<String, Integer>> getAllRewards(File dataFolder) {
		HashMap<String, HashMap<String, Integer>> finalMap = new HashMap<String, HashMap<String, Integer>>();
		
		finalMap.put("Archaic_Age", getAgeRewards("Archaic_Age", dataFolder));
		finalMap.put("Classic_Age", getAgeRewards("Classic_Age", dataFolder));
		finalMap.put("Netheric_Age", getAgeRewards("Netheric_Age", dataFolder));
		finalMap.put("Heroic_Age", getAgeRewards("Heroic_Age", dataFolder));
		finalMap.put("Mythic_Age", getAgeRewards("Mythic_Age", dataFolder));
		
		return finalMap;
	}
	
	public static synchronized HashMap<String, Integer> getAgeRewards(String age, File dataFolder) {
		HashMap<String, Integer> ageRewards = new HashMap<String, Integer>();
		JSONObject rewards = new JSONObject();
		JSONParser jsonParser = new JSONParser();
		
		try {
			FileReader reader = null;
			if (dataFolder.getPath().contains("\\"))
				reader = new FileReader(dataFolder.getPath() + "\\rewards.json");
			else
				reader = new FileReader(dataFolder.getPath() + "/rewards.json");
			
			rewards = (JSONObject) jsonParser.parse(reader);
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		
		JSONObject ageObject = new JSONObject();
		
		ageObject = (JSONObject) rewards.get(age);
		
		for (Iterator<?> it = ageObject.keySet().iterator(); it.hasNext();) {
			String key = (String) it.next();
			Long tmp = (Long) ageObject.get(key);
			ageRewards.put(key, Integer.parseInt(tmp.toString()));
		}
		
		return ageRewards;
	}
	
	public static synchronized void givePlayerReward(HashMap<String, Integer> ageReward, Player p, String age) {
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		ItemStack container;
		switch (age) {
		case "Archaic":
			container = new ItemStack(Material.RED_SHULKER_BOX);
			break;
		case "Classic":
			container = new ItemStack(Material.ORANGE_SHULKER_BOX);
			break;
		case "Netheric":
			container = new ItemStack(Material.YELLOW_SHULKER_BOX);
			break;
		case "Heroic":
			container = new ItemStack(Material.LIME_SHULKER_BOX);
			break;
		case "Mythic":
			container = new ItemStack(Material.GREEN_SHULKER_BOX);
			break;
		default:
			container = new ItemStack(Material.BLUE_SHULKER_BOX);
			break;
		}
		BlockStateMeta containerMeta = (BlockStateMeta) container.getItemMeta();
		ShulkerBox box = (ShulkerBox) containerMeta.getBlockState();
		Inventory inv = box.getInventory();
		
		for (String k : ageReward.keySet()) {
			items.add(new ItemStack(Material.matchMaterial(k), ageReward.get(k)));
		}

		for (ItemStack it : items) {
			inv.addItem(it);
		}

		box.update();
		containerMeta.setBlockState(box);
		container.setItemMeta(containerMeta);
		
		ItemMeta itM = container.getItemMeta();
		itM.setDisplayName(age + "_Age");
		container.setItemMeta(itM);
		
		p.getInventory().addItem(container);
	}
}
