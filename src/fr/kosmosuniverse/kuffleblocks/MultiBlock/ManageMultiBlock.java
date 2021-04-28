package fr.kosmosuniverse.kuffleblocks.MultiBlock;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

public class ManageMultiBlock {
	public HashMap<String, AMultiblock> multiBlocks = new HashMap<String, AMultiblock>();
	
	public ManageMultiBlock() {
		multiBlocks.put("EndTeleporter", new EndTeleporter());
		multiBlocks.put("OverWorldTeleporter", new OverWorldTeleporter());
	}
	
	public HashMap<String, AMultiblock> getMultiBlocks() {
		return multiBlocks;
	}
	
	public Inventory getAllMultiBlocksInventory() {
		Inventory inv = Bukkit.createInventory(null, getNbRows(), "§8AllMultiBlocks");
		int i = 0;
		
		for (String key : multiBlocks.keySet()) {
			inv.setItem(i, multiBlocks.get(key).getItem());
			i++;
		}
		
		return (inv);
	}
	
	private int getNbRows() {
		int rows = multiBlocks.size() / 9;
		
		if (multiBlocks.size() % 9 == 0) {
			return rows * 9;
		} else {
			return (rows + 1) * 9;
		}
	}
	
	public AMultiblock findMultiBlockByName(String name) {
		for (String key : multiBlocks.keySet()) {
			if (multiBlocks.get(key).getName().equals(name)) {
				return (multiBlocks.get(key));
			}
		}
		
		return null;
	}
	
	public AMultiblock findMultiBlockByCore(Material core) {
		for (String key : multiBlocks.keySet()) {
			if (multiBlocks.get(key).getMultiblock().getCore() == core) {
				return (multiBlocks.get(key));
			}
		}
		
		return null;
	}
	
	public AMultiblock findMultiBlockByInventoryName(String invName) {
		for (String key : multiBlocks.keySet()) {
			if (invName.contains(multiBlocks.get(key).getName())) {
				return (multiBlocks.get(key));
			}
		}
		
		return null;
	}
	
	public AMultiblock findMultiBlockByItem(Material item) {
		for (String key : multiBlocks.keySet()) {
			if (multiBlocks.get(key).getItem().getType() == item) {
				return (multiBlocks.get(key));
			}
		}
		
		return null;
	}
}
