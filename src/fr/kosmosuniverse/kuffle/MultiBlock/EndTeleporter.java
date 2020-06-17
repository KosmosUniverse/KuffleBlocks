package fr.kosmosuniverse.kuffle.MultiBlock;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.kosmosuniverse.kuffle.KuffleMain;

public class EndTeleporter extends AMultiblock {
	public EndTeleporter() {
		name = "End Teleporter";
		
		squareSize = 3;
		
		item = new ItemStack(Material.OBSIDIAN);
		ItemMeta itM = item.getItemMeta();
		itM.setDisplayName(name);
		item.setItemMeta(itM);
		
		multiblock = new MultiBlock(Material.OBSIDIAN,
				new Pattern(Material.COAL_BLOCK, -1, 0, -1),
				new Pattern(Material.COAL_BLOCK, 0, 0, -1),
				new Pattern(Material.COAL_BLOCK, 1, 0, -1),
				new Pattern(Material.NETHER_BRICK_STAIRS, -1, 0, 0),
				new Pattern(Material.NETHER_BRICK_STAIRS, 1, 0, 0),
				new Pattern(Material.NETHER_BRICK_STAIRS, -1, 0, 1),
				new Pattern(Material.NETHER_BRICK_STAIRS, 0, 0, 1),
				new Pattern(Material.NETHER_BRICK_STAIRS, 1, 0, 1),
				
				new Pattern(Material.STONE_BRICK_WALL, -1, 1, 0),
				new Pattern(Material.STONE_BRICK_WALL, 1, 1, 0),
				new Pattern(Material.RED_NETHER_BRICK_WALL, 0, 1, -1),
				new Pattern(Material.IRON_BLOCK, -1, 1, -1),
				new Pattern(Material.GOLD_BLOCK, 1, 1, -1),
				
				new Pattern(Material.DIAMOND_BLOCK, 0, 2, -1));
		
		createInventories();
		findNormalWorld();
	}

	@Override
	public void onActivate(KuffleMain _km, Player player, ActivationType type) {
		if (type == ActivationType.ASSEMBLE) {
			player.sendMessage("You just constructed " + name);
		}
		else if (type == ActivationType.ACTIVATE) {
			if (world != null) {
				player.sendMessage("You just activated " + name);
				player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 50, false, false, false));
				Location tmp = new Location(Bukkit.getWorld(world.getName() + "_the_end"), player.getLocation().getX() + 1000, 60.0, player.getLocation().getZ() + 1000);
				
				while (tmp.getBlock().getType() != Material.END_STONE) {
					tmp.add(10, 0, 10);
				}
				
				player.teleport(tmp.add(0, 10, 0));
				player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);	
			}
		}
	}

	@Override
	public void createInventories() {
		Inventory inv = Bukkit.createInventory(null, 27, "§8" + name + " Layer 1");
		
		ItemStack grayPane = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
		ItemStack limePane = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
		ItemStack redPane = new ItemStack(Material.RED_STAINED_GLASS_PANE);
		ItemStack bluePane = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
		ItemMeta itM = grayPane.getItemMeta();
		
		itM.setDisplayName(" ");
		grayPane.setItemMeta(itM);
		itM = limePane.getItemMeta();
		itM.setDisplayName(" ");
		limePane.setItemMeta(itM);
		itM = redPane.getItemMeta();
		itM.setDisplayName("<- Back");
		redPane.setItemMeta(itM);
		itM = bluePane.getItemMeta();
		itM.setDisplayName("Next ->");
		bluePane.setItemMeta(itM);
		
		for (int i = 0; i < 27; i++) {
			if (i == 0) {
				inv.setItem(i, new ItemStack(redPane));
			} else if (i == 8) {
				inv.setItem(i, new ItemStack(bluePane));
			} else if (i == 3 || i == 4 || i == 5) {
				inv.setItem(i, new ItemStack(Material.COAL_BLOCK));
			} else if (i == 12 || i == 14 || i == 21 || i == 22 || i == 23) {
				inv.setItem(i, new ItemStack(Material.NETHER_BRICK_STAIRS));
			} else if (i == 13) {
				inv.setItem(i, new ItemStack(Material.OBSIDIAN));
			} else {
				inv.setItem(i, new ItemStack(limePane));
			}
		}
		
		invs.add(inv);
		
		itM = redPane.getItemMeta();
		itM.setDisplayName("<- Previous");
		redPane.setItemMeta(itM);
		
		inv = Bukkit.createInventory(null, 27, "§8" + name + " Layer 2");
		
		for (int i = 0; i < 27; i++) {
			if (i == 0) {
				inv.setItem(i, new ItemStack(redPane));
			} else if (i == 8) {
				inv.setItem(i, new ItemStack(bluePane));
			} else if (i == 3) {
				inv.setItem(i, new ItemStack(Material.IRON_BLOCK));
			} else if (i == 4) {
				inv.setItem(i, new ItemStack(Material.NETHER_BRICK_WALL));
			} else if (i == 5) {
				inv.setItem(i, new ItemStack(Material.GOLD_BLOCK));
			} else if (i == 12 || i == 14) {
				inv.setItem(i, new ItemStack(Material.STONE_BRICK_WALL));
			} else if (i == 13 || i == 21 || i == 22 || i == 23) {
				inv.setItem(i, new ItemStack(grayPane));
			} else {
				inv.setItem(i, new ItemStack(limePane));
			}
		}
		
		invs.add(inv);
		
		inv = Bukkit.createInventory(null, 27, "§8" + name + " Layer 3");
		
		for (int i = 0; i < 27; i++) {
			if (i == 0) {
				inv.setItem(i, new ItemStack(redPane));
			} else if (i == 4) {
				inv.setItem(i, new ItemStack(Material.DIAMOND_BLOCK));
			} else if (i == 3 || i == 5 || i == 12 || i == 13 || i == 14|| i == 21 || i == 22 || i == 23) {
				inv.setItem(i, new ItemStack(grayPane));
			} else {
				inv.setItem(i, new ItemStack(limePane));
			}
		}
		
		invs.add(inv);
	}
}
