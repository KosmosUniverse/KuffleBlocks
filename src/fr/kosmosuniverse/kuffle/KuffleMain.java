package fr.kosmosuniverse.kuffle;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import fr.kosmosuniverse.kuffle.Commands.*;
import fr.kosmosuniverse.kuffle.Core.ChooseBlockInList;
import fr.kosmosuniverse.kuffle.Core.Config;
import fr.kosmosuniverse.kuffle.Core.GameTask;
import fr.kosmosuniverse.kuffle.Core.LangManager;
import fr.kosmosuniverse.kuffle.Core.RewardElem;
import fr.kosmosuniverse.kuffle.Core.RewardManager;
import fr.kosmosuniverse.kuffle.Core.Scores;
import fr.kosmosuniverse.kuffle.Crafts.ACrafts;
import fr.kosmosuniverse.kuffle.Crafts.ManageCrafts;
import fr.kosmosuniverse.kuffle.Listeners.*;
import fr.kosmosuniverse.kuffle.MultiBlock.ManageMultiBlock;
import fr.kosmosuniverse.kuffle.TabCmd.*;
import fr.kosmosuniverse.kuffle.utils.Utils;

public class KuffleMain extends JavaPlugin {
	public HashMap<String, HashMap<String, RewardElem>> allRewards;
	public HashMap<String, HashMap<String, String>> allLang;
	public HashMap<String, ArrayList<Inventory>> blocksInvs;
	public HashMap<String, ArrayList<String>> allBlocks;
	public HashMap<String, Boolean> playerRank = new HashMap<String, Boolean>();
	public HashMap<String, Location> backCmd;
	public HashMap<String, PotionEffectType> effects;
	public ArrayList<GameTask> games;
	public ArrayList<String> langs;
	public Config config;
	public ManageCrafts crafts;
	public ManageMultiBlock multiBlock;
	public Scores scores;
	public Inventory playersHeads;
	
	public boolean paused = false;
	
	@Override
	public void onEnable() {
		saveDefaultConfig();
		reloadConfig();
		
		try {
			InputStream in = getResource("blocks_" + Utils.getVersion() + ".json");
			String result = Utils.readFileContent(in);
			allBlocks = ChooseBlockInList.getAllBlocks(result, this.getDataFolder());
			
			in.close();
			
			in = getResource("rewards.json");
			result = Utils.readFileContent(in);
			allRewards = RewardManager.getAllRewards(result, this.getDataFolder());
			
			in.close();
			
			in = getResource("blocks_lang.json");
			result = Utils.readFileContent(in);
			allLang = LangManager.getAllBlocksLang(result, this.getDataFolder());
			
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		langs = LangManager.findAllLangs(allLang);
		blocksInvs = ChooseBlockInList.getBlocksInvs(allBlocks);
		effects = RewardManager.getAllEffects();
		games = new ArrayList<GameTask>();
		crafts = new ManageCrafts(this);
		multiBlock = new ManageMultiBlock();
		scores = new Scores(this);
		backCmd = new HashMap<>();
		
		config.setupConfig(this, getConfig());
		
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
		getCommand("kteleport").setExecutor(new KuffleTeleport(this));
		getCommand("klang").setExecutor(new KuffleLang(this));
		
		System.out.println("[Kuffle] Add Plugin Tab Completer.");
		getCommand("klist").setTabCompleter(new KuffleListTab(this));
		getCommand("kadminskip").setTabCompleter(new KuffleAdminSkipTab(this));
		getCommand("kvalidate").setTabCompleter(new KuffleValidateTab(this));
		getCommand("kadminspawn").setTabCompleter(new KuffleAdminSpawnTab(this));
		getCommand("kmultiblocks").setTabCompleter(new KuffleMultiBlocksTab(this));
		getCommand("klang").setTabCompleter(new KuffleLangTab(this));
		
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
		allLang.clear();
		
		System.out.println("[Kuffle] : Plugin turned OFF.");
	}
	
	
}
