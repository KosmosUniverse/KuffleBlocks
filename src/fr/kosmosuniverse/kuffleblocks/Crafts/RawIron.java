package fr.kosmosuniverse.kuffleblocks.Crafts;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.RecipeChoice.MaterialChoice;
import org.bukkit.inventory.meta.ItemMeta;

import fr.kosmosuniverse.kuffleblocks.KuffleMain;
import fr.kosmosuniverse.kuffleblocks.utils.Utils;

public class RawIron extends ACrafts {
	MaterialChoice mc;
	
	public RawIron(KuffleMain _km) {
		name = "RawIron";
		
		recipe = new ShapelessRecipe(new NamespacedKey(_km, name), new ItemStack(Material.RAW_IRON, 2));
		
		ArrayList<Material> ores = new ArrayList<Material>();
		
		ores.add(Material.IRON_ORE);
		
		if (Utils.findVersionNumber(_km, Utils.getVersion()) >= Utils.findVersionNumber(_km, "1.17")) {
			ores.add(Material.DEEPSLATE_IRON_ORE);
		}
		
		mc = new MaterialChoice(ores);
		
		((ShapelessRecipe) recipe).addIngredient(mc);
		
		item = new ItemStack(Material.RAW_IRON);
	}
	
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
		
		ItemStack customOre = new ItemStack(Material.IRON_ORE);
		itM = customOre.getItemMeta();
		itM.setDisplayName(ChatColor.BLUE + "Any" + ChatColor.GREEN + " Iron " + ChatColor.RED + "Ore");
		customOre.setItemMeta(itM);
		
		for (int i = 0; i < 27; i++) {
			if (i == 0) {
				inv.setItem(i, new ItemStack(redPane));
			} else if (i == 3) {
				inv.setItem(i, customOre);
			} else if (i == 16) {
				inv.setItem(i, new ItemStack(Material.RAW_IRON, 2));
			} else if (i == 4 || i == 5 || i == 12 || i == 13 || i == 14 || i == 21 || i == 22 || i == 23) {
				inv.setItem(i, new ItemStack(grayPane));
			} else {
				inv.setItem(i, new ItemStack(limePane));
			}
		}
		
		return (inv);
	}
}
