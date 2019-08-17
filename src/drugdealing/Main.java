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
import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin {
	
	public Logger console = getLogger();
	public static Economy economy = null;
	public File datafolder = null;
	public Messages messages = null;
	public Drugs drugs = null;
	
	public void onEnable() {
        loadVault();
		loadCommands();
		loadEvents();
		loadManagers();
		datafolder = getDataFolder();
		messages = new Messages(this);
		drugs = new Drugs(this);
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
	}
	
	private void loadEvents() {
		getServer().getPluginManager().registerEvents(new PlantedDrug(this), this);
		getServer().getPluginManager().registerEvents(new RemoveUprootedPlants(this), this);
	}
	
	@SuppressWarnings("unused")
	private void loadManagers() {
		BukkitTask task1 = new PlantsUpdater(this).runTaskTimer(this, 0, 20 * 20/*20 seconds*/);
	}
	
	
	
}
