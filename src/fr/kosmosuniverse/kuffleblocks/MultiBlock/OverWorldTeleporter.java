package fr.kosmosuniverse.kuffleblocks.MultiBlock;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.kosmosuniverse.kuffleblocks.KuffleMain;

public class OverWorldTeleporter extends AMultiblock {
	public OverWorldTeleporter() {
		name = "OverWorldTeleporter";
		
		squareSize = 3;
		
		item = new ItemStack(Material.END_PORTAL_FRAME);
		ItemMeta itM = item.getItemMeta();
		itM.setDisplayName(name);
		item.setItemMeta(itM);
		
		multiblock = new MultiBlock(Material.END_PORTAL_FRAME);
		
		multiblock.addLevel(new Level(0, 3,
				new Pattern(Material.END_STONE_BRICKS, -1, 0, -1),
				new Pattern(Material.QUARTZ_PILLAR, 0, 0, -1),
				new Pattern(Material.END_STONE_BRICKS, 1, 0, -1),
				new Pattern(Material.PURPUR_STAIRS, -1, 0, 0),
				new Pattern(Material.END_PORTAL_FRAME, 0, 0, 0),
				new Pattern(Material.PURPUR_STAIRS, 1, 0, 0),
				new Pattern(Material.PURPUR_STAIRS, -1, 0, 1),
				new Pattern(Material.PURPUR_STAIRS, 0, 0, 1),
				new Pattern(Material.PURPUR_STAIRS, 1, 0, 1)));
		
		multiblock.addLevel(new Level(1, 3,
				new Pattern(Material.PURPUR_PILLAR, -1, 1, -1),
				new Pattern(Material.CHISELED_QUARTZ_BLOCK, 0, 1, -1),
				new Pattern(Material.PURPUR_PILLAR, 1, 1, -1),
				new Pattern(Material.END_ROD, -1, 1, 0),
				new Pattern(Material.AIR, 0, 1, 0),
				new Pattern(Material.END_ROD, 1, 1, 0),
				new Pattern(Material.AIR, -1, 1, 1),
				new Pattern(Material.AIR, 0, 1, 1),
				new Pattern(Material.AIR, 1, 1, 1)));
		
		multiblock.addLevel(new Level(2, 3,
				new Pattern(Material.AIR, -1, 2, -1),
				new Pattern(Material.PURPUR_PILLAR, 0, 2, -1),
				new Pattern(Material.AIR, 1, 2, -1),
				new Pattern(Material.AIR, -1, 2, 0),
				new Pattern(Material.AIR, 0, 2, 0),
				new Pattern(Material.AIR, 1, 2, 0),
				new Pattern(Material.AIR, -1, 2, 1),
				new Pattern(Material.AIR, 0, 2, 1),
				new Pattern(Material.AIR, 1, 2, 1)));
		
		createInventories();
		findNormalWorld();
	}

	@Override
	public void onActivate(KuffleMain _km, Player player, ActivationType type) {
		if (type == ActivationType.ASSEMBLE) {
			player.sendMessage("You just constructed " + name);
		} else if (type == ActivationType.ACTIVATE) {
			if (world != null) {
				player.sendMessage("You just activated " + name);
				player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 50, false, false, false));
				
				Location tmp = new Location(Bukkit.getWorld(world.getName()), player.getLocation().getX() - 1000, 80.0, player.getLocation().getZ() - 1000);
				
				tmp.setY(tmp.getWorld().getHighestBlockAt(tmp).getY() + 2.0);
				
				player.teleport(tmp);
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
			} else if (i == 3 || i == 5) {
				inv.setItem(i, new ItemStack(Material.END_STONE_BRICKS));
			} else if (i == 4) {
				inv.setItem(i, new ItemStack(Material.QUARTZ_PILLAR));
			} else if (i == 12 || i == 14 || i == 21 || i == 22 || i == 23) {
				inv.setItem(i, new ItemStack(Material.PURPUR_STAIRS));
			} else if (i == 13) {
				inv.setItem(i, new ItemStack(Material.END_PORTAL_FRAME));
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
			} else if (i == 3 || i == 5) {
				inv.setItem(i, new ItemStack(Material.PURPUR_PILLAR));
			} else if (i == 4) {
				inv.setItem(i, new ItemStack(Material.CHISELED_QUARTZ_BLOCK));
			} else if (i == 12 || i == 14) {
				inv.setItem(i, new ItemStack(Material.END_ROD));
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
				inv.setItem(i, new ItemStack(Material.PURPUR_PILLAR));
			} else if (i == 3 || i == 5 || i == 12 || i == 13 || i == 14|| i == 21 || i == 22 || i == 23) {
				inv.setItem(i, new ItemStack(grayPane));
			} else {
				inv.setItem(i, new ItemStack(limePane));
			}
		}
		
		invs.add(inv);
	}
}
