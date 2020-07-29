package com.github.tommyt0mmy.drugdealing;

import com.github.tommyt0mmy.drugdealing.commands.CreateNPC;
import com.github.tommyt0mmy.drugdealing.commands.GetDrug;
import com.github.tommyt0mmy.drugdealing.commands.GetPlant;
import com.github.tommyt0mmy.drugdealing.commands.Help;
import com.github.tommyt0mmy.drugdealing.commands.RemoveNPC;
import com.github.tommyt0mmy.drugdealing.events.ConsumedDrug;
import com.github.tommyt0mmy.drugdealing.events.NpcInteractions;
import com.github.tommyt0mmy.drugdealing.events.PlantedDrug;
import com.github.tommyt0mmy.drugdealing.events.PreventSaplingGrowth;
import com.github.tommyt0mmy.drugdealing.events.RemoveUprootedPlants;
import com.github.tommyt0mmy.drugdealing.managers.NpcRegister;
import com.github.tommyt0mmy.drugdealing.managers.PlantsRegister;
import com.github.tommyt0mmy.drugdealing.managers.PlantsUpdater;
import com.github.tommyt0mmy.drugdealing.tabcompleters.CreateNPCTabCompleter;
import com.github.tommyt0mmy.drugdealing.tabcompleters.getDrugTabCompleter;
import com.github.tommyt0mmy.drugdealing.tabcompleters.getPlantTabCompleter;
import com.github.tommyt0mmy.drugdealing.tabcompleters.HelpTabCompleter;
import com.github.tommyt0mmy.drugdealing.utility.Configs;
import com.github.tommyt0mmy.drugdealing.utility.Messages;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.*;
import java.util.logging.Logger;

public class DrugDealing extends JavaPlugin {
	
	private static DrugDealing instance;
	
	public Logger console = getLogger();
	public static Economy economy = null;
	public File datafolder = null;
	public Messages messages = null;
	public Drugs drugs = null;
	public PlantsRegister plantsreg = null;
	public NpcRegister npcsreg = null;
	public Configs configs = null;
	public List< UUID > toRemoveNPCS = new ArrayList<>();
	public String version = getDescription().getVersion();
	
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
		messages = new Messages();
		drugs = new Drugs();
		plantsreg = new PlantsRegister();
		configs = new Configs();
		npcsreg = new NpcRegister();
		
		console.info("Loaded successfully");
	}
	
	public void onDisable() {
		console.info("Unloading plugin...");
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
    		console.severe("Disabled due to no Vault economy found!");
    		getServer().getPluginManager().disablePlugin(this);
    	}
    }
	
	private void loadCommands() {
		getCommand("drugdealing").setExecutor(new Help());
		getCommand("drugdealing").setTabCompleter(new HelpTabCompleter());
		getCommand("getplant").setExecutor(new GetPlant(this));
		getCommand("getplant").setTabCompleter(new getPlantTabCompleter(this));
		getCommand("getdrug").setExecutor(new GetDrug(this));
		getCommand("getdrug").setTabCompleter(new getDrugTabCompleter(this));
		getCommand("setnpc").setExecutor(new CreateNPC());
		getCommand("setnpc").setTabCompleter(new CreateNPCTabCompleter());
		getCommand("removenpc").setExecutor(new RemoveNPC());
	}
	
	private void loadEvents() {
		getServer().getPluginManager().registerEvents(new PlantedDrug(this), this);
		getServer().getPluginManager().registerEvents(new RemoveUprootedPlants(this), this);
		getServer().getPluginManager().registerEvents(new PreventSaplingGrowth(this), this);
		getServer().getPluginManager().registerEvents(new NpcInteractions(), this);
		getServer().getPluginManager().registerEvents(new ConsumedDrug(), this);
	}
	
	@SuppressWarnings("unused")
	private void loadManagers() {
		BukkitTask task1 = new PlantsUpdater(this).runTaskTimer(this, 0, 10 * 20/*10 seconds*/);
	}
		
}
