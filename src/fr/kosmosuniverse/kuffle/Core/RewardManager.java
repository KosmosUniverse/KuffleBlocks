package fr.kosmosuniverse.kuffle.Core;

import java.io.File;
import java.io.FileReader;
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
	public static synchronized HashMap<String, HashMap<String, RewardElem>> getAllRewards(File dataFolder) {
		HashMap<String, HashMap<String, RewardElem>> finalMap = new HashMap<String, HashMap<String, RewardElem>>();
		
		finalMap.put("Archaic_Age", getAgeRewards("Archaic_Age", dataFolder));
		finalMap.put("Classic_Age", getAgeRewards("Classic_Age", dataFolder));
		finalMap.put("Mineric_Age", getAgeRewards("Mineric_Age", dataFolder));
		finalMap.put("Netheric_Age", getAgeRewards("Netheric_Age", dataFolder));
		finalMap.put("Heroic_Age", getAgeRewards("Heroic_Age", dataFolder));
		finalMap.put("Mythic_Age", getAgeRewards("Mythic_Age", dataFolder));
		
		return finalMap;
	}
	
	public static synchronized HashMap<String, RewardElem> getAgeRewards(String age, File dataFolder) {
		HashMap<String, RewardElem> ageRewards = new HashMap<String, RewardElem>();
		JSONObject rewards = new JSONObject();
		JSONParser jsonParser = new JSONParser();
		FileWriter writer = null;
		
		try {
			FileReader reader = null;
			if (dataFolder.getPath().contains("\\")) {
				reader = new FileReader(dataFolder.getPath() + "\\rewards.json");
				writer = new FileWriter(dataFolder.getPath() + "\\logs.txt", true);
			} else {
				reader = new FileReader(dataFolder.getPath() + "/rewards.json");
				writer = new FileWriter(dataFolder.getPath() + "/logs.txt", true);
			}
			
			rewards = (JSONObject) jsonParser.parse(reader);
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
	
	public static HashMap<String, PotionEffectType> getAllEffects() {
		HashMap<String, PotionEffectType> effectMap = new HashMap<String, PotionEffectType>();
		
		effectMap.put("absorption", PotionEffectType.ABSORPTION);
		effectMap.put("damage_resistance", PotionEffectType.DAMAGE_RESISTANCE);
		effectMap.put("dolphins_grace", PotionEffectType.DOLPHINS_GRACE);
		effectMap.put("fast_digging", PotionEffectType.FAST_DIGGING);
		effectMap.put("fire_resistance", PotionEffectType.FIRE_RESISTANCE);
		effectMap.put("heal", PotionEffectType.HEAL);
		effectMap.put("health_boost", PotionEffectType.HEALTH_BOOST);
		effectMap.put("invisibility", PotionEffectType.INVISIBILITY);
		effectMap.put("jump", PotionEffectType.JUMP);
		effectMap.put("luck", PotionEffectType.LUCK);
		effectMap.put("night_vision", PotionEffectType.NIGHT_VISION);
		effectMap.put("regeneration", PotionEffectType.REGENERATION);
		effectMap.put("speed", PotionEffectType.SPEED);
		effectMap.put("water_breathing", PotionEffectType.WATER_BREATHING);
		
		return effectMap;
	}
	
	public static synchronized void givePlayerReward(HashMap<String, RewardElem> ageReward, HashMap<String, PotionEffectType> effects, Player p, String age) {
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		ItemStack container;
		
		switch (age) {
		case "Archaic":
			container = new ItemStack(Material.RED_SHULKER_BOX);
			break;
		case "Classic":
			container = new ItemStack(Material.ORANGE_SHULKER_BOX);
			break;
		case "Mineric":
			container = new ItemStack(Material.YELLOW_SHULKER_BOX);
			break;
		case "Netheric":
			container = new ItemStack(Material.LIME_SHULKER_BOX);
			break;
		case "Heroic":
			container = new ItemStack(Material.GREEN_SHULKER_BOX);
			break;
		case "Mythic":
			container = new ItemStack(Material.BLUE_SHULKER_BOX);
			break;
		default:
			container = new ItemStack(Material.BLACK_SHULKER_BOX);
			break;
		}
		
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
				p.addPotionEffect(new PotionEffect(findEffect(effects, ageReward.get(k).getEffect()), 999999, 2));
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
		itM.setDisplayName(age + "_Age");
		container.setItemMeta(itM);
		
		p.getInventory().addItem(container);
	}
	
	public static void managePreviousEffects(HashMap<String, RewardElem> ageReward, HashMap<String, PotionEffectType> effects, Player p, String age) {
		for (String key : ageReward.keySet()) {
			if (key.contains("potion")) {
				p.removePotionEffect(findEffect(effects, ageReward.get(key).getEffect()));
			}
		}
	}
	
	private static Enchantment getEnchantment(String enchant) {
		for (Enchantment e : Enchantment.values()) {
			if (e.getKey().toString().contains(enchant)) {
				return e;
			}
		}
		return null;
	}

	private static PotionEffectType findEffect(HashMap<String, PotionEffectType> effects, String effect) {
		for (String key : effects.keySet()) {
			if (key.contains(effect)){
				return (effects.get(key));
			}
		}
		
		return null;
	}
}
