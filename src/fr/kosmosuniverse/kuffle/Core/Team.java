package fr.kosmosuniverse.kuffle.Core;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Team {
	public ArrayList<Player> players = new ArrayList<Player>();
	private Random random = new Random();
	public String name;
	public ChatColor color;
	
	public Team(String _name) {
		name = _name;
		color = ChatColor.values()[random.nextInt(ChatColor.values().length)];
	}
	
	public Team(String _name, ChatColor _color) {
		name = _name;
		color = _color;
	}
	
	public boolean hasPlayer(String player)	{
		for (Player item : players) {
			if (item.getDisplayName().equals(player)) {
				return true;
			}
		}
		
		return false;
	}
	
	public ArrayList<String> getPlayersName() {
		ArrayList<String> names = new ArrayList<String>();
		
		for (Player item : players) {
			names.add(item.getDisplayName());
		}
		
		return names;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(color + name);
		sb.append("\n  Color: " + color.name());
		sb.append("\n  Players:");
		if (players.size() == 0) {
			sb.append(" NONE.");
		} else {
			for (Player item : players) {
				sb.append("\n    - " + item.getDisplayName());
			}
		}
		
		sb.append("§r");
		
		return sb.toString();
	}
}
