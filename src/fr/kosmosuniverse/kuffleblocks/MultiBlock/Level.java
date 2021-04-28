package fr.kosmosuniverse.kuffleblocks.MultiBlock;

import java.util.ArrayList;

import org.bukkit.Location;

public class Level {
	private double levelNb;
	private int length;
	private ArrayList<ArrayList<Pattern>> levelNS = new ArrayList<ArrayList<Pattern>>();
	private ArrayList<ArrayList<Pattern>> levelEW = new ArrayList<ArrayList<Pattern>>();
	
	public Level(double _lNb, int _length, Pattern ... patterns) {
		levelNb = _lNb;
		length = _length;
		ArrayList<Pattern> tmp = new ArrayList<Pattern>();
		int i = 0;
		
		for (Pattern p : patterns) {
			if (i == length) {
				i = 0;
				levelNS.add(new ArrayList<Pattern>(tmp));
				tmp.clear();
			}
			
			tmp.add(p);
			i++;
		}
		
		levelNS.add(new ArrayList<Pattern>(tmp));
		tmp.clear();
		
		turnLevel();
	}
	
	public void turnLevel() {
		ArrayList<Pattern> tmp = new ArrayList<Pattern>();
		
		for (ArrayList<Pattern> row: levelNS) {
			for (Pattern p : row) {
				tmp.add(new Pattern(p.getMaterial(), p.getX(), p.getY(), p.getZ()));
			}
			
			levelEW.add(new ArrayList<Pattern>(tmp));
			tmp.clear();
		}
		
		int i = 0;
		int j = 1;
		
		for (ArrayList<Pattern> row: levelEW) {
			for (Pattern p : row) {
				p.setMaterial(levelNS.get(i).get(length - j).getMaterial());
				i++;
			}
			
			i = 0;
			j++;
		}
	}
	
	public boolean checkRowsNS(Location loc, double direction) {
		Location tmp;
		
		for (ArrayList<Pattern> row : levelNS) {
			for (Pattern p : row) {
				tmp = loc.clone();
				tmp.add(p.getX() * direction, p.getY(), p.getZ() * direction);
				if (tmp.getBlock().getType() != p.getMaterial())
					return false;
			}
		}
		
		return true;
	}
	
	public boolean checkRowsEW(Location loc, double direction) {
		Location tmp;
		
		for (ArrayList<Pattern> row : levelEW) {
			for (Pattern p : row) {
				tmp = loc.clone();
				tmp.add(p.getX() * direction, p.getY(), p.getZ() * direction);
				if (tmp.getBlock().getType() != p.getMaterial())
					return false;
			}
		}
		
		return true;
	}
	
	public double getLevelNb() {
		return (levelNb);
	}
	
	public void spawnLevel(Location loc) {
		Location tmp;
		
		for (ArrayList<Pattern> row : levelEW) {
			for (Pattern p : row) {
				tmp = loc.clone();
				tmp.add(p.getX(), p.getY(), p.getZ());
				tmp.getBlock().setType(p.getMaterial());
			}
		}
	}
}
