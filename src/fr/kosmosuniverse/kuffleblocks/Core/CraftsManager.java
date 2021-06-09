package fr.kosmosuniverse.kuffleblocks.Core;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.kosmosuniverse.kuffleblocks.KuffleMain;
import fr.kosmosuniverse.kuffleblocks.Crafts.ACrafts;
import fr.kosmosuniverse.kuffleblocks.Crafts.Bell;
import fr.kosmosuniverse.kuffleblocks.Crafts.Coal;
import fr.kosmosuniverse.kuffleblocks.Crafts.CoalOre;
import fr.kosmosuniverse.kuffleblocks.Crafts.Diamond;
import fr.kosmosuniverse.kuffleblocks.Crafts.DiamondOre;
import fr.kosmosuniverse.kuffleblocks.Crafts.Emerald;
import fr.kosmosuniverse.kuffleblocks.Crafts.EmeraldOre;
import fr.kosmosuniverse.kuffleblocks.Crafts.EndPortalFrame;
import fr.kosmosuniverse.kuffleblocks.Crafts.Lapis;
import fr.kosmosuniverse.kuffleblocks.Crafts.LapisOre;
import fr.kosmosuniverse.kuffleblocks.Crafts.MossyCobblestone;
import fr.kosmosuniverse.kuffleblocks.Crafts.MossyStoneBrick;
import fr.kosmosuniverse.kuffleblocks.Crafts.Quartz;
import fr.kosmosuniverse.kuffleblocks.Crafts.QuartzOre;
import fr.kosmosuniverse.kuffleblocks.Crafts.RedNetherBrick;
import fr.kosmosuniverse.kuffleblocks.Crafts.RedSand;
import fr.kosmosuniverse.kuffleblocks.Crafts.Redstone;
import fr.kosmosuniverse.kuffleblocks.Crafts.RedstoneOre;
import fr.kosmosuniverse.kuffleblocks.utils.Utils;

public class CraftsManager {
	private ArrayList<ACrafts> recipes = new ArrayList<ACrafts>();
	
	public CraftsManager(KuffleMain _km) {
		if (!_km.config.getCrafts()) {
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
		recipes.add(new Quartz(_km));
		
		recipes.add(new CoalOre(_km));
		recipes.add(new LapisOre(_km));
		recipes.add(new RedstoneOre(_km));
		recipes.add(new DiamondOre(_km));
		recipes.add(new EmeraldOre(_km));
		recipes.add(new QuartzOre(_km));
		
		recipes.add(new RedNetherBrick(_km));
		
		recipes.add(new EndPortalFrame(_km));
		recipes.add(new Bell(_km));
	}
	
	public void clear() {
		if (recipes != null) {
			recipes.clear();
		}
	}
	
	public void addCraft(ACrafts craft) {
		recipes.add(craft);
	}
	
	public void removeCraft(String name) {
		ACrafts craft = null;
		
		for (ACrafts tmp : recipes) {
			if (tmp.getName().equals(name)) {
				craft = tmp;
			}
		}
		
		if (craft != null) {
			recipes.remove(craft);
		}
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
	
	public ACrafts findCraftInventoryByItem(ItemStack item) {
		for (ACrafts craft : recipes) {
			if (Utils.compareItems(craft.getItem(), item, item.hasItemMeta(),
					item.hasItemMeta() ? item.getItemMeta().hasDisplayName() : false,
					item.hasItemMeta() ? item.getItemMeta().hasLore() : false)) {
				return (craft);
			}
		}
		
		return null;
	}
	
	public ACrafts findCraftByInventoryName(String invName) {
		for (ACrafts craft : recipes) {
			String name = "§8" + craft.getName();
			
			if (invName.contains(name)) {
				return (craft);
			}
		}
		
		return null;
	}
	
	public ItemStack findItemByName(String itemName) {
		for (ACrafts craft : recipes) {
			if (itemName.equals(craft.getName())) {
				return (craft.getItem());
			}
		}
		
		return null;
	}
}
