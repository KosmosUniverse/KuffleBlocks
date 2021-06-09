package fr.kosmosuniverse.kuffleblocks.Crafts;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import fr.kosmosuniverse.kuffleblocks.KuffleMain;

public class Template extends ACrafts {
	ArrayList<Material> compose;
	
	public Template(KuffleMain _km, String age, ArrayList<Material> _compose) {
		compose = _compose;
		name = age + "Template";
		item = new ItemStack(Material.EMERALD);
		
		ItemMeta itM = item.getItemMeta();
		itM.setDisplayName(ChatColor.DARK_RED + name);
		
		ArrayList<String> lore = new ArrayList<String>();
		
		lore.add("Single Use " + age + " Template.");
		lore.add("Right click to validate your item.");
		
		itM.setLore(lore);
		
		item.setItemMeta(itM);
		
		recipe = new ShapelessRecipe(new NamespacedKey(_km, name), item);
		
		for (int cnt = 0; cnt < _km.config.getSBTTAmount(); cnt++) {
			((ShapelessRecipe) recipe).addIngredient(compose.get(cnt));
		}
	}

	@Override
	public Inventory getInventoryRecipe() {
		Inventory inv = Bukkit.createInventory(null,  27, "§8" + name);
		ItemStack grayPane = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
		ItemStack limePane = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
		ItemStack redPane = new ItemStack(Material.RED_STAINED_GLASS_PANE);
		ItemMeta itM = grayPane.getItemMeta();
		
		itM.setDisplayName(" ");
		grayPane.setItemMeta(itM);
		itM = limePane.getItemMeta();
		itM.setDisplayName(" ");
		limePane.setItemMeta(itM);
		itM = redPane.getItemMeta();
		itM.setDisplayName("<- Back");
		redPane.setItemMeta(itM);
		
		int cnt = 0;
		
		for (int i = 0; i < 27; i++) {
			if (i == 0) {
				inv.setItem(i, new ItemStack(redPane));
			} else if (i == 3 || i == 4 || i == 5 ||
					i == 12 || i == 13 || i == 14 ||
					i == 21 || i == 22 || i == 23) {
				if (cnt < compose.size()) {
					inv.setItem(i, new ItemStack(compose.get(cnt)));
					cnt++;
				} else {
					inv.setItem(i, new ItemStack(grayPane));
				}
			} else if (i == 16) {
				inv.setItem(i, item);
			} else {
				inv.setItem(i, new ItemStack(limePane));
			}
		}
		
		return (inv);
	}

}
