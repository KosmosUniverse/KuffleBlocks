package fr.kosmosuniverse.kuffle.MultiBlock;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.kosmosuniverse.kuffle.KuffleMain;

public abstract class AMultiblock {
	protected String name;
	protected int squareSize;
	protected MultiBlock multiblock;
	protected ArrayList<Inventory> invs = new ArrayList<Inventory>();
	protected ItemStack item;
	protected World world = null;
	
	public abstract void onActivate(KuffleMain _km, Player player, ActivationType type);
	public abstract void createInventories();
	
	public Inventory getInventory(Inventory current, ItemStack item, Inventory master, boolean first) {
		int idx = -1;

		if (first) {
			return (invs.get(0));
		}
		
		for (Inventory inv : invs) {
			if (inv.equals(current)) {
				idx = invs.indexOf(inv);
				break;
			}
		}
		
		if (idx == -1) {
			return null;
		}
		
		if (item.getType() == Material.BLUE_STAINED_GLASS_PANE) {
			if (idx == invs.size() - 1) {
				return null;
			}
			idx += 1;
			return (invs.get(idx));
		} else if (item.getType() == Material.RED_STAINED_GLASS_PANE) {
			if (item.getItemMeta().getDisplayName().equals("<- Back")) {
				return (master);
			} else if (item.getItemMeta().getDisplayName().equals("<- Previous")) {
				if (idx > 0) {
					idx -= 1;
					return (invs.get(idx));
				}
			}
		}
		
		return null;
	}
	
	public void findNormalWorld() {
		for (World w : Bukkit.getWorlds()) {
			if (!w.getName().contains("nether") && !w.getName().contains("the_end")) {
				world = w;
			}
		}
	}
	
	public String getName() {
		return name;
	}
	
	public ItemStack getItem() {
		return item;
	}
	
	public MultiBlock getMultiblock() {
		return multiblock;
	}
}
