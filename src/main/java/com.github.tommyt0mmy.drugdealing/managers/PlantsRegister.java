//Handles the plants.yml file

package com.github.tommyt0mmy.drugdealing.managers;

import com.github.tommyt0mmy.drugdealing.DrugDealing;
import com.github.tommyt0mmy.drugdealing.utility.DrugType;
import com.github.tommyt0mmy.drugdealing.utility.Helper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class PlantsRegister
{
    private final DrugDealing instance = DrugDealing.getInstance();
    private Random randomGenerator;
    private File registerConfigFile; //todo remove variable
    private FileConfiguration registerConfig; //todo remove variable

    public PlantsRegister()
    {
        randomGenerator = new Random();
    }

    @Deprecated
    private void loadRegister()
    { //loading plants.yml
        registerConfigFile = new File(instance.dataFolder, "plants.yml");
        if (!registerConfigFile.exists())
        {
            registerConfigFile.getParentFile().mkdirs();
            instance.saveResource("plants.yml", false);
            instance.console.info("Created file plants.yml");
        }

        registerConfig = new YamlConfiguration();
        try
        {
            registerConfig.load(registerConfigFile);
        } catch (Exception e)
        {
            instance.console.severe("Couldn't load plants.yml file properly!");
        }
    }

    private int randomGrowthTime()
    {
        int minTime = instance.settings.getFileConfiguration().getInt("growthTimeMin");
        int maxTime = instance.settings.getFileConfiguration().getInt("growthTimeMax");
        int randomTime = randomGenerator.nextInt(maxTime) + minTime;
        return Helper.getCurrentTimeSeconds() + randomTime;
    }

    @Deprecated
    public void addPlantOld(Block plant, String type)
    {
        addPlant(plant.getLocation(), DrugType.valueOf(type.toUpperCase() + "_PLANT"));
        Location loc = plant.getLocation();
        String plantName = "plants.plant" + registerConfig.getInt("plantsNumber");
        int newPlantsNumber = registerConfig.getInt("plantsNumber") + 1;
        registerConfig.set(".plantsNumber", newPlantsNumber);
        registerConfig.set(plantName + ".type", type);
        registerConfig.set(plantName + ".x", loc.getBlockX());
        registerConfig.set(plantName + ".y", loc.getBlockY());
        registerConfig.set(plantName + ".z", loc.getBlockZ());
        registerConfig.set(plantName + ".world", loc.getWorld().getName());
        registerConfig.set(plantName + ".grown", false);
        registerConfig.set(plantName + ".growthTime", randomGrowthTime());

        //saving plants.yml
        try
        {
            registerConfig.save(registerConfigFile);
        } catch (IOException e) {instance.console.severe("Couldn't save plants.yml file properly!");}
    }

    public void addPlant(Location location, DrugType plantType)
    {
        try
        {
            DrugDealing.database.savePlant(plantType, randomGrowthTime(), location);
        } catch (SQLException e)
        {
            instance.console.severe("Couldn't store plant data to database");
            e.printStackTrace();
        }
    }

    @Deprecated
    public void removePlantOld(Location loc)
    {
        String plantName = getPlantPath(loc);
        if (!plantName.equals(""))
        {
            registerConfig.set(plantName, null);

            //saving plants.yml
            try
            {
                registerConfig.save(registerConfigFile);
            } catch (IOException e) {instance.console.severe("Couldn't save plants.yml file properly!");}
        }
    }

    public void removePlant(Location location)
    {
        try
        {
            int plantId = getPlantId(location);
            DrugDealing.database.removePlant(plantId);
        } catch (SQLException e)
        {
            instance.console.severe("Couldn't remove plant data from database");
            e.printStackTrace();
        }
    }

    @Deprecated
    public String getTypeOld(Location loc)
    {
        String type = "";
        String plantPath = getPlantPath(loc);
        if (!plantPath.equals(""))
        {
            type = (String) registerConfig.get(plantPath + ".type");
        }
        return type;
    }

    public DrugType getDrugType(@NotNull Location location)
    {
        try
        {
            int plantId = getPlantId(location);
            return DrugDealing.database.getPlantType(plantId);
        } catch (SQLException e)
        {
            instance.console.severe("Couldn't get plant type from database");
            e.printStackTrace();
            return null;
        }
    }

    @Deprecated
    public int getGrowthTimeOld(Location loc)
    {
        int growthTime;
        String plantPath = getPlantPath(loc);
        growthTime = registerConfig.getInt(plantPath + ".growthTime");
        return growthTime;
    }

    public int getGrowthTime(@NotNull Location location)
    {
        try
        {
            int plantId = getPlantId(location);
            return DrugDealing.database.getGrowthtime(plantId);
        } catch (SQLException e)
        {
            instance.console.severe("Couldn't get plant growth time from database");
            e.printStackTrace();
            return 0;
        }
    }

    @Deprecated
    public boolean isGrownOld(Block plantBlock)
    {
        boolean grown = false;
        Location loc = plantBlock.getLocation();
        if (isDrugPlantOld(loc))
        { //if the given block represents a plant base
            ConfigurationSection plantCS = getPlant(getPlantPath(loc));
            grown = plantCS.getBoolean("grown");
        }
        return grown;
    }

    public boolean isTimeGrown(@NotNull Location loc)
    {
        int growthTime = getGrowthTime(loc);
        int currentTime = Helper.getCurrentTimeSeconds();

        return growthTime - currentTime <= 0;
    }

    public boolean isPhysicallyGrown(@NotNull Location loc)
    {
        try
        {
            return DrugDealing.database.getPhysicallyGrown(getPlantId(loc));
        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        return false;
    }

    public void setPhysicallyGrown(@NotNull Location loc, boolean physicallyGrown)
    {
        try
        {
            DrugDealing.database.setPhysicallyGrown(getPlantId(loc), physicallyGrown);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Deprecated
    public boolean isDrugPlantOld(Location loc)
    {
        return !getPlantPath(loc).equals("");
    }

    public boolean isDrugPlant(Location loc)
    {
        try
        {
            return DrugDealing.database.findPlant(loc);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    @Deprecated
    private String getPlantPath(Location loc)
    { //given a location returns the path of the plant in plants.ynl file
        int plantsCount = registerConfig.getInt("plantsNumber");
        for (int i = 0; i < plantsCount; i++)
        {
            String currentPlantPath = "plants.plant" + i;
            if (registerConfig.get(currentPlantPath) == null)
            {
                continue;
            }
            ConfigurationSection plant = registerConfig.getConfigurationSection(currentPlantPath);
            int x = plant.getInt("x");
            int y = plant.getInt("y");
            int z = plant.getInt("z");
            World w = Bukkit.getWorld(plant.getString("world"));
            if (loc.getWorld().getName().equals(w.getName()) && x == loc.getBlockX() && y == loc.getBlockY() && z == loc.getBlockZ())
            {
                return currentPlantPath;
            }
        }
        return "";
    }

    @Deprecated
    public ConfigurationSection getPlant(String plantName)
    {
        return registerConfig.getConfigurationSection(plantName);
    }

    @Deprecated
    public ConfigurationSection getPlant(Location plantLocation)
    {
        return registerConfig.getConfigurationSection(getPlantPath(plantLocation));
    }

    @Deprecated
    public List<ConfigurationSection> getPlants()
    {
        List<ConfigurationSection> plants = new ArrayList<>();
        for (int i = 0; i < registerConfig.getInt("plantsNumber"); i++)
        {
            String currentPlant = "plants.plant" + i;
            if (registerConfig.get(currentPlant) != null)
            {
                plants.add(getPlant(currentPlant));
            }
        }

        return plants;
    }

    @Deprecated
    public Location getPlantLocationOld(ConfigurationSection plant)
    {
        World w = Bukkit.getWorld(Objects.requireNonNull(plant.getString("world")));
        int x = plant.getInt("x");
        int y = plant.getInt("y");
        int z = plant.getInt("z");
        return new Location(w, x, y, z);
    }

    public Location getPlantLocation(int plantId)
    {
        try
        {
            return DrugDealing.database.getPlantLocation(plantId);
        } catch (SQLException e)
        {
            instance.console.severe("Couldn't get plant location from database");
            e.printStackTrace();
        }
        return null;
    }

    public int getPlantId(@NotNull Location location)
    {
        try
        {
            return DrugDealing.database.getPlantId(location);
        } catch (SQLException e)
        {
            instance.console.severe("Couldn't get plant id from database");
            e.printStackTrace();
            return 0;
        }
    }
}
