package fr.kosmosuniverse.kuffle;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import fr.kosmosuniverse.kuffle.Commands.KuffleAdminLoad;
import fr.kosmosuniverse.kuffle.Commands.KuffleAdminSave;
import fr.kosmosuniverse.kuffle.Commands.KuffleAdminSkip;
import fr.kosmosuniverse.kuffle.Commands.KuffleAdminSpawn;
import fr.kosmosuniverse.kuffle.Commands.KuffleAgeBlocks;
import fr.kosmosuniverse.kuffle.Commands.KuffleBack;
import fr.kosmosuniverse.kuffle.Commands.KuffleCrafts;
import fr.kosmosuniverse.kuffle.Commands.KuffleList;
import fr.kosmosuniverse.kuffle.Commands.KuffleMultiBlocks;
import fr.kosmosuniverse.kuffle.Commands.KufflePause;
import fr.kosmosuniverse.kuffle.Commands.KuffleResume;
import fr.kosmosuniverse.kuffle.Commands.KuffleSkip;
import fr.kosmosuniverse.kuffle.Commands.KuffleStart;
import fr.kosmosuniverse.kuffle.Commands.KuffleStop;
import fr.kosmosuniverse.kuffle.Commands.KuffleValidate;
import fr.kosmosuniverse.kuffle.Core.ChooseBlockInList;
import fr.kosmosuniverse.kuffle.Core.GameTask;
import fr.kosmosuniverse.kuffle.Core.RewardElem;
import fr.kosmosuniverse.kuffle.Core.RewardManager;
import fr.kosmosuniverse.kuffle.Core.Scores;
import fr.kosmosuniverse.kuffle.Crafts.ACrafts;
import fr.kosmosuniverse.kuffle.Crafts.ManageCrafts;
import fr.kosmosuniverse.kuffle.Listeners.InventoryRecipeListener;
import fr.kosmosuniverse.kuffle.Listeners.PlayerEventListener;
import fr.kosmosuniverse.kuffle.Listeners.PlayerMove;
import fr.kosmosuniverse.kuffle.MultiBlock.ManageMultiBlock;
import fr.kosmosuniverse.kuffle.TabCmd.KuffleAdminSkipTab;
import fr.kosmosuniverse.kuffle.TabCmd.KuffleAdminSpawnTab;
import fr.kosmosuniverse.kuffle.TabCmd.KuffleListTab;
import fr.kosmosuniverse.kuffle.TabCmd.KuffleMultiBlocksTab;
import fr.kosmosuniverse.kuffle.TabCmd.KuffleValidateTab;
import fr.kosmosuniverse.kuffle.utils.Utils;

public class KuffleMain extends JavaPlugin {
	public HashMap<String, ArrayList<String>> allBlocks;
	public HashMap<String, ArrayList<Inventory>> blocksInvs;
	public HashMap<String, HashMap<String, RewardElem>> allRewards;
	public HashMap<String, PotionEffectType> effects;
	public ArrayList<GameTask> games;
	public ManageCrafts crafts;
	public ManageMultiBlock multiBlock;
	public Scores scores;
	public HashMap<String, Location> backCmd;
	
	public boolean paused = false;
	
