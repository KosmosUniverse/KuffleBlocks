package fr.kosmosuniverse.kuffle.Commands;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.kosmosuniverse.kuffle.KuffleMain;

public class KuffleConfig implements CommandExecutor {
	private KuffleMain km;
	
	public KuffleConfig(KuffleMain _km) {
		km = _km;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if (!(sender instanceof Player))
			return false;
		
		if (args.length % 2 == 1) {
			return false;
		}
		
		Player player = (Player) sender;
		
		if (!player.hasPermission("kconfig")) {
			player.sendMessage("You are not allowed to do this command.");
			return false;
		}
		
		if (args.length == 0) {
			player.sendMessage(km.config.displayConfig());
		} else {
			String before = "";
			
			for (int i = 0; i < args.length; i++) {
				if (i % 2 == 0) {
					if (before != "") {
						before = "";
					}
					
					before = args[i];
				} else {
					if (km.config.stringElems.containsKey(before)) {
						try {
							boolean ret = (boolean) Class.forName("fr.kosmosuniverse.kuffle.Core.Config").getMethod(km.config.stringElems.get(before), String.class).invoke(km.config, args[i]);
							
							if (ret) {
								sender.sendMessage("Config : parameter [" + before + "] set to [" + args[i] + "].");	
							} else {
								sender.sendMessage("Config : parameter [" + before + "] cannot be set to [" + args[i] + "] due to current game state.");
							}
						} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
								| NoSuchMethodException | SecurityException | ClassNotFoundException e) {
							e.printStackTrace();
						}
					} else if (km.config.booleanElems.containsKey(before)) {
						try {
							String tmp = args[i].toLowerCase();
							
							if (!tmp.equals("true") && !tmp.equals("false")) {
								player.sendMessage(km.config.booleanErrorMsg);
							}
							
							boolean boolValue = Boolean.parseBoolean(tmp);
							
							boolean ret = (boolean) Class.forName("fr.kosmosuniverse.kuffle.Core.Config").getMethod(km.config.booleanElems.get(before), boolean.class).invoke(km.config, boolValue);
							
							if (ret) {
								sender.sendMessage("Config : parameter [" + before + "] set to [" + args[i] + "].");	
							} else {
								sender.sendMessage("Config : parameter [" + before + "] cannot be set to [" + boolValue + "] due to current game state.");
							}
						} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
								| NoSuchMethodException | SecurityException | ClassNotFoundException e) {
							e.printStackTrace();
						}
					} else if (km.config.intElems.containsKey(before)) {
						try {
							int intValue = Integer.parseInt(args[i]);
							
							boolean ret = (boolean) Class.forName("fr.kosmosuniverse.kuffle.Core.Config").getMethod(km.config.intElems.get(before), int.class).invoke(km.config, intValue);

							if (ret) {
								sender.sendMessage("Config : parameter [" + before + "] set to [" + args[i] + "].");	
							} else {
								sender.sendMessage("Config : parameter [" + before + "] cannot be set to [" + intValue + "] due to current game state.");
							}
						} catch (NumberFormatException e) {
							player.sendMessage(km.config.intErrorMsg);
						} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
								| NoSuchMethodException | SecurityException | ClassNotFoundException e) {
							e.printStackTrace();
						} 
					} else {
						player.sendMessage("Key " + before + " not recognized.");
					}
				}
			}
		}
		
		return true;
	}
}
