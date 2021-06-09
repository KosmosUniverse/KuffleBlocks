package fr.kosmosuniverse.kuffleblocks;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import fr.kosmosuniverse.kuffleblocks.Commands.*;
import fr.kosmosuniverse.kuffleblocks.Core.Age;
import fr.kosmosuniverse.kuffleblocks.Core.AgeManager;
import fr.kosmosuniverse.kuffleblocks.Core.BlockManager;
import fr.kosmosuniverse.kuffleblocks.Core.Config;
import fr.kosmosuniverse.kuffleblocks.Core.LangManager;
import fr.kosmosuniverse.kuffleblocks.Core.Level;
import fr.kosmosuniverse.kuffleblocks.Core.LevelManager;
import fr.kosmosuniverse.kuffleblocks.Core.Logs;
import fr.kosmosuniverse.kuffleblocks.Core.CraftsManager;
import fr.kosmosuniverse.kuffleblocks.Core.Game;
import fr.kosmosuniverse.kuffleblocks.Core.GameLoop;
import fr.kosmosuniverse.kuffleblocks.Core.TeamsManager;
import fr.kosmosuniverse.kuffleblocks.Core.RewardElem;
import fr.kosmosuniverse.kuffleblocks.Core.RewardManager;
import fr.kosmosuniverse.kuffleblocks.Core.Scores;
import fr.kosmosuniverse.kuffleblocks.Crafts.ACrafts;
import fr.kosmosuniverse.kuffleblocks.Listeners.*;
import fr.kosmosuniverse.kuffleblocks.MultiBlock.ManageMultiBlock;
import fr.kosmosuniverse.kuffleblocks.TabCmd.*;
import fr.kosmosuniverse.kuffleblocks.utils.FilesConformity;
import fr.kosmosuniverse.kuffleblocks.utils.Utils;

public class KuffleMain extends JavaPlugin {
	public HashMap<String, HashMap<String, RewardElem>> allRewards;
	public HashMap<String, HashMap<String, String>> allBlocksLangs;
	public HashMap<String, HashMap<String, String>> allLangs;
	public HashMap<String, ArrayList<Inventory>> blocksInvs;
	public HashMap<String, ArrayList<String>> allBlocks;
	public HashMap<String, ArrayList<String>> allSbtts = new HashMap<String, ArrayList<String>>();

	public HashMap<String, Game> games = new HashMap<String, Game>();
	public HashMap<String, Integer> playerRank = new HashMap<String, Integer>();
	
	public ArrayList<String> langs;
	public ArrayList<Age> ages;
	public ArrayList<Level> levels;
	
	public GameLoop loop;
	public Config config;
	public CraftsManager crafts;
	public ManageMultiBlock multiBlock;
	public TeamsManager teams;
	public Scores scores;
	public Logs logs;
	public Inventory playersHeads;
	public PlayerEvents playerEvents;
	
	public boolean paused = false;
	public boolean loaded = false;
	public boolean gameStarted = false;
	
