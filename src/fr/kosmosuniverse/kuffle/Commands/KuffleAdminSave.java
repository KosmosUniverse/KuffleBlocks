package fr.kosmosuniverse.kuffle.Commands;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import fr.kosmosuniverse.kuffle.KuffleMain;
import fr.kosmosuniverse.kuffle.Core.GameTask;

public class KuffleAdminSave implements CommandExecutor {
	private KuffleMain km;
	private File dataFolder;
	
	public KuffleAdminSave(KuffleMain _km, File _dataFolder) {
		km = _km;
		dataFolder = _dataFolder;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if (!(sender instanceof Player))
			return false;
		
		Player player = (Player) sender;
		
		if (!player.hasPermission("kadminsave")) {
			player.sendMessage("You are not allowed to do this command.");
			return false;
		}
		
		if (km.games.size() == 0) {
			player.sendMessage("No game launched, you can launch a game with kstart command.");
			return false;
		} else if (!km.games.get(0).getEnable()) {
			player.sendMessage("No game launched, you can launch a game with kstart command.");
			return false;
		}
		
		FileWriter writer = null;
		
		km.paused = true;
		
		for (GameTask gt : km.games) {
			gt.disable();
			for (PotionEffect pe : gt.getPlayer().getActivePotionEffects()) {
				gt.getPlayer().removePotionEffect(pe.getType());
			}
		}
		
		for (GameTask gt : km.games) {
			try {
				if (dataFolder.getPath().contains("\\")) {
					writer = new FileWriter(dataFolder.getPath() + "\\" + gt.getPlayer().getDisplayName());
				} else {
					writer = new FileWriter(dataFolder.getPath() + "/" + gt.getPlayer().getDisplayName());
				}
				
				writer.write(gt.saveGame());
				writer.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		km.scores.clear();
		
		for (GameTask gt : km.games) {
			gt.exit();
			gt.kill();
		}
		
		km.games.clear();
		
		km.paused = false;
		
		return true;
	}
}
