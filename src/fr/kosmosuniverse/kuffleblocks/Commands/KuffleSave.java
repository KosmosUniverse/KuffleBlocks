package fr.kosmosuniverse.kuffleblocks.Commands;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

import fr.kosmosuniverse.kuffleblocks.KuffleMain;
import fr.kosmosuniverse.kuffleblocks.utils.Utils;

public class KuffleSave implements CommandExecutor {
	private KuffleMain km;
	private File dataFolder;
	
	public KuffleSave(KuffleMain _km, File _dataFolder) {
		km = _km;
		dataFolder = _dataFolder;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if (!(sender instanceof Player))
			return false;
		
		Player player = (Player) sender;
		
		km.logs.logMsg(player, Utils.getLangString(km, player.getName(), "CMD_PERF").replace("<#>", "<kb-save>"));
		
		if (!player.hasPermission("kb-save")) {
			km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "NOT_ALLOWED"));
			
			return false;
		}
		
		if (!km.gameStarted) {
			km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "GAME_NOT_LAUNCHED"));
			return false;
		}
		
		FileWriter writer = null;
		
		km.paused = true;
		
		for (String playerName : km.games.keySet()) {
			try {
				if (dataFolder.getPath().contains("\\")) {
					writer = new FileWriter(dataFolder.getPath() + "\\" + playerName + ".kb");
				} else {
					writer = new FileWriter(dataFolder.getPath() + "/" + playerName + ".kb");
				}
				
				writer.write(km.games.get(playerName).save());
				writer.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			km.games.get(playerName).stop();
		}
		
		if (km.config.getTeam()) {
			try {
				if (dataFolder.getPath().contains("\\")) {
					writer = new FileWriter(dataFolder.getPath() + "\\" + "Teams.kb");
				} else {
					writer = new FileWriter(dataFolder.getPath() + "/" + "Teams.kb");
				}
				
				writer.write(km.teams.saveTeams());
				writer.close();
							
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			if (dataFolder.getPath().contains("\\")) {
				writer = new FileWriter(dataFolder.getPath() + "\\" + "Game.kb");
			} else {
				writer = new FileWriter(dataFolder.getPath() + "/" + "Game.kb");
			}
			
			JSONObject global = new JSONObject();

			global.put("config", km.config.saveConfig());
			global.put("ranks", saveRanks());
			
			writer.write(global.toJSONString());
			writer.close();
			
			global.clear();
						
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Utils.removeTemplates(km);
		km.scores.clear();
		km.games.clear();
		km.loop.kill();
		
		km.paused = false;
		km.gameStarted = false;
		km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "GAME_SAVED"));
		
		return true;
	}
	
	@SuppressWarnings("unchecked")
	private JSONObject saveRanks() {
		JSONObject rankObj = new JSONObject();
		
		for (String playerName : km.playerRank.keySet()) {
			rankObj.put(playerName, km.playerRank.get(playerName));
		}
		
		return rankObj;
	}
}
