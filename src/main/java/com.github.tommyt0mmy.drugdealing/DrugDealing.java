package com.github.tommyt0mmy.drugdealing;

import com.github.tommyt0mmy.drugdealing.commands.CreateNPC;
import com.github.tommyt0mmy.drugdealing.commands.GetPlant;
import com.github.tommyt0mmy.drugdealing.commands.Help;
import com.github.tommyt0mmy.drugdealing.events.PlantedDrug;
import com.github.tommyt0mmy.drugdealing.events.PreventSaplingGrowth;
import com.github.tommyt0mmy.drugdealing.events.RemoveUprootedPlants;
import com.github.tommyt0mmy.drugdealing.managers.PlantsRegister;
import com.github.tommyt0mmy.drugdealing.managers.PlantsUpdater;
import com.github.tommyt0mmy.drugdealing.tabcompleters.CreateNPCTabCompleter;
import com.github.tommyt0mmy.drugdealing.tabcompleters.getPlantTabCompleter;
import com.github.tommyt0mmy.drugdealing.utility.Configs;
import com.github.tommyt0mmy.drugdealing.utility.Messages;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.logging.Logger;

public class DrugDealing extends JavaPlugin {
	
	private static DrugDealing instance;
	
	public Logger console = getLogger();
	public static Economy economy = null;
	public File datafolder = null;
	public Messages messages = null;
	public Drugs drugs = null;
	public PlantsRegister plantsreg = null;
	public Configs configs = null;
	
	public static DrugDealing getInstance() {
		return instance;
	}
	
	@SuppressWarnings("static-access")
	private void setInstance(DrugDealing instance) {
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
