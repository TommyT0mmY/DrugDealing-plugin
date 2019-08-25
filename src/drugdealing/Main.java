package drugdealing;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import drugdealing.commands.*;
import drugdealing.events.*;
import drugdealing.managers.*;
import drugdealing.tabcompleters.*;
import drugdealing.utility.*;
import drugdealing.npc.*;
import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin {
	
	private static Main instance;
	private NPCMain NPCMain;
	
	public Logger console = getLogger();
	public static Economy economy = null;
	public File datafolder = null;
	public Messages messages = null;
	public Drugs drugs = null;
	public PlantsRegister plantsreg = null;
	public Configs configs = null;
	public NPC npc = null;
	
	public static Main getInstance() {
		return instance;
	}
	
	@SuppressWarnings("static-access")
	private void setInstance(Main instance) {
		this.instance = instance;
	}
	
	public void onEnable() {
		setInstance(this);
        loadVault();
		loadCommands();
		loadEvents();
		loadManagers();
		
		datafolder = getDataFolder();
		messages = new Messages(this);
		drugs = new Drugs(this);
		plantsreg = new PlantsRegister(this);
		configs = new Configs(this);
		NPCMain = new NPCMain();
		npc = NPCMain.getNPC();
		
		
		console.info("Loaded successfully");
	}
	
	public void onDisable() {
		console.info("Unloaded successfully");
	}
	
    private boolean setupEconomy() { //Vault
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }
    
    private void loadVault() {
    	if (!setupEconomy()) {
    		console.severe("Disabled due to no Vault dependency found!");
    		getServer().getPluginManager().disablePlugin(this);
    		return;
    	}
    }
	
	private void loadCommands() {
		getCommand("drugdealing").setExecutor(new Help(this));
		getCommand("getplant").setExecutor(new GetPlant(this));
		getCommand("getplant").setTabCompleter(new getPlantTabCompleter(this));
		getCommand("setnpc").setExecutor(new CreateNPC());
		getCommand("setnpc").setTabCompleter(new CreateNPCTabCompleter());
	}
	
	private void loadEvents() {
		getServer().getPluginManager().registerEvents(new PlantedDrug(this), this);
		getServer().getPluginManager().registerEvents(new RemoveUprootedPlants(this), this);
		getServer().getPluginManager().registerEvents(new PreventSaplingGrowth(this), this);
	}
	
	@SuppressWarnings("unused")
	private void loadManagers() {
		BukkitTask task1 = new PlantsUpdater(this).runTaskTimer(this, 0, 10 * 20/*10 seconds*/);
	}
		
}
