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
import com.github.tommyt0mmy.drugdealing.events.onPlayerJoin;
import com.github.tommyt0mmy.drugdealing.managers.NpcRegister;
import com.github.tommyt0mmy.drugdealing.managers.PlantsRegister;
import com.github.tommyt0mmy.drugdealing.managers.PlantsUpdater;
import com.github.tommyt0mmy.drugdealing.managers.UpdateChecker;
import com.github.tommyt0mmy.drugdealing.tabcompleters.CreateNPCTabCompleter;
import com.github.tommyt0mmy.drugdealing.tabcompleters.getDrugTabCompleter;
import com.github.tommyt0mmy.drugdealing.tabcompleters.getPlantTabCompleter;
import com.github.tommyt0mmy.drugdealing.tabcompleters.HelpTabCompleter;
import com.github.tommyt0mmy.drugdealing.utility.Configs;
import com.github.tommyt0mmy.drugdealing.utility.Messages;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;
import java.util.logging.Logger;

public class DrugDealing extends JavaPlugin
{

    private static DrugDealing instance;

    @SuppressWarnings("FieldCanBeLocal")
    private final int spigotResourceId = 82163;
    private final String spigotResourceUrl = "https://www.spigotmc.org/resources/drugdealing.82163/";

    public final Logger console = getLogger();
    public static Economy economy = null;
    public File dataFolder = null;
    public Messages messages = null;
    public Drugs drugs = null;
    public PlantsRegister plantsRegister = null;
    public NpcRegister npcRegister = null;
    public Configs configs = null;
    public List<UUID> toRemoveNPCs = new ArrayList<>();
    public String version = getDescription().getVersion();

    public static DrugDealing getInstance()
    {
        return instance;
    }

    private void setInstance(DrugDealing instance)
    {
        DrugDealing.instance = instance;
    }

    public void onEnable()
    {
        setInstance(this);

        dataFolder = getDataFolder();
        messages = new Messages();
        drugs = new Drugs();
        plantsRegister = new PlantsRegister();
        configs = new Configs();
        npcRegister = new NpcRegister();

        Objects.requireNonNull(getCommand("drugdealing")).setExecutor(new Help());
        Objects.requireNonNull(getCommand("drugdealing")).setTabCompleter(new HelpTabCompleter());
        Objects.requireNonNull(getCommand("getplant")).setExecutor(new GetPlant());
        Objects.requireNonNull(getCommand("getplant")).setTabCompleter(new getPlantTabCompleter());
        Objects.requireNonNull(getCommand("getdrug")).setExecutor(new GetDrug());
        Objects.requireNonNull(getCommand("getdrug")).setTabCompleter(new getDrugTabCompleter());
        Objects.requireNonNull(getCommand("setnpc")).setExecutor(new CreateNPC());
        Objects.requireNonNull(getCommand("setnpc")).setTabCompleter(new CreateNPCTabCompleter());
        Objects.requireNonNull(getCommand("removenpc")).setExecutor(new RemoveNPC());

        getServer().getPluginManager().registerEvents(new PlantedDrug(), this);
        getServer().getPluginManager().registerEvents(new RemoveUprootedPlants(), this);
        getServer().getPluginManager().registerEvents(new PreventSaplingGrowth(), this);
        getServer().getPluginManager().registerEvents(new NpcInteractions(), this);
        getServer().getPluginManager().registerEvents(new ConsumedDrug(), this);
        getServer().getPluginManager().registerEvents(new onPlayerJoin(), this);

        new PlantsUpdater().runTaskTimer(this, 0, 10 * 20/*10 seconds*/);

        //checking for updates
        UpdateChecker updateChecker = new UpdateChecker();
        if (updateChecker.needsUpdate())
        {
            console.info("An update for DrugDealing is available at:");
            console.info(spigotResourceUrl);
            console.info(String.format("Installed version: %s Lastest version: %s", updateChecker.getCurrent_version(), updateChecker.getLatest_version()));
        }

        if (!setupEconomy())
        {
            console.severe("Invalid economy system, disabling plugin!");
            getServer().getPluginManager().disablePlugin(this);
        } else {
            console.info("Loaded successfully");
        }
    }

    public void onDisable()
    {
        console.info("Unloading plugin...");
    }

    private boolean setupEconomy()
    { //Vault
        if (getServer().getPluginManager().getPlugin("Vault") == null)
        {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null)
        {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }

    public int getSpigotResourceId()
    {
        return spigotResourceId;
    }

    public String getSpigotResourceUrl()
    {
        return spigotResourceUrl;
    }
}
