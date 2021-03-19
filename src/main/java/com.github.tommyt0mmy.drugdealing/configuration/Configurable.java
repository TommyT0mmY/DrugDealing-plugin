package com.github.tommyt0mmy.drugdealing.configuration;

import com.github.tommyt0mmy.drugdealing.DrugDealing;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public abstract class Configurable
{
    protected DrugDealing plugin;
    private String filename;
    private FileConfiguration configurationFC;
    private File configurationFile;

    public Configurable(DrugDealing plugin, String filename)
    {
        this.plugin = plugin;
        this.filename = filename;
        setup();
    }

    private void setup()
    {
        configurationFile = new File(plugin.getDataFolder(), filename);
        if (!configurationFile.exists())
        {
            plugin.getDataFolder().mkdirs();
            plugin.saveResource(filename, true);
            plugin.getLogger().info(String.format("Created %s", filename));
        }

        configurationFC = new YamlConfiguration();

        try
        {
            configurationFC.load(configurationFile);
        } catch (Exception e) {logError(e);}

        checkForNewParameters();
    }

    private void checkForNewParameters()
    {
        String prev_data = configurationFC.saveToString(); //saving old data to string
        FileConfiguration prevDataFC = new YamlConfiguration(); //old data FileConf.
        FileConfiguration defaultDataFC = new YamlConfiguration(); //default data

        //getting prev data and default data, and saving default data to file
        try
        {
            plugin.saveResource(filename, true); //loading default values into messages.yml
            defaultDataFC.load(configurationFile);
            configurationFC.load(configurationFile);
            prevDataFC.loadFromString(prev_data); //loading old data to prevDataFC

        } catch (Exception e) {logError(e);}

        //replacing default values with prev data, leaving new parameters with the default value
        for (String currKey : defaultDataFC.getKeys(true))
        {
            if (prevDataFC.contains(currKey))
            {
                //plugin.getLogger().info(String.format("replacing %s with %s", defaultDataFC.get(currKey), prevDataFC.get(currKey))); //DEBUG
                configurationFC.set(currKey, prevDataFC.get(currKey));
            } else
            {
                configurationFC.set(currKey, defaultDataFC.get(currKey));
            }
        }

        //saving final data to file
        try
        {
            configurationFC.save(configurationFile);
        } catch (Exception e) {logError(e);}
    }

    private void logError(Exception e)
    {
        plugin.getLogger().severe(String.format("Couldn't load %s properly!", filename));
        e.printStackTrace();
    }

    public FileConfiguration getFileConfiguration()
    {
        return configurationFC;
    }

    public void setFileConfiguration(FileConfiguration fileConfiguration)
    {
        this.configurationFC = fileConfiguration;
        try
        {
            configurationFC.save(configurationFile);
        } catch (IOException e) {e.printStackTrace();}
    }
}