	@Override
	public void onEnable() {
		saveDefaultConfig();
		reloadConfig();
		
		if ((ages = AgeManager.getAges(FilesConformity.getContent(this, "ages.json"))) == null) {
			this.getPluginLoader().disablePlugin(this);
			return ;
		}
		
		if ((allBlocks = BlockManager.getAllBlocks(ages, FilesConformity.getContent(this, "blocks_" + Utils.getVersion() + ".json"), this.getDataFolder())) == null) {
			this.getPluginLoader().disablePlugin(this);
			return ;
		}
		
		if ((allSbtts = BlockManager.getAllBlocks(ages, FilesConformity.getContent(this, "sbtt_" + Utils.getVersion() + ".json"), this.getDataFolder())) == null) {
			this.getPluginLoader().disablePlugin(this);
			return ;
		}
		
		if ((allRewards = RewardManager.getAllRewards(ages, FilesConformity.getContent(this, "rewards_" + Utils.getVersion() + ".json"), this.getDataFolder())) == null) {
			this.getPluginLoader().disablePlugin(this);
			return ;
		}
		
		if ((allBlocksLangs = LangManager.getAllBlocksLang(FilesConformity.getContent(this, "blocks_lang.json"), this.getDataFolder())) == null) {
			this.getPluginLoader().disablePlugin(this);
			return ;
		}
		
		if ((allLangs = LangManager.getAllBlocksLang(FilesConformity.getContent(this, "langs.json"), this.getDataFolder())) == null) {
			this.getPluginLoader().disablePlugin(this);
			return ;
		}
		
		if ((levels = LevelManager.getLevels(FilesConformity.getContent(this, "levels.json"))) == null) {
			this.getPluginLoader().disablePlugin(this);
			return ;
		}
		
		logs = new Logs(this.getDataFolder());
		langs = LangManager.findAllLangs(allBlocksLangs);
		
		config = new Config(this);
		config.setupConfig(this, getConfig());
		
		crafts = new CraftsManager(this);
		blocksInvs = BlockManager.getBlocksInvs(allBlocks);
		scores = new Scores(this);
		
		config = new Config(this);
		config.setupConfig(this, getConfig());
		
		games = new HashMap<String, Game>();
		multiBlock = new ManageMultiBlock();
		teams = new TeamsManager();
		scores = new Scores(this);

		int cnt = 0;
		
		for (ACrafts item : crafts.getRecipeList()) {
			getServer().addRecipe(item.getRecipe());
			cnt++;
		}
		System.out.println("[KuffleBlocks] " + Utils.getLangString(this, null, "ADD_CRAFTS").replace("%i", "" + cnt));
		
		playerEvents = new PlayerEvents(this, this.getDataFolder());
		
		getServer().getPluginManager().registerEvents(new PlayerMove(this), this);
		getServer().getPluginManager().registerEvents(new InventoryListeners(this), this);
		getServer().getPluginManager().registerEvents(playerEvents, this);
		System.out.println("[KuffleBlocks] " + Utils.getLangString(this, null, "ADD_LISTENERS").replace("%i", "3"));
		
		getCommand("kb-config").setExecutor(new KuffleConfig(this));
		getCommand("kb-list").setExecutor(new KuffleList(this));
		getCommand("kb-save").setExecutor(new KuffleSave(this, this.getDataFolder()));
		getCommand("kb-load").setExecutor(new KuffleLoad(this, this.getDataFolder()));
		getCommand("kb-start").setExecutor(new KuffleStart(this));
		getCommand("kb-stop").setExecutor(new KuffleStop(this));
		getCommand("kb-pause").setExecutor(new KufflePause(this));
		getCommand("kb-resume").setExecutor(new KuffleResume(this));
		getCommand("kb-ageblocks").setExecutor(new KuffleAgeBlocks(this));
		getCommand("kb-crafts").setExecutor(new KuffleCrafts(this));
		getCommand("kb-lang").setExecutor(new KuffleLang(this));
		getCommand("kb-skip").setExecutor(new KuffleSkip(this));
		getCommand("kb-abandon").setExecutor(new KuffleAbandon(this));
		getCommand("kb-adminskip").setExecutor(new KuffleSkip(this));
		getCommand("kb-validate").setExecutor(new KuffleValidate(this));
		getCommand("kb-validate-age").setExecutor(new KuffleValidate(this));
		getCommand("kb-players").setExecutor(new KufflePlayers(this));
		getCommand("kb-multiblocks").setExecutor(new KuffleMultiBlocks(this));
		
		getCommand("kb-team-create").setExecutor(new KuffleTeamCreate(this));
		getCommand("kb-team-delete").setExecutor(new KuffleTeamDelete(this));
		getCommand("kb-team-color").setExecutor(new KuffleTeamColor(this));
		getCommand("kb-team-show").setExecutor(new KuffleTeamShow(this));
		getCommand("kb-team-affect-player").setExecutor(new KuffleTeamAffectPlayer(this));
		getCommand("kb-team-remove-player").setExecutor(new KuffleTeamRemovePlayer(this));
		getCommand("kb-team-reset-players").setExecutor(new KuffleTeamResetPlayers(this));
		getCommand("kb-team-random-player").setExecutor(new KuffleTeamRandomPlayer(this));
		System.out.println("[KuffleBlocks] " + Utils.getLangString(this, null, "ADD_CMD").replace("%i", "25"));

		getCommand("kb-config").setTabCompleter(new KuffleConfigTab(this));
		getCommand("kb-list").setTabCompleter(new KuffleListTab(this));
		getCommand("kb-lang").setTabCompleter(new KuffleLangTab(this));
		getCommand("kb-ageblocks").setTabCompleter(new KuffleAgeBlocksTab(this));
		getCommand("kb-validate").setTabCompleter(new KuffleValidateTab(this));
		getCommand("kb-validate-age").setTabCompleter(new KuffleValidateTab(this));
		
		getCommand("kb-team-create").setTabCompleter(new KuffleTeamCreateTab(this));
		getCommand("kb-team-delete").setTabCompleter(new KuffleTeamDeleteTab(this));
		getCommand("kb-team-color").setTabCompleter(new KuffleTeamColorTab(this));
		getCommand("kb-team-show").setTabCompleter(new KuffleTeamShowTab(this));
		getCommand("kb-team-affect-player").setTabCompleter(new KuffleTeamAffectPlayerTab(this));
		getCommand("kb-team-remove-player").setTabCompleter(new KuffleTeamRemovePlayerTab(this));
		getCommand("kb-team-reset-players").setTabCompleter(new KuffleTeamResetPlayersTab(this));
		System.out.println("[KuffleBlocks] " + Utils.getLangString(this, null, "ADD_TAB").replace("%i", "13"));
		
		loaded = true;
		
		System.out.println("[KuffleBlocks] Plugin turned ON.");
	}
	
