package fr.kosmosuniverse.kuffleblocks.Core;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class RewardManager {
	public static synchronized HashMap<String, HashMap<String, RewardElem>> getAllRewards(ArrayList<Age> ages, String rewardsContent, File dataFolder) {
		HashMap<String, HashMap<String, RewardElem>> finalMap = new HashMap<String, HashMap<String, RewardElem>>();
		
		int max = AgeManager.getAgeMaxNumber(ages);
		
		for (int ageCnt = 0; ageCnt <= max; ageCnt++) {
			finalMap.put(AgeManager.getAgeByNumber(ages, ageCnt).name, getAgeRewards(AgeManager.getAgeByNumber(ages, ageCnt).name, rewardsContent, dataFolder));
		}
		
		return finalMap;
	}
	
	public static synchronized HashMap<String, RewardElem> getAgeRewards(String age, String rewardsContent, File dataFolder) {
		HashMap<String, RewardElem> ageRewards = new HashMap<String, RewardElem>();
		JSONObject rewards = new JSONObject();
		JSONParser jsonParser = new JSONParser();
		FileWriter writer = null;
		
		try {
			if (dataFolder.getPath().contains("\\")) {
				writer = new FileWriter(dataFolder.getPath() + "\\logs.txt", true);
			} else {
				writer = new FileWriter(dataFolder.getPath() + "/logs.txt", true);
			}
			
			rewards = (JSONObject) jsonParser.parse(rewardsContent);
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		
		JSONObject ageObject = new JSONObject();
		
		ageObject = (JSONObject) rewards.get(age);
		
		for (Iterator<?> it = ageObject.keySet().iterator(); it.hasNext();) {
			String key = (String) it.next();
			JSONObject tmp = (JSONObject) ageObject.get(key);
			ageRewards.put(key, new RewardElem(key, Integer.parseInt(((Long) tmp.get("Amount")).toString()), (String) tmp.get("Enchant"), Integer.parseInt(((Long) tmp.get("Level")).toString()), (String) tmp.get("Effect")));
		}
		
		try {
			StringBuilder sb = new StringBuilder();

			for (String key : ageRewards.keySet()) {
				sb.append(ageRewards.get(key).toString()).append("\n");
			}
			
			writer.append(sb.toString());
			
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return ageRewards;
	}
	
	public static synchronized void givePlayerRewardEffect(HashMap<String, RewardElem> ageReward, Player p, String age) {
		for (String k : ageReward.keySet()) {
			if (k.contains("potion")) {				
				p.addPotionEffect(new PotionEffect(findEffect(ageReward.get(k).getEffect()), 999999, ageReward.get(k).getAmount()));
			}
		}
	}
	
	public static synchronized void givePlayerReward(HashMap<String, RewardElem> ageReward, Player p, ArrayList<Age> ages, int age) {
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		
		ItemStack container = new ItemStack(AgeManager.getAgeByNumber(ages, age).box);
		
		BlockStateMeta containerMeta = (BlockStateMeta) container.getItemMeta();
		ShulkerBox box = (ShulkerBox) containerMeta.getBlockState();
		Inventory inv = box.getInventory();
		
		for (String k : ageReward.keySet()) {
			ItemStack it;
			
			if (ageReward.get(k).enchant()) {
				it = new ItemStack(Material.matchMaterial(k), ageReward.get(k).getAmount());
				
				if (ageReward.get(k).getEnchant().contains(",")) {
					String[] tmp = ageReward.get(k).getEnchant().split(",");
					
					for (String enchant : tmp) {
						if (getEnchantment(enchant) != null) {
							it.addUnsafeEnchantment(getEnchantment(enchant), ageReward.get(k).getLevel());
						}
					}
				} else {
					if (getEnchantment(ageReward.get(k).getEnchant()) != null) {
						it.addUnsafeEnchantment(getEnchantment(ageReward.get(k).getEnchant()), ageReward.get(k).getLevel());		
					}
				}
				
				items.add(new ItemStack(it));
			} else if (k.contains("potion")) {
				if (ageReward.get(k).getEffect().contains(",")) {
					String[] tmp = ageReward.get(k).getEffect().split(",");
					
					for (String effect : tmp) {
						if (getEnchantment(effect) != null) {
							p.addPotionEffect(new PotionEffect(findEffect(ageReward.get(k).getEffect()), 999999, ageReward.get(k).getAmount()));
						}
					}
				} else {
					p.addPotionEffect(new PotionEffect(findEffect(ageReward.get(k).getEffect()), 999999, ageReward.get(k).getAmount()));
				}
			} else {
				it = new ItemStack(Material.matchMaterial(k), ageReward.get(k).getAmount());
				items.add(new ItemStack(it));
			}
		}

		for (ItemStack it : items) {
			inv.addItem(it);
		}

		box.update();
		containerMeta.setBlockState(box);
		container.setItemMeta(containerMeta);
		
		ItemMeta itM = container.getItemMeta();
		itM.setDisplayName(AgeManager.getAgeByNumber(ages, age).name.replace("_", " "));
		container.setItemMeta(itM);
		
		HashMap<Integer, ItemStack> ret = p.getInventory().addItem(container);
		
		if (ret != null) {
			for (Integer i : ret.keySet()) {
				p.getWorld().dropItem(p.getLocation(), ret.get(i));
			}
		}
	}
	
	public static void managePreviousEffects(HashMap<String, RewardElem> ageReward, Player p, String age) {
		for (String key : ageReward.keySet()) {
			if (key.contains("potion")) {
				p.removePotionEffect(findEffect(ageReward.get(key).getEffect()));
			}
		}
	}
	
	public static Enchantment getEnchantment(String enchant) {
		for (Enchantment e : Enchantment.values()) {
			if (e.getKey().toString().split(":")[1].equals(enchant)) {
				return e;
			}
		}
		return null;
	}

	private static PotionEffectType findEffect(String effect) {
		for (PotionEffectType potion : PotionEffectType.values()) {
			if (potion.getName().equalsIgnoreCase(effect)) {
				return potion;
			}
		}
		
		return null;
	}
}
