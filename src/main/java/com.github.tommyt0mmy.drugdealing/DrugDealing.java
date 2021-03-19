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
import com.github.tommyt0mmy.drugdealing.tabcompleters.HelpTabCompleter;
import com.github.tommyt0mmy.drugdealing.tabcompleters.getDrugTabCompleter;
import com.github.tommyt0mmy.drugdealing.tabcompleters.getPlantTabCompleter;
import com.github.tommyt0mmy.drugdealing.utility.Language;
import com.github.tommyt0mmy.drugdealing.utility.Settings;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Logger;

public class DrugDealing extends JavaPlugin
{

    public static Economy economy = null;
    private static DrugDealing instance;
    public final Logger console = getLogger();
    @SuppressWarnings("FieldCanBeLocal")
    private final int spigotResourceId = 82163;
    private final String spigotResourceUrl = "https://www.spigotmc.org/resources/drugdealing.82163/";
    public File dataFolder = null;
    public Language language = null;
    public Drugs drugs = null;
    public PlantsRegister plantsRegister = null;
    public NpcRegister npcRegister = null;
    public Settings settings = null;
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
        language = new Language(this);
        drugs = new Drugs();
        plantsRegister = new PlantsRegister();
        settings = new Settings(this);
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

        //Checking for updates on dedicated thread
        Runnable updateCheckerRunnable = () ->
        {
            //checking for updates
            UpdateChecker updateChecker = new UpdateChecker();
            if (updateChecker.networkError())
            {
                console.warning("Couldn't connect properly to api.spigotmc.org");
            } else if (updateChecker.invalidResourceError())
            {
                console.severe("Invalid Resource error!");
                console.severe("Please, if this error keeps occurring, open an issue here: https://github.com/TommyT0mmY/DrugDealing-plugin/issues");
            } else if (updateChecker.needsUpdate())
            {
                console.info("An update for DrugDealing is available at:");
                console.info(spigotResourceUrl);
                console.info(String.format("Installed version: %s Latest version: %s", updateChecker.getCurrent_version(), updateChecker.getLatest_version()));
            } else
            {
                console.info("Up to date");
            }
        };
        Thread updateCheckerThread = new Thread(updateCheckerRunnable);
        updateCheckerThread.setName("DrugDealing Update Checker");
        updateCheckerThread.start();

        //Economy system check
        if (!setupEconomy())
        {
            console.severe("Invalid economy system, disabling plugin!");
            getServer().getPluginManager().disablePlugin(this);
        } else
        {
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
