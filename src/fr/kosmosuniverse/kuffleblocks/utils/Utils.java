package fr.kosmosuniverse.kuffleblocks.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import fr.kosmosuniverse.kuffleblocks.Core.GameTask;
import fr.kosmosuniverse.kuffleblocks.Core.Level;

public class Utils {
	public static String readFileContent(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();
 
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line + System.lineSeparator());
        }
 
        return sb.toString();
	}
	
	public static ItemStack getHead(Player player) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skull = (SkullMeta) item.getItemMeta();
        
        skull.setDisplayName(player.getName());
        skull.setOwningPlayer(player);
        item.setItemMeta(skull);
        
        return item;
    }
	
	public static ChatColor getColor(int age) {
		switch (age) {
		case 0:
			return (ChatColor.RED);
		case 1:
			return (ChatColor.GOLD);
		case 2:
			return (ChatColor.YELLOW);
		case 3:
			return (ChatColor.GREEN);
		case 4:
			return (ChatColor.DARK_GREEN);
		case 5:
			return (ChatColor.DARK_BLUE);
		default:
			return (ChatColor.DARK_PURPLE);
		}
	}
	
	public static String getVersion() {
		String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
		
		version = version.split("v")[1];
		version = version.split("_")[0] + "." + version.split("_")[1];
		
		return version;
	}
	
	public static long minSecondsWithLevel(Level level) {
		Random r = new Random();
		
		if (level == Level.EASY) {
			return 3;
		} else if (level == Level.NORMAL) {
			return 6;
		} else if (level == Level.EXPERT) {
			return (6 + (r.nextInt(9) + 1));
		}
		
		return -1;
	}
	
	public static long maxSecondsWithLevel(Level level) {
		if (level == Level.EASY) {
			return 40;
		} else if (level == Level.NORMAL) {
			return 30;
		} else if (level == Level.EXPERT) {
			return 20;
		}
		
		return -1;
	}
	
	public static ArrayList<Player> getPlayerList(ArrayList<GameTask> games) {
		ArrayList<Player> players = new ArrayList<Player>();
		
		for (GameTask gt : games) {
			players.add(gt.getPlayer());
		}
		
		return players;
	}
	
	public static ArrayList<String> getPlayerNames(ArrayList<GameTask> games) {
		ArrayList<String> players = new ArrayList<String>();
		
		for (GameTask gt : games) {
			players.add(gt.getPlayer().getDisplayName());
		}
		
		return players;
	}
	
	public static ArrayList<Location> getAllPlayerLocation(ArrayList<GameTask> games) {
		ArrayList<Location> locs = new ArrayList<Location>();
		
		for (GameTask gt : games) {
			locs.add(gt.getSpawnLoc());
		}
		
		return locs;
	}
	
	public static ChatColor findChatColor(String color) {
		for (ChatColor item : ChatColor.values()) {
			if (item.name().equals(color)) {
				return item;
			}
		}
		
		return null;
	}
	
	public static Player getPlayerInList(ArrayList<GameTask> games, String name) {
		for (GameTask gt : games) {
			if (gt.getPlayer().getDisplayName().equals(name)) {
				return gt.getPlayer();
			}
		}
		
		return null;
	}
}
 