package fr.kosmosuniverse.kuffleblocks.MultiBlock;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.kosmosuniverse.kuffleblocks.KuffleMain;
import fr.kosmosuniverse.kuffleblocks.Core.Age;
import fr.kosmosuniverse.kuffleblocks.Core.AgeManager;
import fr.kosmosuniverse.kuffleblocks.Core.BlockManager;
import fr.kosmosuniverse.kuffleblocks.utils.Utils;

public class MultiblockManager {
	public HashMap<String, AMultiblock> multiBlocks = new HashMap<String, AMultiblock>();
	public HashMap<Material, String> cores = new HashMap<Material, String>();
	
	public MultiblockManager(KuffleMain km) {
		multiBlocks.put("EndTeleporter", new EndTeleporter());
		multiBlocks.put("OverWorldTeleporter", new OverWorldTeleporter());
		
		reloadCores(km);
	}
	
	public HashMap<String, AMultiblock> getMultiBlocks() {
		return multiBlocks;
	}
	
	public void reloadCores(KuffleMain km) {
		cores.clear();
		
		if (km.ksmbt != null) {
			km.ksmbt.list.clear();
		}
		
		for (String name : multiBlocks.keySet()) {
			cores.put(multiBlocks.get(name).getItem().getType(), name);
			
			if (km.ksmbt != null) {
				km.ksmbt.list.add(name);
			}
		}
	}
	
	public void clear() {
		for (String age : multiBlocks.keySet()) {
			multiBlocks.get(age).multiblock.clear();
			multiBlocks.get(age).invs.clear();
		}
		
		multiBlocks.clear();
		cores.clear();
	}
	
	public void createTemplates(KuffleMain km) {
		ArrayList<String> done = new ArrayList<String>();
		ArrayList<Material> tmp = new ArrayList<Material>();
		
		for (int ageCnt = 0; ageCnt < km.config.getMaxAges(); ageCnt++) {
			Age age = AgeManager.getAgeByNumber(km.ages, ageCnt);
			
			for (int i = 0; i < km.config.getSBTTAmount(); i++) {
				done.add(BlockManager.newBlock(done, km.allSbtts.get(age.name)));
			}
			
			for (String block : done) {
				tmp.add(Material.matchMaterial(block));
			}
			
			multiBlocks.put(age.name, new Template(age.name, tmp));

			tmp.clear();
			done.clear();
		}
		
		reloadCores(km);
	}
	
	public void reloadTemplate(KuffleMain km, String age) {
		ArrayList<Material> compose = new ArrayList<Material>();
		ArrayList<String> done = new ArrayList<String>();
		
		multiBlocks.get(age).multiblock.clear();
		multiBlocks.get(age).invs.clear();
		
		for (int i = 0; i < km.config.getSBTTAmount(); i++) {
			compose.add(Material.matchMaterial(BlockManager.newBlock(done, km.allSbtts.get(age))));
		}
		
		multiBlocks.put(age, new Template(age, compose));

		done.clear();
		compose.clear();
		
		reloadCores(km);
	}
	
	public void removeTemplates(KuffleMain km) {
		if (!km.config.getSBTT()) {
			return ;
		}
		for (int ageCnt = 0; ageCnt < km.config.getMaxAges(); ageCnt++) {
			Age age = AgeManager.getAgeByNumber(km.ages, ageCnt);
			
			multiBlocks.get(age.name).multiblock.clear();
			multiBlocks.get(age.name).invs.clear();
			multiBlocks.remove(age.name);
		}
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
		if (!multiBlocks.containsKey(name)) {
			return null;
		}
		
		return (multiBlocks.get(name));
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
	
	public AMultiblock findMultiBlockByItemType(Material item) {
		for (String key : multiBlocks.keySet()) {
			if (multiBlocks.get(key).getItem().getType() == item) {
				return (multiBlocks.get(key));
			}
		}
		
		return null;
	}
	
	public AMultiblock findMultiBlockByItem(ItemStack item) {
		for (String key : multiBlocks.keySet()) {
			if (Utils.compareItems(multiBlocks.get(key).getItem(), item, true, true, false)) {
				return (multiBlocks.get(key));
			}
		}
		
		return null;
	}
}
