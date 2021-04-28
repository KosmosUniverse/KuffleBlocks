package fr.kosmosuniverse.kuffleblocks;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import fr.kosmosuniverse.kuffleblocks.Commands.*;
import fr.kosmosuniverse.kuffleblocks.Core.ChooseBlockInList;
import fr.kosmosuniverse.kuffleblocks.Core.Config;
import fr.kosmosuniverse.kuffleblocks.Core.GameTask;
import fr.kosmosuniverse.kuffleblocks.Core.LangManager;
import fr.kosmosuniverse.kuffleblocks.Core.Logs;
import fr.kosmosuniverse.kuffleblocks.Core.ManageTeams;
import fr.kosmosuniverse.kuffleblocks.Core.RewardElem;
import fr.kosmosuniverse.kuffleblocks.Core.RewardManager;
import fr.kosmosuniverse.kuffleblocks.Core.Scores;
import fr.kosmosuniverse.kuffleblocks.Crafts.ACrafts;
import fr.kosmosuniverse.kuffleblocks.Crafts.ManageCrafts;
import fr.kosmosuniverse.kuffleblocks.Listeners.*;
import fr.kosmosuniverse.kuffleblocks.MultiBlock.ManageMultiBlock;
import fr.kosmosuniverse.kuffleblocks.TabCmd.*;
import fr.kosmosuniverse.kuffleblocks.utils.Utils;

public class KuffleMain extends JavaPlugin {
	public HashMap<String, HashMap<String, RewardElem>> allRewards;
	public HashMap<String, HashMap<String, String>> allLang;
	public HashMap<String, ArrayList<Inventory>> blocksInvs;
	public HashMap<String, ArrayList<String>> allBlocks;
	public HashMap<String, Boolean> playerRank = new HashMap<String, Boolean>();
	public HashMap<String, PotionEffectType> effects;
	public ArrayList<GameTask> games;
	public ArrayList<String> langs;
	public Config config;
	public ManageCrafts crafts;
	public ManageMultiBlock multiBlock;
	public ManageTeams teams;
	public Scores scores;
	public Logs logs;
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
			
			in = getResource("rewards_" + Utils.getVersion() + ".json");
			result = Utils.readFileContent(in);
			allRewards = RewardManager.getAllRewards(result, this.getDataFolder());
			
			in.close();
			
			in = getResource("blocks_lang.json");
			result = Utils.readFileContent(in);
			allLang = LangManager.getAllBlocksLang(result, this.getDataFolder());
			
			in.close();
			
			logs = new Logs(this.getDataFolder());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		langs = LangManager.findAllLangs(allLang);
		blocksInvs = ChooseBlockInList.getBlocksInvs(allBlocks);
		effects = RewardManager.getAllEffects();
		
		config = new Config(this);
		config.setupConfig(this, getConfig());
		
		games = new ArrayList<GameTask>();
		crafts = new ManageCrafts(this);
		multiBlock = new ManageMultiBlock();
		teams = new ManageTeams();
		scores = new Scores(this);
	
		System.out.println("[KuffleBlocks] Add Custom Crafts.");
		for (ACrafts item : crafts.getRecipeList()) {
			getServer().addRecipe(item.getRecipe());
		}
		
		System.out.println("[KuffleBlocks] Add Game Listeners.");
		getServer().getPluginManager().registerEvents(new PlayerMove(this), this);
		getServer().getPluginManager().registerEvents(new InventoryRecipeListener(this), this);
		getServer().getPluginManager().registerEvents(new PlayerEventListener(this, this.getDataFolder()), this);
		
		System.out.println("[KuffleBlocks] Add Plugin Commands.");
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
		getCommand("kplayers").setExecutor(new KufflePlayers(this));
		getCommand("klang").setExecutor(new KuffleLang(this));
		getCommand("kconfig").setExecutor(new KuffleConfig(this));
		getCommand("ktest").setExecutor(new KuffleTest(this));
		
		getCommand("kteam-create").setExecutor(new KuffleTeamCreate(this));
		getCommand("kteam-delete").setExecutor(new KuffleTeamDelete(this));
		getCommand("kteam-color").setExecutor(new KuffleTeamColor(this));
		getCommand("kteam-show").setExecutor(new KuffleTeamShow(this));
		getCommand("kteam-affect-player").setExecutor(new KuffleTeamAffectPlayer(this));
		getCommand("kteam-remove-player").setExecutor(new KuffleTeamRemovePlayer(this));
		getCommand("kteam-reset-players").setExecutor(new KuffleTeamResetPlayers(this));
		getCommand("kteam-random-player").setExecutor(new KuffleTeamRandomPlayer(this));
		
		System.out.println("[KuffleBlocks] Add Plugin Tab Completer.");
		getCommand("klist").setTabCompleter(new KuffleListTab(this));
		getCommand("kadminskip").setTabCompleter(new KuffleAdminSkipTab(this));
		getCommand("kvalidate").setTabCompleter(new KuffleValidateTab(this));
		getCommand("kadminspawn").setTabCompleter(new KuffleAdminSpawnTab(this));
		getCommand("kmultiblocks").setTabCompleter(new KuffleMultiBlocksTab(this));
		getCommand("klang").setTabCompleter(new KuffleLangTab(this));
		getCommand("kconfig").setTabCompleter(new KuffleConfigTab(this));
		
		getCommand("kteam-create").setTabCompleter(new KuffleTeamCreateTab(this));
		getCommand("kteam-delete").setTabCompleter(new KuffleTeamDeleteTab(this));
		getCommand("kteam-color").setTabCompleter(new KuffleTeamColorTab(this));
		getCommand("kteam-show").setTabCompleter(new KuffleTeamShowTab(this));
		getCommand("kteam-affect-player").setTabCompleter(new KuffleTeamAffectPlayerTab(this));
		getCommand("kteam-remove-player").setTabCompleter(new KuffleTeamRemovePlayerTab(this));
		getCommand("kteam-reset-players").setTabCompleter(new KuffleTeamResetPlayersTab(this));
		
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
		
		System.out.println("[KuffleBlocks] : Plugin turned OFF.");
	}
	
	
}
