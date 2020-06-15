package fr.kosmosuniverse.kuffle.MultiBlock;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import fr.kosmosuniverse.kuffle.KuffleMain;

public abstract class AMultiblock {
	protected String name;
	protected int squareSize;
	protected MultiBlock multiblock;
	protected ArrayList<Inventory> invs;
	
	public abstract void onActivate(KuffleMain _km, Player player, ActivationType type);
	public abstract void createInventories();
	
	public Inventory getInventory(Inventory before, Material material, Inventory upInv) {
		return null;
	}
	
	public String getName() {
		return name;
	}
	
	public MultiBlock getMultiblock() {
		return multiblock;
	}
}
