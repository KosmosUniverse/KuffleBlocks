package fr.kosmosuniverse.kuffle.MultiBlock;

import org.bukkit.Material;

public class Pattern {
	private Material m;
	private int x;
	private int y;
	private int z;
	
	public Pattern(Material _m, int _x, int _y, int _z) {
		m = _m;
		x = _x;
		y = _y;
		z = _z;
	}
	
	public Material getMaterial() {
		return this.m;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public int getZ() {
		return this.z;
	}
}
