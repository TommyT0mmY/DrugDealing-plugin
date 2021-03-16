//Handles configs.yml file

package com.github.tommyt0mmy.drugdealing.utility;

import com.github.tommyt0mmy.drugdealing.DrugDealing;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Configs
{

    private final DrugDealing plugin = DrugDealing.getInstance();

    public Configs()
    {
        loadConfigs();
    }

    @SuppressWarnings("FieldCanBeLocal")
    private File configsFile;
    private FileConfiguration configs;

    private void loadConfigs()
    { //loading plants.yml
        configsFile = new File(plugin.dataFolder, "configs.yml");
        if (!configsFile.exists())
        {
            configsFile.getParentFile().mkdirs();
            plugin.saveResource("configs.yml", false);
            plugin.console.info("Created file configs.yml");
        }

        configs = new YamlConfiguration();
        try
        {
            configs.load(configsFile);
        } catch (Exception e)
        {
            plugin.console.severe("Couldn't load configs.yml file properly!");
        }
    }

    public FileConfiguration getConfigs()
    {
        return configs;
    }

}
