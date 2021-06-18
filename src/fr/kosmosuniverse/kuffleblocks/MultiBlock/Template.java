package fr.kosmosuniverse.kuffleblocks.MultiBlock;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.kosmosuniverse.kuffleblocks.KuffleMain;
import fr.kosmosuniverse.kuffleblocks.Core.AgeManager;
import fr.kosmosuniverse.kuffleblocks.Core.Game;

public class Template extends AMultiblock {
	ArrayList<Material> compose;
	
	public Template(String _name, ArrayList<Material> _compose) {
		name = _name;
		compose = _compose;
		
		squareSize = 1;
		
		item = new ItemStack(compose.get(compose.size() - 1));
		ItemMeta itM = item.getItemMeta();
		itM.setDisplayName(name);
		item.setItemMeta(itM);
		
		multiblock = new MultiBlock(compose.get(compose.size() - 1));
		
		for (int i = 0; i < compose.size(); i++) {
			multiblock.addLevel(new Level(i - (compose.size() - 1), squareSize, new Pattern(compose.get(i), 0, i - (compose.size() - 1), 0)));
		}
		
		createInventories();
	}
	
	@Override
	public void onActivate(KuffleMain _km, Player player, ActivationType type) {
		if (type == ActivationType.ACTIVATE) {
			Game tmp = _km.games.get(player.getName());
			String age = AgeManager.getAgeByNumber(_km.ages, tmp.getAge()).name;
			
			for (String playerName : _km.games.keySet()) {
				_km.games.get(playerName).getPlayer().sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + player.getName() + ChatColor.RESET + "" + ChatColor.BLUE + " just used Template !");
			}
			
			tmp.found();
			_km.multiBlock.reloadTemplate(_km, age);
		}
	}

	@Override
	public void createInventories() {
		Inventory inv;
		
		ItemStack grayPane = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
		ItemStack limePane = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
		ItemStack redPane = new ItemStack(Material.RED_STAINED_GLASS_PANE);
		ItemStack redPanePrev = new ItemStack(Material.RED_STAINED_GLASS_PANE);
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
		itM = redPanePrev.getItemMeta();
		itM.setDisplayName("<- Previous");
		redPanePrev.setItemMeta(itM);
		
		for (int cnt = 0; cnt < compose.size(); cnt++) {
			inv = Bukkit.createInventory(null, 27, "§8" + name + " Layer " + (cnt + 1));
			
			for (int i = 0; i < 27; i++) {
				if (i == 0) {
					inv.setItem(i, new ItemStack(cnt == 0 ? redPane : redPanePrev));
				} else if (i == 8) {
					inv.setItem(i, new ItemStack(cnt == (compose.size() - 1) ? limePane : bluePane));
				} else if (i == 13) {
					inv.setItem(i, new ItemStack(compose.get(cnt)));
				} else {
					inv.setItem(i, new ItemStack(limePane));
				}
			}
			
			invs.add(inv);
		}
	}
}
