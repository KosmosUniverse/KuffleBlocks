package fr.kosmosuniverse.kuffle.Crafts;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import fr.kosmosuniverse.kuffle.KuffleMain;

public class ManageCrafts {
	private ArrayList<ACrafts> recipes = new ArrayList<ACrafts>();
	
	public ManageCrafts(KuffleMain _km) {
		if (!_km.getConfig().getBoolean("game_settings.custom_crafts")) {
			return;
		}
		
		recipes.add(new RedSand(_km));
		recipes.add(new MossyCobblestone(_km));
		recipes.add(new MossyStoneBrick(_km));
		
		recipes.add(new Coal(_km));
		recipes.add(new Lapis(_km));
		recipes.add(new Redstone(_km));
		recipes.add(new Diamond(_km));
		recipes.add(new Emerald(_km));
		
		recipes.add(new CoalOre(_km));
		recipes.add(new LapisOre(_km));
		recipes.add(new RedstoneOre(_km));
		recipes.add(new DiamondOre(_km));
		recipes.add(new EmeraldOre(_km));
	}
	
	public ArrayList<ACrafts> getRecipeList() {
		return (recipes);
	}
	
	public Inventory getAllCraftsInventory() {
		Inventory inv = Bukkit.createInventory(null, getNbRows(), "§8AllCustomCrafts");
		int i = 0;
		
		for (ACrafts item : recipes) {
			inv.setItem(i, item.getItem());
			i++;
		}
		
		return (inv);
	}
	
	private int getNbRows() {
		int rows = recipes.size() / 9;
		
		if (recipes.size() % 9 == 0) {
			return rows * 9;
		} else {
			return (rows + 1) * 9;
		}
	}
	
	public ACrafts findCraftInventoryByItem(Material item) {
		for (ACrafts craft : recipes) {
			if (craft.getItem().getType() == item) {
				return (craft);
			}
		}
		
		return null;
	}
	
	public ACrafts findCraftByInventoryName(String invName) {
		for (ACrafts craft : recipes) {
			if (invName.contains(craft.getName())) {
				return (craft);
			}
		}
		
		return null;
	}
}