	public void updatePlayersHead(String player, String currentBlock) {
		ItemMeta itM;
		
		for (ItemStack item : playersHeads) {
			if (item != null) {
				itM = item.getItemMeta();
				
				if (itM.getDisplayName().equals(player)) {
					ArrayList<String> lore = new ArrayList<String>();
					
					lore.add(currentBlock);
					
					itM.setLore(lore);
					item.setItemMeta(itM);
				}
			}
		}
	}
	
	@Override
	public void onDisable() {
		if (loaded) {
			killAll();
		}
		
		System.out.println("[KuffleBlocks] : Plugin turned OFF.");
	}
	
	public void addRecipe(Recipe recipe) {
		getServer().addRecipe(recipe);
	}
	
	public void removeRecipe(String name) {
		NamespacedKey n = new NamespacedKey(this, name);
		
		for (String playerName : games.keySet()) {
			games.get(playerName).getPlayer().undiscoverRecipe(n);
		}
		
		getServer().removeRecipe(n);
	}
	
	private void killAll() {
		if (allRewards != null) {
			for (String key : allRewards.keySet()) {
				allRewards.get(key).clear();
			}
			
			allRewards.clear();
		}
		
		if (allBlocks != null) {
			for (String key : allBlocks.keySet()) {
				allBlocks.get(key).clear();
			}
			
			allBlocks.clear();
		}

		
		if (allBlocksLangs != null) {
			for (String key : allBlocksLangs.keySet()) {
				allBlocksLangs.get(key).clear();
			}
			
			allBlocksLangs.clear();
		}
		
		if (blocksInvs != null) {
			for (String key : blocksInvs.keySet()) {
				blocksInvs.get(key).clear();
			}
			
			blocksInvs.clear();
		}
		
		if (playerRank != null) {
			playerRank.clear();
		}
		
		if (games != null) {
			games.clear();	
		}
		
		if (langs != null) { 
			langs.clear();
		}
		
		if (ages != null) {
			ages.clear();	
		}
		
		if (crafts != null) {
			for (ACrafts craft : crafts.getRecipeList()) {
				removeRecipe(craft.getName());
			}
			
			crafts.clear();
		}
		
		if (config != null) {
			config.clear();
		}
	}
}
