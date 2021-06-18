package fr.kosmosuniverse.kuffleblocks.Commands;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import fr.kosmosuniverse.kuffleblocks.KuffleMain;
import fr.kosmosuniverse.kuffleblocks.Core.AgeManager;
import fr.kosmosuniverse.kuffleblocks.utils.Utils;

public class KuffleAgeBlocks implements CommandExecutor  {
	private KuffleMain km;

	public KuffleAgeBlocks(KuffleMain _km) {
		km = _km;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cnd, String msg, String[] args) {
		if (!(sender instanceof Player))
			return false;
		
		Player player = (Player) sender;
		
		km.logs.logMsg(player, Utils.getLangString(km, player.getName(), "CMD_PERF").replace("<#>", "<kb-ageblocks>"));
		
		if (!player.hasPermission("kb-ageblocks")) {
			km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "NOT_ALLOWED"));
			return false;
		}
		
		if (args.length > 1) {
			return false;
		}
		
		if (km.gameStarted) {
			if (!km.games.containsKey(player.getName())) {
				km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "NOT_PLAYING"));
				return true;
			}
			
			String age;
			
			if (args.length == 0) {
				age = AgeManager.getAgeByNumber(km.ages, km.games.get(player.getName()).getAge()).name;	
			} else {
				age = args[0];
				
				if (!AgeManager.ageExists(km.ages, age)) {
					km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "AGE_NOT_EXISTS"));
					return false;
				}
			}
			
			ArrayList<Inventory> ageBlocks = km.blocksInvs.get(age);
			
			player.openInventory(ageBlocks.get(0));
		} else {
			km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "GAME_NOT_LAUNCHED"));			
		}

		return true;
	}
}
