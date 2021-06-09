package fr.kosmosuniverse.kuffleblocks.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.kosmosuniverse.kuffleblocks.KuffleMain;
import fr.kosmosuniverse.kuffleblocks.MultiBlock.AMultiblock;
import fr.kosmosuniverse.kuffleblocks.utils.Utils;

public class KuffleSpawnMuliblock implements CommandExecutor {
	private KuffleMain km;

	public KuffleSpawnMuliblock(KuffleMain _km) {
		km = _km;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if (!(sender instanceof Player))
			return false;
		
		Player player = (Player) sender;
		
		km.logs.logMsg(player, Utils.getLangString(km, player.getName(), "CMD_PERF").replace("<#>", "<kb-spawn-multiblock>"));
		
		if (!player.hasPermission("kb-spawn-multiblock")) {
			km.logs.writeMsg(player, Utils.getLangString(km, player.getName(), "NOT_ALLOWED"));
			return false;
		}
		
		if (args.length != 1) {
			return false;
		}
		
		AMultiblock tmp = km.multiBlock.findMultiBlockByName(args[0]);
		
		tmp.getMultiblock().spawnMultiBlock(player);
		
		return true;
	}
}
