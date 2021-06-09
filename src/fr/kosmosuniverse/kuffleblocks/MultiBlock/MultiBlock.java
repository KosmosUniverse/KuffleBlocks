package fr.kosmosuniverse.kuffleblocks.MultiBlock;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class MultiBlock {
	private Material Core;
	private ArrayList<Level> pattern;
	
	public MultiBlock(Material _m) {
		Core = _m;
		pattern = new ArrayList<Level>();
	}
		
	public Material getCore() {
		return Core;
	}
	
	public void addLevel(Level l) {
		pattern.add(l);
	}

	public boolean checkMultiBlock(Location CoreLoc, Player player) {
		Location newLoc = new Location(CoreLoc.getWorld(), CoreLoc.getBlockX(), CoreLoc.getBlockY(), CoreLoc.getBlockZ());
		
		if (checkNorthSouth(newLoc, 1) || checkNorthSouth(newLoc, -1) || checkEastWest(newLoc, 1) || checkEastWest(newLoc, -1)) {
			return true;
		}

		return false;
	}
	
	public boolean checkNorthSouth(Location loc, double direction) {
		for (Level l : pattern) {
			if (!l.checkRowsNS(loc, direction))
				return false;
		}
		
		return true;
	}
	
	public boolean checkEastWest(Location loc, double direction) {
		for (Level l : pattern) {
			if (!l.checkRowsEW(loc, direction))
				return false;
		}
		
		return true;
	}
	
	public void spawnMultiBlock(Player player) {
		Location newLoc = new Location(player.getLocation().getWorld(), player.getLocation().getBlockX(), player.getLocation().getBlockY() + 20, player.getLocation().getBlockZ());

		newLoc.getBlock().setType(this.Core);

		for (Level l : pattern) {
			l.spawnLevel(newLoc);
		}
	}
}