	@Override
	public void onEnable() {
		saveDefaultConfig();
		reloadConfig();
		
		if (!getConfig().contains("game_settings.block_per_age") || getConfig().getInt("game_settings.block_per_age") < 1) {
			Bukkit.broadcastMessage("Config for block per age is not correct, use of default value.");
			getConfig().set("game_settings.block_per_age", 5);
		}
		if (!getConfig().contains("game_settings.spreadplayers.minimum_distance") || getConfig().getInt("game_settings.spreadplayers.minimum_distance") < 1) {
			Bukkit.broadcastMessage("Config for spreadplayers minimum distance is not correct, use of default value.");
			getConfig().set("game_settings.spreadplayers.minimum_distance", 100);
		}
		if (!getConfig().contains("game_settings.spreadplayers.maximum_distance") || getConfig().getInt("game_settings.spreadplayers.maximum_area") < getConfig().getInt("game_settings.spreadplayers.minimum_distance")) {
			Bukkit.broadcastMessage("Config for spreadplayers maximum area is not correct, use of default value.");
			getConfig().set("game_settings.spreadplayers.maximum_area", 500);
		}
		if (!getConfig().contains("game_settings.start_time") || getConfig().getInt("game_settings.start_time") < 1) {
			Bukkit.broadcastMessage("Config for start time is not correct, use of default value.");
			getConfig().set("game_settings.start_time", 4);
		}
		if (!getConfig().contains("game_settings.time_added") || getConfig().getInt("game_settings.time_added") < 1) {
			Bukkit.broadcastMessage("Config for time added is not correct, use of default value.");
			getConfig().set("game_settings.time_added", 2);
		}
		if (!getConfig().contains("game_settings.skip.enable")) {
			Bukkit.broadcastMessage("Config for enabling skip is not correct, use of default value.");
			getConfig().set("game_settings.skip.enable", true);
		}
		if (!getConfig().contains("game_settings.skip.age") || getConfig().getInt("game_settings.skip.age") < 1) {
			Bukkit.broadcastMessage("Config for min skip age is not correct, use of default value.");
			getConfig().set("game_settings.skip.age", 2);
		}
		if (!getConfig().contains("game_settings.custom_crafts")) {
			Bukkit.broadcastMessage("Config for enabling custom crafts is not correct, use of default value.");
			getConfig().set("game_settings.custom_crafts", true);
		}
		if (!getConfig().contains("game_settings.see_block_count")) {
			Bukkit.broadcastMessage("Config for enabling block count display is not correct, use of default value.");
			getConfig().set("game_settings.see_block_count", true);
		}
		
		try {
			InputStream in = getResource("blocks_" + getVersion() + ".json");
			String result = Utils.readJSONFile(in);
			allBlocks = ChooseBlockInList.getAllBlocks(result, this.getDataFolder());
			
			in.close();
			
			in = getResource("rewards.json");
			result = Utils.readJSONFile(in);
			allRewards = RewardManager.getAllRewards(result, this.getDataFolder());
			
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		blocksInvs = ChooseBlockInList.getBlocksInvs(allBlocks);
		effects = RewardManager.getAllEffects();
		games = new ArrayList<GameTask>();
		crafts = new ManageCrafts(this);
		multiBlock = new ManageMultiBlock();
		scores = new Scores(this);
		backCmd = new HashMap<>();
		
		System.out.println("[Kuffle] Add Custom Crafts.");
		for (ACrafts item : crafts.getRecipeList()) {
			getServer().addRecipe(item.getRecipe());
		}
		
		System.out.println("[Kuffle] Add Game Listeners.");
		getServer().getPluginManager().registerEvents(new PlayerMove(this), this);
		getServer().getPluginManager().registerEvents(new InventoryRecipeListener(this), this);
		getServer().getPluginManager().registerEvents(new PlayerEventListener(this, this.getDataFolder()), this);
		
		System.out.println("[Kuffle] Add Plugin Commands.");
		getCommand("klist").setExecutor(new KuffleList(this));
		getCommand("kstart").setExecutor(new KuffleStart(this));
		getCommand("kstop").setExecutor(new KuffleStop(this));
		getCommand("kpause").setExecutor(new KufflePause(this));
		getCommand("kresume").setExecutor(new KuffleResume(this));
		getCommand("kvalidate").setExecutor(new KuffleValidate(this));
		getCommand("kadminskip").setExecutor(new KuffleAdminSkip(this));
		getCommand("kadminspawn").setExecutor(new KuffleAdminSpawn(this));
		getCommand("kadminsave").setExecutor(new KuffleAdminSave(this, this.getDataFolder()));
		getCommand("kadminload").setExecutor(new KuffleAdminLoad(this, this.getDataFolder()));
		getCommand("kback").setExecutor(new KuffleBack(this));
		getCommand("kskip").setExecutor(new KuffleSkip(this));
		getCommand("kcrafts").setExecutor(new KuffleCrafts(this));
		getCommand("kmultiBlocks").setExecutor(new KuffleMultiBlocks(this));
		getCommand("kageblocks").setExecutor(new KuffleAgeBlocks(this));
		
		System.out.println("[Kuffle] Add Plugin Tab Completer.");
		getCommand("klist").setTabCompleter(new KuffleListTab(this));
		getCommand("kadminskip").setTabCompleter(new KuffleAdminSkipTab(this));
		getCommand("kvalidate").setTabCompleter(new KuffleValidateTab(this));
		getCommand("kadminspawn").setTabCompleter(new KuffleAdminSpawnTab(this));
		getCommand("kmultiblocks").setTabCompleter(new KuffleMultiBlocksTab(this));
		
		System.out.println("[Kuffle] Plugin turned ON.");
	}
	
	@Override
	public void onDisable() {
		if (games != null && games.size() != 0) {
			for (GameTask gt : games) {
				gt.exit();
				gt.kill();
			}
			games.clear();
		}
		
		allBlocks.clear();
		allRewards.clear();
		
		System.out.println("[Kuffle] : Plugin turned OFF.");
	}
	
	public static String getVersion() {
		String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
		
		version = version.split("v")[1];
		version = version.split("_")[0] + "." + version.split("_")[1];
		
		return version;
	}
}
