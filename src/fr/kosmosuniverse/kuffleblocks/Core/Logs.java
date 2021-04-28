package fr.kosmosuniverse.kuffleblocks.Core;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Logs {
	public String path;
	
	public Logs(File dataFolder) {
		String s_path;
		Path p_path;
		
		if (dataFolder.getPath().contains("\\")) {
			s_path = dataFolder.getPath() + "\\logs_game.txt";
		} else {
			s_path = dataFolder.getPath() + "/logs_game.txt";
		}
		
		path = s_path;
		p_path = Paths.get(s_path);
		
		try {
			if (!Files.exists(p_path)) {
				Files.createFile(p_path);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void logBroadcastMsg(String msg) {		
		try {
			FileWriter writer = new FileWriter(path, true);
			
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");  
			LocalDateTime now = LocalDateTime.now();  
			
			writer.write(dtf.format(now) + " : [SYSTEM] -> " + msg);
			
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public void writeBroadcastMsg(String msg) {
		Bukkit.broadcastMessage(msg);
		
		try {
			FileWriter writer = new FileWriter(path, true);
			
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");  
			LocalDateTime now = LocalDateTime.now();  
			
			writer.write(dtf.format(now) + " : [SYSTEM] -> " + msg);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public void logMsg(Player to, String msg) {
		try {
			FileWriter writer = new FileWriter(path, true);
			
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");  
			LocalDateTime now = LocalDateTime.now();  
			
			writer.write(dtf.format(now) + " : [" + to.getDisplayName() + "] -> " + msg);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public void writeMsg(Player to, String msg) {
		to.sendMessage(msg);
		
		try {
			FileWriter writer = new FileWriter(path, true);
			
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");  
			LocalDateTime now = LocalDateTime.now();  
			
			writer.write(dtf.format(now) + " : [" + to.getDisplayName() + "] -> " + msg);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
}
