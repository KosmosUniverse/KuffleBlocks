package fr.kosmosuniverse.kuffleblocks.Core;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.kosmosuniverse.kuffleblocks.KuffleMain;
import fr.kosmosuniverse.kuffleblocks.Crafts.*;
import fr.kosmosuniverse.kuffleblocks.utils.Utils;

public class CraftsManager {
	private ArrayList<ACrafts> recipes = new ArrayList<ACrafts>();
	
	public CraftsManager(KuffleMain _km) {
		recipes.add(new EndPortalFrame(_km));
		
		if (!_km.config.getCrafts()) {
			return;
		}

		recipes.add(new Mycelium(_km));
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
		recipes.add(new Bell(_km));
		recipes.add(new Cobweb(_km));
		
		if (Utils.findVersionNumber(_km, Utils.getVersion()) >= Utils.findVersionNumber(_km, "1.17")) {
			recipes.add(new BuddingAmethyst(_km));
			recipes.add(new CoalOreDeepslate(_km));
			recipes.add(new CopperOreDeepslate(_km));
			recipes.add(new DiamondOreDeepslate(_km));
			recipes.add(new EmeraldOreDeepslate(_km));
			recipes.add(new GoldOreDeepslate(_km));
			recipes.add(new IronOreDeepslate(_km));
			recipes.add(new LapisOreDeepslate(_km));
			recipes.add(new RedstoneOreDeepslate(_km));
			recipes.add(new CopperOre(_km));
			recipes.add(new GoldOre(_km));
			recipes.add(new IronOre(_km));
			recipes.add(new RawCopper(_km));
			recipes.add(new RawGold(_km));
			recipes.add(new RawIron(_km));
			recipes.add(new MossBlock(_km));
			recipes.add(new SmallDripleaf(_km));
			recipes.add(new PowderSnowBucket(_km));
			recipes.add(new PointedDripstone(_km));
		}
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
