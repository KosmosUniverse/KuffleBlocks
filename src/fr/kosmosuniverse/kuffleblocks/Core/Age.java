package fr.kosmosuniverse.kuffleblocks.Core;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public class Age {
	public String name;
	public int number;
	public ChatColor color;
	public Material box;
	
	public Age(String _name, int _number, ChatColor _color, Material _box) {
		name = _name;
		number = _number;
		color = _color;
		box = _box;
	}
	
	public Age(String _name, int _number, String _color, String _box) {
		name = _name;
		number = _number;
		color = ChatColor.valueOf(_color);
		box = Material.matchMaterial(_box);
	}
}
