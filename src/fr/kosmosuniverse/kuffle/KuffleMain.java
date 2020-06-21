package fr.kosmosuniverse.kuffle;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import fr.kosmosuniverse.kuffle.Commands.KuffleAdminLoad;
import fr.kosmosuniverse.kuffle.Commands.KuffleAdminSave;
import fr.kosmosuniverse.kuffle.Commands.KuffleAdminSkip;
import fr.kosmosuniverse.kuffle.Commands.KuffleAdminSpawn;
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

public class KuffleMain extends JavaPlugin {
	public HashMap<String, ArrayList<String>> allBlocks = ChooseBlockInList.getAllBlocks(this.getDataFolder());
	public HashMap<String, HashMap<String, RewardElem>> allRewards = RewardManager.getAllRewards(this.getDataFolder());
	public HashMap<String, PotionEffectType> effects = RewardManager.getAllEffects();
	public ArrayList<GameTask> games = new ArrayList<GameTask>();
	public ManageCrafts crafts = new ManageCrafts(this);
	public ManageMultiBlock multiBlock = new ManageMultiBlock();
	public Scores scores;
	public KuffleListTab listTab = new KuffleListTab();
	public KuffleAdminSkipTab skipTab = new KuffleAdminSkipTab();
	public KuffleValidateTab validateTab = new KuffleValidateTab();
	
	public boolean paused = false;
	
	@Override
	public void onEnable() {
		saveDefaultConfig();
		System.out.println("[Kuffle] : Plugin turned ON.");
		reloadConfig();
		
		if (getConfig().getInt("game_settings.block_per_age") < 1) {
			Bukkit.broadcastMessage("Config for block per age is not correct, use of default value.");
			getConfig().set("game_settings.block_per_age", 10);
		}
		if (getConfig().getInt("game_settings.spreadplayers.minimum_distance") < 1) {
			Bukkit.broadcastMessage("Config for spreadplayers minimum distance is not correct, use of default value.");
			getConfig().set("game_settings.spreadplayers.minimum_distance", 1000);
		}
		if (getConfig().getInt("game_settings.spreadplayers.maximum_area") < getConfig().getInt("game_settings.spreadplayers.minimum_distance")) {
			Bukkit.broadcastMessage("Config for spreadplayers maximum area is not correct, use of default value.");
			getConfig().set("game_settings.spreadplayers.maximum_area", 5000);
		}
		if (getConfig().getInt("game_settings.start_time") < 1) {
			Bukkit.broadcastMessage("Config for start time is not correct, use of default value.");
			getConfig().set("game_settings.start_time", 4);
		}
		if (getConfig().getInt("game_settings.time_added") < 1) {
			Bukkit.broadcastMessage("Config for time added is not correct, use of default value.");
			getConfig().set("game_settings.time_added", 2);
		}
		
		scores = new Scores(this);
		
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
		getCommand("kskip").setExecutor(new KuffleSkip(this));
		getCommand("kcrafts").setExecutor(new KuffleCrafts(this));
		getCommand("kmultiBlocks").setExecutor(new KuffleMultiBlocks(this));
		
		System.out.println("[Kuffle] Add Plugin Tab Completer.");
		
		listTab = new KuffleListTab();
		skipTab = new KuffleAdminSkipTab();
		validateTab = new KuffleValidateTab();
		
		getCommand("klist").setTabCompleter(listTab);
		getCommand("kadminskip").setTabCompleter(skipTab);
		getCommand("kvalidate").setTabCompleter(validateTab);
		getCommand("kadminspawn").setTabCompleter(new KuffleAdminSpawnTab(this));
		getCommand("kmultiblocks").setTabCompleter(new KuffleMultiBlocksTab(this));
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
}
