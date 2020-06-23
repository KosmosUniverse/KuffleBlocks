package fr.kosmosuniverse.kuffle.MultiBlock;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class MultiBlock {
	private Material Core;
	private Pattern[] patternsNS;
	
	public MultiBlock(Material _m, Pattern... _patterns) {
		Core = _m;
		patternsNS = _patterns;
	}
		
	public Material getCore() {
		return Core;
	}
	
	public Pattern[] getPatterns() {
		return patternsNS;
	}
	
	public List<Material> getBlockTypes() {
		ArrayList<Material> types = new ArrayList<Material>();
		
		types.add(Core);
		
		for (Pattern p : patternsNS) {
			if (!types.contains(p.getMaterial()))
				types.add(p.getMaterial());
		}
		return types;
	}

	public boolean checkMultiBlock(Location CoreLoc, Player player) {
		Location newLoc = new Location(CoreLoc.getWorld(), CoreLoc.getBlockX(), CoreLoc.getBlockY(), CoreLoc.getBlockZ());
		
		if (checkNorthSouth(newLoc, 1) || checkNorthSouth(newLoc, -1)/* || checkEastWest(newLoc, 1) || checkEastWest(newLoc, -1)*/) {
			return true;
		}

		return false;
	}
	
	private boolean checkNorthSouth(Location loc, double direction) {
		Location tmp;
		
		for (Pattern p : patternsNS) {
			tmp = loc.clone();
			tmp.add(p.getX() * direction, p.getY(), p.getZ() * direction);
			if (tmp.getBlock().getType() != p.getMaterial())
				return false;
		}
		
		return true;
	}
	
/*	private boolean checkEastWest(Location loc, double direction) {
		Location tmp;
		
		System.out.println("Check East");
		
		ArrayList<Pattern> tmpPatterns = new ArrayList<Pattern>();

		for (Pattern p : patterns) {
			if (p.getX() == 0 || p.getZ() == 0) {
				if (p.getX() == 0) {
					tmpPatterns.add(new Pattern(p.getMaterial(), p.getZ(), p.getY(), p.getX()));
				} else {
					tmpPatterns.add(new Pattern(p.getMaterial(), p.getZ(), p.getY(), p.getX() * -1));
				}
			} else {
				if ((p.getX() > 0 && p.getZ() < 0) || (p.getX() < 0 && p.getZ() < 0)) {
					tmpPatterns.add(new Pattern(p.getMaterial(), p.getX() * -1, p.getY(), p.getZ()));
				} else if ((p.getX() > 0 && p.getZ() > 0) || (p.getX() < 0 && p.getZ() > 0)) {
					tmpPatterns.add(new Pattern(p.getMaterial(), p.getX(), p.getY(), p.getZ() * -1));
				} else {
					tmpPatterns.add(new Pattern(p.getMaterial(), p.getX(), p.getY(), p.getZ()));
				}
			}
		}
		
		System.out.println("Check EastWest: " + direction);
		
		for (Pattern p : tmpPatterns) {
			tmp = loc.clone();
			tmp.add(p.getX() * direction, p.getY(), p.getZ() * direction);
			System.out.println("Location: " + tmp.toString() + ", Material: " + tmp.getBlock().getType());
			if (tmp.getBlock().getType() != p.getMaterial())
				return false;
		}
		
		System.out.println("Check East: END");
		
		tmpPatterns.clear();
		
		return true;
	}*/
	
	/*private boolean checkWest(Location loc) {
		Location tmp;

		System.out.println("Check West");

		ArrayList<Pattern> tmpPatterns = new ArrayList<Pattern>();

		for (Pattern p : patterns) {
			if (p.getX() == 0 || p.getZ() == 0) {
				if (p.getX() == 0) {
					tmpPatterns.add(new Pattern(p.getMaterial(), p.getZ() * -1, p.getY(), p.getX()));
				} else {
					tmpPatterns.add(new Pattern(p.getMaterial(), p.getZ(), p.getY(), p.getX()));
				}
			} else {
				if ((p.getX() > 0 && p.getZ() < 0) || (p.getX() < 0 && p.getZ() > 0)) {
					tmpPatterns.add(new Pattern(p.getMaterial(), p.getX(), p.getY(), p.getZ() * -1));
				} else if ((p.getX() > 0 && p.getZ() > 0) || (p.getX() < 0 && p.getZ() < 0)) {
					tmpPatterns.add(new Pattern(p.getMaterial(), p.getX() * -1, p.getY(), p.getZ()));
				} else {
					tmpPatterns.add(new Pattern(p.getMaterial(), p.getX(), p.getY(), p.getZ()));
				}
			}
		}
		
		System.out.println("Check West: START");
		
		for (Pattern p : tmpPatterns) {
			tmp = loc.clone();
			tmp.add(p.getX(), p.getY(), p.getZ());
			System.out.println("Location: " + tmp.toString() + ", Material: " + tmp.getBlock().getType());
			if (tmp.getBlock().getType() != p.getMaterial())
				return false;
		}
		
		System.out.println("Check West: END");
		
		tmpPatterns.clear();
		
		return true;
	}*/

	public void spawnMultiBlock(Player player) {
		Location newLoc = new Location(player.getLocation().getWorld(), player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ());
		Location tmp;

		newLoc.getBlock().setType(this.Core);

		for (Pattern p : patternsNS) {
			tmp = newLoc.clone();
			tmp.add(p.getX(), p.getY(), p.getZ());
			tmp.getBlock().setType(p.getMaterial());
		}
	}
}
